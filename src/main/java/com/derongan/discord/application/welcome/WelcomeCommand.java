package com.derongan.discord.application.welcome;

import com.derongan.discord.application.Command;
import javax.inject.Inject;
import net.dv8tion.jda.api.entities.Message;

public class WelcomeCommand implements Command {

  @Inject
  public WelcomeCommand() {
  }

  @Override
  public void handleInput(Message input) {
    input.getChannel().sendMessage("Welcome to mine in abyss!").queue();
  }
}
