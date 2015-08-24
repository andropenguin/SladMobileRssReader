package com.sarltokyo.sladmobilerssreader.data;

import rx.Observable;

/**
 * Created by osabe on 15/08/20.
 */
public interface DataRepository {
    Observable<RssData> getRssData(String param);
}
