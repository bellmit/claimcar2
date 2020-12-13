<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div class="table_wrap">
	<div class="table_title f14">险别赔付</div>
		<div class="formtable">
			<c:if test="${flag eq '1' }">
			<div class="row cl">
				<input type="button" class="btn btn-primary mb-10" id="manageCompBZ" value="理算计算">
			</div>
			</c:if>
			<c:if test="${flag eq '2' }">
				<div class="row cl">
					<input type="button" class="btn btn-primary mt-10" id="getBZAmt" value="获取交强赔付金额">
					（您可点此按钮获取损失项的交强已赔付金额，也可以手动输入）<br>
					<input type="button" class="btn btn-disabled mt-10" id="manageCompBI" value="生成计算书（商业险试算）">
					（理算计算前，请先获取免赔率）
				</div>
			</c:if>
			<div class="row cl">
				<div class="col-12"><!-- class="textarea"  style="width:100%;font-size: 14px;padding: 4px; box-sizing: border-box;" --> 
					<textarea  style="width:100%;font-size: 14px;overflow-y:auto;overpadding: 4px; box-sizing: border-box;" rows="10" name="prpLCompensate.lcText" >${prpLCompensate.lcText }</textarea>
				</div>
			</div>
	</div>
	<p>
	<div class="table_cont">
		<div class="formtable" id="allFee_table">
			<div class="row cl">
				<label class="form_label col-2">总赔款金额</label>
				<div class="form_input col-2">
					<span id="sumLossALL">${prpLCompensate.sumAmt }</span>        
					<input type="hidden"  name="prpLCompensate.sumAmt" value="${prpLCompensate.sumAmt }"/>
				</div>
				<c:if test="${flag eq '1' }">
				<label class="form_label col-2">预付/垫付赔款</label>
				<div class="form_input col-2">
					<span id="preSumALL">${prpLCompensate.sumPreAmt} </span>         
					<input type="hidden"  name="prpLCompensate.sumPreAmt" value="${prpLCompensate.sumPreAmt} "/>
				</div>
				</c:if>
				<c:if test="${flag eq '2' }">
				<label class="form_label col-2">预付赔款</label>
				<div class="form_input col-2">
					<span id="preSumALL">${prpLCompensate.sumPreAmt} </span>         
					<input type="hidden"  name="prpLCompensate.sumPreAmt" value="${prpLCompensate.sumPreAmt} "/>
				</div>
				</c:if>
				<label class="form_label col-2">本次赔款金额</label>
				<div class="form_input col-2">
					<span id="sumThisALL">${prpLCompensate.sumPaidAmt}</span>    
					<input type="hidden"  name="prpLCompensate.sumPaidAmt" value="${prpLCompensate.sumPaidAmt}"/>     
				</div>
				
				
			</div>
			<div class="row cl">
				<label class="form_label col-2">总费用金额</label>
				<div class="form_input col-2">
					<span id="feeALL">${prpLCompensate.sumFee }</span> 
					<input type="hidden"  name="prpLCompensate.sumFee" value="${prpLCompensate.sumFee }"/>     
				</div>
				<label class="form_label col-2">预付费用</label>
				<div class="form_input col-2">
					<span id="allPreAmtPerss">${prpLCompensate.sumPreFee }</span> 
					<input type="hidden"  name="prpLCompensate.sumPreFee" value="${prpLCompensate.sumPreFee }"/>     
				</div>
				<label class="form_label col-2">本次赔付费用</label>
				<div class="form_input col-2">
					<span id="feeALLtoallPreAmt">${prpLCompensate.sumPaidFee }</span> 
					<input type="hidden"  name="prpLCompensate.sumPaidFee" value="${prpLCompensate.sumPaidFee }"/>     
				</div>
				
			</div>
			<div class="row cl">
			<c:if test="${flag eq '2' }">
				<label class="form_label col-2">不计免赔金额</label>
				<div class="form_input col-2">
					<span id="deductOffALL"></span> 
					<!-- <input type="hidden"  name="deductOffAmt" />  -->   
					<input type="hidden"  name="deductOffALL" />    
				</div>
				</c:if>
				<c:if test="${flag eq '2' }">
				<label class="form_label col-2">扣交强总金额</label>
				<div class="form_input col-2">
					<span id="bzPaidALL">${prpLCompensate.sumBzPaid}</span> 
					<input type="hidden"  name="prpLCompensate.sumBzPaid" value="${prpLCompensate.sumBzPaid}"/>    
				</div>
				</c:if>
			<%-- 	<label class="form_label col-2">扣交强总金额</label>
				<div class="form_input col-2">
					<span id="bzPaidALL">${prpLCompensate.sumRealPay}</span>   
					<input type="hidden"  name="prpLCompensate.sumRealPay" id="compSum" value="${prpLCompensate.sumRealPay}"/>   
				</div> --%>
			</div>
		</div>
	</div>
</div>

<div class="table_wrap">
	<div class="table_title f14">理算意见</div>
		<div class="formtable">
			<div class="col-12">
				<textarea class="textarea" name="prpLCompensate.remark" datatype="*1-200" 
				errormsg="请输入200以内的中文字符" ignore="ignore">${prpLCompensate.remark }</textarea>
			</div>
	</div>
</div>