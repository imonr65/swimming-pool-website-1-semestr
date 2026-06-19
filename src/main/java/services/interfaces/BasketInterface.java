package services.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface BasketInterface {
    Map<Long, Integer> getBasketFromCookies(HttpServletRequest request);

    void saveBasketToCookies(Map<Long, Integer> basketItems, HttpServletResponse resp);
}
