<%--@elvariable id="profile" type="model.User"--%>
<%--@elvariable id="user" type="model.User"--%>
<%--@elvariable id="messagesList" type="java.util.List<model.Message>"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="dot"%>
<div class="container-fluid col-sm-12">
    <div class="panel panel-default">
        <div class="panel-heading">
            <strong>Last messages</strong>
        </div>
        <div class="panel-body" id="chat" style="max-height: 65%; min-height: 65%; overflow-y: auto;">
            <div class="chat-message">
                <ul class="chat" style="padding: 0; list-style-type: none">
                    <c:forEach var="message" items="${messagesList}">
                        <c:choose>
                            <c:when test="${message.sender==user}">
                                <c:set var="alignment" value="left" scope="page"/>
                                <c:set var="avatar" value="${message.recipient.name}" scope="page"/>
                                <c:set var="user_profile" value="${message.recipient}" scope="page"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="alignment" value="right" scope="page"/>
                                <c:set var="avatar" value="${message.sender.name}" scope="page"/>
                                <c:set var="user_profile" value="${message.sender}" scope="page"/>
                            </c:otherwise>
                        </c:choose>
                        <a href="conversation?id=${user_profile.id}" id="conversation" title="Send message">
                        <li class="${alignment} clearfix">
                            <span class="chat-img pull-${alignment}">
                                <img width="60" height="60" avatar=${avatar}>
                            </span>
                            <div class="chat-body clearfix">
                                <div class="header">
                                    <strong class="primary-font">${message.sender.name} to ${message.recipient.name}</strong>
                                    <small class="pull-right text-muted"><span class="glyphicon glyphicon-time"></span> <dot:dateOrTime date="${message.date}"/> </small>
                                </div>
                                <div class="last-message text-muted">${message.body}</div>
                            </div>
                        </li>
                        </a>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>
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