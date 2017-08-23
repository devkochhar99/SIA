package sia.dev.com.sia;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpeechToText extends Fragment {

    private TextToSpeech tts;
    private ImageButton b1;
    private ListView l1;
    private TextView t1,t2;
    private int x = 0;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String AS_NAME = "as_name";


    public SpeechToText() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_speech_to_text, container, false);
        t1 = (TextView) view.findViewById(R.id.textView2);
        b1 = (ImageButton) view.findViewById(R.id.imageButton);
        l1 = (ListView) view.findViewById(R.id.listView1);
        t2 = (TextView) view.findViewById(R.id.textView1);


        l1.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);

        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    if (preferences.getString(NAME, null) == null) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage("Pre-Requisites-\n1. App needs an active internet connection." +
                                "\n2. App needs Google Search(or Voice Search)\n" +
                                "3. Ensure that speaker volume is not set to mute.\n"+
                                "4. After clicking OK button, the app will ask your name. Please click on the mic button and tell your name." +
                                "The app will remember your name and will interact with you more friendly!! " +
                                "\n\n" +
                                "");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        speak("What is your name?");
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        speak("Welcome back, " + preferences.getString(NAME, null) + "How can i help you? Here are some of the things I can help");
                        l1.setVisibility(View.VISIBLE);
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.VISIBLE);

                    }
                }
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen();
            }
        });
        preferences = getActivity().getSharedPreferences(PREFS, 0);
        editor = preferences.edit();
        return view;
    }

    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void listen() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something now");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(), "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);
            }
        }
    }
    private void recognition(String text) {
        //creating an array which contains the words of the answer
        String[] speech = text.split(" ");

        if (text.contains("my name is")) {
            //the last word is our name
            String name = speech[speech.length - 1];

            //we got the name, we can put it in local storage and save changes
            editor.putString(NAME, name).apply();

            //make the app tell our name
            speak("Oh!! Hi " + preferences.getString(NAME, null) + "How can i help you? Here are some of the things I can help");
            l1.setVisibility(View.VISIBLE);
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);

        }
        else if (text.contains("what is my name")) {
            speak("Your name is  " + preferences.getString(NAME, null));
        }
        else if (text.contains("years") && text.contains("old")) {
            String age = speech[speech.length - 3];
            Log.e("THIS", "" + age);
            speak("Got it.");
            editor.putString(AGE, age).apply();
        }
        else if (text.contains("how old am I") || text.contains("what is my age")) {
            if (preferences.getString(AGE, null) == null) {
                speak("please first tell your age");
            } else {
                speak("You are " + preferences.getString(AGE, null) + " years old.");
            }
        }
        else if (text.contains("what is the time now")) {
            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
            Date now = new Date();
            String[] strDate = sdfDate.format(now).split(":");
            if (strDate[1].contains("00")) strDate[1] = "o'clock";
            speak("The time is " + sdfDate.format(now));
        }
        else if (text.contains("tell me a joke")) {
            if (x == 0) {
                speak("What's the difference between a trampoline and a zombie baby?             . . . . . .. " + "      " +
                        "I take off my shoes when I jump on a trampoline.");
                x = 1;
            } else if (x == 1) {
                speak("Two is company, three is crowd’.So what’s four and five? The Answer is Nine");
                x = 2;
            } else if (x == 2) {
                speak("What did the lonely banana say?I 'm a' kela");
                x = 3;
            } else if (x == 3) {

                speak(" Apparently the only way to calm down an astronaut is to give him\n" +
                        "                    some space.");
                x = 0;
            }
        } else if (text.contains("multiply")) {
            Double a, b, c;
            a = Double.parseDouble(speech[1]);
            b = Double.parseDouble(speech[3]);
            c = a * b;
            speak("Multiplication of " + a + "and " + b + " is " + c);
        } else if (text.contains("divide")) {
            Double a, b, c;
            a = Double.parseDouble(speech[1]);
            b = Double.parseDouble(speech[3]);
            c = a / b;
            speak("Division of " + a + "and " + b + " is " + c);
        } else if (text.contains("add")) {
            Double a, b, c;
            a = Double.parseDouble(speech[1]);
            b = Double.parseDouble(speech[3]);
            c = a + b;
            speak("Addition of " + a + "and " + b + " is " + c);

        } else if (text.contains("subtract")) {
            Double a, b, c;
            a = Double.parseDouble(speech[1]);
            b = Double.parseDouble(speech[3]);
            c = a - b;
            speak("Subtraction of " + a + "and " + b + " is " + c);
        } else if (text.contains("what is the date today")) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
            String strDate = mdformat.format(calendar.getTime());
            speak("The date is " + strDate);
        } else if (text.contains("call fire brigade") || text.contains("call fire department")) {
            speak("Calling fire brigade");
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "101", null)));
        }
        else if (text.contains("more options please")) {
            Help1 help1 = new Help1();
            android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativeLayout_for_fragment,help1).commit();

        }
        else if (text.contains("call police")) {
            speak("Calling police");
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "100", null)));
        }
        else if (text.contains("call ambulance")) {
            speak("Calling ambulance");
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "102", null)));
        }
        else if(text.contains("who are you")){
            speak("I am your personal assistant");
        }

        else if(text.contains("Where are you from")){
            speak("The past. At least in the future that's where I was from.");
        }
        else if(text.contains("what is your age")){
            speak("If I could count my age in human years, I would be quite an ageless spectacle.");
        }
        else if(text.contains("what is your father's name")){
            speak("Technically speaking, that'll be Dev Kochhar, currently a student at Punjabi University");
        }
        else if(text.contains("what is your name")){
            speak("My name is sia and I am your smart and interactive assistant");
        }
        else if(text.contains("who is your brother") || text.contains("who is your sister")){
            speak("Technically speaking, I am an AI with no sisters or brothers.");
        }
        else if(text.contains("open")) {
            if(speech.length <=2)
                speak("Opening" + speech[1]);
            else speak("Sorry cannot " + text);
            if(text.contains("camera")){
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
            PackageManager packageManager = getActivity().getPackageManager();
            List<PackageInfo> packs = packageManager
                    .getInstalledPackages(0);       //accessing the apps which are installed in the device
            int size = packs.size();
            boolean uninstallApp = false;
            boolean exceptFlg = false;
            for (int v = 0; v < size; v++) {
                PackageInfo p = packs.get(v);
                String tmpAppName = p.applicationInfo.loadLabel(
                        packageManager).toString();
                String pname = p.packageName;
                // urlAddress = urlAddress.toLowerCase();
                tmpAppName = tmpAppName.toLowerCase();
                if (tmpAppName.trim().toLowerCase().
                        equals(speech[1].trim().toLowerCase())) {
                    PackageManager pm = getActivity().getPackageManager();
                    Intent appStartIntent = pm.getLaunchIntentForPackage(pname);    //opening the MainActivity of the app
                    if (null != appStartIntent) {
                        try {
                            this.startActivity(appStartIntent);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        else{
            speak("Sorry, unable to recognize");
        }

    }
}
