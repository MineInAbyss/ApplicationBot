package com.derongan.discord.application.review;

import com.derongan.discord.application.Application;
import com.derongan.discord.application.ApplicationTracker;
import com.derongan.discord.application.Command;
import javax.inject.Inject;
import net.dv8tion.jda.api.entities.Message;

public class ListCommand implements Command {

  private final ApplicationTracker applicationTracker;

  @Inject
  public ListCommand(ApplicationTracker applicationTracker) {
    this.applicationTracker = applicationTracker;
  }

  @Override
  public void handleInput(Message input) {
    StringBuilder messageToSend = new StringBuilder("Active Applications:");
    int i = 0;
    for (Application application : applicationTracker.listApplications()) {
      messageToSend.append("\n").append(++i).append(". ");
      messageToSend
          .append(input.getGuild().getMemberById(application.getApplicantId()).getEffectiveName())
          .append(" - ").append(application.getStatus().name());
    }

    input.getChannel().sendMessage(messageToSend.toString()).queue();
  }
}
