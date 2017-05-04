package zone.qube.survey.form.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zone.qube.survey.R;
import zone.qube.survey.form.viewmodel.SurveyQuestionViewModel;
import zone.qube.survey.form.viewmodel.SurveyScreenViewModel;

public class SurveyScreenView extends AbstractSurveyElementView {

    @BindView(R.id.survey_screen_title)
    TextView mTitle;
    @BindView(R.id.survey_screen_text)
    TextView mText;
    @BindView(R.id.survey_screen_next)
    Button mNext;
    @BindView(R.id.survey_screen_questions_container)
    LinearLayout mQuestionContainer;
    private OnForthPressedListener mOnNextListener;

    public SurveyScreenView(
            final Context context,
            final SurveyScreenViewModel surveyScreenViewModel
    ) {
        super(context, surveyScreenViewModel);
    }


    protected void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.layout_survey_screen, this);
        ButterKnife.bind(this);
        getSurveyScreenViewModel().getSurveyManager().addObserver(this);
        mQuestionContainer.removeAllViews();
        final SurveyQuestionViewModel[] questionVMs = getSurveyScreenViewModel().getQuestions();
        if (questionVMs != null) {
            for (SurveyQuestionViewModel eQVM : questionVMs) {
                AbstractSurveyQuestionView tv = AbstractSurveyQuestionView.from(getContext(), eQVM);
                mQuestionContainer.addView(tv);
            }
        }
        updateUi();
    }

    protected void updateUi() {
        mTitle.setText(getSurveyScreenViewModel().getTitle());
        mText.setText(getSurveyScreenViewModel().getText());
        mNext.setEnabled(getSurveyScreenViewModel().isValid());
    }

    private SurveyScreenViewModel getSurveyScreenViewModel() {
        return (SurveyScreenViewModel) getSurveyElementViewModel();
    }

    @OnClick(R.id.survey_screen_next)
    public void onClick() {
        if (mOnNextListener == null) {
            return;
        }
        mOnNextListener.onForthPressed();
    }

    public void setOnForthPressedListener(final OnForthPressedListener onForthPressedListener) {
        mOnNextListener = onForthPressedListener;
    }

    public interface OnForthPressedListener {
        void onForthPressed();
    }
}
