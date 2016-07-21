<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="container-fluid col-sm-12">
    <div class="panel panel-default ">
        <div class="panel-heading">
            <strong>My friends</strong>
        </div>
        <form action="/myprofile" method="get" id="seachProfileForm" role="form">
            <table class="table table-hover">
                <c:forEach var="profile" items="${profilesList}">
                    <tr>
                        <c:if test ="${user.id != profile.id}">
                            <td class="col-md-6">
                                <a href="profile?id=${profile.id}" id="profile" title="View profile">
                                        ${profile.name}
                                </a>
                            <td>
                            <td class="col-md-3">${profile.email}<td>
                            <td  class="col-md-1">
                                <a href="conversation?id=${profile.id}" id="conversation" title="Send message">
                                    <span class="glyphicon glyphicon-envelope"></span>
                                </a>
                            </td>
                            <td class="col-md-1">
                                <a href="#" id="addFriend" title="Add friend"
                                   onclick="document.getElementById('action').value = 'addFriend';
                                           document.getElementById('id').value = '${profile.id}';
                                           document.getElementById('profileForm').submit();">
                                    <span class="glyphicon glyphicon-plus-sign"></span>
                                </a>
                            </td>
                            <td class="col-md-1">
                                <a href="#" id="removeFriend" title="Remove from friends"
                                   onclick="document.getElementById('action').value = 'removeFriend';
                                           document.getElementById('id').value = '${profile.id}';
                                           document.getElementById('profileForm').submit();">
                                    <span class="glyphicon glyphicon-minus-sign"></span>
                                </a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
        </form>
        <form action ="/myprofile" method="post" id="profileForm" role="form">
            <input type="hidden" id="id" name="id">
            <input type="hidden" id="action" name="action">
        </form>
    </div>
</div>
