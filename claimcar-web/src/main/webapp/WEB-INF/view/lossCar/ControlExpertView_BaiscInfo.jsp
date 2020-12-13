<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
		<div class="table_wrap">
			<div class="table_title f14">基本信息</div>
				<div class="table_cont basicInforoVer">
					<div class="formtable">	
						<div class="row cl">
							<label class="form_label col-2">报案号:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo.registNo}
							</div>
							<label class="form_label col-2">车牌号:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo.vehicleRegistionNumber}
							</div>
							<label class="form_label col-2">品牌名称:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo.vehicleBrandName}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">VIN码:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo.vin}
							</div>
							<label class="form_label col-2">损失方:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo.lossType}
							</div>
							<label class="form_label col-2">车型名称:</label>
							
							<div class="form_input col-2">
								${prplTestinfoMainVo.vehicleModelName}	
							</div>
						</div>
						</div>
						<div class="formtable">	
						<div class="row cl">
							<label class="form_label col-2">查勘员:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo.checkEmployee}
							</div>
							<label class="form_label col-2">定损员:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo.confirmlossEmployee}
							</div>
							<label class="form_label col-2">修理厂名称:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo.repairFactoryName}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">出险时间:</label>
							<div class="form_input col-2">
								<fmt:formatDate value="${prplTestinfoMainVo.eventDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-2">定损时间:</label>
							<div class="form_input col-2">
								<fmt:formatDate value="${prplTestinfoMainVo.confirmlossDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-2">案件检测时间:</label>
							<div class="form_input col-2">
								<fmt:formatDate value="${prplTestinfoMainVo.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
						</div>
						
					</div>
				</div>
			</div>	