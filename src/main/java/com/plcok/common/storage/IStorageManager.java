package com.plcok.common.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IStorageManager {
    String put(String prefix, MultipartFile file) throws IOException;
    boolean remove(String path);
}
