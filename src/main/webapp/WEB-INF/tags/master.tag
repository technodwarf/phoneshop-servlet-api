<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<html>
<head>
  <title>${pageTitle}</title>
  <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<body class="product-list">
  <header>
    <a href="${pageContext.servletContext.contextPath}">
      <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
      PhoneShop
    </a>
    <a href="http://localhost:8080/phoneshop-servlet-api/cart" style="position: absolute;right: 25px">
      <img src="http://cdn.onlinewebfonts.com/svg/img_60846.png"/>
      ${cart.totalCost}$
    </a>
  </header>
  <main>
    <jsp:doBody/>
  </main>
  <p>
    - PhoneShop, Vlad Konovalov
  </p>
</body>
</html>