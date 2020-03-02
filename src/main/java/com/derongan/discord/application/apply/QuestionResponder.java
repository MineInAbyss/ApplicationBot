package com.derongan.discord.application.apply;

import com.derongan.discord.application.Application;
import com.derongan.discord.application.Application.Builder;
import com.derongan.discord.application.ApplicationTracker;
import com.derongan.discord.application.Command;
import com.derongan.discord.application.Questionnaire;
import com.derongan.discord.application.Status;
import com.derongan.discord.application.config.RoleFetcher;
import com.google.common.collect.ImmutableList;
import java.util.Optional;
import javax.inject.Inject;
import net.dv8tion.jda.api.entities.Message;

public class QuestionResponder implements Command {

  private final ApplicationTracker applicationTracker;
  private final RoleFetcher roleFetcher;

  @Inject
  public QuestionResponder(ApplicationTracker applicationTracker,
      RoleFetcher roleFetcher) {
    this.applicationTracker = applicationTracker;
    this.roleFetcher = roleFetcher;
  }

  @Override
  public void handleInput(Message input) {
    Optional<Application> application = applicationTracker.getApplicationForUser(input.getAuthor());

    application.ifPresent(a -> proceed(a, input));
  }

  public void proceed(Application application, Message input) {
    if (application.getStatus() == Status.STARTED) {
      Questionnaire questionnaire = roleFetcher.getRoleInfo(application.getRoleId()).get()
          .getQuestions();
      ImmutableList<String> questions = questionnaire.getQuestions();

      Builder newApp = application.toBuilder()
          .addResponse(questions.get(application.getResponses().size()), input.getContentRaw());

      // Done
      if (application.getResponses().size() + 1 == questions.size()) {
        newApp.setStatus(Status.COMPLETED);
        input.getChannel().sendMessage("Thank you for applying!").queue();
        applicationTracker.submitApplication(input.getAuthor(), newApp.build());
      } else {
        String question = questions.get(application.getResponses().size() + 1);
        input.getChannel().sendMessage(question).queue();
        applicationTracker.updateApplication(input.getAuthor(), newApp.build());
      }
    }
  }
}
