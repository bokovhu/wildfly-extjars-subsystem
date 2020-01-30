package me.bokov.extjars;

import me.bokov.extjars.deployment.ExtJars;
import org.jboss.as.controller.*;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

public class DeploymentAdd extends AbstractAddStepHandler {

    public static final DeploymentAdd INSTANCE = new DeploymentAdd ();

    private DeploymentAdd () {
        super (DeploymentDefinition.ATTRIBUTES);
    }

    @Override
    protected void performRuntime (
            OperationContext context, ModelNode operation, ModelNode model
    ) throws OperationFailedException {

        ModelNode moduleParameter = DeploymentDefinition.MODULE
                .resolveModelAttribute (context, model);
        PathAddress address = PathAddress.pathAddress (
                operation.get (ModelDescriptionConstants.ADDRESS)
        );
        String name = address.getElement (1).getValue ();

        if (moduleParameter.isDefined ()) {
            ExtJars.add (name, moduleParameter.asString ());
        }

    }

}
