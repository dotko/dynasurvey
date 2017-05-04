package zone.qube.survey.form.ui.view;

import android.content.Context;

import zone.qube.survey.form.viewmodel.SurveyQuestionViewModel;

public abstract class AbstractSurveyQuestionView extends AbstractSurveyElementView {

    public AbstractSurveyQuestionView(
            final Context context,
            final SurveyQuestionViewModel surveyQuestionViewModel
    ) {
        super(context, surveyQuestionViewModel);
    }

    public SurveyQuestionViewModel getQuestionVM() {
        return (SurveyQuestionViewModel) getSurveyElementViewModel();
    }


    public static AbstractSurveyQuestionView from(
            final Context context,
            final SurveyQuestionViewModel surveyQuestionViewModel
    ) {
        switch (surveyQuestionViewModel.getType()) {
            case MULTISELECT:
            case SELECT:
                return new SurveySelectQuestionView(context, surveyQuestionViewModel);
            case NUMBER:
                return new SurveyNumberQuestionView(context, surveyQuestionViewModel);
            case CURRENCY:
                return new SurveyCurrencyQuestionView(context, surveyQuestionViewModel);
            case TEXT:
            default:
                return new SurveyTextQuestionView(context, surveyQuestionViewModel);
        }
    }

}
