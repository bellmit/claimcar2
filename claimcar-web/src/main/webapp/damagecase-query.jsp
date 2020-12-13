<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>案件查询</title>
		<!--[if lt IE 9]>
			<script type="text/javascript" src="lib/html5.js"></script>
			<script type="text/javascript" src="lib/respond.min.js"></script>
			<script type="text/javascript" src="lib/PIE_IE678.js"></script>
		<![endif]-->
		<link href="css/H-ui/H-ui.min.css" rel="stylesheet" type="text/css" />
		<link href="css/H-ui/H-ui.admin.css" rel="stylesheet" type="text/css" />
		<link href="lib/icheck/icheck.css" rel="stylesheet" type="text/css" />
		<link href="lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="css/H-ui/workbench.css" />
		<link rel="stylesheet" href="css/H-ui/fee.css" />
		<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>
		<!--[if IE 6]>
			<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
			<script>DD_belatedPNG.fix('*');</script>
		<![endif]-->
	</head>
	<body>
		<!--case-query start-->
		<div class="pd-20 workbench_div">
            <!--查询条件 开始-->
			<div class="bdmessage">
				<div class="mb-10 f-14"><strong>请输入定损任务查询条件</strong></div>
				<div class="table-cont pd-10">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label class="form-label col-1"><font class="c-red">*</font>报案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
							<label class="form-label col-1"><font class="c-red">*</font>车牌号码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
							<label class="form-label col-1"><font class="c-red">*</font>案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box">
									<select class="select">
										<option>1</option>
										<option>1</option>
									</select>
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1"><font class="c-red">*</font>保单号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
							<label class="form-label col-1">被保险人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
							<label class="form-label col-1">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box">
									<select class="select">
										<option>1</option>
										<option>1</option>
									</select>
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1">流入时间</label>
							<div class="formControls col-3">
							  <input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" class="input-text Wdate" style="width:47%">
		-
		<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" class="input-text Wdate" style="width:47%;">
                            </div>
							<label class="form-label col-1">车架号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
							<label class="form-label col-1">任务类型</label>
							<div class="formControls col-3">
								<span class="select-box">
									<select class="select">
										<option>1</option>
										<option>1</option>
									</select>
								</span>
							</div>
						</div>
						
						<div class="row mb-3 cl">
							<label class="form-label col-1">任务状态</label>
							<div class="formControls col-6">
								<div class="radio-box">
                                  <input type="checkbox" id="zt1" name="zt1">
                                  <label for="zt1">未接收</label>
                                </div>
                                <div class="radio-box">
                                  <input type="checkbox" id="zt2" name="zt2">
                                  <label for="zt2">已接收待处理</label>
                                </div>
                                <div class="radio-box">
                                  <input type="checkbox" id="zt3" name="zt3">
                                  <label for="zt3">正在处理</label>
                                </div>
                                <div class="radio-box">
                                  <input type="checkbox" id="zt4" name="zt4">
                                  <label for="zt4">已处理</label>
                                </div>
                                <div class="radio-box">
                                  <input type="checkbox" id="zt5" name="zt5">
                                  <label for="zt5">已退回</label>
                                </div>
							</div>
						</div>
                      <div class="line"></div>
                      <div class="row">
                      	<span class="col-offset-11 col-1">
                            <a class="btn btn-primary fl">查询</a>
                        </span>
                      </div>
					</div>
				</div>
			</div>
			<!--案查询条件 结束-->
        
        
			<!--标签页 开始-->
			<div class="bxmessage">
				<div class="tabbox">
					<div id="tab-system" class="HuiTab">					
						<div class="tabBar f_gray4 cl">
                            <span><img src="images/circle1.png"/>未接收（5）超时（2）</span>
                            <span><img src="images/circle2.png"/>已接收待处理（5）</span>
                            <span><img src="images/circle3.png"/>正在处理（5）</span>
                            <span><img src="images/circle4.png"/>已处理（5）</span>
                            <span><img src="images/circle5.png"/>已退回（5）</span>
						</div>
						<div class="tabCon clearfix">
							<table class="table table-border table-hover" cellpadding="0" cellspacing="0">
								<thead>
									<tr>
										<th>报案号</th>
										<th>保单号</th>
										<th>损失方</th>
										<th>查勘估损金额</th>
										<th>定损金额</th>
										<th>被保险人</th>
										<th>承保机构</th>
										<th>流入时间</th>
										<th>提交人</th>
                                        <th>任务类型</th>
                                        <th>业务标志</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td><a href="javascript:;" onClick="caseDetails('案件详细','carlose.html','1')">T400000020111201009224</a></td>
										<td>0000201112010</td>
										<td>三者车</td>
										<td>300.00</td>
										<td>280.00</td>
                                        <td>张三三</td>
										<td>深圳福田岗厦部</td>
										<td>2015-09-13</td>
                                        <td>张思</td>
                                        <td>追加定损</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
                                    <tr>
										<td><a href="javascript:;" onClick="caseDetails('案件详细','carlose.html','1')">T400000020111201009224</a></td>
										<td>0000201112010</td>
										<td>三者车</td>
										<td>300.00</td>
										<td>280.00</td>
                                        <td>张三三</td>
										<td>深圳福田岗厦部</td>
										<td>2015-09-13</td>
                                        <td>张思</td>
                                        <td>追加定损</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
                                    <tr>
										<td><a href="javascript:;" onClick="caseDetails('案件详细','carlose.html','1')">T400000020111201009224</a></td>
										<td>0000201112010</td>
										<td>三者车</td>
										<td>300.00</td>
										<td>280.00</td>
                                        <td>张三三</td>
										<td>深圳福田岗厦部</td>
										<td>2015-09-13</td>
                                        <td>张思</td>
                                        <td>追加定损</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
										</td>
									</tr>
                                    <tr>
										<td><a href="javascript:;" onClick="caseDetails('案件详细','carlose.html','1')">T400000020111201009224</a></td>
										<td>0000201112010</td>
										<td>三者车</td>
										<td>300.00</td>
										<td>280.00</td>
                                        <td>张三三</td>
										<td>深圳福田岗厦部</td>
										<td>2015-09-13</td>
                                        <td>张思</td>
                                        <td>追加定损</td>
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
								<div class="fl">
									<span>共3页，每页显示</span>
									<select>
										<option>10</option>
										<option>20</option>
										<option>30</option>
									</select>
									<span>条</span>
								</div>
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
						<div class="tabCon">sssss</div>
                        <div class="tabCon">sssss</div>
                        <div class="tabCon">sssss</div>
                        <div class="tabCon">sssss</div>
					</div>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
		<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>
		<script type="text/javascript" src="lib/Validform/5.3.2/Validform.min.js"></script>
		<script type="text/javascript" src="lib/layer/1.9.3/layer.js"></script>
		<script type="text/javascript" src="lib/icheck/jquery.icheck.min.js"></script>
		<script type="text/javascript" src="js/H-ui.js"></script>
		<script type="text/javascript" src="js/H-ui.admin.js"></script>
		<script type="text/javascript" src="lib/My97DatePicker/WdatePicker.js"></script> 
		<script type="text/javascript">
			$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");	
			/*图片-编辑*/
			function caseDetails (title,url,id){
				var index = layer.open({
					type: 2,
					title: title,
					content: url
				});
				layer.full(index);
			}		
		</script>
		<!--workbench_div     结束-->
	</body>
</html>
