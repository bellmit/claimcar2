<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_wrap">
	<div class="table_title f14">保单基本信息</div>
	<div class="table_cont">
		<div class="formtable">
			<div class="row cl">
				<label class="form_label col-1">保单号</label>
				<div class="form_input col-3">${prpLCMainVo.policyNo }</div>
				<label class="form_label col-1">险种名称</label>
				
				<c:set var="riskCode">
					<app:codetrans codeType="CarRiskCode" codeCode="${prpLCMainVo.riskCode }"/>
				</c:set>
				<div class="form_input col-2">${riskCode } </div>
				<label class="form_label col-2">ID（供财务收付查询使用）</label>
				<div class="form_input col-3"> </div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">被保险人</label>
				<div class="form_input col-3">${prpLCMainVo.insuredName }</div>
				<label class="form_label col-1">客户等级</label>
				<div class="form_input col-3">${prpLRegistVo.customerLevel}</div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">保险币别</label>
				<div class="form_input col-3">${prpLCMainVo.currency }</div>
				<label class="form_label col-1">保险金额</label>
				<div class="form_input col-3">25777785</div>
				<label class="form_label col-1">调度次数</label>
				<div class="form_input col-3">1</div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">起保日期</label>
				<c:set var="startDate">
					<fmt:formatDate value="${prpLCMainVo.startDate}"/>
				</c:set>
				<div class="form_input col-2">${startDate}</div>
				<label class="form_label col-2">终保日期</label>
				<c:set var="endDate">
					<fmt:formatDate value="${prpLCMainVo.endDate}"/>
				</c:set>
				<div class="form_input col-2">${endDate}</div>
				<label class="form_label col-2">承保公司</label>
				<div class="form_input col-2">${prpLCMainVo.comCode}</div>
			</div>
						<div class="row cl">
				<label class="form_label col-1">车牌号码</label>
				<div class="form_input col-2">${prpLRegistExtVo.licenseNo}</div>
				<label class="form_label col-2">号牌底色</label>
				
				<c:set var="licenseColorCode">
					<app:codetrans codeType="ColorCode" codeCode="${prpCItemCar.licenseColorCode}"/>
				</c:set>
				<div class="form_input col-2">${licenseColorCode}</div>
				<label class="form_label col-2">车辆种类</label>
				
				<c:set var="carKindCode">
					<app:codetrans codeType="CarKind" codeCode="${prpCItemCar.carKindCode}"/>
				</c:set>
				<div class="form_input col-2">${carKindCode}</div>
			</div>
						<div class="row cl">
				<label class="form_label col-1">车型名称</label>
				<c:set var="carType">
					<app:codetrans codeType="CarTypeShow" codeCode="${prpCItemCar.carType}"/>
				</c:set>
				<div class="form_input col-2">${carType}</div>
				<!-- <label class="form_label col-2">条款代码</label>
				<div class="form_input col-2">机动车辆保险条款【F19】</div> -->
				<label class="form_label col-2">代理人</label>
				<div class="form_input col-2">${prpLCMainVo.agentCode}</div>
			</div>
		</div>
	</div>
</div>


<div class="table_wrap">
	<div class="table_title f14">报案基本信息</div>
	<div class="table_cont">
		<div class="formtable">
			<div class="row cl">
				<label class="form_label col-1">报案号</label>
				<div class="form_input col-3">${prpLCMainVo.registNo }</div>
				<label class="form_label col-1">出险区域</label>
				<div class="form_input col-2">${prpLRegistVo.damageAddress }</div>
				<label class="form_label col-2">出险次数</label>
				<div class="form_input col-3">
					${prpLCMainVo.registTimes }<span>次</span>
					<input class="btn ml-5" id="" onclick="caseDetails('${prpLCMainVo.registNo}')" type="button" value="...">  
				</div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">出险地点</label>
				<div class="form_input col-3">${prpLRegistVo.damageAddress } </div>
				<label class="form_label col-1">出险原因</label>
				<div class="form_input col-2"><app:codetrans codeType="DamageCode" codeCode="${prpLRegistVo.damageCode }"/></div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">出险日期</label>
				<c:set var="damageTime">
					<fmt:formatDate value="${prpLRegistVo.damageTime}"/>
				</c:set>
				<div class="form_input col-2">${damageTime}</div>
				<label class="form_label col-2">报案日期</label>
				<c:set var="reportTime">
					<fmt:formatDate value="${prpLRegistVo.reportTime}"/>
				</c:set>
				<div class="form_input col-2">${reportTime}</div>
				<label class="form_label col-2">查勘地点</label>
				<div class="form_input col-2">${prpLRegistExtVo.checkAddress}</div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">报案人</label>
				<div class="form_input col-3">${prpLRegistVo.reportorName}</div>
				<label class="form_label col-1">报案人电话</label>
				<div class="form_input col-3">${prpLRegistVo.reportorPhone}</div>
				<label class="form_label col-1">与被保险人关系</label>
				<div class="form_input col-3"><app:codetrans codeType="InsuredIdentity" codeCode="${prpLRegistVo.reportorRelation}"/></div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">驾驶人姓名</label>
				<div class="form_input col-2">${prpLRegistVo.driverName}</div>
				<label class="form_label col-2">案件紧急程度</label>
				<div class="form_input col-2"><app:codetrans codeType="MercyFlag" codeCode="${prpLRegistVo.mercyFlag}"/></div>
				<label class="form_label col-2">损失金额</label>
				<div class="form_input col-2">${prpLClaimVo.sumDefLoss} </div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">出险经过说明</label>
				<div class="form_input col-2">${prpLRegistExtVo.dangerRemark}</div>
				<label class="form_label col-2">赔案类别</label>
				<div class="form_input col-2">正常赔案</div>
				<label class="form_label col-2">车牌号</label>
				<div class="form_input col-2">${prpLRegistExtVo.licenseNo}</div>
			</div>
		</div>
	</div>
</div>

