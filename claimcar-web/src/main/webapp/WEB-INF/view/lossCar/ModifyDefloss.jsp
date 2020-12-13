<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>车辆定损修改</title>
		<style type="text/css">
			.text-r{
				background-color: #F5F5F5
			}
		</style>
	</head>
	<body>
	
		<div class="fixedmargin page_wrap">
			<form  id="lossAdjustform" role="form" method="post" action="" name="fm" >
				<input type="hidden" id="lossId" name="lossId" value="${lossCarMainVo.id }"/>
				<input type="hidden" id="registNo" value="${lossCarMainVo.registNo }"/>
				<input type="hidden" id="deflossFlag" value="${deflossFlag }"/>
				<div class="table_wrap">
				<div class="table_title f14">车辆信息</div>
					<div class="table_cont ">
						<div class="formtable">	
							<div class="row cl">
								<label class="form_label col-2">报案号</label>
								<div class="form_input col-2">
									${lossCarMainVo.registNo }
								</div>
								<label class="form_label col-2">赔案类别</label>
								<div class="form_input col-2">
									<app:codetrans codeType="CaseCode" codeCode="${commonVo.claimType }"/>
								</div>
								<label class="form_label col-2">案件性质</label>
								<div class="form_input col-2">
									<app:codetrans codeType="Lflag" codeCode="${lossCarMainVo.lflag }"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">定损车牌号码</label>
								<div class="form_input col-2">
									${carInfoVo.licenseNo }
								</div>
								<label class="form_label col-2">损失方</label>
								<div class="form_input col-2">
									<c:choose>
										<c:when test="${lossCarMainVo.deflossCarType eq '1'}"> 
											主车
										</c:when>
										<c:otherwise>
											三者车
										</c:otherwise>
									</c:choose>
								</div>
								<label class="form_label col-2">车主</label>
								<div class="form_input col-2">
									${carInfoVo.carOwner }
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">初登日期</label>
								<div class="form_input col-2">
									<app:date  date='${carInfoVo.enrollDate }'/>
								</div>
								<label class="form_label col-2">车辆种类</label>
								<div class="form_input col-2"> 
									<c:choose>
										 <c:when test="${lossCarMainVo.deflossCarType eq '1'}"> 
											<app:codetrans codeType="VehicleTypeShow" codeCode="${prpLCItemCarVo.carType }" />
										 </c:when>
									     <c:otherwise>
									     	<app:codetrans codeType="VehicleType" codeCode="${carInfoVo.platformCarKindCode }" />
									    </c:otherwise>
							 		</c:choose>
									<app:codetrans codeType="CarKind" codeCode="${carInfoVo.carKindCode }" />
								</div>
								<label class="form_label col-2">车架号</label>
								<div class="form_input col-2">
									${carInfoVo.frameNo }
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">车牌种类</label>
								<div class="form_input col-2">
									<app:codetrans codeType="LicenseKindCode" codeCode="${carInfoVo.licenseType }" />
								</div>
								<label class="form_label col-2">车型名称</label>
								<div class="form_input col-2">
									${carInfoVo.modelName }
								</div>
							</div>
						</div>
					</div>
				</div>	
			<div class="table_wrap">
				<div class="table_title f14">车辆定损修改</div>
				<div class="table_cont ">
					<table class="table table-border">
						<thead class="text-c">
							<tr>
								<th >定损类型</th>
								<th>损失方</th>
							</tr>
						</thead>
						<tbody>
						<tr class="text-c">
							<td>车辆定损修改</td>
							<td>${carInfoVo.licenseNo }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form>
			<div class="text-c">
				<c:if test="${commonVo.handleStatus ne '1' }">
					<input class="btn btn-primary" type="button" onclick="launchDefloss()" value="提交">
				</c:if>
			</div>
		</div>
		<script type="text/javascript" src="/claimcar/js/deflossEdit/DeflossAdjust.js"></script>
		
	</body>

</html>