<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div id="MajorcaseOption">
	<div class="formtable ">
		<table class="table table-border">
			<thead class="text-c">
				<tr>
					<th>当前环节</th>
					<th>下个环节</th>
					<th>人员选择</th>
				</tr>
			</thead>
			<tbody class="text-c">
				<tr>
					<td>
						<input type="hidden" name="submitNextVo.comCode" value="${nextVo.comCode}" />
						<input type="hidden" name="submitNextVo.currentNode" value="${nextVo.currentNode }">
						<input type="hidden" name="submitNextVo.currentName" value="${nextVo.currentName }">
						<input type="hidden" name="submitNextVo.auditStatus" value="${nextVo.auditStatus }">
						${nextVo.currentName }
					</td>
					<td>
						<input type="hidden" name="submitNextVo.nextNode" value="${nextVo.nextNode }">
						<input type="hidden" name="submitNextVo.nextName" value="${nextVo.nextName }">
						${nextVo.nextName }
					</td>
					<td>
						<input type="hidden" name="submitNextVo.assignUser" value="${nextVo.assignUser }">
						<input type="hidden" name="submitNextVo.assignCom" value="${nextVo.assignCom }">
						<select class="select" name="assignUser" value="${nextVo.assignUser }">
							<option value="${nextVo.assignUser}"><app:codetrans codeType="UserCode" codeCode="${nextVo.assignUser}"/></option>
						</select>
					</td>
				</tr>
				<c:if test="${majorcaseFlag == '1'&& !empty nextVo.otherNodes}">
					<tr>
						<td>
							${nextVo.currentName }
						</td>
						<td>
							<input type="hidden" name="submitNextVo.otherNodes" value="${nextVo.otherNodes }">
							<input type="hidden" name="submitNextVo.otherNodesName" value="${nextVo.otherNodesName }" >
							${nextVo.otherNodesName }
						</td>
						<td>
							<select class="select" name="submitNextVo.assignUser">
								<option value="${nextVo.assignUser}">${nextVo.assignUser}</option>
							</select>
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
	<div class="layui-layer-btn">
		<a class="layui-layer-btn0" id="submitNextNode">确认提交</a>
	</div>
</div>