<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>



		<!-- 行号查询信息页面  开始 -->
		<div class="formtable BankNum mt-10">
			<div class="table_title f14">行号查询</div>
			<div class="table_cont pd-5">
				<div class="row cl">
					<label class="form_label col-2 ">地址【A】： </label> 
					<label class="form_label col-3"><span id="NumProv"></span></label>
					<label class="form_label col-3"><span id="NumCity"></span></label>
				</div>
				<div class="line"></div>
				<div class="row cl">
					<label class="form_label col-2">开户行【B】：</label> 
					<label class="form_label col-3"><span id="NumBankName"></span></label> 
					<span class="c-red col-1"></span>
					<div class="form_input col-2 ">
						<input type="text" class="input-text" id="NumBankQueryT" />
					</div>
					<div class="form_input col-2 ml-15">
						<a class="btn btn-zd mr-10 fl">检索</a>
					</div>
				</div>
			</div>
			<div class="table_title f14">查询结果</div>
			<div class="table_cont">
				<table class="table table-border table-hover" id="BankNumTable">
					<thead class="text-c">
						<tr>
							<th>选择</th>
							<th>行号</th>
							<th>站点名称</th>
						</tr>
					</thead>
					<tbody class="text-c">
						<tr onclick="getBankNumTab(this,'2341234','中国农业银行股份有限公司上海莘庄支行')">
							<td>
								<input type="radio" id="TabChecked" group="BNum" name="BNum"/>
							</td>
							<td>2341234</td>
							<td>中国农业银行股份有限公司上海莘庄支行</td>
						</tr>
						<tr onclick="getBankNumTab(this,'1236549','中国农业银行股份有限公司上海莘庄支行')">
							<td>
								<input type="radio" id="TabChecked" group="BNum" name="BNum"/>
							</td>
							<td>1236549</td>
							<td>中国农业银行股份有限公司上海莘庄支行</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			<div class="btn-footer clearfix text-c">
				<a class="btn btn-primary" id="saveBtn" onclick="layerHiddenBN()">确定</a> <a
					class="btn btn-primary ml-5" id="cancelBtn" onclick="layerHiddenBN()">取消</a>
			</div>
		</div>

		<!-- 行号查询信息页面  结束 -->

	
