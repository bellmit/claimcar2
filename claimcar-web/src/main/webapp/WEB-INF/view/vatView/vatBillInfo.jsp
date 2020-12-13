<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>发票(计算书)关系页面</title>
</head>
<body>
<div class="page_wrap">
<!--查询条件 开始-->
<div class="table_wrap">
<div class="table_cont pd-10">
<div class="formtable f_gray4">
  <form action="#" id="billComInfo">
  <input type="hidden" name="vatQueryViewVo.billNo" value="${vatQueryViewVo.billNo}"/>
  <input type="hidden" name="vatQueryViewVo.billCode" value="${vatQueryViewVo.billCode}"/>
  <input type="hidden" id="billNum" value="${vatQueryViewVo.billNum}"/>
  <input type="hidden" id="registerStatus" value="${vatQueryViewVo.registerStatus}"/>
  <!-- 请求是否来自发票登录页面 rgisterFlag等于1是，其它为否-->
  <input type="hidden" id="rgisterFlag" value="${rgisterFlag}"/>
  <table border="1" class="table table-border table-hover data-table">
  <tr>
  <th colspan="9" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;发票信息</font></th>
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
  </tr>
      
  </table>
  </form>
  </div>
  </div>
       <!--标签页 开始-->
	   <div class="tabbox">
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix">
				    <form action="#" id="billregister">
					<table class="table table-border table-hover data-table"  id="resultDataTable">
						<thead>
						<tr>
						<c:if test="${rgisterFlag ne '1' }">
						    <th colspan="12" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;计算书信息</font>
						    </th>
						 </c:if>
						 <c:if test="${rgisterFlag eq '1' && vatQueryViewVo.registerStatus ne '1'}">
						    <th colspan="11" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;计算书信息</font>
						    &nbsp;&nbsp;<font size="2" color="red">(温馨提示：请将该发票下您需要登记金额的记录显示在同一页中进行操作)</font>
						    </th>
						    <th colspan="1" style="height: 30px;">
						       <a class="btn btn-primary" onclick="savevat()">提交</a>
						    </th>
						 </c:if>
						</tr>    
							<tr class="text-c">
							  <th class="text-c">序号</th>
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
							  <th class="text-c">本张发票<br>登记金额</th>
							</tr>
						</thead>
						<input type="hidden" name="billId" value="${vatQueryViewVo.billId}"/>
						<!-- 价税合计金额 -->
						<input type="hidden" id="billNum" value="${vatQueryViewVo.billNum}"/>
						
						<tbody class="text-c">
						
						</tbody>
						
						
					</table>
					</form>
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
    <script type="text/javascript" src="/claimcar/lib/Validform/5.3.2/Validform.js"></script>
	<script type="text/javascript" src="/claimcar/lib/layer/v2.1/layer.js"></script>
	<script type="text/javascript" src="/claimcar/lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.js"></script> 
	<script type="text/javascript" src="/claimcar/h-ui/js/H-ui.admin.js"></script>
	<script type="text/javascript" src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
	<script  type="text/javascript" src="/claimcar/lib/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/common.js"></script> 
	<script type="text/javascript" src="/claimcar/js/common/AjaxList.js"></script>
	<script type="text/javascript" src="/claimcar/plugins/qtip/jquery.qtip.js"></script> 
	<script type="text/javascript" src="/claimcar/plugins/select2-3.4.4/select2.js"></script>
	<script type="text/javascript" src="${ctx}/js/base/vatBillInfo.js"></script>
	<script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script>
</body>
</html>