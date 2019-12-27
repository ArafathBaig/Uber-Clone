package arafath.myappcom.uberclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("qknSwxpub1BMfb6gxsrSnwGIRaQitJDZCn4pCjSq")
                // if defined
                .clientKey("d5LsBXZiPkOQ421jehxftqkjqkNi3L08UV4ST8Vn")
                .server(getString(R.string.back4app_server_url))
                .build()
        );
    }
}
