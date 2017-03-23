package top.leekm.minihttp.core.http;

import top.leekm.minihttp.core.MiniHttpServer;

/**
 * Created by lkm on 2017/3/23.
 */

public class HttpContext {

    private MiniHttpServer server;

    public void attachContext(MiniHttpServer server) {
        this.server = server;
    }

    public <T> T queryLocalService(Class<T> clazz) {
        return server.queryLocalService(clazz);
    }
}
