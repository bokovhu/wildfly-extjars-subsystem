package me.bokov.extjars;

import me.bokov.extjars.deployment.ExtJars;
import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;

public class DeploymentRemove extends AbstractRemoveStepHandler {

    public static final DeploymentRemove INSTANCE = new DeploymentRemove ();

    private DeploymentRemove () {

    }

    @Override
    protected void performRuntime (
            OperationContext context, ModelNode operation, ModelNode model
    ) throws OperationFailedException {

        PathAddress address = PathAddress.pathAddress (
                operation.get (ModelDescriptionConstants.ADDRESS)
        );
        String name = address.getElement (1).getValue ();
        ExtJars.remove (name);

    }

}
