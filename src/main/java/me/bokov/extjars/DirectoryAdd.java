package me.bokov.extjars;

import me.bokov.extjars.deployment.ExtJars;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;

public class DirectoryAdd extends AbstractAddStepHandler {

    public static final DirectoryAdd INSTANCE = new DirectoryAdd ();

    private DirectoryAdd () {
        super (DirectoryDefinition.ATTRIBUTES);
    }

    @Override
    protected void performRuntime (
            OperationContext context, ModelNode operation, ModelNode model
    ) throws OperationFailedException {

        ModelNode locationParameter = DirectoryDefinition.LOCATION
                .resolveModelAttribute (context, model);
        PathAddress address = PathAddress.pathAddress (
                operation.get (ModelDescriptionConstants.ADDRESS)
        );
        String deployment = address.getElement (1).getValue ();
        String name = address.getElement (2).getValue ();

        if (locationParameter.isDefined ()) {
            ExtJars.addDirectory (
                    deployment,
                    name,
                    locationParameter.asString ()
            );
        }

    }

}
