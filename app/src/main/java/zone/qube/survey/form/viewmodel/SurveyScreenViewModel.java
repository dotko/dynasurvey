package zone.qube.survey.form.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import zone.qube.survey.form.model.SurveyManager;

public class SurveyScreenViewModel extends AbstractSurveyElementViewModel {

    public SurveyScreenViewModel(
            @NonNull final String elementId,
            @NonNull final SurveyManager surveyManager
    ) {
        super(elementId, surveyManager);
    }

    public String getTitle() {
        return fillTemplateString(getSurvey().getScreens().get(getKey()).getTitle());
    }

    public String getText() {
        return fillTemplateString(getSurvey().getScreens().get(getKey()).getText());
    }

    @Nullable
    public SurveyQuestionViewModel[] getQuestions() {
        final String[] questionIds = getSurvey().getScreens().get(getKey()).getQuestionIds();
        if (questionIds == null) {
            return null;
        }
        SurveyQuestionViewModel[] surveyQuestionViewModels =
                new SurveyQuestionViewModel[questionIds.length];
        for (int i = 0; i < questionIds.length; i++) {
            surveyQuestionViewModels[i] = new SurveyQuestionViewModel(
                    questionIds[i],
                    getSurveyManager()
            );
        }
        return surveyQuestionViewModels;
    }

    @Override
    public boolean isValid() {

        final SurveyQuestionViewModel[] surveyQuestionViewModels = getQuestions();
        if (surveyQuestionViewModels == null) {
            return true; // No questions are valid questions
        }
        for (SurveyQuestionViewModel surveyQuestionViewModel : surveyQuestionViewModels) {
            if (!surveyQuestionViewModel.isValid()) {
                return false;
            }
        }
        return true;
    }


}
