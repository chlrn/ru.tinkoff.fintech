package com.example.rutinkofffintech.TASK_8.service;

import com.example.rutinkofffintech.TASK_8.exception.CurrencyNotFoundException;
import com.example.rutinkofffintech.TASK_8.exception.CurrencyServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class CurrencyServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CurrencyService currencyService;

    @Value("${currency-service.url}")
    private String currencyServiceUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void testGetCurrencyRateSuccess() {
        String xmlResponse = "<ValCurs><Valute><CharCode>USD</CharCode><Value>75,1234</Value><Nominal>1</Nominal></Valute></ValCurs>";

        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(xmlResponse));

        BigDecimal rate = currencyService.getCurrencyRate("USD");

        assertNotNull(rate);
        assertEquals(new BigDecimal("75.1234"), rate);
    }

    @Test
    public void testGetCurrencyRateCurrencyNotFound() {
        // Пример XML без валюты EUR
        String xmlResponse = "<ValCurs><Valute><CharCode>USD</CharCode><Value>75,1234</Value><Nominal>1</Nominal></Valute></ValCurs>";

        // Заглушка для ответа
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(xmlResponse));

        // Проверка на выбрасывание исключения
        assertThrows(CurrencyNotFoundException.class, () -> {
            currencyService.getCurrencyRate("EUR");
        });
    }

    @Test
    public void testConvertCurrencySuccess() {
        // Пример XML ответа от API для USD и EUR
        String xmlUsdResponse = "<ValCurs><Valute><CharCode>USD</CharCode><Value>75,1234</Value><Nominal>1</Nominal></Valute></ValCurs>";
        String xmlEurResponse = "<ValCurs><Valute><CharCode>EUR</CharCode><Value>85,6789</Value><Nominal>1</Nominal></Valute></ValCurs>";

        // Заглушки для ответов
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(xmlUsdResponse), Mono.just(xmlEurResponse));

        // Вызов метода
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal convertedAmount = currencyService.convertCurrency("USD", "EUR", amount);

        assertNotNull(convertedAmount);
        // Проверка результата (для примера, результат округлен до двух знаков)
        BigDecimal expectedAmount = amount.multiply(new BigDecimal("75.1234"))
                .divide(new BigDecimal("85.6789"), RoundingMode.HALF_UP);
        assertEquals(expectedAmount, convertedAmount);
    }

    @Test
    public void testFallbackGetRate() {
        // Проверка, что при вызове fallback метод выбрасывается нужное исключение
        assertThrows(CurrencyServiceUnavailableException.class, () -> {
            currencyService.fallbackGetRate("USD", new RuntimeException("Service failure"));
        });
    }
}
