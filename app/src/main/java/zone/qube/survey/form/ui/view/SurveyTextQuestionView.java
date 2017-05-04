package zone.qube.survey.form.ui.view;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import zone.qube.survey.R;
import zone.qube.survey.form.viewmodel.SurveyQuestionViewModel;

public class SurveyTextQuestionView extends AbstractSurveyQuestionView {
    public SurveyTextQuestionView(
            final Context context,
            final SurveyQuestionViewModel surveyQuestionViewModel
    ) {
        super(context, surveyQuestionViewModel);
    }


    @BindView(R.id.survey_text_question_title)
    TextView vTitle;
    @BindView(R.id.survey_text_question_value)
    TextInputEditText vValue;


    @Override
    protected void init(final AttributeSet attributeSet, final int defStyleAttr) {
        inflate(getContext(), R.layout.layout_survey_text_question, this);
        ButterKnife.bind(this);
        vValue.setText(getQuestionVM().getValue());
        getQuestionVM().getSurveyManager().addObserver(this);
        updateUi();
    }

    protected void updateUi() {
        vTitle.setText(getQuestionVM().getTitle());
        vValue.setError(getQuestionVM().isValid() ? null : getQuestionVM().getErrorMessage());
        final String currentValue = vValue.getText() == null ? "" : vValue.getText().toString();
        final String newValue = getQuestionVM().getValue();
        if (!currentValue.equals(newValue)) {
            vValue.setText(newValue);
        }
    }

    @OnTextChanged(R.id.survey_text_question_value)
    void onTextChanged(CharSequence ch) {
        getQuestionVM().updateQuestionResponse(ch.toString());
    }

}
