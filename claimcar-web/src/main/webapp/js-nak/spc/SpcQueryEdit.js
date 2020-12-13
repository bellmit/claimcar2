$(function() {
	
	var condsTable = $('#condsTable').DataTable({
		"paging" : false,
		"info" : false,
		"searching" : false,
		"scrollX" : true,
		"scrollY":  400
	});
	$('.btn-condsTable').on( 'click', function () {
		var counter = $("#condsTable tbody tr").length;
		var noCounter = counter + 1;
		condsTable.row.add( [
            '<input name="spcQueryConds['+ counter +'].choose" id="spcQueryConds['+ counter +'].choose" class="form-control" value="1" type="checkbox" checked>',
            '<input id="spcQueryConds['+ counter +'].serialNo" name="spcQueryConds['+ counter +'].serialNo" class="form-control" type="text" value="'+noCounter+'"	/>',
            '<input id="spcQueryConds['+ counter +'].labelText" name="spcQueryConds['+ counter +'].labelText" class="form-control" type="text"/>',
            '<input id="spcQueryConds['+ counter +'].inputId" name="spcQueryConds['+ counter +'].inputId" class="form-control" type="text"/>',
            '<input id="spcQueryConds['+ counter +'].name" name="spcQueryConds['+ counter +'].name" class="form-control" type="text"/>',
            '<input id="spcQueryConds['+ counter +'].placeholder" name="spcQueryConds['+ counter +'].placeholder" class="form-control" type="text"/>',
            '<select id="spcQueryConds['+ counter +'].required" name="spcQueryConds['+ counter +'].required"'
				+'	class="form-control">'
				+'	<option value="false">false</option>'
				+'	<option value="true">true</option>'
				+'</select>',
			'<input id="spcQueryConds['+ counter +'].title" name="spcQueryConds['+ counter +'].title" class="form-control" type="text"/>',
            '<select id="spcQueryConds['+ counter +'].dataType" name="spcQueryConds['+ counter +'].dataType"'
	            + '									class="form-control">'
	            + '		<option value="String">String</option>'
	            + '		<option value="Date">Date</option>'
	            + '</select>',
            '<select id="spcQueryConds['+ counter +'].queryType" name="spcQueryConds['+ counter +'].queryType"'
				+ '									class="form-control">'
				+ '								<option value="equal">equal</option>'
				+ '								<option value="not equal">not equal</option>'
				+ '									<option value="begin with">begin with</option>'
				+ '									<option value="not begin with">not begin with</option>'
				+ '									<option value="end with">end with</option>'
				+ '									<option value="not end with">not end with</option>'
				+ '									<option value="contain"> not contain</option>'
				+ '									<option value=" not contain">Date</option>'
				+ '									<option value="between">between</option>'
				+ '									<option value="not between">not between</option>'
				+ '									<option value="is null">is null</option>'
				+ '									<option value="is not null">is not null</option>'
				+ '									<option value="is in">is in</option>'
				+ '									<option value="not in">not in</option>'
				+ '								</select>',
            '<select id="spcQueryConds['+ counter +'].inputType" name="spcQueryConds['+ counter +'].inputType"'
				+ '									class="form-control">'
				+ '									<option value="input">input</option>'
				+ '									<option value="date">date</option>'
				+ '									<option value="radio">radio</option>'
				+ '									<option value="checkbox">checkbox</option>'
				+ '									<option value="select">select</option>'
				+ '									<option value="codeSelect">codeSelect</option>'
				+ '									<option value="zTreeCode">zTreeCode</option>'
				+ '								</select>',
            '<input id="spcQueryConds['+ counter +'].keysValues" name="spcQueryConds['+ counter +'].keysValues" class="form-control" type="text"/>',
            '<input id="spcQueryConds['+ counter +'].codeSelectType" name="spcQueryConds['+ counter +'].codeSelectType" class="form-control" type="text"/>',
            '<input id="spcQueryConds['+ counter +'].codeSelectCodeType" name="spcQueryConds['+ counter +'].codeSelectCodeType" class="form-control" type="text"/>',
            '<input id="spcQueryConds['+ counter +'].groupSize" name="spcQueryConds['+ counter +'].groupSize" class="form-control" type="text" value="4"/>',
            '<input id="spcQueryConds['+ counter +'].labelSize" name="spcQueryConds['+ counter +'].labelSize" class="form-control" type="text" value="6"/>',
            '<input id="spcQueryConds['+ counter +'].inputSize" name="spcQueryConds['+ counter +'].inputSize" class="form-control" type="text" value="6"/>'
        ] ).draw();
    } );
	var opersTable = $('#opersTable').DataTable({
		"paging" : false,
		"info" : false,
		"searching" : false,
		"scrollX" : true,
		"scrollY":  400
	});
	$('.btn-opersTable').on( 'click', function () {
		var counter = $("#opersTable tbody tr").length;
		controlNO = counter + 1 ;
		opersTable.row.add( [
            '<input name="spcQueryOpers['+ counter +'].choose" id="spcQueryOpers['+ counter +'].choose" class="form-control" value="1" type="checkbox" checked>',
            '<input id="spcQueryOpers['+ counter +'].serialNo" name="spcQueryOpers['+ counter +'].serialNo" class="form-control" type="text" value="'+ controlNO +'"/>',
            '<input id="spcQueryOpers['+ counter +'].name" name="spcQueryOpers['+ counter +'].name" class="form-control" type="text"/>',
            '<input id="spcQueryOpers['+ counter +'].operClass" name="spcQueryOpers['+ counter +'].operClass" class="form-control" type="text"/>',
            '<textarea id="spcQueryOpers['+ counter +'].click" name="spcQueryOpers['+ counter +'].click" class="form-control" type="text"  rows="3" cols="20"></textarea>'
        ] ).draw();
    } );
	var columnsTable = $('#columnsTable').DataTable({
		"paging" : false,
		"info" : false,
		"searching" : false,
		"scrollX" : true,
		"scrollY":  400
	});
	$('.btn-columnsTable').on( 'click', function () {
		var counter = $("#columnsTable tbody tr").length;
		resultNo = counter + 1;
		columnsTable.row.add( [
            '<input name="spcQueryColumns['+ counter +'].choose" id="spcQueryColumns['+ counter +'].choose" class="form-control" value="1" type="checkbox" checked>',
            '<input id="spcQueryColumns['+ counter +'].serialNo" name="spcQueryColumns['+ counter +'].serialNo" class="form-control" type="text" value="'+ resultNo +'"/>',
            '<input id="spcQueryColumns['+ counter +'].title" name="spcQueryColumns['+ counter +'].title" class="form-control" type="text"/>',
            '<input id="spcQueryColumns['+ counter +'].columnData" name="spcQueryColumns['+ counter +'].columnData" class="form-control" type="text"/>',
            '<select id="spcQueryColumns['+ counter +'].align" name="spcQueryColumns['+ counter +'].align"'
				+'									class="form-control">'
				+'									<option value="left">left</option>'
				+'									<option value="center">center</option>'
				+'									<option value="right">right</option>'
				+'								</select>',
	        '<select id="spcQueryColumns['+ counter +'].searchable" name="spcQueryColumns['+ counter +'].searchable"'
				+'									class="form-control">'
				+'									<option value="true">true</option>'
				+'									<option value="false">false</option>'
				+'								</select>',
	        '<select id="spcQueryColumns['+ counter +'].orderable" name="spcQueryColumns['+ counter +'].orderable"'
				+'									class="form-control">'
				+'									<option value="true">true</option>'
				+'									<option value="false">false</option>'
				+'								</select>',
			'<select id="spcQueryColumns['+ counter +'].showType" name="spcQueryColumns['+ counter +'].showType"'
				+'									class="form-control">'
				+'									<option value="true">true</option>'
				+'									<option value="false">false</option>'
				+'								</select>',
            '<textarea id="spcQueryColumns['+ counter +'].render" name="spcQueryColumns['+ counter +'].render" class="form-control" type="text"  rows="3" cols="20"></textarea>'
        ] ).draw();
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
	ajaxEdit.targetUrl = contextPath + "/spcQuery/save"; 
	ajaxEdit.afterSuccess=function(){
		history.go(-1);
	}; 
	//绑定表单
	ajaxEdit.bindForm();
	
	$("button.btn-return").click(function() {
		history.go(-1);
	});
	
	
	
	 $(".collapseThree").bind("click", function () {
		 
         $.ajax({
             type: "post",
             url: contextPath + "/spc/autopreView",
             data: $("#editForm").serialize(),
             async: false, 
             success: function (obj) {
            	 alert(obj.statusText )
            	 $("#preview").html(obj.statusText );
             }
         });
     });
	
	
	
	
});