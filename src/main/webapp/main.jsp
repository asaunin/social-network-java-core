<%--@elvariable id="user" type="model.User"--%>
<%--@elvariable id="currentTab" type="int"--%>
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
    <title>BenthMates</title>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">

    <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <!--    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-formhelpers/2.3.0/js/bootstrap-formhelpers.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-formhelpers/2.3.0/css/bootstrap-formhelpers.min.css">-->

</head>

<script>app.use(express.static('public'));</script>


<body id="body">

<c:if test="${currentTab==null}">
    <c:set var="currentTab" value="profile" scope="session"/>
</c:if>

<div class="container" id="maincont">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="">Benchmates</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="/contact"><span class="glyphicon glyphicon-user"></span> My profile</a></li>
                    <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="row content">
        <div class="col-sm-2">
            <div class="well">
                <img class="img-rounded img-responsive center-block" alt="user_pic" src="/img/deactivated_200.gif">
            </div>
            <div class="well" id="tabs">
                <form action="/logout" method="get">
                    <ul class="nav nav-pills nav-stacked">
                        <li<c:if test="${currentTab=='profile' || currentTab=='contact'}"> class="active"</c:if>>
                            <a href="profile?id=${user.id}">Profile</a>
                        </li>
                        <li<c:if test="${currentTab=='friends'}"> class="active"</c:if>>
                            <a href="friends">Friends</a>
                        </li>
                        <li<c:if test="${currentTab=='users'}"> class="active"</c:if>>
                            <a href="users">Users</a>
                        </li>
                        <li<c:if test="${currentTab=='messages' || currentTab=='conversation'}"> class="active"</c:if>>
                            <a href="messages?id=${user.id}">Messages</a>
                        </li>
                    </ul>
                </form>
            </div>
        </div>
        <div class="col-sm-10">
            <div class="tab-content">
                <div class="tab-pane fade<c:if test="${currentTab=='profile'}"> in active</c:if>" id="profile">
                    <jsp:include page="profile.jsp"/>
                </div>
                <div class="tab-pane fade<c:if test="${currentTab=='friends'}"> in active</c:if>" id="friends">
                    <jsp:include page="friends.jsp"/>
                </div>
                <div class="tab-pane fade<c:if test="${currentTab=='users'}"> in active</c:if>" id="users">
                    <jsp:include page="users.jsp"/>
                </div>
                <div class="tab-pane fade<c:if test="${currentTab=='conversation'}"> in active</c:if>" id="conversation">
                    <jsp:include page="conversation.jsp"/>
                </div>
                <div class="tab-pane fade<c:if test="${currentTab=='messages'}"> in active</c:if>" id="messages">
                    <jsp:include page="messages.jsp"/>
                </div>
                <div class="tab-pane fade<c:if test="${currentTab=='contact'}"> in active</c:if>" id="contact">
                    <jsp:include page="contact.jsp"/>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-footer">
        </div>
    </div>
</div>

</body>
</html>

<script>
    <!--Need to be replaced as injections for success functions-->
    function searchFriend(currentTab) {
        var searchText;
        if (currentTab=='friends')
            searchText = $('input[name="friendsSearchText"]').val();
        else
            searchText = $('input[name="usersSearchText"]').val();
        $.ajax({
            url: '/' + currentTab,
            type: 'get',
            data: {
                searchText: searchText
            },
            success: function (data) {
                $('body').html(data);
            },
            error: function (xhr) {
                $("#maincont").html(xhr.responseText);
            }
        });
    }
    function addFriend(id) {
        $.ajax({
            url: '/users',
            type: 'post',
            data: {
                id: id,
                action: "addFriend"
            },
            success: function (data) {
                $('body').html(data);
            },
            error: function (xhr) {
                $("#maincont").html(xhr.responseText);
            }
        });
    }
    function removeFriend(id) {
        $.ajax({
            url: '/users',
            type: 'post',
            data: {
                id: id,
                action: "removeFriend"
            },
            success: function (data) {
                $('body').html(data);
            },
            error: function (xhr) {
                $("#maincont").html(xhr.responseText);
            }
        });
    }
</script>