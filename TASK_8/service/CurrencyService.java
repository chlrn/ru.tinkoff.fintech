package com.example.rutinkofffintech.TASK_8.service;

import com.example.rutinkofffintech.TASK_8.exception.CurrencyNotFoundException;
import com.example.rutinkofffintech.TASK_8.exception.CurrencyServiceUnavailableException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.xml.sax.InputSource;

import javax.naming.ServiceUnavailableException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final String CHAR_CODE = "CharCode";
    private static final String VALUE = "Value";
    private static final String NOMINAL = "Nominal";

    private final WebClient.Builder webClientBuilder;

    @Value("${currency-service.url}")
    private String currencyServiceUrl;

    private final XmlMapper xmlMapper = new XmlMapper();  // Jackson XML Mapper

    @Cacheable(value = "currencyRates", key = "#code", unless = "#result == null")
    @CircuitBreaker(name = "currencyService", fallbackMethod = "fallbackGetRate")
    public BigDecimal getCurrencyRate(String code) {
        String response = webClientBuilder.build()
                .get()
                .uri(currencyServiceUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Парсинг XML с использованием XPath
        return parseCurrencyRate(response, code);
    }

    private BigDecimal parseCurrencyRate(String xml, String code) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            InputSource inputSource = new InputSource(new StringReader(xml));

            String expression = String.format("//Valute[CharCode='%s']", code);
            var valuteNode = (org.w3c.dom.Element) xPath.evaluate(expression, inputSource, XPathConstants.NODE);

            if (valuteNode == null) {
                throw new CurrencyNotFoundException("Currency not found for code: " + code);
            }

            // Извлекаем курс валюты и номинал
            String valueStr = valuteNode.getElementsByTagName(VALUE).item(0).getTextContent().replace(",", ".");
            BigDecimal value = new BigDecimal(valueStr);

            String nominalStr = valuteNode.getElementsByTagName(NOMINAL).item(0).getTextContent();
            BigDecimal nominal = new BigDecimal(nominalStr);

            // Возвращаем реальный курс
            return value.divide(nominal, RoundingMode.HALF_UP);

        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);
        }
    }

    BigDecimal fallbackGetRate(String code, Throwable throwable) {
        throw new CurrencyServiceUnavailableException("Currency service is unavailable for currency code: " + code, throwable);
    }


    public BigDecimal convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        BigDecimal fromRate = getCurrencyRate(fromCurrency);
        BigDecimal toRate = getCurrencyRate(toCurrency);
        return amount.multiply(fromRate).divide(toRate, RoundingMode.HALF_UP);
    }
}
