<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>理算任务发起</title>
</head>
<body>
	<!-- 隐藏域 -->
	<input type="hidden" class="input-text" name="endCaseCI" value="${endCaseCI }" />
	<input type="hidden" class="input-text" name="endCaseBI" value="${endCaseBI }" />
	<input type="hidden" class="input-text" name="autoFlag" value="${autoFlag }" />
	<!-- 隐藏域结束 -->
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<div class="top_btn">
				 <a class="btn  btn-primary" onclick="viewEndorseInfo('${registVo.registNo}')">保单批改记录</a>
				 <a class="btn  btn-primary" onclick="viewPolicyInfo('${registVo.registNo}')">保单详细信息</a>
				 <a class="btn  btn-primary"  onclick="feeImageView()">人伤赔偿标准</a>
				  <a class="btn  btn-primary" id="compeRegistMsg">案件备注</a>
			</div>
			<br />
		</div>
		<br />
		<p>
		<div class="table_cont">
			<form id="compLaunForm" role="form" method="post" name="fm">
				<div class="table_wrap">
					<div class="table_title f14">案件信息</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<label class="form_label col-2">报案号</label>
								<div class="form_input col-3">
								${registVo.registNo }
								</div>
								<label class="form_label col-3">案件性质</label>
								<div class="form_input col-3">自赔</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">客户联系电话</label>
								<div class="form_input col-3">${registVo.insuredPhone }</div>
								<label class="form_label col-3">被保险人</label>
								<div class="form_input col-3">${insuredName }</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">报案人联系电话</label>
								<div class="form_input col-3">${registVo.reportorPhone }</div>
							</div>
						</div>
					</div>
				</div>


				<div class="table_wrap">
					<div class="table_title f14">已发起交强险计算书</div>
						<div class="formtable">
							<div class="table_cont">
								<table class="table table-bordered table-bg">
									<thead class="text-c">
										<tr>
											<th>计算书号</th>
											<th>保单号</th>
											<th>计算书类型</th>
											<th>核赔通过时间</th>
											<th>详细信息</th>
										</tr>
									</thead>
									<tbody class="text-c" id=" ">
									<c:forEach var="prpLCompensate" items="${compCIList }" varStatus="">
										<tr>
											<td>${prpLCompensate.compensateNo }</td>
											<td>${prpLCompensate.policyNo }</td>
											<td>交强计算书</td>
											<c:if test="${prpLCompensate.underwriteFlag eq '0' }">
												<td>未核赔通过</td>
											</c:if>
											<c:if test="${prpLCompensate.underwriteFlag eq '1' }">
												<td>${prpLCompensate.underwriteDate }</td>
											</c:if>
											<td><input class="btn" type="button" value="查看详细信息">
											</td>
										</tr>
									</c:forEach>
									</tbody>
								</table>
							</div>
							<p>
							<div class="table_wrap">
								<div class="table_title f14">已发起商业险计算书</div>
								<div class="table_cont">
									<table class="table table-bordered table-bg">
										<thead class="text-c">
											<tr>
												<th>计算书号</th>
												<th>保单号</th>
												<th>计算书类型</th>
												<th>核赔通过时间</th>
												<th>详细信息</th>
											</tr>
										</thead>
										<tbody class="text-c" id=" ">
											<c:forEach var="prpLCompensate" items="${compBIList }" varStatus="">
										<tr>
											<td>${prpLCompensate.compensateNo }</td>
											<td>${prpLCompensate.policyNo }</td>
											<td>商业计算书</td>
											<c:if test="${prpLCompensate.underwriteFlag eq '0' }">
												<td>未核赔通过</td>
											</c:if>
											<c:if test="${prpLCompensate.underwriteFlag eq '1' }">
												<td>${prpLCompensate.underwriteDate }</td>
											</c:if>
											<td>
												<input class="btn" type="button" value="查看详细信息">
											</td>
										</tr>
									</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
				</div>

				<div class="table_wrap">
					<div class="table_title f14">理算任务发起</div>
					<div class="formtable">
					<div class="table_cont">
						<div class="row cl mb-15">
							<div class="col-9 c-blue">
								提示：您可以选择单独发起理算任务，也可以选择【交强商业同时理算】按钮！
							</div>
						</div>
						<div class="row cl ">
							<div class="col-9 ml-10">
								<c:if test="${CIPolicyNo ne 'null' }">
								<input type="radio" name="faqi" value="${CIPolicyNo }" id="CI" class="radio">${CIPolicyNo }（交强）
								</c:if>
								<c:if test="${BIPolicyNo ne 'null' }">
								<input type="radio" name="faqi" value="${BIPolicyNo }" id="BI" class="radio ml-10">${BIPolicyNo }（商业）
								</c:if>
							</div>
						</div>
						<div class="mt-15">
							<input class="btn" type="button" id="submitOne" value="产生理算任务" />
							<input class="btn" type="button" id="submitAll" value="交强商业同时理算" />
							<input class="btn" type="button" id="submitAuto" value="自动产生理算任务" />
					    </div>
					</div>
				</div>
				</div>
				<!-- 隐藏   提交理算后的弹出窗口 -->
				<div style="display:none" id="layerDiv"></div>
				
				<input type="hidden" class="input-text" name="prpLCompensate[0].registNo" value="${registVo.registNo }" />
				<input type="hidden" class="input-text" name="prpLCompensate[0].policyNo"/>
				<input type="hidden" class="input-text" name="prpLCompensate[0].lawsuitFlag"/>
				<input type="hidden" class="input-text" name="prpLCompensate[1].registNo" value="${registVo.registNo }" />
				<input type="hidden" class="input-text" name="prpLCompensate[1].policyNo"/>
				<input type="hidden" class="input-text" name="bcFlag"/>
				

			</form>
			<div style="display:none" id="compLinkDiv"></div>
			<div class="hide mt-10" id="oneDiv">
				<div class="row cl">
					<label class="form_label col-4">人员代码：</label>
					<div class="form_input col-8">
						<select name="handlerUser" type="select">
							<option>1000000000</option>
							<option>1000000001</option>
						</select>
					</div>
				</div>
			</div>
			<div class="hide mt-10" id="allDiv">
				<div class="row cl">
					<label class="form_label col-3">交强人员代码：</label>
					<div class="form_input col-3">
						<select name="prpLCompensate[0].handlerUser" type="select">
							<option>1000000000</option>
							<option>1000000001</option>
						</select>
					</div>
					<label class="form_label col-3">商业人员代码：</label>
					<div class="form_input col-3">
						<select name="prpLCompensate[1].handlerUser" type="select">
							<option>1000000000</option>
							<option>1000000001</option>
						</select>
					</div>
				</div>
			</div>
		</div>
	<!-- 	<div class="text-c mt-10">
		<input class="btn btn-primary ml-5" onclick="" type="button" value="返回">
		</div> -->

	<script type="text/javascript" src="/claimcar/js/compensate/compensateLaunchEdit.js"></script>
	<script type="text/javascript">
		
	</script>
</body>

</html>