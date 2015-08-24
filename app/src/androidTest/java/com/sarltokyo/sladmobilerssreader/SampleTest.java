package com.sarltokyo.sladmobilerssreader;

import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.Root;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.WindowManager;
import android.widget.ListView;
import com.novoda.rxmocks.RxMocks;
import com.novoda.rxmocks.SimpleEvents;
import com.novoda.rxpresso.RxPresso;
import com.sarltokyo.sladmobilerssreader.app.MainActivity;
import com.sarltokyo.sladmobilerssreader.app.R;
import com.sarltokyo.sladmobilerssreader.app.RssListFragment;
import com.sarltokyo.sladmobilerssreader.data.DataRepository;
import com.sarltokyo.sladmobilerssreader.data.Item;
import com.sarltokyo.sladmobilerssreader.data.RssData;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.novoda.rxmocks.RxExpect.any;
import static com.novoda.rxmocks.RxExpect.anyError;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by osabe on 15/08/20.
 */
@RunWith(AndroidJUnit4.class)
public class SampleTest {

    private RxPresso rxPresso;
    private DataRepository mockedRepo;

    private final static int DUMMY_DATA_NUM = 10;
    private final static int DUMMY_REFRESH_DATA_NUM = 5;

    @Rule
    public ActivityTestRule<MainActivity> rule
            = new ActivityTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            App application
                    = (App) InstrumentationRegistry.getTargetContext().getApplicationContext();

            mockedRepo = RxMocks.mock(DataRepository.class);
            application.setRepository(mockedRepo);

            rxPresso = new RxPresso(mockedRepo);
            Espresso.registerIdlingResources(rxPresso);
        }

        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
            Espresso.unregisterIdlingResources(rxPresso);
            rxPresso.resetMocks();
        }
    };

    @Test
    public void testSucces() throws Exception {
        // ダミーデータ作成
        List<Item> items = createDummyItems(DUMMY_DATA_NUM);

        RssData mockRssData = mock(RssData.class);
        when(mockRssData.getItems()).thenReturn(items);

        MainActivity activity = rule.getActivity();

        // 何を返すか
        rxPresso.given(mockedRepo.getRssData(RssListFragment.RSS_FEED_URL))
                .withEventsFrom(SimpleEvents.onNext(mockRssData))
                // テストを実行する前に、何のイベントを待っているか
                .expect(any(RssData.class))
                .thenOnView(withId(android.R.id.list))
                .check(matches(isDisplayed()));

        // リストの行数のテスト
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.my_fragment);
        RssListFragment listFragment = (RssListFragment)fragment;
        final ListView listView = listFragment.getListView();
        assertThat(listView.getCount(), is(DUMMY_DATA_NUM));

        // リストの各行の内容表示のテスト
        for (int i = 0; i < DUMMY_DATA_NUM; i++) {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(i)
                    .onChildView(withId(R.id.item_title))
                    .check(matches(withText("dummy title" + i)));

            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(i)
                    .onChildView(withId(R.id.item_descr))
                    .check(matches(withText("dummy text" + i)));
        }
    }

    @Test
    public void testToDetail() throws Exception {
        // ダミーデータ作成
        List<Item> items = createDummyItems(DUMMY_DATA_NUM);

        RssData mockRssData = mock(RssData.class);
        when(mockRssData.getItems()).thenReturn(items);

        rule.getActivity();

        // 何を返すか
        rxPresso.given(mockedRepo.getRssData(RssListFragment.RSS_FEED_URL))
                .withEventsFrom(SimpleEvents.onNext(mockRssData))
                // テストを実行する前に、何のイベントを待っているか
                .expect(any(RssData.class))
                .thenOnView(withId(android.R.id.list))
                .check(matches(isDisplayed()));

        // 遷移先の画面の内容をテストする
        // RxPressoの使い方がわからず、Espresoでやる
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0)
                .perform(click());
        onView(withId(R.id.item_detail_title)).check(matches(withText("dummy title0")));
        onView(withId(R.id.item_detail_link)).check(matches(withText("http://dummy0.com")));
        onView(withId(R.id.item_detail_descr)).check(matches(withText("dummy text0")));
    }

    @Test
    public void testRefresh() throws Exception {
        // ダミーデータ作成
        List<Item> items = createDummyItems(DUMMY_DATA_NUM);

        RssData mockRssData = mock(RssData.class);
        when(mockRssData.getItems()).thenReturn(items);

        MainActivity activity = rule.getActivity();

        // 何を返すか
        rxPresso.given(mockedRepo.getRssData(RssListFragment.RSS_FEED_URL))
                .withEventsFrom(SimpleEvents.onNext(mockRssData))
                        // テストを実行する前に、何のイベントを待っているか
                .expect(any(RssData.class))
                .thenOnView(withId(android.R.id.list))
                .check(matches(isDisplayed()));

        // 更新用データ作成
        List<Item> refreshItems = createDummyRefreshItems(DUMMY_REFRESH_DATA_NUM);
        RssData refreshMockRssData = mock(RssData.class);
        when(refreshMockRssData.getItems()).thenReturn(refreshItems);

        // 何を返すか
        rxPresso.given(mockedRepo.getRssData(RssListFragment.RSS_FEED_URL))
                .withEventsFrom(SimpleEvents.onNext(refreshMockRssData))
                // テストを実行する前に、何のイベントを待っているか
                .expect(any(RssData.class))
                .thenOnView(withId(R.id.action_refresh))
                .perform(click());

        // リストの行数のテスト
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.my_fragment);
        RssListFragment listFragment = (RssListFragment)fragment;
        final ListView listView = listFragment.getListView();
        assertThat(listView.getCount(), is(DUMMY_REFRESH_DATA_NUM));

        // リストの各行の内容表示のテスト
        for (int i = 0; i < DUMMY_REFRESH_DATA_NUM; i++) {
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(i)
                    .onChildView(withId(R.id.item_title))
                    .check(matches(withText("dummy title" + (DUMMY_DATA_NUM +i))));

            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(i)
                    .onChildView(withId(R.id.item_descr))
                    .check(matches(withText("dummy text" + (DUMMY_DATA_NUM + i))));
        }
    }

    /**
     * ダミーデータ作成
     * @param count
     * @return
     */
    private List<Item> createDummyItems(int count) {
        List<Item> items = new ArrayList<Item>();
        for (int i = 0; i < count; i++) {
            Item item = new Item();
            item.setTitle("dummy title" + i);
            item.setLink("http://dummy" + i + ".com");
            item.setDescription("dummy text" + i);
            items.add(item);
        }
        return items;
    }

    /**
     * 更新用データ作成
     * @param count
     * @return
     */
    private List<Item> createDummyRefreshItems(int count) {
        List<Item> refreshItems = new ArrayList<Item>();
        for (int i = DUMMY_DATA_NUM; i < DUMMY_DATA_NUM + count; i++) {
            Item item = new Item();
            item.setTitle("dummy title" + i);
            item.setLink("http://dummy" + i + ".com");
            item.setDescription("dummy text" + i);
            refreshItems.add(item);
        }
        return refreshItems;
    }


    @Test
    public void testIOException() throws Exception {

        rule.getActivity();

        // エラーでToastが表示されるかのテスト
        // 何を返すか
        rxPresso.given(mockedRepo.getRssData(RssListFragment.RSS_FEED_URL))
                .withEventsFrom(SimpleEvents.<RssData>onError(new IOException("error")))
                // テストを実行する前に、何のイベントを待っているか
                .expect(anyError(RssData.class, IOException.class))
                .thenOnView(withText("error")).inRoot(isToast())
                .check(matches(isDisplayed()));
    }

    /**
     * Matcher that is Toast window.
     * http://baroqueworksdevjp.blogspot.jp/2015/03/espressotoast.html
     */
    public static Matcher<Root> isToast() {
        return new TypeSafeMatcher<Root>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("is toast");
            }

            @Override
            public boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                    IBinder windowToken = root.getDecorView().getWindowToken();
                    IBinder appToken = root.getDecorView().getApplicationWindowToken();
                    if (windowToken == appToken) {
                        // windowToken == appToken means this window isn't contained by any other windows.
                        // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
