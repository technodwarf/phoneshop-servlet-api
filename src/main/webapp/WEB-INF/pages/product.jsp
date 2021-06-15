<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>

    </p>
    <form method="post">
        <table style="border-spacing: 3px;border-collapse: separate">
            <tr>
                <th colspan="2">${product.description}</th>
            </tr>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>Price</td>
                <td>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>${product.stock}</td>
            </tr>
            <tr>
                <td>quantity</td>
                <td>
                    <input name="quantity">
                    <button>Add to cart</button>
                </td>
                <td>
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
                </td>
            </tr>
        </table>
        <p></p>
        <footer>
            <jsp:include page="footer.jsp"/>
        </footer>
    </form>
</tags:master>