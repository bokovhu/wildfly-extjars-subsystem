package me.bokov.extjars.deployment;

import java.io.Serializable;

public class ExtJarDirectoryDefinition implements Serializable {

    private final String name;
    private final String location;

    public ExtJarDirectoryDefinition (String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName () {
        return name;
    }

    public String getLocation () {
        return location;
    }

}
