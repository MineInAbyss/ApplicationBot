package com.derongan.discord.application.apply;

import com.derongan.discord.application.ApplicationTracker;
import com.derongan.discord.application.Command;
import javax.inject.Inject;
import net.dv8tion.jda.api.entities.Message;

public class CancelCommand implements Command {

  private final ApplicationTracker applicationTracker;

  @Inject
  public CancelCommand(ApplicationTracker applicationTracker) {
    this.applicationTracker = applicationTracker;
  }

  @Override
  public void handleInput(Message input) {
    if (applicationTracker
        .getApplicationForUser(input.getAuthor()).isPresent()) {
      applicationTracker.cancelApplication(input.getAuthor());
      input.getChannel().sendMessage("Application canceled").queue();
    } else {
      input.getChannel().sendMessage("No application found").queue();
    }
  }
}
