package com.covyne.bouncenet;
import com.covyne.bouncenet.core.IEnvironmentVariables;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;


@TestConfiguration
public class TestEnvironmentVariables {

    private final Map<String, String> environmentVariablesMap = new HashMap<>() {{
        put("MONGODB_URI", "mongodb://127.0.0.1:27017");
    }};

    @Bean
    public IEnvironmentVariables GetEnvironmentVariables(){

        return new IEnvironmentVariables() {
            @Override
            public String Get(String name) {
               return environmentVariablesMap.get(name);
            }

            @Override
            public String Get(String name, String defaultVal) {
                return environmentVariablesMap.getOrDefault(name, defaultVal);
            }
        };
    }
}
