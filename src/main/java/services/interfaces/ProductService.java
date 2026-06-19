package services.interfaces;

import dto.ProductDto;
import models.ProductCategory;

import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    List<ProductCategory> getAllProductCategories() throws SQLException;

    void addProduct(ProductDto product, Part photoPart) throws SQLException, IOException;

    void deleteProductById(Long id) throws SQLException;

}
