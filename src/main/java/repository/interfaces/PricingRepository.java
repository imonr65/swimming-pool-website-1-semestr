package repository.interfaces;

import models.Pricing;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PricingRepository extends CrudRepository<Pricing> {
    List<Pricing> findAll() throws SQLException;

    List<Pricing> findByCategoryAndPeopleType(Integer categoryId, Integer peopleTypeId) throws SQLException;

    List<Pricing> findAllWithDetails() throws SQLException;

    void removeById(Integer id) throws SQLException;

    Optional<Pricing> findByCategoryIdAndTypeId(Integer categoryId, Integer typeId) throws SQLException;
}
