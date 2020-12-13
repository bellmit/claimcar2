/**
 * 
 */

$(function() {
	// 1.控件初始化
	$(".form_date").datetimepicker(
			{
		format : "yyyy-mm-dd",
		language : "zh-CN",
		autoclose : true,
		todayBtn : true,
		todayHighlight : true,
		showMeridian : false,
		minView : "month",
		pickerPosition : "bottom-left",
	});
	$(".form_date_AP").datetimepicker(
			{
				 format: "yyyy-mm-dd HH:ii:ss",
				 language : "zh-CN",
				 showMeridian: true,  //设置为AM/PM的12小时制或24小时制，默认值为false，此时时间为12小时制
				 startDate: "2014-06-24 10:35",  
				 autoclose: true, //设置当选择一个日期之后是否立即关闭此日期时间选择器
				 startView: 1,//日期时间选择器打开之后首先显示的视图，
				 todayBtn: true,
				 maxView:"day",
				 minView : "month",
				 pickerPosition : "bottom-left"
	});
	$(".form_date_24").datetimepicker(
			{
				format : "yyyy-mm-dd",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				minView : "month",
				pickerPosition : "bottom-left",
			
			});
	$(".form_date_decade").datetimepicker(
			{
				format: "yyyy",
				language : "zh-CN",
				showMeridian: false,  
				startDate: "2014-06-24 10:35",  
				autoclose: true,        
				todayBtn: true,
				startView: 4,
				minView : "decade",
				pickerPosition : "bottom-left"
			});
	$(".form_date_year").datetimepicker(
			{
				format: "yyyy-mm",
				language : "zh-CN",
				showMeridian: false,  
				startDate: "2014-06-24 10:35",  
				autoclose: true,        
				todayBtn: true,
				startView: 3,
				maxView : "year",
				minView : "year",
				pickerPosition : "bottom-left"
			});
	$(".form_date_month").datetimepicker(
			{
				format: "yyyy-mm-dd",
				language : "zh-CN",
				showMeridian: false,  
				startDate: "2014-06-24 10:35",  
				autoclose: true,        
				todayBtn: true,
				startView: 2,
				maxView : "month",
				minView : "month",
				pickerPosition : "bottom-left"
			});
	$(".form_date_day").datetimepicker(
			{
				format: "yyyy-mm-dd hh",
				language : "zh-CN",
				showMeridian: false,  
				startDate: "2014-06-24 10:35",  
				autoclose: true,        
				todayBtn: true,
				startView: 1,
				maxView : "day",
				minView : "day",
				pickerPosition : "bottom-left"
			});
	$(".form_date_hour").datetimepicker(
			{
				format: "yyyy-mm-dd hh:ii",
				language : "zh-CN",
				showMeridian: false,  
				startDate: "2014-06-24 10:35",  
				autoclose: true,        
				todayBtn: true,
				startView: 0,
				minuteStep:1,
				maxView : "hour",
				minView : "hour",
				pickerPosition : "bottom-left"
			});
	$(".form_date_one").datetimepicker(
			{
				format : "yyyy年mm月dd日",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				startView : "month",
				maxView : "year",
				minView : "month",
				pickerPosition : "bottom-left",
			
			});
	$(".form_date_two").datetimepicker(
			{
				format : "yyyy/mm/dd",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				startView : "month",
				maxView : "year",
				minView : "month",
				pickerPosition : "bottom-left",
			
			});
	$(".form_date_three").datetimepicker(
			{
				format : "dd/mm/yyyy",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				startView : "month",
				maxView : "year",
				minView : "month",
				pickerPosition : "bottom-left",
			
			});
	$(".form_date_four").datetimepicker(
			{
				format : "dd/mm/yy",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				startView : "month",
				maxView : "year",
				minView : "month",
				pickerPosition : "bottom-left",
			
			});
	$(".form_date_five").datetimepicker(
			{
				format : "yyyy/mm",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				startView : "month",
				maxView : "decade",
				minView : 3,
				pickerPosition : "bottom-left",
			
			});
	$(".form_date_six").datetimepicker(
			{
				format : "mm/dd",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				maxView : "year",
				startView : "month",
				minView : "month",
				pickerPosition : "bottom-left",
			
			});
	$(".form_date_time_one").datetimepicker(
			{
				format : "yyyy-mm-dd hh:ii:ss",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				minuteStep:1,
			//	minView : "day",
				pickerPosition : "bottom-left",
			
			});
	$(".form_date_time_two").datetimepicker(
			{
				format : "yyyy-mm-dd hh:ii",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				minuteStep:1,
				//minView : "day",
				pickerPosition : "bottom-left",
			
			});
	$(".form_date_time_three").datetimepicker(
			{
				format : "yyyy-mm-dd hh时",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				minView : "day",
				pickerPosition : "bottom-left",
			
			});
	$(".form_time_one").datetimepicker(
			{
				format : "hh:ii:ss",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				maxView:"day",
				startView:"day",
				minuteStep:1,
				pickerPosition : "bottom-left",
			
			});
	$(".form_time_two").datetimepicker(
			{
				format : "hh:ii",
				language : "zh-CN",
				autoclose : true,
				todayBtn : true,
				todayHighlight : true,
				showMeridian : false,
				startView:"day",
				maxView:"day",
				minuteStep:1,
				pickerPosition : "bottom-left",
			
			});
	$(".form_show_one").datetimepicker(
			{
				 format: "yyyy-mm-dd",
				 language : "zh-CN",
				 showMeridian: false, 
				 startDate: "2014-06-24 10:35",  
				 autoclose: true,
				 startView: 2,
				 todayBtn: true,
				// maxView:"day",
				// minView : "month",
				 pickerPosition : "bottom-left"
	});
	$(".form_show_two").datetimepicker(
			{
				 format: "yyyy-mm-dd",
				 language : "zh-CN",
				 showMeridian: false, 
				 startDate: "2014-06-24 10:35",  
				 autoclose: true,
				 startView: 2,
				 todayBtn: true,
				// maxView:"day",
				// minView : "month",
				 pickerPosition : "bottom-left"
	});
	$("#datechange").change(function() {
		 var selectval = $('#datechange').val(); 
		// alert(selectval);
		$("#today").val(GetDateStr(0));//填充内容
		
		$("#before").val(GetDateStr(parseInt(selectval)));
		
	});
	function GetDateStr(AddDayCount)
	{
	    var dd = new Date();
	    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
	    var y = dd.getFullYear();
	    var m = (dd.getMonth()+1)<10?"0"+(dd.getMonth()+1):(dd.getMonth()+1);//获取当前月份的日期，不足10补0
	    var d = dd.getDate()<10?"0"+dd.getDate():dd.getDate(); //获取当前几号，不足10补0
	    return y+"-" + m+"-" + d;

	}
});

