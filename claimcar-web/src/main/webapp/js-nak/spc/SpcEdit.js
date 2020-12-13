
var editMode=$("#editMode").val();
if(editMode == 'create'){
$(function() {
	var opersTable = $('#inputTable0').DataTable({
		"paging" : false,
		"info" : false,
		"searching" : false,
		"scrollX" : true,
		"scrollY":  400
	});
	$('#inputTable0 tbody').on( 'click', '.btn-delete', function () {
		opersTable.row( $(this).parents('tr')).remove().draw();
	} );
	$('.btn-inputTable0').on( 'click', function () {
		var counter = $("#inputTable0 tbody tr").length;
		var NoCounter = counter + 1;
		opersTable.row.add( [
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].choose"  name="spcEditPanels[0].spcEditInputs[' + counter + '].choose" class="form-control" value="1" type="checkbox" checked>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].serialNo" name="spcEditPanels[0].spcEditInputs[' + counter + '].serialNo" class="form-control" type="text" value="'+NoCounter+'"/>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].inputId" name="spcEditPanels[0].spcEditInputs[' + counter + '].inputId" class="form-control" type="text"/>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].name" name="spcEditPanels[0].spcEditInputs[' + counter + '].name" class="form-control" type="text"/>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].labelText" name="spcEditPanels[0].spcEditInputs[' + counter + '].labelText" class="form-control" type="text"/>',
            '<select id="spcEditPanels[0].spcEditInputs[' + counter + '].inputType" name="spcEditPanels[0].spcEditInputs[' + counter + '].inputType"'
				+'	class="form-control">'
				+'	<option value="input">input</option>'
				+'	<option value="date">date</option>'
				+'	<option value="radio">radio</option>'
				+'	<option value="checkbox">checkbox</option>'
				+'	<option value="select">select</option>'
				+'	<option value="textarea">textarea</option>'
				+'	<option value="codeSelect">codeSelect</option>'
				+'	<option value="zTreeCode">zTreeCode</option>'
				+'</select>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].inputValue" name="spcEditPanels[0].spcEditInputs[' + counter + '].inputValue" class="form-control" type="text"/>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].groupSize" name="spcEditPanels[0].spcEditInputs[' + counter + '].groupSize" class="form-control" type="text" value="4"/>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].labelSize" name="spcEditPanels[0].spcEditInputs[' + counter + '].labelSize" class="form-control" type="text" value="6"/>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].inputSize" name="spcEditPanels[0].spcEditInputs[' + counter + '].inputSize" class="form-control" type="text" value="6"/>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].keysValues" name="spcEditPanels[0].spcEditInputs[' + counter + '].keysValues" class="form-control" type="text"/>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].codeSelectType" name="spcEditPanels[0].spcEditInputs[' + counter + '].codeSelectType" class="form-control" type="text"/>',
            '<input id="spcEditPanels[0].spcEditInputs[' + counter + '].codeSelectCodeType" name="spcEditPanels[0].spcEditInputs[' + counter + '].codeSelectCodeType" class="form-control" type="text"/>',
            '<button type="button" class="btn btn-default glyphicon glyphicon-minus btn-delete">删除</button>'
            ] ).draw();
		
		$('#inputTable0 tbody').on( 'click', '.btn-delete', function () {
			opersTable.row( $(this).parents('tr')).remove().draw();
		} );
    } );
	
	$('.btn-createPanel').on( 'click', function () { 
		var divLength = $("div .newPanel").length;
		var no = divLength + 1;
		var div = '<div class="panel-group newPanel" id="accordion' + divLength + '">'
					+'<div class="panel panel-info">'
					+'	<div class="panel-heading">'
					+'		<h4 class="panel-title">'
					+'<input id="spcEditPanels[' + divLength + '].choose" name="spcEditPanels[' + divLength + '].choose" type="checkbox" value="1" checked/>'
					+'			&nbsp;<a data-toggle="collapse" data-parent="#accordion' + divLength + '"'
					+'				href="#collapse' + divLength + '">panel' + divLength + '</a>'
					+'		</h4>'
					+'	</div>'
					+'	<div id="collapse' + divLength + '" class="panel-collapse collapse in">'
					+'		<div class="panel-body">'
					+'			<div class="row">'
					+'				<div class="col-lg-4 col-md-6 col-xs-12">'
					+'					<div class="row form-group">'
					+'						<div class="col-xs-4">'
					+'							<label for="spcEditPanels[' + divLength + '].serialNo" class="control-label">序号</label>'
					+'						</div>'
					+'						<div class="col-xs-8">'
					+'							<input id="spcEditPanels[' + divLength + '].serialNo" name="spcEditPanels[' + divLength + '].serialNo"'
					+'								value="' + no + '" class="form-control" type="text" />'
					+'						</div>'
					+'					</div>'
					+'				</div>'
					+'				<div class="col-lg-4 col-md-6 col-xs-12">'
					+'					<div class="row form-group">'
					+'						<div class="col-xs-4">'
					+'							<label for="spcEditPanels[' + divLength + '].panelType" class="control-label">panel类型</label>'
					+'						</div>'
					+'						<div class="col-xs-8">'
					+'							<select id="spcEditPanels[' + divLength + '].panelType" name="spcEditPanels[' + divLength + '].panelType"'
					+'								class="form-control">'
					+'								<option value="normal">标准</option>'
					+'								<option value="+-">加减条</option>'
					+'							</select>'
					+'						</div>'
					+'					</div>'
					+'				</div>'
					+'				<div class="col-lg-4 col-md-6 col-xs-12">'
					+'					<div class="row form-group ">'
					+'						<div class="col-xs-4">'
					+'							<label for="spcEditPanels[' + divLength + '].name" class="control-label">panel名称</label>'
					+'						</div>'
					+'						<div class="col-xs-8">'
					+'							<input id="spcEditPanels[' + divLength + '].name" name="spcEditPanels[' + divLength + '].name"'
					+'								class="form-control" type="text"/>'
					+'						</div>'
					+'					</div>'
					+'				</div>'
					+'			</div>'
					+'			<div class="row">'
					+'				<table id="inputTable' + divLength + '" class="display" cellspacing="0" width="100%">'
					+'					<thead>'
					+'						<tr>'
					+'							<th>选择</th>'
					+'							<th>serialNo</th>'
					+'							<th>id</th>'
					+'							<th>name</th>'
					+'							<th>labelText</th>'
					+'							<th>inputType</th>'
					+'							<th>inputValue</th>'
					+'							<th>groupSize</th>'
					+'							<th>labelSize</th>'
					+'							<th>inputSize</th>'
					+'							<th>keysValues</th>'
					+'							<th>codeSelectType</th>'
					+'							<th>codeSelectCodeType</th>'
					+'							<th>删除</th>'
					+'						</tr>'
					+'					</thead>'
					+'					<tbody>'
					+'						<tr>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].choose"  name="spcEditPanels[' + divLength + '].spcEditInputs[0].choose" class="form-control" value="1" type="checkbox" checked></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].serialNo" name="spcEditPanels[' + divLength + '].spcEditInputs[0].serialNo" class="form-control" type="text" value="1"/></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].inputId" name="spcEditPanels[' + divLength + '].spcEditInputs[0].inputId" class="form-control" type="text"/></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].name" name="spcEditPanels[' + divLength + '].spcEditInputs[0].name" class="form-control" type="text"/></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].labelText" name="spcEditPanels[' + divLength + '].spcEditInputs[0].labelText" class="form-control" type="text"/></td>'
					+'							<td>'
					+'								<select id="spcEditPanels[' + divLength + '].spcEditInputs[0].inputType" name="spcEditPanels[' + divLength + '].spcEditInputs[0].inputType"'
					+'									class="form-control">'
					+'									<option value="input">input</option>'
					+'									<option value="date">date</option>'
					+'									<option value="radio">radio</option>'
					+'									<option value="checkbox">checkbox</option>'
					+'									<option value="select">select</option>'
					+'									<option value="textarea">textarea</option>'
					+'									<option value="codeSelect">codeSelect</option>'
					+'									<option value="zTreeCode">zTreeCode</option>'
					+'								</select>'
					+'							</td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].inputValue" name="spcEditPanels[' + divLength + '].spcEditInputs[0].inputValue" class="form-control" type="text"/></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].groupSize" name="spcEditPanels[' + divLength + '].spcEditInputs[0].groupSize" class="form-control" type="text" value="4"/></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].labelSize" name="spcEditPanels[' + divLength + '].spcEditInputs[0].labelSize" class="form-control" type="text" value="6"/></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].inputSize" name="spcEditPanels[' + divLength + '].spcEditInputs[0].inputSize" class="form-control" type="text" value="6"/></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].keysValues" name="spcEditPanels[' + divLength + '].spcEditInputs[0].keysValues" class="form-control" type="text"/></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].codeSelectType" name="spcEditPanels[' + divLength + '].spcEditInputs[0].codeSelectType" class="form-control" type="text"/></td>'
					+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].codeSelectCodeType" name="spcEditPanels[' + divLength + '].spcEditInputs[0].codeSelectCodeType" class="form-control" type="text"/></td>'
					+'							<td><button type="button" class="btn btn-default glyphicon glyphicon-minus btn-delete">删除</button></td>'	
					+'						</tr>'
					+'					</tbody>'
					+'				</table>'
					+'				<button type="button"'
					+'					class="btn btn-default glyphicon glyphicon-plus btn-inputTable' + divLength + '">新增</button>'
					+'			</div>'
					+'		</div>'
					+'	</div>'
					+'</div>'
				+'</div>';
		$(this).before(div);
		
		var opersTablePlus = $('#inputTable' + divLength).DataTable({
			"paging" : false,
			"info" : false,
			"searching" : false,
			"scrollX" : true,
			"scrollY":  400
		});
		$('#inputTable' + divLength + ' tbody').on( 'click', '.btn-delete', function () {
			opersTablePlus.row( $(this).parents('tr')).remove().draw();
		} );
		$('.btn-inputTable' + divLength).on( 'click', function () {
			var counter = $("#inputTable" + divLength + " tbody tr").length;
			var NoCounter = counter + 1;
			console.log(counter);
			opersTablePlus.row.add( [
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].choose"  name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].choose" class="form-control" value="1" type="checkbox" checked>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].serialNo" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].serialNo" class="form-control" type="text"  value="'+NoCounter+'"/>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputId" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputId" class="form-control" type="text"/>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].name" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].name" class="form-control" type="text"/>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].labelText" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].labelText" class="form-control" type="text"/>',
	            '<select id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputType" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputType"'
					+'	class="form-control">'
					+'	<option value="input">input</option>'
					+'	<option value="date">date</option>'
					+'	<option value="radio">radio</option>'
					+'	<option value="checkbox">checkbox</option>'
					+'	<option value="select">select</option>'
					+'	<option value="textarea">textarea</option>'
					+'	<option value="codeSelect">codeSelect</option>'
					+'	<option value="zTreeCode">zTreeCode</option>'
					+'</select>',
				'<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputValue" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputValue" class="form-control" type="text"/>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].groupSize" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].groupSize" class="form-control" type="text" value="4"/>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].labelSize" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].labelSize" class="form-control" type="text" value="6"/>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputSize" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputSize" class="form-control" type="text" value="6"/>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].keysValues" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].keysValues" class="form-control" type="text"/>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].codeSelectType" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].codeSelectType" class="form-control" type="text"/>',
	            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].codeSelectCodeType" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].codeSelectCodeType" class="form-control" type="text"/>',
	            '<button type="button" class="btn btn-default glyphicon glyphicon-minus btn-delete">删除</button>'
	            ] ).draw();
			$('#inputTable' + divLength + ' tbody').on( 'click', '.btn-delete', function () {
				opersTablePlus.row( $(this).parents('tr')).remove().draw();
			} );
	    } );
	} );
	 
	

	
	
	
	
	
	//1.控件初始化
	$(".form_date").datetimepicker({
		format : "yyyy-mm-dd",
		language : "zh-CN",
		autoclose : true,
		todayBtn : true,
		todayHighlight : true,
		showMeridian : false,
		minView : "month",
		pickerPosition : "bottom-left",
	});
	//3.页面校验
	//校验规则
	var rules = {
		pageCode : "required",
	};

	//4.Ajax表单操作相关
	var ajaxEdit = new AjaxEdit("#editForm");
	ajaxEdit.rules = rules;
	ajaxEdit.targetUrl = contextPath + "/spc/saveSpcEdit"; 
	ajaxEdit.afterSuccess=function(){
		history.go(-1);
	}; 
	//绑定表单
	ajaxEdit.bindForm();
	
	$("button.btn-return").click(function() {
		history.go(-1);
	});
});
}



if(editMode != 'create'){
	$(function() {
		var opersTable = $("table[id^='inputTable']").DataTable({
			"paging" : false,
			"info" : false,
			"searching" : false,
			"scrollX" : true,
			"scrollY":  400
		});
		for(var i=0; i<$("div[id^='accordionUpdate']").length; i++){
			$("button.btn-deleteAll").click(function() {
				var selectedIds = getSelectedIds();
				if (selectedIds == "") {
					bootbox.alert("请选择要删除的记录");
					return false;
				}
				var url = contextPath + "/spc/queryBatchdel/" + selectedIds;
				batchDeleteRows("search",url);
			});
			$('#inputTable'+i+' tbody').on( 'click', '.btn-delete', function () {
				var j = $(this).parents('table').attr("id").substr(10,1);
				var fatherObj = $(this).parents('tr');
				var id = $(this).parent().parent().attr("id");
				if (id == "") {
					bootbox.alert("请选择要删除的记录");
					return false;
				}
				var url = contextPath + "/spc/editDelete/" + id;
				bootbox.confirm("确定要删除吗?", function(result) {
					if (result) {
						$.ajax({
							type : "POST",
							url : url,
							data : "",
							async : false,
							success : function(obj) {
								if (obj.status == '200') {
									bootbox.alert("删除成功");
									opersTable.table('#inputTable' + j).row(fatherObj).remove().draw();
								}else if(obj.status == '500'){
									bootbox.alert(obj.statusText);
								}
							},
							error : function(XMLHttpRequest, textStatus, errorThrown) {
								flag = false;
								bootbox.alert(textStatus + errorThrown);
							}
						});
					}
				});
			} );
			$('.btn-inputTable'+i).on( 'click', function () {
				var m = $(this).attr("class").charAt($(this).attr("class").length - 1) ;
				var counter = $("#inputTable"+m+" tbody tr").length ;
				var NoCounter = counter + 1;
				opersTable.table('#inputTable' + m).row.add( [
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].choose"  name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].choose" class="form-control" value="1" type="checkbox" checked>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].serialNo" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].serialNo" class="form-control" type="text"  value="'+NoCounter+'"/>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].inputId" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].inputId" class="form-control" type="text"/>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].name" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].name" class="form-control" type="text"/>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].labelText" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].labelText" class="form-control" type="text"/>',
						'<select id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].inputType" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].inputType"'
							+'	class="form-control">'
							+'	<option value="input">input</option>'
							+'	<option value="date">date</option>'
							+'	<option value="radio">radio</option>'
							+'	<option value="checkbox">checkbox</option>'
							+'	<option value="select">select</option>'
							+'	<option value="textarea">textarea</option>'
							+'	<option value="codeSelect">codeSelect</option>'
							+'	<option value="zTreeCode">zTreeCode</option>'
							+'</select>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].inputValue" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].inputValue" class="form-control" type="text"/>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].groupSize" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].groupSize" class="form-control" type="text" value="4"/>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].labelSize" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].labelSize" class="form-control" type="text" value="6"/>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].inputSize" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].inputSize" class="form-control" type="text" value="6"/>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].keysValues" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].keysValues" class="form-control" type="text"/>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].codeSelectType" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].codeSelectType" class="form-control" type="text"/>',
						'<input id="spcEditPanels['+m+'].spcEditInputs[' + counter + '].codeSelectCodeType" name="spcEditPanels['+m+'].spcEditInputs[' + counter + '].codeSelectCodeType" class="form-control" type="text"/>',
						'<button type="button" class="btn btn-default glyphicon glyphicon-minus btn-delete">删除</button>'
		            ] ).draw();
		    } );
		}
		$('.btn-createPanel').on( 'click', function () { 
			var divLength = $("div .newPanel").length;
			var div = '<div class="panel-group newPanel" id="accordion' + divLength + '">'
						+'<div class="panel panel-info">'
						+'	<div class="panel-heading">'
						+'		<h4 class="panel-title">'
						+'<input id="spcEditPanels[' + divLength + '].choose" name="spcEditPanels[' + divLength + '].choose" type="checkbox" value="1" checked/>'
						+'			&nbsp;<a data-toggle="collapse" data-parent="#accordion' + divLength + '"'
						+'				href="#collapse' + divLength + '">panel' + divLength + '</a>'
						+'		</h4>'
						+'	</div>'
						+'	<div id="collapse' + divLength + '" class="panel-collapse collapse in">'
						+'		<div class="panel-body">'
						+'			<div class="row">'
						+'				<div class="col-lg-4 col-md-6 col-xs-12">'
						+'					<div class="row form-group">'
						+'						<div class="col-xs-4">'
						+'							<label for="spcEditPanels[' + divLength + '].serialNo" class="control-label">序号</label>'
						+'						</div>'
						+'						<div class="col-xs-8">'
						+'							<input id="spcEditPanels[' + divLength + '].serialNo" name="spcEditPanels[' + divLength + '].serialNo"'
						+'								class="form-control" type="text"/>'
						+'						</div>'
						+'					</div>'
						+'				</div>'
						+'				<div class="col-lg-4 col-md-6 col-xs-12">'
						+'					<div class="row form-group">'
						+'						<div class="col-xs-4">'
						+'							<label for="spcEditPanels[' + divLength + '].panelType" class="control-label">panel类型</label>'
						+'						</div>'
						+'						<div class="col-xs-8">'
						+'							<select id="spcEditPanels[' + divLength + '].panelType" name="spcEditPanels[' + divLength + '].panelType"'
						+'								class="form-control">'
						+'								<option value="normal">标准</option>'
						+'								<option value="+-">加减条</option>'
						+'							</select>'
						+'						</div>'
						+'					</div>'
						+'				</div>'
						+'				<div class="col-lg-4 col-md-6 col-xs-12">'
						+'					<div class="row form-group ">'
						+'						<div class="col-xs-4">'
						+'							<label for="spcEditPanels[' + divLength + '].name" class="control-label">panel名称</label>'
						+'						</div>'
						+'						<div class="col-xs-8">'
						+'							<input id="spcEditPanels[' + divLength + '].name" name="spcEditPanels[' + divLength + '].name"'
						+'								class="form-control" type="text"/>'
						+'						</div>'
						+'					</div>'
						+'				</div>'
						+'			</div>'
						+'			<div class="row">'
						+'				<table id="inputTable' + divLength + '" class="display" cellspacing="0" width="100%">'
						+'					<thead>'
						+'						<tr>'
						+'							<th>选择</th>'
						+'							<th>serialNo</th>'
						+'							<th>id</th>'
						+'							<th>name</th>'
						+'							<th>labelText</th>'
						+'							<th>inputType</th>'
						+'							<th>inputValue</th>'
						+'							<th>groupSize</th>'
						+'							<th>labelSize</th>'
						+'							<th>inputSize</th>'
						+'							<th>keysValues</th>'
						+'							<th>codeSelectType</th>'
						+'							<th>codeSelectCodeType</th>'
						+'							<th>删除</th>'
						+'						</tr>'
						+'					</thead>'
						+'					<tbody>'
						+'						<tr>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].choose"  name="spcEditPanels[' + divLength + '].spcEditInputs[0].choose" class="form-control" value="1" type="checkbox" checked></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].serialNo" name="spcEditPanels[' + divLength + '].spcEditInputs[0].serialNo" class="form-control" type="text" value="1"/></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].inputId" name="spcEditPanels[' + divLength + '].spcEditInputs[0].inputId" class="form-control" type="text"/></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].name" name="spcEditPanels[' + divLength + '].spcEditInputs[0].name" class="form-control" type="text"/></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].labelText" name="spcEditPanels[' + divLength + '].spcEditInputs[0].labelText" class="form-control" type="text"/></td>'
						+'							<td>'
						+'								<select id="spcEditPanels[' + divLength + '].spcEditInputs[0].inputType" name="spcEditPanels[' + divLength + '].spcEditInputs[0].inputType"'
						+'									class="form-control">'
						+'									<option value="input">input</option>'
						+'									<option value="date">date</option>'
						+'									<option value="radio">radio</option>'
						+'									<option value="checkbox">checkbox</option>'
						+'									<option value="select">select</option>'
						+'									<option value="textarea">textarea</option>'
						+'									<option value="codeSelect">codeSelect</option>'
						+'									<option value="zTreeCode">zTreeCode</option>'
						+'								</select>'
						+'							</td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].inputValue" name="spcEditPanels[' + divLength + '].spcEditInputs[0].inputValue" class="form-control" type="text"/></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].groupSize" name="spcEditPanels[' + divLength + '].spcEditInputs[0].groupSize" class="form-control" type="text" value="4"/></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].labelSize" name="spcEditPanels[' + divLength + '].spcEditInputs[0].labelSize" class="form-control" type="text" value="6"/></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].inputSize" name="spcEditPanels[' + divLength + '].spcEditInputs[0].inputSize" class="form-control" type="text" value="6"/></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].keysValues" name="spcEditPanels[' + divLength + '].spcEditInputs[0].keysValues" class="form-control" type="text"/></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].codeSelectType" name="spcEditPanels[' + divLength + '].spcEditInputs[0].codeSelectType" class="form-control" type="text"/></td>'
						+'							<td><input id="spcEditPanels[' + divLength + '].spcEditInputs[0].codeSelectCodeType" name="spcEditPanels[' + divLength + '].spcEditInputs[0].codeSelectCodeType" class="form-control" type="text"/></td>'
						+'							<td><button type="button" class="btn btn-default glyphicon glyphicon-minus btn-delete">删除</button></td>'	
						+'						</tr>'
						+'					</tbody>'
						+'				</table>'
						+'				<button type="button"'
						+'					class="btn btn-default glyphicon glyphicon-plus btn-inputTable' + divLength + '">新增</button>'
						+'			</div>'
						+'		</div>'
						+'	</div>'
						+'</div>'
					+'</div>';
			$(this).before(div);
			
			var opersTablePlus = $('#inputTable' + divLength).DataTable({
				"paging" : false,
				"info" : false,
				"searching" : false,
				"scrollX" : true,
				"scrollY":  400
			});
			$('#inputTable' + divLength + ' tbody').on( 'click', '.btn-delete', function () {
				opersTablePlus.row( $(this).parents('tr')).remove().draw();
			} );
			$('.btn-inputTable' + divLength).on( 'click', function () {
				var counter = $("#inputTable" + divLength + " tbody tr").length;
				var NOCounter = counter + 1;
				opersTablePlus.row.add( [
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].choose"  name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].choose" class="form-control" value="1" type="checkbox" checked>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].serialNo" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].serialNo" class="form-control" type="text" value="'+NOCounter+'"/>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputId" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputId" class="form-control" type="text"/>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].name" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].name" class="form-control" type="text"/>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].labelText" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].labelText" class="form-control" type="text"/>',
		            '<select id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputType" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputType"'
						+'	class="form-control">'
						+'	<option value="input">input</option>'
						+'	<option value="date">date</option>'
						+'	<option value="radio">radio</option>'
						+'	<option value="checkbox">checkbox</option>'
						+'	<option value="select">select</option>'
						+'	<option value="textarea">textarea</option>'
						+'	<option value="codeSelect">codeSelect</option>'
						+'	<option value="zTreeCode">zTreeCode</option>'
						+'</select>',
					'<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputValue" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputValue" class="form-control" type="text"/>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].groupSize" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].groupSize" class="form-control" type="text" value="4"/>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].labelSize" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].labelSize" class="form-control" type="text" value="6"/>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputSize" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].inputSize" class="form-control" type="text" value="6"/>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].keysValues" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].keysValues" class="form-control" type="text"/>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].codeSelectType" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].codeSelectType" class="form-control" type="text"/>',
		            '<input id="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].codeSelectCodeType" name="spcEditPanels[' + divLength + '].spcEditInputs[' + counter + '].codeSelectCodeType" class="form-control" type="text"/>',
		            '<button type="button" class="btn btn-default glyphicon glyphicon-minus btn-delete">删除</button>'
		            ] ).draw();
				$('#inputTable' + divLength + ' tbody').on( 'click', '.btn-delete', function () {
					opersTablePlus.row( $(this).parents('tr')).remove().draw();
				} );
		    } );
		} );
		 
		

		
		
		
		
		
		//1.控件初始化
		$(".form_date").datetimepicker({
			format : "yyyy-mm-dd",
			language : "zh-CN",
			autoclose : true,
			todayBtn : true,
			todayHighlight : true,
			showMeridian : false,
			minView : "month",
			pickerPosition : "bottom-left",
		});
		//3.页面校验
		//校验规则
		var rules = {
			pageCode : "required",
		};

		//4.Ajax表单操作相关
		var ajaxEdit = new AjaxEdit("#editForm");
		ajaxEdit.rules = rules;
		ajaxEdit.targetUrl = contextPath + "/spc/saveSpcEdit"; 
		ajaxEdit.afterSuccess=function(){
			history.go(-1);
		}; 
		//绑定表单
		ajaxEdit.bindForm();
		
		$("button.btn-return").click(function() {
			history.go(-1);
		});
	});
}




function deleteRow(r)
{
	var i=r.parentNode.parentNode.rowIndex;
	document.getElementById('inputTable0').deleteRow(i);
}