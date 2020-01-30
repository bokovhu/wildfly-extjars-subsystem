package me.bokov.extjars.deployment;

import org.jboss.as.controller.management.Capabilities;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.controller.services.path.PathManagerService;
import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.deployment.*;
import org.jboss.as.server.deployment.module.ModuleRootMarker;
import org.jboss.as.server.deployment.module.MountHandle;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.as.server.deployment.module.TempFileProviderService;
import org.jboss.logging.Logger;
import org.jboss.vfs.*;
import org.jboss.vfs.util.SuffixMatchFilter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

/**
 * An example deployment unit processor that does nothing. To add more deployment
 * processors copy this class, and add to the {@link AbstractDeploymentChainStep}
 * {@link me.bokov.extjars.SubsystemAdd#performBoottime(org.jboss.as.controller.OperationContext, org.jboss.dmr.ModelNode, org.jboss.dmr.ModelNode, org.jboss.as.controller.ServiceVerificationHandler, java.util.List)}
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class SubsystemDeploymentProcessor implements DeploymentUnitProcessor {

    Logger log = Logger.getLogger(SubsystemDeploymentProcessor.class);

    public static final Phase PHASE = Phase.STRUCTURE;
    public static final int PRIORITY = 0x0900 + 1;

    public static final VirtualFileFilter FILTER = new SuffixMatchFilter (".jar", VisitorAttributes.DEFAULT);

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit ();

        if (!deploymentUnit.getName ().endsWith ("war")) {
            return;
        }

        log.info("Processing " + deploymentUnit.getName () + " (" + deploymentUnit.getServiceName ().getCanonicalName () + ")");

        ExtJars.printList ();

        final Set <String> externalJarDirectories = ExtJars.get (deploymentUnit.getName ());

        if (externalJarDirectories != null && !externalJarDirectories.isEmpty ()) {

            final VirtualFile deploymentRoot = deploymentUnit.getAttachment (Attachments.DEPLOYMENT_ROOT).getRoot ();

            for (String jarDirectory : externalJarDirectories) {

                log.info ("Finding JARs in directory " + jarDirectory);

                final VirtualFile jarDirectoryFile = VFS.getChild (jarDirectory);

                if (jarDirectoryFile != null && jarDirectoryFile.exists ()) {

                    try {

                        for (VirtualFile jarFile : jarDirectoryFile.getChildren (FILTER)) {

                            log.info (
                                    "Adding " + jarFile.getPathName () + " to " + deploymentUnit.getName ()
                            );

                            VirtualFile mountPoint = deploymentRoot.getChild ("WEB-INF").getChild ("lib").getChild (jarFile.getName ());

                            Closeable closeable = VFS.mountZip (
                                    jarFile,
                                    mountPoint,
                                    TempFileProviderService.provider ()
                            );

                            final ResourceRoot jarResourceRoot = new ResourceRoot (
                                    jarFile.getName (),
                                    mountPoint,
                                    new MountHandle (closeable)
                            );
                            ModuleRootMarker.mark (jarResourceRoot, true);
                            deploymentUnit.addToAttachmentList (Attachments.RESOURCE_ROOTS, jarResourceRoot);

                            log.info ("Mounted " + jarFile.getPathName () + " @ " + mountPoint.getPathNameRelativeTo (deploymentRoot));

                        }

                    } catch (Exception ex) {
                        throw new RuntimeException (ex);
                    }

                } else {

                    log.info (jarDirectoryFile.getPathName () + " does not exist!");

                }

            }

        }

    }

    @Override
    public void undeploy(DeploymentUnit context) {
    }

}
