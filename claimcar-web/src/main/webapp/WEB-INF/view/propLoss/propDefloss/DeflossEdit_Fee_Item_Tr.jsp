<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
		<!--第一次初始化为空  -->	

<c:forEach var="prpLDlossCharge" items="${prpLDlossChargeVos}" varStatus="status">
												<tr class="text-c">
														<td width="6%"><input class="btn btn-default radius" onclick="delSubRisk(this)" name="prpLDlossChargeVo_${status.index + size }"  type="button" value="-"/></td>
														<td style="width: 11.111%;">
																<input type="hidden" name="prpLDlossChargeVos[${status.index + size }].id" value="${prpLDlossCharge.id }"/>
																<input type="hidden" id=feeRegistNo name="prpLDlossChargeVos[${status.index + size }].registNo" value="${prpLDlossCharge.registNo }"/>
					 											<input type="text" class="input-text" name="prpLDlossChargeVos[${status.index + size  }].lossItemName"  value="${prpLdlossPropFee.lossItemName }"/>
					 									</td>
					 									<td style="width: 13.111%;">
												 					<span class="select-box">
												 						<app:codeSelect codeType="LossSpeciesCode" type="select" 
												 						name="prpLDlossChargeVos[${status.index + size  }].lossSpeciesCode" 
												 						value="${prpLdlossPropFee.lossSpeciesCode }"/>  
																	</span>
					 									</td>
					 									<td style="width: 13.111%;">
											 					 <span class="select-box">
															      <app:codeSelect codeType="FeeTypeCode" type="select" 
															      name="prpLDlossChargeVos[${status.index + size  }].feeTypeCode" 
															      value="${prpLdlossPropFee.feeTypeCode }"/>
																</span> 
											 			</td>
											 			<td style="width: 11.111%;">
											 					<input type="text" class="input-text" name="prpLDlossChargeVos[${status.index + size  }].lossQuantity" value="${prpLdlossPropFee.lossQuantity }"/>
											 			</td>
											 			<td style="width: 11.111%;">
											 					<input type="text" class="input-text" name="prpLDlossChargeVos[${status.index + size  }].unitPrice" value="${prpLdlossPropFee.unitPrice }"/>
											 			</td>
											 			<td style="width: 11.111%;">
											 					 <div class="radio-box">
											 							<app:codeSelect codeType="IsValid" type="radio" 
											 							name="prpLDlossChargeVos[${status.index + size  }].recycleFlag" 
											 							value="${prpLdlossPropFee.recycleFlag }"/> 
																 </div> 
											 			</td>
											 				<td style="width: 11.111%;">
											 					<input type="text" class="input-text" name="prpLDlossChargeVos[${status.index + size  }].recyclePrice" value="${prpLdlossPropFee.recyclePrice }"/>
											 			</td>
											 			<td style="width: 11.111%;">
											 					<input type="text" class="input-text" name="prpLDlossChargeVos[${status.index + size  }].sumDefloss" value="${prpLdlossPropFee.sumDefloss }"/>
											 			</td>
												</tr>
</c:forEach>
			
