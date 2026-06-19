package services.impl;

import services.interfaces.BasketInterface;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class BasketServiceImpl implements BasketInterface {

    public Map<Long, Integer> getBasketFromCookies(HttpServletRequest req) {
        Map<Long, Integer> basketItems = new HashMap<>();
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("basket".equals(cookie.getName())) {
                    String encodedValue = cookie.getValue();
                    if (encodedValue != null && !encodedValue.isEmpty()) {
                        try {
                            byte[] decodedBytes = Base64.getDecoder().decode(encodedValue);
                            String decodedValue = new String(decodedBytes, StandardCharsets.UTF_8);

                            if (!decodedValue.isEmpty()) {
                                String[] items = decodedValue.split(",");
                                for (String item : items) {
                                    String[] parts = item.split(":");
                                    if (parts.length == 2) {
                                        try {
                                            Long id = Long.parseLong(parts[0]);
                                            Integer quantity = Integer.parseInt(parts[1]);
                                            basketItems.put(id, quantity);
                                        } catch (NumberFormatException e) {
                                            System.err.println("Invalid basket item format: " + item);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Error decoding basket cookie: " + e.getMessage());
                        }
                    }
                    break;
                }
            }
        }
        return basketItems;
    }

    public void saveBasketToCookies(Map<Long, Integer> basketItems, HttpServletResponse resp) {
        try {
            StringBuilder basketValue = new StringBuilder();
            for (Map.Entry<Long, Integer> entry : basketItems.entrySet()) {
                if (!basketValue.isEmpty()) {
                    basketValue.append(",");
                }
                basketValue.append(entry.getKey()).append(":").append(entry.getValue());
            }

            String encodedValue = Base64.getEncoder().encodeToString(
                    basketValue.toString().getBytes(StandardCharsets.UTF_8)
            );

            Cookie basketCookie = new Cookie("basket", encodedValue);
            basketCookie.setMaxAge(30 * 24 * 60 * 60);
            basketCookie.setPath("/");
            resp.addCookie(basketCookie);

        } catch (Exception e) {
            System.err.println("Error saving basket to cookies: " + e.getMessage());
            clearBasketCookies(resp);
        }
    }

    private void clearBasketCookies(HttpServletResponse resp) {
        Cookie basketCookie = new Cookie("basket", "");
        basketCookie.setMaxAge(0);
        basketCookie.setPath("/");
        resp.addCookie(basketCookie);
    }

}
