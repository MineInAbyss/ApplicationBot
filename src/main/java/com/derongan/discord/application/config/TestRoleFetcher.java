package com.derongan.discord.application.config;

import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;

public class TestRoleFetcher implements RoleFetcher {

  private final Map<Long, RoleInfo> rolesById;

  @Inject
  public TestRoleFetcher(Map<Long, RoleInfo> rolesById) {
    this.rolesById = rolesById;
  }

  @Override
  public Optional<RoleInfo> getRoleInfo(Long roleId) {
    return Optional.ofNullable(rolesById.get(roleId));
  }

  @Override
  public Set<RoleInfo> getAvailableRoleInfo() {
    return ImmutableSet.copyOf(rolesById.values());
  }
}
