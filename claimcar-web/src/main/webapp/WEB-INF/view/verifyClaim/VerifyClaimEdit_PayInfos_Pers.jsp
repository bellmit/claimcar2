<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
	<tr class="detail-body">
		<td width="10%">
			<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao " onclick="delPayInfo(this)"></button>
		</td>
		<td><input type="text" class="input-text" placeholder="收款人下拉？" /></td> 
		<td><app:codeSelect codeType="YN10" type="radio" name="" value=""/></td>
		<td><input type="text" class="input-text" placeholder="例外原因自定义标签" /></td>
		<td>
			<input type="text" class="input-text" name="" value="" />
		</td>
		<td>
			<input type="text" class="input-text" name="" value="" />
		</td>
		<td>
			<input type="text" class="input-text" name="" value="" />
	</tr>
