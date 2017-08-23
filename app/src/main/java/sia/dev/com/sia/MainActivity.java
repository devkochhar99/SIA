package sia.dev.com.sia;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String PREFS = "prefs";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String AS_NAME = "as_name";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextToSpeech tts;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation


        SpeechToText speechToText = new SpeechToText();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativeLayout_for_fragment, speechToText).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences(PREFS, 0);
        editor = preferences.edit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to exit?");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            speak("Ok!! See You soon!!");
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menuAbout){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Mentor - Ms. Swati Kurra\n\n Developers - \n\n1. Dev Kochhar\n2. Tinku Rani\n3. Ashima Garg\n4. Rahul Bhatia");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        if(id == R.id.menuShare) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Sharing facility will be available shortly";
            String shareSub = "Sharing facility will be available shortly";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            Toast.makeText(this,"Sharing facility will be available shortly",Toast.LENGTH_LONG).show();

        }

        if (id == R.id.menuExit) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to exit?");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        speak("Ok!! See You soon!!");
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        return true;
    }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_speechtotext) {

            SpeechToText speechToText = new SpeechToText();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left,R.anim.anim_slide_out_from_left).replace(R.id.relativeLayout_for_fragment, speechToText).commit();

        } else if (id == R.id.nav_texttospeech) {
            TextToSpeech1 textToSpeech1 = new TextToSpeech1();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left,R.anim.anim_slide_out_from_left).replace(R.id.relativeLayout_for_fragment,textToSpeech1).commit();

        }
       else if (id == R.id.nav_feedback) {
            SendFeedback feedback = new SendFeedback();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left,R.anim.anim_slide_out_from_left).replace(R.id.relativeLayout_for_fragment,feedback).commit();

        } else if (id == R.id.nav_help) {

            Help1 help1 = new Help1();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left,R.anim.anim_slide_out_from_left).replace(R.id.relativeLayout_for_fragment,help1).commit();

        } else if (id == R.id.nav_write) {
           Write write = new Write();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left,R.anim.anim_slide_out_from_left).replace(R.id.relativeLayout_for_fragment,write).commit();


        } else if (id == R.id.nav_exit) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to exit?");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            speak("Ok!! See You soon!!");
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
