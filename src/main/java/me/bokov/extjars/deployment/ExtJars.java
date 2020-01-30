package me.bokov.extjars.deployment;

import org.jboss.logging.Logger;

import java.util.*;

public final class ExtJars {

    private static final Map<String, ExtJarDefinition> definitions = Collections.synchronizedMap (new HashMap<> ());

    private static final Logger log = Logger.getLogger (ExtJars.class.getName ());

    public static void add (final String name, final String module) {
        log.info ("Adding " + name + ", module: " + module);
        synchronized (definitions) {
            definitions.put (
                    name,
                    new ExtJarDefinition (name, module, new HashMap<> ())
            );
        }
    }

    public static void clear () {
        log.info ("Clearing");
        synchronized (definitions) {
            definitions.clear ();
        }
    }

    public static void remove (final String name) {
        log.info ("Removing " + name);
        synchronized (definitions) {
            definitions.remove (name);
        }
    }

    public static void removeDirectory (final String deployment, final String dirName) {
        log.info ("Removing directory " + dirName + " from " + deployment);
        synchronized (deployment) {
            Optional.ofNullable (definitions.get (deployment))
                    .ifPresent (
                            def -> {
                                Map<String, ExtJarDirectoryDefinition> nextDirs = new HashMap<> (def.getDirectories ());
                                nextDirs.remove (deployment);
                                definitions.put (
                                        deployment,
                                        new ExtJarDefinition (
                                                def.getName (),
                                                def.getModule (),
                                                nextDirs
                                        )
                                );
                            }
                    );
        }
    }

    public static void addDirectory (final String name, final String directoryName, final String directoryLocation) {
        log.info ("Adding directory " + directoryName + " (" + directoryLocation + ")" + " to " + name);
        synchronized (definitions) {
            ExtJarDefinition current = definitions.get (name);
            if (current == null) {
                throw new IllegalArgumentException (name + " does not exist!");
            }
            Map<String, ExtJarDirectoryDefinition> nextDirs = new HashMap<> (current.getDirectories ());
            nextDirs.put (directoryName, new ExtJarDirectoryDefinition (directoryName, directoryLocation));
            definitions.put (
                    name,
                    new ExtJarDefinition (
                            current.getName (),
                            current.getModule (),
                            nextDirs
                    )
            );
        }
    }

    public static Set<String> get (final String module) {
        synchronized (definitions) {
            for (ExtJarDefinition def : definitions.values ()) {
                if (def.getModule ().equals (module)) {
                    return def.mapToDirectoryLocations ();
                }
            }
        }
        return Collections.emptySet ();
    }

    public static void printList () {
        synchronized (definitions) {
            for (Map.Entry <String, ExtJarDefinition> entry : definitions.entrySet ()) {
                ExtJarDefinition definition = entry.getValue ();
                log.info (entry.getKey () + " => " + definition.getName () + ", " + definition.getModule ());
                for (String dir : definition.mapToDirectoryLocations ()) {
                    log.info (definition.getName () + " => " + dir);
                }
            }
        }
    }

}
