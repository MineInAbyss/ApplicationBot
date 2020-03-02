package com.derongan.discord.application;

import com.derongan.discord.application.apply.PrivateCommand;
import com.derongan.discord.application.config.ChannelConfig;
import java.util.Map;
import javax.inject.Inject;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class CommandHandler {

  private final Map<String, Command> applyCommands;
  private final Map<String, Command> reviewCommands;
  private final Map<String, Command> anyCommands;
  private final Command questionHandler;
  private final ChannelConfig channelConfig;
  private static final Command DEFAULT_COMMAND = input -> {
  };

  // TODO this binding is not the best way
  @Inject
  public CommandHandler(@CommandChannel(Channel.APPLY) Map<String, Command> applyCommands,
      @CommandChannel(Channel.REVIEW) Map<String, Command> reviewCommands,
      @CommandChannel(Channel.ANY) Map<String, Command> anyCommands,
      @PrivateCommand Command questionHandler,
      ChannelConfig channelConfig) {
    this.applyCommands = applyCommands;
    this.reviewCommands = reviewCommands;
    this.anyCommands = anyCommands;
    this.questionHandler = questionHandler;
    this.channelConfig = channelConfig;
  }

  public void handle(Message message) {
    String[] split = message.getContentRaw().split(" ");

    if (split.length == 0) {
      return;
    }

    String key = split[0];

    Command command;

    if (message.getChannel().getIdLong() == channelConfig.getApplicationChannelId()) {
      command = applyCommands.getOrDefault(key, anyCommands.getOrDefault(key, DEFAULT_COMMAND));
    } else if (message.getCategory() != null
        && message.getCategory().getIdLong() == channelConfig.getApplicationCategoryId()) {
      command = reviewCommands.getOrDefault(key, anyCommands.getOrDefault(key, DEFAULT_COMMAND));
    } else if (message.isFromType(ChannelType.PRIVATE)) {
      command = questionHandler;
    } else {
      command = anyCommands.getOrDefault(key, DEFAULT_COMMAND);
    }

    command.handleInput(message);
  }
}
