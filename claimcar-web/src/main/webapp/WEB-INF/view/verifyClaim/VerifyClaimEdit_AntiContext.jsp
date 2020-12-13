<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<style>
	div#LSTable .row.cl label.check-box {
		display:block;
	}
</style>
<div class="table_title f14">反洗钱可疑交易特征</div>
<div class="table_cont">
	<div class="formtable">
	<div class="row cl">
	<label class="form_label col-2">理算意见:</label>
	</div>
		<div class="row cl">
 			<label class="form_label col-2">是否可疑交易</label>
 			<div class="form_input col-3 clearfix">
				  <label class="radio-box"><%--  --%>
				  <input id="prplcomContextVoflag" type="hidden" value="${prplcomContextCVo.flag}" />
					<input type="radio" name="person" id="isFlagY" class="SunSiRadio mr-5" <c:if test="${prplcomContextCVo.flag eq '1'}">checked="checked"</c:if> value="1" onclick="lossOrNot(1,'LS')" disabled="disabled"/><font color="black">是</font>
				  </label>
				  <label class="radio-box">
					<input type="radio" name="person" id="isFlagN" class="SunSiRadio mr-5" <c:if test="${prplcomContextCVo.flag != '1'}">checked="checked"</c:if> value="0" onclick="lossOrNot(0,'LS')" disabled="disabled"/><font color="black">否</font>
				  </label>
				
			</div>
		</div>

		<div class=" hide" id="LSTable">
			<div class="row cl" id="">
				<label class="form_label col-2">可疑交易特征</label>
				<div class="form_input col-8" style="height:100%;">
				<app:codeSelect codeType="ISType" value="${prplcomContextCVo.flagContext}" type="checkbox" disabled="true"/>
				
				
				</div>
		    </div> 
		    </p>
			
		   <div class="formtable">
		   		<label class="form_label col-2">原因</label>
				<div class=" col-8">
					<textarea class="textarea" disabled="true">${prplcomContextCVo.causes}</textarea>
				</div>
			</div>	
		</div>	
		</p>
	<div class="row cl">
	<label class="form_label col-2">核赔意见:</label>
	</div>
	<div class="row cl">
 			<label class="form_label col-2">是否可疑交易</label>
 			<div class="form_input col-3 clearfix">
				  <label class="radio-box">
				  <input id="prplcomContextVoHflag" type="hidden" value="${prplcomContextVo.flag}" />
					<input type="radio" name="HFLAG" id="isFlagY" class="SunSiRadio mr-5" <c:if test="${prplcomContextVo.flag eq '1'}">checked="checked"</c:if> value="1" onclick="lossHOrNot(1,'LSH')" /><font color="black">是</font>
				  </label>
				  <label class="radio-box">
					<input type="radio" name="HFLAG" id="isFlagN" class="SunSiRadio mr-5" <c:if test="${prplcomContextVo.flag != '1'}">checked="checked"</c:if> value="0" onclick="lossHOrNot(0,'LSH')" /><font color="black">否</font>
				  </label>
				<input id="ContextVoFlag" name="prplcomContextVo.flag" type="hidden" value="${prplcomContextVo.flag}" />
			</div>
		</div>
	
	<div class=" hide" id="LSHTable">
		   <div class="formtable">
		   		<label class="form_label col-2">原因</label>
				<div class=" col-8">
					<textarea class="textarea" name="prplcomContextVo.causes" datatype="*1-200" 
					errormsg="请输入200以内的中文字符" id="causes">${prplcomContextVo.causes}</textarea>
				</div>
			</div>	
		</div>	
		</p>
	</div>
</div>

