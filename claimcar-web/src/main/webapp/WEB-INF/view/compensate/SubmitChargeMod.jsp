<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div>
	<div class="line"></div>
	<div class="formtable ">
		<table class="table table-border">
			<thead class="text-c">
				<tr>
					<th width="20%">当前环节</th>
					<th width="20%">下个环节</th>
					<th width="60%">人员选择</th>
				</tr>
			</thead>
			<tbody class="text-c">
			<tbody class="text-c">
				<tr>
					<td>
						<input type="hidden" name="submitNextVo.comCode" value="${nextVo.comCode}" />
						<input type="hidden" name="submitNextVo.currentNode" value="${nextVo.currentNode }">
						<input type="hidden" name="submitNextVo.currentName" value="${nextVo.currentName }">
						${nextVo.currentName }
					</td>
					<td>
						<input type="hidden" name="submitNextVo.nextNode" value="${nextVo.nextNode }">
						<input type="hidden" name="submitNextVo.nextName" value="${nextVo.nextName }">
						${nextVo.nextName }
					</td>
					<td colspan="2">
						<select class="select" name="submitNextVo.assignCom" style="width: 40%">
							<option value="${nextVo.assignCom}">${nextVo.assignCom}</option>
						</select> <select class="select" name="submitNextVo.assignUser" style="width: 55%">
							<option value="${nextVo.assignUser}">${nextVo.assignUser}</option>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="line"></div>
	<div class="layui-layer-btn text-c">
		<a class="layui-layer-btn0" id="submitNextNode">确认提交</a>
	</div>
</div>