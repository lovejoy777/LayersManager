package com.lovejoy777.rroandlayersmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lovejoy777 on 02/04/15.
 */
public class CustomGridAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] title;
    private final String[] subTitle;
    private final int[] Imageid;
    public CustomGridAdapter(Context c, String[] title, String[] subTitle, int[] Imageid) {
        mContext = c;
        this.Imageid = Imageid;
        this.title = title;
        this.subTitle = subTitle;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.button_gridview_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            TextView textView1 = (TextView) grid.findViewById(R.id.grid_text1);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(title[position]);
            textView1.setText(subTitle[position]);
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}