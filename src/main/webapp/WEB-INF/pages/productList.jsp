<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <input name="query" value="${param.query}" placeholder="Enter product name...">
        <button>Search</button>
    </form>
    <c:if test="${not empty param.error}">
        <div class="error" style="color:red;font-weight: bold">
                ${param.error}
        </div>
    </c:if>
    <c:if test="${not empty param.message}">
        <div class="success" style="color:green;font-style: italic">
                ${param.message}
        </div>
    </c:if>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description
                <a href="?sort=description&order=asc&query=${param.query}">↑</a>
                <a href="?sort=description&order=desc&query=${param.query}">↓</a>
            </td>
            <td>Price
                <a href="?sort=price&order=asc&query=${param.query}">↑</a>
                <a href="?sort=price&order=desc&query=${param.query}">↓</a>
            </td>
            <td>Quick checkout</td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
                <td>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
                <td>
                    <form id="addCartItem${product.id}">
                        <input name="quantity" value="1" size="1" style="text-align: right">
                        <input name="productId" type="hidden" value="${product.id}">
                        <button form="addCartItem${product.id}" formmethod="post">
                            Add to
                            <img src="http://cdn.onlinewebfonts.com/svg/img_60846.png" style="height: 12px"/>
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <footer>
        <jsp:include page="footer.jsp"/>
    </footer>
</tags:master>