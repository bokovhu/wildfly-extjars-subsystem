package me.bokov.extjars.deployment;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtJarDefinition implements Serializable {

    private final String name;
    private final String module;
    private final Map <String, ExtJarDirectoryDefinition> directories;

    public ExtJarDefinition (String name, String module, Map<String, ExtJarDirectoryDefinition> directories) {
        this.name = name;
        this.module = module;
        this.directories = Collections.unmodifiableMap (directories);
    }

    public String getName () {
        return name;
    }

    public String getModule () {
        return module;
    }

    public Map<String, ExtJarDirectoryDefinition> getDirectories () {
        return directories;
    }

    public Set <String> mapToDirectoryLocations () {
        return directories.values ()
                .stream ()
                .map (ExtJarDirectoryDefinition::getLocation)
                .collect(Collectors.toSet());
    }

}
