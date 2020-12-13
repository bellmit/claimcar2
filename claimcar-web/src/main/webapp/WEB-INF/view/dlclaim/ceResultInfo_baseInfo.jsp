<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
		<div class="table_wrap">
			<div class="table_title f14">基本信息</div>
				<div class="table_cont basicInforoVer">
					<div class="formtable">	
						<div class="row cl">
							<label class="form_label col-2">报案号:</label>
							<div class="form_input col-2">
								${prplcecheckResultVo.claimNotificationNo}
							</div>
							<label class="form_label col-3">任务号:</label>
							<div class="form_input col-2">
								${prplcecheckResultVo.estimateTaskNo}
							</div>
							<label class="form_label col-2">提取码:</label>
							<div class="form_input col-1">
								${prplcecheckResultVo.extractionCode}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">是否大案:</label>
							<div class="form_input col-2">
							<c:choose>
							<c:when test="${prplcecheckResultVo.isBigCase eq '1' }">
							是
							</c:when>
							<c:otherwise>
							否
							</c:otherwise>
							</c:choose>
							</div>
							<label class="form_label col-3">预判金额:</label>
							<div class="form_input col-2">
							  ${prplcecheckResultVo.anticipationLoss}
							</div>
							<label class="form_label col-2">保险公司业务节点:</label>
							<div class="form_input col-1">
								${prplcecheckResultVo.operateName}
							</div>
						</div>
					   <!--  </div>
					   
						<div class="formtable">	 -->
						<div class="row cl">
							<label class="form_label col-2">车牌号:</label>
							<div class="form_input col-2">
								${prplcecheckResultVo.licensePlateNo}
							</div>
							<label class="form_label col-3">是否标的车:</label>
							<div class="form_input col-2">
							    <c:choose>
							<c:when test="${prplcecheckResultVo.subjectThird eq '1' }">
							是
							</c:when>
							<c:otherwise>
							否
							</c:otherwise>
							</c:choose>	
							</div>
							<label class="form_label col-2">车辆品牌编码:</label>
							<div class="form_input col-1">
								${prplcecheckResultVo.vehicleBrandCode}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">车辆品牌名称:</label>
							<div class="form_input col-2">
							${prplcecheckResultVo.vehicleBrandName}
							</div>
							<label class="form_label col-3">车型名称:</label>
							<div class="form_input col-2">
							${prplcecheckResultVo.modelName}	
							</div>
							<label class="form_label col-2">车架号:</label>
							<div class="form_input col-1">
							${prplcecheckResultVo.vin}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">案件检测开始时间:</label>
							<div class="form_input col-2">
							  <fmt:formatDate value="${prplcecheckResultVo.detectionStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-3">案件检测结束时间:</label>
							<div class="form_input col-2">
							  <fmt:formatDate value="${prplcecheckResultVo.detectionEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-2">总减损金额:</label>
							<div class="form_input col-1">
							 ${prplcecheckResultVo.totalRestValue}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">案件检测报告的URL地址:</label>
							<div class="form_input col-10">
							   ${prplcecheckResultVo.reportURL}
							</div>
						</div>
						
					</div>
				</div>
			</div>	
			