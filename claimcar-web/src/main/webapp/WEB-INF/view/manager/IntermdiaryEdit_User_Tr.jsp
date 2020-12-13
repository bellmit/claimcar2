<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="intermUserVo" items="${intermUserList}" varStatus="status">
	<c:set var="pe_Idx" value="${status.index + size}" />
	<tr class="text-c" id="intermUserTr_${pe_Idx}" name="intermUserpropTr">
		<input type="hidden" name="prpdIntermUserVo[${pe_Idx}].id" value="${intermUserVo.id}" />
		<td>
			<input type="text" class="input-text" style="width:50%" name="prpdIntermUserVo[${pe_Idx}].userCode"  
			id="interUserCodeAA${pe_Idx}" value="${intermUserVo.userCode}"  readonly="readonly"/>
		</td>

		<td><input type="text" class="input-text" style="width: 50%" readonly="readonly"
			name="prpdIntermUserVo[${pe_Idx}].userName"  id="intermUserNameAA${pe_Idx}"
			value="${intermUserVo.userName}" /></td>
		<td>
			<button class="btn btn-plus Hui-iconfont Hui-iconfont-jianhao"
				onclick="intermUserDelete111(this)" type="button"
				name="delIntermUser_${pe_Idx}"></button>
		</td>
	</tr>
</c:forEach>
