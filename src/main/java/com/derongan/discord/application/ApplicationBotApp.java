package com.derongan.discord.application;

import com.derongan.discord.application.apply.ApplyModule;
import com.derongan.discord.application.config.ChannelConfig;
import com.derongan.discord.application.config.TestConfigModule;
import com.derongan.discord.application.debug.DebugModule;
import com.derongan.discord.application.review.ReviewModule;
import dagger.Component;
import javax.inject.Singleton;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class ApplicationBotApp {

  private final CommandHandler commandHandler;
  private final ChannelConfig channelConfig;

  @Singleton
  @Component(modules = {DebugModule.class, ApplyModule.class, ReviewModule.class,
      TestConfigModule.class,
      JDAModule.class})
  interface CommandHandlerFactory {

    ChannelConfig channelConfig();

    CommandHandler handler();

    JDA jda();
  }

  public ApplicationBotApp(CommandHandler commandHandler, ChannelConfig channelConfig) {
    this.commandHandler = commandHandler;
    this.channelConfig = channelConfig;
  }

  public static void main(String[] args) throws LoginException, InterruptedException {
    CommandHandlerFactory handlerFactory = DaggerApplicationBotApp_CommandHandlerFactory.create();
    CommandHandler handler = handlerFactory.handler();
    ChannelConfig channelConfig = handlerFactory.channelConfig();
    JDA jda = handlerFactory.jda();

    jda.awaitReady();

    jda.addEventListener(new ApplicationBotApp(handler, channelConfig));

  }

  @SubscribeEvent
  public void onMessage(MessageReceivedEvent messageReceivedEvent) {
    commandHandler.handle(messageReceivedEvent.getMessage());
  }
}
