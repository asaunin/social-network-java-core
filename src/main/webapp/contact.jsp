<%--@elvariable id="user" type="model.User"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="container-fluid col-sm-12" style="min-height: 80%;">
    <div class="panel panel-default ">
        <div class="panel-heading">
            <strong>${user.name}</strong>
        </div>
        <div class="panel-body" style="background: #ffffff">
            <form class="form-horizontal" action="/contact" method="post" id="contact_form">
                <label>Contact information:</label>
                <div class="form-group">
                    <label class="col-md-4 control-label">First Name</label>
                    <div class="col-md-4 inputGroupContainer">
                        <div class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                            <input name="first_name" placeholder="First name" class="form-control" type="text" required
                                   maxlength="255" value='${user.first_name}'>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">Last Name</label>
                    <div class="col-md-4 inputGroupContainer">
                        <div class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                            <input name="last_name" placeholder="Last name" class="form-control" type="text" required
                                   maxlength="255" value='${user.last_name}'>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">E-Mail</label>
                    <div class="col-md-4 inputGroupContainer">
                        <div class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
                            <input name="email" placeholder="E-mail" class="form-control" type="text" required
                                   maxlength="255" value='${user.email}'>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">Phone</label>
                    <div class="col-md-4 inputGroupContainer">
                        <div class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-earphone"></i></span>
                            <input name="phone" id="phone" placeholder="9111234567" class="form-control bfh-phone"
                                   type="text" required value='${user.phone}' data-format="+7 (ddd) ddd-dd-dd">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">Birth date</label>
                    <div class="col-md-4 inputGroupContainer">
                        <div class="bfh-datepicker" data-name="birth_date" id="birth_date" data-date='<fmt:formatDate value="${user.birth_date}" pattern="dd.MM.yyyy"/>' data-format="d.m.y"
                             data-min="01/01/1900" data-max="today">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">Sex</label>
                    <div class="col-md-4">
                        <label class="radio-inline"><input type="radio" name="sex" value="1"
                                                           <c:if test="${user.sex==1}">checked</c:if>>Male</label>
                        <label class="radio-inline"><input type="radio" name="sex" value="2"
                                                           <c:if test="${user.sex==2}">checked</c:if>>Female</label>
                    </div>
                </div>
                <div class="form-group">
                    <c:if test="${not empty requestScope.changeContactMessage}">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-4 alert ${requestScope.alertType}">
                            <strong>${requestScope.changeContactMessage}</strong>
                        </div>
                    </c:if>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label"></label>
                    <div class="col-md-4">
                        <button type="submit" class="btn btn-primary" name="action" value="changeContact">Submit contact information</button>
                    </div>
                </div>
            </form>
            <form class="form-horizontal" action="/contact" method="post" id="password_form">
                <hr>
                <label>Change password:</label>
                <div class="form-group">
                    <label class="col-md-4 control-label">Old password</label>
                    <div class="col-md-4 inputGroupContainer">
                        <div class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                            <input type="password" name="old_password" id="old_password" autocomplete="off"
                                   class="form-control" required
                                   maxlength="255">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">New password</label>
                    <div class="col-md-4 inputGroupContainer">
                        <div class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                            <input type="password" name="password" id="password" autocomplete="off" class="form-control"
                                   required
                                   maxlength="255">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label">Confirm password</label>
                    <div class="col-md-4 inputGroupContainer">
                        <div class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                            <input type="password" name="confirm_password" id="confirm_password" autocomplete="off"
                                   class="form-control"
                                   required maxlength="255">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <c:if test="${not empty requestScope.changePasswordMessage}">
                        <label class="col-md-4 control-label"></label>
                        <div class="col-md-4 alert ${requestScope.alertType}">
                            <strong>${requestScope.changePasswordMessage}</strong>
                        </div>
                    </c:if>
                </div>
                <div class="form-group">
                    <label class="col-md-4 control-label"></label>
                    <div class="col-md-4">
                        <button type="submit" class="btn btn-primary" name="action" value="changePassword">Submit new password</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
