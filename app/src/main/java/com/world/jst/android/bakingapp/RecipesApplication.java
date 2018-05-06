package com.world.jst.android.bakingapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RecipesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("recipes.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
