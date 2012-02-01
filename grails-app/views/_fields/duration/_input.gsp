<%@ page defaultCodec="html" %>
<g:if test="${required}"><joda:periodPicker name="${property}" value="${value}" required=""/></g:if>
<g:else><joda:periodPicker name="${property}" value="${value}"/></g:else>