package com.example.rutinkofffintech.TASK_8.service;

import com.example.rutinkofffintech.TASK_8.exception.CurrencyNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.naming.ServiceUnavailableException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final WebClient.Builder webClientBuilder;

    @Value("${currency-service.url}")
    private String currencyServiceUrl;

    @Cacheable(value = "currencyRates", key = "#code", unless = "#result == null")
    @CircuitBreaker(name = "currencyService", fallbackMethod = "fallbackGetRate")
    public Double getCurrencyRate(String code) {
        String response = webClientBuilder.build()
                .get()
                .uri(currencyServiceUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Парсинг XML-ответа от API ЦБ
        return parseCurrencyRate(response, code);
    }

    private Double parseCurrencyRate(String xml, String code) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            NodeList nodeList = document.getElementsByTagName("Valute");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element valuteElement = (Element) nodeList.item(i);

                // Извлечение кода валюты
                String charCode = valuteElement.getElementsByTagName("CharCode").item(0).getTextContent();

                if (charCode.equalsIgnoreCase(code)) {
                    // Извлечение курса валюты
                    String valueStr = valuteElement.getElementsByTagName("Value").item(0).getTextContent();
                    valueStr = valueStr.replace(",", "."); // Меняем запятую на точку

                    double value = Double.parseDouble(valueStr);

                    // Извлечение номинала (сколько единиц валюты)
                    String nominalStr = valuteElement.getElementsByTagName("Nominal").item(0).getTextContent();
                    int nominal = Integer.parseInt(nominalStr);

                    // Рассчитываем реальный курс валюты, делим на номинал
                    return value / nominal;
                }
            }

            // Если не нашли валюту с данным кодом, выбрасываем исключение
            throw new CurrencyNotFoundException("Currency not found for code: " + code);

        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);
        }
    }

    private Double fallbackGetRate(String code, Throwable throwable) throws ServiceUnavailableException {
        throw new ServiceUnavailableException("Currency service is unavailable");
    }

    public double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        double fromRate = getCurrencyRate(fromCurrency);
        double toRate = getCurrencyRate(toCurrency);
        return (amount * fromRate) / toRate;
    }
}
