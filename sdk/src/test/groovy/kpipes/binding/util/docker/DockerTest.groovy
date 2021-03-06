/**
 * Licensed to the Kafkaless under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kpipes.binding.util.docker

import org.kafkaless.util.Config
import org.kafkaless.util.process.DefaultProcessManager
import org.kafkaless.util.process.SudoResolver
import org.assertj.core.api.Assertions
import org.junit.Test

import static org.kafkaless.util.Uuids.uuid

class DockerTest {

    // Fixtures

    Docker docker = new CommandLineDocker(new DefaultProcessManager(new SudoResolver(new Config())))

    def containerName = uuid()

    // Tests

    @Test
    void shouldCreateContainer() {
        def status = docker.startService(Container.container('ubuntu', containerName))
        Assertions.assertThat(status).isEqualTo(ServiceStartupResults.created)
    }

    @Test
    void shouldStartCreatedContainer() {
        def startupStatus = docker.startService(new ContainerBuilder('ubuntu').name(containerName).net('host').arguments('top').build())
        def containerStatus = docker.status(containerName)
        Assertions.assertThat(startupStatus).isEqualTo(ServiceStartupResults.created)
        Assertions.assertThat(containerStatus).isEqualTo(ContainerStatus.running)
    }

    @Test
    void shouldNotStartContainerSecondTime() {
        def container = new ContainerBuilder('ubuntu').name(containerName).build()
        docker.startService(container)
        def status = docker.startService(container)
        Assertions.assertThat(status).isEqualTo(ServiceStartupResults.alreadyRunning)
    }

    @Test
    void shouldStopContainer() {
        // Given
        docker.startService(new ContainerBuilder('ubuntu').name(containerName).arguments('top').build())

        // When
        docker.stop(containerName)

        // Then
        def containerStatus = docker.status(containerName)
        Assertions.assertThat(containerStatus).isEqualTo(ContainerStatus.created)
    }

    @Test
    void shouldInspectContainer() {
        // Given
        docker.startService(new ContainerBuilder('ubuntu').name(containerName).arguments('top').build())

        // When
        def inspectResults = docker.inspect(containerName)

        // Then
        Assertions.assertThat(inspectResults.environment()).isNotNull()
    }

    @Test
    void shouldSetEnvironmentOnContainer() {
        // Given
        docker.startService(new ContainerBuilder('ubuntu').name(containerName).environment([foo: 'bar']).arguments('top').build())

        // When
        def inspectResults = docker.inspect(containerName)

        // Then
        Assertions.assertThat(inspectResults.environment()).containsEntry('foo', 'bar')
    }

}
