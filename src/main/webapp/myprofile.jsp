<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style type="text/css">
    <%@include file="/css/chat.css"%>
</style>
<script>
    <%@include file="/js/avatar.js"%>
</script>

<html>
<head>
    <title>BenthMates</title>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <!--<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.2/css/bootstrap-select.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/0.8.2/css/flag-icon.min.css">-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.2/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.2/js/bootstrap-select.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>

<body>

<c:if test="${currentTab==null}">
    <c:set var="currentTab" value="profile" scope="session"/>
</c:if>

<div class="container">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">Benchmates</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#"><span class="glyphicon glyphicon-user"></span> My profile</a></li>
                    <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="row content">
        <div class="col-sm-2">
            <div class="well">
                <img class="img-rounded img-responsive center-block" alt="user_pic" src="http://vk.com/images/deactivated_200.gif">
            </div>
            <div class="well" id="tabs">
                <form action="/logout" method="get">
                    <ul class="nav nav-pills nav-stacked">
                        <li<c:if test="${currentTab=='profile'}"> class="active"</c:if>>
                            <a href="profile">Profile</a>
                        </li>
                        <li<c:if test="${currentTab=='friends'}"> class="active"</c:if>>
                            <a href="friends">Friends</a>
                        </li>
                        <li<c:if test="${currentTab=='messages' || currentTab=='conversation'}"> class="active"</c:if>>
                            <a href="messages">Messages</a>
                        </li>
                        <li<c:if test="${currentTab=='followers'}"> class="active"</c:if>>
                            <a href="followers">Followers</a>
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
                <div class="tab-pane fade<c:if test="${currentTab=='conversation'}"> in active</c:if>" id="conversation">
                    <jsp:include page="conversation.jsp"/>
                </div>
                <div class="tab-pane fade<c:if test="${currentTab=='followers'}"> in active</c:if>" id="followers">
                    <h3>Followers</h3>
                    <p>Some content</p>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-footer">
        </div>
    </div>
</div>

<!--<script>
    $(document).ready(function(){
        $(".nav-pills a").click(function(){
            //$(this).tab('show');
            //alert($(this).attr('id'));
            //$.post($(this).attr('id'), { name:"Donald", town:"Ducktown" });
            $.ajax({
                url : $(this).attr('id'),
                type: 'get',
                data : {
                    userName : 'fhdh'
               }
            });
        });
    });
</script>-->

</body>
</html>