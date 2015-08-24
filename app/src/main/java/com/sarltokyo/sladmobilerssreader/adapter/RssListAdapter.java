package com.sarltokyo.sladmobilerssreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.sarltokyo.sladmobilerssreader.app.R;
import com.sarltokyo.sladmobilerssreader.data.Item;

import java.util.List;

/**
 * Created by osabe on 15/08/16.
 */
public class RssListAdapter extends ArrayAdapter<Item> {
    private LayoutInflater mInflater;
    private TextView mTitle;
    private TextView mDescr;

    public RssListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        mInflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 1行ごとのビューを生成する
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_row, null);
        }

        // 現在参照しているリストの位置からItemを取得する
        Item item = this.getItem(position);
        if (item != null) {
            // Itemから必要なデータを取り出し、それぞれTextViewにセットする
            String title = item.getTitle().toString();
            mTitle = (TextView)view.findViewById(R.id.item_title);
            mTitle.setText(title);
            String descr = item.getDescription().toString();
            mDescr = (TextView)view.findViewById(R.id.item_descr);
            mDescr.setText(descr);
        }
        return view;
    }
}
