package com.covyne.bouncenet.core;

public interface IEnvironmentVariables {

    String Get(String name);

    String Get(String name, String defaultVal);

}
