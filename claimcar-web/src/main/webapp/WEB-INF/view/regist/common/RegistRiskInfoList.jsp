<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
	<!-- 弹出出险信息 -->
		<div class="f-12 table_cont" name="RegistRiskInfo">
			
			<c:choose>
				<c:when test="${!empty prpLRegistRiskInfoMap['CI-No']}">
					<div class="CiIndemInfo  mb-5">
						<input type="hidden" name="prpLRegistRiskInfoMap" value="${prpLRegistRiskInfoMap}" />
						<span>交强险保单：${prpLRegistRiskInfoMap['CI-No']}</span><br>
						<span>有效期内出险次数：</span> <span>${empty prpLRegistRiskInfoMap['CI-DangerNum']? 0:prpLRegistRiskInfoMap['CI-DangerNum']}</span><br>
						<span>7天内出险次数:</span> <span>${empty prpLRegistRiskInfoMap['CI-DangerInSum'] ? 0:prpLRegistRiskInfoMap['CI-DangerInSum']}</span><br>	
						<c:if test="${prpLRegistRiskInfoMap['CIRepeatReport']=='1'}">
						 	<span>该保单48小时内已报过案</span> <span class="c-red">疑似重复报案</span><br>
						</c:if>
						<c:if test="${prpLRegistRiskInfoMap['CIPolicy-A7'] eq '1'}">
					     <span>交强保单生效后7天内出险</span><br> 
					     </c:if>
					     <c:if test="${prpLRegistRiskInfoMap['CIPolicy-B7'] eq '1'}">
					     <span>交强保单失效前7天内出险</span><br> 
					     </c:if>
						<c:if test="${recoveryFlag2 == '1'}">
						<span STYLE="color: red">当前核赔涉及追偿处理</span>
						</c:if>
					</div>
					<hr style="color:#333">
				</c:when>
				<c:otherwise>
				
				</c:otherwise>
			</c:choose>
			
			<c:choose>
				<c:when test="${!empty prpLRegistRiskInfoMap['BI-No']}">
					<div class="IndemnityInfo mb-5 ">
						<span>商业险保单：${prpLRegistRiskInfoMap['BI-No']}</span><br>	
						<span>有效期内出险次数：</span> <span>${empty prpLRegistRiskInfoMap['BI-DangerNum']? 0:prpLRegistRiskInfoMap['BI-DangerNum']}</span><br>
						<span>7天内出险次数:</span> <span>${empty prpLRegistRiskInfoMap['BI-DangerInSum']? 0:prpLRegistRiskInfoMap['BI-DangerInSum']}</span><br>
						<c:if test="${prpLRegistRiskInfoMap['BIRepeatReport']=='1'}">
						 	<span>该保单48小时内已报过案</span> <span class="c-red">疑似重复报案</span><br>
						</c:if>
						<c:if test="${registNo ne ''}">
						<span>车身划痕出险次数:</span> <span>${empty prpLRegistRiskInfoMap['BI-CSHH']? 0:prpLRegistRiskInfoMap['BI-CSHH']}</span><br> 
						<span>剩余金额：</span> <span>${empty prpLRegistRiskInfoMap['BI-HHJE']? 0:prpLRegistRiskInfoMap['BI-HHJE']}</span><br>
						</c:if>
						<c:if test="${registNo ne ''}">
						<span>附加车轮单独损失险剩余保额:</span> <span>${empty prpLRegistRiskInfoMap['W1RESTVALUE']? 0:prpLRegistRiskInfoMap['W1RESTVALUE']}</span><br>
						</c:if> 
						<c:if test="${prpLRegistRiskInfoMap['BIPolicy-A7'] eq '1'}">
					     <span>商业保单生效后7天内出险</span><br> 
					     </c:if>
					  <c:if test="${prpLRegistRiskInfoMap['BIPolicy-B7'] eq '1'}">
					     <span>商业保单失效前7天内出险</span><br> 
					     </c:if>
						<c:if test="${recoveryFlag1 == '1'}">
						<span STYLE="color: red">当前核赔涉及追偿处理</span>
						</c:if> 
					</div>
					<hr style="color:#333">
				</c:when>
					<c:otherwise>
					</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${reconcileFlag eq '1'}" >
	                 <span STYLE="color: red">该案件为现场调解案件</span><br>
	                 </c:when>
	                 <c:otherwise>
	                 
	                 </c:otherwise>
	        </c:choose>
	        <c:choose>
				<c:when test="${index eq '1'}" >
	                 <span STYLE="color: red">该案件为诉讼案件</span><br>
	                 </c:when>
	                 <c:otherwise>
	                 
	                 </c:otherwise>
	        </c:choose>    
			<div class="ExtendInfo">
				<span>是否夜间出险</span>
				<c:choose>
					<c:when test="${prpLRegistRiskInfoMap['YJCX'] eq '1'}">
					 	<span>是</span><br>
					</c:when>
					<c:otherwise>
						<span>否</span><br>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${prpLRegistRiskInfoMap['BA-D48'] eq '1'}">
					 	<span>报案时间超过出险时间48小时</span><br>
					</c:when>
					<c:otherwise>
						
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${prpLRegistRiskInfoMap['DWQC'] eq '1'}">
					 	<span class="c-red">该案件为代位求偿案件，可能涉及追偿</span><br> 
					</c:when>
					<c:otherwise>
						
					</c:otherwise>
				</c:choose>
				
			</div>
		</div>


	<!-- 弹出出险信息结束 -->


