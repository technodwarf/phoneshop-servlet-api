<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Advanced search
    </p>
    <form>
        <table>
            <tr>
                <td>Description</td>
                <td>
                    <input name="query" value="${param.query}" placeholder="Enter product name...">
                    <c:choose>
                        <c:when test="${param.searchType eq 'all words'}">
                            <select name="searchType">
                                <option selected>all words</option>
                                <option>any word</option>
                            </select>
                        </c:when>
                        <c:otherwise>
                            <select name="searchType">
                                <option selected>all words</option>
                                <option selected>any word</option>
                            </select>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td>Min price :</td>
                <td>
                    <input name="minprice" value="${param.minprice}">
                </td>
            </tr>
            <tr>
                <td>Max price :</td>
                <td>
                    <input name="maxprice" value="${param.maxprice}">
                </td>
            </tr>
            <tr>
                <td>
                    <button>Search</button>
                </td>
            </tr>
        </table>
    </form>
    <c:if test="${not empty param.error}">
        <div class="error">
                ${param.error}
        </div>
    </c:if>
    <c:if test="${not empty param.message}">
        <div class="success">
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
                    <a href="${pageContext.servletContext.contextPath}/productPriceHistory/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td>
                    <form id="addCartItem${product.id}">
                        <input class="textRightAlign" name="quantity" value="1" size="1">
                        <input name="productId" type="hidden" value="${product.id}">
                        <button form="addCartItem${product.id}" formmethod="post">
                            Add to
                            <img class="smallCart" src="http://cdn.onlinewebfonts.com/svg/img_60846.png">
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
