package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class selectCountryScr extends AppCompatActivity {
    Button selCountryBackBtn;
    TextView selCountryHeader;
    ListView selCountryTbl;
    JSONArray countriesArr;
    countryListAdapter clistAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            countriesArr = new JSONArray(loadJSONFromAsset());
            String fetched = "";
        }catch (Exception r){
            String error = r.getLocalizedMessage();
        }
        selCountryHeader = (TextView)findViewById(R.id.selCountryHeader);
        selCountryBackBtn = (Button)findViewById(R.id.selCountryBackBtn);
        selCountryTbl = (ListView)findViewById(R.id.selCountryTbl);
        clistAdapter = new countryListAdapter();
        selCountryTbl.setAdapter(clistAdapter);
        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        selCountryHeader.setTypeface(tp);

        selCountryBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.setData(Uri.parse("CANCEL"));
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("CountryCodes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    class countryListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return countriesArr.length();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            System.out.println("HI ENTER::::");

            LayoutInflater i = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View v = i.inflate(R.layout.country_cell, parent,false);

            TextView countryNameLbl = (TextView)v.findViewById(R.id.countryNameLbl);

            Typeface tHR = Typeface.createFromAsset(getAssets(),"arial.ttf");
            countryNameLbl.setTypeface(tHR);
            try{
                countryNameLbl.setText(countriesArr.getJSONObject(position).getString("name")+"("+countriesArr.getJSONObject(position).getString("dial_code")+")");
            }catch (Exception r){}

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent();

                    if(getIntent().getStringExtra("ISNAME").equalsIgnoreCase("YES")){
                        try {
                            data.setData(Uri.parse(countriesArr.getJSONObject(position).getString("name")));
                            setResult(RESULT_OK, data);
                        }catch (Exception r){}
                    }else{
                        try {
                            data.setData(Uri.parse(countriesArr.getJSONObject(position).getString("dial_code")));
                            setResult(RESULT_OK, data);
                        }catch (Exception r){}
                    }

//---set the data to pass back---

//---close the activity---
                    finish();
                }
            });
            return v;
        }
    }
}
