package zone.qube.survey.form.model.response;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

public class Survey implements Serializable {

    public static final String SPLIT_SEPARATOR = "\\|";
    public static final String SEPARATOR = "|";

    @SerializedName("id")
    private String mId;
    @SerializedName("start_screen")
    private String mStartScreenId;
    @SerializedName("screens")
    private HashMap<String, Screen> mScreens;
    @SerializedName("questions")
    private HashMap<String, Question> mQuestions;
    @SerializedName("checks")
    private HashMap<String, Check> mChecks;
    @SerializedName("rules")
    private HashMap<String, Rule> mRules;
    @SerializedName("transitions")
    private HashMap<String, Transition> mTransitions;

    public String getId() {
        return mId;
    }

    public String getStartScreenId() {
        return mStartScreenId;
    }

    public HashMap<String, Screen> getScreens() {
        return mScreens;
    }

    public HashMap<String, Question> getQuestions() {
        return mQuestions;
    }

    public HashMap<String, Check> getChecks() {
        return mChecks;
    }

    public HashMap<String, Rule> getRules() {
        return mRules;
    }

    public HashMap<String, Transition> getTransitions() {
        return mTransitions;
    }

    public static class Screen implements Serializable {

        @SerializedName("title")
        private String mTitle;
        @SerializedName("text")
        private String mText;
        @SerializedName("questions")
        private String[] mQuestionIds;

        public String getTitle() {
            return mTitle;
        }

        public String getText() {
            return mText;
        }

        public String[] getQuestionIds() {
            return mQuestionIds;
        }
    }

    public static class Question implements Serializable {
        @SerializedName("title")
        private String mTitle;
        @SerializedName("text")
        private String mText;
        @SerializedName("type")
        private Type mType;
        @SerializedName("value")
        private String mValue;
        @SerializedName("options")
        private Option[] mOptions;
        @SerializedName("validation_rules")
        private String[] mValidationRuleIds;

        public String getTitle() {
            return mTitle;
        }

        public String getText() {
            return mText;
        }

        public Type getType() {
            return mType;
        }

        public String getValue() {
            return mValue;
        }

        public Option[] getOptions() {
            return mOptions;
        }

        public String[] getValidationRuleIds() {
            return mValidationRuleIds;
        }

        public enum Type implements Serializable {
            @SerializedName("TEXT")
            TEXT("TEXT"),
            @SerializedName("NUMBER")
            NUMBER("NUMBER"),
            @SerializedName("CURRENCY")
            CURRENCY("CURRENCY"),
            @SerializedName("SELECT")
            SELECT("SELECT"),
            @SerializedName("MULTISELECT")
            MULTISELECT("MULTISELECT");

            private final String mStrValue;

            Type(final String value) {
                mStrValue = value;
            }
        }

        public static class Option implements Serializable {
            @SerializedName("id")
            private String mId;
            @SerializedName("title")
            private String mTitle;

            public String getId() {
                return mId;
            }

            public String getTitle() {
                return mTitle;
            }
        }

    }

    public static class Check implements Serializable {
        @SerializedName("type")
        private Type mType;
        @SerializedName("check_value")
        private String mCheckValue;

        public Type getType() {
            return mType;
        }

        public String getCheckValue() {
            return mCheckValue;
        }

        public boolean isValid(@Nullable final String value) {
            if (value == null) {
                return false;
            }
            try {
                switch (mType) {
                    case SEL_COUNT_EQ:
                        return value.split(SPLIT_SEPARATOR).length == Integer.parseInt(getCheckValue());
                    case SEL_COUNT_GT:
                        return value.split(SPLIT_SEPARATOR).length > Integer.parseInt(getCheckValue());
                    case SEL_COUNT_LT:
                        return value.split(SPLIT_SEPARATOR).length < Integer.parseInt(getCheckValue());
                    case SEL_HAS:
                        return value.contains(getCheckValue());
                    case SEL_HAS_NOT:
                        return !value.contains(getCheckValue());
                    case TXT_LEN_GT:
                        return value.length() > Integer.parseInt(getCheckValue());
                    case TXT_LEN_LT:
                        return value.length() < Integer.parseInt(getCheckValue());
                    case NUM_GT:
                        return Double.parseDouble(value) > Double.parseDouble(getCheckValue());
                    case NUM_LT:
                        return Double.parseDouble(value) < Double.parseDouble(getCheckValue());
                    default:
                        return true; // Not null and unknown check is always valid
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // TODO: Deal with this
                return false;
            }
        }

        public enum Type implements Serializable {
            @SerializedName("SEL_COUNT_EQ")
            SEL_COUNT_EQ("SEL_COUNT_EQ"),
            @SerializedName("SEL_COUNT_GT")
            SEL_COUNT_GT("SEL_COUNT_GT"),
            @SerializedName("SEL_COUNT_LT")
            SEL_COUNT_LT("SEL_COUNT_LT"),
            @SerializedName("SEL_HAS")
            SEL_HAS("SEL_HAS"),
            @SerializedName("SEL_HAS_NOT")
            SEL_HAS_NOT("SEL_HAS_NOT"),
            @SerializedName("TXT_LEN_GT")
            TXT_LEN_GT("TXT_LEN_GT"),
            @SerializedName("TXT_LEN_LT")
            TXT_LEN_LT("TXT_LEN_LT"),
            @SerializedName("NUM_GT")
            NUM_GT("NUM_GT"),
            @SerializedName("NUM_LT")
            NUM_LT("NUM_LT");

            private final String mStrValue;

            Type(final String value) {
                mStrValue = value;
            }

        }

    }

    public static class Rule implements Serializable {
        @SerializedName("subject")
        private String mSubject;
        @SerializedName("checks")
        private String[] mChecks;
        @SerializedName("err_msg")
        private String mErrorMessage;

        public String getSubjectId() {
            return mSubject;
        }

        public String[] getCheckIds() {
            return mChecks;
        }

        public String getErrorMessage() {
            return mErrorMessage;
        }

    }

    public static class Transition implements Serializable {

        @SerializedName("destinations")
        private Destination[] mDestinations;

        public Destination[] getDestinations() {
            return mDestinations;
        }

        public static class Destination implements Serializable {
            @SerializedName("target")
            private String mTarget;
            @SerializedName("rules")
            private String[] mRules;

            public String getTarget() {
                return mTarget;
            }

            public String[] getRuleIds() {
                return mRules;
            }
        }
    }

    public static class SurveyWrapper implements Serializable {
        @SerializedName("survey")
        private Survey mSurvey;

        public Survey getSurvey() {
            return mSurvey;
        }

        public static SurveyWrapper fromJson(final String s) {
            return new Gson().fromJson(s, SurveyWrapper.class);
        }
    }

    public static class Result implements Serializable {

        @SerializedName("id")
        private String mId;
        @SerializedName("responses")
        private JsonObject mResponses;


        public Result(@NonNull final Survey survey) {
            mId = survey.getId();
            mResponses = new JsonObject();
        }

        @NonNull
        public JsonObject getResponses() {
            return mResponses;
        }

    }
}

