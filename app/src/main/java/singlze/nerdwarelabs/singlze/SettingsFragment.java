package singlze.nerdwarelabs.singlze;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
    TextView termsOfUsageLbl,privacyPolicyLbl,feedbackLbl,aboutusLbl,rateusLbl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        View view = inflater.inflate(R.layout.fragment_settings, null);
        termsOfUsageLbl = (TextView)view.findViewById(R.id.termsOfUsageLbl);
        privacyPolicyLbl = (TextView)view.findViewById(R.id.privacyPolicyLbl);
        feedbackLbl = (TextView)view.findViewById(R.id.feedbackLbl);
        aboutusLbl = (TextView)view.findViewById(R.id.aboutusLbl);
        rateusLbl = (TextView)view.findViewById(R.id.rateusLbl);

        Typeface tarial = Typeface.createFromAsset(getActivity().getAssets(),"arial.ttf");
        termsOfUsageLbl.setTypeface(tarial);
        privacyPolicyLbl.setTypeface(tarial);
        feedbackLbl.setTypeface(tarial);
        aboutusLbl.setTypeface(tarial);
        rateusLbl.setTypeface(tarial);

        return view;
    }
}
