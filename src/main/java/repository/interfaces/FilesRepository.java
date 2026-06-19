package repository.interfaces;


import models.FileInfo;

import java.sql.SQLException;


public interface FilesRepository extends CrudRepository<FileInfo> {
    FileInfo findById(Long id);

    Long saveAndReturnId(FileInfo fileInfo) throws SQLException;

    void removeById(Long id) throws SQLException;
}
