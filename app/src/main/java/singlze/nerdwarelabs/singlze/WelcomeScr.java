package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class WelcomeScr extends AppCompatActivity {
    TextView wpunchline,lbroadcastw,shortvideosw,chatlbl;
    SharedPreferences sharedpreferences;
    public static final String UDATAPREFERENCES = "UDATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        wpunchline = (TextView)findViewById(R.id.wpunchline);
        lbroadcastw = (TextView)findViewById(R.id.lbroadcastw);
        shortvideosw = (TextView)findViewById(R.id.shortvideosw);
        chatlbl = (TextView)findViewById(R.id.chatlbl);
        settingUpView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String u_id = sharedpreferences.getString("user_id","");
                if(u_id.length()>0){
                    Intent i = new Intent(WelcomeScr.this, HomeScr.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(WelcomeScr.this, accountScr.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 3000);

    }


    public void settingUpView(){
        Typeface tcano = Typeface.createFromAsset(getAssets(),"Cano.otf");
        wpunchline.setTypeface(tcano);
        chatlbl.setTypeface(tcano);
        lbroadcastw.setTypeface(tcano);
        shortvideosw.setTypeface(tcano);
    }
}
