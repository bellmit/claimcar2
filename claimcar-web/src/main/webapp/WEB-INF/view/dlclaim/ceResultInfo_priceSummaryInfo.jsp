<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
		<div class="table_wrap">
			<div class="table_title f14">减损统计汇总</div>
				<div class="table_cont basicInforoVer">
					<div class="formtable">	
						<div class="row cl">
							<label class="form_label col-2">保险公司提交金额:</label>
							<div class="form_input col-2">
								${prplpriceSummaryVo.confirmLossPrice}
							</div>
							<label class="form_label col-3">欺诈风险命中数量:</label>
							<div class="form_input col-2">
								${prplpriceSummaryVo.fraudRiskHit}
							</div>
							<label class="form_label col-2">配件总金额:</label>
							<div class="form_input col-1">
								${prplpriceSummaryVo.partTotalPrice}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">ce建议配件总金额:</label>
							<div class="form_input col-2">
							 ${prplpriceSummaryVo.cePartTotalPrice}
							</div>
							<label class="form_label col-3">ce建议配件减损总金额:</label>
							<div class="form_input col-2">
							  ${prplpriceSummaryVo.savingPartTotalPrice}
							</div>
							<label class="form_label col-2">工时总金额:</label>
							<div class="form_input col-1">
								${prplpriceSummaryVo.laborTotalPrice}
							</div>
						</div>
					   <!--  </div>
					   
						<div class="formtable">	 -->
						<div class="row cl">
							<label class="form_label col-2">ce建议工时总金额:</label>
							<div class="form_input col-2">
								${prplpriceSummaryVo.ceLaborTotalPrice}
							</div>
							<label class="form_label col-3">ce建议工时减损总金额:</label>
							<div class="form_input col-2">
							    ${prplpriceSummaryVo.savingLaborTotalPrice}
							</div>
							<label class="form_label col-2">辅料总金额:</label>
							<div class="form_input col-1">
								${prplpriceSummaryVo.smallPartTotalPrice}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">ce建议辅料总金额:</label>
							<div class="form_input col-2">
							${prplpriceSummaryVo.ceSmallPartTotalPrice}
							</div>
							<label class="form_label col-3">ce建议辅料减损总金额:</label>
							<div class="form_input col-2">
							${prplpriceSummaryVo.savingSmallPartTotalPrice}	
							</div>
							<label class="form_label col-2">金额总计:</label>
							<div class="form_input col-1">
							${prplpriceSummaryVo.totalPrice}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">ce建议金额总计:</label>
							<div class="form_input col-2">
							  ${prplpriceSummaryVo.ceTotalPrice}
							</div>
							<label class="form_label col-3">ce建议减损金额总计:</label>
							<div class="form_input col-2">
							 ${prplpriceSummaryVo.savingTotalPrice}
							</div>
							<label class="form_label col-2">保险公司提交的施救费:</label>
							<div class="form_input col-1">
							 ${prplpriceSummaryVo.rescueFee}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-2">ce建议施救费:</label>
							<div class="form_input col-2">
							   ${prplpriceSummaryVo.ceRescueFee}
							</div>
							<label class="form_label col-3">ce建议施救费减损金额:</label>
							<div class="form_input col-2">
							   ${prplpriceSummaryVo.savingRescueFee}
							</div>
						</div>
						
					</div>
				</div>
			</div>	
			