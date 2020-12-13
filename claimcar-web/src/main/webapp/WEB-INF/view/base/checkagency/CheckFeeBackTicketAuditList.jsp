<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>查勘费退票审核查询</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
					<input type="hidden" name="nodeCode" value="">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">业务号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="*4-24" ignore="ignore" errormsg="请输入4到22位数" name="prpLWfTaskQueryVo.bussNo" value="" />
							</div>
							<label class="form-label col-1 text-c">立案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="prpLWfTaskQueryVo.claimNo" value="" />
							</div>
							<label class="form-label col-1 text-c">查勘机构</label>
							<div class="formControls col-3">
								<app:codeSelect codeType="" type="select" id="checkCode" name="prpLWfTaskQueryVo.checkCode" lableType="name" value="" dataSource="${dictVos}"/>
								<c:if test="${empty dictVos}"> <select  class="select "   value=""  dataType="selectMust"> </select> </c:if>
                         		<font color="red">*</font>
								
							</div>
						</div>
						<div class="row mb-3 cl">
						   <label class="form-label col-1 text-c">流入日期</label>
							<!-- 时间间隔要求为一个月 -->
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="prpLWfTaskQueryVo.taskInTimeStart" value="<fmt:formatDate value='${taskInTimeStart}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})" datatype="*" />
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="prpLWfTaskQueryVo.taskInTimeEnd" value="<fmt:formatDate value='${taskInTimeEnd}' pattern='yyyy-MM-dd'/>"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" datatype="*" /><font color="red">*</font>
								
							</div>
							
							
							<label class="form-label col-1 text-c">任务状态</label>
							<div class="formControls col-3">
							 	<label>
							 		<input type="radio" name="handleStatus" value="0" onchange="changeHandleStatus(this)" checked="checked">未处理
							 	</label>
							 	<label>
							    	<input type="radio" name="handleStatus" onchange="changeHandleStatus(this)" value="3">已处理
							    </label>
							   <font color="red">*</font>	
						    </div>
							<label class="form-label col-1 text-c"></label>
							<div class="formControls col-3">
							</div>
						</div>
						

                        
						<div class="line"></div>
						<div class="row cl text-c">
							<span class="col-offset-8 col-4">
								<button class="btn btn-primary btn-outline btn-search" disabled type="submit">
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询
								</button> 
							</span>
						</div>
					</form>
				</div>
			</div>
			
			<br />
			<!-- 查询条件结束 -->
            <!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl"> 
					<span onclick="changeHandleTab(0)"><i class="Hui-iconfont handing">&#xe619;</i>未处理</span> 
					<span onclick="changeHandleTab(3)"><i class="Hui-iconfont handout">&#xe619;</i>已处理</span>
				</div>
				<div class="tabCon clearfix table_count table_list">
					<table id="DataTable_0" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>业务号</th>
								<th>账户名称</th>
								<th>开户银行</th>
								<th>银行账户</th>
								<th>修改原因</th>
								<th>申请日期</th>
								<th>收付原因</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
			    </div>
			    <div class="tabCon clearfix table_count table_list">
					<table id="DataTable_3" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>业务号</th>
								<th>账户名称</th>
								<th>开户银行</th>
								<th>银行账户</th>
								<th>修改原因</th>
								<th>申请日期</th>
								<th>收付原因</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
			    </div>
			    
			<!--标签页 结束-->
		</div>
	</div>
</div>
<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
<%-- <script type="text/javascript" src="${ctx }/js/common/AjaxList.js"></script>  --%>
<script type="text/javascript">
var columns0 = [
      			{"data" : null},  //业务号
      			{"data" : "accountName"},//开户名称
      		    {"data" : "bankName"},//开户银行
      		    {"data" : "accountNo"}, //银行账户
      	        {"data" : "remark"},//修改原因
      	        {"data" : "appTime"},//申请日期
      	        {"data" : "payTypeName"}//收付原因
      	        
      			];

   var columns3 = [
      			{"data" :null}, //业务号
      			{"data" : "accountName"},//开户名称
      		    {"data" : "bankName"},//开户银行
      		    {"data" : "accountNo"}, //银行账户
      	        {"data" : "remark"},//修改原因
      	        {"data" : "appTime"},//申请日期
      	        {"data" : "payTypeName"} //收付原因
      	        
      			];
	       	  
function rowCallback(row, data, displayIndex, displayIndexFull) {
	$('td:eq(0)', row).html("<a  onclick=checkAuditMoreMessage('"+data.bussNo+"','"+data.accountId+"','"+data.backaccountId+"','"+data.checkCode+"','"+data.registNo+"','"+data.auditId+"');>"+data.bussNo+"</a>");
	
}  
function isIgnoreTime(bussNo,claimNo){
	if(!isBlank(bussNo)){
		bussNo  = bussNo.replace(/\s+/g,"");
	}
	if(!isBlank(claimNo)){
		claimNo  = claimNo.replace(/\s+/g,"");
	}
    if(!isBlank(bussNo)){
		return false;
	}  else if(!isBlank(claimNo)){
		return false;
	} 
	return true;
}
//时间减法
function getDays(strDateStart,strDateEnd){ 
	var strSeparator = "-"; //日期分隔符 
	var oDate1; 
	var oDate2; 
	var iDays; 
	oDate1= strDateStart.split(strSeparator); 
	oDate2= strDateEnd.split(strSeparator); 
	var strDateS = new Date(oDate1[0], oDate1[1]-1, oDate1[2]); 
	var strDateE = new Date(oDate2[0], oDate2[1]-1, oDate2[2]); 
	iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数 
	return iDays ; 
} 
function search(){
	var handleStatus=$("input[name='handleStatus']:checked").val();
	if(isBlank(handleStatus)){
		layer.msg("请选择任务状态");
		return false;
	}
	var bussNo = $("*[name='prpLWfTaskQueryVo.bussNo']").val(); 
	var claimNo = $("*[name='prpLWfTaskQueryVo.claimNo']").val();
	if(isIgnoreTime(bussNo,claimNo)){
		var damageTimeStart = $("*[name='prpLWfTaskQueryVo.taskInTimeStart']").val();
		var damageTimeEnd = $("*[name='prpLWfTaskQueryVo.taskInTimeEnd']").val();
		var day = getDays(damageTimeEnd,damageTimeStart);//计算整数天数
		if(day>30){
			layer.alert("流入日期查询范围最大不能超过30天");
			return false;
		}
	} 
	var ajaxList = new AjaxList("#DataTable_"+handleStatus);
	ajaxList.targetUrl ='/claimcar/checkfee/checkFeeAccountAuditSearch';
	ajaxList.postData = $("#form").serializeJson();
	ajaxList.rowCallback = rowCallback;
	if(handleStatus=='0'){
		ajaxList.columns = columns0;
	}else if(handleStatus=='3'){
		ajaxList.columns = columns3;
	}
	ajaxList.query();
}

/* function checkMoreMessage(bussNo,accountId){
	var params ="?bussNo=" + bussNo+"&accountId="+accountId;
	var url = "/claimcar/assessors/assessorMoreMessageList.do";
	var index = layer.open({
		type : 2,
		title : "业务单详情信息页面",
		area: ['60%','60%'],
		content : url + params
	});
	layer.full(index);
	
} */

//本案件下，公估费费用查看yzy
var assessor_index;
function checkAuditMoreMessage(bussNo,accountId,backaccountId,checkCode,registNo,auditId) {
	var url = "/claimcar/checkfee/checkfeeAuditMoreMessageList?bussNo="+bussNo+"&accountId="+accountId+"&backaccountId="+backaccountId+"&checkCode="+checkCode+"&registNo="+registNo+"&auditId="+auditId;
	/* if (assessor_index != null) {
		layer.close(assessor_index);
	} else {
		assessor_index = layer.open({
			type : 2,
			title : '业务单详情信息页面',
			shade : false,
			area : [ '80%', '95%' ],
			scrollbar: true,
			content :[ url, "yes" ],
			end : function() {
				assessor_index = null;
			}
		});
	} */
	openTaskEditWin("理赔账户信息修改", url);
	
	
	
}

</script>
</body>
</html>