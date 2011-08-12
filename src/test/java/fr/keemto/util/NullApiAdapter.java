/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package fr.keemto.util;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

public class NullApiAdapter implements ApiAdapter<Object> {


    public NullApiAdapter() {
    }

    public boolean test(Object api) {
        return true;
    }

    public void setConnectionValues(Object api, ConnectionValues values) {

    }

    public UserProfile fetchUserProfile(Object api) {
        return UserProfile.EMPTY;
    }

    public void updateStatus(Object api, String message) {
    }

}