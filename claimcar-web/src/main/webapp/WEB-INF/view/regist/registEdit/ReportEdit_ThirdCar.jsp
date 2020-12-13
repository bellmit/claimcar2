<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpLRegistCarLoss" varStatus="status" items="${prpLRegistCarLosses}">
	<c:if test="${ !(prpLRegistCarLoss.lossparty eq 1) }">
	<div class="table_cont pb-10">
		<div class="formtable ">
			<!-- prplregistcarloss 隐藏域  开始 -->
			<input type="hidden" name="prpLRegistCarLosses[${status.index}].id" value="${prpLRegistCarLoss.id}" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index}].frameNo" value="33333" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index}].lossparty" value="3" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index}].createUser" value="${prpLRegistCarLoss.createUser}" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index}].createTime" value="<fmt:formatDate value='${prpLRegistCarLoss.createTime}' type="both" />" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index}].updateUser" value="${prpLRegistCarLoss.updateUser}" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index}].updateTime" value="<fmt:formatDate value='${prpLRegistCarLoss.updateTime}' type="both" />" />
			<!-- prplregistcarloss 隐藏域  结束 -->
			<div class="row cl">
			 <label class="form_label col-2">车牌号</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" id="licenseNo" name="prpLRegistCarLosses[${status.index}].licenseNo" onchange="linceseNoChg(this)" value="${prpLRegistCarLoss.licenseNo}" datatype="carLicenseNo" />
				</div>
				
				<label class="form_label col-3">厂牌型号</label>
				<div class="form_input col-2">
					<app:codeSelect clazz="input-text" style="width: 96%" codeType="BrandCode" 
					name="prpLRegistCarLosses[${status.index}].brand" value="${prpLRegistCarLoss.brand}" type="select" />
				</div>
			</div>
			
			
			<div class="row cl">
				<label class="form_label col-1">损失部位</label>
				<!-- <span class="form_label mt-15"> -->
				<div class="form_input col-10">
					<app:codeSelect codeType="LossPart" id="lossPart" name="prpLRegistCarLosses[${status.index}].losspart" 
					value="${prpLRegistCarLoss.losspart}" type="checkbox" />
				</div>
				<label class="check-box f1">
					<%-- <input type="checkbox" class="allLossCbx_3" originvalue="" style="vertical-align:middle" id="lossPart_${status.index+carSize}" name="prpLRegistCarLosses"
					onclick="allCheck(this)">全部 --%>
					<%-- <input type="checkbox" class="allLossCbx_3" originvalue="" style="vertical-align:middle" id="lossPart_${status.index+carSize}" name="prpLRegistCarLosses">全部 --%>
				</label>
			</div>
			<div class="row cl">
				<!-- //删除行驶状态 任务288 -->
				<c:if test="${!empty prpLRegistCarLoss.lossremark}">
				<div class="form_input col-8" id="Loss_${status.index}">
					<input type="text" class="input-text" id="lossremark"  style="width: 100%" name="prpLRegistCarLosses[${status.index}].lossremark" maxlength="200" value="${prpLRegistCarLoss.lossremark}" datatype="*0-200" placeholder="请输入0-200个字符" />
				</div>
				</c:if>
				<c:if test="${empty prpLRegistCarLoss.lossremark}">
				<div class="form_input col-8 hidden" id="Loss_${status.index}">
					<input type="text" class="input-text" id="lossremark"  style="width: 100%" name="prpLRegistCarLosses[${status.index}].lossremark" maxlength="200" value="${prpLRegistCarLoss.lossremark}" datatype="*0-200" placeholder="请输入0-200个字符" />
				</div>
				</c:if>
			</div>
			<c:if test="${prpLRegistVo.registTaskFlag eq 1}">
			<div class="row cl">
				<label class="form_label fl">推荐修理厂名称</label>
				<div class="form_input col-10">
						<input type="text" class="input-text" style="width: 100%" value="${prpLRegistCarLoss.repairName}" maxlength="200" datatype="s0-200"  />
				</div>
			</div>
			</c:if>
         
			<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delSancheBtn" onclick="delThirdCar(this)"  name="thirdCar_${status.index}">刪除</button>
		   </div> 
		</div>
	</c:if>
</c:forEach>
