<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改身份证信息</title>
<style>
    .box{
    margin-left:20px;
    margin-top:4px;
    }
    .inputBox{
    width:150px;
    height:18px;
    }
    .submitBox{
    width:50px;
    height:25px;
    backgroundcolor:blue;
    }
    .spanBox{
    margin-left:20px;
    }
</style>
</head>
<body>
	<div class="table_wrap">
		<form  action="#">
			<div class="row mb-3 cl box">
				<lable class="form_label col-2 text-r">账号&nbsp</lable>
				<lable class="form_label col-3">
				<input type='text' id="userCode" name="userCode" value="${userCode}" readonly="readonly" class="input-text" style="width:90%"/>
				</lable>
			</div>
			<div class="row mb-3 cl box">
				<lable class="form_label col-2 text-r">姓名&nbsp</lable>
				<lable class="form_label col-3">
				<input type="text"  value="${userName}" readonly="readonly" class="input-text"  style="width:90%"/>
				</lable>
			</div>
			<div class="row mb-3 cl box">
				<lable class="form_label col-2 text-r">原身份证&nbsp</lable>
				<lable class="form_label col-3">
				<input type="text" value="${identifyNumber}" readonly="readonly" class="input-text"  style="width:90%"/>
				</lable>
			</div>
			<div class="row mb-3 cl box">
				<lable class="form_label col-2 text-r">修改后身份证&nbsp</lable>
				<lable class="form_label col-5">
				<input type="text"  id="identifyNumber" name="identifyNumber"  datatype="*" maxlength="18" class="input-text"  style="width:54%"/>
				</lable>
			</div>
			<div class="box col-6 text-c ">
				<input type="button" value="提交" onclick="submit1()" class="btn btn-primary"/> 
				
				<input class="btn btn-primary " type="reset" name="rest" id="rest" value="重置">
				<span id='msg' class="spanBox"> </span>
			</div>
		</form>
	</div>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<script type="text/javascript">
	function submit1() {
		var identifyNumber = $("#identifyNumber").val();
		var isIDCard = new RegExp("/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/");
		//if(isIDCard.test(identifyNumber)){
		if(isCardID(identifyNumber)){
			var params = {
					userCode : $('#userCode').val(),
					identifyNumber : identifyNumber
				};
				$.ajax({
					url : "/claimcar/updatePwd/updateCarID.do",
					type : 'post',
					dataType : 'json',
					data : params,
					async : false,
					success : function(result) {
						if (eval(result.status) != "200") {
							$('#msg').html("<font color='red'>身份证修改失败！</font>");
						} else {
							$('#msg').html("<font color='red'>身份证修改成功！</font>");
							setTimeout(
									function() {
										var index = parent.layer
												.getFrameIndex(window.name);
										parent.layer.close(index);
									}, 2000); 
						}
					}
				});
		}else{
			$('#msg').html("<font color='red'>身份证格式错误！</font>");
			return false;
		}
	}
	
	var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
	
	function isCardID(sId){
		 var iSum=0 ;
		 var info="" ;
		 if(!/^\d{17}(\d|x)$/i.test(sId)) return false;
		 sId=sId.replace(/x$/i,"a");
		 if(aCity[parseInt(sId.substr(0,2))]==null) return false;
		 sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2));
		 var d=new Date(sBirthday.replace(/-/g,"/")) ;
		 if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))return false;
		 for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11) ;
		 if(iSum%11!=1) return false;
		 //aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女");//此次还可以判断出输入的身份证号的人性别
		 return true;
		}
	</script>
</body>
</html>