<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>计算书管理</title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<input type="hidden" name="nodeCode" value="" />
					    <input type="hidden" id="regFlag" value="${regFlag}" />
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c"> 报案号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" datatype="n4-22" ignore="ignore" errormsg="请输入4到22位数" name="vatQueryViewVo.registNo" value="" />
							</div>
							<label class="form-label col-1 text-c">计算书号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="vatQueryViewVo.compensateNo" value="" />
							</div>
							<label class="form-label col-1 text-c">归属机构</label>
							<div class="formControls col-3">
								<span class="select-box"> 
								 <app:codeSelect codeType="ComCodeSelect" type="select" id="comCode"
										name="vatQueryViewVo.comCode" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
						    <label class="form-label col-1 text-c">保单号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.policyNo" value="" />
							</div>
							<label class="form-label col-1 text-c">收款人 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.payName" value="" />
							</div>
							<label class="form-label col-1 text-c">收款人账号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.accountNo" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">待办状态</label>
							<div class="formControls col-3">
								<span class="select-box"> <select name="vatQueryViewVo.workFlag" class=" select ">
										<option value=""></option>
										<option value="0">待办</option>
										<option value="1">已办</option>
								</select>
								</span>
							</div>
							<label class="form-label col-1 text-c">销方名称 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.saleName" value="" />
							</div>
							<label class="form-label col-1 text-c">销方纳税人识别号 </label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.saleNo" value="" />
							</div>
						</div>
                        <div class="row mb-3 cl"> 
							<label class="form-label col-1 text-c">发票号码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.billNo" value="" />
							</div>
							<label class="form-label col-1 text-c">发票代码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.billCode" value="" />
							</div>
							<label class="form-label col-1 text-c">被保险人</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.policyName" value="" />
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-1 text-c">标的车牌号</label>
							<div class="formControls col-3">
								<input type="text" class="input-text" name="vatQueryViewVo.licenseNo" value="" />
							</div>
							<label class="form-label col-1 text-c">报案日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="vatQueryViewVo.reportStartTime"
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="vatQueryViewVo.reportEndTime"
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})"/>
							</div>
							<label class="form-label col-1 text-c">出险日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="vatQueryViewVo.damageStartTime" 
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="vatQueryViewVo.damageEndTime" 
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})"/>
							</div>
						</div>
                        <div class="row mb-3 cl">
                         <label class="form-label col-1 text-c">核损通过时间</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="vatQueryViewVo.underwriteStartDate" 
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="vatQueryViewVo.underwriteEndDate" 
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" />
							</div>
							<label class="form-label col-1 text-c">结案日期</label>
							<div class="formControls col-3">
								<input type="text" class="Wdate" id="tiDateMin" name="vatQueryViewVo.endcaseStartTime" 
									onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"/>
								<span class="datespt">-</span>
								<input type="text" class="Wdate" id="tiDateMax" name="vatQueryViewVo.endcaseEndTime" 
									onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})"/>
                             </div>
						</div>

						<div class="line"></div>
						<div class="row cl text-c">
							<span class="col-offset-8 col-4">
								<button class="btn btn-primary btn-outline btn-search" disabled type="submit">
									<i class="Hui-iconfont  Hui-iconfont-search2"></i> 查询</button>
									&nbsp;&nbsp;
								<button type="reset" class="btn btn-primary" value="reset">重置</button>
							</span>
						</div>
					</form>
				</div>
			</div>
			<br />
			<!-- 查询条件结束 -->

			<!--标签页 开始-->
			<div class="tabbox">
				<div id="tab-system" class="HuiTab">
					<div class="tabCon clearfix">
						<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
							<thead>
								<tr class="text-c">
								    <th>序号</th>
									<th style="width: 14%">报案号</th>
									<th style="width: 14%">计算书号</th>
									<th>业务类型</th>
									<th>费用类型</th>
									<th>归属机构</th>
									<th>核损通过时间</th>
									<th>收款人</th>
									<th>收款人账号</th>
									<th>赔付金额</th>
									<th>已登记金额</th>
									<th>待办状态</th>
									<th>发票影像操作</th>
								</tr>
							</thead>
							<tbody class="text-c">
							</tbody>
						</table>
						<!--table   结束-->
						<div class="row text-c">
							<br />
						</div>
					</div>
				</div>
			</div>
			<!--标签页 结束-->
		</div>
    </div>
		<!-- 此处放页面数据 -->
		<script type="text/javascript" src="/claimcar/plugins/ajaxfileupload/ajaxfileupload.js"></script>
		<script type="text/javascript">
			$(function() {
				$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
				bindValidForm($('#form'), search);
				var regFlag=$("#regFlag").val();
				if(regFlag=='1'){
					$("select[name='vatQueryViewVo.workFlag']").val("0");
					search();
				}
				
			});

			var columns = [
			{
				"data" : "indexNo"
			},{//序号
				
				"data" : "registNo"
			}, //报案号
			{
				"data" : "compensateNo"
			}, //计算书号
			{
				"data" : "bussName"
			}, //业务类型
			{
				"data" : "feeName"
			}, //费用类型
			{
				"data" : "comName"
			}, //机构名称
			{
				"data" : "underwriteDate"
			} ,//核损通过时间
			{
				"data" : "payName" 
			},//收款人
			{
				"data" :"accountNo"
			},//收款人账号
			{
				"data" : "sumAmt"
			} ,//赔付金额
			{
				"data" :"registerNum"
			},//已登记金额
			{
				"data" : "workName"
			},//待办状态
			{
				"data" : null
			}//发票影像操作
			];

			function rowCallback(row, data, displayIndex, displayIndexFull) {
				$('td:eq(2)',row).html("<a   onclick=linkedBillView('"+data.registNo+"','"+data.compensateNo+"','"+data.bussType+"','"+data.feeCode+"','"+data.payId+"','"+data.sumAmt+"','"+data.registerNum+"','"+data.bussId+"')>"+data.compensateNo+"</a>");
				$('td:eq(12)',row).html("<span class='btn-upload'><a href='javascript:void();' class='btn btn-primary radius'>上传</a><input name='file' id='Upfile"+data.indexNo+"' type='file' value='点我上传'  multiple='multiple' onchange=importExcel('"+data.compensateNo+"','"+data.bussType+"','"+data.feeCode+"','"+data.payId+"','"+data.payName+"','"+data.indexNo+"','"+data.bussId+"') class='input-file'/></span>&nbsp;&nbsp;<a href=/claimcar/bill/urlReqQueryParam.do?bussNo="+data.compensateNo+" target='_blank' class='btn btn-primary radius'>查看</a>");
				
			}

			function search() {
				var ajaxList = new AjaxList("#resultDataTable");
				ajaxList.targetUrl = '/claimcar/bill/billSupplymentSerach.do';
				ajaxList.postData = $("#form").serializeJson();
				ajaxList.columns = columns;
				ajaxList.rowCallback = rowCallback;
				ajaxList.query();
			}
            
			//展示对应的关联发票信息
			function linkedBillView(registNo,compensateNo,bussType,feeCode,payId,sumAmt,registerNum,bussId){
				 var goUrl ="/claimcar/bill/compenInfoList.do?registNo="+registNo+"&compensateNo="+compensateNo+"&bussType="+bussType+"&feeCode="+feeCode+"&payId="+payId+"&sumAmt="+sumAmt+"&registerNum="+registerNum+"&bussId="+bussId;
				 openTaskEditWin("计算书(发票)关联信息",goUrl);
			}
			
			

			function importExcel(compensateNo,bussType,feeCode,payId,payName,indexNo,bussId) {
				
				//添加附件start
				var formData = new FormData();
		        for(var i=0;i<$("#Upfile"+indexNo)[0].files.length;i++) {  //循环获取上传个文件
		            formData.append("file", $("#Upfile"+indexNo)[0].files[i]);
		        }
		        formData.append("compensateNo",compensateNo);
		        formData.append("bussType",bussType);
		        formData.append("feeCode",feeCode);
		        formData.append("payId",payId);
		        formData.append("payName",payName);
		        formData.append("bussId",bussId);
		        $.ajax({
		            "url": "/claimcar/bill/importExcel.ajax",
		            "data" : formData,
		            "dataType":"json",
		            "type": "post",
		            "contentType" : false, //上传文件一定要是false
		            "processData":false,
		            "success" : function(json) {
		            	
						if (json.status !='500') {
							layer.msg("上传发票成功！",{icon:6},3000);
						}else{
							layer.msg(json.statusText,{icon:5},3000);
						}
					}
		        });
	           //添加附件end
			}
			
	</script>
</body>
</html>