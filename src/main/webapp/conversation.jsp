<%--@elvariable id="profile" type="model.User"--%>
<%--@elvariable id="user" type="model.User"--%>
<%--@elvariable id="messageList" type="java.util.List<model.Message>"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="dot"%>
<div class="container-fluid col-sm-12">
    <div class="panel panel-default">
        <div class="panel-heading">
            Conversation with <strong>${profile.name}</strong>
        </div>
        <div class="panel-body" id="chat" style="max-height: 65%; min-height: 65%; overflow-y: auto;">
            <div class="chat-message">
                <ul class="chat" style="padding: 0; list-style-type: none">
                    <c:forEach var="message" items="${messageList}">
                        <c:choose>
                            <c:when test="${message.sender==user}">
                                <c:set var="alignment" value="left" scope="page"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="alignment" value="right" scope="page"/>
                            </c:otherwise>
                        </c:choose>
                        <li class="${alignment} clearfix">
                            <span class="chat-img pull-${alignment}">
                                <img width="60" height="60" avatar=${message.sender.name}>
                            </span>
                            <div class="chat-body clearfix">
                                <div class="header">
                                    <strong class="primary-font">${message.sender.name}</strong>
                                    <small class="pull-right text-muted"><span class="glyphicon glyphicon-time"></span> <dot:dateOrTime date="${message.date}"/> </small>
                                </div>
                                <div class="last-message text-muted">${message.body}</div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="panel-footer">
            <form action ="/conversation" method="post" id="conversationForm" role="form">
                <div class="form-group">
                    <textarea class="form-control" rows="3" required maxlength="255" id="messageBody" name="messageBody"></textarea>
                </div>
                <input type="hidden" name="id" value=${profile.id}>
                <button type="submit" class="btn btn-primary" name="action" value="sendMessage">Send</button>
            </form>
        </div>
    </div>
</div>
<!--<script>
    $(document).ready(function(){
        $("#sendMessage").click(function(){
            $.ajax({
                url: '/sendMessage',
                type: 'post',
                data: { id: ${profile.id}, body: $("#messageBody").val() },
                success: function (response) {
                    location.reload();
                    //Добавить append последнего сообщения
                    $("#messageBody").val("");
                    $("#chat").scrollTop($("#chat").height);
                }
            });
        });
    });
</script>-->
<script>
    $(document.getElementById("chat")).scroll(function() {
        if($(document.getElementById("chat")).scrollTop() == 0) {
//            alert("Got to the top");
        }
    });
</script>
<script>
    var obj = $('.panel-body');
    var t = obj.val();
    if(obj.length){
        //To put cursor at the end
        obj.focus().val(obj.val());
        //To scroll the textarea
        obj.scrollTop(obj.height());
    }
</script>