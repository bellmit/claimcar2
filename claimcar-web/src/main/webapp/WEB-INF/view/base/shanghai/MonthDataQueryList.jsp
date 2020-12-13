<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title >理赔上海平台月数据统计查询</title>
<style type='text/css'>	
table {
	border-collapse: inherit
}

th{
	font-size: 14px;
}

.table2 {
	border: 2px solid #91c9f9;
}

.table2 th {
	border-right: 1px solid #91c9f9;
	border-bottom: 1px solid #91c9f9;
	background: #e9f4fe;
}

.table2 td {
    font-size: 14	px;	
	border-right: 1px solid #91c9f9;
}

.table2 tr th:last-child, .table tr th:last-child {
	border-right: 0;
}

.table2 tr th:first-child, .table tr th:first-child {
	border-left: 0;
}

.table2 tr td:last-child, .table tr th:last-child {
	border-right: 0;
}

.table2 tr td:first-child, .table tr th:first-child {
	border-left: 0;
}
</style>
</head>
<body>


	<div class="page_wrap">
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal mt-15" role="form" 
						method="post" action="/claimcar/shangHaiData/shangHaiQuery.do">
						<label class="form-label col-3 text-c ">统计区间:</label>
						<div class="formControls col-4">
							<input type="text" class="Wdate" id="tiDateMin"	name="countStartDate"  datatype="*" 	value="<fmt:formatDate value='${countStartDate}' pattern='yyyy-MM-dd'/>"
								onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'tiDateMax\')||\'%y-%M-%d\'}'})"	/> <span class="datespt">-</span> 
								<input type="text" class="Wdate" id="tiDateMax" name="countEndDate" datatype="*"  value="<fmt:formatDate value='${countEndDate }' pattern='yyyy-MM-dd'/>"
								onfocus="WdatePicker({minDate:'#F{$dp.$D(\'tiDateMin\')}',maxDate:'%y-%M-%d'})" 	/>
						</div>
						<div class="text-c ">
							<input type="button" onclick="search()"
								class="btn btn-primary btn-outline btn-search" id="button"
								disabled value="查询" /> </span><br />
						</div>
					</form>

					<div id="tab-system" class="HuiTab mt-20">
						<div class="clearfix bk-gray">
							<span style="font-size:16px">理赔信息服务系统月数据统计表</span>
							<table class="table2"
								cellpadding="0" cellspacing="0" id="resultDataTable">	
								<thead>
									<tr>
										<th width="40%" colspan="4" class="text-c">公司：鼎和财险</th>
										<th width="20%" colspan="2" class="text-c">申报日期：</th>
										<th width="40%" colspan="5" class="text-c"><fmt:formatDate
												value='${currentTime}' pattern='yyyy-MM-dd' /></th>
									</tr>
									<tr>
										<th width="10%" rowspan="2" class="text-c">时间</th>
										<th width="18%" colspan="2" class="text-c">报案件数</th>	
										<th width="18%" colspan="2" class="text-c">结案件数</th>
										<th width="18%" colspan="2" class="text-c">注销件数</th>
										<th width="18%" colspan="2" class="text-c">零赔款结案件数</th>
										<th width="18%" colspan="2" class="text-c">结案金额</th>
									</tr>
									<tr>
										<th width="9%" class="text-c">交强险</th>
										<th width="9%" class="text-c">商业险</th>
										<th width="9%" class="text-c">交强险</th>
										<th width="9%" class="text-c">商业险</th>
										<th width="9%" class="text-c">交强险</th>
										<th width="9%" class="text-c">商业险</th>
										<th width="9%" class="text-c">交强险</th>
										<th width="9%" class="text-c">商业险</th>
										<th width="9%" class="text-c">交强险</th>
										<th width="9%" class="text-c">商业险</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="text-c"><fmt:formatDate
												value='${countStartDate}' pattern='yyyy-MM-dd' />至<fmt:formatDate
												value='${countEndDate}' pattern='yyyy-MM-dd' /></td>
										<c:forEach items="${resultList}" var="list">
											<td class="text-c">${list}</td>
										</c:forEach>
									</tr>
								</tbody>

							</table>
							<span>备注：<br /> 1、报案件数为当月实际报案数，即报案日期在统计区间的数据；<br /> 2、
								结案件数为当月实际正常结案数（不考虑案件的报案时间）；<br /> 3、
								注销件数为当月实际各类注销的案件件数（不考虑案件的报案时间）；<br /> 4、
								零赔款结案件数为当月实际以零金额作为结案金额的案件件数（不考虑案件的报案时间）；<br /> 5、
								结案金额为对应结案案件的赔款总金额（不考虑案件的报案时间）；<br /> 6、 对于追加的案件，不作为结案数进行统计。
							</span>
						</div>
						<div class="row text-c mt-10" >
							<!-- 导出成一个表格 -->
							<button class="btn btn-primary btn-outline btn-report "
								id="export" onclick="exportExcel()" type="button">导出</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx }/js/common/AjaxList.js"></script>
	<script type="text/javascript"
		src="/claimcar/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
	<script type="text/javascript">
		$(function() {
		});

		var columns = [ {
			"data" : "JQRegistCount"
		}, {
			"data" : "SYRegistCount"
		}, {
			"data" : "JQCaseCount"
		}, {
			"data" : "SYCaseCount"
		}, {
			"data" : "JQApplyCount"
		}, {
			"data" : "SYApplyCount"
		}, {
			"data" : "JQZeroCount"
		}, {
			"data" : "SYZeroCount"
		}, {
			"data" : "JQCaseAmount"
		}, {
			"data" : "SYCaseAmount"
		}, {
			"data" : "requestTime"
		}, {
			"data" : null
		} ];

		function search() {
			if(!check()){
				return false ;
			}
			$('#form').submit();
		}

		function exportExcel() {
		if(!check()){
				return false ;
			}
			window.open("/claimcar/shangHaiData/exportExcel.do?"
					+ $('#form').serialize());
		}
		function check(){
			var flag = true;
			var tiDateMin= $('#tiDateMin').val();
			var tiDateMax= $('#tiDateMax').val();
			if(tiDateMin == "" || tiDateMax == ""){
				layer.alert("日期不能为空");
				flag = false;
			}
			return flag;
		}
	</script>
</body>


</html>