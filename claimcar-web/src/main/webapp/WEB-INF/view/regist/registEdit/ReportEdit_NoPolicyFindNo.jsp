<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>保单验证</title>
</head>
<body>
<div class="page_wrap">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post" action="#">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							
							<label for="policyNo" class="form_label col-1"><input type="radio" value="1" checked="checked" name="policyInfoVo.checkFlag" id="checkFlag" />保单号</label>
							<div class="form_input col-3"> 
								<input id="policyNo" type="text" class="input-text" name="policyInfoVo.policyNo"  clazz="must" onfocus="radioChecked(1)"/>
							</div>
							
							
							<label for="licenseNo" class="form_label col-1"><input type="radio" value="2" name="policyInfoVo.checkFlag" id="checkFlag" />车牌号</label>
							<div class="form_input col-3">
								<input id="licenseNo" type="text" class="input-text" name="policyInfoVo.licenseNo" onfocus="radioChecked(2)"/>
							</div>
							
						
							
							<label for="engineNo" class="form_label col-1"><input type="radio" value="4" name="policyInfoVo.checkFlag" id="checkFlag" />发动机号</label>
							<div class="form_input col-3">
								<input id="engineNo" type="text" class="input-text" name="policyInfoVo.engineNo" onfocus="radioChecked(4)"/>
							</div>
							
						</div>
						<div class="row mb-3 cl">
							
							<label for="vinNo" class="form_label col-1"><input type="radio" value="6" name="policyInfoVo.checkFlag" id="checkFlag" />VIN 码</label>
							<div class="form_input col-3">
								<input id="vinNo" type="text" class="input-text" name="policyInfoVo.vinNo" onfocus="radioChecked(6)"/>
							</div>
							
							<label for="frameNo" class="form_label col-1"><input type="radio" value="5" name="policyInfoVo.checkFlag" id="checkFlag" />车架号</label>
							<div class="form_input col-3">
								<input id="frameNo" type="text" class="input-text" name="policyInfoVo.frameNo" onfocus="radioChecked(5)"/>
							</div>
							
						<label for="insuredName" class="form_label col-1"><input type="radio" value="3" name="policyInfoVo.checkFlag" id="checkFlag" />被保险人</label>
							<div class="form_input col-3">
								<input id="insuredName" type="text" class="input-text" name="policyInfoVo.insuredName" onfocus="radioChecked(3)"/>
							</div>
							
						</div>
						<div class="row mb-3 cl">
							<div>
							<label for="dgDateMin" class="form_label col-1">出险时间</label>
							<div class="form_input col-3">
								<input disabled="disabled"  id="damageTime"  value="<fmt:formatDate value='${policyInfoVo.damageTime}'  pattern='yyyy-MM-dd HH:mm' />" 
								datatype="*" type="text" class="Wdate wd96" 
								onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd HH:mm'})" 
								/><font class="must">*</font>
								<input type="hidden" name="policyInfoVo.damageTime" value="<fmt:formatDate value='${policyInfoVo.damageTime}'  pattern='yyyy-MM-dd HH:mm' />" />
							</div>
							</div>
							<label for="licenseColor" class="form_label col-1">号牌底色</label>
							<div class="form_input col-3">
								<span class="select-box" > 
									<app:codeSelect id="licenseColor" codeType="LicenseColor" name="policyInfoVo.licenseColor"  type="select" />
								</span>
							</div>
							<div>
							<label for="insuredIdNo" class="form_label col-1"><input type="radio" value="7" name="policyInfoVo.checkFlag" id="checkFlag" />身份证号</label>
							<div class="form_input col-3">
								<input id="insuredIdNo" type="text" class="input-text" name="policyInfoVo.insuredIdNo" placeholder="被保险人身份证" onfocus="radioChecked(7)" datatype="idcard" ignore="ignore"/>
							</div>
							</div>
						</div>
						
						<div class="row mb-3 cl">
							<label class="form_label col-2"></label>
							<div class="form_input col-7">
							</div>
							<div class="form_input col-2 text-l">
								<label><input id="onlyValid" value="1" name="policyInfoVo.onlyValid" type="checkbox" />仅查询有效保单</label>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="submit" disabled>查询</button>
							</span>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--案查询条件 结束-->

		<!-- 隐藏域开始 -->
		<input id="registNo" type="hidden" value="${registNo}" />
		<!-- 隐藏域结束 -->
		
		<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th style="width: 4%" >选择</th>
									<th>客户等级</th>
									<th style="width: 17%">保单号</th>
									<th>车牌号</th>
									<th>承保机构</th>
									<th>被保险人</th>
									<th width="160">车型</th>
									<th>车架号/发动机号</th>
									<th>起保日期</th>
									<th>终保日期</th>
									<th>状态</th>
								</tr>
							</thead>
							<tbody class="text-c">
							</tbody>
						</table>
						<!--table   结束-->
						<!--无保单报案保存成功后生成的临时报案号获取 隐藏域-->
						<input type="hidden" id="registTmpNo"/>
						<div class="row text-c">
							<br/>
						</div>
						<div class="row text-c">
							<button class="btn btn-primary btn-confirm">选定</button>
							<button class="btn btn-primary" onclick="closeThis()" type="button">返回</button>
						</div>
					</div>
					<div class="tabCon">tabCon22</div>
				</div>
			</div>
		<!--标签页 结束-->
</div>
	<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
	<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
	<script type="text/javascript" src="/claimcar/lib/Validform/5.3.2/Validform.js"></script>
	<script type="text/javascript" src="/claimcar/lib/layer/v2.1/layer.js"></script>
	<script type="text/javascript" src="/claimcar/lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.js"></script> 
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.admin.js"></script>
	<script type="text/javascript" src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
	<script  type="text/javascript" src="/claimcar/lib/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/common.js"></script> 
	<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
	<script type="text/javascript" src="/claimcar/plugins/qtip/jquery.qtip.js"></script> 
	<script type="text/javascript" src="/claimcar/plugins/select2-3.4.4/select2.js"></script>
	<script type="text/javascript">
	var columns = [
		       		{
		       			"data" : null,
		       			"orderable" : false,
		       			"targets" : 0
		       		}, {
		       			"data" : null,
		       			"orderable" : false,
		       			"targets" : 0
		       		}, {
		       			"data" : "policyNo"
		       		}, {
		       			"data" : "licenseNo"
		       		}, {
		       			"data" : "comCodeName"
		       		}, {
		       			"data" : "insuredName"
		       		}, {
		       			"data" : "brandName",
		       			render : function(data, type, row) {
		    				return shortTxt(data,22);
		    			}
		       		}, {
		       			"data" : "frameNo"
		       		}, {
		       			"data" : "startDate",
		       			render : function(data, type, row) {
							return DateUtils.cutToDate(data);
						}
		       		}, {
		       			"data" : "endDate",
		       			render : function(data, type, row) {
							return DateUtils.cutToDate(data);
						}
		       		}, {
		       			"data" : "validFlag",
		       			render : function(data, type, row) {
		       				var  name="有效";
		       				if(data=='0')name="<span style='color:red'>无效</span>";
		       				if(data=='2')name="<span style='color:red'>未起保</span>";
		       				if(data=='3')name="<span style='color:red'>已退保</span>";
		       				if(data=='4')name="<span style='color:red'>已终保</span>";
		       				if(data=='5')name="<span style='color:red'>已停驶</span>";
		    				return name;
		    			}
		       		} ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		var goUrl = "/claimcar/policyView/policyView.do?policyNo="+ data.policyNo;
		if (data.validFlag == '0' || data.validFlag == '2' || data.validFlag == '3'|| data.validFlag == '4' || data.validFlag == '5') {
			$('td:eq(0)', row).html("<input disabled='disabled' name='checkCode' group='ids' risktype='" + data.riskType + "' onclick='relatePly(this)' type='checkbox' id='" + data.policyNo + "' value='" + data.relatedPolicyNo + "' licenseno='"+data.licenseNo+"' insuredname='"+data.insuredName+"' />");
		} else {
			$('td:eq(0)', row).html("<input name='checkCode' group='ids' risktype='" + data.riskType + "' onclick='relatePly(this)' type='checkbox' id='" + data.policyNo + "' value='" + data.relatedPolicyNo + "' licenseno='"+data.licenseNo+"' insuredname='"+data.insuredName+"' frameNo='"+data.frameNo+"' />");	
		}
		
		$('td:eq(1)', row).html("");
		$('td:eq(2)', row).html("<a href="+goUrl+"  target='_blank'>"+data.riskType+data.policyNo+"</a>");
		$('td:eq(7)', row).html(data.frameNo+"<br>"+data.engineNo);
		
  	}
	
	
	$(function(){
		
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
		
		bindValidForm($('#form'),searchTable);
		
		function searchTable () {
			if (checkBeforeQuery()) {
				layer.msg("需输入出险日期、号牌底色之外的其他查询条件进行查询。");
				return;
			}
			//出险时间
			var nowTime = Date.parse(new Date());
			var damageTime = Date.parse($("#damageTime").val().replace('-','/'));
			if (damageTime > nowTime) {
				layer.msg("出险时间不能大于当前时间");
				return false;
			}
			var ajaxList = new AjaxList("#resultDataTable");
			ajaxList.targetUrl = '/claimcar/policyQuery/search.do/';
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
		}
 
		
		$("button.btn-confirm").click(
				function() {
					layer.load(0, {shade : [0.8, '#393D49']});
					//var selectedIds = getSelectedIds();
					if (!relateCheck()) {
						layer.closeAll('loading');
						return false;
					}
					var registNo = $("#registNo").val();
					var policyNo = "";
					var relatedPlyNo = "";
					var res = null;
					$('input:checkbox[name=checkCode]:checked').each(function(i){
				        if(0==i){
				        	policyNo = $(this).attr("id");
				        	res = checkPolicyIsGB(policyNo);
				        }else{
				        	relatedPlyNo = $(this).attr("id");
				        	res = checkPolicyIsGB(relatedPlyNo);
				        }
					});
					/* if ($("input[name='checkCode']:checked").length == 1) {
						policyNo = $("input[name='checkCode']:checked").attr("id");
					} else {
						
					} */
					/* var $tbody = $("#tbody");
					var params = {
						"registNo" : registNo,
						"policyNo" : policyNo,
					}; */
					if(res.statusText == "Y"){
		    			layer.confirm(res.data, {
		    				btn : [ '确定', '取消' ]
		    				}, function(index) {
		    					layer.close(index);
		    					var url = "/claimcar/regist/noPolicyFindNoConfirm.do?registNo="+registNo+"&policyNo="+policyNo+"&relatedPlyNo="+relatedPlyNo;
		    					location.href = url;
		    					layer.closeAll();
				    			layer.load(0, {shade : [0.8, '#393D49']});
		    					},function(index){
		    						layer.closeAll();
		    					});
		    		}else if(res.statusText == 'N'){
		    			var url = "/claimcar/regist/noPolicyFindNoConfirm.do?registNo="+registNo+"&policyNo="+policyNo+"&relatedPlyNo="+relatedPlyNo;
    					location.href = url;
    					layer.closeAll();
		    			layer.load(0, {shade : [0.8, '#393D49']});
		    		}
					//var url = "/claimcar/regist/noPolicyFindNoConfirm.do?registNo="+registNo+"&policyNo="+policyNo+"&relatedPlyNo="+relatedPlyNo;
					//location.href = url;
					//layer.close();
					
					/* $.post(checkUrl, params, function(checkResult) {
						if (checkResult == 1) {
							$.post(relationUrl, params, function(relationResult) {
								$tbody.append(relationResult);
								layer.msg("操作成功");
							});
						} else if (checkResult == 2){
							layer.msg("该保单已进行过保单取消操作，不能再进行保单关联。");
						}
					}); */
				}
			);
		
		
		
		});
	
			function radioChecked(val){
				$("input[name='policyInfoVo.checkFlag']").each(
					function(){
						if( $(this).val()==val){
							$(this).prop("checked",true);
						}
					});
			}
			
			//校验查询条件
			function checkBeforeQuery(){
				var checkedIndex = $("input[name='policyInfoVo.checkFlag']").filter(":checked").val();
				var flag = false;
				if (checkedIndex == 1) {
					var policyNo = $.trim($("#policyNo").val());
					if (policyNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 2) {
					var licenseNo = $.trim($("#licenseNo").val());
					if (licenseNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 3) {
					var insuredName = $.trim($("#insuredName").val());
					if (insuredName == "") {
						flag = true;
					}
				}
				if (checkedIndex == 4) {
					var engineNo = $.trim($("#engineNo").val());
					if (engineNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 5) {
					var frameNo = $.trim($("#frameNo").val());
					if (frameNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 6) {
					var vinNo = $.trim($("#vinNo").val());
					if (vinNo == "") {
						flag = true;
					}
				}
				if (checkedIndex == 7) {
					var vinNo = $.trim($("#insuredIdNo").val());
					if (vinNo == "") {
						flag = true;
					}
				}
				return flag;
			}

			//关联保单自动勾选及关联校验
			function relatePly(element) {
				var relatePly = $(element).val();
				if ($(element).prop("checked")) {
					var a = $("#" + relatePly).attr("disabled");
					if(!a){
						$("#" + relatePly).prop("checked", true);
					}
				}
			}
			
			//勾选数量及关联勾选校验
			function relateCheck() {
				//勾选保单不能超过2张
				if ($("input[name='checkCode']:checked").length == 0) {
					layer.alert("请选择需要报案的保单");
					return false;
				}
				//勾选保单不能超过2张
				if ($("input[name='checkCode']:checked").length > 2) {
					layer.alert("最多只能选择2张保单进行报案。");
					return false;
				}
				
				if ($("input[name='checkCode']:checked").length == 2) {
					var licenseNoArr = new Array();
					var insuredNameArr = new Array();
					var risktypeArr = new Array();
					//var frameNo = new Array();
					$("input[name='checkCode']:checked").each(function(){
						licenseNoArr.push($(this).attr("licenseno"));
						insuredNameArr.push($(this).attr("insuredname"));
						risktypeArr.push($(this).attr("risktype"));
						//frameNo.push($(this).attr("frameNo"));
					});
					//勾选的保单标的车牌号需要一致
					 if (licenseNoArr[0] != licenseNoArr[1]) {
						layer.alert("被保险人相同，保单号关联，只能选择同一辆标的车进行报案");
						return false;
					} 
					//勾选的保单被保险人需要一致
					if (insuredNameArr[0] != insuredNameArr[1]) {
						layer.alert("只能选择同一个被保险人的两张保单进行报案。");
						return false;
					}
					//勾选的保单类型不能相同risktype
					if (risktypeArr[0] == risktypeArr[1]) {
						layer.alert("只能选择同一被保险人的关联商业险和交强险进行报案。");
						return false;
					}
					//勾选的车架号不相同frameNo
					/* if (frameNo[0] != frameNo[1]) {
						layer.alert("只能选择同一车架号的关联商业险和交强险进行报案。");
						return false;
					} */
				}
				
				
				
				
				
				return true;
			}
			var rtIndex=null;
			function reportTmp(){
				var iName = $("input[name='policyInfoVo.insuredName']").val();
				var liNo = $("input[name='policyInfoVo.licenseNo']").val();
				var enNo = $("input[name='policyInfoVo.engineNo']").val();
				var frNo = $("input[name='policyInfoVo.frameNo']").val();
				var url="/claimcar/regist/ReportTmpEdit.do?iName="+iName+"&liNo="+liNo+"&enNo="+enNo+"&frNo="+frNo+"";
				if(rtIndex==null){
					rtIndex=layer.open({
					    type: 2,
					    title: '无保单报案',
					    shade: false,
					    area: ['1100px', '500px'],
					    content:url,
					    end:function(){
					    	rtIndex=null;
					    	var registTmpNo = $("#registTmpNo").val();
					    	//alert(registTmpNo);
					    	//保存成功后跳转到报案登记界面
					    	if(registTmpNo!=null&&registTmpNo!=""){
					    		window.location.href="/claimcar/regist/addRegist.do?registNo="+registTmpNo+"&tempRegistFlag=1"; 
					    	}
					    	
					    }
					});
				}
			}
			function closeThis() {
				var url = "/claimcar/regist/edit.do?registNo=";
				location.href = url + $("#registNo").val();
			}
			//检验是否为联共保保单
			function checkPolicyIsGB(relatedPlyNo){
				
				var plyNoArr = new Array();
				var result ;
				//拿到需要查询的保单 保单+关联保单(不一定存在)
				$("input[name='checkCode']:checked").each(function() {
					plyNoArr.push($(this).attr("id"));
				});
				if(relatedPlyNo!=null){
					plyNoArr.push(relatedPlyNo);
				}
//				$.each(plyNoArr,function(i,n){
//					alert("第"+i+"个保单号为:"+n);
//				});
				$.ajax({
					url : "/claimcar/policyQuery/queryPolicyIsGB.ajax", // 后台校验
					type : 'post', // 数据发送方式
					dataType : 'json', // 接受数据格式
					data:{
						'plyNoArr':plyNoArr
					},
					async : false,
					traditional: true,
					success : function(res) {// 回调方法，可单独定义
						if(res.statusText == 'Y'){
							//alert("存在未实收保费的保单");
							returnFlag = 'Y';
							result=res;
						}else{
							//alert("不存在未实收保费的保单");
							returnFlag = 'N';
							result=res;
						}
					}
				});
				return result;
			}
	</script>
	
	<!--workbench_div     结束-->
</body>
</html>
