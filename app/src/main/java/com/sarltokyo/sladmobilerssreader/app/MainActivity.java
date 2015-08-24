package com.sarltokyo.sladmobilerssreader.app;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.sarltokyo.sladmobilerssreader.data.Item;


public class MainActivity extends AppCompatActivity
        implements RssListFragment.OnListItemClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();

        //既にFragmentが作成されているかチェック
        if (manager.findFragmentByTag("list_fragment_tag") == null) {
            Log.d(TAG, "RssListFragment created");
            FragmentTransaction ft = manager.beginTransaction();
            RssListFragment fragment = new RssListFragment();
            ft.add(R.id.my_fragment, fragment, "list_fragment_tag");
            ft.commit();
            // リストのアイテムがタップされたときに呼び出されるリスナーをセット
            fragment.setOnListItemClickListener(this);
        } else {
            RssListFragment fragment = (RssListFragment)manager.findFragmentByTag("list_fragment_tag");
            // リストのアイテムがタップされたときに呼び出されるリスナーをセット
            fragment.setOnListItemClickListener(this);
        }
    }

    @Override
    public void onListItemClick(int position, Item item) {
        Log.d(TAG, "onListItemClick called, position = " + position);
        String title = item.getTitle().toString();
        String link = item.getLink().toString();
        String descr = item.getDescription().toString();

        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        args.putString("LINK", link);
        args.putString("DESCRIPTION", descr);
        detailFragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.my_fragment, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Log.d(TAG, "refresh clicked");
            FragmentManager manager = getSupportFragmentManager();
            RssListFragment fragment = (RssListFragment)manager.findFragmentByTag("list_fragment_tag");
            if (fragment == null) return true;
            fragment.refreshRss();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
