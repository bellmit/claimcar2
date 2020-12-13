<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<tr  class="text-c">
	<input type="hidden" name="repairPhoneVo[${phoneId }].createTime" value="${repairPhoneVo.createTime }"/>
	<input type="hidden" name="repairPhoneVo[${phoneId }].updateTime" value="${repairPhoneVo.updateTime }"/>
	<input type="hidden" name="repairPhoneVo[${phoneId }].id" value="${repairPhoneVo.id }" />
	<td>
		<input type="text" datatype="m"  nullmsg="请录入推送修手机号码" name="repairPhoneVo[${phoneId }].phoneNumber" class="input-text" value="${repairPhoneVo.phoneNumber}"/>
	</td>
	<td><input type="text" class="input-text" name="repairPhoneVo[${phoneId }].remark" value="${repairPhoneVo.remark}"/>
	</td>
	<td>
		<button type="button"
			class="btn btn-plus Hui-iconfont Hui-iconfont-jianhao"
			onclick="delPhoneTr(this)" name="delPhonetr_${phoneId}">
		</button>
	</td>
</tr>