<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
		<div class="table_wrap">
			<div class="table_title f14">基本信息</div>
				<div class="table_cont basicInforoVer">
					<div class="formtable">	
						<div class="row cl">
							<label class="form_label col-2">报案号:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo2.registNo}
							</div>
							<label class="form_label col-2">车牌号:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo2.vehicleRegistionNumber}
							</div>
							<label class="form_label col-2">品牌名称:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo2.vehicleBrandName}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">VIN码:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo2.vin}
							</div>
							<label class="form_label col-2">损失方:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo2.lossType}
							</div>
							<label class="form_label col-2">车型名称:</label>
							
							<div class="form_input col-2">
								${prplTestinfoMainVo2.vehicleModelName}	
							</div>
						</div>
						</div>
						<div class="formtable">	
						<div class="row cl">
							<label class="form_label col-2">查勘员:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo2.checkEmployee}
							</div>
							<label class="form_label col-2">定损员:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo2.confirmlossEmployee}
							</div>
							<label class="form_label col-2">修理厂名称:</label>
							<div class="form_input col-2">
								${prplTestinfoMainVo2.repairFactoryName}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">出险时间:</label>
							<div class="form_input col-2">
								<fmt:formatDate value="${prplTestinfoMainVo2.eventDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-2">定损时间:</label>
							<div class="form_input col-2">
								<fmt:formatDate value="${prplTestinfoMainVo2.confirmlossDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-2">案件检测时间:</label>
							<div class="form_input col-2">
								<fmt:formatDate value="${prplTestinfoMainVo2.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
						</div>
						
					</div>
				</div>
			</div>	