package com.plcok.common.storage;

import com.azure.storage.blob.BlobContainerClient;
import com.plcok.common.extension.FileExtension;
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
    @Value("${spring.cloud.azure.storage.blob.container-name}")
    String containerName;

    private final ResourceLoader resourceLoader;

    private final BlobContainerClient blobContainerClient;

    private WritableResource getResource(String name) {
        return (WritableResource) resourceLoader.getResource(String.format("azure-blob://%s/%s", containerName, name));
    }

    public String put(String prefix, MultipartFile file) throws IOException {
        var resource = getResource(String.format("%s/%s.%s", prefix, UUID.randomUUID(), file.getExtension()));

        var stream = resource.getOutputStream();
        stream.write(file.getBytes());
        stream.close();

        return resource.getFilename();
    }

    @Override
    public boolean remove(String path) {
        var blobClient = blobContainerClient.getBlobClient(path);
        return blobClient.deleteIfExists();
    }
}
