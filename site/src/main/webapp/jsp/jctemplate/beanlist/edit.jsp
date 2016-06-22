<tr>
<td class="fieldrow" id="fieldrow_${fieldName}" name="fieldrow_${fieldName}">
  <label for="${fieldName}">${fieldHeader}</label>
</td>
<td>
  <!-- Cloudflare setting -->
  <!--email_off-->
  <select name="${fieldName}" style="float: left" size="10" multiple="true">
    <c:forEach items='${lookupMap.get("${fieldName}")}' var="lookupBean">
      <c:if test="${bean.${fieldName}.contains(lookupBean.id)}">
      <option value='<c:out value="${lookupBean.id}"/>'><c:out value="${lookupBean.displayValue}"/> [<c:out value="${lookupBean.id}"/>]</option>
      </c:if>
    </c:forEach>
  </select>
  <div id="selectors" style="float: left">
    <a href="#">&lt;</a>
    <a href="#">&gt;</a>
  </div>
  <select name="${fieldName}-lookup" style="float: left" size="10" multiple="true">
    <c:forEach items='${lookupMap.get("${fieldName}")}' var="lookupBean">
      <c:if test="!${bean.${fieldName}.contains(lookupBean.id)}">
      <option value='<c:out value="${lookupBean.id}"/>'><c:out value="${lookupBean.displayValue}"/> [<c:out value="${lookupBean.id}"/>]</option>
      </c:if>
    </c:forEach>
  </select>
  <!--/email_off-->
</td>
</tr>