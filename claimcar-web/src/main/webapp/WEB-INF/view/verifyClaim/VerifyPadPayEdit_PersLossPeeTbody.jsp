<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<tr id="${trIndex }">
    <td>医疗费</td>
    <td>交通强制责任险</td>
    <td>
    	<input type="text" class="input-text" name="" value="4500" />
    </td>
    <td>
    	<input type="text" class="input-text" name="" placeholder="收款人选择" />
    </td>
    <td><app:codeSelect codeType="YN10" type="radio" name="" value=""/></td>
    <td><input type="text" class="input-text" placeholder="例外原因自定义标签" /></td>
    <td>
    	<input type="text" class="input-text" name="" value="" />
    </td>
    <td>
    	<input type="text" class="input-text" name="" value="" />
    </td>
</tr>