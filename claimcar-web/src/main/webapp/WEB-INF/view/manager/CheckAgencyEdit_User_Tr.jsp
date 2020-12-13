<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="checkUserVo" items="${checkUserList}" varStatus="status">
	<c:set var="pe_Idx" value="${status.index + size}" />
	<tr class="text-c" id="checkUserTr_${pe_Idx}" name="checkUserpropTr">
		<input type="hidden" name="prpdcheckUserVo[${pe_Idx}].id" value="${checkUserVo.id}" />
		<td>
			<input type="text" class="input-text" style="width:50%" name="prpdcheckUserVo[${pe_Idx}].userCode"  
			id="checkUserCodeAA${pe_Idx}" value="${checkUserVo.userCode}"  readonly="readonly"/>
		</td>

		<td><input type="text" class="input-text" style="width: 50%" readonly="readonly"
			name="prpdcheckUserVo[${pe_Idx}].userName"  id="checkUserNameAA${pe_Idx}"
			value="${checkUserVo.userName}" /></td>
		<td>
			<button class="btn btn-plus Hui-iconfont Hui-iconfont-jianhao"
				onclick="checkUserDelete(this)" type="button"
				name="delcheckUser_${pe_Idx}"></button>
		</td>
	</tr>
</c:forEach>
