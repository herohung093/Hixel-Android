package com.hixel.hixel.view.adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hixel.hixel.R;

public class SliderAdapter extends PagerAdapter {

    private Context context;

    public String[] headers = {
            "INTUITIVE ANALYTICS",
            "YOUR PORTFOLIO",
            "JOIN US!"
    };

    public String[] descriptions = {
            "Forget candle charts and complicated ratios, our algorithm turns it all into a simple scale from 1-5",
            "You add the companies you like and the algorithm does the analytics behind the scenes.",
            "That's are there is too it! We make the complex simple so you can make better financial decisions with less stress. Come join us today, and get dat $$$$$"
    };

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return headers.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView image = view.findViewById(R.id.slide_image);
        TextView heading = view.findViewById(R.id.heading);
        TextView description = view.findViewById(R.id.description);

        heading.setText(headers[position]);
        description.setText(descriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
