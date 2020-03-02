package com.derongan.discord.application.review;

import com.derongan.discord.application.Channel;
import com.derongan.discord.application.Command;
import com.derongan.discord.application.CommandChannel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public abstract class ReviewModule {

  @Binds
  @IntoMap
  @StringKey("approve")
  @CommandChannel(Channel.REVIEW)
  abstract Command approveCommand(ApproveCommand approveCommand);

  @Binds
  @IntoMap
  @StringKey("reject")
  @CommandChannel(Channel.REVIEW)
  abstract Command rejectCommand(RejectCommand rejectCommand);

//  @Binds
//  @IntoMap
//  @StringKey("list")
//  @CommandChannel(Channel.REVIEW)
//  abstract Command listCommand(ListCommand listCommand);
//
//  @Binds
//  @IntoMap
//  @StringKey("show")
//  @CommandChannel(Channel.REVIEW)
//  abstract Command showCommand(ShowCommand showCommand);
}
