package me.bokov.extjars;

import me.bokov.extjars.deployment.ExtJars;
import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;

public class DirectoryRemove extends AbstractRemoveStepHandler {

    public static final DirectoryRemove INSTANCE = new DirectoryRemove ();

    private DirectoryRemove () {

    }

    @Override
    protected void performRuntime (
            OperationContext context, ModelNode operation, ModelNode model
    ) throws OperationFailedException {

        PathAddress address = PathAddress.pathAddress (
                operation.get (ModelDescriptionConstants.ADDRESS)
        );

        String deployment = address.getElement (1).getValue ();
        String name = address.getElement (2).getValue ();

        ExtJars.removeDirectory (deployment, name);

    }

}
