package com.derongan.discord.application.welcome;

import com.derongan.discord.application.Command;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class WelcomeModule {

  @Binds
  @Join
  abstract Command welcomeCommand(WelcomeCommand welcomeCommand);

}
