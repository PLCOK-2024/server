package com.example.demo;

import jakarta.persistence.*;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Profile("test")
@Service
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private Map<String, String> tableIdColumns;

    @Override
    public void afterPropertiesSet() {
        tableIdColumns = entityManager.getMetamodel().getEntities().stream()
                .filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
                .collect(Collectors.toMap(
                        entity -> camelCaseToSnakeCase(entity.getName()),
                        entity -> getIdColumnName(entity)
                ));
    }

    private String getIdColumnName(EntityType<?> entity) {
        return "id";
    }

    private static String camelCaseToSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for (Map.Entry<String, String> entry : tableIdColumns.entrySet()) {
            String tableName = entry.getKey();
            String idColumn = entry.getValue();
            if (!isTableExists(tableName)) {
                continue;
            }
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    private boolean isTableExists(String tableName) {
        String checkTableQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = :tableName";
        Query query = entityManager.createNativeQuery(checkTableQuery);
        query.setParameter("tableName", tableName.toUpperCase());
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }
}