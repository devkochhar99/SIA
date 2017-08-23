package sia.dev.com.sia;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextToSpeech1 extends Fragment {
    private EditText e1;
    private Button b1,b2;
    TextToSpeech t1;

    public TextToSpeech1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_to_speech1, container, false);

        e1 = (EditText) view.findViewById(R.id.editText);
        b1 = (Button) view.findViewById(R.id.button);
        b2 = (Button) view.findViewById(R.id.button3);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                String pasteData = "";
                if (!(clipboard.hasPrimaryClip())) {



                } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {

                    // since the clipboard has data but it is not plain text

                    Toast.makeText(getActivity(),"Cannot paste as data copied is not plain text",Toast.LENGTH_LONG).show();

                } else {

                    //since the clipboard contains plain text.
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                    // Gets the clipboard as text.
                    pasteData = item.getText().toString();
                    e1.setText(pasteData);
                    Toast.makeText(getActivity(),"Text pasted Successfully",Toast.LENGTH_LONG).show();

                }

            }
        });
        t1 = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = e1.getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        return view;
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

}