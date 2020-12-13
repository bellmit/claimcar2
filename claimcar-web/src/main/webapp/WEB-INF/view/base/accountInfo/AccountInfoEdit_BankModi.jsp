<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>



<!-- 银行信息  开始 -->
<div class="table_wrap">
	<div class="formtable">
		<div class="formtable mt-10">
			<div class="row cl">
				<input type="hidden" name=" " value=" "/>
				<label class="form_label col-3">收款方户名：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text"
						name=" " value=" "  datatype="*0-100" />
				</div>
				<span class="c-red col-1">*</span> 
				<label class="form_label col-2">收款方账号：</label>
				<div class="form_input col-2 ml-5">
					<input type="text" class="input-text"
						name=" " value=" " datatype="*0-100" />
				</div>
				<span class="c-red col-1">*</span>
			</div>

		</div>
		<p>
		<div class="line"></div>
		<p>
		<div class="formtable mt-5">
			<div class="row cl">
				<label class="form_label col-3">收款方开户行省/市：</label>
				<div class="form_label col-6" style="text-align: left">
					<app:areaSelect targetElmId="intermBankProvCity" showLevel="2" style="width:190px" />
					<input type="hidden" id="intermBankProvCity" name="areaSelectCode" />
					<span class="c-red">*</span>

				</div>
				
			</div>

			<div class="row cl">
				<label class="form_label col-3">收款方开户行：</label>
				<div class="form_input col-2">
					<span class="select-box" style="width: 100%"> 
					<app:codeSelect codeType="BankCode" name=" "
							id="BankName" type="select" clazz="must" value=" "/>
					</span>
				</div>

				<div class="form_input col-2">
					<input type="text" class="input-text" id="BankQueryText" />
				</div>
				<span class="c-red col-1">*</span>
				<div class="form_input col-1">
					<a class="btn btn-zd mr-10 fl"
						onClick="layerShowBankNum('检索行号','BankNumQ')">检索行号</a>
				</div>
			</div>
		</div>
		<p>
		<div class="line"></div>
		<p>
		<div class="formtable">
				
			<div class="row cl">
				<label class="form_label col-3">收款方银行行号：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text"
						name=" " id="BankNumber" datatype="*0-100"
						readonly value=" "/>
				</div>
				<span class="c-red col-1">*</span>
				<label class="form_label col-2">收款人银行类型：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" name=" "
						id="BankOutlets" readonly value=" " />
				</div>
				<span class="c-red col-1"></span> 
				
			</div>
			<div class="row cl mt-5 ml-5">
				<label class="form_label col-3">转账汇款模式：</label>
				<app:codeSelect codeType="PriorityFlag" type="radio" nullToVal="N" name=" " value=" "/>
				<font class="must">*</font>
			</div>
			<div class="row cl">
				<label class="form_label col-3">收款方手机号码：</label>
				<div class="form_input col-2">
					<input type="text" class="input-text" name=" " value=" " datatype="*0-100" />
				</div>
				<span class="c-red col-1">*</span>
			</div>
			<div class="row cl">
			<label class="form_label col-3">用途：</label>
			<div class="form_input col-5">
				<input type="text" class="input-text" name=" " value=" " datatype="*0-100" />
			</div>
		</div>
		</div>

		<div class="btn-footer clearfix text-c">
			<a class="btn btn-primary " id="saveBtn" onclick="layerHiddenBank()">确定</a> 
			<a class="btn btn-primary ml-5" id="cancelBtn" onclick="layerCancelBank()">取消</a>
		</div>

	</div>

</div>
<!-- 银行信息  结束 -->







