package com.derongan.discord.application.review;

import com.derongan.discord.application.Application;
import com.derongan.discord.application.ApplicationTracker;
import com.derongan.discord.application.Command;
import java.util.Optional;
import javax.inject.Inject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class ApproveCommand implements Command {

  private final ApplicationTracker applicationTracker;

  @Inject
  public ApproveCommand(ApplicationTracker applicationTracker) {
    this.applicationTracker = applicationTracker;
  }

  @Override
  public void handleInput(Message input) {
    Guild guild = input.getGuild();
    long applicantId = Long.parseLong(input.getChannel().getName().split("-")[2]);

    Member member = guild.getMemberById(applicantId);

    Optional<Application> applicationForUser = applicationTracker
        .getApplicationForUser(member.getUser());

    if (applicationForUser.isPresent()) {
      Application application = applicationForUser.get();

      applicationTracker.updateApplication(member.getUser(),
          application.toBuilder().addApproval(input.getAuthor().getIdLong()).build());
    }
  }
}
