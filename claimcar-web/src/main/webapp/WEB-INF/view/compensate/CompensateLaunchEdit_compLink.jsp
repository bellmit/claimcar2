<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_wrap">
	<div class="table_title f14">系统提示</div>
	<div class="table_cont">
		<div class="formtable">
			<div class="row cl">
				<label class="form_label col-2">当前业务号为：</label>
				<div class="form_input col-4">
					${yewuNo }
				</div>
			</div>
			<div class="row cl text-c">
				<div class="form_input col-2">
					<input class="btn btn-primary ml-5" onclick="closeCompLayer()" type="button" value="关闭">
				</div>
			</div>
		</div>
	</div>
	<div class="table_title f14">快速链接专栏</div>
	<div class="table_cont">
		<%-- <c:forEach var="compVo" items="${compMap }" varStatus="status"> --%>
			<div class="row cl">
				<c:if test="${!empty Ci }">
					<label class="form_label col-2">交强计算书</label>
					<div class="form_input col-4">
					${Ci }
					</div>
					<div class="form_input col-2">
						<input class="btn btn-primary ml-5" onclick="compLink('${Ci }')" type="button" value="处理">
					</div>
				</c:if>
				<c:if test="${!empty Bi }">
						<label class="form_label col-2">商业计算书</label>
						<div class="form_input col-4">
							${Bi}
						</div>
						<div class="form_input col-2">
							<input class="btn btn-primary ml-5" onclick="compLink('${Bi }')" type="button" value="处理">
						</div>
				</c:if>
				
			</div>
		<%-- </c:forEach> --%>
	</div>
</div>

