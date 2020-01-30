package me.bokov.extjars;

import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;

import java.io.IOException;

/**
 * This is the bare bones test example that tests subsystem
 * It does same things that {@link SubsystemParsingTestCase} does but most of internals are already done in AbstractSubsystemBaseTest
 * If you need more control over what happens in tests look at  {@link SubsystemParsingTestCase}
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a>
 */
public class SubsystemBaseParsingTestCase extends AbstractSubsystemBaseTest {

    public SubsystemBaseParsingTestCase() {
        super(SubsystemExtension.SUBSYSTEM_NAME, new SubsystemExtension());
    }


    @Override
    protected String getSubsystemXml() throws IOException {
        return "<subsystem xmlns=\"" + SubsystemExtension.NAMESPACE + "\">" +
                "<deployment name=\"test.war\" module=\"deployment.test.war\">" +
                "<directory name=\"plugins-directory\" location=\"../plugins\" />" +
                "<directory name=\"other-plugins-directory\" location=\"~/wildfly-plugins\" />" +
                "<directory name=\"main-plugins-directory\" location=\"${jboss.server.home.dir}/my-plugins-dir\" />" +
                "</deployment>" +
                "</subsystem>";
    }

}
