package me.bokov.extjars;

import java.util.Arrays;
import java.util.Collection;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PersistentResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;

/**
 * @author <a href="mailto:tcerar@redhat.com">Tomaz Cerar</a>
 */
public class SubsystemDefinition extends PersistentResourceDefinition {

    static final AttributeDefinition[] ATTRIBUTES = {  };

    static final SubsystemDefinition INSTANCE = new SubsystemDefinition();

    private SubsystemDefinition() {
        super(SubsystemExtension.SUBSYSTEM_PATH,
                SubsystemExtension.getResourceDescriptionResolver(null),
                SubsystemAdd.INSTANCE,
                SubsystemRemove.INSTANCE);
    }

    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
    }

    @Override
    public Collection<AttributeDefinition> getAttributes() {
        return Arrays.asList(ATTRIBUTES);
    }
}
