<%@ page import="com.alex.util.DateTimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <style>
        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 70%;
            margin: auto;
        }

        td, th {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        tr:nth-child(even) {
            background-color: #dddddd;
        }

        h2 {
            text-align: center;
        }

        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <table>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        <jsp:useBean id="meals" scope="request" type="java.util.List"/>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="com.alex.model.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                    <%=DateTimeUtil.toString(meal.getDateTime())%>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
