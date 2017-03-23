package top.leekm.minihttp.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by lkm on 2017/3/20.
 */

public class Helper {

    public static byte[] intToByteL(int value) {
        byte[] result = new byte[4];
        result[0] = (byte) ((value >> 24) & 0xFF);
        result[1] = (byte) ((value >> 16) & 0xFF);
        result[2] = (byte) ((value >> 8) & 0xFF);
        result[3] = (byte) (value & 0xFF);
        return result;
    }

    public static String intToIp(int value) {
        return (value & 0xFF) + "."
                + ((value >> 8) & 0xFF) + "."
                + ((value >> 16) & 0xFF) + "."
                + ((value >> 24) & 0xFF);
    }

    public static String byteToIp(byte[] values) {
        StringBuffer stringBuffer = new StringBuffer(8);
        for (byte value : values) {
            stringBuffer.append(value & 0xFF);
            stringBuffer.append('.');
        }
        stringBuffer.setLength(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    public static String drain(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int buffer;
        while ((buffer = inputStream.read()) >= 0) {
            baos.write(buffer);
        }
        return baos.toString();
    }

    private final static Random random = new Random();
    public static String randBase64Str(int rawSize) {
        byte[] buffer = new byte[rawSize];
        for (int i = 0;i < rawSize;++i) {
            buffer[i] = (byte) random.nextInt();
        }
        return Base64.encodeToString(buffer, Base64.NO_WRAP);
    }
}
