package com.derongan.discord.application.debug;

import com.derongan.discord.application.Channel;
import com.derongan.discord.application.Command;
import com.derongan.discord.application.CommandChannel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public abstract class DebugModule {

  @Binds
  @IntoMap
  @StringKey("ping")
  @CommandChannel(Channel.ANY)
  abstract Command pingCommand(PingCommand pingCommand);
}
