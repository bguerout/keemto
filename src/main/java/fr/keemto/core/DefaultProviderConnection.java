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


public class DefaultProviderConnection implements ProviderConnection {

    private final String providerId;
    private final String providerUserId;
    private final String displayName;
    private final String profileUrl;
    private final String imageUrl;

    public DefaultProviderConnection(String providerId, String providerUserId, String displayName, String profileUrl, String imageUrl) {
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
    }

    public DefaultProviderConnection(String providerId) {
        this(providerId, null, null, null, null);
    }

    @Override
    public String getId() {
        return getProviderId() + "-" + getProviderUserId();
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getProviderUserId() {
        return providerUserId;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getProfileUrl() {
        return profileUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean isAnonymous() {
        return getProviderUserId() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DefaultProviderConnection that = (DefaultProviderConnection) o;

        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) {
            return false;
        }
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) {
            return false;
        }
        if (profileUrl != null ? !profileUrl.equals(that.profileUrl) : that.profileUrl != null) {
            return false;
        }
        if (providerId != null ? !providerId.equals(that.providerId) : that.providerId != null) {
            return false;
        }
        if (providerUserId != null ? !providerUserId.equals(that.providerUserId) : that.providerUserId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = providerId != null ? providerId.hashCode() : 0;
        result = 31 * result + (providerUserId != null ? providerUserId.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (profileUrl != null ? profileUrl.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DefaultProviderConnection{" +
                "providerId='" + providerId + '\'' +
                ", providerUserId='" + providerUserId + '\'' +
                ", displayName='" + displayName +
                '}';
    }
}
