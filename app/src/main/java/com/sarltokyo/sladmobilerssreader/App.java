package com.sarltokyo.sladmobilerssreader;

import android.app.Application;
import com.sarltokyo.sladmobilerssreader.data.ConcreteDataRepository;
import com.sarltokyo.sladmobilerssreader.data.DataRepository;

/**
 * Created by osabe on 15/08/20.
 */
public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }

    private DataRepository repository = new ConcreteDataRepository();

    public DataRepository getRepository() {
        return repository;
    }

    public void setRepository(DataRepository repository) {
        this.repository = repository;
    }
}
