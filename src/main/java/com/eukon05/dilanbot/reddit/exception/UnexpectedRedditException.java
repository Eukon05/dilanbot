package com.eukon05.dilanbot.reddit.exception;

import com.eukon05.dilanbot.DilanException;
import com.eukon05.dilanbot.Message;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

public class UnexpectedRedditException extends DilanException {
    private final String redditErrorMessage;

    public UnexpectedRedditException(String redditErrorMessage) {
        this.redditErrorMessage = redditErrorMessage;
    }

    @Override
    public void handle(InteractionFollowupMessageBuilder responder, String locale) {
        responder.setContent(String.format(Message.REDDIT_ERROR.get(locale), redditErrorMessage)).send();
    }
}
