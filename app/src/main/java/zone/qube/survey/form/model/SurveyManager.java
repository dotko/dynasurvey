package zone.qube.survey.form.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import zone.qube.survey.form.model.response.Survey;

import static zone.qube.survey.form.model.response.Survey.SPLIT_SEPARATOR;

public class SurveyManager extends Observable {

    private final Survey.Result mSurveyResult;
    private final Survey mSurvey;
    private StateMachine mStateMachine;

    public SurveyManager(@NonNull final Survey survey) {
        mSurvey = survey;
        mSurveyResult = new Survey.Result(survey);
        initResult();
        mStateMachine = new StateMachine(this) {
        };
    }

    private void initResult() {
        for (HashMap.Entry<String, Survey.Question> entry : getSurvey().getQuestions().entrySet()) {
            switch (entry.getValue().getType()) {
                case TEXT:
                case CURRENCY:
                case NUMBER:
                case SELECT:
                case MULTISELECT:
                    setValue(entry.getKey(), entry.getValue().getValue());
                    break;
            }

        }

    }

    @NonNull
    public Survey getSurvey() {
        return mSurvey;
    }

    @NonNull
    public Survey.Result getSurveyResult() {
        return mSurveyResult;
    }

    public StateMachine getStateMachine() {
        return mStateMachine;
    }

    public boolean isRuleValid(@NonNull final Survey.Rule rule) {
        final String value = getValue(rule.getSubjectId());
        final String[] checkIds = rule.getCheckIds();
        if (checkIds == null) {
            return true;
        }
        for (String checkId : checkIds) {
            try {
                if (!getSurvey().getChecks().get(checkId).isValid(value)) {
                    return false;
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
                return true; // Validating missing checks is logged but valid
            }
        }
        return true;
    }


    private boolean areRulesValid(@Nullable final String[] ruleIds) {
        if (ruleIds == null) {
            return true; // No rules are valid rules
        }
        for (String ruleId : ruleIds) {
            if (!isRuleValid(getSurvey().getRules().get(ruleId))) {
                return false;
            }
        }
        return true;

    }

    public void setValue(@NonNull final String key, @Nullable final String value) {
        if (value == null) {
            getResponses().remove(key);
            return;
        }
        if (value.equals(getValue(key))) {
            return;
        }
        getResponses().addProperty(key, value);
        setChanged();
        notifyObservers();
    }

    @Nullable
    public String getValue(@NonNull final String key) {
        final JsonElement jsonElement = getResponses().get(key);
        return jsonElement == null ? null : jsonElement.getAsString();
    }

    @NonNull
    public JsonObject getResponses() {
        return getSurveyResult().getResponses();
    }

    @Nullable
    public String[] getValues(@NonNull final String key) {
        final String value = getValue(key);
        return value == null ? null : value.split(SPLIT_SEPARATOR);
    }


    public static abstract class StateMachine extends Observable {

        public static final String STATE_ALPHA = "|STATE:ALPHA|";
        public static final String STATE_OMEGA = "|STATE:OMEGA|";


        private SurveyManager mSurveyManager;
        private ArrayList<String> mPath;

        public StateMachine(final SurveyManager surveyManager) {
            mSurveyManager = surveyManager;
            mPath = new ArrayList<>();
            mPath.add(STATE_ALPHA);
            mPath.add(mSurveyManager.getSurvey().getStartScreenId());
        }

        public String stepForth() {
            final Survey.Transition transition = mSurveyManager
                    .getSurvey()
                    .getTransitions()
                    .get(getCurrentState());
            final Survey.Transition.Destination[] destinations;
            if (transition == null) {
                destinations = null; // If this state(screen) doesn't have a transition it is a leaf
            } else {
                destinations = transition.getDestinations();
            }
            if (destinations != null) {
                for (Survey.Transition.Destination destination : destinations) {
                    final boolean valid = mSurveyManager.areRulesValid(destination.getRuleIds());
                    if (valid) {
                        mPath.add(destination.getTarget());
                        setChanged();
                        notifyObservers();
                        return getCurrentState();
                    }
                }
            }
            mPath.add(STATE_OMEGA);
            setChanged();
            notifyObservers();
            return getCurrentState();
        }

        public String stepBack() {
            if (mPath.size() > 1) {
                mPath.remove(mPath.size() - 1);
            }
            setChanged();
            notifyObservers();
            return getCurrentState();
        }


        public String getCurrentState() {
            if (mPath.isEmpty()) {
                return STATE_ALPHA;
            }
            return mPath.get(mPath.size() - 1);
        }

    }
}
