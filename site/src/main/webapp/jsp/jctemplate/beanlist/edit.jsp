<tr>
<td class="fieldrow" id="fieldrow_${fieldName}" name="fieldrow_${fieldName}">
  <label for="${fieldName}">${fieldHeader}</label>
</td>
<script>
function add(selectName) {
    var actualSelect = document.getElementById(selectName);
    var lookupSelect = document.getElementById(selectName+"-lookup");
    for (var i=lookupSelect.options.length; --i>0; ) {
        var option = lookupSelect.options[i];
        if (option.selected) {
            lookupActual.options.appendChild(option);
            lookupSelect.options.removeChild(option);
        }
    }
}
function remove() {
    var actualSelect = document.getElementById(selectName);
    var lookupSelect = document.getElementById(selectName+"-lookup");
    for (var i=lookupSelect.options.length; --i>0; ) {
        var option = lookupSelect.options[i];
        if (option.selected) {
            lookupActual.options.appendChild(option);
            lookupSelect.options.removeChild(option);
        }
    }
}
</script>
<td>
  <!-- Cloudflare setting -->
  <!--email_off-->
  <select id="${fieldName}" name="${fieldName}" style="float: left" size="10" multiple="true">
    <c:forEach items='${lookupMap.get("${fieldName}")}' var="lookupBean">
      <c:if test="${bean.${fieldName}.contains(lookupBean.id)}">
      <option value='<c:out value="${lookupBean.id}"/>'><c:out value="${lookupBean.displayValue}"/> [<c:out value="${lookupBean.id}"/>]</option>
      </c:if>
    </c:forEach>
  </select>
  <div id="selectors" style="float: left">
    <a href="javascript:add(${fieldName})">&lt;</a>
    <a href="javascript:remove(${fieldName})">&gt;</a>
  </div>
  <select id="${fieldName}-lookup" name="${fieldName}-lookup" style="float: left" size="10" multiple="true">
    <c:forEach items='${lookupMap.get("${fieldName}")}' var="lookupBean">
      <c:if test="${!bean.${fieldName}.contains(lookupBean.id)}">
      <option value='<c:out value="${lookupBean.id}"/>'><c:out value="${lookupBean.displayValue}"/> [<c:out value="${lookupBean.id}"/>]</option>
      </c:if>
    </c:forEach>
  </select>
  <!--/email_off-->
</td>
</tr>