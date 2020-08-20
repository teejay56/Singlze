package singlze.nerdwarelabs.singlze;

import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class filtersScr extends AppCompatActivity {
    Button filterBackBtn,radionearbyBtn,radiocityBtn,radiostateBtn,radiocountryBtn;
    TextView filterHeader,filterCountryHeader,countryValLbl,filterdistanceHeader;
    ImageView radiocountryImg,radiostateImg,radiocityImg,radionearbyImg;
    ImageView radiobothImg,radiomenImg,radiowomenImg;
    Button radiobothBtn,radiomenBtn,radiowomenBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        filterHeader = (TextView)findViewById(R.id.filterHeader);
        filterBackBtn = (Button)findViewById(R.id.filterBackBtn);
        countryValLbl = (TextView)findViewById(R.id.countryValLbl);
        radionearbyBtn = (Button)findViewById(R.id.radionearbyBtn);
        radiocityBtn = (Button)findViewById(R.id.radiocityBtn);
        radiostateBtn = (Button)findViewById(R.id.radiostateBtn);
        radiocountryBtn = (Button)findViewById(R.id.radiocountryBtn);
        radiocountryImg = (ImageView)findViewById(R.id.radiocountryImg);
        radiostateImg = (ImageView)findViewById(R.id.radiostateImg);
        radiocityImg = (ImageView)findViewById(R.id.radiocityImg);
        radionearbyImg = (ImageView)findViewById(R.id.radionearbyImg);
        radiobothImg = (ImageView)findViewById(R.id.radiobothImg);
        radiomenImg = (ImageView)findViewById(R.id.radiomenImg);
        radiowomenImg = (ImageView)findViewById(R.id.radiowomenImg);
        radiobothBtn = (Button)findViewById(R.id.radiobothBtn);
        radiomenBtn = (Button)findViewById(R.id.radiomenBtn);
        radiowomenBtn = (Button)findViewById(R.id.radiowomenBtn);
        filterdistanceHeader = (TextView)findViewById(R.id.filterdistanceHeader);
        filterCountryHeader = (TextView) findViewById(R.id.filterCountryHeader);
        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        filterHeader.setTypeface(tp);
        countryValLbl.setTypeface(tp);
        filterCountryHeader.setTypeface(tp);
        filterdistanceHeader.setTypeface(tp);
        radiocityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiocityImg.setImageResource(R.drawable.cradioon);
                radiocountryImg.setImageResource(R.drawable.cradiooff);
                radiostateImg.setImageResource(R.drawable.cradiooff);
                radionearbyImg.setImageResource(R.drawable.cradiooff);

                radiocityBtn.setText("1");
                radiocountryBtn.setText("0");
                radionearbyBtn.setText("0");
                radiostateBtn.setText("0");
            }
        });

        radiobothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiobothImg.setImageResource(R.drawable.cradioon);
                radiomenImg.setImageResource(R.drawable.cradiooff);
                radiowomenImg.setImageResource(R.drawable.cradiooff);


                radiobothBtn.setText("1");
                radiomenBtn.setText("0");
                radiowomenBtn.setText("0");

            }
        });

        radiomenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiobothImg.setImageResource(R.drawable.cradiooff);
                radiomenImg.setImageResource(R.drawable.cradioon);
                radiowomenImg.setImageResource(R.drawable.cradiooff);


                radiobothBtn.setText("0");
                radiomenBtn.setText("1");
                radiowomenBtn.setText("0");
            }
        });

        radiowomenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiobothImg.setImageResource(R.drawable.cradiooff);
                radiomenImg.setImageResource(R.drawable.cradiooff);
                radiowomenImg.setImageResource(R.drawable.cradioon);


                radiobothBtn.setText("0");
                radiomenBtn.setText("0");
                radiowomenBtn.setText("1");
            }
        });

        radiocountryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiocityImg.setImageResource(R.drawable.cradiooff);
                radiocountryImg.setImageResource(R.drawable.cradioon);
                radiostateImg.setImageResource(R.drawable.cradiooff);
                radionearbyImg.setImageResource(R.drawable.cradiooff);

                radiocityBtn.setText("0");
                radiocountryBtn.setText("1");
                radionearbyBtn.setText("0");
                radiostateBtn.setText("0");
            }
        });

        radionearbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiocityImg.setImageResource(R.drawable.cradiooff);
                radiocountryImg.setImageResource(R.drawable.cradiooff);
                radiostateImg.setImageResource(R.drawable.cradiooff);
                radionearbyImg.setImageResource(R.drawable.cradioon);

                radiocityBtn.setText("0");
                radiocountryBtn.setText("0");
                radionearbyBtn.setText("1");
                radiostateBtn.setText("0");
            }
        });

        radiostateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiocityImg.setImageResource(R.drawable.cradiooff);
                radiocountryImg.setImageResource(R.drawable.cradiooff);
                radiostateImg.setImageResource(R.drawable.cradioon);
                radionearbyImg.setImageResource(R.drawable.cradiooff);

                radiocityBtn.setText("0");
                radiocountryBtn.setText("0");
                radionearbyBtn.setText("0");
                radiostateBtn.setText("1");
            }
        });

        filterBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
