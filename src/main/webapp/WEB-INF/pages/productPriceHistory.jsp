<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Price History">
    <p>
            ${product.description}
    </p>
    <table>
        <c:forEach var="productPriceHistory" items="${priceHistoryList}">
            <tr>
                <td>
                    <c:out value="${productPriceHistory.date}"></c:out>
                </td>
                <td>
                    <c:out value="${productPriceHistory.price}"></c:out>
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>