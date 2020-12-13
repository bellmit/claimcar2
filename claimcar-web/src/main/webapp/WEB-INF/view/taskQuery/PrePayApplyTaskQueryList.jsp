<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>预付任务发起查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet"  />
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
								<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="claimVo.registNo" value=""
									onfocus="radioChecked(this)" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
							<label class="form-label col-1 text-c"> 保单号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="claimVo.policyNo" value=""
									onfocus="radioChecked(this)" datatype="n4-21" ignore="ignore" errormsg="请输入4到21位数"/>
							</div>
							<label class="form-label col-1 text-c">  立案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="claimVo.claimNo" value=""
									onfocus="radioChecked(this)" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数"/>
							</div>
						</div>
						
						<div class="row mb-3 cl">
						<label class="form-label col-1 text-c"> 车牌号码
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="wfTaskQueryVo.licenseNo" value=""
									onfocus="radioChecked(this)" datatype="carStrLicenseNo" ignore="ignore" />
							</div>
							
							<label class="form-label col-1 text-c">险种代码</label>
							<div class="formControls col-3">
								<span class="select-box"> 
								 <app:codeSelect codeType="RiskCode" type="select" id="riskCode" 
										name="claimVo.riskCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c"> 被保险人名称
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="wfTaskQueryVo.insuredName" value=""
									onfocus="radioChecked(this)" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="ComCodeSelect" type="select" id="comCode" 
										name="claimVo.comCode" lableType="code-name" />
								</span>
							</div>
							<label class="form-label col-1 text-c">案件紧急程度</label>
							<div class="formControls col-3">
								<span class="select-box"> <app:codeSelect
										codeType="MercyFlag" type="select" name="claimVo.mercyFlag"
										lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="line"></div>
						<br/><br/>
						<div class="row">
							<span class="col-offset-10 col-2">
							<button class="btn btn-primary btn-outline btn-search"  type="submit" disabled>
								<i class="Hui-iconfont  Hui-iconfont-search2"></i>  查询</button>
							<!-- <button class="btn btn-primary">
									查询
								</button> -->
							</span><br />
						</div>
						<br/>
					</form>
				</div>
			</div>
			<!--案查询条件 结束-->

			<!--标签页 开始-->
			<div id="tab-system" class="HuiTab">
				<div class="tabBar cl"> 
					<span onclick="changeHandleTab(2)"><i class="Hui-iconfont handing">&#xe619;</i>待申请任务</span> 
				</div>
				<!--正在处理-->
				<div class="tabCon clearfix">
					<table id="DataTable_Pay" class="table table-border table-hover data-table" cellpadding="0" cellspacing="0"  >
						<thead>
							<tr class="text-c">
								<th>紧急程度</th>
								<th>报案号</th>
								<th>被保险人</th>
								<th>提交人</th>
							</tr>
						</thead>
						<tbody class="text-c">
						</tbody>
					</table>
				</div>
			</div>
			<!--标签页 结束-->
			
		</div>
	</div>
<script type="text/javascript" src="${ctx }/js/flow/flowCommon.js"></script>

	<script type="text/javascript">
 

	var columns = [ 
		{"data" : "mercyFlagName"},
		{"data" : "registNo"}, //报案号
		{"data" : "insuredName"}, //被保险人
		{"data" : "createUserName"} //提交人
	];
	function rowCallback(row, data, displayIndex, displayIndexFull) {
		var url = "/claimcar/prePay/prePayApply.do?registNo=" + data.registNo;
		$('td:eq(1)', row).html("<a  onclick='checkSubRogation(\""+data.isSubRogation+"\",\""+url+"\");')>"+data.registNo+"</a>");
	}
	function search(){
		var ajaxList = new AjaxList("#DataTable_Pay");
		ajaxList.targetUrl = '/claimcar/padpay/prePayFinds.do';
		ajaxList.postData = $("#form").serializeJson();
		ajaxList.columns = columns;
		ajaxList.rowCallback = rowCallback;
		ajaxList.query();
}
	function checkSubRogation(isSubRogation,url){
		if(isSubRogation=="1"){
			layer.msg("该案件为代位求偿案件，不能发起预付任务!");
			return false;
		}else{
			openTaskEditWin("预付任务发起",url);
		}
	}

	</script>
</body>
</html>
