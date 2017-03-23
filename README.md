# MiniHttp

A Mini-HttpServer write for android, for debug convenience.

### Integration

1. `git clone  git@github.com:LeeKM/MiniHttp.git`
2. `cp -R ./MiniHttp/minihttp $YOUR_PORJECT_ROOT`
3. add `':minihttp'` to your settings.gradle file
4. add it as a dependency of your project `compile project(path: ':minihttp')`

### Usage

start a MiniServer
```
    MiniHttpServer server = new MiniHttpServer();
    server.start(); // start server
    //...
    server.stop(); // stop server
```
Currently, the server can only response `404 Not Found`, you should regist your own FileService or HttpHandler.

#### FileService

This implment provide accessibility to local files, this is needed by [defaultHttpHandler](https://github.com/LeeKM/MiniHttp/blob/master/minihttp/src/main/java/top/leekm/minihttp/core/http/LocalFileHttpHandler.java) which process `GET` request for static resources.
You can regist your implement as follow:
```
    server.registLocalService(FileService.class, new FileServiceForAndroid());
```
An Example: [FileServiceForAndroid.java](https://github.com/LeeKM/MiniHttp/blob/master/minihttp/src/main/java/top/leekm/minihttp/core/http/FileServiceForAndroid.java)

#### HttpHandler

You can custom your http response by regist HttpHandler to certain `PATH`. Currently it supports fixed path only.
An example:
```
// response server's current time
public class TimeService extends HttpHandler {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = format.formate(new Date());
        response.getWriter.write("ServerTime: " + currentTime);
    }
}
// regist this handler to server and you can get server time with URL:
// http://a.b.c.e:port/time
server.registHttpHandler("/time", TimeService.class);
```

### Performance

OK

### Demo
unzip MiniServer.zip and `adb push ./MiniServer /sdcard/`, visit http://a.b.c.d:port/index.html, default port is 8088.