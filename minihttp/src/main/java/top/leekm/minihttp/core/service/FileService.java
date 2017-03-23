package top.leekm.minihttp.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lkm on 2017/3/23.
 */

public interface FileService {

    boolean isFileExist(String path) throws IOException;

    InputStream openReadOnlyFile(String path) throws IOException;

    long getFileLength(String path) throws IOException;

    OutputStream openWriteOnlyFile(String path) throws IOException;

    boolean deleteFile(String path) throws IOException;

    boolean createFile(String path) throws IOException;

    boolean createDirs(String path) throws IOException;

    boolean deleteDirs(String path) throws IOException;

}
