package me.divelog.core.web.message;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * GenericWeb의 grid용 Data클래스가 구현하면 message()를 이용해서 key->i18n message를 찾아주는 기능을 사용할 수 있음.
 * Locale은 Spring의 LocaleContextHolder이용
 * <p>
 * Created by toby on 17/11/2016.
 */
public abstract class MessageSourceSupport {
    protected MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected String message(String key, Object[] args, String defaultMessage) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, defaultMessage, locale);
    }

    protected String message(String key) {
        return message(key, null, null);
    }

    protected String message(String key, Object[] args) {
        return message(key, args, null);
    }

    protected String message(String key, String defaultMessage) {
        return message(key, null, defaultMessage);
    }
}
