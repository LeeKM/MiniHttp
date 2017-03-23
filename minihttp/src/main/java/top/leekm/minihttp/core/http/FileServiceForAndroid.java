package top.leekm.minihttp.core.http;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import top.leekm.minihttp.core.service.FileService;

/**
 * Created by lkm on 2017/3/23.
 */
public class FileServiceForAndroid implements FileService {

    private static String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    public void setRoot(String root) {
        this.ROOT = root;
    }

    private File getFile(String path) {
        return new File(ROOT + path);
    }

    @Override
    public boolean isFileExist(String path) throws IOException {
        return getFile(path).exists();
    }

    @Override
    public InputStream openReadOnlyFile(String path) throws IOException {
        return new FileInputStream(getFile(path));
    }

    @Override
    public long getFileLength(String path) throws IOException {
        return getFile(path).length();
    }

    @Override
    public OutputStream openWriteOnlyFile(String path) throws IOException {
        return new FileOutputStream(getFile(path));
    }

    @Override
    public boolean deleteFile(String path) throws IOException {
        return false;
    }

    @Override
    public boolean createFile(String path) throws IOException {
        return false;
    }

    @Override
    public boolean createDirs(String path) throws IOException {
        return false;
    }

    @Override
    public boolean deleteDirs(String path) throws IOException {
        return false;
    }
}
