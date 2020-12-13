<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpLCertifyItemVo"
	items="${prpLCertifyMainVo.prpLCertifyItems}" varStatus="status">
	<input type='hidden' value="1" id="hasCertify">
	<!-- 用来判断是否含有单证 -->
	<div class="row cl">
		<div class="t_title f14 t_close" onclick="openOrCloseTitle(this,${prpLCertifyItemVo.id},'certifyInfo${status.index + size}')">
			<label class="form_label col-4">${prpLCertifyItemVo.certifyTypeName}</label>
			<div class="form_input col-4">
				<input type="hidden" value="${prpLCertifyItemVo.id}"
					name="prpLCertifyItemVos[${status.index + size}].id">
				<app:codeSelect codeType="YNCom" type="radio" clazz="directFlag"
					name="prpLCertifyItemVos[${status.index + size}].directFlag"
					value="${prpLCertifyItemVo.directFlag}" />
			</div>
		</div>
		<div class="formtable" style="display: none">
			<table class="table table-border table-bordered text-c">
				<thead class="text-c">
					<tr>
						<th>单证类型</th>
						<th>是否必传</th>
						<th>是否上传</th>
						<th>上传数量</th>
					</tr>
				</thead>
				<tbody class="text-c" id="certifyInfo${status.index + size}">
					<%-- <c:forEach var="prpLCertifyDirectVo"
						items="${prpLCertifyItemVo.prpLCertifyDirects}" varStatus="status">
						<input type="hidden" value="${prpLCertifyDirectVo.lossItemCode}"
							class="lossItemCode">
					
					</c:forEach> --%>
						<%-- <tr>
							<td width="100px">${prpLCertifyDirectVo.lossItemName}</td>
							<td width="100px"><app:codetrans codeType="YN"
									codeCode="${prpLCertifyDirectVo.mustUpload}" /></td>
							<td width="100px"><app:codetrans codeType="YN10"
									codeCode="${prpLCertifyDirectVo.provideInd}" /></td>
							<td width="100px">${prpLCertifyDirectVo.imgNumber}</td>
						</tr> --%>
				</tbody>
				<c:forEach var="prpLCertifyDirectVo" items="${prpLCertifyItemVo.prpLCertifyDirects}" varStatus="status">
						<input type="hidden" value="${prpLCertifyDirectVo.lossItemCode}" class="lossItemCode"/>
					
                </c:forEach>
			</table>
		</div>
	</div>
</c:forEach>