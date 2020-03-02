package com.derongan.discord.application.apply;

import com.derongan.discord.application.Command;
import com.derongan.discord.application.config.RoleFetcher;
import com.derongan.discord.application.config.RoleInfo;
import javax.inject.Inject;
import net.dv8tion.jda.api.entities.Message;

public class ListCommand implements Command {

  private final RoleFetcher roleFetcher;

  @Inject
  public ListCommand(RoleFetcher roleFetcher) {
    this.roleFetcher = roleFetcher;
  }

  @Override
  public void handleInput(Message input) {
    StringBuilder messageToSend = new StringBuilder("Available Roles:");
    int i = 0;
    for (RoleInfo roleInfo : roleFetcher.getAvailableRoleInfo()) {
      messageToSend.append("\n").append(++i).append(". ");
      messageToSend
          .append(input.getGuild().getRoleById(roleInfo.getRoleId()).getName());
    }

    input.getChannel().sendMessage(messageToSend.toString()).queue();
  }
}
