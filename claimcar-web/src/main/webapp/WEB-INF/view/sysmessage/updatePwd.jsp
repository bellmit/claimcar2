<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
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
		<input type="hidden" id='userCode' value="${UserCode }" />
		<input type="hidden" id='flag' value="0"/>
		<form action="#">

			<div class="row mb-3 cl box">
				<lable class="form_label col-1 text-c">账号</lable>
				<lable class="form_label col-3">
				<input type='text' value="${UserCode}" readonly="readonly" class="input-text" style="width:90%"/>
				</lable>
			</div>
			<div class="row mb-3 cl box">
				<lable class="form_label col-1 text-c">原密码</lable>
				<lable class="form_label col-3">
				<input type="password" id="oldpwd" name="oldpwd"  class="input-text"  style="width:90%"/>
				</lable>
			</div>
			<div class="row mb-3 cl box">
				<lable class="form_label col-1 text-c">新密码</lable>
				<lable class="form_label col-3">
				<input type="password" id="newpwd1" name="newpwd1" class="input-text"  style="width:90%"/>
				</lable>
			</div>
			<div class="row mb-3 cl box">
				<lable class="form_label col-1 text-c">确认密码</lable>
				<lable class="form_label col-3">
				<input type="password" id="newpwd2" name="newpwd2" class="input-text"  style="width:90%"/>
				</lable>
			</div>
			<div class="box">
				<input type="button" value="提交" onclick='submit1()' class="btn  btn-primary"/> 
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
	$('#oldpwd').blur(function(){  //校验原密码
		var oldpwd =  $('#oldpwd').val();
		var params ={
				userCode : $('#userCode').val(),
				  oldPwd :  oldpwd
		};
	if( oldpwd != "" ){
		$.ajax({
			url:"/claimcar/updatePwd/checkOldPwd.do",
			type : 'post',
			dataType : 'json',
			data : params,
			async : false,
			success : function(result){
				if(result.data == false){
					$('#msg').html("<font color='red'>原密码输入错误</font>");
					$('#flag').val("0");
				}else{
					$('#msg').html("<font color='red'>请修改密码</font>");
					$('#flag').val("1");
				}
			}
		});
	}
		
	});
	$('input').change(function(){
		$('#msg').html("");
	});
	$('#newpwd1').blur(function(){
		if ($('#newpwd1').val() == '') {
			$('#msg').html("<font color='red'>请输入新密码</font>");
			}
		if ($('#newpwd1').val().length < 4) {
			$('#msg').html("<font color='red'>新密码不能小于四位</font>");
			}
		});
	$('#newpwd2').blur(function(){
		if ($('#newpwd2').val() != $('#newpwd1').val()) {
			$('#msg').html("<font color='red'>确认密码错误，请重新输入</font>");
			}
		});
		function isPwdeq() {
			var pwd1 = $('#newpwd1').val();
			var pwd2 = $('#newpwd2').val();
			if (pwd1 != "" && pwd2 != "" && pwd1 == pwd2) {
				return true;
			}
		};

		function submit1() {
			if (!checkPwd()) {
				return false;
			}
			if ($('#flag').val() == "0") {
				$('#msg').html("<font color='red'>原密码输入错误,不能修改密码</font>");
				return false;
			}
			var params = {
				userCode : $('#userCode').val(),
				oldPwd : $('#oldpwd').val(),
				newPwd1 : $("#newpwd1").val()
			};
			$.ajax({
				url : "/claimcar/updatePwd/updatePwd.do",
				type : 'post',
				dataType : 'json',
				data : params,
				async : false,
				success : function(result) {
					if (result.data == false) {
						$('#msg').html("<font color='red'>原密码错误，密码修改失败</font>");
					} else {
						$('#msg').html("<font color='red'>密码修改成功</font>");
						setTimeout(
								function() {
									var index = parent.layer
											.getFrameIndex(window.name);
									parent.layer.close(index);
								}, 2000);
					}
				}
			});
		}
		function checkPwd() {
			var newpwd1 = $('#newpwd1').val();
			var newpwd2 = $('#newpwd2').val();
			if (newpwd1 == "" || newpwd1.length < 4) {
				$('#msg').html("<font color='red'>密码不能少于4位</font>");
				return false;
			}
			if (!(newpwd1 == newpwd2)) {
				$('#msg').html("<font color='red'>两次输入的密码不一样</font>");
				// alert("两次输入的密码不一样");
				return false;
			}
			if (newpwd1 == newpwd2) {
				return true;
			}
			return false;
		}
	</script>
</body>
</html>