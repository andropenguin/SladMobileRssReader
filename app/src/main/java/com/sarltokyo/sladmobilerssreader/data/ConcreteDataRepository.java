package com.sarltokyo.sladmobilerssreader.data;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import rx.Observable;
import rx.Subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by osabe on 15/08/20.
 */
public class ConcreteDataRepository implements DataRepository {
    @Override
    public Observable<RssData> getRssData(final String param) {
        return Observable.create(
                new Observable.OnSubscribe<RssData>() {
                    @Override
                    public void call(Subscriber<? super RssData> subscriber) {
                        try {
                            List<Item> result = null;
                            URL url = new URL(param);
                            InputStream is = url.openConnection().getInputStream();
                            result = parseXml(is);
                            RssData rssData = new RssData();
                            rssData.setItems(result);
                            subscriber.onNext(rssData);
                        } catch (IOException e) {
                            subscriber.onError(e);
                        } catch (XmlPullParserException e) {
                            subscriber.onError(e);
                        }
                    }
                }
        );
    }

    // XMLをパースする
    public List<Item> parseXml(InputStream is)  throws IOException, XmlPullParserException {
        List<Item> items = new ArrayList<Item>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(is, null);
            int eventType = parser.getEventType();
            Item currentItem = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = null;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")) {
                            currentItem = new Item();
                        } else if (currentItem != null) {
                            if (tag.equals("title")) {
                                currentItem.setTitle(parser.nextText());
                            } else if (tag.equals("link")) {
                                currentItem.setLink(parser.nextText());
                            } else if (tag.equals("description")) {
                                currentItem.setDescription(htmlTagRemover(parser.nextText()));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")) {
                            items.add(currentItem);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (XmlPullParserException e) {
            throw e;
        }
        return items;
    }

    /**
     * HTMLタグ削除（すべて）
     * http://it--trick.appspot.com/article/30051/40001/70001/70002.html
     *
     * @param str 文字列
     * @return HTMLタグ削除後の文字列
     */
    public static String htmlTagRemover(String str) {
        // 文字列のすべてのタグを取り除く
        return str.replaceAll("<.+?>", "");
    }
}
