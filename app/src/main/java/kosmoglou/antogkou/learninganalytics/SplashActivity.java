package kosmoglou.antogkou.learninganalytics;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

// ============= THIS ACTIVITY SERVES AS SPLASH SCREEN ACTIVITY WHEN APP IS OPNENED ============= \\

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000; // 4 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //TODO

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                // after 4 seconds user is taken to MainActivity
                Intent menuIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(menuIntent);
                overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
