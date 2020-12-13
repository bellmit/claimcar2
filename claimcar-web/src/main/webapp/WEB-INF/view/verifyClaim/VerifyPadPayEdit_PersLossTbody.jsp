<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<tr id="${trIndex }">
    <td width="10%">
    	<button type="button" class="btn btn-minus Hui-iconfont Hui-iconfont-jianhao " onclick="delPayInfo(this,'plSize')"></button>
    </td>
    <td>
    	<input type="text" class="input-text" name="" value="张三" />
    </td>
    <td>第三者人员伤亡<font class="must">*</font></td>
    <td><input type="text" class="input-text" placeholder="伤情类型自定义标签" /></td>
    <!--<app:codeSelect codeType=" " type="select" name="" value=""/>-->
    <td><input type="text" class="input-text" name="" value="23" /></td>
    <td>
    	<app:codeSelect codeType="SexCode" type="select" name="" value=""/>
    </td>
    <td><input type="text" class="input-text" name="" value=" " /></td>
    <td><input type="text" class="input-text" name="" value=" " /></td>
    <td>
    	<input type="text" class="input-text" placeholder="医疗机构自定义标签组" />
    </td>
    <td><input type="text" class="input-text" name="" value="" /></td>
</tr>