package com.example.demo.common.storage;

import com.example.demo.common.extension.FileExtension;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ExtensionMethod(FileExtension.class)
public class AzureStorageManager implements IStorageManager {
    private final ResourceLoader resourceLoader;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    String containerName = null;

    private WritableResource getResource(String name) {
        return (WritableResource) resourceLoader.getResource(String.format("azure-blob://%s/%s", containerName, name));
    }

    public String put(String prefix, MultipartFile file) throws IOException {
        var resource = getResource(String.format("%s/%s.%s", prefix, UUID.randomUUID(), file.getExtension()));

        var stream = resource.getOutputStream();
        stream.write(file.getBytes());
        stream.close();

        return resource.getURL().toString();
    }
}
