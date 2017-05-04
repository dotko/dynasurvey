package zone.qube.survey.form.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import butterknife.ButterKnife;
import zone.qube.survey.R;
import zone.qube.survey.form.viewmodel.SurveyQuestionViewModel;

public class SurveyNumberQuestionView extends SurveyTextQuestionView {
    public SurveyNumberQuestionView(
            final Context context,
            final SurveyQuestionViewModel surveyQuestionViewModel
    ) {
        super(context, surveyQuestionViewModel);
    }

    @Override
    protected void init(final AttributeSet attributeSet, final int defStyleAttr) {
        inflate(getContext(), R.layout.layout_survey_text_question, this);
        ButterKnife.bind(this);
        final String originalValue = getQuestionVM().getValue(); // Setting input type resets value
        vValue.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        vValue.setText(originalValue);
        getQuestionVM().getSurveyManager().addObserver(this);
        updateUi();
    }
}
