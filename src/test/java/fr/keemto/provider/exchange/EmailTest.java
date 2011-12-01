package fr.keemto.provider.exchange;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmailTest {

    @Test
    public void shouldExposeRecipientsAsACommaSeparatedString() throws Exception {
        List<String> recipients = Lists.newArrayList("1@domain.fr", "2@domain.fr");
        Email email = new Email("id", "user@gmail.com", "subject", "body", System.currentTimeMillis(), recipients);

        String recipientsAsString = email.getRecipientsAsString();

        assertThat(recipientsAsString, equalTo("1@domain.fr,2@domain.fr"));
    }

    @Test
    public void shouldBuildMailWithACommaSeparatedRecipients() throws Exception {
        Email email = new Email("id", "user@gmail.com", "subject", "body", System.currentTimeMillis(), "1@domain.fr,2@domain.fr");

        List<String> recipients = email.getRecipients();

        assertThat(recipients, hasItems("1@domain.fr", "2@domain.fr"));
    }

    @Test
    public void shouldConvertBodyToHtmlFragment() throws Exception {
        String html = "<html><body><div>Here is a text</div></body></html>";
        Email email = new Email("id", "user@gmail.com", "subject", html, System.currentTimeMillis(), "1@domain.fr,2@domain.fr");

        String fragment = email.getBodyAsHtmlFragment();

        assertThat(fragment, equalTo("<div>Here is a text</div>"));
    }


    @Test
    public void shouldConvertBodyToHtmlFragmentWithCarriageReturn() throws Exception {
        String html = FileUtils.readFileToString(ResourceUtils.getFile("src/test/resources/email.html"));
        Email email = new Email("id", "user@gmail.com", "subject", html, System.currentTimeMillis(), "1@domain.fr,2@domain.fr");

        String fragment = email.getBodyAsHtmlFragment();

        assertThat(fragment, equalTo("\n<div>\n    A line&nbsp;\n</div>\n<br>\n"));
    }

    @Test
    public void whenEmailBodyIsNotHtmlShouldWrapWithPreTag() throws Exception {
        String emailBody = "this is a text";
        Email email = new Email("id", "user@gmail.com", "subject", emailBody, System.currentTimeMillis(), "1@domain.fr,2@domain.fr");

        String fragment = email.getBodyAsHtmlFragment();

        assertThat(fragment, equalTo("<pre>this is a text</pre>"));
    }
}
