<%--suppress ELValidationInJSP --%>
<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 10.02.2021
  Time: 1:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <title>Edit meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meals</h2>
<form action="meals" method="post">
    <input type="hidden" name="id" value="${meal.id}"/>
    <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
    <fmt:formatDate pattern="yyyy-MM-dd" value="${parsedDateTime}" var="parsedDate"/>
    <fmt:formatDate pattern="HH:mm" value="${parsedDateTime}" var="parsedTime"/>
    <table>
        <tr>
            <td>Date</td>
            <td>
                <label><input type="date" name="date" value="${parsedDate}"/></label>
            </td>
        </tr>
        <tr>
            <td>Time</td>
            <td>
                <label><input type="time" name="time" value="${parsedTime}"/></label>
            </td>
        </tr>
        <tr>
            <td>Description</td>
            <td>
                <label><input type="text" name="description" value="${meal.description}"/></label>
            </td>
        </tr>
        <tr>
            <td>Calories</td>
            <td>
                <label><input type="number" name="calories" value="${meal.calories}"/></label>
            </td>
        </tr>
        <tr>
            <td>
                <label><input type="submit" /></label>
            </td>
        </tr>
    </table>

</form>

</body>
</html>
