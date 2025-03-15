package com.REST.Krzysztof_Swedziol_REST.controller;

import com.REST.Krzysztof_Swedziol_REST.model.FormRequestResponse;
import com.REST.Krzysztof_Swedziol_REST.model.FormsModel;
import com.REST.Krzysztof_Swedziol_REST.model.RequestHistory;
import com.REST.Krzysztof_Swedziol_REST.model.ResponseModel;
import com.REST.Krzysztof_Swedziol_REST.service.ExchangeRateService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/")
public class ApplicationController {
    private final ExchangeRateService exchangeRateService;
    private RequestHistory requestHistory;

    @Autowired
    public ApplicationController(ExchangeRateService exchangeRateService, RequestHistory requestHistory) {
        this.exchangeRateService = exchangeRateService;
        this.requestHistory = requestHistory;
    }

    @GetMapping("")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/index.html");
    }

    @GetMapping("history")
    public ResponseEntity<?> history() throws IOException {
        if (requestHistory == null) {
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(requestHistory.getRequestHistory());
        }
    }

    @GetMapping("/forms/{id}")
    public ResponseEntity<?> getForms(@PathVariable() int id) throws IOException {
        FormRequestResponse form = requestHistory.getFormById(id);
        if (form != null) {
            return ResponseEntity.ok(form);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("process-form")
    public ResponseEntity<?> processForm(HttpServletResponse response, @RequestBody FormsModel form) throws IOException {
        List<String> availableCurrencies = List.of("USD", "EUR", "PLN", "GBP");
        String sourceCurrency = form.getSourceCurrency();
        String targetCurrency = form.getTargetCurrency();
        double value = form.getAmount();

        if(!availableCurrencies.contains(sourceCurrency) || !availableCurrencies.contains(targetCurrency)){
            return ResponseEntity.notFound().build();
        }
        double exchangeRate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
        double result = exchangeRateService.calculateValue(sourceCurrency, targetCurrency, value);

        ResponseModel responseResult = new ResponseModel(exchangeRate, result);

        FormRequestResponse requestResponse = new FormRequestResponse(form, responseResult);
        requestHistory.add(requestResponse);

        return ResponseEntity.ok(requestHistory);
    }

    @PutMapping("replace-form/{id}")
    public ResponseEntity<?> replaceForm(@PathVariable int id, @RequestBody FormsModel form) throws IOException {
        if(requestHistory.getFormById(id) == null){
            return ResponseEntity.notFound().build();
        }else{
            List<String> availableCurrencies = List.of("USD", "EUR", "PLN", "GBP");
            String sourceCurrency = form.getSourceCurrency();
            String targetCurrency = form.getTargetCurrency();
            double value = form.getAmount();

            if(!availableCurrencies.contains(sourceCurrency) || !availableCurrencies.contains(targetCurrency)){
                return ResponseEntity.notFound().build();
            }
            double exchangeRate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
            double result = exchangeRateService.calculateValue(sourceCurrency, targetCurrency, value);

            ResponseModel responseResult = new ResponseModel(exchangeRate, result);
            FormRequestResponse requestResponse = new FormRequestResponse(form, responseResult);

            requestHistory.replaceFormById(id, requestResponse);
            return ResponseEntity.ok(requestHistory);
        }
    }
}
