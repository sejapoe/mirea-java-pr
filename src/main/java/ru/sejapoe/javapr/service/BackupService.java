package ru.sejapoe.javapr.service;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.sejapoe.javapr.repo.BaseRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@ManagedResource
@RequiredArgsConstructor
public class BackupService {
    private final List<BaseRepository<?, ?>> repositories;
    private final JsonComponentModule jsonComponentModule;
    private final ObjectMapper jacksonObjectMapper;

    @Value("${backup.directory}")
    private String backupDirectoryPath;

    //    @Scheduled(cron = "0 0,30 * * * *")
    @ManagedOperation
    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    public void doBackup() {
        log.info("Starting backup...");
        try {
            File backupDirectory = new File(backupDirectoryPath);
            if (!backupDirectory.exists()) backupDirectory.mkdir();

            if (!backupDirectory.isDirectory()) {
                log.error("Backup failed: {} is not a directory", backupDirectoryPath);
                return;
            }

            for (File file : Objects.requireNonNull(backupDirectory.listFiles())) {
                file.delete();
            }

            for (BaseRepository<?, ?> repository : repositories) {
                backupEntities(repository);
            }
        } catch (NullPointerException | SecurityException | JsonProcessingException | FileNotFoundException e) {
            log.error("Backup failed", e);
        }
    }

    private void backupEntities(BaseRepository<?, ?> repository) throws JsonProcessingException, FileNotFoundException {
        var repositoryClass = repository.getEntityClass();
        String name = repositoryClass.getName();
        File file = new File(backupDirectoryPath, name + ".json");
        List<?> all = repository.findAll();
        String s = jacksonObjectMapper.writeValueAsString(all);
        try (var writer = new PrintWriter(file)) {
            writer.println(s);
        }
    }

    private static final TypeResolver RESOLVER = new TypeResolver();

//    private Class<?> getEntityClass(BaseRepository<?, ?> repository) {
//        Class<?> targetClass = AopUtils.getTargetClass(repository);
//        Type[] interfaces = targetClass.getInterfaces();
//
//        for (Type t : interfaces) {
//            if (t instanceof Class<?>) {
//                Class<?> clazz = (Class<?>) t;
//
//                if (clazz.getPackage().getName().startsWith("ru.sejapoe")) {
//
//                    // Repositories should implement only ONE interface from application packages
//
//                    Type genericInterface = clazz.getGenericInterfaces()[0];
//
//                    return (Class<?>) ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
//                }
//            }
//        }
//
//        return null;
//    }
}
