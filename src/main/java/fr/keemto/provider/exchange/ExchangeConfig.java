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
package fr.keemto.provider.exchange;

import fr.keemto.provider.exchange.importer.ExchangeMailFinder;
import fr.keemto.provider.exchange.importer.ExchangeServiceFactory;
import fr.keemto.provider.exchange.importer.MailImporterTask;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.WebCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.URI;

@Configuration
public class ExchangeConfig {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Value("${keemto.ews.xebia.uri}")
    protected URI serviceUrl;

    @Value("${keemto.ews.xebia.login}")
    protected String login;

    @Value("${keemto.ews.xebia.pwd}")
    protected String pwd;

    @Bean
    public MailImporterTask mailImporterTask() {
        ExchangeServiceFactory factory = new ExchangeServiceFactory(xebiaExchangeService());
        ExchangeMailFinder mailFinder = new ExchangeMailFinder(factory);
        return new MailImporterTask(mailFinder, mailRepository());
    }

    @Bean
    public MailRepository mailRepository() {
        return new JdbcMailRepository(jdbcTemplate);
    }

    @Bean
    public ExchangeService xebiaExchangeService() {

        ExchangeService exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        exchangeService.setUrl(serviceUrl);
        ExchangeCredentials credentials = new WebCredentials(login, pwd);
        exchangeService.setCredentials(credentials);
        return new ExchangeService();
    }
}
