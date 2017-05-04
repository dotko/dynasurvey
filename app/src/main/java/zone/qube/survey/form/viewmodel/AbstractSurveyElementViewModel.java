package zone.qube.survey.form.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zone.qube.survey.form.model.SurveyManager;
import zone.qube.survey.form.model.response.Survey;

public abstract class AbstractSurveyElementViewModel {

    private static final String PLACEHOLDER_REGEX = "\\$\\{(.+?)\\}";

    private final String mElementId;
    private final SurveyManager mSurveyManager;

    public AbstractSurveyElementViewModel(
            @NonNull String elementId,
            @NonNull SurveyManager surveyManager
    ) {
        mElementId = elementId;
        mSurveyManager = surveyManager;
    }

    /**
     * Override this method to provide your own validation
     *
     * @return true - by default
     */
    public boolean isValid() {
        return true;
    }

    public String getKey() {
        return mElementId;
    }

    public SurveyManager getSurveyManager() {
        return mSurveyManager;
    }

    public Survey getSurvey() {
        return mSurveyManager.getSurvey();
    }

    public Survey.Result getResult() {
        return mSurveyManager.getSurveyResult();
    }


    @Nullable
    public String fillTemplateString(@Nullable final String input) {
        if (input == null) {
            return null;
        }
        String result = input;
        Matcher m = Pattern.compile(PLACEHOLDER_REGEX).matcher(result);
        while (m.find()) {
            final String template = m.group(0);
            final String value = getSurveyManager().getValue(m.group(1));
            if (template == null || value == null) {
                continue;
            }
            result = result.replace(template, value);
            m = Pattern.compile(PLACEHOLDER_REGEX).matcher(result);
        }
        return result;
    }

    boolean isRuleValid(@NonNull final Survey.Rule rule) {
        final String value = getSurveyManager().getValue(rule.getSubjectId());
        if (rule.getCheckIds() == null) {
            return true;
        }
        for (String checkId : rule.getCheckIds()) {
            if (!getSurvey().getChecks().get(checkId).isValid(value)) {
                return false;
            }
        }
        return true;
    }
}
