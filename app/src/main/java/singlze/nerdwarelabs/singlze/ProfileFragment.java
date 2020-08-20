package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    RelativeLayout nChangePasswordHolder;
    Button cancelChangePassBtn,changePasswordBtn,updatepasswordBtn,profileSettBtn;
    TextView changePassHeaderLbl,changePassSubheader;
    EditText newPasswordTxt,currentPassTxt,confirmPassTxt;
    RelativeLayout currentPassHolder,newPasswordHolder,confirmPasswordHolder;
    SharedPreferences sharedpreferences;
    CircleImageView profile_image;
    public static final String UDATAPREFERENCES = "UDATA";
    TextView usernameLbl,coinsLbl,buyCreditLbl,withdrawLbl,changePassLbl,logoutLbl,followersCount,followersLbl,followingCount,followingLbl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        sharedpreferences = getActivity().getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        View view =inflater.inflate(R.layout.fragment_profile, null);
        profileSettBtn = (Button)view.findViewById(R.id.profileSettBtn);
        profile_image = (CircleImageView)view.findViewById(R.id.profile_image);
        changePasswordBtn = (Button)view.findViewById(R.id.changePasswordBtn);
        confirmPassTxt = (EditText)view.findViewById(R.id.confirmPassTxt);
        currentPassTxt = (EditText)view.findViewById(R.id.currentPassTxt);
        newPasswordTxt = (EditText)view.findViewById(R.id.newPasswordTxt);
        changePassSubheader = (TextView)view.findViewById(R.id.changePassSubheader);
        changePassHeaderLbl = (TextView)view.findViewById(R.id.changePassHeaderLbl);
        cancelChangePassBtn = (Button)view.findViewById(R.id.cancelChangePassBtn);
        updatepasswordBtn = (Button)view.findViewById(R.id.updatepasswordBtn);
        currentPassHolder = (RelativeLayout)view.findViewById(R.id.currentPassHolder);
        newPasswordHolder = (RelativeLayout)view.findViewById(R.id.newpasswordHolder);
        confirmPasswordHolder = (RelativeLayout)view.findViewById(R.id.confirmPassHolder);
        nChangePasswordHolder = (RelativeLayout)view.findViewById(R.id.nChangePasswordHolder);
        usernameLbl = (TextView)view.findViewById(R.id.userNameLbl);
        coinsLbl = (TextView)view.findViewById(R.id.coinsLbl);
        buyCreditLbl = (TextView)view.findViewById(R.id.buyCreditLbl);
        withdrawLbl = (TextView)view.findViewById(R.id.withdrawLbl);
        changePassLbl = (TextView)view.findViewById(R.id.changepassLbl);
        logoutLbl = (TextView)view.findViewById(R.id.logoutLbl);

        followersCount = (TextView)view.findViewById(R.id.followersCount);
        followersLbl = (TextView)view.findViewById(R.id.followersLabel);
        followingCount = (TextView)view.findViewById(R.id.followingCount);
        followingLbl = (TextView)view.findViewById(R.id.followingLabel);

        usernameLbl.setText(sharedpreferences.getString("user_name",""));


        String propic = sharedpreferences.getString("user_pic","");
        if(propic.equalsIgnoreCase("NA")==false){
            propic = "https://singlze.com/"+propic;
            Picasso.with(getActivity())
                    .load(propic)
                    .placeholder(R.drawable.defpic)
                    .into(profile_image);

        }

        profileSettBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),profileSettings.class);
                startActivity(i);
            }
        });

        updatepasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPassTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(currentPassHolder);
                }else if(newPasswordTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(newPasswordHolder);
                }else if(confirmPassTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(confirmPasswordHolder);
                }
            }
        });


        setupUI();
        return view;
    }


    @Nullable
    @Override
    public void onResume(){
        super.onResume();
        usernameLbl.setText(sharedpreferences.getString("user_name",""));
        String propic = sharedpreferences.getString("user_pic","");
        if(propic.equalsIgnoreCase("NA")==false){
            propic = "https://singlze.com/"+propic;
            Picasso.with(getActivity())
                    .load(propic)
                    .placeholder(R.drawable.defpic)
                    .into(profile_image);

        }
    }
    public void setupUI(){

        Typeface tp = Typeface.createFromAsset(getActivity().getAssets(),"arial.ttf");
        Typeface barial = Typeface.createFromAsset(getActivity().getAssets(),"arialbd.ttf");
        usernameLbl.setTypeface(tp);
        coinsLbl.setTypeface(tp);
        buyCreditLbl.setTypeface(tp);
        withdrawLbl.setTypeface(tp);
        changePassLbl.setTypeface(tp);
        logoutLbl.setTypeface(tp);

        followingLbl.setTypeface(tp);
        followingCount.setTypeface(tp);
        followersLbl.setTypeface(tp);
        followersCount.setTypeface(tp);
        updatepasswordBtn.setTypeface(tp);
        changePassHeaderLbl.setTypeface(barial);
        changePassSubheader.setTypeface(tp);
        newPasswordTxt.setTypeface(tp);
        currentPassTxt.setTypeface(tp);
        confirmPassTxt.setTypeface(tp);

        cancelChangePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nChangePasswordHolder.setVisibility(View.GONE);
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                nChangePasswordHolder.setVisibility(View.VISIBLE);
            }
        });
    }
}
