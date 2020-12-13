<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

	<form id="ReportTmpEditForm">
		<div class="table_wrap table_cont mt-30 md-30">

		<div class="f14 text-c">基本信息</div>
		<div class="line mt-10 mb-10"></div>
			<div class="formtable">
				<div class="row cl">
					<label class="form_label col-3">临时保单号：</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" readonly="readonly" placeholder="系统自动生成" />
					</div>
					<label class="form_label col-2">被保险人：</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" name="mainVo.insuredName" value="${iName}" id="iName"/>
					</div>
				 </div>
				 <input type="hidden" class="input-text" name="mainVo.comCode" readonly="readonly" value="990000"/>
				 <div class="row cl">
					<label class="form_label col-3">出险时间：</label>
					<div class="form_input col-2">
						<%-- <span class="select-box"> 
							<app:codeSelect codeType="ComCodeLv2" name="mainVo.comCode" type="select" clazz="must" />
						</span> --%>
						<input type="text" class="Wdate"  id="damageTime" name="mainVo.damageTime" 
							value="${damageTime }" style="width:99%"  pattern="yyyy-MM-dd HH:mm" datatype="*"
							onFocus="WdatePicker({maxDate: '#F{$dp.$D(\'nowDate\')}',dateFmt:'yyyy-MM-dd HH:mm' })" />
						<c:set var="nowDate">
							<fmt:formatDate value="${nowDate }" pattern="yyyy-MM-dd HH:mm" />
						</c:set>
						<input type="hidden" class="input-text" id="nowDate" value="${nowDate }"/>
					</div>

					<label class="form_label col-2">车牌号码：</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" name="itemCarVo.licenseNo" 
							  datatype="carLicenseNo" nullmsg="请填写车牌号码" value="${liNo}"/>
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-3">发动机号：</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" name="itemCarVo.engineNo" value="${param.enNo}"/>
					</div>
					<label class="form_label col-2">车架号：</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" name="itemCarVo.frameNo" value="${param.frNo}" onkeyup="toUpperCase(this)"/>
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-3">厂牌型号：</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" name="itemCarVo.brandName"/>
					</div>
					<label class="form_label col-2">号牌底色：</label>
					<div class="form_input col-2">
						<span class="select-box" > 
							<app:codeSelect id="licenseColor" codeType="LicenseColor" name="itemCarVo.licenseColor"  type="select" />
						</span>
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-3">起保日期：</label>
					<div class="form_input col-2">
						<input type="text" class="Wdate"  id="dgDateMin" name="mainVo.startTime" 
								onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})" style="width:97%" />
					</div>
					<label class="form_label col-2">终保日期：</label>
					<div class="form_input col-2">
						<input type="text" class="Wdate"  id="dgDateMin" name="mainVo.endTime" 
								onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})" style="width:97%" />
					</div>
					 
				</div>
				<div class="row cl">
				<%-- <label class="form_label col-3">车牌号码：</label>
					<div class="form_input col-2">
						<input type="text" class="input-text" name="itemCarVo.licenseNo" 
							datatype="/^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/" nullmsg="请填写车牌号码" value="${liNo}"/>
					</div> --%>
			     <label class="form_label col-3">险种名称：</label>
					<div class="form_input col-2">
						<input type="checkbox" name="mainVo.riskCode" id="RiskCode" value="1101"  checked="checked"/>交强险
						<input type="checkbox" name="mainVo.riskCode" id="RiskCode" value="1201" checked="checked"/>商业险
						<!-- <input type="hidden" name="mainVo.riskCode" value=" "/> 事件 if checked name="mainVo.riskCode" value= -->
					</div>
				</div>
			</div>
			<p>
			<div class="line mt-10 mb-10"></div>
			<div class="f14 text-c">标的</div>
			<div class="formtable">
				<div class="table_cont" id="CItemKind">
					<table class="table table-border table-hover">
						<thead class="text-c">
							<tr>
								<th width="10%">
									<button type="button"  class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="addCItemKind()"></button>
							    </th>
								<th>险别名称</th>
								<th>保额</th>
							</tr>
						</thead>
						<tbody id="CkindTbody" class="text-c">
						<input type="hidden" id="kindSize" value="${fn:length(prpLTmpCItemKindVos)}">
							<%@include file="ReportTmpEdit_CItemKind.jsp" %>
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="line mt-10 mb-10"></div>
		<!-- 	<div class="f14 text-c">备注</div>
			<div class="formtable">
				<textarea class="textarea" style="height: 65px" name="mainVo.remark" placeholder="请输入备注"></textarea>
			</div> -->
			
			<div class="btn-footer clearfix text-c">
				<a class="btn btn-primary ml-5" id="reportTmpSubmit">登记案件</a>
			</div>
		</div>
	</form>
	<script type="text/javascript">
	$("#reportTmpSubmit").click(function(){	//提交表单
		parent.$("#damageTime").val($("#damageTime").val());
		var iName = $("#iName").val();
		if(iName==null||iName==""){
			layer.alert("请输入被保险人");
			return;
		}
		//判断有没有选险种名称
		var riskCode = $("input [name$='riskCode']");
		var s = 0;
		for(var i=0; i<riskCode.length; i++){ 
			if(riskCode[i].checked){
				s+=1; 
			}
		} 
		if(s!=0){
			layer.alert("请选择险种名称");
			return;
		}
		$("#ReportTmpEditForm").submit();
		});
	function setDate(){
		var date = new Date();
		var d = formatDate (date,1);
		$("[name='mainVo.endTime']").val(d);
		var dd = formatDate (date,0);
		$("[name='mainVo.startTime']").val(dd);
	}
	function formatDate (strTime,SorE) {//日期格式化
	    var date = new Date(strTime);
		if(SorE=="1"){
			return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		}else{
			var y = date.getFullYear()-1;
			return y+"-"+(date.getMonth()+1)+"-"+date.getDate();
		}
	    
	}
 
	$(function (){
		setDate();

		var ajaxEdit = new AjaxEdit($('#ReportTmpEditForm'));
		ajaxEdit.targetUrl = "/claimcar/regist/saveReportTmpEdit.do"; 
		ajaxEdit.afterSuccess=function(result){
			if(result.status=="200"){
				parent.$('#registTmpNo').val(result.data);
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index);
			}
		}; 
		//绑定表单
		ajaxEdit.bindForm();
					
	});
		
		//新增财产损失项
		function addCItemKind() {
			//判断有没有选险种名称
			var s = 0;
			$('input[name="mainVo.riskCode"]:checked').each(function(){ 
					s+=1; 
				} );
			if(s==0){
				layer.alert("请选择险种名称");
				return;
			}
			var $tbody = $("#CkindTbody");
			var kindSize = $("#kindSize").val();
			var params = {
				"kindSize" : kindSize,
			};
			var url = "/claimcar/regist/addCItemKind.ajax";
			$.post(url, params, function(result) {
				$tbody.append(result);
				$("#kindSize").val(kindSize + 1);
			});
		}
		//删除财产损失项
		function delCItemKind(element) {
			var $parentTr = $(element).parent().parent();
			var kindSize = $("#kindSize").val();
			$("#kindSize").val(kindSize - 1);// 删除一条
			$parentTr.remove();
		}
		
	</script>
