package com.sarltokyo.sladmobilerssreader.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.TextView;

/**
 * Created by osabe on 15/08/19.
 */
public class DetailFragment extends Fragment {
    private TextView mTitle;
    private TextView mLink;
    private TextView mDescr;
    private String mTitleStr;
    private String mLinkStr;
    private String mDescrStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_detail, container, false);

        // 詳細がめんで、ActionBarを非表示にする
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        if (savedInstanceState != null) {
            mTitleStr = savedInstanceState.getString("TITLE");
            mLinkStr = savedInstanceState.getString("LINK");
            mDescrStr = savedInstanceState.getString("DESCRIPTION");
        } else {

            Bundle args = getArguments();
            mTitleStr = args.getString("TITLE");
            mLinkStr = args.getString("LINK");
            mDescrStr = args.getString("DESCRIPTION");
        }
        mTitle = (TextView)view.findViewById(R.id.item_detail_title);
        mTitle.setText(mTitleStr);
        mLink = (TextView)view.findViewById(R.id.item_detail_link);
        mLink.setText(mLinkStr);
        mDescr = (TextView)view.findViewById(R.id.item_detail_descr);
        mDescr.setText(mDescrStr);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("TITLE", mTitleStr);
        outState.putString("LINK", mLinkStr);
        outState.putString("DESCRIPTION", mDescrStr);
    }
}
