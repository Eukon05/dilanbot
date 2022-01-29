package com.eukon05.dilanbot.Listeners;

import com.eukon05.dilanbot.DTOs.ServerDTO;
import com.eukon05.dilanbot.Services.ServerService;
import lombok.Setter;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractMessageListener implements MessageCreateListener {

    @Value("${prefix}")
    private String defaultPrefix;

    final String keyWord;
    ServerService serverService;

    @Setter
    private boolean allowDefaultPrefix = false;

    public AbstractMessageListener(String keyWord){
        this.keyWord=keyWord;
    }

    @Autowired
    private void setServerService(ServerService serverService){
        this.serverService=serverService;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        if(event.getMessageAuthor().isYourself())
            return;

        ServerDTO serverDTO = serverService.getServerById(event.getServer().get().getId());
        String message = event.getMessageContent();
        String value;

        if(!message.startsWith(serverDTO.getPrefix() + keyWord)) {
            if(!allowDefaultPrefix || !message.startsWith(defaultPrefix + keyWord))
                return;
            else
                value = message.replaceFirst(defaultPrefix + keyWord,"").trim();
        }
        else
            value = message.replaceFirst(serverDTO.getPrefix() + keyWord,"").trim();

        if(value.endsWith(serverDTO.getPrefix() + " repeat") || (allowDefaultPrefix && value.endsWith(defaultPrefix + " repeat"))){

            value = value.replace(serverDTO.getPrefix() + " repeat", "").trim();
            value = value.replace(defaultPrefix + " repeat","").trim();

            final String finalValue = value;
            event.getChannel().sendMessage("Enter how many times to run the command:");
            event.getChannel().addMessageCreateListener(new MessageCreateListener() {

                @Override
                public void onMessageCreate(MessageCreateEvent input) {

                    try{

                        if(input.getMessage().getAuthor().isYourself())
                            return;

                        int number = Integer.parseInt(input.getMessageContent());

                        if(number>10){
                            input.getChannel().sendMessage("You can't repeat an action more than 10 times! Please enter another number:");
                            return;
                        }

                        input.getChannel().removeListener(MessageCreateListener.class, this);

                        for(int i=1; i<=number; i++)
                            childOnMessageCreate(event, serverDTO, finalValue);
                    }
                    catch (NumberFormatException e){
                        input.getChannel().sendMessage("Please enter a number!");
                    }
                }

            });

        }
        else
            childOnMessageCreate(event, serverDTO, value);

    }

    /*
    Thanks to StackOverflow user Joakim Danielson for coming up with a great idea of moving the listeners main code
    to another abstract method, instead of overriding onMessageCreate.
    https://bit.ly/3p3zGrE


    This method is problematic, since it requires A TON of arguments to function correctly.
    That is caused by not having any variables associated with the listener class itself, but rather as temporary variables accessed only inside the methods.
    onMessageCreate() method needs to pass all required objects to childOnMessageCreate().
    This can be sorted out by introducing a new type of object, that could store all necessary variables inside it.

    Answering the next question, all listener classes have their childOnMessageCreate() method create and start a new thread.
    That is a temporary solution that allows the bot to execute multiple commands at the same time without focusing on one task and then moving on to another.
    This is almost necessary, because if the bot was added to many, many servers, it could be overloaded fairly quickly if every listener had to
    complete a heavy task before accepting new requests.

    Also, every overridden childOnMessageCreate() method creates a new ServerTextChannel type object called "channel".
    This is for convenience only and should be done in a super-method or removed at all
     */
    abstract void childOnMessageCreate(MessageCreateEvent event, ServerDTO serverDTO, String value);

}
