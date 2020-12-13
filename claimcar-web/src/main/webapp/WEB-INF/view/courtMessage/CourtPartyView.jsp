<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>当事人详细信息</title>
<style>
.tableoverlable label.form_label.col-1{width:9.3333%;padding-right:0;}
.tableoverlable .form_input.col-3{  width: 23%;margin-left: 1%;}
.tableoverlable .form_input.col-2{  width: 15%;margin-left: 0.6%;}
.btn.garage{
  width: 44%;
  line-height: 31px;
  padding: 0;
}
#mns {margin-left: -4px;}
</style>
</head>
<body>
	<div class="page_wrap">
		<form id="saveCar" class="saveForm" role="form" >
			<input type="hidden" id="carId" name="checkCarVo.carid" value="${checkCarVo.carid}">
			<input type="hidden" id="carSerialNo" name="checkCarVo.serialNo" value="${checkCarVo.serialNo}">
			<input type="hidden" id="carRegistNo" name="checkCarVo.registNo" value="${checkCarVo.registNo}">
			<input type="hidden" id="sessionUserCode" name="checkCarVo.sessionUserCode" value="${sessionUserCode}">
			<input type="hidden" id="sessionComCode" name="checkCarVo.sessionComCode" value="${sessionComCode}">
			<input type="hidden" value="${carKindMap}">
			
			<!-- 工作流的值 -->
			<input type="hidden" id="carStatus" value="${status}">
			<input type="hidden" id="carNodeCode" value="${nodeCode}">
			
			<!-- 用于判断是否是复勘 -->
			<%-- <input type="hidden" id="stateCheck" name="stateCheck" value="${stateCheck}">
			<input type="hidden" id="checkModify" name="checkModify" value="${checkModify}"> --%>
			<input type="hidden" name="addCarFlag" value="${addCarFlag}">
			<input type="hidden" id="kindCode_A" value="${kindCode_A}">
			
			<div class="table_wrap">
				<div class="table_title f14">当事人信息</div>
				<div class="table_cont ">
					<div class="formtable tableoverlable">
						<div class="row  cl">
							<label class="form_label col-1">姓名：</label>
							<div class="form_input col-3">
								${courtPartyVo.name}
							</div>
							<label class="form_label col-1">证件类型：</label>
							<div class="form_input col-3">
									<app:codetrans codeType="PersonIdType" codeCode="${courtPartyVo.personidtype}" />
							</div>
							<label class="form_label col-1">证件号：</label>
							<div class="form_input col-3">
								${courtPartyVo.personID}
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">当事人类型：</label>
							<div class="form_input col-3">
								<app:codetrans codeType="PersonType" codeCode="${courtPartyVo.persontype}" />
							</div>
							<label class="form_label col-1">车牌号码：</label>
								<div class="form_input col-3">
									${courtPartyVo.carno}
								</div>
							<label class="form_label col-1">国籍：</label>
							<div class="form_input col-3">
									<app:codetrans codeType="Nationality" codeCode="${courtPartyVo.gj}" />
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">身份类型：</label>
							<div class="form_input col-3">
									<app:codetrans codeType="SFLX" codeCode="${courtPartyVo.sflx}" />
							</div>
							<label class="form_label col-1">性别：</label>
							<div class="form_input col-3">
								<app:codetrans codeType="Sex" codeCode="${courtPartyVo.sex}" />
							</div>
							<label class="form_label col-1">即时履行金额：</label>
							<div class="form_input col-3">
								${courtPartyVo.jslxje}
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">民族：</label>
							<div class="form_input col-3">
								<app:codetrans codeType="Nation" codeCode="${courtPartyVo.nation}" />
							</div>
							<label class="form_label col-1">手机号码：</label>
							<div class="form_input col-3">
								${courtPartyVo.phone}
							</div>
							<label class="form_label col-1">是否死亡：</label>
							<div class="form_input col-3">
								<app:codetrans codeType="IsValid" codeCode="${courtPartyVo.death}" />
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">送达地址：</label>
							<div class="form_input col-3">
								${courtPartyVo.sdaddr}
							</div>
							<label class="form_label col-1">电子邮箱：</label>
							<div class="form_input col-3">
								${courtPartyVo.email}
							</div>
							<label class="form_label col-1">居住地：</label>
							<div class="form_input col-3">
								${courtPartyVo.address}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">出生日期：</label>
							<div class="form_input col-3">
								<fmt:formatDate  value="${courtPartyVo.birth}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-1">户口性质：</label>
								<div class="form_input col-3">
									<app:codetrans codeType="HKXZ" codeCode="${courtPartyVo.hkxz}" />
								</div>
								<label class="form_label col-1">调解号：</label>
								<div class="form_input col-3">
									${courtPartyVo.nediationnum}
								</div>
						</div>
						<div class="row cl">
								<label class="form_label col-1">伤残赔偿系数：</label>
								<div class="form_input col-3">
									${courtPartyVo.scxs}
								</div>
								<label class="form_label col-1">户籍所在地：</label>
								<div class="form_input col-3">
										${courtPartyVo.hj}
								</div>
								<label class="form_label col-1">交通方式：</label>
								<div class="form_input col-3">
										<app:codetrans codeType="PersonVehicle" codeCode="${courtPartyVo.personvehicle}" />
								</div>
						</div>
							<div class="row cl">
								<label class="form_label col-1">申请赔偿金额：</label>
								<div class="form_input col-3">
									${courtPartyVo.applicationmoney}
								</div>
								<label class="form_label col-1" >协议金额：</label>
								<div class="form_input col-3"  >
									 ${courtPartyVo.protocolmoey}
								</div>
							</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<script type="text/javascript">
		//------
	</script>
</body>
</html>