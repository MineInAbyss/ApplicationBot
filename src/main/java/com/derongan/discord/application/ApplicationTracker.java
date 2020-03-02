package com.derongan.discord.application;

import static com.google.common.base.Preconditions.checkArgument;

import com.derongan.discord.application.config.ChannelConfig;
import com.derongan.discord.application.config.RoleFetcher;
import com.derongan.discord.application.config.RoleInfo;
import com.derongan.discord.application.review.ApplicationEmbedFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

// TODO abstractions are cool right?
@Singleton
public class ApplicationTracker {

  private final RoleFetcher roleFetcher;
  private final ApplicationEmbedFactory applicationEmbedFactory;
  private final ChannelConfig channelConfig;
  private final String CHANNEL_FORMAT_STRING = "application for %d";
  private Map<Long, Application> applications;

  @Inject
  public ApplicationTracker(RoleFetcher roleFetcher,
      ApplicationEmbedFactory applicationEmbedFactory,
      ChannelConfig channelConfig) {
    this.roleFetcher = roleFetcher;
    this.applicationEmbedFactory = applicationEmbedFactory;
    this.channelConfig = channelConfig;
    this.applications = new HashMap<>();
  }

  public Optional<Application> getApplicationForUser(User user) {
    return Optional.ofNullable(applications.get(user.getIdLong()));
  }

  public void startApplication(Member member, Role role) {
    Application application = Application.builder().setApplicantId(member.getIdLong())
        .setRoleId(role.getIdLong())
        .setStatus(Status.STARTED)
        .setGuildId(member.getGuild().getIdLong())
        .build();
    applications
        .put(member.getIdLong(),
            application);

    member.getUser().openPrivateChannel()
        .queue(privateChannel -> privateChannel
            .sendMessage(getRoleInfo(application).get().getQuestions().getQuestions().get(0))
            .queue());
  }

  public void cancelApplication(User user) {
    applications.remove(user.getIdLong());
  }

  public ImmutableList<Application> listApplications() {
    return ImmutableList.copyOf(applications.values());
  }

  public void updateApplication(User user, Application application) {
    applications.put(user.getIdLong(), application);

    Guild guild = user.getJDA().getGuildById(application.getGuildId());

    Optional<RoleInfo> roleInfo = getRoleInfo(application);

    RoleInfo role = roleInfo.get();
    int requiredVotes = role.getRequiredVotes();

    long approvals = application.getVotes().entrySet().stream().filter(Entry::getValue).count();
    if (requiredVotes == approvals) {
      if (approvals >= role.getRequiredApprovals()) {
        guild.addRoleToMember(user.getIdLong(), guild.getRoleById(application.getRoleId())).queue();
        user.openPrivateChannel()
            .queue(privateChannel -> privateChannel.sendMessage("You promoto yay").queue());
      } else {
        user.openPrivateChannel()
            .queue(privateChannel -> privateChannel.sendMessage("You no promoto boo").queue());
      }
      guild.getTextChannelsByName(String.format(CHANNEL_FORMAT_STRING, user.getIdLong()), true)
          .get(0).delete().queue();
      applications.remove(user.getIdLong());
    }
  }

  public void submitApplication(User user, Application application) {
    checkArgument(application.getStatus() == Status.COMPLETED);
    applications.put(user.getIdLong(), application);

    Guild guild = user.getJDA().getGuildById(application.getGuildId());

    guild.getCategoryById(channelConfig.getApplicationCategoryId())
        .createTextChannel(String.format(CHANNEL_FORMAT_STRING, application.getApplicantId()))
        .queue(textChannel -> {

          ImmutableSet<Long> approvalRoles = roleFetcher.getRoleInfo(application.getRoleId()).get()
              .getApprovalRoles();

          ImmutableSet<Long> discusserRoles = roleFetcher.getRoleInfo(application.getRoleId()).get()
              .getDiscusserRoles();

          approvalRoles.forEach(roleId -> {
            textChannel.getManager()
                .putPermissionOverride(guild.getRoleById(roleId),
                    ImmutableSet.of(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY),
                    ImmutableSet.of()).queue();
          });

          discusserRoles.forEach(roleId -> {
            textChannel.getManager()
                .putPermissionOverride(guild.getRoleById(roleId),
                    ImmutableSet.of(Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY),
                    ImmutableSet.of(Permission.MESSAGE_ADD_REACTION)).queue();
          });

          String approverMentions = approvalRoles.stream().map(guild::getRoleById)
              .filter(Objects::nonNull).map(
                  IMentionable::getAsMention).collect(Collectors.joining(", "));

          String discusserMentions = approvalRoles.stream().map(guild::getRoleById)
              .filter(Objects::nonNull).map(Role::getName).collect(Collectors.joining(", "));

          textChannel
              .sendMessage(
                  new MessageBuilder()
                      .append("Approvers: ")
                      .append(approverMentions)
                      .append('\n')
                      .append("Viewers: ")
                      .append(discusserMentions).build())
              .queue(message -> {
                textChannel
                    .sendMessage(applicationEmbedFactory.createEmbedForApplication(application))
                    .queue();
                message.addReaction("U+1F44D").queue();
                message.addReaction("U+1F44E").queue();
              });
        });
  }

  private Optional<RoleInfo> getRoleInfo(Application application) {
    return roleFetcher.getRoleInfo(application.getRoleId());
  }
}
