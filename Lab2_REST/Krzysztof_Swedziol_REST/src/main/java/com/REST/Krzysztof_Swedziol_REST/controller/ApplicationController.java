package com.REST.Krzysztof_Swedziol_REST.controller;

import com.REST.Krzysztof_Swedziol_REST.model.FormRequestResponse;
import com.REST.Krzysztof_Swedziol_REST.model.FormsModel;
import com.REST.Krzysztof_Swedziol_REST.model.RequestHistory;
import com.REST.Krzysztof_Swedziol_REST.model.ResponseModel;
import com.REST.Krzysztof_Swedziol_REST.service.ExchangeRateService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
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
    public String history(Model model) throws IOException {
        if (requestHistory == null) {
            return "error";
        }else{
            model.addAttribute("history", requestHistory.getRequestHistory());
            return "history";
        }
    }

    @GetMapping("forms/{id}")
    public ResponseEntity<?> getForms(@PathVariable() int id) throws IOException {
        FormRequestResponse form = requestHistory.getFormById(id);
        if (form != null) {
            return ResponseEntity.ok(form);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("process-form")
    public String processFormHtml(
            @RequestParam String sourceCurrency,
            @RequestParam String targetCurrency,
            @RequestParam double amount,
            Model model) {

        List<String> availableCurrencies = List.of("USD", "EUR", "PLN", "GBP");

        if (!availableCurrencies.contains(sourceCurrency) || !availableCurrencies.contains(targetCurrency)) {
            return "error";
        }

        double exchangeRate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
        double result = exchangeRateService.calculateValue(sourceCurrency, targetCurrency, amount);

        FormsModel form = new FormsModel(sourceCurrency, targetCurrency, amount);
        ResponseModel responseResult = new ResponseModel(exchangeRate, result);
        FormRequestResponse requestResponse = new FormRequestResponse(form, responseResult);
        requestHistory.add(requestResponse);

        model.addAttribute("response", responseResult);

        return "result";
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

    @DeleteMapping("remove-form/{id}")
    public ResponseEntity<?> removeForm(@PathVariable int id) {
        if (requestHistory.getFormById(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            requestHistory.removeFormById(id);
            return ResponseEntity.ok("Form with id " + id + " removed successfully!");
        }
    }
}
