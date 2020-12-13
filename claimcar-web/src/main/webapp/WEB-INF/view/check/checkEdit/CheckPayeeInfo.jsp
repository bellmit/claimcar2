<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!-- 收款人信息 开始 -->
<div class="table_wrap" id="payInfos">
	<div id="payInfo_title" class="table_title f14">收款人账户信息</div>
	<div class="table_cont">
		<div class="formtable tableoverlable">
			<div class="row  cl">
				<label class="form_label col-1">收款人类型</label>
				<div class="form_input col-3">被保险人
					<input type="hidden" id="rrrrrr" class="input-text" value="${payCustomVo.payObjectType}" />
				</div>
			</div>
			<div class="row  cl">
				<label class="form_label col-1">收款人</label>
				<div class="form_input col-3">${payCustomVo.payeeName}
					<%-- <input type="text" class="input-text" value="${payCustomVo.payeeName}" readonly="readonly"/> --%>
				</div>
				<label class="form_label col-1 text-l">身份证号</label>
				<div class="form_input col-3 text-l">
					<input type="text" class="input-text" value="${payCustomVo.certifyNo}" readonly="readonly"/>
				</div>
				<label class="form_label col-1">收款联系电话</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" value="${payCustomVo.payeeMobile}" readonly="readonly"/> 
				</div>
			</div>
			<div class="row  cl">
				<label class="form_label col-1">开户银行</label>
				<div class="form_input col-3">
					<input type="hidden" class="input-text" value="${payCustomVo.bankName}" readonly="readonly"/>
					<app:codetrans codeType="BankCode" codeCode="${payCustomVo.bankName}"/>
				</div>
				<label class="form_label col-1">账户名</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" value="${payCustomVo.payeeName}" readonly="readonly"/>
				</div>
				<label class="form_label col-1">银行账号</label>
				<div class="form_input col-3">
					<input type="text" class="input-text" value="${payCustomVo.accountNo}" readonly="readonly"/>
				</div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">银行信息</label>
				<div class="form_input col-3">
					<input type="button" class="btn btn-secondary" onclick="payCustomOpen('S','${payCustomVo.id}')" value="收款人账户信息维护">
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 收款人信息 结束 -->
