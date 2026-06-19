<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Корзина</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="shop-container">
    <h1>Корзина</h1>

    <c:if test="${empty basketProducts}">
        <div class="empty-cart">
            <p>Ваша корзина пуста</p>
            <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary">Вернуться в магазин</a>
        </div>
    </c:if>

    <c:if test="${not empty basketProducts}">
        <div class="cart-items">
            <c:forEach items="${basketProducts}" var="product">
                <div class="cart-item">
                    <div class="item-image strict"> <!-- Добавлен класс strict -->
                        <c:if test="${not empty product.photoId}">
                            <img src="${pageContext.request.contextPath}/uploaded/files?id=${product.photoId}"
                                 alt="${product.name}">
                        </c:if>
                        <c:if test="${empty product.photoId}">
                            <div class="no-image">Нет изображения</div>
                        </c:if>
                    </div>

                    <div class="item-details">
                        <h3>${product.name}</h3>
                        <p class="item-price">${product.price} руб. × ${basketItems[product.id]} шт.</p>
                        <p class="item-total">Итого: ${product.price * basketItems[product.id]} руб.</p>
                    </div>

                    <div class="item-actions">
                        <form action="${pageContext.request.contextPath}/basket" method="post" class="quantity-form">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="productId" value="${product.id}">
                            <input type="number" name="quantity" value="${basketItems[product.id]}"
                                   min="1" max="99" class="quantity-input">
                            <button type="submit" class="btn btn-secondary">Обновить</button>
                        </form>

                        <form action="${pageContext.request.contextPath}/basket" method="post" class="remove-form">
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="productId" value="${product.id}">
                            <button type="submit" class="btn btn-danger">Удалить</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="cart-summary">
            <div class="total-price">
                <h3>Общая сумма: ${totalPrice} руб.</h3>
            </div>

            <div class="cart-actions">
                <form action="${pageContext.request.contextPath}/basket" method="post">
                    <input type="hidden" name="action" value="clear">
                    <button type="submit" class="btn btn-danger">Очистить корзину</button>
                </form>

                <a href="${pageContext.request.contextPath}/shop" class="btn btn-secondary">Продолжить покупки</a>

                <button class="btn btn-primary" onclick="checkout()">Оформить заказ</button>
            </div>
        </div>
    </c:if>
</div>

<script>
    function checkout() {
        alert('Функция оформления заказа будет реализована позже');
    }
</script>

<script>
    function resizeBasketImages() {
        const basketImages = document.querySelectorAll('.item-image.strict img');

        basketImages.forEach(img => {
            if (img.complete) {
                resizeBasketImage(img);
            } else {
                img.addEventListener('load', function () {
                    resizeBasketImage(this);
                });
            }
            applyBasketImageStyles(img);
        });
    }

    function resizeBasketImage(img) {
        const container = img.closest('.item-image.strict');
        if (!container) return;

        const containerWidth = container.clientWidth - 10;
        const containerHeight = container.clientHeight - 10;

        img.style.maxWidth = containerWidth + 'px';
        img.style.maxHeight = containerHeight + 'px';
        img.style.width = 'auto';
        img.style.height = 'auto';
        img.style.objectFit = 'contain';
    }

    function applyBasketImageStyles(img) {
        img.style.maxWidth = '100%';
        img.style.maxHeight = '120px';
        img.style.width = 'auto';
        img.style.height = 'auto';
        img.style.objectFit = 'contain';
        img.style.display = 'block';
        img.style.margin = '0 auto';
    }

    document.addEventListener('DOMContentLoaded', function () {
        resizeBasketImages();

        setTimeout(resizeBasketImages, 500);
        setTimeout(resizeBasketImages, 1000);
    });

    window.addEventListener('resize', resizeBasketImages);
</script>

<jsp:include page="footer.jsp"/>
</body>
</html>