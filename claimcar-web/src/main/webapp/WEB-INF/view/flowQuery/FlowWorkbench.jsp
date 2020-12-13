<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>工作台</title>
		<link href="/claimcar/plugins/qtip/jquery.qtip.min.css" rel="stylesheet" type="text/css" />
		
				<link  href="/claimcar/css/workbench.css"  rel="stylesheet" type="text/css" />
				
	</head>
	<body>
		<!--workbench_div     开始-->
		<div class="pd-20 workbench_div">
			<!--标签页bxmessage    开始-->
			<div class="bxmessage">
				<div class="tabbox">
					<div id="tab-system" class="HuiTab">					
						<div class="tabBar f_gray4 cl">
                            <span onclick="search('0')"><i class="Hui-iconfont handun">&#xe619;</i>未接收（${resultPageNum}）（超时${timeOutNum }）</span>
                            <span onclick="search('1')"><i class="Hui-iconfont handun">&#xe619;</i>可处理（${resultPage2Num}）</span>
                            <span onclick="search('2')"><i class="Hui-iconfont handing">&#xe619;</i>正在处理（${resultPage3Num}）</span>
                            <span onclick="search('6')"><i class="Hui-iconfont handback">&#xe619;</i>已退回（${resultPage4Num}）</span>
                            <c:if test="${hasAccBackTask  eq 'true' }">
								<span onclick="search('AccBack')">账号信息修改(${AccBackNum })</span>
							</c:if>	
						</div>
						<!--未接收的消息    开始-->
						<div class="tabCon clearfix">
							<table id="DataTable_0"  class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>节点</th>
										<th>报案号</th>
										<th>保单号</th>
										<th>被保险人</th>
										<th>损失方</th>
										<th>承保机构</th>
										<th>流入时间</th>
										<th>提交人</th>
										<th>业务标识</th>
									</tr>
								</thead>
								<tbody class="text-c">
								</tbody>								
							</table>
							<!--table   结束-->
						</div>
						<!--可处理   开始-->
						<div class="tabCon clearfix">
							<table id="DataTable_1"  class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>节点</th>
										<th>报案号</th>
										<th>保单号</th>
										<th>被保险人</th>
										<th>损失方</th>
										<th>承保机构</th>
										<th>流入时间</th>
										<th>提交人</th>
										<th>业务标识</th>
									</tr>
								</thead>
								<tbody class="text-c">
								</tbody>
							</table>
							<!--table   结束-->
						</div>
						<!-- 正在处理 -->
						<div class="tabCon clearfix">
							<table id="DataTable_2"  class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>节点</th>
										<th>报案号</th>
										<th>保单号</th>
										<th>被保险人</th>
										<th>损失方</th>
										<th>承保机构</th>
										<th>流入时间</th>
										<th>提交人</th>
										<th>业务标识</th>
									</tr>
								</thead>
								<tbody class="text-c">
								</tbody>								
							</table>
						</div>
						<!-- 正在处理  end-->
						<!-- 已退回  -->
						<div class="tabCon clearfix">
							<table id="DataTable_6"  class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
								<thead>
									<tr class="text-c">
										<th>节点</th>
										<th>报案号</th>
										<th>保单号</th>
										<th>被保险人</th>
										<th>损失方</th>
										<th>承保机构</th>
										<th>流入时间</th>
										<th>提交人</th>
										<th>业务标识</th>
									</tr>
								</thead>
								<tbody class="text-c">
								</tbody>								
							</table>
						</div>
						<!-- 已退回  end -->
						<c:if test="${hasAccBackTask == 'true' }">
						<!-- 账号信息修改 -->
							<div class="tabCon clearfix" id="table_6"> 
								<table id="DataTable_AccBack" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
								<thead>
									<tr class="text-c">
									<th>计算书号</th>
									<th>开户银行</th>
									<th>开户人</th>
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
						<!-- 账号信息修改 end -->
						</c:if>
					</div>
				</div>
			</div>
			<!--标签页bxmessage    结束-->
			<div class="fotter_cont clearfix">
				<!--系统公告   开始-->
			<div class="fotter_notice fl">
				<div class="notice_title clearfix">
					<div class="fl f14">
						<strong>系统公告</strong>
					</div>
					<a class="fr more f_gray4" onclick="openTaskEditWin('系统公告', '/claimcar/sysMsg/initMoreSysUtiBulletinList.do')">查看更多</a>
				</div>
				<div class="noticecont" id="SysUtiBulletinBox">
				</div>
			</div>
			<!--系统公告   结束-->
			<!--留言通知   开始-->
			<div class="fotter_notice message fl">
				<div class="notice_title clearfix">
					<input type="hidden" name="userCode" value="${user.userCode }" />
					<div class="fl f14"><strong>留言/通知</strong></div>
					<a class="fr more f_gray4"  onclick="openTaskEditWin('留言通知', '/claimcar/sysMsg/initMoreSysMsgList.do')">查看更多</a>
				</div>
				<div class="noticecont" id="SysMsgBox">
				</div>
			</div>
			<!--留言通知   结束-->
				
				<!--快捷菜单   开始-->
				<div class="fotter_notice fl">
					<div class="notice_title clearfix">
						<div class="fl f14"><strong>快捷菜单</strong></div>						
					</div>
					<div class="noticecont">
						<ul>
							<li><a src="/claimcar/images/lccx.png" id="aa"
								href="/claimcar/workbench/showTaskList.do?intoPage=FlowWorkbenchList"> 
								<img src="/claimcar/images/lccx.png" />
								<p>工作台切换</p></a>
							</li>
							<li>
								<img src="/claimcar/images/rwcx.png"/>
								<p>任务查询</p>
							</li>
							<li>
								<img src="/claimcar/images/lccx.png"/>
								<p>流程查询</p>
							</li>
						</ul>
					</div>
				</div>
				<!--快捷菜单   结束-->
			</div>
			<c:if test="${isCheckRole eq 'true'}">
				<div class="notice_title clearfix">
					<div class="fl f14">
						<strong>查勘指标</strong>
					</div>
				</div>
				<%@include file="../check/checkIndex/checkIndexShow_child.jsp" %>
			</c:if>
		</div>
		
		<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>
		<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
		<script type="text/javascript" src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
		<script type="text/javascript">
		var nodeCode;
		
		var columns0 = [
		    			{"data" : "taskName"}, //节点
		    			{"data" : "registNo"}, //报案号
		    			{"data" : "policyNoHtml"}, //保单号
		    			{"data" : "insuredName"}, //被保险人
		    			{"data" : "deflossCarType"}, //损失方
		    			{"data" : "comCodeName"}, //承保机构
		    			{"data" : "taskInTime"}, //流入时间
		    			{"data" : "taskInUserName"}, //提交人
		    			{"data" : "bussTagHtml"}
		    		];
		
		var columns3 = [
			       		{
			       			"data":"compensateNo"     //计算书号
			       		},{
			       			"data" : "bankNameName"     //开户银行
			       		}, {
			       			"data" : "accountName"  //开户人
			       		},{
			       			"data" : "accountNo"   //银行账户
			       		},{
			       			"data" : "remark"   //修改原因
			       		},{
			       			"data" : "appTime",    //申请日期
			       		},{
			       			"data" : "payTypeName",    //收付原因
			       		}
		];
		
		function rowCallback(row, data, displayIndex, displayIndexFull) {
			if(nodeCode == "AccBack"){
				//$('td:eq(1)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.policyNo+"</a>");
				$('td:eq(0)', row).html("<a onclick=accountInfo('"+data.compensateNo+"','"+data.registNo+"','"+data.flag+"','"+data.chargeCode+"','"+data.payType+"','"+data.serialNo+"');>"+data.compensateNo+"</a>");
			}else{
				$('td:eq(1)', row).html("<a onclick=init('"+data.nodeCode+"','"+data.subNodeCode+"','"+data.taskId+"','"+data.registNo+"','"+data.claimNo+"','"+data.workStatus+"','"+data.handlerIdKey+"','"+data.handlerStatus+"','"+data.taskName+"');>"+data.registNo+"</a>");
			}
		}
		
		function search(subNodeCode) {

			nodeCode = subNodeCode;
			var params = {
				"subNodeCode" : subNodeCode,
				"handleStatus" : subNodeCode
			};

			var ajaxList = new AjaxList("#DataTable_" + subNodeCode);
			ajaxList.targetUrl = 'searchWorkbench.do';// + $("#form").serialize();
			ajaxList.postData = params;
			ajaxList.rowCallback = rowCallback;
			if(subNodeCode == 'AccBack'){
				ajaxList.columns = columns3;
			}else{
				ajaxList.columns = columns0;
			}
			ajaxList.query();
		}
		
		$(function() {
			getSysMsgRece();
			getSysUtiBulletin();
			search('0');
		});
		
		function getSysMsgRece(){//获取留言信息
			var userCode = $("[name='userCode']").val();
			var $tbody = $("#SysMsgBox");
			var params = {
					"userCode" : userCode,
				};
				var url = "/claimcar/sysMsg/findMsgCont.ajax";
				$.post(url, params, function(result) {
					$tbody.append(result);
				});
		}
		function getSysUtiBulletin(){//获取系统公告
			var $tbody = $("#SysUtiBulletinBox");
			var url = "/claimcar/sysMsg/findSysUtiBulletin.ajax";
			$.post(url, function(result) {
				$tbody.append(result);
			});
		}
		
		function accountInfo(compensateNo,registNo,payeeId,chargeCode,payType,serialNo){
			var taskName = "理赔账户信息修改处理";
			var handleStatus="0";
			var isVerify = "0";
			
			var url = "/claimcar/accountInfo/accountInfoInit.do?compensateNo="+compensateNo+"&handleStatus="
					+handleStatus+"&registNo="+registNo+"&payeeId="+payeeId+"&isVerify="+isVerify+"&payType="+payType+"&serialNo="+serialNo;
			openTaskEditWin(taskName,url);
		}
		
		function init(nodeCode,subNodeCode,taskId,registNo,claimNo,workStatus,handlerIdKey,handlerStatus,taskName){
			var url = "";
			if(nodeCode=="Sched"){
				url = "/claimcar/schedule/preScheduleEdit.do?flowTaskId="+taskId+"&registNo="+registNo;
			}else if(nodeCode=="Check" || nodeCode=="ChkBig" ){
				url = "/claimcar/check/initCheck.do?flowTaskId="+taskId;
			}else if(subNodeCode=="ReCanApp"){
				url = "/claimcar/claimRecover/claimCancelInit.do?claimNo="+claimNo+"&taskId="+taskId+"&workStatus="+workStatus+"&handlerIdKey="+handlerIdKey;
			}else if(subNodeCode=="CancelApp" || subNodeCode=="CancelAppJuPei"){
				url = "/claimcar/claim/claimCancelInit.do?claimNo="+claimNo+"&taskId="+taskId+"&workStatus="+workStatus+"&handlerIdKey="+handlerIdKey;
			}else if(subNodeCode=="ReCanVrf_LV1" || subNodeCode=="CancelVrf_LV1" || subNodeCode=="CancelLVrf_LV1"){
				var types = "1";//分公司是1
				url = "/claimcar/claim/claimCancelCheckInit.do?claimNo="+claimNo+"&types="+types+"&flowTaskId="+taskId+"&handlerStatus="+handlerStatus+"&subNodeCode"+subNodeCode;
			}else if(subNodeCode=="ReCanLVrf_LV11"){
				var types = "2";//总公司是2
				url = "/claimcar/claim/claimCancelCheckInit.do?claimNo="+claimNo+"&types="+types+"&flowTaskId="+taskId+"&handlerStatus="+handlerStatus+"&subNodeCode"+subNodeCode;
			}else if(subNodeCode=="ReOpenVrf_LV1" || subNodeCode=="ReOpenVrf_LV2"){
				url = "/claimcar/reOpen/reOpenCheckEdit.do?flowTaskId="+taskId;
			}else if(nodeCode=="VLoss" && subNodeCode.substr(0,5)=="VLCar"){
				url = "/claimcar/defloss/preAddVerifyLoss.do?flowTaskId="+taskId;
			}else if(nodeCode=="VLoss" && subNodeCode.substr(0,6)=="VLProp"){
				url = "/claimcar/proploss/initPropVerifyLoss.do?flowTaskId="+taskId;
			}else if(nodeCode=="RecLoss" ){
				url = "/claimcar/recLoss/init.do?flowTaskId="+taskId;
			}else if(nodeCode=="VPrice" ){
				url = "/claimcar/defloss/preAddVerifyPrice.do?flowTaskId="+taskId;
			}else if(nodeCode=="Certi" ){
				url = "/claimcar/certify/init.do?flowTaskId="+taskId+"&registNo="+registNo+"&certifyMakeup"+null;
			}else if(nodeCode=="Compe" ){
				url = "/claimcar/compensate/compensateEdit.do?flowTaskId="+taskId;
			}else if(nodeCode=="VClaim" ){
				url = "/claimcar/verifyClaim/verifyClaimEdit.do?flowTaskId="+taskId;
			}else if(nodeCode=="DLoss" &&(subNodeCode=="DLCar" || subNodeCode=="DLChk" || subNodeCode=="DLCarMod" || subNodeCode=="DLCarAdd")){
				url = "/claimcar/defloss/preAddDefloss.do?flowTaskId="+taskId+"&sign="+"0";
			}else if(nodeCode=="DLoss" &&(subNodeCode=="DLProp" || subNodeCode=="DLPropAdd" || subNodeCode=="DLPropMod" )){
				url = "/claimcar/proploss/initPropCertainLoss.do?flowTaskId="+taskId;
			}else if(nodeCode=="PrePay"){
				url = "/claimcar/prePay/prePayApplyEdit.do?flowTaskId="+taskId;
			}else if(nodeCode=="PadPay"){
				url = "/claimcar/padPay/padPayEdit.do?registNo="+registNo+"&taskInKey="+""+"&flowTaskId="+taskId;
			}else if(nodeCode=="RecPay"){
				url = "/claimcar/recPay/recPayEdit.do?flowTaskId="+taskId;
			}
			
			openTaskEditWin(taskName,url);
		}
		
		$("#aa").click(function(){
			layer.load();
		});
	</script>
		<!--workbench_div     结束-->
	</body>
</html>
    