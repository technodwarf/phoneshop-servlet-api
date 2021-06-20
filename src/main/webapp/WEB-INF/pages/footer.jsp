<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="recentlyViewed" type="java.util.ArrayList" scope="request"/>

<c:if test="${recentlyViewed.size() > 0}">
    <h3>Recently viewed</h3>
    <table class="priceHistoryRow">
        <tr>
            <c:forEach var="product" items="${recentlyViewed}">
                <td class="textCenterAlign">
                    <img class="product-tile" src="${product.imageUrl}">
                    <br>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                    <br>
                    <a>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
            </c:forEach>
        </tr>
    </table>
</c:if>