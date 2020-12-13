<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>批单打印格式选择</title>
</head>
<body>
<!-- 1表示word格式打印，2表示pdf格式打印 -->
<div class="table_wrap" style="height:1000px">
<div class="table_cont  mb-100 ">
	<div class="formtable text-c mb-100">
		<div class=" row cl text-c mb-100" >
			<div class=" col-offset-1 form_input col-2">
				<a class="btn  btn-primary" onclick="certifyprintWo('${mainId}','${registNo}','${compensateNo}','${index}','1')"><font size='2'>Word格式打印</font></a>
			</div>
			<div class="form_input col-2">
				<a class="btn  btn-primary" onclick="certifyprintWo('${mainId}','${registNo}','${compensateNo}','${index}','2')"><font size='2'>Pdf格式打印</font></a>
			</div>
		</div>
		<p>
	</div>
</div>
<p>
<p>
<p>

</div>

<!-- A-报案代抄单打印    B-查勘信息打印   C-定损信息打印  D-核损信息打印  E-理算信息打印  F-理算书附页打印  G-理算书收据打印  H-反洗钱信息打印 -->
<script type="text/javascript">
function certifyprintWo(mainId,registNo,compensateNo,index,sign){
	var params="";
	var url="";
	var title="";
	if(index=='A'){
		 params = "?registNo=" + registNo+"&sign="+sign;
		 url = "/claimcar/certifyPrint/prpLRegist.doc"+params;	
		 title='机动车辆保险报案记录';
	}
	
	if(index=='B'){
		params = "?registNo=" + registNo+"&sign="+sign;
		url = "/claimcar/certifyPrint/checkTask.doc"+params;
		title='查勘报告';
	}
	
	if(index=='C'){
		
		params = "?registNo="+registNo+"&sign="+sign+"&mainId="+mainId;
		url = "/claimcar/certifyPrint/lossCarInfo.doc"+params;
		title='定损清单';
	}
	if(index=='D'){
		params = "?registNo="+registNo+"&sign="+sign+"&mainId="+mainId;
		url = "/claimcar/certifyPrint/verifyLossCarInfo.doc"+params;
		title='核损清单';
	}
	
	if(index=='E'){
		params = "?registNo=" + registNo+"&compensateNo="+compensateNo+"&sign="+sign;
		url = "/claimcar/certifyPrint/compensateInfo.doc"+params;
		title="赔款理算书";
	}
	
	if(index=='F'){
		params="?registNo=" + registNo+"&compensateNo="+compensateNo+"&sign="+sign;
		url="/claimcar/certifyPrint/compensateInfofuye.doc"+params;
		title="赔款理算书附页";
	}
	
	if(index=='G'){
		params="?registNo=" + registNo+"&compensateNo="+compensateNo+"&sign="+sign+"&mainId="+mainId;
		url = "/claimcar/certifyPrint/compensateInfoNote.doc"+params;
		title="赔款通知书/收据";
	}
	if(index=='e'){
		params = "?registNo=" + registNo+"&compensateNo="+compensateNo+"&sign="+sign;
		url = "/claimcar/certifyPrint/compensateInfo.doc"+params;
		title="赔款理算书";
		 openWinCom(title,url); 
		 return;
	}
	if(index=='P'){
		params = "?registNo=" + registNo+"&compensateNo="+compensateNo+"&sign="+sign;
		url = "/claimcar/certifyPrint/prePadPayView.doc"+params;
		title="预付（垫付）计算书";
		 openWinCom(title,url); 
		 return;
	}
	if(index=='H'){
		params = "?registNo=" + registNo+"&sign="+sign+"&mainId="+mainId+"&claimNo="+compensateNo;
		url = "/claimcar/certifyPrint/AMLInfo.doc"+params;
		title="反洗钱信息";
	}
	openWinCom(title,url);
	
}



</script>

</body>

</html>