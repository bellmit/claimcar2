<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>报案查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					<input type="hidden"  name="nodeCode"  value="${nodeCode }" >
					<input type="hidden"  name="subNodeCode"  value="${subNodeCode }" >
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="registVo.registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="registVo.policyNo"  value="" />
							</div>
							<label class="form-label col-1 text-c">被保险人
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registVo.insuredName" value=""/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 车牌号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="registVo.licenseNo" value="" />
							</div>
							<label class="form-label col-1 text-c">车架号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registVo.frameNo" value=""/>
							</div>
							<label class="form-label col-1 text-c">发动机号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registVo.engineNo" value=""/>
							</div>
						</div>
						<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">座席人员</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registVo.firstRegUserCode" value=""/>
							</div>
							<label class="form-label col-1 text-c">出险时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate"  id="dgDateMin" name="registVo.damageTimeStart"  onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'dgDateMax\')||\'%y-%M-%d\'}'})">
								 -
								 <input type="text" class="Wdate"  id="dgDateMax" name="registVo.damageTimeEnd" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'dgDateMin\')}',maxDate:'%y-%M-%d'})" >
							</div>
							<label class="form-label col-1 text-c">联系电话</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="registVo.linkerNumber"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">现场报案</label>
							<div class="formControls col-3">
							<select name="registVo.isOnSitReport"  style="width:95%">
							<option>
							是
							</option>
							<option>
							否
							</option>
							</select>
								<!-- <div class="radio-box">
									 <label ><input type="radio" id="yes-1" name="registVo.isOnSitReport" checked>是</label>
								</div>
								<div class="radio-box">
									<label  ><input type="radio" id="no-1" name="registVo.isOnSitReport"> 否</label>
								</div> -->
							</div>
							<label class="form-label col-1 text-c">报案时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" name="registVo.reportTimeStart" id="rptDateMin" 
								value="<fmt:formatDate value='${reportTimeStart}' 
								pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'rgtDateMax\')||\'%y-%M-%d\'}'})"
										datatype="*" />
								 -
								 <input type="text" class="Wdate" name="registVo.reportTimeEnd" id="rptDateMax"

								   value="<fmt:formatDate value='${reportTimeEnd}' pattern='yyyy-MM-dd'/>"
										onfocus="WdatePicker({minDate:'#F{$dp.$D(\'rgtDateMin\')}',maxDate:'%y-%M-%d'})"
										datatype="*" /> 
							</div>
						</div>
						<div class="row mb-8 cl">
							<label class="form-label col-1 text-c">案件状态</label>
							<div class="formControls col-5">
								<div class="radio-box">
								 	<label><!-- registTaskFlag -->
								 		<input type="radio"  name="registTaskFlag" value="0" onchange="changeregistTaskFlag(this)" checked="checked">正在处理
								 	<label>
								 </div>
								 <div class="radio-box">
									<label>
										<input type="radio"  name="registTaskFlag" onchange="changeregistTaskFlag(this)" value="1">已处理
									</label>
								 </div>
								 <div class="radio-box">
									<label>
								 	 	<input type="radio"  name="registTaskFlag" onchange="changeregistTaskFlag(this)" value="7">注销
								 	</label> 
								 </div>				
							</div>
							<div class="form-label col-3">
							<label> </label>
							</div>
							<div class="formControls col-3">
								<label><input name="registVo.tempRegistFlag" type="checkbox" value="1">仅查临时案件</label>
							</div>
						</div>
						<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<button class="btn btn-primary btn-outline btn-search"
									id="search" type="button" disabled>查询</button>
							</span><br />
						</div>
					</form>
				</div>
			</div>
						
			<!--案查询条件 结束-->


			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl"> 
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handing">&#xe619;</i>正在处理</span> 
					<span onclick="changeHandleTab(1)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
					<span onclick="changeHandleTab(7)"><i class="Hui-iconfont handout">&#xe619;</i>注销</span>
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号</th>
								<th>出险地点</th>
								<th>出险日期</th>
								<th>报案时间</th>
								<th>保单类型</th>
								<th>处理人员</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!-- 已处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_1" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号</th>
								<th>出险地点</th>
								<th>出险日期</th>
								<th>报案时间</th>
								<th>保单类型</th>
								<th>处理人员</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<!--注销-->
				<div class="tabCon clearfix">
					<table id="DataTable_2" class="table table-border table-hover data-table" cellpadding="0" >
						<thead>
							<tr>
								<tr>
								<th>业务标识</th>
								<th>报案号</th>
								<th>保单号</th>
								<th>车牌号</th>
								<th>出险地点</th>
								<th>出险日期</th>
								<th>报案时间</th>
								<th>保单类型</th>
								<th>处理人员</th>
							</tr>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
	</div>

	<script type="text/javascript" src="/claimcar/js/flowCommon.js"></script>
	<script type="text/javascript">
		//----------正在处理  --- start
		var columns = [ 
			{"data" : "bussTagHtml"}, //业务标识
			{"data" : "registNo"}, //报案号
	        {"data" : "policyNo"}, //保单号
	        {"data" : "licenseNo"}, //车牌号
			{"data" : "damageAddress"}, //出险地点
			{"data" : "damageTime",
				render : function(data, type, row) {
					return DateUtils.cutToMinute(data);
				}}, //出险日期
			{"data" : "reportTime",
				render : function(data, type, row) {
					return DateUtils.cutToMinute(data);
				}	
			}, //报案时间
			{"data" : "policyType"}, //保单类型
			{"data" : "updateUser"} //处理人员
			/* {"data" : null, //业务标识
				render : function(data, type, row) {
					return "";
				}
			},  */
			];

		function rowCallback(row, data, displayIndex, displayIndexFull) {
			var goUrl="/claimcar/regist/edit.do/"+data.registNo;
			$('td:eq(1)', row).html("<a target='_blank'  onClick=openTaskEditWin('报案编辑','"+goUrl+"') '>" + data.registNo + "</a>");

			/* var  goUrl1="/claimcar/policyView/policyView.do?policyNo="+ data.policyNo;
			$('td:eq(1)', row).html("<a href="+goUrl1+"  target='_blank'>"+data.policyNo+"</a>"); */
		}
		
		function ajaxSubmit(tableId){
			var ajaxList = new AjaxList("#"+tableId+"");
			ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
			ajaxList.postData=$("#form").serializeJson();
			ajaxList.columns = columns;
			ajaxList.rowCallback = rowCallback;
			ajaxList.query();
		}
 
		
		
		//---------注销--- end

		
		$(function() {
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
			$("#search").click(function() {
				search();
			});
			
		});
		/**切换选择的任务类型*/
		function changeregistTaskFlag(obj){
			//var registTaskFlagRadio=form.[registVo.registTaskFlag];
			var registTaskFlagRadio=form.registTaskFlag;
			var selectIdx=0;
			for(var i=0;i<registTaskFlagRadio.length;i++){
				if(registTaskFlagRadio[i].checked==true){
					selectIdx=i;
					break;
				}
			}
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", selectIdx);
		}
		function changeHandleTab(val){
			$("input[name='registTaskFlag']").each(
				function(){
					if( $(this).val()==val){
						$(this).prop("checked",true);
						var handleStatus=$("input[name='registTaskFlag']:checked").val();
						var tBody=$("#DataTable_"+handleStatus).find("tbody");
						if(!(tBody.children().length>0)){
							layer.load();
							setTimeout(function(){ 
								search();
								layer.closeAll('loading');
							},0.5*1000); 
							
						}
					}
				});
			/* $("input[name='workStatus']").each(
					function(){
						if( $(this).val()==val){
							$(this).prop("checked",true);
							search();
						}
					}); */
		}
		function search(){
			var registTaskFlag=$("input[name='registTaskFlag']:checked").val();
			
			if(isBlank(registTaskFlag)){
				layer.msg("请选择任务状态");
				return false;
			}
			if(registTaskFlag=='0'){
				ajaxSubmit("DataTable_"+registTaskFlag);
			}else if(registTaskFlag=='1'){
				ajaxSubmit("DataTable_"+registTaskFlag);
			}else if(registTaskFlag=='7'){
				ajaxSubmit("DataTable_2");
			}
		}
	</script>
</body>
</html>
