<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>计算书(发票)关系页面</title>
</head>
<body>
<div class="page_wrap">
<!--查询条件 开始-->
<div class="table_wrap">
<div class="table_cont pd-10">
<div class="formtable f_gray4">
  <form action="#" id="billComInfo">
  <input type="hidden" name="vatQueryViewVo.registNo" value="${vatQueryViewVo.registNo}"/>
  <input type="hidden" name="vatQueryViewVo.compensateNo" value="${vatQueryViewVo.compensateNo}"/>
  <input type="hidden" name="vatQueryViewVo.bussType" value="${vatQueryViewVo.bussType}"/>
  <input type="hidden" name="vatQueryViewVo.feeCode" value="${vatQueryViewVo.feeCode}"/>
  <input type="hidden" name="vatQueryViewVo.payId" value="${vatQueryViewVo.payId}"/>
  <input type="hidden" name="vatQueryViewVo.bussId" value="${vatQueryViewVo.bussId}"/>
  <table border="1" class="table table-border table-hover data-table">
  <tr>
  <th colspan="10" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;计算书信息</font></th>
  </tr>
  <tr style="height: 30px;">
  <th class="text-c">报案号</th>
  <th class="text-c">计算书号</th>
  <th class="text-c">赔款类型</th>
  <th class="text-c">费用类型</th>
  <th class="text-c">归属机构</th>
  <th class="text-c">核赔通过时间</th>
  <th class="text-c">收款人</th>
  <th class="text-c">收款人账号</th>
  <th class="text-c">赔付金额</th>
  <th class="text-c">已登记金额</th>
  </tr>
  <tr class="text-c" style="height: 30px;">
  <td>${vatQueryViewVo.registNo}</td>
  <td>${vatQueryViewVo.compensateNo}</td>
  <td>${vatQueryViewVo.bussName}</td>
  <td>${vatQueryViewVo.feeName}</td>
  <td>${vatQueryViewVo.comName}</td>
  <td><fmt:formatDate value='${vatQueryViewVo.underwriteDate}' pattern='yyyy-MM-dd HH:mm'/></td>
  <td>${vatQueryViewVo.payName}</td>
  <td>${vatQueryViewVo.accountNo}</td>
  <td>${vatQueryViewVo.sumAmt}</td>
  <td>${vatQueryViewVo.registerNum}</td>
  </tr>
      
  </table>
  </form>
  </div>
  </div>
       <!--标签页 开始-->
	   <div class="tabbox">
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix">
					<table class="table table-border table-hover data-table"  id="resultDataTable">
						<thead>
						<tr>
						 <th colspan="10" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;发票信息</font></th>
						</tr>
						   
							<tr class="text-c">
							    <th>发票号码</th>
								<th>发票代码</th>
								<th>开票日期</th>
								<th>销方名称</th>
								<th>销方纳税人识别号</th>
								<th>发票不含税金额</th>
								<th>发票税额</th>
								<th>发票税率</th>
								<th>发票价税合计</th>
								<th>登记金额<br>(此计算书上)</th>
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
  <script type="text/javascript">
  $(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
		bindValidForm($('#billComInfo'), search);
		search();
  });
  
  
  
  
		var columns = [
		   			{
		   				"data" : "billNo"
		   			},{//发票号码
		   				
		   				"data" : "billCode"
		   			}, //发票代码
		   			{
		   				"data" : "billDate"
		   			}, //开票日期
		   			{
		   				"data" : "saleName"
		   			}, //销方名称
		   			{
		   				"data" : "saleNo"
		   			}, //销方纳税人识别号
		   			{
		   				"data" : "billNnum"
		   			}, //发票不含税金额
		   			{
		   				"data" : "billSnum"
		   			} ,//发票税额
		   			{
		   				"data" : "billSlName" 
		   			},//发票税率
		   			{
		   				"data" :"billNum"
		   			},//发票价税合计
		   			{
		   				"data" : "registerNum"
		   			} //已登记金额
		   			
		   			];

		   			function rowCallback(row, data, displayIndex, displayIndexFull) {
		   				$('td:eq(0)',row).html("<a   onclick=linkedCompInfoView('"+data.billNo+"','"+data.billCode+"')>"+data.billNo+"</a>");
		   			}

		   			function search() {
		   				var ajaxList = new AjaxList("#resultDataTable");
		   				ajaxList.targetUrl = '/claimcar/bill/vatCompenInfoSerach.do';
		   				ajaxList.postData = $("#billComInfo").serializeJson();
		   				ajaxList.columns = columns;
		   				ajaxList.rowCallback = rowCallback;
		   				ajaxList.query();
		   			}
		               
		   		//展示对应的关联计算书信息
				function linkedCompInfoView(billNo,billCode){
					 var goUrl ="/claimcar/bill/billInfoList.ajax?billNo="+billNo+"&billCode="+billCode;
					 openTaskEditWin("发票(计算书)关联信息",goUrl);
				}
	
  </script>
</body>
</html>