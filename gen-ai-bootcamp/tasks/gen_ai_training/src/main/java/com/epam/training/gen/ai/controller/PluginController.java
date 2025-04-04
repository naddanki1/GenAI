package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.Light;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.contextvariables.ContextVariable;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kernel")
public class PluginController {
    private final Kernel kernel;

    /**
     * Get the list of available lights.
     */
    @GetMapping("/lights")
    public ResponseEntity<Map<String, Object>> getLights() {
        Map<String, Object> res = new HashMap<>();
        try {
            // Retrieve the LightPlugin from the kernel
            // Retrieve the KernelPlugin instance
            KernelPlugin lightPlugin = kernel.getPlugin("LightPlugin");

            // Get the function from the plugin
            KernelFunction<List<Light>> getLightsFunction = lightPlugin.get("get_lights");

            if (getLightsFunction == null) {
                throw new IllegalArgumentException("Function 'get_lights' not found in plugin.");
            }

            // Invoke function asynchronously
            List<Light> lights = getLightsFunction.invoke(kernel).getResult();

            res.put("status", "success");
            res.put("data", lights);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Failed to fetch lights");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping("/lights/change-state")
    public ResponseEntity<Map<String, Object>> changeState(@RequestBody Light light) {
        Map<String, Object> res = new HashMap<>();
        try {
            KernelPlugin lightPlugin = kernel.getPlugin("LightPlugin");

            // Get the function from the plugin
            KernelFunction<Light> changeStateFunction = lightPlugin.get("change_state");

            if (changeStateFunction == null) {
                throw new IllegalArgumentException("Function 'change_state' not found in plugin.");
            }

            // Convert Light object to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String lightJson = objectMapper.writeValueAsString(light);

            // Create KernelFunctionArguments
            KernelFunctionArguments args = KernelFunctionArguments.builder()
                    .withInput(ContextVariable.of(lightJson))
                    .build();

            Light updatedLight = changeStateFunction.invoke(kernel, args,null,null).getResult();

            res.put("status", "success");
            res.put("data", updatedLight);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Failed to update light state: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
    /**
     * Endpoint to calculate age
     */
    @GetMapping("/age-calculator/{birthYear}")
    public ResponseEntity<Map<String, Object>> calculateAge(@PathVariable String birthYear) {
        Map<String, Object> res = new HashMap<>();
        try {
            KernelFunction<String> ageFunction = kernel.getPlugin("AgeCalculatorPlugin").get("calculate_age");

            if (ageFunction == null) {
                throw new IllegalArgumentException("Function 'calculate_age' not found in plugin.");
            }

            KernelFunctionArguments args = KernelFunctionArguments.builder()
                    .withInput(birthYear)
                    .build();

            String age = ageFunction.invoke(kernel, args, null, null).getResult();

            res.put("status", "success");
            res.put("age", age);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    /**
     * Endpoint to convert currency
     */
    @GetMapping("/currency-converter")
    public ResponseEntity<Map<String, Object>> convertCurrency(
            @RequestParam double amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {

        Map<String, Object> res = new HashMap<>();
        try {
            KernelFunction<String> currencyFunction = kernel.getPlugin("CurrencyConverterPlugin").get("convert_currency");

            if (currencyFunction == null) {
                throw new IllegalArgumentException("Function 'convert_currency' not found in plugin.");
            }
            Map<String, ContextVariable<?>> arguments = new HashMap();
            arguments.put("amount", ContextVariable.of(String.valueOf(amount)));
            arguments.put("fromCurrency", ContextVariable.of(fromCurrency));
            arguments.put("toCurrency", ContextVariable.of(toCurrency));

            KernelFunctionArguments args = KernelFunctionArguments.builder()
                    .withVariables(arguments)
                    .build();

            String convertedAmount = currencyFunction.invoke(kernel, args, null, null).getResult();

            res.put("status", "success");
            res.put("convertedAmount", convertedAmount);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }
}
