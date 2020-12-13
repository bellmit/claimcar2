<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
 <style>
	.row.cl label.check-box {
		display:block;
	}
	</style>
<div class="table_title f14">反洗钱可疑交易特征</div>
<div class="table_cont  md-100">
	<div class="formtable">
		<div class="row cl">
 			<label class="form_label col-2">是否可疑交易</label>
 			<div class="form_input col-3 clearfix">
				  <label class="radio-box">
				 <input  type="hidden" id="prplcomContextVoflag" value="${prplcomContextVo.flag}"/>
					<input type="radio" name="person" id="isFlagY" class="SunSiRadio mr-5" <c:if test="${prplcomContextVo.flag eq '1'}">checked="checked"</c:if> value="1" onclick="lossOrNot(1,'LS')" /><font color="black">是</font>
				  </label>
				  <label class="radio-box">
					<input type="radio" name="person" id="isFlagN" class="SunSiRadio mr-5" <c:if test="${prplcomContextVo.flag != '1'}">checked="checked"</c:if> value="0" onclick="lossOrNot(0,'LS')" /><font color="black">否</font>
				  </label>
				<input id="ContextVoFlag" name="prplcomContextVo.flag" type="hidden" value="${prplcomContextVo.flag}" />
			</div>
		</div>

		<div class=" hide" id="LSTable">
			<div class="row cl" id="">
				<label class="form_label col-2">可疑交易特征</label>
				<div class="form_input col-8" style="height:100%;">
				<c:if test="${prpLWfTaskVo.handlerStatus eq '3' }">
					<app:codeSelect codeType="ISType" name="prplcomContextVo.flagContext" 
					value="${prplcomContextVo.flagContext}" type="checkbox" datatype="*" id="flagContext"/>
				</c:if>
				<c:if test="${prpLWfTaskVo.handlerStatus ne '3' }">
					<app:codeSelect codeType="ISType1" name="prplcomContextVo.flagContext" 
					value="${prplcomContextVo.flagContext}" type="checkbox" datatype="*" id="flagContext"/>
				</c:if>
				
				</div>
		    </div> 
		   <div class="formtable">
		   		<label class="form_label col-2">原因</label>
				<div class=" col-8">
					<textarea class="textarea" name="prplcomContextVo.causes" datatype="*1-200" 
					errormsg="请输入200以内的中文字符" id="causes">${prplcomContextVo.causes}</textarea>
				</div>
			</div>	
		</div>	
	</div>
</div>

