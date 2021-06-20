<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Product List">
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
    <c:if test="${empty cart.items}">
        <a href="${pageContext.servletContext.contextPath}">
            <img class="emptyCartPNG" src="https://www.99fashionbrands.com/wp-content/uploads/2020/12/empty_cart-1200x900.png">
        </a>
    </c:if>
    <c:if test="${not empty cart.items}">
        Total cost :
        <fmt:formatNumber value="${cart.totalCost}" type="currency"
                          currencySymbol="${cart.items[0].product.currency.symbol}"/>
        </p>
        <p>
            Total quantity : ${cart.totalQuantity}
        </p>
        <c:if test="${not empty param.message}">
            <div class="success">
                    ${param.message}
            </div>
        </c:if>
        <c:if test="${not empty errors}">
            <div class="error">
                Some of the items were not updated.
                See below for more info.
            </div>
        </c:if>
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
                </td>
                <td>
                    Amount
                </td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.items}">
                <tr>
                    <td>
                        <img class="product-tile"
                             src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                ${item.product.description}
                        </a>
                    </td>
                    <td>
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                    <td class="quantity">
                        <c:if test="${not empty errors[item.product.id]}">
                            <div class="error">
                                    ${errors[item.product.id]}
                            </div>
                        </c:if>
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <input name="quantity" value="${quantity}" class="quantity"/>
                        <input type="hidden" name="productId" value="${item.product.id}"/>
                    </td>
                    <td>
                        <button form="deleteCartItem"
                                formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p>
            <button>Update</button>
        </p>
        </form>
        <form action="${pageContext.servletContext.contextPath}/checkout">
            <button>Checkout</button>
        </form>
    </c:if>
    <form id="deleteCartItem" method="post">
    </form>
</tags:master>