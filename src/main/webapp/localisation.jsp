<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="text-center">
    <form name="localisation" method="post">
        <input type="hidden" name="locale">
        <a href="#" onClick='newLocale("en")' class="label label-default">English</a>
        <a href="#" onClick='newLocale("ru")' class="label label-default">Русский</a>
    </form>
</div>
<script type="text/javascript">
    function newLocale(locale){
        document.forms.localisation.locale.value = locale;
        document.forms.localisation.action="localisation";
        document.forms.localisation.submit();
    }
</script>