package com.sarltokyo.sladmobilerssreader.data;

import java.util.List;

/**
 * Created by osabe on 15/08/20.
 */
public class RssData {
    List<Item> mItems;

    public void setItems(List<Item> items) {
        mItems = items;
    }

    public List<Item> getItems() {
        return mItems;
    }
}
