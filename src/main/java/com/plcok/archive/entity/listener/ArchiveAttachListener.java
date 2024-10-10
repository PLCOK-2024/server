package com.plcok.archive.entity.listener;

import com.plcok.archive.entity.ArchiveAttach;
import com.plcok.common.storage.IStorageManager;
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ArchiveAttachListener {
    private IStorageManager storageManager;

    @Autowired
    public void init(ApplicationContext applicationContext) {
        storageManager = applicationContext.getBean(IStorageManager.class);
    }

    @PreRemove
    public void postRemove(ArchiveAttach archiveAttach) {
        storageManager.remove(archiveAttach.getPath());
    }
}
