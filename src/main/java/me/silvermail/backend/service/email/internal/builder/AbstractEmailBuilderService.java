package me.silvermail.backend.service.email.internal.builder;

import me.silvermail.backend.model.email.internal.AbstractInternalEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Service
abstract public class AbstractEmailBuilderService {
    protected static final String TEMPLATE_DIR = "email/";

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected TemplateEngine templateEngine;

    @Value("${frontend.host}")
    protected String frontendHost;

    public void render(AbstractInternalEmail email, Context context) {
        Locale locale = Locale.ENGLISH;
        if (email.getLanguage() != null) {
            locale = Locale.forLanguageTag(email.getLanguage());
        }

        context.setLocale(locale);
        context.setVariable("frontendHost", frontendHost);

        email.setSubject(messageSource.getMessage(email.getSubjectKey(), null, locale));
        email.setBody(
                this.templateEngine.process(TEMPLATE_DIR + email.getTemplateName(), context)
        );
    }

    abstract public void build(AbstractInternalEmail email) throws Exception;
}
