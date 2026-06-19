package servlets.admins;

import dto.ProductDto;
import models.ProductCategory;
import services.interfaces.ProductService;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/products")
@MultipartConfig
public class AdminProductsServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        productService = (ProductService) config.getServletContext().getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProductCategory> productCategories;
        try {
            productCategories = productService.getAllProductCategories();
            req.setAttribute("activeTab", "products");
            req.setAttribute("productCategories", productCategories);

            req.getRequestDispatcher("/jsp/admin.jsp").forward(req, resp);
        } catch (SQLException e) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Error loading products: " + e.getMessage() + "\"}");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            try {
                String idParam = req.getParameter("id");
                if (idParam == null || idParam.trim().isEmpty()) {
                    resp.setContentType("application/json");
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"status\": \"error\", \"message\": \"Product ID is required\"}");
                    return;
                }

                Long productId = Long.parseLong(idParam);
                productService.deleteProductById(productId);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"status\": \"success\", \"message\": \"Product deleted successfully\"}");
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid product ID format\"}");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"status\": \"error\", \"message\": \"Failed to delete product: " + e.getMessage() + "\"}");
            }
        } else {
            try {
                Part part = req.getPart("photo");

                ProductDto productDto = new ProductDto();
                productDto.setName(req.getParameter("name"));
                productDto.setCategoryId(Long.parseLong(req.getParameter("categoryId")));
                productDto.setDescription(req.getParameter("description"));
                productDto.setPrice(Integer.parseInt(req.getParameter("price")));
                productDto.setIsAvailable(Boolean.valueOf(req.getParameter("isAvailable")));

                productService.addProduct(productDto, part);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"status\": \"success\", \"message\": \"Product added successfully\"}");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"status\": \"error\", \"message\": \"Failed to add product: " + e.getMessage() + "\"}");
            }
        }
    }
}
