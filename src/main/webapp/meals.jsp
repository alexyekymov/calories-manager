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
    <br><br>
    <form method="post" action="meals">
        <input type="hidden" value="${param.id}" name="id">
        <table>
            <tr>
                <th>Date</th>
                <th>Description</th>
                <th>Calories</th>
                <th>Action</th>
            </tr>
            <tr>
                <td>
                    <label>
                        <input type="datetime-local" value="${param.date ne null ? param.date : ''}" name="dateTime" required>
                    </label>
                </td>
                <td>
                    <label>
                        <input type="text" value="${param.description ne null ? param.description : ''}" name="description" required>
                    </label>
                </td>
                <td>
                    <label>
                        <input type="number" value="${param.calories ne null ? param.calories : ''}" name="calories" required>
                    </label>
                </td>
                <td>
                    <button type="submit">Save</button>
                </td>
            </tr>
        </table>
    </form>

    <br><br>
    <table>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th colspan="2" style="text-align: center">Actions</th>
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
                <td>
                    <a href="meals?action=update&id=${meal.id}&date=${meal.dateTime}&description=${meal.description}&calories=${meal.calories}">Update</a>
                </td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
