<%@ page defaultCodec="html" %>
<g:if test="${required}"><joda:datetimeLocalField name="${property}" value="${value}" required=""/></g:if>
<g:else><joda:datetimeLocalField name="${property}" value="${value}"/></g:else>