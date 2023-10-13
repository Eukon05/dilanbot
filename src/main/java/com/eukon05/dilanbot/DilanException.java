package com.eukon05.dilanbot;

import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

public abstract class DilanException extends RuntimeException {
    public abstract void handle(InteractionFollowupMessageBuilder responder, String locale);
}
