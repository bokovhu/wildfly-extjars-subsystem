package me.bokov.extjars;

import org.jboss.as.controller.*;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.dmr.ModelType;

import java.util.Arrays;
import java.util.Collection;

public class DeploymentDefinition extends PersistentResourceDefinition {

    public static final SimpleAttributeDefinition MODULE = new SimpleAttributeDefinitionBuilder ("module", ModelType.STRING)
            .setXmlName ("module")
            .setRequired (true)
            .setAllowExpression (false)
            .setFlags (AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .build ();

    public static final AttributeDefinition [] ATTRIBUTES = { MODULE };

    public static final PathElement PATH = PathElement.pathElement ("deployment");
    public static final String KEY_PREFIX = "deployment";

    public static final DeploymentDefinition INSTANCE = new DeploymentDefinition ();

    private DeploymentDefinition () {
        super (
                PATH,
                SubsystemExtension.getResourceDescriptionResolver (KEY_PREFIX),
                DeploymentAdd.INSTANCE,
                DeploymentRemove.INSTANCE
        );
    }

    @Override
    public Collection<AttributeDefinition> getAttributes () {
        return Arrays.asList (ATTRIBUTES);
    }

}
