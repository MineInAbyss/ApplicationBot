package com.derongan.discord.application.config;

import javax.inject.Inject;

public class TestChannelConfig implements ChannelConfig {
  @Inject
  public TestChannelConfig() {
  }

  @Override
  public long getApplicationChannelId() {
    return 660981097076621319L;
  }

  @Override
  public long getApplicationCategoryId() {
    return 662097888930693165L;
  }
}
