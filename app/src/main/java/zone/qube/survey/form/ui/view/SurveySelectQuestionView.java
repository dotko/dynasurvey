package zone.qube.survey.form.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import zone.qube.survey.R;
import zone.qube.survey.form.model.response.Survey;
import zone.qube.survey.form.viewmodel.SurveyQuestionViewModel;

import static zone.qube.survey.form.model.response.Survey.SEPARATOR;

class SurveySelectQuestionView extends AbstractSurveyQuestionView
        implements CompoundButton.OnCheckedChangeListener {

    private HashMap<RadioButton, Survey.Question.Option> mRadioToOptionMap = new HashMap<>();

    @BindView(R.id.survey_select_question_title)
    TextView vTitle;
    @BindView(R.id.survey_select_question_radiogroup)
    RadioGroup vRadioGroup;
    @BindView(R.id.survey_select_question_error)
    TextView vError;

    public SurveySelectQuestionView(
            final Context context,
            final SurveyQuestionViewModel surveyQuestionViewModel
    ) {
        super(context, surveyQuestionViewModel);
    }

    @Override
    protected void init(final AttributeSet attributeSet, final int defStyleAttr) {
        inflate(getContext(), R.layout.layout_survey_select_question, this);
        ButterKnife.bind(this);
        initUi();
        getQuestionVM().getSurveyManager().addObserver(this);
        updateUi();
    }

    private void initUi() {
        vRadioGroup.removeAllViews();
        for (Survey.Question.Option option : getQuestionVM().getOptions()) {
            CompoundButton compoundButton;
            switch (getQuestionVM().getQuestion().getType()) {
                case SELECT:
                    compoundButton = new RadioButton(getContext());
                    break;
                case MULTISELECT:
                default:
                    compoundButton = new CheckBox(getContext());
            }
            compoundButton.setOnCheckedChangeListener(this);
            compoundButton.setTag(option);
            compoundButton.setText(getQuestionVM().getTitle());
            vRadioGroup.addView(compoundButton);
        }
    }

    protected void updateUi() {
        vTitle.setText(getQuestionVM().getTitle());
        for (int i = 0; i < vRadioGroup.getChildCount(); i++) {
            final CompoundButton compoundButton = (CompoundButton) vRadioGroup.getChildAt(i);
            final Survey.Question.Option option = (Survey.Question.Option) compoundButton.getTag();
            compoundButton.setText(getQuestionVM().fillTemplateString(option.getTitle()));
            compoundButton.setChecked(getQuestionVM().isSelected(option.getId()));
        }
        vError.setText(getQuestionVM().getErrorMessage());
        vError.setVisibility(getQuestionVM().isValid() ? GONE : VISIBLE);
    }

    @Override
    public void onCheckedChanged(final CompoundButton _, final boolean __) {
        getQuestionVM().updateQuestionResponse(getValue());
    }

    private String getValue() {
        ArrayList<String> selectedIds = new ArrayList<>();
        for (int i = 0; i < vRadioGroup.getChildCount(); i++) {
            final CompoundButton compoundButton = (CompoundButton) vRadioGroup.getChildAt(i);
            if (compoundButton.isChecked()) {
                final Survey.Question.Option option = (Survey.Question.Option) compoundButton.getTag();
                selectedIds.add(option.getId());
            }
        }
        return selectedIds.isEmpty() ? null : android.text.TextUtils.join(SEPARATOR, selectedIds);
    }

}
