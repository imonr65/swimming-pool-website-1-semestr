package services.interfaces;

import dto.PriceDto;
import models.Pricing;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PricingInterface {
    void addPricing(PriceDto pricingDto) throws SQLException;

    void deletePricing(Integer id) throws SQLException;

    Pricing getPricingById(Integer id) throws SQLException;

    List<Pricing> getAllPricings() throws SQLException;

    List<Pricing> getAllPricingsWithDetails() throws SQLException;

    List<Pricing> getPricingsByCategoryAndPeopleType(Integer categoryId, Integer peopleTypeId) throws SQLException;

    Pricing findPriceForWorkoutAndUserType(Integer workoutId, Integer peopleTypeId) throws SQLException;

    Optional<Pricing> findPricingByCategoryAndPeopleType(Integer categoryId, Integer peopleTypeId) throws SQLException;

}
