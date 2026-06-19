package services.impl;

import dto.ProductDto;
import models.Product;
import models.ProductCategory;
import repository.interfaces.ProductCategoriesRepository;
import repository.interfaces.ProductRepository;
import services.interfaces.FilesService;
import services.interfaces.ProductService;

import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoriesRepository productCategoriesRepository;
    private final FilesService filesService;

    public ProductServiceImpl(ProductRepository productRepository, ProductCategoriesRepository productCategoriesRepository, FilesService filesService) {
        this.productRepository = productRepository;
        this.productCategoriesRepository = productCategoriesRepository;
        this.filesService = filesService;
    }

    @Override
    public List<ProductCategory> getAllProductCategories() throws SQLException {
        return productCategoriesRepository.findAll();
    }

    @Override
    public void addProduct(ProductDto productDto, Part photoPart) throws SQLException, IOException {
        Long photoId = filesService.saveFileToStorageAndGetId(
                photoPart.getInputStream(),
                photoPart.getSubmittedFileName(),
                photoPart.getContentType(),
                photoPart.getSize()
        );
        System.out.println(photoId);
        ProductCategory productCategory = ProductCategory.builder()
                .id(productDto.getCategoryId())
                .build();
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .isAvailable(productDto.getIsAvailable())
                .price(productDto.getPrice())
                .productCategory(productCategory)
                .photoId(photoId)
                .build();

        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) throws SQLException {
        productRepository.removeById(id);
    }
}
