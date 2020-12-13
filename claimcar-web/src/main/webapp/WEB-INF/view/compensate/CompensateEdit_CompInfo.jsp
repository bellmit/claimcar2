<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!-- 计算书基本信息 -->
<div class="table_wrap">
	<div class="table_title f14">计算书基本信息</div>
	<div class="table_cont">
		<div class="formtable">
			<!-- 隐藏域 -->
		<input type="hidden" id="oldClaim" value="${oldClaim }">
		<input type="hidden" name="flag" id="flag" value="${flag }">
		<input type="hidden" name="buJiState" id="buJiState" value="${buJiState }">
		<input type="hidden" name="flowTaskId" id="taskId" value="${flowTaskId }">
		<input type="hidden" name="handlerStatus" id="status" value="${prpLWfTaskVo.handlerStatus }">
		<input type="hidden" name="compWorkStatus" id="compWorkStatus" value="${prpLWfTaskVo.workStatus }">
		<input type="hidden"  name="prpLCompensate.makeCom" value="${prpLCompensate.makeCom}"/>
		<input type="hidden"  name="prpLCompensate.comCode" value="${prpLCompensate.comCode}"/>
		<input type="hidden"  name="prpLCompensate.riskCode"  id="riskCode" value="${prpLCompensate.riskCode}"/>
		<input type="hidden" name="prpLCompensate.policyNo" value="${prpLCompensate.policyNo}"/>
		<input type="hidden"  name="prpLCompensate.handler1Code" value="${prpLCompensate.handler1Code}"/>
		<input type="hidden"  name="prpLCompensate.currency" value="${prpLCompensate.currency}"/>
		<input type="hidden"  name="prplcomContextVo.id" value="${prplcomContextVo.id}"/>
		<input type="hidden"  name="prpLCompensate.deductType" value="${prpLCompensate.deductType }"/>
		<input type="hidden"  name="lawsuitFlagInitVal" value="${prpLCompensate.lawsuitFlag }"/>
		<input type="hidden"  name="prpLCompensate.registNo"  value="${prpLCompensate.registNo}" />
		<input type="hidden" id="quantity" value="${quantity}" />
		<input type="hidden" id="unitamount" value="${unitamount}" />
		<input type="hidden" id="cprcCase" value="${cprcCase}" />
		<input type="hidden" id="haveKindNT" value="${haveKindNT}" />
		<input type="hidden" name="dwFlag" id="dwFlag" value="${dwFlag}" />
		<input type="hidden" name="dwPersFlag" id="dwPersFlag" value="${dwPersFlag}" />
		<input type="hidden" id="qfLicenseMap" value="${qfLicenseMap}" />
		<input type="hidden" id="coinsFlag" value="${prpLCMain.coinsFlag}" />
		<input type="hidden" id="coinsSize" value="${coinsSize}" />
		<input type="hidden" id="payrefFlag" value="${payrefFlag}" />

			<c:if test="${flag eq '1' }">
			<input type="hidden" name="prpLCompensate.indemnityDutyRate" value="${prpLCompensate.indemnityDutyRate }" />
			<input type="hidden" name="prpLCompensate.indemnityDuty" value="${prpLCompensate.indemnityDuty }" />
			</c:if>
		<input type="hidden"  name="prpLCompensate.allLossFlag" value="${prpLCompensate.allLossFlag }"/>
		<input type="hidden"  name="prpLCompensate.compensateType" value="${prpLCompensate.compensateType }"/>
			<!-- 隐藏域 -->
			<div class="row cl">
				<label class="form_label col-2">计算书号</label>
				<div class="form_input col-2">
					<!-- 是否后台规则生成传入 -->
							<span id="compensateNo">${prpLCompensate.compensateNo }</span>
					        <input type="hidden"  name="prpLCompensate.compensateNo" value="${prpLCompensate.compensateNo }"/>
					        <input type="hidden"  name="prplcomContextVo.compensateNo" value="${prpLCompensate.compensateNo }"/>
				</div>
				<label class="form_label col-2">报案号</label>
				<div class="form_input col-2">
					<!-- 报案保单信息表 -->
					${prpLCompensate.registNo}

				</div>
				<label class="form_label col-2">立案号</label>
				<div class="form_input col-2">
							${prpLCompensate.claimNo}
							<input type="hidden"  name="prpLCompensate.claimNo" value="${prpLCompensate.claimNo}" />
				</div>
			</div>
			<div class="row cl">
				<label class="form_label col-2">计算书类型</label>
				<div class="form_input col-2">
					<c:if test="${flag eq '1' }">
							交强险计算书
						</c:if>
					<c:if test="${flag eq '2' }">
							商业险计算书
						</c:if>

				</div>
				<label class="form_label col-2">是否互碰自赔</label>
				<div class="form_input col-2">
					<!-- 案件类型1-正常案件，2-互碰自赔，3-代位求偿 -->
					<c:if test="${prpLCompensate.caseType eq '2' }">
								是
							</c:if>
					<c:if test="${prpLCompensate.caseType ne '2' }">
								否
							</c:if>
							<input type="hidden"  name="prpLCompensate.caseType" value="${prpLCompensate.caseType }" />
				</div>
				<label class="form_label col-2">是否代位求偿</label>
				<div class="form_input col-2">
					<c:if test="${prpLCompensate.caseType eq '3' }">
								是
							</c:if>
					<c:if test="${prpLCompensate.caseType ne '3' }">
								否
							</c:if>
				</div>
			</div>
			<div class="row cl">
				<label class="form_label col-2">是否发起追偿</label>
				<div class="form_input col-3">
							<app:codeSelect codeType="YN10" type="radio" name="prpLCompensate.recoveryFlag" value="${prpLCompensate.recoveryFlag }" nullToVal="0" />
					<c:if test="${(compWfFlag eq '1') || (compWfFlag eq '2') }">
								<input type="hidden" name="prpLCompensate.recoveryFlag" value="${prpLCompensate.recoveryFlag }"/>
					</c:if>
				</div>
				<c:if test="${flag eq '2' }">
					<label class="form_label col-1">车损险保额</label>
					<div class="form_input col-2">
						<!-- 理算主表没有这个字段  要增加-->
								<span>￥${prpLCompensate.carLossAmt }</span>
								<input type="hidden"  name="prpLCompensate.carLossAmt" value="${prpLCompensate.carLossAmt }" />
					</div>

					<label class="form_label col-2">是否自然灾害</label>
					<div class="form_input col-2">
								<app:codeSelect codeType="YN10" style="min-width:50px" type="radio" name="prpLCompensate.prpLCompensateExt.disastersFlag" value="${prpLCompensate.prpLCompensateExt.disastersFlag }" nullToVal="0"/>
						<c:if test="${(compWfFlag eq '1') || (compWfFlag eq '2') }">
									<input type="hidden" name="prpLCompensate.prpLCompensateExt.disastersFlag" value="${prpLCompensate.prpLCompensateExt.disastersFlag }"/>
						</c:if>
					</div>
				</c:if>
			</div>
			<div class="agreeControlDiv">
				<div class="row cl">
					<label class="form_label col-2">是否诉讼</label>
					<div class="form_input col-3">
								<app:codeSelect codeType="YN10" type="radio" name="prpLCompensate.lawsuitFlag" value="${prpLCompensate.lawsuitFlag }" nullToVal="0" onclick="checkAllowFlag()"/>
						<font class="must">*</font>
						<c:if test="${(compWfFlag eq '1') || (compWfFlag eq '2') }">
									<input type="hidden" name="prpLCompensate.lawsuitFlag" value="${prpLCompensate.lawsuitFlag }"/>
						</c:if>
					</div>
					<div id="agreeDiv" class="hide">
						<label class="form_label col-1">协议金额</label>
						<div class="form_input col-2">
									<input type="text" class="input-text" name="prpLCompensate.agreeAmt" value="${prpLCompensate.agreeAmt }" datatype="amount" />
						</div>
						<label class="form_label col-2">协议情况备注</label>
						<div class="form_input col-2">
									<input type="text" class="input-text" name="prpLCompensate.agreeDesc" value="${prpLCompensate.agreeDesc }" datatype="*0-20" errormsg="请输入0-20个字符" ignore="ignore"/>
						</div>
					</div>
				</div>

				<div class="row cl">
					<label class="form_label col-2">是否通融</label>
					<div class="form_input col-3">
								<app:codeSelect codeType="YN10" type="radio" name="prpLCompensate.allowFlag" value="${prpLCompensate.allowFlag }" nullToVal="0" onclick="checkAllowFlag()"/>
						<c:if test="${(compWfFlag eq '1') || (compWfFlag eq '2') }">
									<input type="hidden" name="prpLCompensate.allowFlag" value="${prpLCompensate.allowFlag }"/>
						</c:if>
					</div>
					<label class="form_label col-1">被保险人电话</label>
					<div class="form_input col-3">
							  <input type="text" class="input-text" name="prpLCompensate.insuredPhone" nullmsg="请录入被保险人电话" value="${prpLCompensate.insuredPhone }" datatype="mlandline" style="width:60%" maxlength="20"/>
					</div>
				</div>
			</div>
		     		<c:if test="${prpLCMain.coinsFlag  != '0' && not empty prpLCMain.coinsFlag}">
				<div class="row cl">
					<label class="form_label col-2">是否共保协议</label>
					<div class="form_input col-3">
								<app:codeSelect codeType="YN10" type="radio" name="prpLCompensate.coinsFlag" value="${prpLCompensate.coinsFlag }" nullToVal="0" />
					</div>
					<div id="coinsDiv" class="hide">
						<label class="form_label col-1">协议金额</label>
						<div class="form_input col-2">
									<input type="text" class="input-text" name="prpLCompensate.coinsAmt" value="${prpLCompensate.coinsAmt }" datatype="amount" />
						</div>
						<label class="form_label col-2">协议情况备注</label>
						<div class="form_input col-2">
									<input type="text" class="input-text" name="prpLCompensate.coinsDesc" value="${prpLCompensate.coinsDesc }" datatype="*0-20" errormsg="请输入0-20个字符" ignore="ignore"/>
						</div>
					</div>
				</div>
			</c:if>
			<div class="row cl">
				<label class="form_label col-2">是否欺诈</label>
				<div class="form_input col-3">
								<app:codeSelect codeType="IsValid" type="radio" name="isFraud" id="isFraud" value="${isFraud }" nullToVal="0" disabled="true"/>
				</div>
				<c:if test="${isFraud == 1 }">
					<label class="form_label col-1">欺诈标志</label>
					<div class="form_input col-2">
						<app:codetrans codeType="FraudLogo" codeCode="${fraudLogo }" />
						<input id="fraudLogo" type="hidden" value="${fraudLogo }" />
					</div>
				</c:if>
			</div>
			<div class="row cl">
				<label class="form_label col-2 ">是否小额人伤案件</label>
				<div class="form_input col-3 ">
			                 <app:codeSelect codeType="YN10" type="radio"  clazz="mr-5" nullToVal="0"  value="${isMinorInjuryCases}" disabled="true"/>
			                 <input type="hidden" id="isMinorInjuryCases" value="${isMinorInjuryCases}"/>
				</div>
			               <input type="hidden" id="compTypeX" value="${compType }"/>
			               <input type="hidden" id="isRefuse" value="${isRefuse }"/>
				<c:if test="${compType==1 }">
					<label class="form_label col-1">是否交强拒赔</label>
				</c:if>
				<c:if test="${compType==2 }">
					<label class="form_label col-1">是否商业拒赔</label>
				</c:if>
				<div class="form_input col-3">
					<span class="radio-box">
						<app:codeSelect codeType="IsValid" type="radio" value="${isRefuse}" name="isRefuse" nullToVal="0" disabled="true" />
					</span>
				</div>
			</div>

            <input type="hidden" value="${reOpenFlag}" id="reOpenFlag">
            <input type="hidden" value="${claimCompleteFlag}" id="claimCompleteFlag">
             <c:if test="${reOpenFlag=='0'}">
			<div class="row cl">
			  <label class="form_label col-2">人伤减损金额</label>
			  <div class="form_input col-2 ">
			  <input type="text" class="input-text" name="prpLCompensate.pisderoAmout" nullmsg="请录入被保险人电话" value="${prpLCompensate.pisderoAmout }"  maxlength="20" id="pisderoAmout" readonly="readonly" style="width:60%;"/>
			  </div>
			  <label class="form_label col-1 " style="text-align:left;">人伤内部减损</label>
			   <div class="form_input col-3 ">
			   <app:codeSelect codeType="YN10" type="radio"  clazz="mr-5" nullToVal="0" name="inSideDeroPersonFlags" disabled="true"  value="${prpLCompensate.inSideDeroPersonFlag}"/>
			   <input type="hidden" name="prpLCompensate.inSideDeroPersonFlag" value="${prpLCompensate.inSideDeroPersonFlag}" />
			  </div>
			  <!-- 减损类型 -->
			  	<label class="form_label col-2">减损类型:</label>
				<div class="form_input col-2">
				<input id="impairmentType" value="${prpLCompensate.impairmentType}" type="hidden">
					<select id="impairmenttypesid"
						name="prpLCompensate.impairmentType"
						style="width: 152px; height: 25px"
						value="${prpLCompensate.impairmentType}">
						<c:if
							test="${prpLCompensate.pisderoAmout>0||prpLCompensate.cisderoAmout>0}">
							<option value=''></option>
							<option value='2'>协谈减损</option>
							<option value='1'>拒赔减损</option>
						</c:if>
						<c:if
							test="${prpLCompensate.pisderoAmout==0&&prpLCompensate.cisderoAmout==0}">
							<option value=""></option>
						</c:if>
					</select>
				</div>
				<!-- 减损类型 -->
			</div>
			<div class="row cl">
			 <label class="form_label col-2 " >车物减损金额</label>
			  <div class="form_input col-2 ">
			  <input type="text" class="input-text" name="prpLCompensate.cisderoAmout"  value="${prpLCompensate.cisderoAmout}" datatype="isDeroAmoutCom"  errormsg="请输入非负整数或者保留两位小数" id="cisderoAmout" style="width:60%;" maxlength="20" oldValue="${prpLCompensate.cisderoAmout}"/>
			  </div>
			  
			  <label class="form_label col-1 " style="text-align:left;">车物内部减损</label>
			   <div class="form_input col-3 ">
			   <app:codeSelect codeType="YN10" type="radio"  clazz="mr-5" nullToVal="0" name="prpLCompensate.inSideDeroFlag" value="${prpLCompensate.inSideDeroFlag}"/>
			   <input type="hidden" name="prpLCompensate.inSideDeroFlagValue" value="" id="inSideDeroFlagValue"/>
			  </div>
			  <!-- 欺诈类型 -->
			  	<label class="form_label col-2">欺诈类型:</label>
				<div class="form_input col-2">
					<input id="fraudType" value="${prpLCompensate.fraudType}" type="hidden">
					<select id="fraudtypeid" name="prpLCompensate.fraudType"
						style="width: 152px; height: 25px"
						value="${prpLCompensate.fraudType}">
				   <c:choose>
			        <c:when test="${(prpLCompensate.pisderoAmout!=0 || prpLCompensate.cisderoAmout!=0) && prpLCompensate.impairmentType eq '1'}">
			                <option value=''></option>
							<option value='1'>故意虚构保险标的</option>
							<option value='2'>编造未曾发生的保险事故</option>
							<option value='3'>编造虚假的事故原因</option>
							<option value='4'>夸大损失程度</option>
							<option value='5'>故意造成保险事故</option>
			        </c:when>
			        <c:otherwise>
			          <option value='' selected="selected"></option>
			        </c:otherwise>
			        </c:choose>
						
					</select>
				</div>
				<!-- 欺诈类型 -->
			</div>
			</c:if>
			<div class="row cl"></div>
			<c:if test="${ isRefuse == 1}">
				<div class="row cl" id="refuseReason">
					<label class="form_label">拒赔原因</label>
				<textarea class="textarea" id="contexts"  name="refuseReason"  disabled="true">${refuseReason}</textarea>
				</div>
			</c:if>
		</div>
	</div>
</div>