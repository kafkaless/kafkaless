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
package org.kafkaless.core.api

import com.google.common.base.MoreObjects
import groovy.transform.ToString
import org.apache.commons.lang3.Validate

class Event {

    private final String key

    private final Map<String, Object> metadata

    private final Optional<Map<String, Object>> payload

    Event(String key, Map<String, Object> metadata, Optional<Map<String, Object>> payload) {
        validateConstructor(key, metadata)
        this.key = key
        this.metadata = metadata
        this.payload = payload
    }

    private validateConstructor(String key, Map<String, Object> metadata) {
        Validate.isTrue(!key.empty, 'Key can be null, but cannot be empty string.')
        Validate.notNull(metadata, 'Metadata can be empty, but cannot be null.')
    }

    String key() {
        key
    }

    Map<String, Object> metadata() {
        metadata
    }

    Optional<Map<String, Object>> payload() {
        payload
    }

    @Override
    String toString() {
        return MoreObjects.toStringHelper(this).
                add("key", key).
                add("metadata", metadata).
                add("payload", payload.orElse([:])).
                toString()
    }

}