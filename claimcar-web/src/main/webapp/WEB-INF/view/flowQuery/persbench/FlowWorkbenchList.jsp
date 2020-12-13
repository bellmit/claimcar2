<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>工作台</title>
<link href="/claimcar/plugins/qtip/jquery.qtip.min.css" rel="stylesheet" type="text/css" />
<link href="/claimcar/css/workbench.css" rel="stylesheet" type="text/css" />

</head>
<body>
	<!--workbench_div     开始-->
	<div class="pd-20 workbench_div">
		<!--标签页bxmessage    开始-->
		<div class="bxmessage">
			<div class="table_wrap">
				<div id="tab-system" class="HuiTab">
					<input type="hidden" id="hasPerLossTask" value="${hasPerLossTask }" />
					<input type="hidden" id="hasAccBackTask" value="${hasAccBackTask }" />
					<input type="hidden" id="isCheckRole" value="${isCheckRole }" />
					<div class="tabBar cl">
						<c:if test="${hasPerLossTask eq 'true'}">
						<span onclick="search('PLFirst','0,1,2')">人伤首次跟踪（${PLFirstNum }）（超时${timeOutNum }）</span> <span onclick="search('PLNext','0,1,2,5,6')">人伤后续跟踪（${PLNextNum }）</span> <span
							onclick="search('PLVerify','0,1,2,5')">人伤跟踪审核（${PLVerifyNum }）</span> <span onclick="search('PLCharge','0,1,2,5')">人伤费用审核（${PLChargeNum }）</span> <span
							onclick="search('PLBig','0,1,2')">人伤大案审核（${PLBigNum }）</span>
						</c:if>
						<c:if test="${hasAccBackTask  eq 'true' }">
						<span onclick="search('AccBack','')">账号信息修改(${AccBackNum })</span>
						</c:if>
						<shiro:hasRole name="claim.bill.register">
						<span onclick="billsearch('1')">发票登记(${taskNum})</span>
						</shiro:hasRole>
					</div>
					<c:if test="${hasPerLossTask == 'true' }">
					<!--人伤首次跟踪的消息    开始-->
					<div class="tabCon clearfix" id="table_1">
						<table id="DataTable_PLFirst" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th>任务状态</th>
									<th>报案号</th>
									<th>保单号</th>
									<th>被保险人</th>
									<th>承保机构</th>
									<th>报案时间</th>
									<th>流入时间</th>
									<th>已报案天数</th>
									<th>案件属地</th>
									<th>提交人</th>
									<th>业务标识</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						<!--table   结束-->

					</div>
					<!-- 人伤后续跟踪 -->
					<div class="tabCon clearfix" id="table_2">
						<table id="DataTable_PLNext" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th>任务状态</th>
									<th>报案号</th>
									<th>保单号</th>
									<th>被保险人</th>
									<th>承保机构</th>
									<th>报案时间</th>
									<th>流入时间</th>
									<th>已报案天数</th>
									<th>距预约处理天数</th>
									<th>案件属地</th>
									<th>提交人</th>
									<th>业务标识</th>
									<th>是否退回案件</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!-- 人伤后续跟踪  end-->
					<!-- 人伤跟踪审核  -->
					<div class="tabCon clearfix" id="table_3">
						<table id="DataTable_PLVerify" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th>任务状态</th>
									<th>报案号</th>
									<th>保单号</th>
									<th>被保险人</th>
									<th>承保机构</th>
									<th>报案时间</th>
									<th>流入时间</th>
									<th>案件属地</th>
									<th>提交人</th>
									<th>业务标识</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!-- 人伤跟踪审核  end -->
					<!-- 人伤费用审核  -->
					<div class="tabCon clearfix"  id="table_4">
						<table id="DataTable_PLCharge" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th>任务状态</th>
									<th>报案号</th>
									<th>保单号</th>
									<th>被保险人</th>
									<th>承保机构</th>
									<th>报案时间</th>
									<th>流入时间</th>
									<th>案件属地</th>
									<th>提交人</th>
									<th>业务标识</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!-- 人伤费用审核  end -->
					<!-- 人伤大案审核  -->
					<div class="tabCon clearfix" id="table_5">
						<table id="DataTable_PLBig" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th>任务状态</th>
									<th>报案号</th>
									<th>保单号</th>
									<th>被保险人</th>
									<th>承保机构</th>
									<th>报案时间</th>
									<th>流入时间</th>
									<th>案件属地</th>
									<th>提交人</th>
									<th>业务标识</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!-- 人伤大案审核  end -->
					</c:if>
					<c:if test="${hasAccBackTask == 'true' }">
					<!-- 账号信息修改 -->
					<div class="tabCon clearfix" id="table_6"> 
						<table id="DataTable_AccBack" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr>
								<th>计算书号</th>
								<th>开户银行</th>
								<th>开户人</th>
								<th>银行账户</th>
								<th>修改原因</th>
								<th>申请日期</th>
								<th>收付原因</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						</table>
					</div> 
					</c:if>
					<!-- 账号信息修改 end -->
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
					<div class="fl f14">
						<strong>快捷菜单</strong>
					</div>
				</div>
				<div class="noticecont">
					<ul>
						<li><a src="/claimcar/images/lccx.png"  id="aa"
						href="/claimcar/workbench/showTaskList.do?intoPage=FlowWorkbench"> 
						<img src="/claimcar/images/lccx.png" />
							<p>工作台切换</p></a></li>
						<li><img src="/claimcar/images/rwcx.png" />
							<p>任务查询</p></li>
						<li><img src="/claimcar/images/lccx.png" />
							<p>流程查询</p></li>
					</ul>
				</div>
			</div>
			<!--快捷菜单   结束-->
		</div>
		<%--查勘指标--%>
		<c:if test="${isCheckRole eq 'true'}">
			<div class="notice_title clearfix">
				<div class="fl f14">
					<strong>查勘指标</strong>
				</div>
			</div>
			<%@include file="../../check/checkIndex/checkIndexShow_child.jsp" %>
		</c:if>
	</div>
	<!--workbench_div     结束-->

	<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>

	<script type="text/javascript">
		var nodeCode = "PLFirst";
		$(function() {
			var hasPerLossTask = $("#hasPerLossTask").val();
			if(hasPerLossTask == "true"){
				search('PLFirst', '0,1,2');
			}else{
				nodeCode = "AccBack";
				search('AccBack','');
			}
			getSysMsgRece();
			getSysUtiBulletin();
		});
		
		//----------未接收   已接收待处理--- start
		var columns0 = [
			{"data" : "handlerStatusName"}, //任务状态
			{"data" : "registNoHtml"}, //报案号
			{"data" : "policyNoHtml"}, //保单号
			{"data" : "insuredName"}, //被保险人
			{"data" : "comCodeName"}, //承保机构
			{"data" : "reportTime"}, //报案时间
			{"data" : "taskInTime"}, //流入时间
			{"data" : "reportDay"}, //已报案天数
			{"data" : null}, //案件属地
			{"data" : "taskInUserName"}, //提交人
			{"data" : "bussTagHtml"}
		];

		var columns1 = [
			{"data" : "handlerStatusName"}, //任务状态
			{"data" : "registNoHtml"}, //报案号
			{"data" : "policyNoHtml"}, //保单号
			{"data" : "insuredName"}, //被保险人
			{"data" : "comCodeName"}, //承保机构
			{"data" : "reportTime"}, //报案时间
			{"data" : "taskInTime"}, //流入时间
			{"data" : "reportDay"}, //已报案天数
			{"data" : "appointmentDay"}, //距预约处理天数
			{"data" : null}, //案件属地
			{"data" : "taskInUserName"}, //提交人
			{"data" : "bussTagHtml"},
			{"data" : "backFlagsName"}//是否退回
		];
		
		var columns2 = [
			{"data" : "handlerStatusName"}, //任务状态
			{"data" : "registNoHtml"}, //报案号
			{"data" : "policyNoHtml"}, //保单号
			{"data" : "insuredName"}, //被保险人
			{"data" : "comCodeName"}, //承保机构
			{"data" : "reportTime"}, //报案时间
			{"data" : "taskInTime"}, //流入时间
			{"data" : null}, //案件属地
			{"data" : "taskInUserName"}, //提交人
			{"data" : "bussTagHtml"}
		];
		
		var columns3 = [
			       		{
			       			"data":"compensateNo"     //计算书号
			       		}, {
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
			if(nodeCode == "PLFirst"){
				$('td:eq(8)', row).html("");
			}else if(nodeCode == "PLNext"){
				$('td:eq(9)', row).html("");
			}else if(nodeCode == "AccBack"){
				//$('td:eq(1)',row).html("<a href=/claimcar/policyView/policyView.do?registNo="+data.registNo+" target='_blank'>"+data.policyNo+"</a>");
				$('td:eq(0)', row).html("<a onclick=accountInfo('"+data.compensateNo+"','"+data.registNo+"','"+data.flag+"','"+data.chargeCode+"','"+data.payType+"','"+data.serialNo+"');>"+data.compensateNo+"</a>");
			}else{
				$('td:eq(7)', row).html("");
			}
		}
		//----------未接收   已接收待处理--- end

		//----------已处理--- end

		function search(subNodeCode, handleStatus) {

			nodeCode = subNodeCode;
			var params = {
				"nodeCode" : "PLoss",
				"subNodeCode" : subNodeCode,
				"handleStatus" : handleStatus
			};

			var ajaxList = new AjaxList("#DataTable_" + subNodeCode);
			ajaxList.targetUrl = 'search.do';// + $("#form").serialize();
			ajaxList.postData = params;
			ajaxList.rowCallback = rowCallback;
			if(subNodeCode == 'PLFirst'){
				ajaxList.columns = columns0;
			}else if(subNodeCode == 'PLNext'){
				ajaxList.columns = columns1;
			}else if(subNodeCode == 'AccBack'){
				ajaxList.columns = columns3;
			}else{
				ajaxList.columns = columns2;
			}
			ajaxList.query();
		}
		
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
		$("#aa").click(function(){
			layer.load();
		});
		
		function billsearch(regFlag){
			var goUrl ="/claimcar/bill/compenManagerQueryList.do?regFlag="+regFlag;
			openTaskEditWin("计算书管理",goUrl);
		}
	</script>
</body>
</html>
