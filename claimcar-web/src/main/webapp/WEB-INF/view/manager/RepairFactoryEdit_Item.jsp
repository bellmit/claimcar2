<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<tr class="text-c">
		<input type="hidden" name="repairBrandVo[${Idx}].id" value="${repairBrandVo.id}"/>
		<td><%-- <span class="select-box">
			<<app:codeSelect codeType="BrandCode" type="select" lableType="code-name"
					onchange="setSelectCode(this,'${Idx}')"
					name=""
					value="${repairBrandVo.brandCode}" nullToVal="" />
			</span> --%>
			<input type="text" class="input-text" name="repairBrandVo[${Idx}].brandName"
				   value="${repairBrandVo.brandName}" readonly/>
		</td>
		<td>
			<input type="text" class="input-text" name="repairBrandVo[${Idx}].brandCode"
					value="${repairBrandVo.brandCode}" readonly/>
		</td>
		<td>
			<input type="text" class="input-text" name="repairBrandVo[${Idx}].compRate"
					value="${repairBrandVo.compRate}" datatype="rate"/>
		</td>
		<td>
			<input type="text" class="input-text" name="repairBrandVo[${Idx}].hourRate"
					value="${repairBrandVo.hourRate}" datatype="rate"/>
		</td>
		<td>
			<button type="button"
				class="btn btn-plus Hui-iconfont Hui-iconfont-jianhao"
				onclick="delRepairTr(this)" name="delRepairTr_${Idx}">
			</button>
		</td>
</tr>
