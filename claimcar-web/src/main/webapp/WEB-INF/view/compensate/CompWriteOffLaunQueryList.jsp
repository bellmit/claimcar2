<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>理算冲销任务发起查询</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont pd-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label class="form_label col-2">报案号：</label>
					 		<div class="form_input col-2">
					 			<input type="text" class="input-text" name="prpLCompensate.registNo" datatype="n4-22" ignore="ignore"/>
					 		</div>
							<label class="form_label col-2">立案号：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text" name="prpLCompensate.claimNo" datatype="n4-22" ignore="ignore" />
							</div>
							<label class="form_label col-2">归属机构：</label>
							<div class="form_input col-2">
								<app:codeSelect codeType="ComCode" name="prpLCompensate.comCode" type="select" lableType="code-name" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form_label col-2">计算书号：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text" name="prpLCompensate.compensateNo" datatype="n4-24" ignore="ignore" />
							</div>
							<label class="form_label col-2">计算书类型：</label>
							<div class="form_input col-2">
								<app:codeSelect codeType="CompKind" type="select" name="prpLCompensate.compensateKind"/>
							</div>
						</div>
						<div class="line mt-10 mb-10"></div>
						<div class="row cl">
							<span class="col-12 text-c">
							   <button class="btn btn-primary btn-outline btn-search"  type="submit" disabled>
								<i class="Hui-iconfont  Hui-iconfont-search2"></i>  查询</button>
							</span>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--案查询条件 结束-->

		<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
							<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
									<th>计算书类型</th>
									<th>报案号</th>
									<th>计算书号</th>
									<th>立案号</th>
									<th>客户等级</th>
									<th>承保机构</th>
									<th>核赔通过时间</th>
									<th>提交人</th>
								</tr>
							</thead>
							<tbody class="text-c">
								<!-- 动态生成表格 -->
							</tbody>
						</table>
				</div>
			</div>
		</div>
		<!--标签页 结束-->
		
		
</div>
	<script type="text/javascript">

	var columns = [
		       		{
		       			"data" :"compensateKindName",
		       			"orderable" : true,
		       			"targets" : 0
		       		}, {
		       			"data" : "registNo"
		       		}, {
		       			"data" : "compensateNo"
		       		},  {
		       			"data" : "claimNo"
		       		},{
		       			"data" : null
		       		},{
		       			"data" : "comCodeName"
		       		}
		       		,{
		       			"data" : "underwriteDate"
		       		}
		       		,{
		       			"data" : "createUserName"
		       		}
		       	  ];
	
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		
		  $('td:eq(1)',row).html("<a onclick=claimToRegistView('"+data.registNo+"');>"+data.registNo+"</a>");

			var url = "/claimcar/compensate/compeWriteOff.do?registNo=" + data.registNo+"&compensateNo="+data.compensateNo+"&claimNo="+data.claimNo;
			$('td:eq(2)', row).html("<a  onclick='openTaskEditWin(\"理算冲销发起\",\""+url+"\");'>"+data.compensateNo+"</a>");
			$('td:eq(4)', row).html(null);
			
			if(data.claimNo!=null){
				  $('td:eq(3)',row).html("<a onclick=prePayToclaimView('"+data.claimNo+"','"+data.registNo+"');>"+data.claimNo+"</a>");
			}
	}		
	
	function search(){
		var ajaxList = new AjaxList("#resultDataTable");
		ajaxList.targetUrl = "/claimcar/compensate/compeWriteOffLaunFind.do";
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.postData=$("#form").serializeJson();
		ajaxList.query();
	}
	
	$(function(){
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
		bindValidForm($('#form'),search);
	});
	
	
	function claimToRegistView(registNo){
	     var title='报案处理';
	     var url='/claimcar/regist/edit.do?registNo='+registNo;
	      openTaskEditWin(title,url);
     }
	
	function prePayToclaimView(claimNo,registNo){
		var sign='0';//商业.交强标志位。0商业，1交强
		var returnflag=true;
		var partclaimNo=claimNo.substring(11,15); 
		var title="";
		if(partclaimNo=='1101'){
			title="立案(交强)处理";
			sign='1';
		}else{
			title="立案(商业)处理";
		}
		var url1="/claimcar/prePay/prePayToClaimView.do?registNo="+registNo+"&sign="+sign;
		var taskId="";
		$.ajax({
			url : url1, // 后台校验
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			async : false,
			success : function(result) {// 回调方法，可单独定义	
			
				if (result.status == "200") {
					    taskId=result.data;
					    if(taskId != null && taskId != "undefined" && taskId != "" && taskId != "null"){
					    	returnflag = false;
					    }
						
				}
			}
		});
		if(returnflag){
			return false;
		}
		var url2="/claimcar/claim/claimView.do?claimNo="+claimNo+"&flowTaskId="+taskId;
	    openTaskEditWin(title,url2);
		
	}
	
	</script>
</body>
</html>
