<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>警报信息展示</title>
<style>
        .img{
            transform:scale(0.5);//设置缩放比例
            -ms-transform:scale(0.5);
            -webkit-transform:scale(0.5);
            -o-transform:scale(0.5);
            -moz-transform:scale(0.5);
        }
       .container{
                -webkit-column-width:160px;
                -moz-column-width:160px;
                -o-colum-width:160px;
                -webkit-column-gap:1px;
                -moz-column-gap:1px;
                -o-column-gap:1px;
        }
/*         div:not(.container){
                -webkit-border-radius:5px;
                -moz-border-radius:5px;
                border-radius:5px;
                background:#D9D9D9;
                border::#CCC 1px solid;
                display:inline-block;
                width:157px;
                position:relative;
                margin:2px;
        } */
        .title{
                 line-height:80px; font-size:18px; color:#900;
                 text-align:center;
                 font-family:"Microsoft YaHei";
        }
        .box{
        width:200px;
        height:320px;
        float:left;
        margin-left:50px;
        margin-top:50px ;
        text-align:center;
        }
        img{
        width:200px;
        height:300px;
        }
        
</style>
</head>
<body>
   <div class="table_wrap">
		<div class="table_title f14">案件信息</div>
		<div class="table_cont">
			<div class="formtable ">
				<div class="row cl">
					<label class="form_label col-1">案件编码</label>
					<div class="form_input col-3">${mainVo.caseId }</div>
					<label class="form_label col-1">报案人姓名</label>
					<div class="form_input col-3">${mainVo.respUserName }</div>
					<label class="form_label col-1">查勘员姓名</label>
					<div class="form_input col-3">${mainVo.surveyMembers }</div>
				</div>
			</div>
			<div class="formtable ">
				<div class="row cl">
					<label class="form_label col-1">出险时间</label>
					<div class="form_input col-3">
						<fmt:formatDate value="${mainVo.accidentTime}" pattern="yyyy-MM-dd HH:mm:ss" />
					</div>
					<label class="form_label col-1">报案人电话</label>
					<div class="form_input col-3">${mainVo.respUserPhone }</div>
					<label class="form_label col-1">查勘员电话</label>
					<div class="form_input col-3">${mainVo.surveyMembersPhone }</div>
					
				</div>

				<div class="row cl">
				    <label class="form_label col-1">出险区域</label>
					<div class="form_input col-3"><app:codetrans codeType="Link_District" codeCode="${mainVo.district}"/></div>
					<label class="form_label col-1">报案时间</label>
					<div class="form_input col-3">
						<fmt:formatDate value="${mainVo.resptime }" pattern="yyyy-MM-dd HH:mm:ss" />
					</div>
					<label class="form_label col-1">出发时间</label>
					<div class="form_input col-3">
						<fmt:formatDate value="${mainVo.startTime }" pattern="yyyy-MM-dd HH:mm:ss" />
					</div>
				</div>

				<div class="row cl">
					<label class="form_label col-1">出险地点</label>
					<div class="form_input col-3">${mainVo.accdientAddress }</div>
					<label class="form_label col-1">案件状态</label>
					<div class="form_input col-3">
					<app:codetrans codeType="Link_Status" codeCode="${mainVo.status }"/>
					</div>
					<label class="form_label col-1">到达时间</label>
					<div class="form_input col-3">
						<fmt:formatDate value="${mainVo.arriveTime}" pattern="yyyy-MM-dd HH:mm:ss" />
					</div>
				</div>

				<div class="row cl">
					<label class="form_label col-1">出险经过</label>
					<div class="form_input col-3">${mainVo.assidentDescribe }</div>
					<label class="form_label col-1">案件类型</label>
					<div class="form_input col-3">
						<c:if test="${mainVo.caseType ==1 }">单方事故</c:if>
						<c:if test="${mainVo.caseType ==2 }">多方事故</c:if>
						<c:if test="${mainVo.caseType ==3 }">双方事故</c:if>
					</div>
					 <label class="form_label col-1">撤离时间</label>
					<div class="form_input col-3">
						<fmt:formatDate value="${mainVo.leaveTime}"
							pattern="yyyy-MM-dd HH:mm:ss" />
					</div>
				
				</div>

				<div class="row cl">
				<label class="form_label col-1">物损信息</label>
					<div class="form_input col-3">${mainVo.itemsName}</div>
					 <label class="form_label col-1">经度</label>
					<div class="form_input col-3">${mainVo.lng }</div>
					<label class="form_label col-1">纬度</label>
					<div class="form_input col-3">${mainVo.lat }</div>
				 
				</div>
			</div>
		</div>
		
		<div class="formtable ">
			<div class="table_title f14">车辆出险信息</div>
			<table class="table table-border table-hover data-table ">
				<thead>
					<tr class="text-c">
						<th>案件编码</th>
						<th>车牌号</th>
						<th>驾驶员姓名</th>
						<th>驾驶员电话</th>
						<th>交强险</th>
						<th>商业险</th>
						<th>是否报案</th>
						<th>责任类型</th>
						<th>大致受伤部位</th>
						<th>车辆类型</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<c:forEach items="${carList }" var="carVo" varStatus="status">
						<tr>
							<td>${carVo.caseId }</td>
							<td>${carVo.hphm }</td>
							<td>${carVo.driverName }</td>
							<td>${carVo.phone }</td>
							<td><app:codetrans codeType="Link_RiskType" codeCode="${carVo.jqx }"/></td>
							<td><app:codetrans codeType="Link_RiskType" codeCode="${carVo.syx }"/></td>
							<td>
							       <c:if test="${carVo.isResp == 1 }">是</c:if> 
							       <c:if test="${carVo.isResp == 2 }">否</c:if></td>
							<td>
							       <c:if test="${carVo.type == 1 }">全责</c:if> 
							       <c:if test="${carVo.type == 2 }">同责</c:if> 
							       <c:if test="${carVo.type == 3 }">主责</c:if> 
							       <c:if test="${carVo.type == 4 }">次责</c:if> 
							       <c:if test="${carVo.type == 5 }">无责</c:if></td>
							<td><app:codetrans codeType="Link_LossPart" codeCode="${carVo.injureLocation }"/></td>
							<td>
							      <c:if test="${carVo.targetOrThirdCarType == 0 }">标的车</c:if>
								  <c:if test="${carVo.targetOrThirdCarType == 1 }">三者车</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div style="background-color:#FFFFFF"></br></div>

		<div class="formtable ">
			<div class="table_title f14">人伤信息信息</div>
			<table class="table table-border table-bg">
				<thead>
					<tr class="text-c">
						<th>案件编码</th>
						<th>姓名</th>
						<th>身份证号码</th>
						<th>性别</th>
						<th>年龄</th>
						<th>户籍类型</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${injuredList }" var="injuredVo"
						varStatus="status">
						<tr class="text-c">
							<td>${injuredVo.caseId }</td>
							<td>${injuredVo.name }</td>
							<td>${injuredVo.sfzmhm }</td>
							<td><c:if test="${injuredVo.sex == 1 }">男</c:if> <c:if
									test="${injuredVo.sex == 2 }">女</c:if></td>
							<td>${injuredVo.age }</td>
							<td>${injuredVo.hj }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
        <div style="background-color:#FFFFFF"></br></div>
        
        <div class="table_title f14">图片信息</div>
		<div class="table_cont">

			<c:forEach items="${imgList }" var="imgs">

				<%-- <div style="position: relative;">
				<img src="${imgs.picUrl }" alt="${imgs.tags }" class="thumbnail" style="position: relative;">
				<span style="position: absolute; top: 0; left: 0;">添加文字...afvasfadfv添加文字...添加文字...</span>
				</div> --%>
		<%-- 		<div>
					<div class="container"
						style="display: inline-block; float: left; width: 300px; margin-top: 30px; margin-left: 50px;"
						onclick="showBigPhoto('${imgs.picUrl }','${imgs.imgId}')">
						<img src="${imgs.picUrl }" alt="${imgs.tags }" class="thumbnail"
							id="${imgs.imgId}"> <br style="display: inline-block;" />
						<div class="text-c">
							<span style="margin-left: auto"> <c:if
									test="${imgs.tags =='1' }">人车合影</c:if> <c:if
									test="${imgs.tags =='2' }">架号钢印照片</c:if> <c:if
									test="${imgs.tags =='3' }">环境照片</c:if> <c:if
									test="${imgs.tags =='4' }">碰撞照片</c:if> <c:if
									test="${imgs.tags =='5'}">人伤物损照片</c:if> <c:if
									test="${imgs.tags =='6'}">证件照片</c:if> <c:if
									test="${imgs.tags =='7' }">交强险标示</c:if> <c:if
									test="${imgs.tags =='8'}">定则协议书</c:if>
							</span>
						</div>
					</div>

				</div> --%>
				
				<div class = "box" onclick="showImg1('${imgs.picUrl }','${imgs.smallPicUrl}')">
				<img src=${imgs.smallPicUrl } alt="${imgs.tags }" class="thumbnail" id="${imgs.imgId}"	>
				<span style="margin-left: auto border:1px solid black"> <c:if
									test="${imgs.tags =='1' }">人车合影</c:if> <c:if
									test="${imgs.tags =='2' }">架号钢印照片</c:if> <c:if
									test="${imgs.tags =='3' }">环境照片</c:if> <c:if
									test="${imgs.tags =='4' }">碰撞照片</c:if> <c:if
									test="${imgs.tags =='5'}">人伤物损照片</c:if> <c:if
									test="${imgs.tags =='6'}">证件照片</c:if> <c:if
									test="${imgs.tags =='7' }">交强险标示</c:if> <c:if
									test="${imgs.tags =='8'}">定则协议书</c:if>
				</div>
			</c:forEach>


		</div>
	
		</div>
<script type="text/javascript">

function showImg1(picUrl,tags){
	layer.open({
		  type: 2,
		  title:'图片展示',
		  area: ['450px', '550px'],
		  fixed: false, //不固定
		  maxmin: true,
		  content: [picUrl]/* '/claimcar/policyLink/showImg.do?picUrl='+picUrl+"&tags="+tags */
		});
}

function showBigPhoto(picUrl,tags){
	layer.open({
		  type: 1,
		  title: false,
		  closeBtn: 0,
		  area: '516px',
		  skin: 'layui-layer-nobg', //没有背景色
		  shadeClose: true,
		  content: $('#'+tags)
		});
	$('#'+tags).attr("src",picUrl);
	//history.go(0);
}
</script>
</body>
</html>