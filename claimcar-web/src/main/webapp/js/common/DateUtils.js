/*** 日期处理工具
 * 方法列表：
 * DateUtils.strToDate(dateStr) :将yyyy-MM-dd格式的字符串转换为Date对象；
 * new Date().toDateStr();格式化日期为yyyy-MM-dd
 * new Date().format('yyyy-MM');格式化日期，指定格式；
 * 
 * 综合用法：
 * DateUtils.strToDate("2014-01-04").addYears(-1).monthLastDay().toDateStr()
 * 上月第一天：new Date().addMonths(-1).monthFirstDay().toDateStr();
 * 
 * 示例可参考/prpc/PrpCList.jsp 中的使用方法
 * */

var DateUtils={
		/**将yyyy-MM-dd格式的String 转换为Date对象**/
		strToDate:function(dateStr){
			dateStr=dateStr.replace("-",",");
			var date=new Date(dateStr);
			return date;
		},
		/**将带时间的格式截取到日期部分**/
		cutToDate:function(dateTimeStr){
			var idx=dateTimeStr.indexOf(" ");
			if(idx == -1) return dateTimeStr;
			var dateStr=dateTimeStr.substring(0,idx);
 			return dateStr;
		},
		cutToMinute:function(dateTimeStr){
			var idx=dateTimeStr.lastIndexOf(":");
			if(idx == -1) return dateTimeStr;
			var dateStr=dateTimeStr.substring(0,idx);
			return dateStr;
		},
		/**calcStartDate:根据计算表达式得到日期的开始时间,支持的表达式有  D/M/Y+-num,M.this本月份，M.prev上月份，M.next下月份**/
		calcStartDate : function(date, expression) {
		var calProp = expression.substring(0, 1);
		var calExp = expression.substring(1);
		var calNum = 0;
		if ($.isNumeric(calExp))
			calNum = parseInt(calExp);
		if (calProp == 'D') {
			date = date.addDays(calNum);
		} else if (calProp == 'M') {
			if (calExp == ".this") {// 本月份
				date = date.monthFirstDay();
			} else if (calExp == ".prev") {// 上月份
				date = date.addMonths(-1).monthFirstDay();
			} else if (calExp == ".next") {// 下月份
				date = date.addMonths(1).monthFirstDay();
			} else {
				date = date.addMonths(calNum);
			}
		} else if (calProp == 'Y') {
			date = date.addYears(calNum);
		}
		return date;
	},
	/**calcEndDate: 根据计算表达式得到日期的结束时间，支持表达式  M.prev上月份，M.next下月份，其他不计算**/
		calcEndDate : function(date, expression) {
		var calProp = expression.substring(0, 1);
		var calExp = expression.substring(1);
		if (calProp == 'M') {
			if (calExp == ".prev") {// 上月份
				date = date.addMonths(-1).monthLastDay();
			} else if (calExp == ".next") {// 下月份
				date = date.addMonths(1).monthLastDay();
			}
		}
		return date;
	},
	/**calcDiffDays:dateStr1-dateStr2，计算两个日期的间隔天数,也可用于比较日期大小*/
	calcDiffDays : function(dateStr1, dateStr2) {
		var date1 = DateUtils.strToDate(dateStr1);
		var date2 = DateUtils.strToDate(dateStr2);
		var diffDays=parseInt((date1 - date2) / 1000 / 60 / 60 /24);
		return diffDays;
	},
	/**calcDiffDate:date1-date2，计算两个日期的间隔天数,也可用于比较日期大小*/
	calcDiffDate : function(date1, date2) {
		var diffDays=parseInt((date1 - date2) / 1000 / 60 / 60 /24);
		return diffDays;
	}

};

/**
 * 日期格式化,使用方法new Date().format('yyyy-MM-dd')
 */
Date.prototype.format = function(format){
	var o = { 
	"M+" : this.getMonth()+1, //month 
	"d+" : this.getDate(), //day 
	"h+" : this.getHours(), //hour 
	"m+" : this.getMinutes(), //minute 
	"s+" : this.getSeconds(), //second 
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
	"S" : this.getMilliseconds() //millisecond 
	};

	if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	if(format == 'NaN-aN-aN'){
		format = '';
	}
	return format; 
};

/*** 日期转换为yyyy-MM-dd*/
Date.prototype.toDateStr = function(){
	return this.format("yyyy-MM-dd"); 
};

/***日期计算：天数*/
Date.prototype.addDays = function(num){
	 this.setDate(this.getDate() + num);
	 return this;
};

/***日期计算：月份*/
Date.prototype.addMonths = function(num){
	 this.setMonth(this.getMonth() + num);
	return this ; 
};

/***日期计算：年份*/
Date.prototype.addYears = function(num){
	 this.setFullYear (this.getFullYear () + num);
	return this ; 
};
/***月的第一天*/
Date.prototype.monthFirstDay = function(){
	 this.setDate(1);
	return this; 
};
/***月的最后一天*/
Date.prototype.monthLastDay = function(){
	this.addMonths(1).setDate(1);
	this.addDays(-1); 
	return this;
};