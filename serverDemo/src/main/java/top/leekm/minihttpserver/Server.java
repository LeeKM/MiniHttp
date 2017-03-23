package top.leekm.minihttpserver;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import java.io.IOException;

import top.leekm.minihttp.core.MiniHttpServer;
import top.leekm.minihttp.core.http.FileServiceForAndroid;
import top.leekm.minihttp.core.service.FileService;

/**
 * Created by lkm on 2017/3/23.
 */
public class Server extends Service {

    private MiniHttpServer server = new MiniHttpServer();

    @Override
    public void onCreate() {
        super.onCreate();

        server.registLocalService(FileService.class,
                new FileServiceForAndroid().setRoot(Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + "/MiniServer"));
        server.registHttpHandler("/action", ActionHandler.class);
        server.registHttpHandler("/echo", EchoHttpHandler.class);

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        server.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
