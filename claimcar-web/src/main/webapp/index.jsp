﻿<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri ="http://shiro.apache.org/tags" prefix="shiro"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<LINK rel="Bookmark" href="/favicon.ico">
<LINK rel="Shortcut Icon" href="/favicon.ico" />

<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<script type="text/javascript" src="lib/PIE-2.0beta1/PIE_IE678.js"></script>
<![endif]-->
<link href="h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="h-ui/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
<link href="skin/default/skin.css" rel="stylesheet" type="text/css"
	id="skin" />
<link href="lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet"
	type="text/css" />
<link href="css/font.css" rel="stylesheet" />
<!-- menu-left icon -->
<link href="css/font1.css" rel="stylesheet" />
<!-- menu-left icon  end-->
<link href="css/style.css" rel="stylesheet" type="text/css" />

<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->

<title>鼎和保险-理赔系统</title>
</head>
<body>
	<header class="Hui-header cl">
		<a class="Hui-logo l" title="H-ui.admin v2.3" href="#"><img
			src="images/logo.png" /></a>
		<ul class="Hui-userbar">
			<li>${user.userName}</li>
			<li class="dropDown dropDown_hover"><a href="#"
				class="dropDown_A">${user.userCode}<i class="Hui-iconfont">&#xe6d5;</i></a>
				<ul class="dropDown-menu radius box-shadow">
					<li><a href="#">个人信息</a></li>		
					<%-- <li><a href="javascript:void(0)" onclick ="updatePwd('${user.userCode}')"">密码修改</a>	</li> --%>
					<li><a href="javascript:void(0)" onclick ="updateCarID('${user.userCode}')"">身份证修改</a></li>	
					<li><a href="logout.do">退出系统</a></li>
				</ul></li>
			<!-- <li id="Hui-msg"><a href="#" title="消息"><span
					class="badge badge-danger">1</span><i
					class="Hui-iconfont icon-message">&#xe68a;</i>业务通知</a></li> -->
		</ul>
		<a aria-hidden="false" class="Hui-nav-toggle" href="#"></a>
	</header>
	<aside class="Hui-aside">
		<input runat="server" id="divScrollValue" type="hidden" value="" />
		<div class="menu_dropdown bk_2">
			<dl>
				<dt>
					<i class="icon01"></i> 常用功能<i
						class="Hui-iconfont menu_dropdown-arrow"><img
						src="images/menuicon.gif"></i>
				</dt>
				<dd>
					<ul>
						<li><a _href="/claimcar/workbench/showTaskList.do"
							href="javascript:void(0)" class="c"> 工作台</a></li>
							
						<li><a _href="/claimcar/flowQuery/initFlowQuery.do"
							href="javascript:void(0)" class="c">赔案流程查询</a></li>
							<li><a _href="/claimcar/policyQuery/policyList.do"href="javascript:void(0)" class="c">保单查询</a></li>
							<shiro:hasRole name="claim.ilogpower">
							<li><a _href="/claimcar/ilogfinalpower/ilogFinalPowerInfoShow.do"href="javascript:void(0)" class="c">ILOG兜底权限维护</a></li>
						    </shiro:hasRole>
						<shiro:hasPermission name="TEST">
						<li><a _href="blank.jsp" href="javascript:void(0)" class="c">我的任务查询</a></li>
							<li><a _href="workbench.do" href="javascript:void(0)"
								class="c">权限控制的</a></li>
						</shiro:hasPermission>
					</ul>
				</dd>
			</dl>
			<shiro:hasRole name="claim.doubleduty">
			<dl>
			<dt>
					<i class="icon02"></i>双代岗查询<i
						class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
			</dt>
			<dd> 
			   <ul>
			   <li><a _href="/claimcar/dcheck/dcheckList.do"href="javascript:void(0)" class="c">双代岗查询</a></li>
			   </ul>
			</dd>
			</dl>
			</shiro:hasRole>
			
			<!-- 警保事故信息处理菜单 -->
		  	<shiro:hasRole name="calim.accidentinformation">  
			    <dl>
			      <dt>
			         <i class="icon04"></i>警保事故信息处理
					  <i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
			      </dt>
			      <dd>
			         <ul>
			            <li>
			              <a _href="/claimcar/szpolice/szpoliceInfoShow.do" href="javascript:void(0)" class="c">事故信息查询</a>
			            </li>
			         </ul>
			      </dd>
			    </dl>
		  	</shiro:hasRole>   
			
			<shiro:hasAnyRoles name="claim.regist,claim.regist.query">
			<dl>
				<dt>
					<i class="icon03"></i> 报案<i 
						src="images/menuicon.gif"></i>
				</dt>
				<dd>
					<ul>
						<%-- <shiro:hasRole name="claim.regist"> --%>
						<li><a _href="/claimcar/policyQuery/checkList.do"href="javascript:void(0)" class="c">报案登记新增</a></li>
						<%-- </shiro:hasRole> --%>
						
						<shiro:hasRole name="claim.regist.query">
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=Regis"href="javascript:void(0)" class="c">案件查询</a></li>
						<li><a _href="/claimcar/registAdd/registAddQueryList.do"href="javascript:void(0)" class="c">补登报案</a></li>
						<li><a _href="/claimcar/carchild/carchildList.do"href="javascript:void(0)" class="c">案件注销审核</a></li>
						
						</shiro:hasRole>
					</ul>
				</dd>
			</dl>
			</shiro:hasAnyRoles>
			<shiro:hasRole name="claim.schedule">
			<dl>
				<dt>
					<i class="icon04"></i> 调度<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
				</dt>
				<dd>
					<li><a _href="/claimcar/taskQuery/initQuery.do?node=Sched" href="javascript:void(0)" class="c">调度处理</a></li>
					<shiro:hasRole name="claim.schedule.notreceive">
					<li><a _href="/claimcar/taskQuery/initQuery.do?node=SchedUnWork" href="javascript:void(0)" class="c">已调度未接收任务</a></li>
					</shiro:hasRole>
				</dd>
			</dl>
			</shiro:hasRole>
			
			<shiro:hasRole name="claim.check">
			<dl>
				<dt>
					<i class="icon05"></i> 查勘<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
				</dt>
				<dd>
					<li><a _href="/claimcar/taskQuery/initQuery.do?node=Check" href="javascript:void(0)" class="c">查勘处理</a></li>
				</dd>
			</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.updateVIN">
			<dl>
				<dt>
					<i class="icon06"></i> VIN码修改<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
				</dt>
				<dd>
					<li><a _href="/claimcar/updateVIN/initQuery.do" href="javascript:void(0)" class="c">VIN码修改</a></li>
				</dd>
			</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.carloss">
				<dl>
					<dt>
						<i class="icon06"></i> 车辆定损<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<ul>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=DLoss&subNode=DLCar" href="javascript:void(0)" class="c">车辆定损</a></li>
							<li><a _href="/claimcar/carLossAdjust/modifyList" href="javascript:void(0)" class="c">车辆定损修改</a></li>
							<li><a _href="/claimcar/carLossAdjust/additionList" href="javascript:void(0)" class="c">车辆追加定损</a></li>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=DLoss&subNode=CancelDLCar" href="javascript:void(0)" class="c">车辆定损注销</a></li> 
						</ul>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.certainloss">
				<dl>
					<dt>
						<i class="icon07"></i> 财产定损<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<ul>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=DLoss&subNode=DLProp" href="javascript:void(0)" class="c">财产定损</a></li>
							<!-- <li><a _href="/claimcar/proploss/propDeflossQuery" href="javascript:void(0)" class="c">财产定损</a></li> -->
							<li><a _href="/claimcar/propQuery/forModifyList" href="javascript:void(0)" class="c">财产定损修改</a></li>
							<li><a _href="/claimcar/propQuery/forAddList" href="javascript:void(0)" class="c">财产追加定损</a></li>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=DLoss&subNode=CancelDLProp" href="javascript:void(0)" class="c">财产定损注销</a></li> 
						</ul>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.canceltask">
				<dl>
					<dt>
						<i class="icon06"></i> 任务注销<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<ul>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=DLoss&subNode=CDLCar" href="javascript:void(0)" class="c">车辆任务注销</a></li> 
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=DLoss&subNode=CDLProp" href="javascript:void(0)" class="c">财产任务注销</a></li>
						</ul>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.verifyprice">
				<dl>
					<dt>
						<i class="icon08 mr-8"></i>核价<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<ul>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=VPrice&subNode=VPCar" href="javascript:void(0)" class="c">核价任务处理</a></li>
						</ul>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasAnyRoles name="claim.verifycarloss,claim.verifyloss">
				<dl>
					<dt>
						<i class="icon09"></i> 核损<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<ul>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=VLoss&subNode=VLCar" href="javascript:void(0)" class="c">车辆核损任务处理</a></li>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=VLoss&subNode=VLProp" href="javascript:void(0)" class="c">财产核损任务处理</a></li>
						</ul>
					</dd>
				</dl>
			</shiro:hasAnyRoles>
			<shiro:hasRole name="claim.verifyloss.agcta">
				<dl id="menu-system">
					<dt>
						<i class="icon06"></i> 复检<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<ul>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=DLoss&subNode=DLChk" href="javascript:void(0)" class="c">复检处理</a></li>
						</ul>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.certainpersonloss">
				<dl>
					<dt>
						<i class="icon10"></i> 人伤跟踪<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PLoss&subNode=PLFirst" href="javascript:void(0)" class="c">人伤首次跟踪</a></li>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PLoss&subNode=PLNext" href="javascript:void(0)" class="c">人伤后续跟踪</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.auditing">
				<dl>
					<dt>
						<i class="icon11"></i> 人伤审核<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<shiro:hasRole name="claim.auditingsonloss">
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PLoss&subNode=PLVerify" href="javascript:void(0)" class="c">人伤跟踪审核</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.auditingsonlosscost">
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PLoss&subNode=PLCharge" href="javascript:void(0)" class="c">人伤费用审核</a></li>
						<li><a _href="/claimcar/persTraceChargeAdjust/preQueryList.do" href="javascript:void(0)" class="c">费用审核修改</a></li>
						</shiro:hasRole>
						<%-- <shiro:hasRole name="claim.auditingsonlosscost.update">
						</shiro:hasRole> --%>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.claim">
				<dl>
					<dt>
						<i class="icon12"></i> 立案<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=Claim" href="javascript:void(0)" class="c">立案查询</a></li>
						<li><a _href="/claimcar/disaster/initDisaster.do" href="javascript:void(0)" class="c">巨灾补录</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.claim.cancel">
				<dl>
					<dt>
						<i class="icon13"></i> 立案注销拒赔申请<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=Cancel&subNode=CancelApp" href="javascript:void(0)" class="c">注销拒赔申请查询 </a></li>
						<!-- 上海的操作人员不开放恢复的菜单 -->
						<c:if test="${!fn:startsWith(user.userCode,'22')}">
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=Cancel&subNode=ReCanApp" href="javascript:void(0)" class="c">注销恢复申请查询 </a></li>
						</c:if>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.claim.cancel.audit">
				<dl>
					<dt>
						<i class="icon13"></i> 立案注销拒赔审核<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
					 <shiro:hasRole name="claim.claim.cancel.branchaudit.insert">
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=Cancel&subNode=CancelVrf" href="javascript:void(0)" class="c" style="font-size:13px;">注销拒赔分公司审核 </a></li>
					</shiro:hasRole>
					<shiro:hasRole name="claim.claim.cancel.headaudit.insert">
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=Cancel&subNode=CancelLVrf" href="javascript:void(0)" class="c" style="font-size:13px;">注销拒赔总公司审核 </a></li>
					</shiro:hasRole>
					<shiro:hasRole name="claim.claim.cancel.branchrecover.audit">
						<!-- 上海的操作人员不开放恢复的菜单 -->
						<c:if test="${!fn:startsWith(user.userCode,'22')}">
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=Cancel&subNode=ReCanVrf" href="javascript:void(0)" class="c" style="font-size:13px;">立案注销恢复分公司审核</a></li>
						</c:if>
					</shiro:hasRole>
					<shiro:hasRole name="claim.claim.cancel.headrecover.audit">
						<!-- 上海的操作人员不开放恢复的菜单 -->
						<c:if test="${!fn:startsWith(user.userCode,'22')}">
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=Cancel&subNode=ReCanLVrf" href="javascript:void(0)" class="c" style="font-size:13px;">立案注销恢复总公司审核</a></li>
						</c:if>
					</shiro:hasRole>
					</dd>
				</dl>
			</shiro:hasRole>
			
			<shiro:hasRole name="claim.certify">
				<dl>
					<dt>
						<i class="icon15"></i> 单证收集<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=Certi" href="javascript:void(0)" class="c">单证收集任务处理</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.certify.print">
				<dl>
					<dt>
						<i class="icon16"></i> 单证打印<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/certifyPrintSearch/vehicleRegistList.do" href="javascript:void(0)" class="c">机动车辆保险报案记录（代抄单）</a></li>
						<!-- <li><a _href="http://10.0.47.101:7024/prpcar/prpc/copyPrintFormat?policyNo=211010020161206004446" href="javascript:void(0)" class="c">原始保单及出险前批单打印</a></li> -->
						<li><a _href="/claimcar/certifyPrintSearch/policyInfoList.do" href="javascript:void(0)" class="c">原始保单及出险前批单打印</a></li>
						<li><a _href="/claimcar/certifyPrintSearch/verifyLossCarList.do" href="javascript:void(0)" class="c">核损清单</a></li>
						<li><a _href="/claimcar/certifyPrintSearch/lossCarList.do" href="javascript:void(0)" class="c">定损清单</a></li>
						<li><a _href="/claimcar/certifyPrintSearch/checkTaskList.do" href="javascript:void(0)" class="c">查勘报告</a></li>
						<!-- <li><a _href="blank.jsp" href="javascript:void(0)" class="c">理算报告书 </a></li> -->
						<li><a _href="/claimcar/certifyPrintSearch/compensateInfoList.do" href="javascript:void(0)" class="c">赔款理算书</a></li>
						<li><a _href="/claimcar/certifyPrintSearch/compensateInfofuyeList.do" href="javascript:void(0)" class="c">赔款理算书附页</a></li>
						<li><a _href="/claimcar/certifyPrintSearch/compensateInfoNoteList.do" href="javascript:void(0)" class="c">赔款通知书/收据</a></li>
						<li><a _href="/claimcar/certifyPrintSearch/prePadPayQueryList.do" href="javascript:void(0)" class="c">预付(垫付)计算书</a></li>
						<li><a _href="/claimcar/certifyPrintSearch/AMLInfoList.do" href="javascript:void(0)" class="c">反洗钱信息</a></li>
					</dd>
				</dl> 
			</shiro:hasRole>
			<shiro:hasRole name="claim.prepay">
				<dl>
					<dt>
						<i class="icon17 mr-8"></i>预付<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PrePayApply&subNode=PrePayApply" href="javascript:void(0)" class="c">预付任务发起</a></li>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PrePay&subNode=PrePay" class="c">预付任务处理</a></li>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PrePayWfApply" class="c">预付冲销发起</a></li>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PrePayWf" href="javascript:void(0)" class="c">预付冲销处理</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.payment">
				<dl>
					<dt>
						<i class="icon18 mr-8"></i>垫付<i class="Hui-iconfontmenu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=Advance&subNode=AdvLaunch" href="javascript:void(0)" class="c">垫付任务发起</a></li>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PadPay" href="javascript:void(0)" class="c">垫付任务处理</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.compensate">
				<dl>
					<dt>
						<i class="icon19 mr-8"></i>理算<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=Compe" class="c">理算任务查询</a></li>
					<!-- 	<li><a _href="/claimcar/compensate/compensateList.do" href="javascript:void(0)" class="c">理算任务发起</a></li> -->
						<li><a _href="/claimcar/compensate/compeWriteOffLaunQueryList.do" href="javascript:void(0)" class="c">理算冲销发起</a></li>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=CompeWf" href="javascript:void(0)" class="c">理算冲销处理</a></li>
						<li><a _href="/claimcar/accountQueryAction/accountInfoList.do" href="javascript:void(0)" class="c">账号信息修改</a></li>
						<li><a _href="/claimcar/accountQueryAction/returnTticketInfoList.do" href="javascript:void(0)" class="c">自动理算账号修改</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.refund">
				<dl>
					<dt>
						<i class="icon21 mr-8"></i>退票审核<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/accountQueryAction/accountInfoVerifyList.do" href="javascript:void(0)" class="c">账号修改审核</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.replevy">
				<dl>
					<dt>
						<i class="icon20 mr-8"></i>追偿<i class="Hui-iconfontmenu_dropdown-arrow"> <img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=RecPay" href="javascript:void(0)" class="c">追偿任务处理</a></li>
						<li><a _href="/claimcar/recPay/recPayLaunchTaskQueryList.do" href="javascript:void(0)" class="c">追偿任务发起</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.undwrt">
				<dl>
					<dt>
						<i class="icon21 mr-8"></i>核赔<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=VClaim" href="javascript:void(0)" class="c">核赔任务处理</a></li>
						<li><a _href="/claimcar/verifyClaimPass/initList.do" href="javascript:void(0)" class="c">核赔通过清单</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.research">
			<dl>
				<dt>
					<i class="icon21 mr-8"></i>调查<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
				</dt>
				<dd>
					<li><a _href="/claimcar/taskQuery/initQuery.do?node=Survey" href="javascript:void(0)" class="c">调查任务处理</a></li>
				</dd>
			</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.endcase">
				<dl>
					<dt>
						<i class="icon22 mr-8"></i>结案<i class="Hui-iconfontmenu_dropdown-arrow"> <img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=EndCas" href="javascript:void(0)" class="c">结案查询</a></li>
						<!-- <li><a _href="/claimcar/endCase/endCaseEdit.do" href="javascript:void(0)" class="c">结案查询</a></li> -->
						<shiro:hasRole name="claim.recase">
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=ReOpenApp" href="javascript:void(0)" class="c">重开赔案登记处理</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.recase.audit">
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=ReOpen&subNode=ReOpenVrf" href="javascript:void(0)" class="c">重开赔案审核</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.recase.check">
						<li><a _href="/claimcar/reOpen/findReOpenCase.do" " href="javascript:void(0)" class="c">重开赔案查看</a></li>
						</shiro:hasRole>
					</dd>
				</dl>
			</shiro:hasRole>
			<c:if test="${!fn:startsWith(user.userCode,'22')}">
			<shiro:hasRole name="claim.subrogation">
				<dl>
					<dt>
						<i class="icon04"></i> 代位求偿处理<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/subrogationEdit/lockConfirmQueryList.do" href="javascript:void(0)" class="c">锁定确认</a></li>
						<li><a _href="/claimcar/subrogationEdit/lockCancelQueryList.do" href="javascript:void(0)" class="c">锁定取消</a></li>
						<li><a _href="/claimcar/subrogationEdit/recoveryConfirmQueryList.do" href="javascript:void(0)" class="c">开始追偿确认</a></li>
						<li><a _href="/claimcar/subrogationEdit/recoveryBackQueryList.do" href="javascript:void(0)" class="c">追偿回馈确认</a></li>
						<li><a _href="/claimcar/subrogationEdit/subrogationCheckQueryList.do" href="javascript:void(0)" class="c">案件互审</a></li>
						<li><a _href="/claimcar/subrogationEdit/claimRecoveryQueryList.do" href="javascript:void(0)" class="c">待结算金额确认</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.subrogation.query">
				<dl>
					<dt>
						<i class="icon04"></i> 代位求偿查询<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/subrogationQuery/policyQueryList.do" href="javascript:void(0)" class="c">风险预警保单信息</a></li>
						<li><a _href="/claimcar/subrogationQuery/claimQueryList.do" href="javascript:void(0)" class="c">风险预警理赔信息</a></li>
						<li><a _href="/claimcar/subrogationQuery/subrogationQueryList.do" href="javascript:void(0)" class="c">被代位查询</a></li>
						<li><a _href="/claimcar/subrogationQuery/recoveryQueryList.do" href="javascript:void(0)" class="c">结算码查询</a></li>
						<li><a _href="/claimcar/subrogationQuery/claimPaidQueryList.do" href="javascript:void(0)" class="c">代位求偿理赔信息</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			</c:if>
			<c:if test="${fn:startsWith(user.userCode,'22')}">
				<%-- <shiro:hasRole name="claim.subrogation"> --%>
					<dl>
						<dt>
							<i class="icon04"></i> 上海代位求偿处理<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
						</dt>
						<dd>
							<li><a _href="/claimcar/subrogationSH/copyInformationList.do" href="javascript:void(0)" class="c">代位求偿抄回信息</a></li>
							<li><a _href="/claimcar/subrogationEdit/claimRecoveryQueryList.do" href="javascript:void(0)" class="c">待结算金额确认</a></li>
							<li><a _href="/claimcar/subrogationEdit/recoveryBackQueryList.do" href="javascript:void(0)" class="c">追偿回馈确认</a></li>
						</dd>
					</dl>	
				<%-- </shiro:hasRole> --%>
			</c:if>
			<shiro:hasRole name="claim.reclaim">
				<dl id="menu-system">
					<dt>
						<i class="icon-sy"></i> 损余回收<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<ul>
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=RecLoss" href="javascript:void(0)" class="c">损余回收任务查询</a></li>
						</ul>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.bigcase.auditing">
				<dl>
					<dt>
						<i class="icon04"></i> 大案审核<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=ChkBig&subNode=ChkBig" href="javascript:void(0)" class="c">车物大案审核</a></li>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=PLBig&subNode=PLBig" href="javascript:void(0)" class="c">人伤大案审核</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.eqtransfer">
				<dl>
					<dt>
						<i class="icon-py"></i> 平级移交<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=Handover" href="javascript:void(0)" class="c">平级移交</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<shiro:hasRole name="claim.adeqtransfer">
				<dl>
					<dt>
						<i class="icon-py"></i> 超级平级移交<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/taskQuery/initQuery.do?node=SHandover" href="javascript:void(0)" class="c">超级平级移交</a></li>
					</dd>
				</dl>
			</shiro:hasRole>
			<!-- 平台交互 -->
				<dl>
					<dt>
						<i class="icon-py"></i>平台交互<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/platformAlternately/platformQueryList.do" href="javascript:void(0)" class="c">平台交互记录</a></li>
						<li><a _href="/claimcar/platformAlternately/platformTaskQueryList.do" href="javascript:void(0)" class="c">平台自动上传交互记录</a></li>
						<li><a _href="/claimcar/interfaceLog/logQueryList.do" href="javascript:void(0)" class="c">接口交互记录</a></li>
						<li><a _href="/claimcar/policyLink/policyLinkQueryList.do" href="javascript:void(0)" class="c">警保联动查询</a></li>
						<c:if test="${user.userCode eq '0000000000'}">
							<li><a _href="/claimcar/quartz/claimForceFailQueryList.do" href="javascript:void(0)" class="c">强制立案失败案件</a></li>
						</c:if>
						<c:if test="${user.userCode eq '0000000000'}">
							<li><a _href="/claimcar/platformAlternately/platformReloadData.do" href="javascript:void(0)" class="c">平台交互补送</a></li>
						</c:if>
						<shiro:hasRole name="claim.platforminteraction.shdatamonth">
					           <li><a _href="/claimcar/shangHaiData/queryDataView.do" href="javascript:void(0)" class="c">上海月数据统计</a></li>
					    </shiro:hasRole>
					</dd>
				</dl>
				
			<!-- 系统功能-->
			<shiro:hasRole name="claim.systemFunction">
				<dl>
					<dt>
						<i class="icon22"></i>系统功能<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/systemFunction/systemFunctionList.do" href="javascript:void(0)" class="c">案件解锁</a></li>
						
					</dd>
				</dl>
			</shiro:hasRole>
			<!-- 系统功能 -->	
				
			<!-- 平台交互 -->
				<shiro:hasRole name="claim.vacation.manage">
				<dl>
					<dt>
						<i class="icon04"></i> 休假<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/holidayManage/holidayManageList.do" href="javascript:void(0)" class="c">休假申请</a></li>
						<shiro:hasRole name="claim.vacation.auditing">
						<li><a _href="/claimcar/holidayManage/holidayCheckList.do" href="javascript:void(0)" class="c">休假审核</a></li>
						</shiro:hasRole>
					</dd>
				</dl>
				</shiro:hasRole>
			<shiro:hasRole name="claim.maintain.system">
				<dl>
					<dt>      
						<i class="icon22"></i> 系统维护<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<shiro:hasRole name="claim.maintain.checkagency">
							<li><a _href="/claimcar/checkagency/checkagencyList" href="javascript:void(0)" class="c">查勘机构管理</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.maintain.organization">
							<li><a _href="/claimcar/manager/intermediaryList.do" href="javascript:void(0)" class="c">公估机构管理</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.maintain.factory">
							<li><a _href="/claimcar/manager/repairFactoryList.do" href="javascript:void(0)" class="c">修理厂维护</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.maintain.hospital">
							<li><a _href="/claimcar/manager/hospitalList.do" href="javascript:void(0)" class="c">医院查询</a></li>
						</shiro:hasRole>
						<!-- <li><a _href="/claimcar/fourSShop/fourSShopList.do" href="javascript:void(0)" class="c">4S店信息维护</a></li> -->
						<shiro:hasRole name="claim.maintain.notetemplate">
							<li><a _href="/claimcar/msgModel/msgModelList.do" href="javascript:void(0)" class="c">短信模板维护</a></li>
							<li><a _href="/claimcar/msgModel/msgModelQueryList.do" href="javascript:void(0)" class="c">短信查询</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.maintain.emailtemplate">
							<li><a _href="/claimcar/mailModel/mailModelList.do" href="javascript:void(0)" class="c">邮件模板维护</a></li>
							<li><a _href="/claimcar/mailModel/mailModelQueryList.do" href="javascript:void(0)" class="c">邮件查询</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.emailsend">
							<li><a _href="/claimcar/mailModel/senderInfoManageList.do" href="javascript:void(0)" class="c">邮件地址管理</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.maintain.digitalmap">
							<li><a _href="/claimcar/digtalmap/digitalmap.do" href="javascript:void(0)" class="c">电子地图</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.maintain.lawoffice">
							<li><a _href="/claimcar/lawFirm/lawFirmList.do" href="javascript:void(0)" class="c">律师事务所维护</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.leadership">
							<li><a _href="/claimcar/msgModel/leaderInfoManageList.do" href="javascript:void(0)" class="c">领导信息管理</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.disability.identification"></shiro:hasRole>
							<li><a _href="/claimcar/manager/appraisaInforList.do" href="javascript:void(0)" class="c">伤残鉴定机构维护</a></li>
						<c:if test="${user.userCode eq '0000000000'}">
							<li><a _href="/claimcar/compensate/unlockCompensate.do" href="javascript:void(0)" class="c">解锁理算节点</a></li>
						</c:if>
						
						<shiro:hasRole name="claim.pay.standard">
						<li><a _href="/claimcar/payfeeInfo/payfeeInfoList.do" href="javascript:void(0)" class="c">人伤赔偿标准维护</a></li>
						</shiro:hasRole>

						<shiro:hasRole name="claim.autoclaimmsgsender">
							<li><a _href="/claimcar/msgModel/autoclaimmsgsender.do" href="javascript:void(0)" class="c">自助理赔短信发送人维护</a></li>
						</shiro:hasRole>
					</dd>
				</dl>
			</shiro:hasRole>
			<!-- 案均配置 -->
			<shiro:hasRole name="claim.registclaim.configuration">
				<dl>
					<dt>
						<i class="icon03"></i>报即立参数配置 <i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<ul>
							<shiro:hasRole name="claim.registclaim.claimavg">
							<li><a _href="/claimcar/claimAvgList/AvgConfigInit.do" href="javascript:void(0)" class="c">案均配置</a></li>
							</shiro:hasRole>
						</ul>
					</dd>
				</dl>
			</shiro:hasRole>
			<!-- 案均配置 -->
			<!-- 公估费 -->
			<shiro:hasRole name="claim.assessment">
				<dl>
					<dt>
						<i class="icon22"></i>公估费管理<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<shiro:hasRole name="claim.assessment.query">
							<li><a _href="/claimcar/assessors/assessorsFeeQueryList.do" href="javascript:void(0)" class="c">公估费查询</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.assessment.task.query">
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=Interm&subNode=IntermTaskQuery" href="javascript:void(0)" class="c">公估费任务查询</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.assessment.check.query">
							<li><a _href="/claimcar/taskQuery/initQuery.do?node=Interm&subNode=IntermCheckQuery" href="javascript:void(0)" class="c">公估费审核查询</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.assessment.refund.amend">
						 <li><a _href="/claimcar/assessors/assessorFeeBackTicketList.do" href="javascript:void(0)" class="c">公估费退票修改</a></li>
						 </shiro:hasRole>
						 <shiro:hasRole name="claim.assessment.refund.auditing">
						 <li><a _href="/claimcar/assessors/assessorFeeBackTicketAuditList.do" href="javascript:void(0)" class="c">公估费退票审核</a></li>
						 </shiro:hasRole>
						 <shiro:hasRole name="claim.assessment.fee.supplement">
						 	<li><a _href="/claimcar/assessors/assessorFeeSupplementList" href="javascript:void(0)" class="c">公估费补录</a></li>
						 </shiro:hasRole>
					</dd>
				</dl>
			</shiro:hasRole>
			<!-- 查勘费 -->
			<shiro:hasRole name="claim.checkfee">
				<dl>
					<dt>
						<i class="icon22"></i>查勘费管理<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<shiro:hasRole name="claim.checkfee.query">
							<li><a _href="/claimcar/checkfee/checkFeeQueryList.do" href="javascript:void(0)" class="c">查勘费查询</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.checkfee.task.query">
							<li><a _href="/claimcar/checkfee/checkFeeTaskQueryList.do?node=CheckFee&subNode=CheckFeeTaskQuery" href="javascript:void(0)" class="c">查勘费任务查询</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.checkfee.check.query">
							<li><a _href="/claimcar/checkfee/checkFeeTaskAuditQueryList.do?node=CheckFee&subNode=CheckFeeCheckQuery" href="javascript:void(0)" class="c">查勘费审核查询</a></li>
						</shiro:hasRole>
						<shiro:hasRole name="claim.checkfee.refund.amend">
						 <li><a _href="/claimcar/checkfee/checkFeeBackTicketList" href="javascript:void(0)" class="c">查勘费退票修改</a></li>
						 </shiro:hasRole>
						 <shiro:hasRole name="claim.checkfee.refund.auditing">
						 <li><a _href="/claimcar/checkfee/checkFeeBackTicketAuditList" href="javascript:void(0)" class="c">查勘费退票审核</a></li>
						 </shiro:hasRole>
						 <shiro:hasRole name="claim.checkfee.fee.supplement">
						 <li><a _href="/claimcar/checkfee/checkFeeSupplementList" href="javascript:void(0)" class="c">查勘费补录</a></li>
						 </shiro:hasRole>
					</dd>
				</dl>
			</shiro:hasRole>
			<!-- 发票登记 -->
			<shiro:hasRole name="claim.bill.register">
			<dl>
					<dt>
						<i class="icon22"></i>发票管理 <i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					<dd>
						<li><a _href="/claimcar/bill/compenManagerQueryList.do" href="javascript:void(0)" class="c">计算书管理</a></li>
						<li><a _href="/claimcar/bill/billRegisterQueryList.do" href="javascript:void(0)" class="c">发票登记</a></li>
						<!-- <li><a _href="/claimcar/checkfee/checkFeeTaskQueryList.do?node=CheckFee&subNode=CheckFeeTaskQuery" href="javascript:void(0)" class="c">查勘费任务查询</a></li>
						<li><a _href="/claimcar/checkfee/checkFeeTaskAuditQueryList.do?node=CheckFee&subNode=CheckFeeCheckQuery" href="javascript:void(0)" class="c">查勘费审核查询</a></li>
						<li><a _href="/claimcar/checkfee/checkFeeBackTicketList" href="javascript:void(0)" class="c">查勘费退票修改</a></li>
						<li><a _href="/claimcar/checkfee/checkFeeBackTicketAuditList" href="javascript:void(0)" class="c">查勘费退票审核</a></li>
						<li><a _href="/claimcar/checkfee/checkFeeSupplementList" href="javascript:void(0)" class="c">查勘费补录</a></li> -->
					</dd>
			</dl>
			</shiro:hasRole>
			
			
			<!-- 清缓存 -->
		 
		     <c:if test="${user.userCode eq '0000000000'}">
		     <dl>
		            <dt>
						<i class="icon22"></i>清缓存操作<i class="Hui-iconfont menu_dropdown-arrow"><img src="images/menuicon.gif"></i>
					</dt>
					
					<dd>
					<li><a _href="/claimcar/clearCache/allClearCache.do" href="javascript:void(0)" class="c">一键清缓存</a></li>
					
					</dd>
			</dl>
			
			</c:if>
			<!-- 清缓存 -->
		</div><!-- 菜单 end -->
	</aside>
	<div class="dislpayArrow">
		<a class="pngfix" href="javascript:void(0);"
			onClick="displaynavbar(this)"></a>
	</div>
	<section class="Hui-article-box">
		<!--固定浮窗  -->
		<ul class="floatLayBox" style="display:none;">
			<li><a class="acc-and-note">风险</a>
				<div class="risk-note-main">
				<div class="accContent">
				  	风险内容 </br> 风险内容 </br> 风险内容 </br> 风险内容 </br> 风险内容 </br> 风险内容 </br>
				</div>	
				</div>
			</li>
			<li><a class="acc-and-note">备注</a>
				<div class="risk-note-main">
					<div class="noteContent">
						备注内容 </br> 备注内容 </br> 备注内容 </br> 备注内容 </br> 备注内容 </br> 备注内容 </br>
					</div>

				</div>
			</li>
		</ul>
		<!--固定浮窗 end  -->
		<div id="Hui-tabNav" class="Hui-tabNav">
			<div class="Hui-tabNav-wp">
				<ul id="min_title_list" class="acrossTab cl">
					<li class="active"><span title="工作台" data-href="/claimcar/workbench/showTaskList.do">工作台</span><em></em></li>
				</ul>
			</div>
			<div class="Hui-tabNav-more btn-group">
				<a id="js-tabNav-prev" class="btn radius btn-default size-S"
					href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a
					id="js-tabNav-next" class="btn radius btn-default size-S"
					href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a>
			</div>
		</div>
		<div id="iframe_box" class="Hui-article">
			<div class="show_iframe">
				<div style="display: none" class="loading"></div>
				<iframe scrolling="yes" frameborder="0" src="/claimcar/workbench/showTaskList.do"></iframe>
			</div>
		</div>
	</section>

	<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript" src="lib/layer/1.9.3/layer.js"></script>
	<script type="text/javascript" src="h-ui/js/H-ui.js"></script>
	<script type="text/javascript" src="h-ui/js/H-ui.admin.js"></script>
	<script type="text/javascript">
		$(function() {
			$(".Hui-aside>.menu_dropdown>dl>dd>ul>li>a").click(
					function() {
						$(this).parent().siblings().children('a').removeClass(
								"c_menu");
						$(this).addClass("c_menu");
					});
			$('.acc-and-note').on('mouseenter',function(){
				$(this).siblings('.risk-note-main').show();
			});
			$('.acc-and-note').on('mouseleave',function(){
				$(this).siblings('.risk-note-main').hide();
			});
		});
	</script>
	<script type="text/javascript">
		/*资讯-添加*/
		function article_add(title, url) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
		/*图片-添加*/
		function picture_add(title, url) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
		/*产品-添加*/
		function product_add(title, url) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
		/*用户-添加*/
		function member_add(title, url, w, h) {
			layer_show(title, url, w, h);
		}
		
		var open_Once;
		function updatePwd(userCode){
			var url = "/claimcar/updatePwd/initUserInfo.do?userCode="+userCode;
		
		if (open_Once != null) {
				layer.close(open_Once);
			} else {
				open_Once =	layer.open({
					type : 2,
					title : "修改密码",
					shade : false,
					offset : [ '40px', '65%' ],
					area : [ '300px', '200px' ],
					content : [ url, 'no' ],
					end : function() {
						open_Once = null;
					}
				});

			}

		}
		
		var open_index;
		function updateCarID(userCode){
			
			var url = "/claimcar/updatePwd/initCarID.do?userCode="+userCode;
			if(open_index != null){
				layer.close(open_index);
			}else{
				open_index = layer.open({
					type : 2,
					title : "身份证信息修改",
					shade : false,
					offset : [ '40px', '65%' ],
					area : [ '400px', '200px' ],
					content : [ url, 'no' ],
					end : function() {
						open_index = null;
					}
				});
			}
			
		}
	</script>
	<script type="text/javascript">
		
	</script>
</body>
</html>