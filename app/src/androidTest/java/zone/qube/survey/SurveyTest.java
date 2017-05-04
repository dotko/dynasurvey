package zone.qube.survey;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import zone.qube.survey.form.model.response.Survey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SurveyTest {

    Survey mSurvey;

    @Before
    public void loadSurvey() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        try {
            Resources res = appContext.getResources();
            InputStream in_s = res.openRawResource(R.raw.survey_response);

            byte[] byteArray = new byte[in_s.available()];
            in_s.read(byteArray);
            mSurvey = Survey.SurveyWrapper.fromJson(new String(byteArray)).getSurvey();
        } catch (Exception e) {
            mSurvey = null;
        }
        assertNotNull(mSurvey);
    }


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("zone.qube.survey", appContext.getPackageName());
    }

    @Test
    public void sanityCheck() {
        assertTrue("This should fail", false);
    }

    @Test
    public void hasScreens() throws Exception {
        assertEquals(mSurvey.getScreens().size(), 8);
        System.out.println(mSurvey.getScreens().keySet());
    }
}
