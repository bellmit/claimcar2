<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_wrap">
	<div class="table_title f14">意见</div>
	<div class="table_cont dingsun-notice ">
		<input type="hidden" name="prpLClaimTextVo.id" value="${prpLClaimTextVo.id }" />
		<input type="hidden" name="prpLClaimTextVo.registNo" value="${prpLClaimTextVo.registNo }" />
		<input type="hidden" name="prpLClaimTextVo.bussTaskId" value="${prpLClaimTextVo.bussTaskId }" />
		<input type="hidden" name="prpLClaimTextVo.textType" value="${prpLClaimTextVo.textType }" />
		<input type="hidden" name="prpLClaimTextVo.nodeCode" value="${prpLClaimTextVo.nodeCode }" />
		<textarea class="textarea w90" id="description" name="prpLClaimTextVo.remark" maxlength="200" placeholder="请输入...">${prpLClaimTextVo.remark }</textarea>
		<font class="must">*</font>
	</div>
</div>
