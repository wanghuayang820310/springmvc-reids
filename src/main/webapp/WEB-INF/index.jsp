<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript">alert($)</script>
<body>
<h2>com.cht.integration.controller.Hello World!</h2>
<form action="${pageContext.request.contextPath}/hello/testbean" method="post">
    用户ID:<input type="text" name="userid"><br>
    姓名:<input type="text" name="userName"><br>
    年龄:<input type="text" name="age"><br>
    <input name="提交" type="submit">
</form>
</body>
</html>
