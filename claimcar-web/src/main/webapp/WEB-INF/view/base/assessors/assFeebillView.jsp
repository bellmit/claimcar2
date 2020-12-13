<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>发票(公估费)登记页面</title>
</head>
<body>
<div class="page_wrap">
<!--查询条件 开始-->
<div class="table_wrap">
<div class="table_cont pd-10">
<div class="formtable f_gray4">
  <table border="1" class="table table-border table-hover data-table">
  <tr>
  <th colspan="12" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;发票信息</font></th>
  </tr>
  <tr style="height: 30px;" class="text-c">
    <th>发票号码</th>
	<th>发票代码</th>
	<th>开票日期</th>
	<th>销方名称</th>
	<th>销方纳税人识别号</th>
	<th>发票不含税金额</th>
	<th>发票税额</th>
	<th>发票税率</th>
	<th>发票价税合计</th>
	<th>验真状态</th>
	<th>推送状态</th>
	<th>打回原因</th>
  </tr>
  <tr class="text-c" style="height: 30px;">
  <td>${vatQueryViewVo.billNo}</td>
  <td>${vatQueryViewVo.billCode}</td>
  <td><fmt:formatDate value='${vatQueryViewVo.billDate}' pattern='yyyy-MM-dd HH:mm'/></td>
  <td>${vatQueryViewVo.saleName}</td>
  <td>${vatQueryViewVo.saleNo}</td>
  <td>${vatQueryViewVo.billNnum}</td>
  <td>${vatQueryViewVo.billSnum}</td>
  <td>${vatQueryViewVo.billSlName}</td>
  <td>${vatQueryViewVo.billNum}</td>
  <td>${vatQueryViewVo.vidflagName}</td>
  <td>${vatQueryViewVo.sendStatusName}</td>
  <td>${vatQueryViewVo.backCauseInfo}</td>
  </tr>
      
  </table>
  </div>
  </div>
       <!--标签页 开始-->
	   <div class="tabbox">
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix">
				    <!--标签页 结束-->
	                <!--table   结束-->
	                <form action="#" id="billregister">
					<table class="table table-border table-hover data-table" id="DataTable_0">
						<thead>
						<tr>
						    <th colspan="10" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;绑定的计算书</font>
						    </th>
						</tr>      
						<tr class="text-c">
							  <th>报案号</th>
							  <th>保单号</th>
							  <th>立案号</th>
							  <th>计算书号</th>
							  <th>险别</th>
							  <th>被保险人</th>
							  <th>已赔付金额</th>
							  <th>任务详情</th>
							  <th>费用金额</th>
							  <th>摘要</th>
						</tr>
						</thead>
					<tbody class="text-c">
					  <c:forEach var="assessorFee" items="${assfeeList}" varStatus="status">
					    <tr>
						<input type="hidden" name="assessorFeeVo1[${status.index}].id" value="${assessorFee.id}"/>
						<td>${assessorFee.registNo }</td>
						<td>${assessorFee.policyNo }
						<c:if test="${!empty assessorFee.policyNoLink}"> </br>${assessorFee.policyNoLink}</c:if>
						</td>
						<td>${assessorFee.claimNo }</td>
						<td>${assessorFee.compensateNo }</td>
						<td><app:codetrans codeType="KindCode" codeCode="${assessorFee.kindCode }"/></td>
						<td>${assessorFee.insurename }</td>
						<c:choose>
						<c:when test="${assessorFee.taskStatus eq '2' }">
						<td>已退票</td>
						</c:when>
						<c:otherwise>
						<td>${assessorFee.payAmount }</td>
						</c:otherwise>
						</c:choose>
						<td>${assessorFee.taskDetail }</td>
						<td>
							${assessorFee.amount}
						</td>
						<td>
						  ${assessorFee.remark}
						</td>
					    </tr>
				      </c:forEach> 
					</tbody>
				  </table>
				  </form>
				</div>
			</div>
			
		</div>
	               
	             
   </div>
   </div>
  <script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script>
  <script type="text/javascript">
  $(function() {
	  $.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
  });
  
  </script>
</body>
</html>