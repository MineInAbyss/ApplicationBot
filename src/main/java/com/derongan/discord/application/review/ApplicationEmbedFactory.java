package com.derongan.discord.application.review;

import static com.google.common.base.Preconditions.checkArgument;

import com.derongan.discord.application.Application;
import com.derongan.discord.application.Status;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class ApplicationEmbedFactory {
  private final JDA jda;

  @Inject
  public ApplicationEmbedFactory(JDA jda) {
    this.jda = jda;
  }

  public MessageEmbed createEmbedForApplication(Application application) {
    checkArgument(application.getStatus() == Status.COMPLETED);
    Guild guild = jda.getGuildById(application.getGuildId());
    Member member = guild.getMemberById(application.getApplicantId());
    User user = member.getUser();
    Role role = guild.getRoleById(application.getRoleId());

    EmbedBuilder embedBuilder = new EmbedBuilder()
        .setAuthor(member.getEffectiveName(), null, user.getEffectiveAvatarUrl())
        .setTitle(String.format("Application for %s#%s", user.getName(), user.getDiscriminator()))
        .setColor(role.getColor());

    application.getResponses().entrySet()
        .forEach(entry -> embedBuilder.addField(entry.getKey(), entry.getValue(), false));

    return embedBuilder.build();
  }
}
