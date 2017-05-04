package zone.qube.survey;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zone.qube.survey.form.model.response.Survey;
import zone.qube.survey.form.ui.activity.SurveyActivity;

public class StartActivity extends AppCompatActivity {

    private static final int SURVEY_ACTIVITY_REQUEST = 1337;
    @BindView(R.id.toolbar)
    Toolbar vToolbar;
    @BindView(R.id.text)
    EditText vEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        setSupportActionBar(vToolbar);
        reset();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            reset();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SURVEY_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Sucessful survey.", Toast.LENGTH_SHORT).show();
                String resultStr = data.getStringExtra(SurveyActivity.KEY_RESULT);
                vEditText.setText(resultStr);
            } else {
                Toast.makeText(this, "Backed out of survey.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void reset() {
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.survey_response);
            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            vEditText.setText(new String(b));
        } catch (Exception e) {
            //TODO: deal with it
        }
    }

    public void startSurvey() {
        try {
            final Bundle bundle = new Bundle();
            bundle.putSerializable(
                    SurveyActivity.KEY_SURVEY,
                    Survey.SurveyWrapper.fromJson(vEditText.getText().toString()).getSurvey()
            );
            final Intent intent = new Intent(StartActivity.this, SurveyActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, SURVEY_ACTIVITY_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        startSurvey();
    }

}
