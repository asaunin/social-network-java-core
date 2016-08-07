<%--@elvariable id="errorMessage" type="String"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style type="text/css">
    <%@include file="/css/chat.css"%>
    <%@include file="/css/bootstrap.min.css"%>
    <%@include file="/css/bootstrap-formhelpers.min.css"%>
</style>
<script>
    <%@include file="/js/jquery.min.js"%>
    <%@include file="/js/avatar.js"%>
    <%@include file="/js/bootstrap.min.js"%>
    <%@include file="/js/bootstrap-formhelpers.min.js"%>
</script>

<html>
<head>
    <title>Login</title>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
</head>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale" var="loc"/>
<fmt:message bundle="${loc}" key="local.label.sign_up_here" var="sign_up_here"/>
<fmt:message bundle="${loc}" key="local.label.dont_have_acc" var="dont_have_acc"/>
<fmt:message bundle="${loc}" key="local.label.sign_in_continue" var="sign_in_continue"/>
<fmt:message bundle="${loc}" key="local.input.email" var="email"/>
<fmt:message bundle="${loc}" key="local.input.password" var="password"/>
<fmt:message bundle="${loc}" key="local.button.sign_in" var="sign_in"/>

<body>
<div class="container" style="margin-top:40px">
    <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <strong>${sign_in_continue}</strong>
                </div>
                <div class="panel-body">
                    <form role="form" action="/login" method="post" autocomplete="off">
                        <div class="col-sm-12 col-md-10 col-md-offset-1 ">
                            <div class="form-group">
                                <div class="input-group">
                                      <span class="input-group-addon">
                                          <i class="glyphicon glyphicon-user"></i>
                                      </span>
                                    <input class="form-control" placeholder='${email}' value='${param.email}' name="email" type="text" autofocus required autocomplete="off" maxlength="255" >
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="input-group">
                                      <span class="input-group-addon">
                                          <i class="glyphicon glyphicon-lock"></i>
                                      </span>
                                    <input class="form-control" placeholder='${password}' name="password" type="password" value="" autocomplete="off" required maxlength="255">
                                </div>
                            </div>
                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger">
                                    <strong>
                                        ${errorMessage}
                                    </strong>
                                </div>
                            </c:if>
                            <div class="form-group">
                                <input type="submit" class="btn btn-lg btn-primary btn-block" value=${sign_in}>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="panel-footer">
                    ${dont_have_acc} <a href="registration.jsp" onClick=""> ${sign_up_here} </a>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="localisation.jsp"/>
</div>

</body>
</html>