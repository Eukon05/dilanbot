package com.eukon05.dilanbot;

import java.util.Locale;
import java.util.ResourceBundle;

public enum Message {
    IS_PAUSED,
    NOT_PAUSED,
    PAUSED,
    DISCONNECT,
    QUEUE_EMPTY,
    LOOP_ENABLED,
    LOOP_DISABLED,
    WORDLIST_NOT_FOUND,
    GENIUS_NOT_FOUND,
    GENIUS_FOOTER,
    NP,
    QUEUE_LESS_PAGES,
    QUEUE_TRACKS,
    QUEUE_INVALID_INDEX,
    QUEUE_LESS_TRACKS,
    QUEUE_SHUFFLED,
    QUEUE_REMOVED_TRACK,
    QUEUE_REMOVED_TRACKS,
    SKIPPED,
    STOPPED,
    SUBREDDIT_PRIVATE,
    SUBREDDIT_NOT_FOUND,
    REDDIT_ERROR,
    REDDIT_FOOTER,
    SUBMISSION_HAS_VIDEO,
    ERROR,
    IO_ERROR,
    JOIN_TITLE,
    JOIN_DESC,
    NO_MATCH,
    PLAYING,
    QUEUED,
    RESUMED,
    VC_USER_NOT_CONNECTED,
    VC_BOT_NOT_CONNECTED,
    VC_DIFFERENT_CHANNELS,
    NOT_PLAYING,
    DM,
    EIGHTBALL_FOOTER;

    public static final String MARKDOWN_URL = "[%s](%s)";

    public String get(String localeCode) {
        return ResourceBundle.getBundle("messages", Locale.forLanguageTag(localeCode)).getString(this.name());
    }

}
