package com.epam.training.gen.ai.plugin;

import com.epam.training.gen.ai.model.Light;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class LightPlugin {
    private final Map<Integer, Light> lights = new HashMap<>();

    public LightPlugin() {
        lights.put(1, new Light(1, "Table Lamp", false, Light.Brightness.MEDIUM, "#FFFFFF"));
        lights.put(2, new Light(2, "Porch light", false, Light.Brightness.HIGH, "#FF0000"));
        lights.put(3, new Light(3, "Chandelier", true, Light.Brightness.LOW, "#FFFF00"));
    }

    @DefineKernelFunction(name = "get_lights", description = "Gets a list of lights and their current state")
    public List<Light> getLights() {
        System.out.println("Getting lights");
        return new ArrayList<>(lights.values());
    }

    @DefineKernelFunction(name = "change_state", description = "Changes the state of the light")
    public Light changeState(
            @KernelFunctionParameter(
                    name = "model",
                    description = "The new state of the model to set",
                    type = String.class) String input
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Light model = objectMapper.readValue(input, Light.class);

        log.info("Changing light " + model.getId() + " " + model.getIsOn());

        if (!lights.containsKey(model.getId())) {
            throw new IllegalArgumentException("Light not found");
        }

        lights.put(model.getId(), model);
        return lights.get(model.getId());
    }
}
