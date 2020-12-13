<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpLTmpCItemKind" varStatus="status" items="${prpLTmpCItemKinds}">
	<tr class="CItemKind-detail-body">
		<td width="10%">
			<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delCItemKindRowBtn"   onclick="delCItemKind(this)"></button>
		</td>
		<td>
			<!-- 险别名称   手工选择，包括交强险和商业险对应的所有险别 -->
			<app:codeSelect type="select" codeType="KindCode" upperCode="Z" name="itemKindVos[${status.index + kindSize}].kindCode" />
		</td>
		<td>
			<input name="itemKindVos[${status.index + kindSize}].amount" />
		</td>
	</tr>
</c:forEach>