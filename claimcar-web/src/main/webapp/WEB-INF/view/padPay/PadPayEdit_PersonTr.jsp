<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<c:forEach var="padPayPersonVo" items="${padPayPersonVos}" varStatus="status">
<input type="hidden" value="${status.index + size}">
<c:set var="pe_Idx" value="${status.index + size}" />
<%-- 第${status.index + size + 1}个 --%>
<div class="table_cont table_list" id="ttttt_${pe_Idx}">
	<input type="hidden" name="padPayPersonVo[${pe_Idx}].id" value="${padPayPersonVo.id}">
	<input type="hidden" name="padPayPersonVo[${pe_Idx}].riskCode" value="${claimVo.riskCode}">
	
 
	<table border="1" class="table table-border" id="padPay_${pe_Idx}">
		<thead class="text-c">
			<tr class="text-c">
				<th style="width: 8%">姓名</th>
				<th style="width: 8%">人伤类型</th>
				<th style="width: 8%">伤情类型</th>
				<th style="width: 6%">年龄</th>
				<th style="width: 6%">性别</th>
				<th style="width: 10%">单位/住址</th>
				<th style="width: 10%">车牌号</th>
				<th colspan="5">医疗机构</th>
			
				<!-- <th style="width: 10%">身份证号</th> -->
			</tr>
		</thead>
		<tbody class="text-c" id="padPayTbOne_${pe_Idx}">
			<tr>
				<%-- <td id="tttt_${pe_Idx}">
		<c:choose>
		<c:when test="${handlerStatus !='3'}">
		   <button type="button" class="btn  btn-primary mt-5"  onclick="CustompadpayOpen('Y',this)">反洗钱信息补录</button>
		  </c:when>
		<c:otherwise>
			<button  type="button" class="btn  btn-disabled">反洗钱信息补录</button>
		</c:otherwise>
	    </c:choose>
		</td> --%>
				<td><span class="select-box" style="width: 90%"> <app:codeSelect
							codeType="" dataSource="${perNameMap}" onchange="changePerName(this,'${pe_Idx}')"
							type="select" lableType="name" name="padPayPersonVo[${pe_Idx}].personName"
							 value="${padPayPersonVo.personName}" clazz="must"/>
				</span><font class="must">*</font></td>

				<td>三者人员伤亡<font class="must">*</font></td>
				<td><app:codeSelect codeType="PersonPayType" type="select"
						clazz="must" name="padPayPersonVo[${pe_Idx}].injuryType"
						value="${padPayPersonVo.injuryType}" /></td>

				<td><input type="text" class="input-text" name="padPayPersonVo[${pe_Idx}].personAge" datatype="age" errormsg="请输入1-120岁！" value="${padPayPersonVo.personAge}" />
				  
					</td>

				<td><span class="select-box" style="width: 90%"> <app:codeSelect
							codeType="SexCode" type="select"
							name="padPayPersonVo[${pe_Idx}].personSex" clazz="must"
							value="${padPayPersonVo.personSex}" />
				</span><font class="must">*</font></td>

				<td><input type="text" class="input-text"
					name="padPayPersonVo[${pe_Idx}].personAddress" datatype="*0-50"
					value="${padPayPersonVo.personAddress}" /></td>

				<td><app:codeSelect codeType="" type="select" clazz="must"
						dataSource="${licenseNoMap}" lableType="name"
						name="padPayPersonVo[${pe_Idx}].licenseNo"
						value="${padPayPersonVo.licenseNo}"/></td>
				<td colspan="5">
					<app:areaSelect targetElmId="hospitalArea_${pe_Idx}"
						areaCode="${padPayPersonVo.hospitalCity}"
						hospitalCode="${padPayPersonVo.hospitalCode}" showLevel="3"
						isHospital="Y" style="width:100px" />
				<input type="hidden" id="hospitalArea_${pe_Idx}" name="padPayPersonVo[${pe_Idx}].hospitalCity"
					value="${padPayPersonVo.hospitalCity}" />
				<input type="hidden" id="hospitalArea_${pe_Idx}_hospitalCode" name="padPayPersonVo[${pe_Idx}].hospitalCode" value="${padPayPersonVo.hospitalCode}" /></td>

				<%-- <td><input type="text" class="input-text" name="padPayPersonVo[${pe_Idx}].personIdfNo"
						value="${padPayPersonVo.personIdfNo}"/></td> --%>
			</tr>
		</tbody>
		<thead class="text-c">
			<tr>
				<th style="width:3%">删除</th>
				<th style="width: 8%">费用名称</th>
				<th style="width: 10%">险别</th>
				<th style="width: 8%">垫付抢救费用</th>
				<th style="width: 10%">身份证号</th>
				<th style="width: 6%">例外标志</th>
				<th style="width: 9%">例外原因</th>
				<th style="width: 10%">收款人</th>
				<th style="width: 10%">收款方帐号</th>
				<th>开户银行</th>
				<th>税收信息</th>
				<th>摘要</th>
			</tr>
		</thead>
		<tbody class="text-c" id="padPayTbTwe_${pe_Idx}">
			<tr id="payTr_${pe_Idx}">
				<td>
					<button type="button"
						class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao "
						name="padPayPer_${pe_Idx}" onclick="delPayInfo2(this,'${pe_Idx}')"></button>
				</td>
				<td>
					<app:codeSelect codeType="FeeType" type="select"
						title="true"	 value="${padPayPersonVo.feeNameCode}"
							name="padPayPersonVo[${pe_Idx}].feeNameCode" />
				</td>
				<td>交通强制责任险</td>

				<td><input type="text" style="width: 85%" class="input-text"
					name="padPayPersonVo[${pe_Idx}].costSum" onchange="sumCost(this)"
					value="${padPayPersonVo.costSum}" datatype="sum"
					errormsg="请输入正确的金额，只能精确到小数后两位！" /> <font class="must">*</font>
				</td>
				
				<td><input type="text" class="input-text" datatype="*0-0|idcard"
					name="padPayPersonVo[${pe_Idx}].personIdfNo"
					value="${padPayPersonVo.personIdfNo}"/></td>
                     <!-- ////// -->
				<td><app:codeSelect codeType="IsValid" type="radio"
						 name="padPayPersonVo[${pe_Idx}].otherFlag" nullToVal="1"
						value="${padPayPersonVo.otherFlag}" onchange="otherFlagCon()"/>
				
				
				<td><span class="select-box" style="width: 90%">
					<app:codeSelect codeType="OtherCase"
						title="true" type="select" name="padPayPersonVo[${pe_Idx}].otherCause"
						value="${padPayPersonVo.otherCause}"  />
					</span></td>
				</td>
				
				

				<!-- onchange="changeCustom(this,'${pe_Idx}')" -->
				
				<td><%-- <app:codeSelect codeType="" type="select" clazz="must"
						dataSource="${customMap}" 
						onclick="showPayCust(this)"
						name="padPayPersonVo[${pe_Idx}].payeeName" 
						value="${padPayPersonVo.payeeName}" /> --%>
				<input type="text" class="input-text" 
				name="padPayPersonVo[${pe_Idx}].payeeName" value="${padPayPersonVo.payeeName}"
				 onclick="showPayCust(this)" readonly="readonly" clazz="must"/>
				 <input type="hidden" name="padPayPersonVo[${pe_Idx}].payeeId" value="${padPayPersonVo.payeeId}">	
					
				
				<input type="hidden" name="padPayPersonVo[${pe_Idx}].payObjectKind" value="${padPayPersonVo.payObjectKind}"/>
				
				</td>
						
				<td><input type="text" class="input-text"
					name="padPayPersonVo[${pe_Idx}].accountNo"
					value="${padPayPersonVo.accountNo}" style="width: 90%"
					readonly="readonly" /> <font class="must">*</font></td>

				<td>
					<input type="text" class="input-text" style="width: 90%"
					name="padPayPersonVo[${pe_Idx}].bankName" readonly="readonly"
					value="<app:codetrans codeType="BankCode" codeCode="${padPayPersonVo.bankName}"/>"/>
					<font class="must">*</font>
				</td>
				<td>
				    <input type="hidden"  name="padPayPersonVo[${pe_Idx}].addTaxRate" value="${padPayPersonVo.addTaxRate }" />
					<input type="hidden"  name="padPayPersonVo[${pe_Idx}].addTaxValue" value="${padPayPersonVo.addTaxValue }" />
					<input type="hidden"  name="padPayPersonVo[${pe_Idx}].noTaxValue" value="${padPayPersonVo.noTaxValue}" />
					<input type="button" class="btn rateBtn" name="[${pe_Idx}]_showButton" onclick="showTaxInfos('${pe_Idx}','${padPayPersonVo.addTaxRate }','${padPayPersonVo.addTaxValue }','${padPayPersonVo.noTaxValue}')" value="..." />
				</td>
				<td><input type="text" class="input-text"
					name="padPayPersonVo[${pe_Idx}].summary" datatype="*1-1000"
					value="${padPayPersonVo.summary}" onchange="checkSpecialCharactor(this,'${padPayPersonVo.summary}')" />
				</td>
			</tr>
		</tbody>
	</table>
</div>
</c:forEach>