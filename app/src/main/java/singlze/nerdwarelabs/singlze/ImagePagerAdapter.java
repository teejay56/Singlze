package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ImagePagerAdapter extends PagerAdapter {
    int count;
    Context context;
    LayoutInflater inflater;
    JSONObject jobj;
    SharedPreferences sharedpreferences;
    public static final String UDATAPREFERENCES = "UDATA";
    public ImagePagerAdapter(Context context, int count, JSONObject obj){
        this.jobj = obj;
        this.context = context;
        this.count = count;
        sharedpreferences = context.getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return count;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.imagepageritem, container, false);
        RelativeLayout fLayout = (RelativeLayout) view.findViewById(R.id.imageCellHolder);

        ImageView vpimg = (ImageView) view.findViewById(R.id.currImgView);

        if(position==0) {
            try {
                String propic = jobj.getString("user_pic");
                if (propic.equalsIgnoreCase("NA") == false) {
                    propic = "https://singlze.com/" + propic;
                    Picasso.with(context)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(vpimg);

                }
            }catch (Exception r){}
        }else if(position==1){
            try {
                String propic = jobj.getString("u_pic2");
                if (propic.equalsIgnoreCase("NA") == false) {
                    propic = "https://singlze.com/" + propic;
                    Picasso.with(context)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(vpimg);

                }
            }catch (Exception r){}
        }else if(position==2){
            try {
                String propic = jobj.getString("u_pic3");
                if (propic.equalsIgnoreCase("NA") == false) {
                    propic = "https://singlze.com/" + propic;
                    Picasso.with(context)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(vpimg);

                }
            }catch (Exception r){}
        }else if(position==3){
            try {
                String propic = jobj.getString("u_pic4");
                if (propic.equalsIgnoreCase("NA") == false) {
                    propic = "https://singlze.com/" + propic;
                    Picasso.with(context)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(vpimg);

                }
            }catch (Exception r){}
        }else if(position==4){
            try {
                String propic = jobj.getString("u_pic5");
                if (propic.equalsIgnoreCase("NA") == false) {
                    propic = "https://singlze.com/" + propic;
                    Picasso.with(context)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(vpimg);

                }
            }catch (Exception r){}
        }

        container.addView(view);
        return view;

    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == o);
    }
}
