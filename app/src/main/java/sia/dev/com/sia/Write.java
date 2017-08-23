package sia.dev.com.sia;


import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Write extends Fragment {
private Button b1,b2;
    private TextView t1;


    public Write() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_write, container, false);
        t1 = (TextView)view.findViewById(R.id.textView19);
        b1 = (Button)view.findViewById(R.id.button1);
        b2 = (Button)view.findViewById(R.id.button2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData myClip;
                int min = 0;
                int max = t1.getText().length();
                if (t1.isFocused()) {
                    final int selStart = t1.getSelectionStart();
                    final int selEnd = t1.getSelectionEnd();
                    min = Math.max(0, Math.min(selStart, selEnd));
                    max = Math.max(0, Math.max(selStart, selEnd));
                }
// here is your selected text
                final CharSequence selectedText = t1.getText().subSequence(min, max);
                String text = selectedText.toString();


// copy to clipboard
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getActivity(),"Text Copied Successfully",Toast.LENGTH_LONG).show();
                }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        return  view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                t1.setText(inSpeech);
            }
        }
    }

}
