package com.eukon05.dilanbot.reddit.exception;

import com.eukon05.dilanbot.DilanException;
import com.eukon05.dilanbot.Message;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

public class SubredditPrivateException extends DilanException {
    @Override
    public void handle(InteractionFollowupMessageBuilder responder, String locale) {
        responder.setContent(Message.SUBREDDIT_PRIVATE.get(locale)).send();
    }
}
