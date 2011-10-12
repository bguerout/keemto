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

package org.springframework.social.test.client;


import org.mockito.Mockito;
import org.springframework.social.support.ClientHttpRequestFactorySelector;

public class MockRestServer extends MockClientHttpRequestFactory {

    public MockClientHttpRequest expectNewRequest() {
        return expectNewRequest();
    }

    public void mockClientHttpRequestFactorySelector() {
        //PowerMockito.mockStatic(ClientHttpRequestFactorySelector.class);
        Mockito.when(ClientHttpRequestFactorySelector.getRequestFactory()).thenReturn(this);
    }

    public ResponseActions expect(RequestMatcher requestMatcher) {
        MockClientHttpRequest request = this.expectNewRequest();
        request.addRequestMatcher(requestMatcher);
        return request;
    }

    public void verify() {
        verifyRequests();
    }

    private static class MockClientHttpRequest extends org.springframework.social.test.client.MockClientHttpRequest {

    }
}
