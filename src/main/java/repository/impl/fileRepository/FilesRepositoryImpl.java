package repository.impl.fileRepository;

import models.FileInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import repository.interfaces.FilesRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class FilesRepositoryImpl implements FilesRepository {

    private DataSource dataSource;

    private final static String SQL_INSERT = "insert into files(storage_file_name, original_file_name, type, size)" +
            "values (?, ?, ?, ?)";
    private final static String SQL_SELECT_BY_ID = "select id, storage_file_name, original_file_name, type, size from files where id = ?";

    private static final String SQL_DELETE_BY_ID = "delete from files where id = ?";

    private JdbcTemplate jdbcTemplate;

    private RowMapper<FileInfo> fileRowMapper = (row, rowNumber) ->
            FileInfo.builder()
                    .id(row.getLong("id"))
                    .originalFileName(row.getString("original_file_name"))
                    .storageFileName(row.getString("storage_file_name"))
                    .type(row.getString("type"))
                    .size(row.getLong("size"))
                    .build();

    public FilesRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public FileInfo findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, fileRowMapper, id);
    }

    @Override
    public Long saveAndReturnId(FileInfo fileInfo) throws SQLException {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, fileInfo.getStorageFileName());
            statement.setString(2, fileInfo.getOriginalFileName());
            statement.setString(3, fileInfo.getType());
            statement.setLong(4, fileInfo.getSize());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long generatedId = generatedKeys.getLong(1);
                        fileInfo.setId(generatedId);
                        return generatedId;
                    }
                }
            }
        }
        throw new SQLException("Failed to insert file");
    }

    @Override
    public Optional<FileInfo> findById(Integer id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public void save(FileInfo entity) throws SQLException {
        jdbcTemplate.update(SQL_INSERT, entity.getStorageFileName(),
                entity.getOriginalFileName(),
                entity.getType(),
                entity.getSize());
    }

    @Override
    public void update(FileInfo entity) throws SQLException {

    }

    @Override
    public void remove(FileInfo entity) throws SQLException {

    }

    @Override
    public void removeById(Long id) throws SQLException {
        PreparedStatement statement = dataSource.getConnection().prepareStatement(SQL_DELETE_BY_ID);
        statement.setLong(1, id);
        statement.executeUpdate();
    }
}
