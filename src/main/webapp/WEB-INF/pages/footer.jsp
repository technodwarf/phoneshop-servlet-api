<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="recentlyViewed" type="java.util.ArrayList" scope="request"/>

<c:if test="${recentlyViewed.size() > 0}">
    <h3>Recently viewed</h3>
    <table style="border-spacing: 3px;border-collapse: separate">
        <tr>
            <c:forEach var="product" items="${recentlyViewed}">
                <td style="text-align: center">
                    <img class="product-tile" src="${product.imageUrl}" style="text-align: center">
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