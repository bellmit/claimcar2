<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
		<!--第一次初始化为空  -->	
<c:forEach var="prpLdlossPropFee" items="${prpLdlossPropMainVo.prpLdlossPropFees }" varStatus="status">
	<tr class="text-c">
			<td width="6%">
				<button type="button" class="fl btn btn-minus Hui-iconfont Hui-iconfont-jianhao" 
					onclick="delSubRisk(this)" name="prpLdlossPropFeeVo_${status.index + size }" > </button>
			</td>
			<td style="width: 5.111%;">
					<input type="text" class="input-text" datatype="*" value="${prpLdlossPropFee.owner }"
						name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].owner" />
				</td>
			<td style="width: 11.111%;">
					<!-- 该字段用于判断该选项能否点击删除事件 -->
					<input type="hidden"  name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size }].validFlag " value="${prpLdlossPropFee.validFlag }"/>
					<input type="hidden" name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size }].id" value="${prpLdlossPropFee.id }"/>
					<input type="hidden" id=feeRegistNo name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size }].registNo" value="${prpLdlossPropFee.registNo }"/>
					<input type="hidden"  name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size }].riskCode " value="${prpLdlossPropFee.riskCode }"/>
					
					<input type="text" class="input-text" datatype="*" value="${prpLdlossPropFee.lossItemName }"
						name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].lossItemName" />
				</td>
				<td style="width: 8.111%;">
	 					<span class="select-box">
	 						<app:codeSelect  name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].lossSpeciesCode" 
	 							value="${prpLdlossPropFee.lossSpeciesCode }" codeType="LossSpeciesCode" type="select" />  
						</span>
				</td>
				<td style="width: 13.111%;">
 					 <span class="select-box">
				      <app:codeSelect  name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].feeTypeCode" 
				      	codeType="PropertyFeeType" type="select" value="${prpLdlossPropFee.feeTypeCode }" />
					</span> 
 			</td>
 			<td style="width: 11.111%;">
 			        
 					<input type="text" style="vertical-align:middle;width:60%;" class="input-text" datatype="n" name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].lossQuantity" 
 						value="${prpLdlossPropFee.lossQuantity }" onblur="calSumLossFee()" />
 					
 					<input type="text" style="vertical-align:middle;width:30%;" datatype="*0-2" ignore="ignore" class="input-text" 
 					    name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].unit" value="${prpLdlossPropFee.unit }" />
 			</td>
 			<td style="width: 11.111%;">
 					<input type="text" class="input-text" datatype="amount" name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].unitPrice" 
 						value="${prpLdlossPropFee.unitPrice }" onblur="calSumLossFee()"/>
 			</td>
 			<td style="width: 11.111%;" class="text-c">
 					 <c:if test="${flag == 't' }" >
 					   <div class="radio-box">
 							<input type="checkbox" name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].recycleFlag"
 										 value="${prpLdlossPropFee.recycleFlag}" onclick="recycleFlag(this)"/>
					   </div> 
					 </c:if>
					 <c:if test="${flag == 'f' }">
					     <div class="radio-box">
					         <input type="checkbox" name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].recycleFlag"
 										 value="${prpLdlossPropFee.recycleFlag}" disabled="disabled"/>
					     </div>
					 </c:if>
					 
 			</td>
 				<td style="width: 11.111%;">
 					<input type="text" class="input-text" datatype="amount" name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].recyclePrice" value="${prpLdlossPropFee.recyclePrice }"
 					  onblur="calSumLossFee()" onchange="defaultRecyclePrice()" />
 			</td>
 			<td style="width: 11.111%;">
 					<input type="text" class="input-text" name="prpLdlossPropMainVo.prpLdlossPropFees[${status.index + size  }].sumDefloss" value="${prpLdlossPropFee.sumDefloss }"  readonly="readonly"/>
 			</td>
	</tr>
</c:forEach>

			
