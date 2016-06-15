<div class="fieldrow" id="fieldrow_${fieldName}" name="fieldrow_${fieldName}">
  <label for="${fieldName}">${fieldHeader}</label>
  <input type="${type}" id="${fieldName}" name="${fieldName}" value='<c:out value="${bean.${fieldName}}" />' placeholder="${fieldHeader}" ${other} />
</div>
