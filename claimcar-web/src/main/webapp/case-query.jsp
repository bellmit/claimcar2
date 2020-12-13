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
				<div class="mb-10 f-14"><strong>请输入报案任务查询条件</strong></div>
				<div class="table-cont pd-10">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label class="form-label col-1"><font class="c-red">*</font>报案号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
							<label class="form-label col-1"><font class="c-red">*</font>保单号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
							<label class="form-label col-1"><font class="c-red">*</font>车牌号码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1"><font class="c-red">*</font>被保险人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
							<label class="form-label col-1"><font class="c-red">*</font>车架号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
							<label class="form-label col-1"><font class="c-red">*</font>发动机号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1">按座席查询</label>
							<div class="formControls col-3">
							  <p>
								  <div class="radio-box">
                                  <input type="radio" id="all" name="zuoxi" checked>
                                  <label for="all">所有</label>
                                </div>
                                <div class="radio-box">
                                  <input type="radio" id="zd" name="zuoxi">
                                  <label for="zd">指定</label>
                                </div>
                                
                                  <span class="select-box" style="width:35%;float:right;">
											<select class="select f_gray4">
												<option>1</option>
												<option>2</option>
											</select>
										</span>
							  </p>
                            </div>
							<label class="form-label col-1">出险时间</label>
							<div class="formControls col-3">
								<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" class="input-text Wdate" style="width:47%">
		-
		<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" class="input-text Wdate" style="width:47%;">
							</div>
							<label class="form-label col-1">联系电话</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"/>
							</div>
						</div>
						
						<div class="row mb-3 cl">
							<label class="form-label col-1">是否现场报案</label>
							<div class="formControls col-3">
								<div class="radio-box">
                                  <input type="radio" id="yes-1" name="baoan" checked>
                                  <label for="yes-1">是</label>
                                </div>
                                <div class="radio-box">
                                  <input type="radio" id="no-1" name="baoan">
                                  <label for="no-1">否</label>
                                </div>
							</div>
							<label class="form-label col-1">报案时间</label>
							<div class="formControls col-3">
								<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" class="input-text Wdate" style="width:47%">
		-
		<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" class="input-text Wdate" style="width:47%;">
							</div>
                            <label class="form-label col-1">出险原因</label>
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
							<label class="form-label col-1">案件状态</label>
							<div class="formControls col-3">
							  <div class="radio-box">
                                  <input type="checkbox" name="zt1" id="zt1">
                                  <label for="zt1">正在处理</label>
                                </div>
                               <div class="radio-box">
                                  <input type="checkbox" name="zt1" id="zt1">
                                  <label for="zt1">已处理</label>
                                </div>
                            </div>
                      </div>
                      <div class="line"></div>
                      <div class="row">
                      	<span class="col-offset-10 col-2">
                        	<p class="fl mt-5"><input name="" type="checkbox" value="">仅查临时案件</p>
                            <a style="margin-left: 10%;" class="btn btn-primary fl">查询</a>
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
                            <span><img src="images/circle3.png"/>正在处理（5）</span>
                            <span><img src="images/circle4.png"/>已处理（5）</span>
						</div>
						<div class="tabCon clearfix">
							<table class="table table-border table-hover" cellpadding="0" cellspacing="0">
								<thead>
									<tr>
										<th>报案号</th>
										<th>保单号</th>
										<th>车牌号</th>
										<th>出险地点</th>
										<th>出险日期</th>
										<th>报案时间</th>
										<th>保单类型</th>
										<th>处理人员</th>
										<th>业务标识</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td><a href="javascript:;" onClick="caseDetails('案件详细','reportquery.html','1')">T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>京AD2121</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
                                        <td>2015-09-28</td>
										<td>商业+交强</td>
										<td>张三</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
									<tr>
										<td><a href="javascript:;" onClick="caseDetails('案件详细','reportquery.html','2')">T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>京AD2121</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
                                        <td>2015-09-28</td>
										<td>商业+交强</td>
										<td>张三</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
                                    <tr>
										<td><a href="javascript:;" onClick="caseDetails('案件详细','reportquery.html','3')">T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>京AD2121</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
                                        <td>2015-09-28</td>
										<td>商业+交强</td>
										<td>张三</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
										</td>
									</tr>
                                    <tr>
										<td><a href="javascript:;" onClick="caseDetails('案件详细','reportquery.html','4')">T400000020111201009224</a></td>
										<td>00002011120100099232</td>
										<td>京AD2121</td>
										<td>深圳福田钢厦部</td>
										<td>2015-09-28</td>
                                        <td>2015-09-28</td>
										<td>商业+交强</td>
										<td>张三</td>
										<td>
											<span class="vip fl">vip</span>
											<span class="hurried fl">急</span>
											<span class="overtime fl">超时</span>
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
