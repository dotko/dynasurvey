package zone.qube.survey.form.viewmodel;

import android.support.annotation.NonNull;

import java.util.Arrays;

import zone.qube.survey.form.model.SurveyManager;
import zone.qube.survey.form.model.response.Survey;

public class SurveyQuestionViewModel extends AbstractSurveyElementViewModel {

    public SurveyQuestionViewModel(
            @NonNull final String elementId,
            @NonNull final SurveyManager surveyManager
    ) {
        super(elementId, surveyManager);
    }

    public Survey.Question getQuestion() {
        return getSurvey().getQuestions().get(getKey());
    }

    public String getTitle() {
        final String title = getQuestion().getTitle();
        return fillTemplateString(title);
    }

    public String getText() {
        final String text = getQuestion().getText();
        return fillTemplateString(text);
    }

    public Survey.Question.Type getType() {
        return getQuestion().getType();
    }

    public String getValue() {
        return getSurveyManager().getValue(getKey());
    }

    public Survey.Question.Option[] getOptions() {
        return getQuestion().getOptions();
    }

    public void updateQuestionResponse(final String value) {
        getSurveyManager().setValue(getKey(), value);
    }

    @Override
    public boolean isValid() {
        String[] ruleIds = getQuestion().getValidationRuleIds();
        if (ruleIds == null) {
            return true;
        }
        for (String ruleId : ruleIds) {
            if (!isRuleValid(getSurvey().getRules().get(ruleId))) {
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage() {
        String[] ruleIds = getQuestion().getValidationRuleIds();
        if (ruleIds != null) {
            for (String ruleId : ruleIds) {
                final Survey.Rule rule = getSurvey().getRules().get(ruleId);
                if (!isRuleValid(rule)) {
                    return fillTemplateString(rule.getErrorMessage());
                }
            }
        }
        return null;
    }

    public boolean isSelected(final String optionId) {
        final String[] values = getSurveyManager().getValues(getKey());
        return values != null && Arrays.asList(values).contains(optionId);
    }
}
