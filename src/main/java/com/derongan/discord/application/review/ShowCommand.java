package com.derongan.discord.application.review;

import com.derongan.discord.application.Application;
import com.derongan.discord.application.ApplicationTracker;
import com.derongan.discord.application.Command;
import com.derongan.discord.application.CommandValidator;
import com.derongan.discord.application.Status;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class ShowCommand implements Command {

  private final CommandValidator validator;
  private final ApplicationTracker applicationTracker;

  @Inject
  public ShowCommand(ApplicationTracker applicationTracker) {
    this.applicationTracker = applicationTracker;
    validator = CommandValidator.builder().addArgumentValidator(argument -> {
    }).build();
  }

  @Override
  public void handleInput(Message input) {
    try {
      List<String> args = validator.validate(input);

      Optional<Application> application = input.getGuild()
          .getMembersByEffectiveName(args.get(0), false)
          .stream().map(Member::getUser).map(applicationTracker::getApplicationForUser)
          .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty)).findFirst();

      if (application.isPresent()) {
        Application app = application.get();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        Member member = input.getGuild().getMemberById(app.getApplicantId());
        Role role = input.getGuild().getRoleById(app.getRoleId());
        embedBuilder
            .setAuthor(member.getEffectiveName(), null, member.getUser().getEffectiveAvatarUrl())
            .setTitle(String.format("Application for %s", role.getName()))
            .setColor(role.getColor());

        if (app.getStatus() == Status.COMPLETED) {
          app.getResponses().entrySet()
              .forEach(entry -> embedBuilder.addField(entry.getKey(), entry.getValue(), false));
        }

        input.getChannel().sendMessage(embedBuilder.build()).queue();
      } else {
        input.getChannel().sendMessage(String.format("Application for %s not found", args.get(0)))
            .queue();
      }
    } catch (IllegalArgumentException e) {
      input.getChannel().sendMessage(e.getMessage()).queue();
    }
  }
}
