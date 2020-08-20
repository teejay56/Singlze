package singlze.nerdwarelabs.singlze;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class buyCreditScr extends AppCompatActivity {
    TextView buyCreditHeader;
    Button buyCreditBackBtn;
    GridView diamondsPackGrid;
    GridAdapter gridAdapterDiamonds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_credit_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        buyCreditBackBtn = (Button)findViewById(R.id.buyCreditBackBtn);
        buyCreditHeader = (TextView)findViewById(R.id.buyCreditHeader);
        diamondsPackGrid = (GridView)findViewById(R.id.diamondsPackGrid);
        gridAdapterDiamonds = new GridAdapter();
        diamondsPackGrid.setAdapter(gridAdapterDiamonds);
        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        buyCreditHeader.setTypeface(tp);

        buyCreditBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub


            return 6;
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

            LayoutInflater i = getLayoutInflater();

            View v = i.inflate(R.layout.diamonds_cell, parent,false);
            ImageView sherDiamondHeader = (ImageView)v.findViewById(R.id.sherDiamondHeader);
            ImageView packLogo = (ImageView)v.findViewById(R.id.packLogo);
            TextView packDiamondCountLbl = (TextView)v.findViewById(R.id.packDiamondCountLbl);
            TextView packCostLbl = (TextView)v.findViewById(R.id.packCostLbl);
            if(position==0){
                packCostLbl.setText("$1.50");
                packDiamondCountLbl.setText("10");
                sherDiamondHeader.setVisibility(View.GONE);
                packLogo.setImageResource(R.drawable.cushiondiamond);
            }else if(position==1){
                packCostLbl.setText("$7.00");
                packDiamondCountLbl.setText("100");
                sherDiamondHeader.setVisibility(View.GONE);
                packLogo.setImageResource(R.drawable.pouchdiamond);
            }else if(position==2){
                packCostLbl.setText("$49.00");
                packDiamondCountLbl.setText("700");
                sherDiamondHeader.setVisibility(View.GONE);
                packLogo.setImageResource(R.drawable.trunkdiamonds);
            }else if(position==3){
                packCostLbl.setText("$150.50");
                packDiamondCountLbl.setText("2150");
                sherDiamondHeader.setVisibility(View.GONE);
                packLogo.setImageResource(R.drawable.potdiamonds);
            }else if(position==4){
                packCostLbl.setText("$525.00");
                packDiamondCountLbl.setText("7500");
                sherDiamondHeader.setVisibility(View.GONE);
                packLogo.setImageResource(R.drawable.barreldiamonds);
            }else if(position==5){
                packCostLbl.setText("$875.00");
                packDiamondCountLbl.setText("12500");
                sherDiamondHeader.setVisibility(View.VISIBLE);
                packLogo.setImageResource(R.drawable.sherdiamonds);
            }

            Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
            packDiamondCountLbl.setTypeface(tp);
            packCostLbl.setTypeface(tp);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) livepic.getLayoutParams();
//            overlaylayer.setLayoutParams(params);
//            v.setLayoutParams(params);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return v;
        }

    }


}
