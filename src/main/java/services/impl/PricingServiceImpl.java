package services.impl;

import dto.PriceDto;
import models.Category;
import models.PeopleType;
import models.Pricing;
import repository.interfaces.PricingRepository;
import services.interfaces.CategoriesService;
import services.interfaces.PeopleTypesService;
import services.interfaces.PricingInterface;
import services.interfaces.WorkoutsService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PricingServiceImpl implements PricingInterface {

    private final PricingRepository pricingRepository;
    private final CategoriesService categoriesService;
    private final PeopleTypesService peopleTypesService;
    private final WorkoutsService workoutsService;

    public PricingServiceImpl(PricingRepository pricingRepository,
                              CategoriesService categoriesService,
                              PeopleTypesService peopleTypesService,
                              WorkoutsService workoutsService) {
        this.pricingRepository = pricingRepository;
        this.categoriesService = categoriesService;
        this.peopleTypesService = peopleTypesService;
        this.workoutsService = workoutsService;
    }

    @Override
    public void addPricing(PriceDto pricingDto) throws SQLException {
        Category category = categoriesService.getCategory(pricingDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        PeopleType peopleType = peopleTypesService.getPeopleTypeById(pricingDto.getPeopleTypeId())
                .orElseThrow(() -> new IllegalArgumentException("People type not found"));

        Pricing pricing = Pricing.builder()
                .name(pricingDto.getName())
                .category(category)
                .priceForOne(pricingDto.getPriceForOne())
                .period(pricingDto.getPeriod())
                .peopleType(peopleType)
                .build();

        pricingRepository.save(pricing);
    }

    @Override
    public void deletePricing(Integer id) throws SQLException {
        pricingRepository.removeById(id);
    }

    @Override
    public Pricing getPricingById(Integer id) throws SQLException {
        return pricingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pricing not found with id: " + id));
    }

    @Override
    public List<Pricing> getAllPricings() throws SQLException {
        return pricingRepository.findAll();
    }

    @Override
    public List<Pricing> getAllPricingsWithDetails() throws SQLException {
        return pricingRepository.findAllWithDetails();
    }

    @Override
    public List<Pricing> getPricingsByCategoryAndPeopleType(Integer categoryId, Integer peopleTypeId) throws SQLException {
        return pricingRepository.findByCategoryAndPeopleType(categoryId, peopleTypeId);
    }

    @Override
    public Pricing findPriceForWorkoutAndUserType(Integer workoutId, Integer peopleTypeId) throws SQLException {
        Integer categoryId = workoutsService.getWorkoutById(workoutId)
                .get()
                .getCategory()
                .getId();

        List<Pricing> pricings = pricingRepository.findByCategoryAndPeopleType(categoryId, peopleTypeId);

        if (pricings.isEmpty()) {
            throw new IllegalArgumentException("No pricing found for this workout and user type");
        }

        return pricings.get(0);
    }

    @Override
    public Optional<Pricing> findPricingByCategoryAndPeopleType(Integer categoryId, Integer peopleTypeId) throws SQLException {
        return pricingRepository.findByCategoryIdAndTypeId(categoryId, peopleTypeId);
    }

}
