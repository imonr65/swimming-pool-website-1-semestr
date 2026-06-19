package services.impl;

import models.FileInfo;
import repository.interfaces.FilesRepository;
import services.interfaces.FilesService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.UUID;

public class FilesServiceImpl implements FilesService {

    private FilesRepository filesRepository;

    public FilesServiceImpl(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    @Override
    public void saveFileToStorage(InputStream file, String orinalFileName, String contentType, Long size) {
        FileInfo fileInfo = FileInfo.builder()
                .originalFileName(orinalFileName)
                .storageFileName(UUID.randomUUID().toString())
                .type(contentType)
                .size(size)
                .build();

        try {
            Files.copy(file, Paths.get("C://filesForProject/" + fileInfo.getStorageFileName() + "." + fileInfo.getType().split("/")[1]));
            filesRepository.save(fileInfo);
        } catch (IOException e) {
            throw new IllegalStateException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void writeFileFromStorage(Long fileId, OutputStream outputStream) {
        FileInfo fileInfo = filesRepository.findById(fileId);
        File file = new File("C://filesForProject/" + fileInfo.getStorageFileName() + "." + fileInfo.getType().split("/")[1]);
        try {
            Files.copy(file.toPath(), outputStream);
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public FileInfo getFileInfo(Long fileId) {
        return filesRepository.findById(fileId);
    }

    @Override
    public Long saveFileToStorageAndGetId(InputStream file, String originalFileName, String contentType, Long size) {
        FileInfo fileInfo = FileInfo.builder()
                .originalFileName(originalFileName)
                .storageFileName(UUID.randomUUID().toString())
                .type(contentType)
                .size(size)
                .build();
        try {
            Files.copy(file, Paths.get("C://filesForProject/" + fileInfo.getStorageFileName() + "."
                    + fileInfo.getType().split("/")[1]));
            return filesRepository.saveAndReturnId(fileInfo);
        } catch (IOException e) {
            throw new IllegalStateException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
