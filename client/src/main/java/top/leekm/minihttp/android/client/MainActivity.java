package top.leekm.minihttp.android.client;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import top.leekm.minihttp.core.Run;
import top.leekm.minihttp.utils.Helper;

public class MainActivity extends Activity implements View.OnClickListener {

    private Console console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configActionBar();
        console = (Console) findViewById(R.id.console);
    }

    @Override
    public void onClick(View view) {
        console.clear();
    }

    public void onButtonClick(View view) {
        executor.execute(new Run() {
            @Override
            protected void todo() throws Throwable {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                URL url = new URL("http://192.168.1.107:8088/index.html?Hello=World&TimeStamp=" +
                        format.format(new Date()) + "&token=" + Helper.randBase64Str(64));
                Log.e("url", url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Session", String.valueOf(System.nanoTime()));
                connection.connect();

                if (connection.getResponseCode() != 200) {
                    String message = connection.getResponseCode() + " " + connection.getResponseMessage();
                    console.println(message);
                } else {
                    InputStream inputStream = connection.getInputStream();
                    console.println(Helper.drain(inputStream));
                }

                connection.disconnect();
            }

            @Override
            protected void onFailed(Throwable ex) {
                super.onFailed(ex);
                console.println(ex.getClass().getName());
            }
        });
    }

    private void configActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.getCustomView().setOnClickListener(this);
    }

    private Executor executor = Executors.newSingleThreadExecutor();
}

