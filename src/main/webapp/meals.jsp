<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 09.02.2021
  Time: 16:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <%--<th>Delite</th>
        <th>Upudate</th>--%>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${meals}">
        <tr style="color:${meal.excess ? 'red' : 'green'}">
            <td><fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                <fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${parsedDateTime}"/></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
                <%-- <td><a href="UserController?action=edit&userId=<c:out value="${user.userid}"/>">Update</a></td>
                 <td><a href="UserController?action=delete&userId=<c:out value="${user.userid}"/>">Delete</a></td>
                 --%>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--
<p><a href="UserController?action=insert">Add User</a></p>--%>
</body>
</html>
