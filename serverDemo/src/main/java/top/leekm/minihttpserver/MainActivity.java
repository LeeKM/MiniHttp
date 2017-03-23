package top.leekm.minihttpserver;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.IOException;

import top.leekm.minihttp.core.Log;
import top.leekm.minihttp.core.MiniHttpServer;
import top.leekm.minihttp.core.http.FileServiceForAndroid;
import top.leekm.minihttp.core.service.FileService;
import top.leekm.minihttp.utils.Helper;
import top.leekm.minihttpserver.demo.R;

public class MainActivity extends Activity
        implements Log.Logger, View.OnClickListener {

    private Console console;
    private MiniHttpServer server = new MiniHttpServer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configActionBar();
        console = (Console) findViewById(R.id.console);
        console.println("localAddress: " + getLocalAddress());
        Log.registLogger(this);
    }

    public void startServer(View view) {
        startService(new Intent(this, Server.class));
    }

    public void stopServer(View view) {
        stopService(new Intent(this, Server.class));
    }

    private String getLocalAddress() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (WifiManager.WIFI_STATE_ENABLED == manager.getWifiState()) {
            DhcpInfo dhcpInfo = manager.getDhcpInfo();
            int address = dhcpInfo.ipAddress;
            return Helper.intToIp(address);
        }
        return "0.0.0.0";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.stop();
        Log.unRegistLogger(this);
    }

    private void configActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.getCustomView().setOnClickListener(this);
    }

    @Override
    public void log(String tag, String message) {
        console.println(tag + "-" + message);
    }

    @Override
    public void warn(String tag, String message) {
        console.println(tag + "-" + message);
    }

    @Override
    public void error(String tag, String message) {
        console.println(tag + "-" + message);
    }

    @Override
    public void exception(String tag, Throwable throwable) {
        console.println(tag + "-" + android.util.Log.getStackTraceString(throwable));
    }

    @Override
    public void onClick(View v) {
        console.clear();
        console.println("localAddress: " + getLocalAddress());
    }
}
