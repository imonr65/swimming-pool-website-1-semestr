<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Магазин</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="shop-container">
    <h1>Магазин</h1>

    <c:if test="${param.added}">
        <div class="alert alert-success">
            Товар добавлен в корзину!
        </div>
    </c:if>

    <form class="filter-form" method="get" action="${pageContext.request.contextPath}/shop">
        <label>Категория:
            <select name="categoryId">
                <option value="">Все категории</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}" ${selectedCategory == category.id ? 'selected' : ''}>
                            ${category.categoryName}
                    </option>
                </c:forEach>
            </select>
        </label>

        <label>Цена от:
            <input type="number" name="minPrice" value="${selectedMinPrice}" placeholder="0" min="0">
        </label>

        <label>Цена до:
            <input type="number" name="maxPrice" value="${selectedMaxPrice}" placeholder="100000" min="0">
        </label>

        <label>
            <input type="checkbox" name="available" value="true" ${selectedAvailable ? 'checked' : ''}>
            Только в наличии
        </label>

        <button type="submit">Применить фильтры</button>
        <a href="${pageContext.request.contextPath}/shop" class="btn btn-secondary">Сбросить</a>
    </form>

    <div class="cart-section">
        <a href="${pageContext.request.contextPath}/basket" class="cart-link">
            🛒 Корзина
        </a>
    </div>

    <div class="products-grid">
        <c:forEach items="${products}" var="product">
            <div class="product-card">
                <div class="product-image strict">
                    <c:if test="${not empty product.photoId}">
                        <img src="${pageContext.request.contextPath}/uploaded/files?id=${product.photoId}"
                             alt="${product.name}"
                             onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                        <div class="no-image" style="display: none;">
                            Нет изображения
                        </div>
                    </c:if>
                    <c:if test="${empty product.photoId}">
                        <div class="no-image">
                            Нет изображения
                        </div>
                    </c:if>
                </div>

                <h3>${product.name}</h3>
                <p class="description">${product.description}</p>
                <p class="price">Цена: ${product.price} руб.</p>
                <p class="category">Категория: ${product.productCategory.categoryName}</p>
                <p class="availability ${product.isAvailable ? 'available' : 'not-available'}">
                        ${product.isAvailable ? '✓ В наличии' : '✗ Нет в наличии'}
                </p>

                <div class="action-buttons">
                    <c:if test="${product.isAvailable}">
                        <form action="${pageContext.request.contextPath}/basket" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${product.id}">
                            <button type="submit" class="btn btn-success">В корзину</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty products}">
        <div class="no-products">
            <p>Товары не найдены. Попробуйте изменить параметры фильтрации.</p>
        </div>
    </c:if>
</div>
<jsp:include page="footer.jsp"/>
<script>
    function resizeProductImages() {
        const productImages = document.querySelectorAll('.product-image.strict img');

        productImages.forEach(img => {
            if (img.complete) {
                resizeImage(img);
            } else {
                img.addEventListener('load', function () {
                    resizeImage(this);
                });
            }
        });
    }

    function resizeImage(img) {
        const container = img.closest('.product-image.strict');
        if (!container) return;

        const containerWidth = container.clientWidth;
        const containerHeight = container.clientHeight;

        img.style.maxWidth = containerWidth + 'px';
        img.style.maxHeight = containerHeight + 'px';
        img.style.width = 'auto';
        img.style.height = 'auto';
        img.style.objectFit = 'contain';
    }

    document.addEventListener('DOMContentLoaded', resizeProductImages);
    window.addEventListener('resize', resizeProductImages);
</script>
</body>
</html>