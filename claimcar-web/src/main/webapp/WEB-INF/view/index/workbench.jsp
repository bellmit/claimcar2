<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>工作台</title>
				<link  href="${ctx }/css/workbench.css"  rel="stylesheet" type="text/css" />
	</head>
	<body>
		<!--workbench_div     开始-->
		<div class="pd-20 workbench_div">
			<!--标签页bxmessage    开始-->
			<div class="bxmessage">
				<div class="tabbox">
					<div id="tab-system" class="HuiTab">					
						<div class="tabBar f_gray4 cl">
                            <span><i class="Hui-iconfont handun">&#xe619;</i>未接收（21）（超时3）</span>
                            <span><i class="Hui-iconfont handin">&#xe619;</i>已接收待处理（5）</span>
                            <span><i class="Hui-iconfont handing">&#xe619;</i>正在处理（5）</span>
                            <span><i class="Hui-iconfont handback">&#xe619;</i>已退回（5）</span>						
						</div>
						<!--未接收的消息    开始-->
						<div class="tabCon table_list clearfix">
							<table class="table table-border table-hover" cellpadding="0" cellspacing="0">
								<thead>
									<tr>
										<th>节点</th>
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
									<tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
									<tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
									<tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
									<tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
									<tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
									<tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
									<tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="vip fl">vip</span>
										</td>
									</tr>
									<tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="overtime fl">超时</span>
										</td>
									</tr><tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
										</td>
									</tr>
									<tr>
										<td>调度</td>
										<td><a>T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>张三三</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
										<td>2015-09-28</td>
										<td>深圳</td>
										<td>李思思</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
										</td>
									</tr>
								</tbody>								
							</table>
							<!--table   结束-->
							<!--分页开始-->
							<div class="pagination f_gray4 clearfix">
								<div class="fr clearfix">
									<ul class="clearfix">
										<li class="nocurrent">&lt;</li>
										<li class="clis">1</li>
										<li>2</li>
										<li>&gt;</li>
									</ul>
								</div>
							</div>
							<!--分页开始-->
						</div>
						<!--未接收的消息    结束-->
						<div class="tabCon table_list">sssss</div>
						<div class="tabCon table_list">aaaaa</div>
						<div class="tabCon table_list">农</div>
						<div class="tabCon table_list">aaaaa</div>
					</div>
				</div>
			</div>
			<!--标签页bxmessage    结束-->
			<div class="fotter_cont clearfix">
				<!--系统公告   开始-->
				<div class="fotter_notice fl">
					<div class="notice_title clearfix">
						<div class="fl f14"><strong>系统公告</strong></div>
						<a class="fr more f_gray4">查看更多</a>
					</div>
					<div class="noticecont">
						<div class="noticelist clearfix">
							<div class="fl s_img"><img src="images/notice.png"/></div>
							<div class="fl notice">
								<p>系统公告系统公告系统公告系统公告系统公告.</p>
								<p class="f_gray4">2015-09-28</p>
							</div>
						</div>
						<div class="noticelist clearfix">
							<div class="fl s_img"><img src="images/notice.png"/></div>
							<div class="fl notice">
								<p>系统公告系统公告系统公告系统公告系统公告.</p>
								<p class="f_gray4">2015-09-28</p>
							</div>
						</div>
						<div class="noticelist clearfix">
							<div class="fl s_img"><img src="images/notice.png"/></div>
							<div class="fl notice">
								<p>系统公告系统公告系统公告系统公告系统公告.</p>
								<p class="f_gray4">2015-09-28</p>
							</div>
						</div>
					</div>
				</div>
				<!--系统公告   结束-->
				<!--留言通知   开始-->
				<div class="fotter_notice message fl">
					<div class="notice_title clearfix">
						<input type="hidden" name="userCode" value="${user.userCode }" />
						<div class="fl f14"><strong>留言/通知</strong></div>
						<a class="fr more f_gray4">查看更多</a>
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
							<li>
								<img src="images/lccx.png"/>
								<p>流程查询</p>
							</li>
							<li>
								<img src="images/rwcx.png"/>
								<p>任务查询</p>
							</li>
							<li>
								<img src="images/zw.png"/>
								<p>暂无内容</p>
							</li>
						</ul>
					</div>
				</div>
				<!--快捷菜单   结束-->
			</div>
		</div>
		<!--workbench_div     结束-->
		<script type="text/javascript">
		$(function(){
			getSysMsgRece();
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
		</script>
	</body>
</html>
    