package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterPlugin {

    private final Map<String, Double> exchangeRates;

    public CurrencyConverterPlugin() {
        // Mocked exchange rates
        exchangeRates = new HashMap<>();
        exchangeRates.put("USD_TO_EUR", 0.92);
        exchangeRates.put("EUR_TO_USD", 1.08);
        exchangeRates.put("USD_TO_INR", 83.25);
        exchangeRates.put("INR_TO_USD", 0.012);
    }

    @DefineKernelFunction(name = "convert_currency", description = "Converts an amount from one currency to another.")
    public String convertCurrency(
            @KernelFunctionParameter(
                    name = "amount",
                    description = "Amount to convert",
                    type = String.class) String amount,

            @KernelFunctionParameter(
                    name = "fromCurrency",
                    description = "Currency to convert from (e.g., USD, EUR, INR)",
                    type = String.class) String fromCurrency,

            @KernelFunctionParameter(
                    name = "toCurrency",
                    description = "Currency to convert to (e.g., USD, EUR, INR)",
                    type = String.class) String toCurrency) {

        String key = fromCurrency.toUpperCase() + "_TO_" + toCurrency.toUpperCase();
        Double rate = exchangeRates.get(key);

        if (rate == null) {
            throw new IllegalArgumentException("Unsupported currency conversion: " + fromCurrency + " to " + toCurrency);
        }

        Double convertedAmount = Double.valueOf(amount) * rate;
        System.out.println("Converting " + amount + " " + fromCurrency + " to " + convertedAmount + " " + toCurrency);
        return convertedAmount.toString();
    }
}
