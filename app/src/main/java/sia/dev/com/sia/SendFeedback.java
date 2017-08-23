package sia.dev.com.sia;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFeedback extends Fragment {
    private RatingBar r1;

    public SendFeedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_feedback, container, false);
        r1 = (RatingBar) view.findViewById(R.id.ratingBar);

        r1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    // TODO Auto-generated method stub
                rating = r1.getRating();
                    Toast.makeText(getActivity(),String.valueOf(rating),Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Rating Your App");
                    intent.putExtra(Intent.EXTRA_TEXT, "Rating - "+rating+"\nWrite any of your suggestions here - \n"+"\nSend By Sia");
                    intent.setData(Uri.parse("mailto:myawaaz1234@gmail.com")); // or just "mailto:" for blank
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                    startActivity(intent);
            }
        });

        return view;
    }
}
