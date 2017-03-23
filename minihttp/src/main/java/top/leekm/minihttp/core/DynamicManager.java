package top.leekm.minihttp.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import top.leekm.minihttp.core.http.HttpHandler;
import top.leekm.minihttp.core.http.LocalFileHttpHandler;

/**
 * Created by lkm on 2017/3/23.
 */

public class DynamicManager {

    private Map<String, Class<? extends HttpHandler>> registedHandler = new ConcurrentHashMap<>();
    private Map<String, HttpHandler> instancedHandler = new ConcurrentHashMap<>();
    private Map<Class<?>, Object> registedService = new ConcurrentHashMap<>();
    private LocalFileHttpHandler defaultHttpHandler = new LocalFileHttpHandler();
    private MiniHttpServer server;

    public DynamicManager(MiniHttpServer server) {
        this.server = server;
        defaultHttpHandler.attachContext(server);
    }

    public <T> T queryLocalService(Class<T> clazz) {
        return (T) registedService.get(clazz);
    }

    public HttpHandler getDefaultHttpHandler() {
        return defaultHttpHandler;
    }

    public void registLocalService(Class<?> clazz, Object service) {
        if (service.getClass().isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(service + " cannot assign to " + clazz);
        }
        registedService.put(clazz, service);
    }

    public void registHttpHandler(String path, Class<? extends HttpHandler> clazz) {
        registedHandler.put(path, clazz);
        if (instancedHandler.containsKey(path)) {
            instancedHandler.remove(path);
        }
    }

    public HttpHandler queryHttpHandler(String path) {
        HttpHandler handler = instancedHandler.get(path);
        if (null == handler) {
            Class<? extends HttpHandler> handleClazz = registedHandler.get(path);
            if (null != handleClazz) {
                handler = instanceHttpHandler(path, handleClazz);
            }
        }
        return handler;
    }

    private synchronized HttpHandler instanceHttpHandler(String path,
                                                         Class<? extends HttpHandler> clazz) {
        HttpHandler handler = null;
        if (null == instancedHandler.get(path)) {
            try {
                handler = clazz.newInstance();
                handler.attachContext(server);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return handler;
    }

    public synchronized void clear() {
        instancedHandler.clear();
    }

}
