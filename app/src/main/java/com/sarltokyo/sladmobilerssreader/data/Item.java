package com.sarltokyo.sladmobilerssreader.data;

/**
 * Created by osabe on 15/08/16.
 */
public class Item {
    // 記事のタイトル
    private CharSequence mTitle;
    // リンク
    private String mLink;
    // 記事の本文
    private CharSequence mDescription;

    public Item() {
        mTitle = "";
        mDescription = "";
    }

    public CharSequence getDescription() {
        return mDescription;
    }

    public void setDescription(CharSequence description) {
        mDescription = description;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }
}