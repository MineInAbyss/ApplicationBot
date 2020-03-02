package com.derongan.discord.application.debug;

import com.derongan.discord.application.Command;
import javax.inject.Inject;
import net.dv8tion.jda.api.entities.Message;

public class PingCommand implements Command {

  @Inject
  public PingCommand() {
  }

  @Override
  public void handleInput(Message input) {
    input.getChannel().sendMessage("pong").queue();
  }
}
