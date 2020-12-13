<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
	<tr class="detail-body">
		<td width="10%">
			<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao "   onclick="delPayInfo(this)"></button>
		</td>
		<td>交通强制责任险</td>
		<td><input type="text" class="input-text" placeholder="来源环节自定义标签" /></td>
		<td><input type="text" class="input-text" placeholder="费用名称自定义标签" /></td>
		<td>
			<input type="text" class="input-text" name="" value="" />
		</td>
		<td>
			<input type="text" class="input-text" name="" value="" />
		</td>
		<td>
			<input type="text" class="input-text" name="" value="" />
		</td>
		<td><input type="text" class="input-text" placeholder="收款人下拉？" /></td>
		<c:if test="${flag eq '1' }">
			<td><app:codeSelect codeType="YN10" type="radio" name="" value=""/></td>
			<td><input type="text" class="input-text" placeholder="例外原因自定义标签" /></td>
		</c:if>
		<td>
			<input type="text" class="input-text" name="" value="" />
		</td>
		<td>
			<input type="text" class="input-text" name="" value="" />
		</td>
		<c:if test="${flag eq '2' }">
		<td>
			<input type="text" class="input-text" name="" value="" />
		</td>
		</c:if>
	</tr>
