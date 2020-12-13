<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_title f14">收款人账户信息</div>
<div class="row cl">
	<c:forEach var="prpLPayCustomVo" items="${prpLPayCustomVoList}"
		varStatus="status">
		<input type="hidden" value="${prpLPayCustomVo.id}"
			name="prpLPayCustomVos[${status.index + size}].id">
		<div class="table_cont">
			<div class="formtable">
				<div class="row cl">
					<label class="form_label col-1">收款人类型</label>
					<div class="form_input col-3">
						<app:codetrans codeType="PayObjectKind"
							codeCode="${prpLPayCustomVo.payObjectKind}" />
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-1">收款人</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" readOnly
							value="${prpLPayCustomVo.payeeName}" />
					</div>
					<label class="form_label col-1">身份证号</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" readOnly
							value="${prpLPayCustomVo.certifyNo}" />
					</div>
					<label class="form_label col-1">联系电话</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" readOnly
							value="${prpLPayCustomVo.payeeMobile}" />
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-1">开户银行:</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" readOnly
							value="<app:codetrans codeType="BankCode"
							codeCode="${prpLPayCustomVo.bankName}" />" />
					</div>
					<label class="form_label col-1">账户名:</label>
					<div class="form_input col-3">
						<input type="text" class="input-text"
							value="${prpLPayCustomVo.payeeName}" />
					</div>
					<label class="form_label col-1">银行账号:</label>
					<div class="form_input col-3">
						<input type="text" class="input-text" readOnly
							value="${prpLPayCustomVo.accountNo}" />
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-1">银行信息:</label>
					<div class="form_input col-3">
						<input type="button" class="btn  btn-primary" id="editPayCustom"
							onclick="payCustomOpen('N','<c:out value='${prpLPayCustomVo.id}'/>')"
							value="银行信息">
					</div>
				</div>

			</div>
		</div>
	</c:forEach>
</div>