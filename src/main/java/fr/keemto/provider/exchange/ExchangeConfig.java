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

import fr.keemto.core.AccountRepository;
import fr.keemto.provider.exchange.importer.ExchangeServiceWrapper;
import fr.keemto.provider.exchange.importer.MailFinder;
import fr.keemto.provider.exchange.importer.MailImporterTask;
import fr.keemto.provider.exchange.importer.NoDataServiceWrapper;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.WebCredentials;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.URI;
import java.util.Arrays;

@Configuration
public class ExchangeConfig {

    private static final Logger log = LoggerFactory.getLogger(ExchangeConfig.class);

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Bean
    public MailImporterTask mailImporterTask(MailFinder mailFinder, MailRepository mailRepository) {
        return new MailImporterTask(mailFinder, mailRepository);
    }

    @Bean
    public MailFinder mailFinder(@Value("${provider.ews.xebia.uri}") URI serviceUrl,
                                 @Value("#{systemProperties['provider.ews.xebia.login']?:null}") String login,
                                 @Value("#{systemProperties['provider.ews.xebia.password']?:null}") String password) {

        ExchangeServiceWrapper serviceWrapper = null;
        if (StringUtils.isEmpty(login)) {
            log.warn("No login provided for exchange provider {}, switching to a mocked provider. No mails will be imported.", serviceUrl);
            serviceWrapper = new NoDataServiceWrapper();
        } else {
            serviceWrapper = createRealWrapper(serviceUrl, login, password);
        }
        return new MailFinder(serviceWrapper);
    }

    private ExchangeServiceWrapper createRealWrapper(URI serviceUrl, String login, String password) {
        ExchangeService exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        exchangeService.setUrl(serviceUrl);
        ExchangeCredentials credentials = new WebCredentials(login, password);
        exchangeService.setCredentials(credentials);
        return new ExchangeServiceWrapper(exchangeService);
    }

    @Bean
    public MailRepository mailRepository() {
        return new JdbcMailRepository(jdbcTemplate);
    }

    @Bean
    public ExchangeAccountFactory mailAccountFactory(MailRepository mailRepository, AccountRepository accountRepository,
                                                     @Value("${provider.ews.xebia.allowed.recipients}") String allowedRecipients) {

        log.info("Registering mail account factory into account repository with allowed recipients {}", allowedRecipients);
        String[] recipients = StringUtils.split(allowedRecipients, ",");
        ExchangeAccountFactory exchangeAccountFactory = new ExchangeAccountFactory(mailRepository, Arrays.asList(recipients));
        log.warn("Should add factory after application context has been created.");//TODO
        accountRepository.addFactory(exchangeAccountFactory);
        return exchangeAccountFactory;

    }
}
