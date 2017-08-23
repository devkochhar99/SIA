package sia.dev.com.sia;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash_screen extends AppCompatActivity {

    private boolean backbtnPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //code that displays the content in full screen mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {      //Handler Used to communicate between the UI and Background thread
//handler is used to wait for specific time and once the timer is out we launched main activity.
            @Override
            public void run() {
                finish();
                if(!backbtnPress){
                    Intent i = new Intent(Splash_screen.this, MainActivity.class);
                    Splash_screen.this.startActivity(i);
                }
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        backbtnPress = true;
        super.onBackPressed();
    }
}