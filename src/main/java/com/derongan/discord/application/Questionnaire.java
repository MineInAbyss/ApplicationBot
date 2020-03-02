package com.derongan.discord.application;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Questionnaire {

  public abstract ImmutableList<String> getQuestions();

  public static Builder builder() {
    return new AutoValue_Questionnaire.Builder();
  }
  
  @AutoValue.Builder
  public abstract static class Builder {

    abstract ImmutableList.Builder<String> questionsBuilder();

    public Builder addQuestion(String question) {
      questionsBuilder().add(question);
      return this;
    }

    public abstract Questionnaire build();
  }
}
