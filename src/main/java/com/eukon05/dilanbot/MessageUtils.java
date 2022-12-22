package com.eukon05.dilanbot;

import java.util.Locale;
import java.util.ResourceBundle;

public final class MessageUtils {

    private MessageUtils() {
    }

    public static final String MARKDOWN_URL = "[%s](%s)";

    public static String getMessage(String key, String localeCode) {
        return ResourceBundle.getBundle("messages", Locale.forLanguageTag(localeCode)).getString(key);
    }

}
