package servlets;

import models.Product;
import services.interfaces.BasketInterface;
import services.interfaces.ShopService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/basket")
public class BasketServlet extends HttpServlet {
    private ShopService shopService;

    private BasketInterface basketService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Long, Integer> basketItems = basketService.getBasketFromCookies(req);

        List<Product> products = new ArrayList<>();
        int totalPrice = 0;

        for (Map.Entry<Long, Integer> entry : basketItems.entrySet()) {
            Optional<Product> productOpt = shopService.getProductById(entry.getKey());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                products.add(product);
                totalPrice += product.getPrice() * entry.getValue();
            }
        }

        req.setAttribute("basketProducts", products);
        req.setAttribute("basketItems", basketItems);
        req.setAttribute("totalPrice", totalPrice);
        req.getRequestDispatcher("/jsp/basket.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String productIdParameter = req.getParameter("productId");

        if (productIdParameter == null || productIdParameter.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
            return;
        }

        Long productId = Long.parseLong(productIdParameter);
        Map<Long, Integer> basketItems = basketService.getBasketFromCookies(req);

        switch (action) {
            case "add":
                basketItems.put(productId, basketItems.getOrDefault(productId, 0) + 1);
                break;
            case "update":
                String quantityParam = req.getParameter("quantity");
                if (quantityParam != null) {
                    int quantity = Integer.parseInt(quantityParam);
                    if (quantity <= 0) {
                        basketItems.remove(productId);
                    } else {
                        basketItems.put(productId, quantity);
                    }
                }
                break;
            case "remove":
                basketItems.remove(productId);
                break;
            case "clear":
                basketItems.clear();
                break;
        }

        basketService.saveBasketToCookies(basketItems, resp);

        if ("add".equals(action)) {
            resp.sendRedirect("/shop?added=true");
        } else {
            resp.sendRedirect("/basket");
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        shopService = (ShopService) config.getServletContext().getAttribute("shopService");
        basketService = (BasketInterface) config.getServletContext().getAttribute("basketService");
    }
}
