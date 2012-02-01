<%@ page defaultCodec="html" %>
<g:if test="${required}"><joda:dateTimeZoneSelect name="${property}" value="${value}" required=""/></g:if>
<g:else><joda:dateTimeZoneSelect name="${property}" value="${value}"/></g:else>