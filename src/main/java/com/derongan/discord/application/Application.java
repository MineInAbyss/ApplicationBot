package com.derongan.discord.application;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

@AutoValue
public abstract class Application {

  public abstract long getApplicantId();

  public abstract long getRoleId();

  public abstract long getGuildId();

  public abstract Status getStatus();

  public abstract ImmutableMap<String, String> getResponses();

  public abstract ImmutableMap<Long, Boolean> getVotes();

  public static Builder builder() {
    return new AutoValue_Application.Builder();
  }

  public abstract Builder toBuilder();


  @AutoValue.Builder
  public abstract static class Builder {

    public abstract ImmutableMap.Builder<String, String> responsesBuilder();

    public abstract ImmutableMap.Builder<Long, Boolean> votesBuilder();

    public abstract Builder setApplicantId(long newApplicantId);

    public abstract Builder setRoleId(long newRoleId);

    public abstract Builder setGuildId(long guildId);

    public Builder addApproval(long userId) {
      votesBuilder().put(userId, true);
      return this;
    }

    public Builder addRejection(long userId) {
      votesBuilder().put(userId, false);
      return this;
    }

    public Builder addResponse(String question, String response) {
      responsesBuilder().put(question, response);
      return this;
    }

    public abstract Builder setStatus(Status status);

    public abstract Application build();
  }
}
