package com.covyne.bouncenet.core;

import org.springframework.stereotype.Service;

@Service
public class EnvironmentVariables implements IEnvironmentVariables {
    @Override
    public String Get(String name) {
        return System.getProperty(name) == null ? System.getenv(name) : null;
    }

    @Override
    public String Get(String name, String defaultVal) {
        return System.getProperty(name) == null ? System.getenv(name) : defaultVal;
    }
}
