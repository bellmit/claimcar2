<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div class="table_wrap">
<c:if test="${flag eq '1' }">
<div class="table_title f14">交强预付赔款明细</div>
</c:if>
<c:if test="${flag eq '2' }">
<div class="table_title f14">商业预付赔款明细</div>
</c:if>
<div class="table_cont">
	<div class="formtable">
		<div class="table_con">
			<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>损失险别</th>
						<th>预付款项类型</th>
						<th>预付金额</th>
						<th>收款人</th>
						<th>例外标志</th>
						<th>例外原因</th>
						<th>收款方账号</th>
						<th>开户银行</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<tr>
						<td>机动车交通事故责任险</td>
						<td>财产损失</td>
						<td><span>2000</span></td>
						<td>
							<input type="text" class="input-text" placeholder="收款人下拉框" />
						</td>
						<td><app:codeSelect codeType="YN10" type="radio" name="" value=""/></td>
						<td><input type="text" class="input-text" placeholder="例外原因自定义标签" /></td>
						<td><span>1211*********3331</span></td>
						<td>中国工商银行</td>
					</tr>
					<tr>
						<td>机动车交通事故责任险</td>
						<td>死亡伤残</td>
						<td><span>2000</span></td>
						<td>
							<input type="text" class="input-text" placeholder="收款人下拉框" />
						</td>
						<td><app:codeSelect codeType="YN10" type="radio" name="" value=""/></td>
						<td><input type="text" class="input-text" placeholder="例外原因自定义标签" /></td>
						<td><span>1211*********3331</span></td>
						<td>中国工商银行</td>
					</tr>

					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<div class="table_wrap">
<c:if test="${flag eq '1' }">
	<div class="table_title f14">交强预付费用明细</div>
</c:if>
<c:if test="${flag eq '2' }">
	<div class="table_title f14">商业预付费用明细</div>
</c:if>
<div class="table_cont">
	<div class="formtable">
		<div class="table_con">
			<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>损失险别</th>
						<th>费用名称</th>
						<th>费用金额</th>
						<th>收款人</th>
						<th>例外标志</th>
						<th>例外原因</th>
						<th>收款方账号</th>
						<th>开户银行</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<tr>
						<td>机动车交通事故责任险</td>
						<td>诉讼费</td>
						<td><span>2000</span></td>
						<td>
							<input type="text" class="input-text" placeholder="收款人下拉框" />
						</td>
						<td><app:codeSelect codeType="YN10" type="radio" name="" value=""/></td>
						<td><input type="text" class="input-text" placeholder="例外原因自定义标签" /></td>
						<td><span>1211*********3331</span></td>
						<td>中国工商银行</td>
					</tr>
					<tr>
						<td>机动车交通事故责任险</td>
						<td>律师费</td>
						<td><span>2000</span></td>
						<td>
							<input type="text" class="input-text" placeholder="收款人下拉框" />
						</td>
						<td><app:codeSelect codeType="YN10" type="radio" name="" value=""/></td>
						<td><input type="text" class="input-text" placeholder="例外原因自定义标签" /></td>
						<td><span>1211*********3331</span></td>
						<td>中国工商银行</td>
					</tr>

					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

	
	
	
	