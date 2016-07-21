<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container-fluid col-sm-12" style="min-height: 80%;">
     <div class="panel panel-default ">
        <div class="panel-heading">
            <strong>${profile.name}</strong>
        </div>
        <table class="table table-hover">
            <tr>
                <td class="col-md-4">First name:<td>
                <td class="col-md-8">${profile.first_name}<td>
            </td>
            <tr>
                <td class="col-md-4">Last name:<td>
                <td class="col-md-8">${profile.last_name}<td>
            </td>
            <tr>
                <td class="col-md-4">Sex:<td>
                <td class="col-md-8">${profile.sex}<td>
            </td>
            <tr>
                <td class="col-md-4">Date of birth:<td>
                <td class="col-md-8">${profile.birth_date}<td>
            </td>
            <tr>
                <td class="col-md-4">Registered at:<td>
                <td class="col-md-8">${profile.reg_date}<td>
            </td>
         </table>
    </div>
</div>