package top.leekm.minihttp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lkm on 2017/3/23.
 */

public class StreamUtils {

    public static long transfer(InputStream is, OutputStream os) throws IOException {
        long total = 0;
        int value;
        while ((value = is.read()) >= 0) {
            total += 1;
            os.write(value);
        }
        return total;
    }

}
