package com.sarltokyo.sladmobilerssreader.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.sarltokyo.sladmobilerssreader.App;
import com.sarltokyo.sladmobilerssreader.adapter.RssListAdapter;
import com.sarltokyo.sladmobilerssreader.data.DataRepository;
import com.sarltokyo.sladmobilerssreader.data.Item;
import com.sarltokyo.sladmobilerssreader.data.RssData;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osabe on 15/08/19.
 */
public class RssListFragment extends ListFragment {
    private final static String TAG = RssListFragment.class.getSimpleName();

    public static final String RSS_FEED_URL = "http://rss.rssad.jp/rss/slashdot/mobile.rss";
    private List<Item> mItems;
    private RssListAdapter mAdapter;

    private Subscription subscription;
    private DataRepository dataRepository;

    // アイテムがタップされたときのリスナー
    public interface OnListItemClickListener {
        public void onListItemClick(int position, Item item);
    }

    // アイテムがタップされたときのリスナー
    private OnListItemClickListener mOnListItemClickListener;

    // アイテムがタップされたときのリスナーをセット

    public void setOnListItemClickListener(OnListItemClickListener l) {
        mOnListItemClickListener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataRepository = App.getInstance().getRepository();

        // Itemオブジェクトを保持するためのリストを生成し、アダプタに追加する
        mItems = new ArrayList<Item>();
        mAdapter = new RssListAdapter(getActivity(), mItems);

        // アダプタをリストビューにセットする
        setListAdapter(mAdapter);

        getRssDataByRxJava(RSS_FEED_URL);
    }

    // リストのアイテムがタップされたときに呼び出される
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        showDetail(position);
    }

    private void showDetail(int position) {
        // タップされたときのリスナーのメソッドを呼び出す
        if (mOnListItemClickListener != null) {
            Item item = (Item)(mAdapter.getItem(position));
            mOnListItemClickListener.onListItemClick(position, item);
        }
    }

    protected void refreshRss() {
        getRssDataByRxJava(RSS_FEED_URL);
    }

    void setRssResult(RssData rssData) {
        mItems = rssData.getItems();
        // todo: ダブリコード
        mAdapter = new RssListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);
    }

    void setRssError() {
        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
    }

    void getRssDataByRxJava(String param) {
        subscription = dataRepository.getRssData(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RssDataObserver());
    }

    private class RssDataObserver implements Observer<RssData> {
        @Override
        public void onCompleted() {
            // nop
        }

        @Override
        public void onError(Throwable e) {
            setRssError();
        }

        @Override
        public void onNext(RssData rssData) {
            setRssResult(rssData);
        }
    }

}
