<%--@elvariable id="currentTab" type="int"--%>
<%--@elvariable id="user" type="model.User"--%>
<%--@elvariable id="numberOfUserPages" type="int"--%>
<%--@elvariable id="currentUserPage" type="int"--%>
<%--@elvariable id="userList" type="java.util.List<model.User>"--%>
<%--@elvariable id="searchText" type="string"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="container-fluid col-sm-12">
    <div class="panel panel-default ">
        <div class="panel-heading">
            <div class="pull-left" style="padding-top: 7px;">
                <strong>
                    Friends
                </strong>
            </div>
            <div class="input-group input-group-sm">
                <input type="text" name="friendsSearchText" id="friendsSearchText" class="form-control" placeholder="Search..."
                       value='${searchText}'
                       style="width: 70%; float: right" onchange="searchFriend('friends')">
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="button" onclick="searchFriend('friends')">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </span>
            </div>
        </div>
        <form action="/main" method="get" id="friendsList" role="form">
            <table class="table table-hover">
                <c:forEach var="user_profile" items="${userList}">
                    <tr>
                        <c:if test="${user.id != user_profile.id}">
                            <td class="col-md-7">
                                <a href="profile?id=${user_profile.id}" id="profile" title="View profile">
                                        ${user_profile.name}
                                </a>
                            <td>
                            <td class="col-md-3">
                                    <c:if test="${user_profile.isfriendofuser}">${user_profile.email}</c:if>
                            <td>
                            <td class="col-md-1">
                                <a href="conversation?id=${user_profile.id}" id="conversation" title="Send message">
                                    <span class="glyphicon glyphicon-envelope"></span>
                                </a>
                            </td>
                            <td class="col-md-1">
                                <c:choose>
                                    <c:when test="${user_profile.isuserfriend}">
                                        <a href="#" title="Remove from friends"
                                           onclick="removeFriend(${user_profile.id})">
                                            <span class="glyphicon glyphicon-minus-sign"></span>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="#" title="Add friend" onclick="addFriend(${user_profile.id})">
                                            <span class="glyphicon glyphicon-plus-sign"></span>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
        </form>
    </div>
    <div class="container-fluid col-sm-12" align="center">
        <ul class="pagination">
            <c:forEach var="userPage" begin="1" end="${numberOfUserPages}">
                <li<c:if test="${currentUserPage==userPage}"> class="active"</c:if>>
                    <a href="friends?userPage=${userPage}">${userPage}</a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
