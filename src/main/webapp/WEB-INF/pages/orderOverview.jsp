<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <h2>Order details :</h2>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Price</td>
            <td>Amount</td>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.items}">
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
                    <a href="${pageContext.servletContext.contextPath}/productPriceHistory/${item.product.id}">
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/></a>
                </td>
                <td class="quantity">
                    <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        ${item.quantity}
                </td>
            </tr>
        </c:forEach>
    </table>

    <h2>Delivery & payment</h2>
    <c:if test="${not empty errors}">
        <div class="error" style="color:red;font-weight: bold">
            Some of the fields were not saved.
        </div>
    </c:if>
    <table>
        <tr>
            <td>First name</td>
            <td>
                    ${order.firstName}
            </td>
        </tr>
        <tr>
            <td>Last name</td>
            <td>
                    ${order.lastName}
            </td>
        </tr>
        <tr>
            <td>Phone number</td>
            <td>
                    ${order.phone}
            </td>
        </tr>
        <tr>
            <td>Delivery address</td>
            <td>
                    ${order.deliveryAddress}
            </td>
        </tr>
        <tr>
            <td>Delivery date</td>
            <td>
                    ${order.deliveryDate}
            </td>
        </tr>
        <tr>
            <td>Payment method</td>
            <td>
                <c:choose>
                    <c:when test="${order.paymentMethod eq 'CASH'}">
                        Cash
                    </c:when>
                    <c:otherwise>
                        Credit card
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <p></p>
    <table>
        <tr>
            <td>Items purchased :</td>
            <td><fmt:formatNumber value="${order.totalQuantity}" var="quantity"/>
                    ${order.totalQuantity}</td>
        </tr>
        <tr>
            <td>Order summary :</td>
            <td>
                <div class="tooltip">
                    <fmt:formatNumber value="${order.totalCost}" type="currency"
                                      currencySymbol="${order.items[0].product.currency.symbol}"/>
                    <span class="tooltiptext">
                            <-
                            <fmt:formatNumber value="${order.subtotal}" type="currency"
                                              currencySymbol="${order.items[0].product.currency.symbol}"/>
                        worth of products + <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                                              currencySymbol="${order.items[0].product.currency.symbol}"/>
                        delivery
                        </span>
                </div>
            </td>
        </tr>
    </table>
    </form>
</tags:master>