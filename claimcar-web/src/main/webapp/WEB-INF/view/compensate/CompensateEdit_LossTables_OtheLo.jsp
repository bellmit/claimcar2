<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpLLossProp" items="${otherLossProps }" varStatus="status">
	<c:set var="otherSize" value="${status.index + size  }" />
	<tr class="detail-body">
		<input type="hidden" name="otherLoss[${otherSize }].propType" value="9" />
		<input type="hidden" name="otherLoss[${otherSize}].dlossId" value="${prpLLossProp.dlossId }" />
		<input type="hidden" name="otherLoss[${otherSize}].riskCode" value="${prpLLossProp.riskCode }"/>
		<input type="hidden" name="otherLoss[${otherSize}].id" value="${prpLLossProp.id }"/>
		<input type="hidden" name="otherLoss[${otherSize}].dlossIdExt" value="${prpLLossProp.dlossIdExt }"/>
		<td width="10%">
			<c:if test ="${empty prpLLossProp.dlossId }">
				<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao " name="otherLossVo_${otherSize }"  
					onclick="delPayInfo(this,'othSize')" > </button>
			</c:if>
			<c:if test ="${not empty prpLLossProp.dlossId }">
				<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao btn-disabled" name="otherLossVo_${otherSize}" 
					disabled="disabled"> </button>
			</c:if>
			
			
		</td>
		<td >
			<app:codetrans codeCode="${prpLLossProp.kindCode }" codeType="KindCode" riskCode="${prpLLossProp.riskCode }"/>
			<input type="hidden" name="otherLoss[${otherSize }].kindCode" value="${prpLLossProp.kindCode }"  />
		</td>
		<td width="10%">
			<c:if test="${(prpLLossProp.kindCode =='X' && cprcCase eq false) && (prpLLossProp.kindCode !='RS' && prpLLossProp.kindCode !='VS' && prpLLossProp.kindCode !='DS' && prpLLossProp.kindCode !='DC')}">
			  <app:codeSelect codeType="name" name="otherLoss[${otherSize }].itemName" type="select" value="${prpLLossProp.itemName }" dataSource="${deviceMap }" clazz="must" />
			</c:if>
			<c:if test="${(prpLLossProp.kindCode !='X' || cprcCase) && (prpLLossProp.kindCode !='RS' && prpLLossProp.kindCode !='VS' && prpLLossProp.kindCode !='DS' && prpLLossProp.kindCode !='DC') }">
				<input type="text" class="input-text" name="otherLoss[${otherSize }].itemName"  value="${prpLLossProp.itemName }"  datatype="*" nullmsg="名称不能为空" />
			</c:if>
			<c:if test="${prpLLossProp.kindCode =='RS' || prpLLossProp.kindCode =='VS' || prpLLossProp.kindCode =='DS' || prpLLossProp.kindCode =='DC' }">
				<input type="text" class="input-text" name="otherLoss[${otherSize }].itemName"  value="${prpLLossProp.itemName }"  datatype="*" ignore="ignore"/>
			</c:if>
		</td>
		
		<c:choose>
		<c:when test="${prpLLossProp.kindCode =='T' || prpLLossProp.kindCode =='C' ||prpLLossProp.kindCode =='RF'}">
			<td>
				<input type="text" class="input-text" name="otherLoss[${otherSize}].lossQuantity" onblur="calTSFee(this)" value="${prpLLossProp.lossQuantity }" 
						datatype="n1-16"  nullmsg="数量/天数不能为空"/>
				<input type="hidden" class="input-text" name="otherLoss[${otherSize}].unitPrice" value="${prpLLossProp.unitPrice }" />
			</td>
			<td>
				<input type="text" class="input-text" name="otherLoss[${otherSize}].sumLoss" datatype="amount" value="${prpLLossProp.sumLoss }"
					ignore="ignore" datatype="n" />
			</td>
		</c:when>
		<c:otherwise>
			<td>
					<c:if test="${prpLLossProp.kindCode != 'DP' && prpLLossProp.kindCode != 'BP' && prpLLossProp.kindCode != 'D11P' && prpLLossProp.kindCode != 'D12P'}">
			  			<input type="text" class="input-text" name="otherLoss[${otherSize}].lossQuantity" value="${prpLLossProp.lossQuantity }" 
						ignore="ignore" datatype="n1-16" />
					</c:if>
					<c:if test="${prpLLossProp.kindCode == 'DP' || prpLLossProp.kindCode == 'BP' || prpLLossProp.kindCode == 'D11P' || prpLLossProp.kindCode == 'D12P'}">
						<input type="text" class="input-text" name="otherLoss[${otherSize}].lossQuantity" value="${prpLLossProp.lossQuantity }" 
						ignore="ignore" datatype="n1-16" readonly/>
					</c:if>
			</td>
			<td>
				<c:if test="${prpLLossProp.kindCode != 'DP' && prpLLossProp.kindCode != 'BP' && prpLLossProp.kindCode != 'D11P' && prpLLossProp.kindCode != 'D12P'}">
				  <input type="text" class="input-text" name="otherLoss[${otherSize}].sumLoss" datatype="amount" value="${prpLLossProp.sumLoss }"
					datatype="n" />
				</c:if>
				<c:if test="${prpLLossProp.kindCode == 'DP' || prpLLossProp.kindCode == 'BP' || prpLLossProp.kindCode == 'D11P' || prpLLossProp.kindCode == 'D12P'}">
					<input type="text" class="input-text" name="otherLoss[${otherSize}].sumLoss" datatype="amount" value="${prpLLossProp.sumLoss }" onchange="validValue(this)" id = "sumLossChange"
					datatype="n" />
					<input type="hidden" name="originLossMaxFee[${otherSize}]" value="${prpLLossProp.originLossMaxFee }"/>
				</c:if>
		</td>
			
		</c:otherwise>
		</c:choose>

			<input type="hidden" class="input-text" name="otherLoss[${otherSize }].otherDeductAmt" datatype="amount" 
					value="${empty prpLLossProp.otherDeductAmt?0.00:prpLLossProp.otherDeductAmt }"/>

		<td>
			<c:if test="${prpLLossProp.kindCode != 'DP' && prpLLossProp.kindCode != 'BP' && prpLLossProp.kindCode != 'D11P' && prpLLossProp.kindCode != 'D12P' &&
					prpLLossProp.kindCode !='RS' && prpLLossProp.kindCode !='VS' && prpLLossProp.kindCode !='DS' && prpLLossProp.kindCode !='DC'}">
			  	<input type="text" class="input-text" name="otherLoss[${otherSize }].sumRealPay" datatype="amount" readonly value="${prpLLossProp.sumRealPay }" />
			</c:if>
			<c:if test="${prpLLossProp.kindCode == 'DP' || prpLLossProp.kindCode == 'BP' || prpLLossProp.kindCode == 'D11P' || prpLLossProp.kindCode == 'D12P' ||
					prpLLossProp.kindCode =='RS' || prpLLossProp.kindCode =='VS' || prpLLossProp.kindCode =='DS' || prpLLossProp.kindCode =='DC'}">
				<input type="text" class="input-text" name="otherLoss[${otherSize }].sumRealPay" datatype="*" ignore="ignore" readonly value="${prpLLossProp.sumRealPay }" />
			</c:if>
			<input type="hidden" name="prpLLoss[${otherSize }].deductOffAmt" value="${prpLLossProp.deductOffAmt }" /><!-- 不计免赔金额 -->
		</td>
		<td>
			<c:if test="${prpLLossProp.kindCode != 'DP' && prpLLossProp.kindCode != 'BP' && prpLLossProp.kindCode != 'D11P' && prpLLossProp.kindCode != 'D12P'}">
			  <input type="text" class="input-text" name="otherLoss[${otherSize }].dutyRate" datatype="*"
								nullmsg="请填写责任比例！" value="${prpLLossProp.dutyRate }" />
			</c:if>
			<c:if test="${prpLLossProp.kindCode == 'DP' || prpLLossProp.kindCode == 'BP' || prpLLossProp.kindCode == 'D11P' || prpLLossProp.kindCode == 'D12P'}">
				<input type="text" class="input-text" name="otherLoss[${otherSize }].dutyRate" readonly value="${prpLLossProp.dutyRate }" />
			</c:if>
		</td>
		<td>
			<input type="text" class="input-text" name="otherLoss[${otherSize }].deductDutyRate" readonly value="${prpLLossProp.deductDutyRate }" />
		</td>
		<td>
			<input type="text" class="input-text" name="otherLoss[${otherSize }].deductAbsRate" readonly value="${prpLLossProp.deductAbsRate }"/>
		</td>
		<td>
			<input type="text" class="input-text" name="otherLoss[${otherSize }].deductAddRate" readonly value="${prpLLossProp.deductAddRate }"/>
		</td>
		
	</tr>
</c:forEach>