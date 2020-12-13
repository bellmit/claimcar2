<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div id="MajorcaseOption">
	<div class="formtable ">
		<table class="table table-border">
			<thead class="text-c">
				<tr>
					<th>人员代码</th>
				</tr>
			</thead>
			<tbody class="text-c">
				<tr>
					<td>
						<input type="hidden" name="submitNextVo.assignUser">
						<input type="hidden" name="submitNextVo.assignCom">
						<select class="select col-1" name="submitNextVo.assignUser" >
							<option value="${nextVo.assignUser}">${assignUser}</option>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="layui-layer-btn">
		<a class="layui-layer-btn0" id="submitNextNode">确认提交</a>
	</div>
</div>