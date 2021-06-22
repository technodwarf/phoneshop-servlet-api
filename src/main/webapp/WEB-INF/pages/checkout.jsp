<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <c:if test="${not empty order.items}">
        <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
            <p></p>
            <c:if test="${not empty param.message}">
                <div class="success">
                        ${param.message}
                </div>
            </c:if>
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
                <div class="error">
                    Some of the fields were not saved.
                </div>
            </c:if>
            <table>
                <tr>
                    <td>First name</td>
                    <td>
                        <c:set var="error" value="${errors['firstName']}"/>
                        <input name="firstName" value="${not empty error ? param['firstName'] : order.firstName}">
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Last name</td>
                    <td>
                        <c:set var="error" value="${errors['lastName']}"/>
                        <input name="lastName" value="${not empty error ? param['lastName'] : order.lastName}">
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Phone number</td>
                    <td>
                        <c:set var="error" value="${errors['phone']}"/>
                        <input name="phone" value="${not empty error ? param['phone'] : order.phone}">
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Delivery address</td>
                    <td>
                        <c:set var="error" value="${errors['deliveryAddress']}"/>
                        <input name="deliveryAddress"
                               value="${not empty error ? param['deliveryAddress'] : order.deliveryAddress}">
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Delivery date</td>
                    <td>
                        <c:set var="error" value="${errors['deliveryDate']}"/>
                        <input type="date" name="deliveryDate"
                               value="${not empty error ? param['deliveryDate'] : order.deliveryDate}">
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Payment method</td>
                    <td>
                        <!--Surely there must be a better way to show selected payment method than this-->
                        <c:set var="error" value="${errors['paymentMethod']}"/>
                        <c:choose>
                            <c:when test="${not empty error}">
                                <select name="paymentMethod">
                                    <option>Cash</option>
                                    <option>Credit card</option>
                                </select>
                                <div class="error">
                                        ${error}
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${order.paymentMethod eq 'CASH' || order.paymentMethod eq null}">
                                        <select name="paymentMethod">
                                            <option selected>Cash</option>
                                            <option>Credit card</option>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <select name="paymentMethod">
                                            <option>Cash</option>
                                            <option selected>Credit card</option>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
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
            <p>
                <button>Place order</button>
            </p>
        </form>
    </c:if>
</tags:master>