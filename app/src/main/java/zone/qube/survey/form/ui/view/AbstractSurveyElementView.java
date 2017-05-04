package zone.qube.survey.form.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.Observable;
import java.util.Observer;

import zone.qube.survey.form.viewmodel.AbstractSurveyElementViewModel;


public abstract class AbstractSurveyElementView extends FrameLayout implements Observer {

    private AbstractSurveyElementViewModel mSurveyElementViewModel;

    public AbstractSurveyElementView(
            final Context context,
            final AbstractSurveyElementViewModel surveyElementViewModel
    ) {
        super(context);
        mSurveyElementViewModel = surveyElementViewModel;
        setSaveEnabled(true);
        setId(hashCode());
        init(null, 0);
    }

    protected abstract void init(final AttributeSet attributeSet, final int defStyleAttr);

    protected abstract void updateUi();

    public AbstractSurveyElementViewModel getSurveyElementViewModel() {
        return mSurveyElementViewModel;
    }

    @Override
    public void update(final Observable o, final Object arg) {
        updateUi();
    }

    public void discard() {
        getSurveyElementViewModel().getSurveyManager().deleteObserver(this);
    }
}
