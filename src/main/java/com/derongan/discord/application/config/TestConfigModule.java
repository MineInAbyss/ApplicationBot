package com.derongan.discord.application.config;

import com.derongan.discord.application.Questionnaire;
import com.google.common.collect.ImmutableSet;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.LongKey;

@Module
public abstract class TestConfigModule {

  @Binds
  abstract ChannelConfig providesChannelConfig(TestChannelConfig testChannelConfig);

  @Binds
  abstract RoleFetcher roleFetcher(TestRoleFetcher testRoleFetcher);

  @Provides
  @IntoMap
  @LongKey(662177352377171978L)
  static RoleInfo provideB() {
    return RoleInfo.builder().setRoleId(662177352377171978L)
        .setApprovalRoles(ImmutableSet.of(662177469146595350L))
        .setRequiredVotes(2)
        .setRequiredApprovals(1)
        .setQuestions(Questionnaire.builder()
            .addQuestion("What time is it?")
            .addQuestion("Why should I trust you?")
            .addQuestion("How much money will you give me?")
            .build())
        .build();
  }
}
