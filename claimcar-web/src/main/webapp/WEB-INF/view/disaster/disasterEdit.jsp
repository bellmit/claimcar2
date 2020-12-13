<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>立案-巨灾补录</title>

</head>
<body>
	<div class="page_wrap">
		<div class="table_wrap">
		    <div class="table_title f14">查询立案巨灾信息</div>
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="checkform" name="checkform" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-5 text-c">
							 	立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" errormsg="请输入4到22位数"  name="claimNo" value="" />
							    <font class="must">*</font>
							</div>
						</div>
						<div class="line"></div>
						<br><br>
						<div class="row text-c">
							<span>
								<input class="btn btn-primary btn-outline btn-search"  onclick="searchDisaster();" type="button" value="下一步"/>
							</span><br />
						</div>
					</form>
				</div>
			</div>
         </div>

        <div class="table_wrap" id="disaster" style="display:none">
		    <div class="table_title f14">巨灾</div>
		         <div class="table_cont ">
		         <form id="form" name="form" class="form-horizontal" role="form" method="post">
			          <div class="formtable tableoverlable">
						<input type="hidden" name="disasterVo.registNo" id="registNo">
						<input type="hidden" name="disasterVo.policyNo" id="policyNo">
						<input type="hidden" name="disasterVo.id" id="disasterId">
						<div class="row cl">
							<label class="form_label col-3">立案号</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"  name="disasterVo.claimNo" id="claimNo" readOnly/>
							</div>
							<label class="form_label col-1">出险时间</label>
							<div class="form_input col-3">
								<input type="text" class="input-text"  name="damageTime" id ="damageTime" readOnly/>
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-3">巨灾一级代码</label>
							<div class="form_input col-3">
								<span class=select-box> <app:codeSelect
										codeType="Disasterone" type="select" onchange="disaster_ones()"
										lableType="code-name" id="disaster_one"
										name="disasterVo.disasterCodeOne"/>
								</span>
							</div>
							<label class="form_label col-1">巨灾名称</label>
							<div class="form_input col-3">
								<input class="input-text" id="disaster_oneName" datetype="*"  readOnly
									name="disasterVo.disasterNameOne"/>
							</div>
						</div>
						<%-- <c:set var="disaster_el" value="${008,其他}"></c:set> --%>
						<div class="row  cl">
							<label class="form_label col-3">巨灾二级代码</label>
							<div class="form_input col-3">
								<span class=select-box>
									<select class="select" onchange="disaster_twos()" id="disaster_two" name="disasterVo.disasterCodeTwo">
									</select>
									<input type="hidden" name="lastSelectCode" value="${disasterVo.disasterCodeTwo}">
								</span>
							</div>
							<label class="form_label col-1">巨灾名称</label>
							<div class="form_input col-3">
								<input class="input-text" id="disaster_twoName" readOnly
									name="disasterVo.disasterNameTwo" >
						</div>
					</div>
				</div>
				</form>
				<br><br>
				<div class="btn-footer clearfix text-c">
				<input class="btn btn-primary btn-kk" type="button" id="save" onclick="save()" value="保存">
			</div>
			</div>
			
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		disTwChange();
	});
	
	function searchDisaster(){
		var rules = {};
		var ajaxEdit = new AjaxEdit($('#checkform'));
		ajaxEdit.beforeCheck = function() {// 校验之前
		};
		ajaxEdit.beforeSubmit = function() {// 提交前补充操作
		};
		ajaxEdit.targetUrl = "/claimcar/disaster/searchDisaster.do";
		ajaxEdit.rules = rules;
		ajaxEdit.afterSuccess = function(data) {// 操作成功后操作
			var status = data.data.status;
		    var resultData = data.data.data;
		    if(status == 'success'){
		    	$("#disaster").show();
		    	$("#claimNo").val(resultData.claimNo);
		    	$("#registNo").val(resultData.registNo);
		    	$("#policyNo").val(resultData.policyNo);
		    	$("#disasterId").val(resultData.disasterId);
		    	var damageTime = new Date(resultData.damageTime);
		    	$("#damageTime").val(damageTime.format('yyyy-MM-dd hh:mm:ss'));
		    }else if(status == 'fail'){
		    	layer.alert(resultData);
		    }
		};
		// 绑定表单
		ajaxEdit.bindForm();
		$("#checkform").submit();
	}
	
	function save(){
		var rules = {};
		var ajaxEdit = new AjaxEdit($('#form'));
		ajaxEdit.beforeCheck = function() {// 校验之前
		};
		ajaxEdit.beforeSubmit = function() {// 提交前补充操作
		};
		ajaxEdit.targetUrl = "/claimcar/disaster/saveDisaster.do";
		ajaxEdit.rules = rules;
		ajaxEdit.afterSuccess = function(data) {// 操作成功后操作
			layer.alert("保存成功",{closeBtn:0},function(){
			    	location.reload();
			});
		};
		// 绑定表单
		ajaxEdit.bindForm();
		$("#form").submit();
	}
	
	//巨灾
	function disaster_ones(){
		var disaster=$("#disaster_one").val();
		var name =$("#disaster_one").find("option[value='"+disaster+"']").text();
		$("#disaster_oneName").val(name.split("-")[1]);
		var disasterOneCode = name.split("-")[0];
		clearDisasterTwo();
		initDisasterTwoInfo(disasterOneCode);
	}
	function disaster_twos(){
		var disaster=$("#disaster_two").find("option:selected").text();
		$("#disaster_twoName").val(disaster.split("-")[1]);
	}
	function disTwChange(){
		var dis_two=$("#disaster_two");
		dis_two.find("option[value='002']").attr("disabled","disabled");
		dis_two.find("option[value='004']").attr("disabled","disabled");
		dis_two.find("option[value='006']").attr("disabled","disabled");
		dis_two.find("option[value='007']").attr("disabled","disabled");
	}

	/**
	 * 当巨灾一级代码发生变化时，将已选择的巨灾二级代码信息清空
	 */
	function clearDisasterTwo() {
		$("#disaster_two").val("");
		$("#disaster_two").text("");
		$("#disaster_twoName").val("");
	}

	function initDisasterTwoInfo(disasterOneCode) {
		$("#disaster_two").empty();
		$("#disaster_two").append("<option value=''></option>");
		$.ajax({
			url : "/claimcar/regist/getDisasterTwoInfo.ajax?disasterOneCode=" + disasterOneCode, // 从再保获取巨灾二级代码
			type : 'post', // 数据发送方式
			success : function(ajaxResult) {// 回调方法，可单独定义
				if (ajaxResult.status == 200 && ajaxResult.data != null) {
					for (var i = 0; i < ajaxResult.data.length; i++) {
						var codeinfo = ajaxResult.data[i];
						var codeArr = codeinfo.split("-");
						$("#disaster_two").append("<option value='"+codeArr[0]+"'>"+codeinfo+"</option>");
					}
				}
			}
		});
	}

	</script>
</body>
</html>
