package com.example.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.calorietracker.R;
import com.example.entities.WelcomeListItem;

import java.util.List;

public class WelcomeListAdaptor extends BaseAdapter {

    private Context context;
    private List<WelcomeListItem> list;
    private LayoutInflater layoutInflater;
    TextView tv_title;
    TextView tv_data;

    public WelcomeListAdaptor(Context context, List<WelcomeListItem> list){
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_lv_welcome, null);
        }

        tv_title =(TextView)convertView.findViewById(R.id.tv_title);
        tv_data = (TextView) convertView.findViewById(R.id.tv_number);

        tv_title.setText(list.get(position).getTitle());
        tv_data.setText(list.get(position).getData());

        return convertView;
    }
}
