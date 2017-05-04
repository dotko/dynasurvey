package zone.qube.survey.form.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.Observable;
import java.util.Observer;

import zone.qube.survey.R;
import zone.qube.survey.form.model.SurveyManager;
import zone.qube.survey.form.model.response.Survey;
import zone.qube.survey.form.ui.view.SurveyScreenView;
import zone.qube.survey.form.viewmodel.SurveyScreenViewModel;

public class SurveyActivity extends AppCompatActivity
        implements Observer,
        SurveyScreenView.OnForthPressedListener {

    public static final String KEY_SURVEY = "key:survey";
    public static final String KEY_RESULT = "key:result";
    private SurveyManager mSurveyManager;
    private SurveyScreenView mScreen;

    private FrameLayout mScreenContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        mScreenContainer = (FrameLayout) findViewById(R.id.survey_activity_screen_container);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Toast.makeText(this, "Empty bundle", Toast.LENGTH_LONG).show();
            finish();
        }
        final Survey survey = (Survey) (bundle != null ? bundle.getSerializable(KEY_SURVEY) : null);
        if (survey != null) {
            mSurveyManager = new SurveyManager(survey);
            mSurveyManager.getStateMachine().addObserver(this);
            init();

        } else {
            Toast.makeText(this, "Null Survey", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void init() {
        showScreen(mSurveyManager.getStateMachine().getCurrentState());
    }

    private void showScreen(final String screenId) {
        if (mScreen != null) {
            mScreen.setOnForthPressedListener(null);
            mScreen.discard();
        }
        mScreen = new SurveyScreenView(this, new SurveyScreenViewModel(screenId, mSurveyManager));
        mScreen.setOnForthPressedListener(this);
        mScreenContainer.removeAllViews();
        mScreenContainer.addView(mScreen);

    }


    @Override
    public void onForthPressed() {
        mSurveyManager.getStateMachine().stepForth();
    }

    @Override
    public void onBackPressed() {
        mSurveyManager.getStateMachine().stepBack();
    }

    @Override
    public void update(final Observable o, final Object arg) {
        final String newState = mSurveyManager.getStateMachine().getCurrentState();
        switch (newState) {
            case SurveyManager.StateMachine.STATE_ALPHA:
                prepareForFinish();
                finish();
                break;
            case SurveyManager.StateMachine.STATE_OMEGA:
                prepareForFinish();
                finish();
                break;
            default:
                showScreen(newState);
        }
    }

    private void prepareForFinish() {
        Intent output = new Intent();
        final String resultStr = new GsonBuilder().setPrettyPrinting().create().toJson(
                mSurveyManager.getSurveyResult(),
                Survey.Result.class
        );
        output.putExtra(KEY_RESULT, resultStr);
        setResult(RESULT_OK, output);
    }

}
