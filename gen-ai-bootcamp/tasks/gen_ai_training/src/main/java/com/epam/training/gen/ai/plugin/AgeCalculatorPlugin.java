package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.time.Year;

public class AgeCalculatorPlugin {

    @DefineKernelFunction(name = "calculate_age", description = "Calculates age based on the birth year.")
    public String calculateAge(
            @KernelFunctionParameter(
                    name = "birthYear",
                    description = "The year of birth",
                    type = String.class) String birthYear) {

        int currentYear = Year.now().getValue();
        int age = currentYear - Integer.valueOf(birthYear);
        System.out.println("Calculating age for birth year: " + birthYear + " -> Age: " + age);

        if (age < 0) {
            throw new IllegalArgumentException("Birth year cannot be in the future.");
        }

        return String.valueOf(age);
    }
}
