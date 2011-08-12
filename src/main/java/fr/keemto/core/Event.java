/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.keemto.core;

import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class Event {

    private final long timestamp;

    private final String user;

    private final String message;

    private final String providerId;

    @JsonCreator
    public Event(@JsonProperty("timestamp") long timestamp, @JsonProperty("user") String user,
                 @JsonProperty("message") String message, @JsonProperty("providerId") String providerId) {
        super();
        this.timestamp = timestamp;
        this.user = user;
        this.message = message;
        this.providerId = providerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public String getProviderId() {
        return providerId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timestamp, user, providerId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Event) {
            Event other = (Event) o;
            return Objects.equal(other.timestamp, timestamp) && Objects.equal(other.user, user)
                    && Objects.equal(other.providerId, providerId);
        }
        return false;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("timestamp", timestamp).add("owner", user).add("message", message)
                .toString();
    }

}
