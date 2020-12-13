<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpLRegistCarLoss" varStatus="status" items="${prpLRegistCarLosses}">
	<div class="table_cont pb-10">
		<div class="formtable ">
			<!-- prplregistcarloss 隐藏域  开始 -->
			<input type="hidden" name="prpLRegistCarLosses[${status.index+carSize}].id" value="" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index+carSize}].frameNo" value="" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index+carSize}].lossparty" value="3" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index+carSize}].createUser" value="${prpLRegistCarLoss.createUser}" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index+carSize}].createTime" value="<fmt:formatDate value='${prpLRegistCarLoss.createTime}' type="both" />" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index+carSize}].updateUser" value="${prpLRegistCarLoss.updateUser}" />
			<input type="hidden" name="prpLRegistCarLosses[${status.index+carSize}].updateTime" value="<fmt:formatDate value='${prpLRegistCarLoss.updateTime}' type="both" />" />
			<!-- prplregistcarloss 隐藏域  结束 -->
			<div class="row cl">
			 <label class="form_label col-1">车牌号</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" id="licenseNo" name="prpLRegistCarLosses[${status.index+carSize}].licenseNo" onchange="linceseNoChg(this)" value="${prpLRegistCarLoss.licenseNo}" datatype="carLicenseNo" />
				</div>
				
				<label class="form_label col-3">厂牌型号</label>
				<div class="form_input col-2">
					<app:codeSelect clazz="input-text" style="width: 96%" codeType="BrandCode" lableType="code-name"
					name="prpLRegistCarLosses[${status.index+carSize}].brand" value="${prpLRegistCarLoss.brand}" type="select" />
				</div>
			</div>
			<div class="row cl">
				 <div style="height:15px;"></div>
			</div>
			<div class="row cl">
				<label class="form_label col-1">损失部位</label>
				<!-- <span class="form_label mt-15"> -->
				<div class="form_input col-10">
					<app:codeSelect codeType="LossPart" id="lossPart" name="prpLRegistCarLosses[${status.index+carSize}].losspart" 
					value="${prpLRegistCarLoss.losspart}" type="checkbox" />
				</div>
				<!-- </span> -->
				<%-- <label class="check-box f1">
					<input type="checkbox" class="allLossCbx_3" originvalue="" style="vertical-align:middle" id="lossPart_${status.index+carSize}" name="prpLRegistCarLosses"
					onclick="allCheck(this)" value="全部">全部</label>
					<input type="checkbox" class="allLossCbx_3" originvalue="" style="vertical-align:middle" id="lossPart_${status.index+carSize}" name="prpLRegistCarLosses"
					>全部
					</label> --%>
			</div>
			<div class="row cl">
				<!-- //删除行驶状态 任务288 -->
				<div class="form_input col-8 hidden" id="Loss_${status.index+carSize}">
					<input type="text" class="input-text" id="lossremark"  style="width: 100%" name="prpLRegistCarLosses[${status.index+carSize}].lossremark" maxlength="200" value="${prpLRegistCarLoss.lossremark}" />
				</div>
			</div>
			<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao delSancheBtn"onclick="delThirdCar(this)"  name="thirdCar_${status.index+carSize}">刪除</button>
		</div> 
	</div>
</c:forEach>
