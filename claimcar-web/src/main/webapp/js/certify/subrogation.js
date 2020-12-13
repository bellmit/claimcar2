$(function(){
	var subrogationFlag = $("#subrogationFlag").val();
	var sourceFlag = $("#sourceFlag").val();
	if(subrogationFlag == '1' && sourceFlag != '1'){
		$("input:radio[name='subrogationMain.subrogationFlag']").prop("disabled",true);
		$("#subRogationDiV").find("input").prop("readOnly",true);
		//$("#subRogationDiV").find("input").prop("disabled",true);
		$("#subRogationDiV").find(".btn").hide();
		$("#subRogationDiV").find("select").prop("disabled",true);
	}
});
/**代位求偿*/
$("input:radio[name='subrogationMain.subrogationFlag']").click(function() {
	var sub_div = $("#subRogationDiV");
	var val = $(this).val();
	if (val == "1") {
		sub_div.show();
		//责任对方内容（责任方为机动车）
		//姓名/名称
		$("#subRogationDiV").find("[name$='linkerName']").attr("datatype","*");
		//车辆VIN码
		$("#subRogationDiV").find("[name$='vinNo']").attr("datatype","vinNo");
		//发动机号
		$("#subRogationDiV").find("[name$='engineNo']").attr("datatype","letterAndNumber");
		//责任对方内容（责任方为非机动车）的姓名/名称
		$("#subrogationPerTbody").find("[name$='name']").attr("datatype","*");
	} else {
		sub_div.hide();
		//责任对方内容（责任方为机动车）
		//姓名/名称
		$("#subRogationDiV").find("[name$='linkerName']").removeAttr("datatype");
		$("#subRogationDiV").find("[name$='linkerName']").removeClass("Validform_error");
		$("#subRogationDiV").find("[name$='linkerName']").qtip('destroy',true);
		//车辆VIN码
		$("#subRogationDiV").find("[name$='vinNo']").removeAttr("datatype");
		$("#subRogationDiV").find("[name$='vinNo']").removeClass("Validform_error");
		$("#subRogationDiV").find("[name$='vinNo']").qtip('destroy',true);
		//发动机号
		$("#subRogationDiV").find("[name$='engineNo']").removeAttr("datatype");
		$("#subRogationDiV").find("[name$='engineNo']").removeClass("Validform_error");
		$("#subRogationDiV").find("[name$='engineNo']").qtip('destroy',true);
		
		//责任对方内容（责任方为非机动车）的姓名/名称
		$("#subRogationDiV").find("[name$='name']").removeAttr("datatype");
		$("#subRogationDiV").find("[name$='name']").removeClass("Validform_error");
		$("#subRogationDiV").find("[name$='name']").qtip('destroy',true);
	}
});

function initSubrogationCar(){//subrogationPersTbody
	var tbody = $("#subrogationCarTable");
	var carSize =$("#subrogationCarSize") ;
	var size = parseInt(carSize.val(),10);
	var registNo =$("#registNo").val() ;// 
	
	var params = {"size":size,"registNo":registNo};
	var url = "/claimcar/certify/loadSubrationCar.ajax";
	$.post(url,params, function(result){
		tbody.append(result);
		reloadRow2(size);
		
		carSize.val(size + 1 );//
	});
}

function reloadRow2 (size){
	var div ="#subrogationCarTbody_"+size+" .select2";
	//alert(div);
	$(div).select2({
		
	});
}

function delSubrogationCar(element){
	var index = $(element).attr("name").split("_")[1];//下标
	var proposalPrefix="subrogationMain.prpLSubrogationCars";
	var parentTr = $(element).parent().parent();
	var carSize =$("#subrogationCarSize") ;//
	var carSizeInt = parseInt(carSize.val(),10);//原附加险条数
	carSize.val(carSizeInt-1);//删除一条
	
	delTr(carSizeInt, index, "subrogationCar_", proposalPrefix);
	for(var i=0;i<2;i++){
		var parentTr1 = parentTr.next();
		parentTr1.remove();
	}
	parentTr.remove();
	//删除tbody
	$("#subrogationCarTbody_"+index).remove();
	
	//调整序号
	var items = $("button[name^='subrogationCar_']");
	items.each(function(){
		name = this.name;
		var idx= name.split("_")[1];
		var idx_ = parseInt(idx) +1;
		$(this).parent().next().html(idx_);
		//调整tbody index
		$(this).parent().parent().parent().attr("id","subrogationCarTbody_"+idx);
	});
}

function initSubrogationPers(){//subrogationPerTbody
	var tbody = $("#subrogationPerTbody");
	var carSize =$("#subrogationPerSize") ;// 
	var registNo =$("#registNo").val() ;// 
	var size = parseInt(carSize.val(),10);
	var params = {"size":size,"registNo":registNo};
	var url = "/claimcar/certify/loadSubrationPers.ajax";
	$.post(url,params, function(result){
		tbody.append(result);
		carSize.val(size + 1 );//
	});
}

function delSubrogationPers(element){
	var index = $(element).attr("name").split("_")[1];//下标
	var proposalPrefix="subrogationMain.prpLSubrogationPersons";
	var parentTr = $(element).parent().parent();
	var $subRiskSize =$("#subrogationPerSize") ;//
	var subRiskSize = parseInt($subRiskSize.val(),10);//
	$subRiskSize.val(subRiskSize-1);//删除一条
	
	delTr(subRiskSize, index, "subrogationPer_", proposalPrefix);
	
	for(var i=0;i<3;i++){
		var parentTr1 = parentTr.next();
		parentTr1.remove();
	}
	parentTr.remove();
	parentTr.remove();
	
	//调整序号
	var items = $("button[name^='subrogationPer_']");
	items.each(function(){
		name = this.name;
		var idx= name.split("_")[1];
		idx = parseInt(idx) +1;
		$(this).parent().next().html(idx);
	});
}
