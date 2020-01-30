package me.bokov.extjars;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.*;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;


/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class SubsystemExtension implements Extension {

    public static final String NAMESPACE = "urn:me.bokov:extjars:1.0";
    public static final String SUBSYSTEM_NAME = "extjars";
    private static final String RESOURCE_NAME = SubsystemExtension.class.getPackage ()
            .getName () + ".LocalDescriptions";

    protected static final PathElement SUBSYSTEM_PATH = PathElement.pathElement (SUBSYSTEM, SUBSYSTEM_NAME);

    private final SubsystemParser parser = new SubsystemParser ();

    static StandardResourceDescriptionResolver getResourceDescriptionResolver (final String keyPrefix) {
        String prefix = SUBSYSTEM_NAME + (keyPrefix == null ? "" : "." + keyPrefix);
        return new StandardResourceDescriptionResolver (
                prefix,
                RESOURCE_NAME,
                SubsystemExtension.class.getClassLoader (),
                true,
                false
        );
    }

    @Override
    public void initializeParsers (ExtensionParsingContext context) {
        context.setSubsystemXmlMapping (SUBSYSTEM_NAME, NAMESPACE, parser);
    }


    @Override
    public void initialize (ExtensionContext context) {
        ModelVersion subsystemVersion = ModelVersion.create (1, 0);
        final SubsystemRegistration subsystem = context.registerSubsystem (SUBSYSTEM_NAME, subsystemVersion);
        final ManagementResourceRegistration registration = subsystem.registerSubsystemModel (SubsystemDefinition.INSTANCE);
        registration.registerOperationHandler (
                GenericSubsystemDescribeHandler.DEFINITION,
                GenericSubsystemDescribeHandler.INSTANCE
        );

        final ManagementResourceRegistration deploymentRegistration = registration.registerSubModel (DeploymentDefinition.INSTANCE);
        final ManagementResourceRegistration directoryRegistration = deploymentRegistration.registerSubModel (DirectoryDefinition.INSTANCE);

        subsystem.registerXMLElementWriter (parser);
    }

    /**
     * The subsystem parser, which uses stax to read and write to and from xml
     */
    private static class SubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>,
            XMLElementWriter<SubsystemMarshallingContext> {

        private final PersistentResourceXMLDescription xmlDescription;

        private SubsystemParser () {
            this.xmlDescription = PersistentResourceXMLDescription.builder (SUBSYSTEM_PATH)
                    // use .addChild(...) to add any children
                    .addChild (
                            PersistentResourceXMLDescription.builder(DeploymentDefinition.PATH)
                                    .addChild (
                                            PersistentResourceXMLDescription.builder(DirectoryDefinition.PATH)
                                                    .addAttribute (DirectoryDefinition.LOCATION)
                                                    .build()
                                    )
                                    .addAttribute (DeploymentDefinition.MODULE)
                                    .build()
                    )
                    .build ();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void writeContent (XMLExtendedStreamWriter writer, SubsystemMarshallingContext context)
                throws XMLStreamException {
            ModelNode model = new ModelNode ();
            model.get (SubsystemDefinition.INSTANCE.getPathElement ().getKeyValuePair ()).set (context.getModelNode ());
            xmlDescription.persist (writer, model, SubsystemExtension.NAMESPACE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void readElement (XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            xmlDescription.parse (reader, PathAddress.EMPTY_ADDRESS, list);
        }

    }

}
