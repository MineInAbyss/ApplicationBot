package com.derongan.discord.application.config;

import com.derongan.discord.application.Questionnaire;
import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class RoleInfo {

  public abstract long getRoleId();

  public abstract ImmutableSet<Long> getApprovalRoles();

  public abstract ImmutableSet<Long> getDiscusserRoles();

  public abstract Questionnaire getQuestions();

  public abstract int getRequiredVotes();

  public abstract int getRequiredApprovals();

  public static Builder builder() {
    return new AutoValue_RoleInfo.Builder().setDiscusserRoles(ImmutableSet.of());
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setRoleId(long roleId);

    public abstract Builder setApprovalRoles(ImmutableSet<Long> approvalRoles);

    public abstract Builder setDiscusserRoles(ImmutableSet<Long> discusserRoles);

    public abstract Builder setQuestions(Questionnaire getQuestions);

    public abstract Builder setRequiredVotes(int requiredVotes);

    public abstract Builder setRequiredApprovals(int requiredApprovals);

    abstract RoleInfo autoBuild();

    public RoleInfo build() {
      RoleInfo roleInfo = autoBuild();
      Preconditions.checkState(roleInfo.getRequiredApprovals() <= roleInfo.getRequiredVotes(),
          "rate over 1.0");

      return roleInfo;
    }
  }
}
