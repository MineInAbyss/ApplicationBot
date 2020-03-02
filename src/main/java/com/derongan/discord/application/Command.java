package com.derongan.discord.application;

import net.dv8tion.jda.api.entities.Message;

public interface Command {

  void handleInput(Message input);
}
