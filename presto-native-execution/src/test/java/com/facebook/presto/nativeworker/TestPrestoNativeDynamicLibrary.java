/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.nativeworker;

// import org.testcontainers.containers.BindMode;
// import org.testcontainers.containers.Container;
// import org.testcontainers.containers.GenericContainer;
// import org.testcontainers.containers.Network;
// import org.testcontainers.containers.wait.strategy.Wait;
// import org.testng.annotations.AfterClass;
// import org.testng.annotations.BeforeClass;

import com.facebook.presto.tests.AbstractTestQueryFramework;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

// import java.time.Duration;
// import static org.testng.Assert.fail;

@Test(singleThreaded = true)
public class TestPrestoNativeDynamicLibrary
        extends AbstractTestQueryFramework
{
    @Override
    protected ContainerQueryRunner createQueryRunner()
            throws Exception
    {
        return new ContainerQueryRunner();
    }
// {
    // private static final String PRESTO_COORDINATOR_IMAGE = System.getProperty("coordinatorImage", "presto-coordinator:latest");
    // private static final String PRESTO_WORKER_IMAGE = System.getProperty("workerImage", "presto-worker:latest");
    // private static final String BASE_DIR = System.getProperty("user.dir");
    // private static final String CONTAINER_TIMEOUT = System.getProperty("containerTimeout", "120");
    // private static final Network network = Network.newNetwork();
    // private GenericContainer<?> coordinator;
    // private GenericContainer<?> worker;
    // @BeforeClass
    // public void setup()
    //         throws InterruptedException
    // {
    //     // TODO: This framework is tested only in Ubuntu x86_64, as there is no support to run the native docker images in ARM based system,
    //     // Once this is fixed, the container details can be added as properties in VM options for testing in IntelliJ.
    //     coordinator = new GenericContainer<>(PRESTO_COORDINATOR_IMAGE)
    //             .withExposedPorts(8081)
    //             .withNetwork(network).withNetworkAliases("presto-coordinator")
    //             .withFileSystemBind(BASE_DIR + "/testcontainers/coordinator/etc", "/opt/presto-server/etc", BindMode.READ_WRITE)
    //             .withFileSystemBind(BASE_DIR + "/testcontainers/coordinator/entrypoint.sh", "/opt/entrypoint.sh", BindMode.READ_ONLY)
    //             .waitingFor(Wait.forLogMessage(".*======== SERVER STARTED ========.*", 1))
    //             .withStartupTimeout(Duration.ofSeconds(Long.parseLong(CONTAINER_TIMEOUT)));

    //     worker = new GenericContainer<>(PRESTO_WORKER_IMAGE)
    //             .withExposedPorts(7777)
    //             .withNetwork(network).withNetworkAliases("presto-worker")
    //             .withFileSystemBind(BASE_DIR + "/testcontainers/nativeworker/velox-etc", "/opt/presto-server/etc", BindMode.READ_ONLY)
    //             .withFileSystemBind(BASE_DIR + "/testcontainers/nativeworker/entrypoint.sh", "/opt/entrypoint.sh", BindMode.READ_ONLY)
    //             .withFileSystemBind(BASE_DIR + "/testcontainers/plugin/libpresto_function_my_dynamic.dylib", "/opt/plugin/libpresto_function_my_dynamic.dylib", BindMode.READ_ONLY)
    //             .waitingFor(Wait.forLogMessage(".*Announcement succeeded: HTTP 202.*", 1));

    //     coordinator.start();
    //     worker.start();

    //     // Wait for worker to announce itself.
    //     TimeUnit.SECONDS.sleep(5);
    // }
    // @AfterClass
    // public void tearDown()
    //             throws InterruptedException
    // {
    //     TimeUnit.SECONDS.sleep(60);
    //     coordinator.stop();
    //     worker.stop();
    // }
    // protected Container.ExecResult executeQuery(String sql)
    //         throws IOException, InterruptedException
    // {
    //     // Command to run inside the coordinator container using the presto-cli.
    //     String[] command = {
    //             "/opt/presto-cli",
    //             "--server",
    //             "presto-coordinator:8081",
    //             "--catalog",
    //             "tpch",
    //             "--schema",
    //             "tiny",
    //             "--execute",
    //             sql
    //     };

    //     Container.ExecResult execResult = coordinator.execInContainer(command);
    //     if (execResult.getExitCode() != 0) {
    //         String errorDetails = "Stdout: " + execResult.getStdout() + "\nStderr: " + execResult.getStderr();
    //         fail("Presto CLI exited with error code: " + execResult.getExitCode() + "\n" + errorDetails);
    //     }
    //     return execResult;
    // }
    @Test
    public void testShowFunctions()
            throws IOException, InterruptedException
    {
        TimeUnit.SECONDS.sleep(20);
        String sql = "SHOW FUNCTIONS";
        assertTrue(
                computeActual(sql).toString().contains("dynamic_123"), "dynamic function was not loaded");
        // Container.ExecResult actualResult = executeQuery(sql);
        // System.out.println(actualResult.toString());
        // assertTrue(actualResult.getStdout().contains("dynamic_123"), "dynamic function was not loaded");
    }
}
