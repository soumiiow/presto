package com.facebook.presto.nativetests;

import com.facebook.presto.nativeworker.NativeQueryRunnerUtils;
import com.facebook.presto.nativeworker.PrestoNativeQueryRunnerUtils;
import com.facebook.presto.testing.QueryRunner;
import com.facebook.presto.tests.AbstractTestCustomFunctions;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.facebook.presto.sidecar.NativeSidecarPluginQueryRunnerUtils.setupNativeSidecarPlugin;
import static java.lang.Boolean.parseBoolean;

public class TestDynamicFunctions extends AbstractTestCustomFunctions {
    @Parameters("storageFormat")
    @Override
    protected void createTables()
    {
        try {
            String storageFormat = System.getProperty("storageFormat");
            QueryRunner javaQueryRunner = PrestoNativeQueryRunnerUtils.javaHiveQueryRunnerBuilder()
                    .setStorageFormat(storageFormat)
                    .setAddStorageFormatToPath(true)
                    .build();
            NativeQueryRunnerUtils.createOrders(javaQueryRunner);
            javaQueryRunner.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Parameters({"storageFormat", "sidecarEnabled"})
    @Override
    protected QueryRunner createQueryRunner() throws Exception
    {
        Path workingDir = Paths.get(System.getProperty("user.dir"));
        // TODO make sure in CI working dir is the right path. should be PRESTO_HOME there but locally, its at working dir.
        Path pluginDir = workingDir
                .resolve("src")
                .resolve("test")
                .resolve("resources")
                .resolve("plugin");
        boolean sidecar = parseBoolean(System.getProperty("sidecarEnabled"));
        QueryRunner queryRunner = PrestoNativeQueryRunnerUtils.nativeHiveQueryRunnerBuilder()
                .setStorageFormat(System.getProperty("storageFormat"))
                .setAddStorageFormatToPath(true)
                .setUseThrift(true)
                .setCoordinatorSidecarEnabled(sidecar)
                .setPluginDirectory(Optional.of(pluginDir.toString()))
                .build();
        if (sidecar) {
            setupNativeSidecarPlugin(queryRunner);
        }
        return queryRunner;
    }

    // Aggregate functions are not supported currently.
    @Override
    @Test (enabled = false)
    public void testCustomSum()
    {
    }

    // Window functions are not supported currently.
    @Override
    @Test (enabled = false)
    public void testCustomRank()
    {
    }
}
