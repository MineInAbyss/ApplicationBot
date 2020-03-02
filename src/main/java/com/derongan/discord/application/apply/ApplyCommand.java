package com.derongan.discord.application.apply;

import com.derongan.discord.application.ApplicationTracker;
import com.derongan.discord.application.Command;
import com.derongan.discord.application.CommandValidator;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;

public class ApplyCommand implements Command {

  private final ApplicationTracker applicationTracker;
  private CommandValidator validator;

  @Inject
  public ApplyCommand(ApplicationTracker applicationTracker) {
    this.applicationTracker = applicationTracker;
    validator = CommandValidator.builder().addArgumentValidator(argument -> {
    }).build();
  }

  @Override
  public void handleInput(Message input) {
    MessageChannel channel = input.getChannel();
    try {
      List<String> args = validator.validate(input);

      Optional<Role> role = input.getGuild().getRolesByName(args.get(0), false).stream()
          .findFirst();

      if (applicationTracker.getApplicationForUser(input.getAuthor()).isPresent()) {
        channel.sendMessage("You already have an application in progress.").queue();
        return;
      }

      if (!role.isPresent()) {
        channel.sendMessage("Role does not exist").queue();
        return;
      }

      applicationTracker.startApplication(input.getMember(), role.get());

      channel.sendMessage("Started Application!").queue();
    } catch (IllegalArgumentException e) {
      channel.sendMessage(e.getMessage()).queue();
    }
  }
}
