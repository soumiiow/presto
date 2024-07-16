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

import com.facebook.presto.testing.MaterializedResult;
import com.facebook.presto.testing.MaterializedRow;
import com.facebook.presto.testing.QueryRunner;
import com.facebook.presto.tests.AbstractTestQueryFramework;
import com.facebook.presto.tests.DistributedQueryRunner;
import org.intellij.lang.annotations.Language;
import org.testcontainers.containers.Container;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.facebook.presto.nativeworker.NativeQueryRunnerUtils.createLineitem;
import static com.facebook.presto.nativeworker.NativeQueryRunnerUtils.createOrders;
import static com.facebook.presto.nativeworker.PrestoNativeQueryRunnerUtils.setupNativeFunctionNamespaceManager;
import static com.facebook.presto.nativeworker.PrestoNativeQueryRunnerUtils.setupSessionPropertyProvider;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.lang.String.format;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
@Test(singleThreaded = true)
public class TestPrestoNativeDynamicLibrary
        extends TestPrestoContainerBasicQueries
{
    @Test
    public void testShowFunctions()
            throws IOException, InterruptedException
    {
        String sql = "SHOW FUNCTIONS";
        Container.ExecResult actualResult = executeQuery(sql);
        assertTrue(actualResult.getStdout().contains("dynamic_123"), "dynamic function was not loaded");
    }
}
