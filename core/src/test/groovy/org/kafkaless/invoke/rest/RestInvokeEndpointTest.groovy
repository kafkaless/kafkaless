package org.kafkaless.invoke.rest

import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.kafkaless.core.Kafkaless
import org.kafkaless.endpoint.management.ManagementService
import org.kafkaless.util.kafka.KafkaTemplate

import static io.vertx.core.Vertx.vertx
import static org.assertj.core.api.Assertions.assertThat
import static org.kafkaless.util.Json.fromJson
import static org.kafkaless.util.Json.jsonString
import static org.kafkaless.util.Uuids.uuid

@RunWith(VertxUnitRunner)
class RestInvokeEndpointTest {

    static def tenant = uuid()

    static def template = new KafkaTemplate('localhost', 9092, 'localhost', 2181)

    static def endpoint = new RestInvokeEndpoint(new ManagementService(template), new MockAuthentication(tenant))

    static {
        endpoint.start()
    }

    def functionName = uuid()

    def payload = [foo: 'bar']

    // Tests

    @Test
    void shouldInvokeFunction(TestContext context) {
        def async = context.async()

        new Kafkaless(template, tenant).functionHandler(functionName) {
            it
        }
        Thread.sleep(5000)

        vertx().createHttpClient().post(8081, 'localhost',"/${functionName}").handler {
            it.bodyHandler {
                def response = fromJson(it.bytes)
                assertThat(response.foo).isEqualTo('bar')
                async.complete()
            }
        }.end(jsonString(payload))
    }

}