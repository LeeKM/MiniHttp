package top.leekm.minihttp.core.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lkm on 2017/3/22.
 */
public class HttpRequest {

    public String method;

    public String path;

    public String httpVersion;

    private Map<String, String> params = new HashMap<>();

    private Map<String, String> properties = new HashMap<>();

    public String getProperty(String key) {
        return properties.get(key);
    }

    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    public HttpRequest() {

    }

    public void clear() {
        this.method = null;
        this.path = null;
        this.httpVersion = null;
        this.params.clear();
        this.properties.clear();
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }

    public String getParam(String name) {
        return params.get(name);
    }

    public Map<String, String> getParams() {
        return new HashMap<>(params);
    }

    public void addParam(String name, String value) {
        params.put(name, value);
    }
}
