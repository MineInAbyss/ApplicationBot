package com.derongan.discord.application;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.dv8tion.jda.api.entities.Message;

@AutoValue
public abstract class CommandValidator {

  abstract ImmutableList<ArgumentValidator> getArgumentValidators();

  public static Builder builder() {
    return new AutoValue_CommandValidator.Builder();
  }

  public List<String> validate(Message message) {
    List<String> arguments = ImmutableList.copyOf(message.getContentStripped().split(" "));
    // First argument is always the command

    for (int i = 0; i < getArgumentValidators().size(); i++) {
      ArgumentValidator validator = getArgumentValidators().get(i);

      if (i + 2 > arguments.size()) {
        throw new IllegalArgumentException("Missing arguments");
      }
      validator.validate(arguments.get(i + 1));
    }

    return arguments.subList(1, arguments.size());
  }

  @AutoValue.Builder
  public abstract static class Builder {

    abstract ImmutableList.Builder<ArgumentValidator> argumentValidatorsBuilder();

    public Builder addArgumentValidator(ArgumentValidator argumentValidator) {
      argumentValidatorsBuilder().add(argumentValidator);
      return this;
    }

    public abstract CommandValidator build();
  }

  public interface ArgumentValidator {
    void validate(String argument);
  }
}
