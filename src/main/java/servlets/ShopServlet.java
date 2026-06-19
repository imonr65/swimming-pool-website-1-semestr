package servlets;

import models.Product;
import models.ProductCategory;
import services.interfaces.ShopService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/shop")
public class ShopServlet extends HttpServlet {

    private ShopService shopService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String categoryIdParameter = req.getParameter("categoryId");
            String minPriceParameter = req.getParameter("minPrice");
            String maxPriceParameter = req.getParameter("maxPrice");
            String availableParameter = req.getParameter("available");

            Long categoryId = null;
            Integer minPrice = null;
            Integer maxPrice = null;
            Boolean isAvailable = null;

            if (categoryIdParameter != null && !categoryIdParameter.isEmpty()) {
                categoryId = Long.parseLong(categoryIdParameter);
            }
            if (minPriceParameter != null && !minPriceParameter.isEmpty()) {
                minPrice = Integer.parseInt(minPriceParameter);
            }
            if (maxPriceParameter != null && !maxPriceParameter.isEmpty()) {
                maxPrice = Integer.parseInt(maxPriceParameter);
            }
            if (availableParameter != null && !availableParameter.isEmpty()) {
                isAvailable = Boolean.parseBoolean(availableParameter);
            }

            List<Product> products;
            if (categoryId != null || minPrice != null || maxPrice != null || isAvailable != null) {
                products = shopService.getFilteredProducts(categoryId, minPrice, maxPrice, isAvailable);
            } else {
                products = shopService.getAllProducts();
            }

            List<ProductCategory> categories = shopService.getAllProductCategories();

            req.setAttribute("products", products);
            req.setAttribute("categories", categories);
            req.setAttribute("selectedCategory", categoryId);
            req.setAttribute("selectedMinPrice", minPrice);
            req.setAttribute("selectedMaxPrice", maxPrice);
            req.setAttribute("selectedAvailable", isAvailable);

            req.getRequestDispatcher("/jsp/shop.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Ошибка при загрузке магазина", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        shopService = (ShopService) config.getServletContext().getAttribute("shopService");
    }
}
