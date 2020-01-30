package me.bokov.extjars;

import org.jboss.as.controller.*;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.dmr.ModelType;

import java.util.Arrays;
import java.util.Collection;

public class DirectoryDefinition extends PersistentResourceDefinition {

    public static final SimpleAttributeDefinition LOCATION = new SimpleAttributeDefinitionBuilder (
            "location",
            ModelType.STRING
    )
            .setXmlName ("location")
            .setRequired (true)
            .setAllowExpression (true)
            .setFlags (AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .build ();

    public static final AttributeDefinition[] ATTRIBUTES = {LOCATION};

    public static final PathElement PATH = PathElement.pathElement ("directory");
    public static final String KEY_PREFIX = "deployment.directory";

    public static final DirectoryDefinition INSTANCE = new DirectoryDefinition ();

    private DirectoryDefinition () {
        super (
                PATH,
                SubsystemExtension.getResourceDescriptionResolver (KEY_PREFIX),
                DirectoryAdd.INSTANCE,
                DirectoryRemove.INSTANCE
        );
    }

    @Override
    public Collection<AttributeDefinition> getAttributes () {
        return Arrays.asList (ATTRIBUTES);
    }

}
