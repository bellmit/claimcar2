<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!-- 新增修理厂 -->
<!DOCTYPE HTML>
<html>
<head>
<title>新增修理厂</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<input type="hidden" id="index" value="${index}" />
	<input type="hidden" id="powerFlag" value="${powerFlag}" />
	<form name="fm" id="form" class="form-horizontal" role="form">
		<div class="table_title f14">修理厂信息</div>
		<div class="table_wrap">
			<div class="table_cont">
				<div class="formtable tableoverlable">
					<input type="hidden" name="repairFactoryVo.id" value="${repairFactoryVo.id}" id="repairId"/>
					<div class="row cl">
						<label class="form_label col-1">归属机构</label>
						<div class="form_input col-3">
							<span class="select-box"> <app:codeSelect type="select"
									codeType="ComCodeBranch" name="repairFactoryVo.comCode"
									value="${repairFactoryVo.comCode}" lableType="code-name" /> <font
								class="must">*</font>
							</span>
						</div>
						
						<label class="form_label col-1">修理厂类型</label>
						<div class="form_input col-3">
							<span class="select-box"> <app:codeSelect
									codeType="RepairFactoryType" name="repairFactoryVo.factoryType"
									clazz="must" type="select"
									value="${repairFactoryVo.factoryType}" /> <font class="must">*</font>
							</span>
						</div>
					
						<label class="form_label col-1">有效标志</label>
						<div class="form_input col-3">
							<span class="select-box"> <app:codeSelect type="select"
									codeType="validFlag" name="repairFactoryVo.validStatus"
									value="${repairFactoryVo.validStatus eq '0'? 0:1}" clazz="must" />
							</span>
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-1">修理厂名称</label>
						<div class="form_input col-3">
							<input type="text" id="factoryName" class="input-text"
								nullmsg="请填写修理厂名称！" name="repairFactoryVo.factoryName"
								datatype="*1-120" value="${repairFactoryVo.factoryName}" /> <font
								class="must">*</font>
						</div>
					
						<label class="form_label col-1">修理厂代码</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.factoryCode"
                                   id = "factoryCode"
								value="${repairFactoryVo.factoryCode}" datatype="*1-30" nullmsg="请填写修理厂代码！"/> <font
								class="must">*</font>
						</div>
						
						<label class="form_label col-1">组织机构编码</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.organizationCode"
								value="${repairFactoryVo.organizationCode}" datatype="s1-25"
								ignore="ignore" />
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-1">维修厂电话</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.telNo" value="${repairFactoryVo.telNo}"
								datatype="m|/^0\d{2,3}-\d{7,8}$/" id="telNo" ignore="ignore">
						</div>
						
						<label class="form_label col-1">联系人</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.linker" value="${repairFactoryVo.linker}"
								datatype="s1-15" ignore="ignore">
						</div>
						
						<label class="form_label col-1">联系人手机</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.mobile" datatype="m|/^0\d{2,3}-\d{7,8}$/"
								ignore="ignore" value="${repairFactoryVo.mobile}">
						</div>
					</div>
					
					<!-- 收款人账户维护 -->
					<div class="row cl">
						<label class="form_label col-1">收款人户名</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.payeeName" id="payeeName"
								value="${repairFactoryVo.payeeName}" datatype="*1-120"
								ignore="ignore" />
						</div>
						<label class="form_label col-1">收款人账号</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.accountNo" id="accountNo"
								value="${repairFactoryVo.accountNo}" datatype="s1-35"
								ignore="ignore" />
						</div>

						<label class="form_label col-1">是否首推</label>
						<div class="form_input col-3">
							<span class="select-box"> <app:codeSelect type="select"
									codeType="YN10" name="repairFactoryVo.preferredFlag"
									value="${repairFactoryVo.preferredFlag}" />
							</span>
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">修理厂合作类型</label>
						<div class="form_input col-3">
							<span class="select-box"> <app:codeSelect type="select"
									codeType="CooperateTypeSelect" name="repairFactoryVo.cooperateType"
									value="${repairFactoryVo.cooperateType}" />
							</span>
						</div>
						
						<label class="form_label col-1">是否合作车商</label>
						<div class="form_input col-3">
							<span class="select-box"> <app:codeSelect type="select"
									codeType="CooperateFactorySelect" name="repairFactoryVo.cooperateFactory"
									value="${repairFactoryVo.cooperateFactory}" />
							</span>
						</div>
						<label class="form_label col-1">是否黑名单</label>
						<div class="form_input col-3">
							<span class="select-box"> <app:codeSelect type="select"
									codeType="BlackListFlagSelect" name="repairFactoryVo.blackListFlag"
									value="${repairFactoryVo.blackListFlag eq '0'? 0:1}" />
							</span>
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">协议是否有效</label>
						<div class="form_input col-3">
							<span class="select-box"> <app:codeSelect type="select"
									codeType="AgreementValidSelec" name="repairFactoryVo.agreementValid"
									value="${repairFactoryVo.agreementValid}"  />
							</span>
						</div>
						<label class="form_label col-1">协议号</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.protocolNumber"
								value="${repairFactoryVo.protocolNumber}" datatype="s1-25"
								ignore="ignore" />
						</div>
						
						<div class="form_input col-3">
							<span class="select-box">
								<input type="hidden" id="userCodeAjax" name="wfTaskSubmitVo.assignUser">
							</span>
						</div>
						
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">价格方案模式</label>
						<div class="form_input col-3">
							<span class="select-box"> <app:codeSelect type="select"
									codeType="PricePlanModeSelect" name="repairFactoryVo.priceSchemeMode"
									value="${repairFactoryVo.priceSchemeMode}"  />
							</span>
						</div>
						
						<label class="form_label col-1">工时折扣</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.hourRate" id="hourRate"
								datatype="/^(0\.\d{0,4}|1(\.0{0,4})?)$/"
								value="${repairFactoryVo.hourRate}" /> <font class="must">*</font>
						</div>
						<label class="form_label col-1">配件折扣</label>
						<div class="form_input col-3">
							<input type="text" class="input-text"
								name="repairFactoryVo.compRate" id="compRate"
								datatype="/^(0\.\d{0,4}|1(\.0{0,4})?)$/"
								value="${repairFactoryVo.compRate}" /> <font class="must">*</font>
						</div>
					</div>

					<div class="row cl accident-over-add">
						<label class="form_label col-1">修理厂地址</label>
						<div class="form_input col-10 clearfix">
							<div id="damageAddressDiv" style="display: inline">
								<app:areaSelect targetElmId="damageAreaCode"
									areaCode="${repairFactoryVo.areaCode}" showLevel="3" />
							</div>
							<input type="hidden" id="damageAreaCode"
								name="repairFactoryVo.areaCode"
								value="${repairFactoryVo.areaCode}" /> <input type="hidden"
								id="damageMapCode" name="repairFactoryVo.city"
								value="${repairFactoryVo.city}" /> <input type="text"
								id="damageAddress" name="repairFactoryVo.address"
								value="${repairFactoryVo.address}" class="input-text w_25"
								placeholder="详细地址" datatype="*" />
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="table_wrap">
			<div class="table_title f14">代理人维护</div>
			<div calss="table_cont table_list">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th width="10%">
								<button type="button"
									class="btn btn-plus Hui-iconfont Hui-iconfont-add"
									onclick="addAgentItem()"></button>
							</th>
							<th style="width: 25%">代理人名称</th>
							<th style="width: 10%">代理人代码</th>
							<th style="width: 15%">出单工号</th>
							<th style="width: 15%">代理人电话</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id='agentTbody'>
						<input type='hidden' id='agentSize'
							value='${fn:length(agentFactoryList) }' />
						<c:forEach var="agentFactoryVo" items="${agentFactoryList}"
							varStatus="agentStatus">
							<c:set var="agentId" value="${agentStatus.index }" />
							<input type="hidden" value="${agentSize}" />
							<%@include file="AgentFactoryEdit_item.jsp"%>
						</c:forEach>

					</tbody>
				</table>
			</div>
		</div>
		<div class="table_wrap">
			<div class="table_title f14">推送修手机号码</div>
			<div calss="table_cont table_list">
				<table class="table table-border table-hover" id='phoneTable'>
					<thead>
						<tr class="text-c">
							<th style="width: 40%">推送修手机号码</th>
							<th style="width: 40%">备注</th>
							<th style="width: 20%">操作</th>
						</tr>
					</thead>
					<tbody id='phoneTbody'>
						<input type='hidden' id='phoneSize'
							value='${fn:length(repairPhoneList) }' />
						<c:forEach var="repairPhoneVo" items="${repairPhoneList}" varStatus="phoneStatus">
							<c:set var="phoneId" value="${phoneStatus.index }" />
							<%@include file="PhoneFactoryEdit_item.jsp"%>
						</c:forEach>

					</tbody>
					<tr>
						<td>
							<button type="button"
								class="btn btn-plus Hui-iconfont Hui-iconfont-add"
								onclick="addPhoneItem()"></button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<!--可修品牌     开始-->
		<div class="table_wrap">
			<div class="table_title f14">可修品牌</div>
			<div class="table_cont table_list">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
							<th style="width: 22%">品牌名称</th>
							<th style="width: 22%">品牌代码</th>
							<th style="width: 22%">配件折扣<font class="must">*</font></th>
							<th style="width: 22%">工时折扣<font class="must">*</font></th>
							<th style="width: 10%">操作</th>
						</tr>
					</thead>
					<tbody id="reTbody">
						<%--tbody内容,财产损失项 --%>
						<input type="hidden" id="repairSize"
							value="${fn:length(repairBrandVos)}">
						<c:forEach var="repairBrandVo" items="${repairBrandVos}" varStatus="status">
							<c:set var="Idx" value="${status.index}" />
							<input type="hidden" value="${repairSize}" />
							<%@include file="RepairFactoryEdit_Item.jsp"%>
						</c:forEach>
					</tbody>
					<tr>
						<td>
							<button type="button"
								class="btn btn-plus Hui-iconfont Hui-iconfont-add"
								onclick="addRepairItem()"></button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<!--可修品牌     结束-->

		<div class="table_wrap">
			<div class="table_title f14">备注</div>
			<div class="row cl">
				<div class="form_input col-12">
					<textarea class="textarea" name="repairFactoryVo.remark"
						placeholder="请输入..." datatype="*0-255">${repairFactoryVo.remark}</textarea>
				</div>
			</div>
		</div>

		<!--撑开页面  开始  -->
		<div class="row cl">
			<div class="form_input col-6"></div>
		</div>
		<div class="row cl">
			<div class="form_input col-6"></div>
		</div>
		<!-- 结束 -->
		<div class="btn-footer clearfix" id="repairHideOrShow">
			<a class="btn btn-primary fl" style="margin-left: 40%" id="submit">保存</a>
			<a class="btn btn-warning f1 ml-5" id="re">重置</a>
			<a class="btn btn-primary fl ml-5" onclick="closeAlert()">关闭</a>
		</div>
		<br>
		<div class="row cl">
			<div class="form_input col-6"></div>
		</div>

	</form>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript"
		src="/claimcar/js/manage/RepairFactoryEdit.js"></script>
</body>
</html>