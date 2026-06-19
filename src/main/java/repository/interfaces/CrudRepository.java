package repository.interfaces;

import java.sql.SQLException;
import java.util.Optional;

public interface CrudRepository<T> {

    Optional<T> findById(Integer id) throws SQLException;

    void save(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    void remove(T entity) throws SQLException;

}
