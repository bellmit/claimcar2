function showAddress(inputId) {
	var searchVal = $("#" + inputId).val();
	if(!(searchVal== "" || searchVal==null)){
		var oneList = ["北京市","上海市","天津市","重庆市","省","自治区","特别行政区"];
		for(var i=0;i<oneList.length;i++){
			pNum = searchVal.indexOf(oneList[i]);
			if(pNum != -1){
				provinceName = searchVal.substring(0, pNum)+oneList[i];
				break;
			}else{
				provinceName="";
			}
			
		}
		if(provinceName=="北京市"||provinceName=="上海市"||provinceName=="天津市"||provinceName=="重庆市"){
			var twoList = ["区","县"];
			for(var i=0;i<twoList.length;i++){
				cNum = searchVal.indexOf(twoList[i]);
				if(cNum != -1){
					cityName = searchVal.substring(3,cNum)+twoList[i];
					break;
				}	
			}
			
			countyName="";
			tNum=cNum;
		}else{
			
			var twoList = ["市","区","盟","州","县"];
			for(var i=0;i<twoList.length;i++){
				cNum = searchVal.indexOf(twoList[i]);
				if(cNum != -1){
					cityName = searchVal.substring(pNum+1,cNum)+twoList[i];
					break;
				}	
			}
			var threeList =["县","区","镇","旗","市"];
			for(var j=0;j<threeList.length;j++){
				tNum = searchVal.lastIndexOf(threeList[j]);
				if(tNum != -1){
					countyName = searchVal.substring(cNum+1,tNum)+threeList[j];
					break;
				}	
			}
		}
	}else{
		provinceName="";
		cityName="";
		countyName="";
		tNum=-1;
	}
	areaCode="";
	postCode="";
	lastName = searchVal.substring(tNum+1,searchVal.length);
	getOption("000000","provinceId",provinceName);
	getOption($("#provinceId").val(),"cityId",cityName);
	getOption($("#cityId").val(),"countyId",countyName);
	$("#lastName").val(lastName);
/*	if(areaCode!=""||areaCode!=null){
		$("#sysareaCode").val(areaCode);
	}
	if(postCode!=""||postCode!=null){
		$("#postCode").val(postCode);
	}*/
	$("#provinceId").change(
			function(){
				provinceName=$("#provinceId option:selected").text();
				getOption("000000","provinceId",provinceName);
				getOption($("#provinceId").val(),"cityId",provinceName);
				getOption($("#cityId").val(),"countyId",cityName);
				$("#sysareaCode").val(areaCode);
				$("#postCode").val(postCode);
			}
	);
	$("#cityId").change(
			function(){
				cityName = $("#cityId option:selected").text();
				getOption($("#provinceId").val(),"cityId",cityName);
				getOption($("#cityId").val(),"countyId",cityName);
				$("#sysareaCode").val(areaCode);
				$("#postCode").val(postCode);
			}
	);
	$("#countyId").change(
			function(){
				countyName = $("#countyId option:selected").text();
				getOption($("#cityId").val(),"countyId",countyName);
				$("#sysareaCode").val(areaCode);
				$("#postCode").val(postCode);
			}
	);
	
	$("body").bind("mousedown", giveValue);
}
function getOption(upperCode,selectId,name){
	
	$.ajax({
	type:"POST",
	async:false,
	url:contextPath + "/areadict/findArea?upperCode="+upperCode,
	success:function(data){
		var list = eval(data);
		$("#"+selectId).html("");
		var fal = name==""?"selected":"";
		$("#"+selectId).append("<option value='-1'"+fal+"></option>");
		for(var i = 0;i<list.length;i++){
			var flag = name==list[i].areaName?"selected":"";
			$("#"+selectId).append("<option value='"+list[i].areaCode+"'"
					+flag+">"+list[i].areaName+"</option>");
			if(flag=="selected"){
				postCode=list[i].postCode;
				areaCode=list[i].areaCode;
				
			}
		}		
	}
	});
}
function giveValue(event) {
	if (!(event.target.id == "addressName" || $(event.target).parents("#addressName").length > 0)) {
		$("body").unbind("mousedown", giveValue);
		$("#addressCName").val(provinceName+cityName+countyName+$("#lastName").val());
	}
}
