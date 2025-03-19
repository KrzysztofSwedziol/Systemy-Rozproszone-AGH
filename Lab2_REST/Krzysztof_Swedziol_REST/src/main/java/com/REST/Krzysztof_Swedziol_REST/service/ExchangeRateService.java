package com.REST.Krzysztof_Swedziol_REST.service;

import com.REST.Krzysztof_Swedziol_REST.model.ExchangeRateHostResponse;
import com.REST.Krzysztof_Swedziol_REST.model.ExchangerateApiResponse;
import com.REST.Krzysztof_Swedziol_REST.model.RequestHistory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {
    private final RestTemplate restTemplate = new RestTemplate();

    public double calculateValue(String currency1, String currency2, double value) {
        double rate = getExchangeRate(currency1, currency2);
        return value * rate;
    }

    public double getExchangeRate(String currency1, String currency2) {
        double rate1 = rateAPI1(currency1, currency2);
        double rate2 = rateAPI2(currency1, currency2);
        return (rate1 + rate2) / 2.0;
    }

    public double rateAPI1(String currency1, String currency2) {
        String url = String.format(
                "https://api.exchangerate.host/latest?base=%s&symbols=%s",
                currency1, currency2
        );
        ExchangeRateHostResponse response = restTemplate.getForObject(url, ExchangeRateHostResponse.class);

        if (response != null && response.getRates() != null) {
            Double rate = response.getRates().get(currency2);
            if (rate != null) {
                return rate;
            }
        }
        return 0.0;
    }

    public double rateAPI2(String currency1, String currency2) {
        String url = String.format(
                "https://api.exchangerate-api.com/v4/latest/%s",
                currency1
        );
        ExchangerateApiResponse response = restTemplate.getForObject(url, ExchangerateApiResponse.class);

        if (response != null && response.getRates() != null) {
            Double rate = response.getRates().get(currency2);
            if (rate != null) {
                return rate;
            }
        }
        return 0.0;
    }
}
