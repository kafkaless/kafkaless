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
package org.kafkaless.org.kafkaless.sdk.api

import org.junit.Test
import org.kafkaless.core.api.Event

import static java.util.Optional.empty
import static org.assertj.core.api.Assertions.assertThat

class EventTest {

    @Test
    void shouldPrintEmptyPayload() {
        def toString = new Event('key', [:], empty()).toString()
        assertThat(toString).contains('payload={}')
    }

}
