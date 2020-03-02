package com.derongan.discord.application;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

@Module
public abstract class JDAModule {

  @Provides
  @Singleton
  static JDA providesJDA() {
    try {
      return new JDABuilder("SECRET_KEY")
          .setEventManager(new AnnotatedEventManager())
          .build();
    } catch (LoginException e) {
      throw new RuntimeException(e);
    }
  }
}
