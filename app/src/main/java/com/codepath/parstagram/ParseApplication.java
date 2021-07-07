package com.codepath.parstagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    /**
     * initializes Parse SDK as soon as the application is created
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("NExVZOHehiQCHNOwAF3Fspxqeuhikag2YilDbcB4")
                .clientKey("Q2Nu0n9KfrQiD7dgVR3PPcce9qSE0VQxbvyGKTda")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
