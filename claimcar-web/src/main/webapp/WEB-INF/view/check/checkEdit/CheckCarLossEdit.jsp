<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>标的车处理</title>
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
			<input type="hidden" id="comCode" name="comCode" value="${comCode}">
			
			<!-- 工作流的值 -->
			<input type="hidden" id="carStatus" value="${status}">
			<input type="hidden" id="carNodeCode" value="${nodeCode}">
			
			<!-- 用于判断是否是复勘 -->
			<%-- <input type="hidden" id="stateCheck" name="stateCheck" value="${stateCheck}">
			<input type="hidden" id="checkModify" name="checkModify" value="${checkModify}"> --%>
			<input type="hidden" name="addCarFlag" value="${addCarFlag}">
			<input type="hidden" id="kindCode_A" value="${kindCode_A}">
			
			<div class="table_wrap">
				<div class="table_title f14">涉案车辆信息</div>
				<div class="table_cont ">
					<div class="formtable tableoverlable">
						<div class="row  cl">
							<label class="form_label col-1">损失类型</label>
							<div class="form_input col-3">
								<span class="select-box"> <c:choose>
										<c:when test="${checkCarVo.serialNo eq 1}">
											<app:codeSelect codeType="LossTargetType" type="select"
												id="checkCarType" clazz="must" disabled="true"
												style="width: 95%" name="checkCarVo.serialNo" value="1" />
											<font class="must">*</font>
										</c:when>
										<c:otherwise>
											<app:codeSelect codeType="LossTargetType" type="select"
												id="checkCarType" style="width: 95%" clazz="must" 
												disabled="true" nullToVal="2" value="2" />
											<font class="must">*</font>
										</c:otherwise>
									</c:choose>
								</span> <font class="must">*</font>
							</div>
							<label class="form_label col-1">车牌号码</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="licenseNo"
									name="checkCarVo.prpLCheckCarInfo.licenseNo"
									value="${checkCarVo.prpLCheckCarInfo.licenseNo}"
									style="width: 95%" nullmsg="请输入车牌号码！" datatype="carLicenseNo" maxlength="11"/>
									<font class="must">*</font>
							</div>
							<label class="form_label col-1">车主</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="carOwner" datatype="*" maxlength="30"
									name="checkCarVo.prpLCheckCarInfo.carOwner" style="width: 95%"
									value="${checkCarVo.prpLCheckCarInfo.carOwner}" nullmsg="请填写车主！"/>
								<font class="must">*</font>
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">发动机号</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="engineNo"
									name="checkCarVo.prpLCheckCarInfo.engineNo" maxlength="30"
									value="${checkCarVo.prpLCheckCarInfo.engineNo}"
									datatype="*1-30" style="width: 95%" /> <font class="must">*</font>
							</div>
							<label class="form_label col-1">车架号</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="frameNo"
									name="checkCarVo.prpLCheckCarInfo.frameNo"
									value="${checkCarVo.prpLCheckCarInfo.frameNo}" nullmsg="请填写车架号！" 
									errormsg="请输入正确的车架号！" style="width: 95%" datatype="vinNo" ignore="ignore" onkeyup="toUpperCase(this)"/> 
									 <!-- datatype="vinNo"  errormsg="请输入正确的车架号！"  id="frameNo"-->
							</div>
							<label class="form_label col-1">VIN码</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="vinNo"
									name="checkCarVo.prpLCheckCarInfo.vinNo"
									value="${checkCarVo.prpLCheckCarInfo.vinNo}"
									errormsg="请输入正确的VIN码！" nullmsg="请填写VIN码！" datatype="vinNo" ignore="ignore"
									style="width: 95%" onkeyup="toUpperCase(this)"/> <font class="must">*</font><!-- datatype="vinNo"  errormsg="请输入正确的VIN码！" nullmsg="请填写VIN码！" id="vinNo"-->
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">号牌底色</label>
							<div class="form_input col-3">
								<span class="select-box"> <app:codeSelect
										codeType="LicenseColor" type="select" id="licenseColorCode"
										lableType="code-name" style="width: 95%"
										name="checkCarVo.prpLCheckCarInfo.licenseColor"
										value="${checkCarVo.prpLCheckCarInfo.licenseColor }" />
								</span>
							</div>
							<label class="form_label col-1">车牌种类</label>
							<div class="form_input col-3">
								<span class="select-box"> <app:codeSelect
										codeType="LicenseKindCode" type="select" id="licenseType"
										lableType="code-name" clazz="must" nullToVal="02"
										name="checkCarVo.prpLCheckCarInfo.licenseType" onchange="setVinChange(this)"
										value="${checkCarVo.prpLCheckCarInfo.licenseType }" />
										<font class="must">*</font>
								</span>
							</div>
							<c:if test="${checkCarVo.serialNo eq 1}">
								<label class="form_label col-1">承保公司</label>
								<div class="form_input col-3">
									<span class="select-box"> <app:codeSelect
											codeType="CIInsurerCompany" type="select" lableType="name"
											name="checkCarVo.prpLCheckCarInfo.insurecomcode"
											nullToVal="DHIC" style="width: 95%" 
											value="${checkCarVo.prpLCheckCarInfo.insurecomcode}"/>
											<%-- <app:codetrans codeType="ComCode" 
											codeCode="${checkCarVo.prpLCheckCarInfo.insurecomcode}"/> --%>
									</span>
								</div> 
							</c:if>
							<c:if test="${checkCarVo.serialNo ne 1}">
								<label class="form_label col-1">电话</label>
								<div class="form_input col-3">
									<input type="text" class="input-text" name="checkCarVo.prpLCheckCarInfo.phone" 
									datatype="m|/^0\d{2,3}-\d{7,8}$/" value="${checkCarVo.prpLCheckCarInfo.phone}" nullmsg="请填写电话！" style="width: 95%"/>
									<font class="must">*</font>
								</div>
							</c:if>
						</div>
						<div class="row cl">
							<label class="form_label col-1">车身颜色</label>
							<div class="form_input col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ColorCode" type="select" id="carColorCode"
										lableType="code-name" style="width: 95%"
										name="checkCarVo.prpLCheckCarInfo.carColorCode"
										value="${checkCarVo.prpLCheckCarInfo.carColorCode }" />
								</span>
							</div>
							<label class="form_label col-1">车型名称</label>
							<div class="form_input col-3">
								<input type="text" id="brandName"
									name="checkCarVo.prpLCheckCarInfo.brandName" class="input-text"
									value="${checkCarVo.prpLCheckCarInfo.brandName}"
									style="width: 95%"/>
								<font class="must">*</font>
								<!-- <input type="button" class="btn btn-secondary text-c garage" href="javascript:;"
									onclick="factoryCheck()" id="factoryButton" value="推荐修理厂"> -->
							</div>
							<label class="form_label col-1">初次登记日期</label>
							<div class="form_input col-3">
							<c:choose>
								<c:when test="${checkCarVo.serialNo eq 1}">
									<input type="text" class="Wdate" id="enrollDate"
									name="checkCarVo.prpLCheckCarInfo.enrollDate"
									value="<app:date date='${checkCarVo.prpLCheckCarInfo.enrollDate}'/>"
									style="width: 95%"
									onfocus="WdatePicker({maxDate:'%y-%M-%d'})" />
								</c:when>
								<c:otherwise>
									<input type="text" class="Wdate" id="enrollDate"
										name="checkCarVo.prpLCheckCarInfo.enrollDate"
										value="<app:date date='${checkCarVo.prpLCheckCarInfo.enrollDate}'/>"
										style="width: 95%"
										onfocus="WdatePicker({maxDate:'%y-%M-%d'})"/>
										<font class="must">*</font>
								</c:otherwise>
							</c:choose>
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">起保日期</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate" id="startDate"
									name="checkCarVo.prpLCheckCarInfo.startDate"
									onfocus="WdatePicker({maxDate:'%y-%M-%d'})"
									value="<app:date date='${checkCarVo.prpLCheckCarInfo.startDate}'/>"
									style="width: 95%" />
							</div>
							<c:if test="${checkCarVo.serialNo eq 1}">
								<label class="form_label col-1">车主性质代码</label>
								<div class="form_input col-3">
									<span class="select-box"> <app:codeSelect
											codeType="CarAttachNature" type="select"
											lableType="code-name"
											name="checkCarVo.prpLCheckCarInfo.carAttachNature"
											value="${checkCarVo.prpLCheckCarInfo.carAttachNature}" />
									</span>
								</div>
							</c:if>
							<label class="form_label col-1">车辆种类</label>
							<div class="form_input col-3">
								<span class="select-box"> 
									<c:choose>
										<c:when test="${checkCarVo.serialNo eq 1}">
											<app:codeSelect
											codeType="VehicleTypeShow" type="select" id="carKindCode"
											lableType="code-name" clazz="must" 
											name="checkCarVo.prpLCheckCarInfo.platformCarKindCode" 
											value="${prpLCItemCarVo.carType}"   disabled="true"  />
											<font class="must">*</font>
										</c:when>
										<c:otherwise>
											<app:codeSelect
											codeType="VehicleType" type="select" id="carKindCode"
											lableType="code-name" clazz="must" nullToVal="11"
											name="checkCarVo.prpLCheckCarInfo.platformCarKindCode" 
											value="${checkCarVo.prpLCheckCarInfo.platformCarKindCode }" />
											<font class="must">*</font>
										</c:otherwise>
									</c:choose>
								</span>
							</div>
						</div>
					</div>
					<c:if test="${checkCarVo.serialNo ne 1}">
						<div class="formtable tableoverlable">
							<div class="row cl">
								<label class="form_label col-1">商业险报案号</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" maxlength="30" datatype="*0-0|letterAndNumber"
										name="checkCarVo.prpLCheckCarInfo.biRegistNo"
										value="${checkCarVo.prpLCheckCarInfo.biRegistNo}" />
								</div>
								<label class="form_label col-1">商业险保单号</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" maxlength="30" datatype="*0-0|letterAndNumber"
										name="checkCarVo.prpLCheckCarInfo.biPolicyNo"
										value="${checkCarVo.prpLCheckCarInfo.biPolicyNo}" />
								</div>
								<label class="form_label col-1">商业承保机构</label>
								<div class="form_input col-3">
									<span class="select-box">
									<app:codeSelect codeType="CIInsurerCompany" type="select"
										lableType="name" style="width: 95%" 
										name="checkCarVo.prpLCheckCarInfo.biInsureComCode"
										value="${checkCarVo.prpLCheckCarInfo.biInsureComCode}"/>
									<input type="hidden" name="checkCarVo.prpLCheckCarInfo.biInsureComName" 
									value="<app:codetrans codeType="CIInsurerCompany" 
									codeCode="${checkCarVo.prpLCheckCarInfo.biInsureComName}"/>">
									</span>
								</div>
								<label class="form_label col-1" style="width: 8%">商业承保地区</label>
								<div class="form_input col-1" style="width: 8%">
									<span class="select-box">
										<app:codeSelect type="select" codeType="DWInsurerArea" 
											name="checkCarVo.prpLCheckCarInfo.biInsurerArea"  style="width: 113%"
												value="${checkCarVo.prpLCheckCarInfo.biInsurerArea}"/>
									</span>
								</div>
							</div>
						</div>
						<div class="formtable tableoverlable">
							<div class="row cl">
								<label class="form_label col-1">交强险报案号</label>
								<div class="form_input col-2">
									<input type="text" class="input-text" maxlength="30" 
										datatype="*0-0|letterAndNumber"
										name="checkCarVo.prpLCheckCarInfo.ciRegistNo"
										value="${checkCarVo.prpLCheckCarInfo.ciRegistNo}" />
								</div>
								<label class="form_label col-1">交强险保单号</label>
								<div class="form_input col-2">
									<input type="text" class="input-text"
										maxlength="30" datatype="*0-0|letterAndNumber"
										name="checkCarVo.prpLCheckCarInfo.ciPolicyNo"
										value="${checkCarVo.prpLCheckCarInfo.ciPolicyNo}" />
								</div>
								<label class="form_label col-1">交强承保机构</label>
								<div class="form_input col-3">
									<span class="select-box">
									<app:codeSelect codeType="CIInsurerCompany" type="select"
										style="width: 95%" lableType="name"
										name="checkCarVo.prpLCheckCarInfo.ciInsureComCode"
										value="${checkCarVo.prpLCheckCarInfo.ciInsureComCode}"/>
									<input type="hidden" name="checkCarVo.prpLCheckCarInfo.ciInsureComName" 
									value="<app:codetrans codeType="CIInsurerCompany" 
									codeCode="${checkCarVo.prpLCheckCarInfo.ciInsureComCode}"/>">
									</span>
								</div>
								<label class="form_label col-1" style="width: 8%">交强承保地区</label>
								<div class="form_input col-1" style="width: 8%" >
									<span class="select-box">
										<app:codeSelect type="select" codeType="DWInsurerArea" 
											name="checkCarVo.prpLCheckCarInfo.ciInsurerArea"  style="width: 113%"
												value="${checkCarVo.prpLCheckCarInfo.ciInsurerArea}"/>
									</span>
								</div>
							</div>
						</div>
					</c:if>
				</div>
			</div>
			<!--涉案车辆信息    结束-->
			<!-- 三者车才显示车辆承保信息 -->
						

			<div class="table_wrap">
				<div class="table_title f14">驾驶人信息</div>
				<div class="table_cont ">
					<div class="formtable tableoverlable">
						<div class="row  cl">
							<label class="form_label col-1">驾驶人姓名</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="driverName" 
									datatype="*1-20" nullmsg="请输入驾驶人姓名！"
									name="checkCarVo.prpLCheckDriver.driverName"
									value="${checkCarVo.prpLCheckDriver.driverName}"
									style="width:95%"/>
								<font class="must">*</font>
							</div>
							<label class="form_label col-1">性别</label>
							<div class="form_input col-3">
								<span class="select-box"> <app:codeSelect
										codeType="SexCode" type="select" lableType="name"
										id="driverSex" clazz="must" style="width: 95%"
										name="checkCarVo.prpLCheckDriver.driverSex"
										value="${checkCarVo.prpLCheckDriver.driverSex }" />
									<font class="must">*</font>
								</span>
							</div>
							<label class="form_label col-1">准驾车型</label>
							<div class="form_input col-3">
								<span class="select-box"> <app:codeSelect
										codeType="DrivingCarType" type="select" nullToVal="C1"
										lableType="code-name" id="drivingCarType" style="width: 95%"
										name="checkCarVo.prpLCheckDriver.drivingCarType"
										value="${checkCarVo.prpLCheckDriver.drivingCarType }" />
								</span>
							</div>
						</div>
						<div class="row cl" id="idType">
							<label class="form_label col-1">证件类型</label>
							<div class="form_input col-3">
								<span class="select-box"> <app:codeSelect
										codeType="IdentifyType" type="select" clazz="must"
										id="identifyTypeB" lableType="code-name" onchange="changeIdentifyType(this)"
										name="checkCarVo.prpLCheckDriver.identifyType" style="width: 95%"
										value="${checkCarVo.prpLCheckDriver.identifyType }" />
										<font class="must">*</font>
								</span>
							</div>
							<label class="form_label col-1">证件号码</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="identifyNumberB"
									nullmsg="请输入证件号码！" datatype="letterAndNumber" 
									name="checkCarVo.prpLCheckDriver.identifyNumber"
									value="${checkCarVo.prpLCheckDriver.identifyNumber}" onchange="toUpperCase(this)"
									style="width: 95%" maxlength="20"/> <font class="must">*</font>
							</div>
							<label class="form_label col-1">驾驶证号码</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" id="drivingLicenseNo"
									nullmsg="请输入驾驶证号码！" datatype="*"
									name="checkCarVo.prpLCheckDriver.drivingLicenseNo"
									value="${checkCarVo.prpLCheckDriver.drivingLicenseNo}" onchange="toUpperCase(this)"
									style="width: 95%" maxlength="20"/> <font class="must">*</font>
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">电话</label>
							<div class="form_input col-3">
								<input style="width: 95%" type="text" class="input-text"
									id="linkPhoneNumber" datatype="m|/^0\d{2,3}-\d{7,8}$/" name="checkCarVo.prpLCheckDriver.linkPhoneNumber"
									value="${checkCarVo.prpLCheckDriver.linkPhoneNumber}" />
									<font class="must">*</font>
							</div>
							<label class="form_label col-1">初次领证日期</label>
							<div class="form_input col-3">
								<c:choose>
									<c:when test="${checkCarVo.serialNo eq 1}">
										<input type="text" class="Wdate" id="acceptLicenseDate"
											name="checkCarVo.prpLCheckDriver.acceptLicenseDate"
											value="<app:date date='${checkCarVo.prpLCheckDriver.acceptLicenseDate}'/>"
											style="width: 95%"
											onfocus="WdatePicker({maxDate:'%y-%M-%d'})"/>
									</c:when>
									<c:otherwise>
										<input type="text" class="Wdate" id="acceptLicenseDate"
											name="checkCarVo.prpLCheckDriver.acceptLicenseDate"
											value="<app:date date='${checkCarVo.prpLCheckDriver.acceptLicenseDate}'/>"
											style="width: 95%"
											onfocus="WdatePicker({maxDate:'%y-%M-%d'})"/>
											<font class="must">*</font>
									</c:otherwise>
								</c:choose>
							</div>
							<label class="form_label col-1">年龄</label>
							<div class="form_input col-3">
								<input style="width: 95%" type="text" class="input-text"
									id="driverAge" name="checkCarVo.prpLCheckDriver.driverAge"
									datatype="*0-0|age" errormsg="请输入0-120岁！"
									value="${checkCarVo.prpLCheckDriver.driverAge}" />
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">驾驶证有效日期</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate" id="driverValidDate"
									name="checkCarVo.prpLCheckDriver.driverValidDate"
									value="<app:date date='${checkCarVo.prpLCheckDriver.driverValidDate}'/>"
									style="width: 95%" onfocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"/>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 驾驶人信息结束 -->

			<div class="table_wrap">
				<div class="table_title f14">查勘信息</div>
				<div id="carCheckInfo">
					<span class="radio-box">
						<app:codeSelect codeType="DamageFlag" type="radio" nullToVal="0"
						onchange="checkInfo()" name="checkCarVo.lossFlag" value="${checkCarVo.lossFlag}" />
					</span>
					<div class="table_cont ">
						<div class="formtable tableoverlable">
							<div class="row  cl">
								<c:if test="${checkCarVo.serialNo eq 1}">
								<label class="form_label col-1">查勘地点</label>
								<div class="form_input col-3">
									<input type="text" class="input-text" id="checkAdress"
										name="checkCarVo.checkAdress" style="width: 95%"
										value="${checkCarVo.checkAdress}" maxlength="100"/>
								</div>
								</c:if>
								<label class="form_label col-1">估损金额</label>
								<div class="form_input col-3">
									<input type="text" class="input-text" id="lossFee"
										name="checkCarVo.lossFee" datatype="*0-0|amount"
										nullmsg="请输入估损金额！" value="${checkCarVo.lossFee}"
										errormsg="请输入正确的金额，只能精确到两位小数！" style="width: 95%"/>
										<font class="must">*</font>
								</div>
								<label class="form_label col-1">施救费</label>
								<div class="form_input col-3">
									<input type="text" class="input-text" id="rescueFee" name="checkCarVo.rescueFee"
										   value="${checkCarVo.rescueFee}" datatype="*0-0|amount"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-1">查勘日期</label>
								<div class="form_input col-3">
									<input type="text" class="Wdate" id="checkTime"
										name="checkCarVo.checkTime"
										value="<app:date date='${checkCarVo.checkTime}'/>"
										onfocus="WdatePicker({maxDate:'%y-%M-%d'})"
										style="width: 95%"/>
								</div>
								<label class="form_label col-1">事故责任比例</label>
								<div class="form_input col-3">
									<span class="select-box" style="width: 46%"> <app:codeSelect
											codeType="IndemnityDuty" type="select" id="Iindemnitydutys"
											name="checkDutyVo.indemnityDuty" clazz="must" nullToVal="4"
											onchange="changeDuty()"
											value="${checkDutyVo.indemnityDuty}"/>
									</span> <input type="text" class="input-text" style="width: 48%"
										id="IindemnityDutyRates" name="checkDutyVo.indemnityDutyRate" maxlength="3"
										onchange="compareDutyRate(this)" datatype="/^(\d{1,2}(\.\d{1,2})?|100(\.0{1,2})?)$/"
										value="${checkDutyVo.indemnityDutyRate}" nullToVal="0.0" nullmsg="请输入信息！"/>
										<font class="must">*</font>
								</div>
								<c:if test="${checkCarVo.serialNo eq '1'}">
								<label class="form_label col-1">险别</label>
								<div class="form_input col-3">
									<span class="select-box">
											<!-- onchange="kindSelect(${checkCarVo.serialNo})" -->
											<app:codeSelect codeType="" type="select" id="kindCode"
											lableType="code-name" name="checkCarVo.kindCode" dataSource="${carKindMap}"
											value="${checkCarVo.kindCode}" nullToVal="A"/><font class="must">*</font>
									</span>
								</div>
								</c:if>
							</div>
							<br />
							<div class="row mb-6 cl">
								<label class="form-label col-1 text-c"><font class="must">*</font>损失部位</label>
								<div class="formControls col-11" id="lossDiv">
									<app:codeSelect type="checkbox" codeType="LossPart"
										name="checkCarVo.lossPart" value="${checkCarVo.lossPart}"/>
										<!-- <input type="checkbox" id="mns" name="comple">全车 -->
										<label class="check-box f1"><input type="checkbox" id="mns" name="comple"
										class="allLossCbx" originvalue="" style="vertical-align:middle" onclick="allCheck(this)"/>全车</label>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-1 text-c"></label>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 查勘信息结束 -->

			<br/> <br/>
			<!-- 底部按钮 -->
			<div class="btn-footer clearfix text-c">
				<!-- <button class="btn btn-primary ml-15"
						id="saveCarInfo" onclick="saveCarInfos()">暂存</button> -->
				<input class="btn btn-primary ml-15" type="button" id="saveCarInfo" 
						onclick="saveCarInfos()" value="保存"/>
				<!-- <a class="btn btn-disabled ml-15" type="button" id="closeCarLoss">关闭</a> -->
				<button type="button" id="closeCarInfo" class="btn btn-disabled ml-15" onclick="closeCarLoss('${nodeCode}')">关闭</button>
			</div>
			<br/><br/>
		</form>
	</div>
	<script type="text/javascript" src="/claimcar/js/checkEdit/checkcar.js"></script>
	<script type="text/javascript">
		//------
	</script>
</body>
</html>