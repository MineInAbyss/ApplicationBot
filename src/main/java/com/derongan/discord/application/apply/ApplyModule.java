package com.derongan.discord.application.apply;

import com.derongan.discord.application.Channel;
import com.derongan.discord.application.Command;
import com.derongan.discord.application.CommandChannel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public abstract class ApplyModule {

  @Binds
  @IntoMap
  @StringKey("apply")
  @CommandChannel(Channel.APPLY)
  abstract Command applyCommand(ApplyCommand applyCommand);

  @Binds
  @IntoMap
  @StringKey("cancel")
  @CommandChannel(Channel.APPLY)
  abstract Command cancelCommand(CancelCommand cancelCommand);

  @Binds
  @IntoMap
  @StringKey("list")
  @CommandChannel(Channel.APPLY)
  abstract Command listCommand(ListCommand listCommand);

  @Binds
  @PrivateCommand
  abstract Command privateCommand(QuestionResponder questionResponder);
}
