package com.derongan.discord.application.config;

import java.util.Optional;
import java.util.Set;

public interface RoleFetcher {

  Optional<RoleInfo> getRoleInfo(Long roleId);

  Set<RoleInfo> getAvailableRoleInfo();
}
