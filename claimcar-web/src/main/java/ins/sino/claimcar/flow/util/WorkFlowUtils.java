package ins.sino.claimcar.flow.util;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.WfFlowNodeShowVo;
import ins.sino.claimcar.flow.vo.WfFlowNodeVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author dengkk
 * @CREATETIME 2016-01-14
 */
public class WorkFlowUtils {
	
	private static String []colors = {"#F1B889","#8DCE47","#F40000","#A2A2A2","#F4F400","#6093C2","#AFAFAF"};
	
	private int leafNodeNum = 0;//叶子节点数
	
	private int deepNum = 1;//层数
	
	private int nodeRow = 0;//节点图总层数
	

	public int getLeafNodeNum() {
		return leafNodeNum;
	}

	public void setLeafNodeNum(int leafNodeNum) {
		this.leafNodeNum = leafNodeNum;
	}

	public int getDeepNum() {
		return deepNum;
	}

	public void setDeepNum(int deepNum) {
		this.deepNum = deepNum;
	}
	

	public int getNodeRow() {
		return nodeRow;
	}

	public void setNodeRow(int nodeRow) {
		this.nodeRow = nodeRow;
	}

	//递归树结构node
	public  void recursion(WfFlowNodeVo node,int deep,List<WfFlowNodeVo> list){
		if(deep > deepNum){
			deepNum = deep;
		}
		node.setRow(deep);//设置节点所在行
		node.setColum(leafNodeNum+1);//设置节点所在列
		if(node.getChildNode() == null || node.getChildNode().size() == 0){//叶子节点
			leafNodeNum++;
			node.setRow(deep);
			node.setColum(leafNodeNum);
			return;
		}
		for(int i = 0 ;i <  node.getChildNode().size();i++){
			WfFlowNodeVo n = node.getChildNode().get(i);
			recursion(n,deep+1,list);
		}
	}
	
	/**
	 * 遍历树结构 初始化节点图list
	 * @param node
	 * @param list
	 */
	public void recursionNodeShow(WfFlowNodeVo node,List<WfFlowNodeShowVo> list){
		for(int i = 0; i < list.size();i ++){
			WfFlowNodeShowVo wfFlowNodeShowVo = list.get(i);
			if(wfFlowNodeShowVo.getChildList() == null){
				wfFlowNodeShowVo.setChildList(new ArrayList<WfFlowNodeVo>());
			}
			List<WfFlowNodeVo> childList = wfFlowNodeShowVo.getChildList();
			if(wfFlowNodeShowVo.getNodeCode().equals(node.getNodeCode())){
				WfFlowNodeVo parentNode = node.getParentNode();
				if(parentNode != null){
					//如果该节点父节点的nodeCode不等于wfFlowNodeShowVo的nodeCode
				    //那么这个节点的colum设置为1
					if(!parentNode.getNodeCode().equals(wfFlowNodeShowVo.getNodeCode())){
						node.setColum(1);
					}
				}
				childList.add(node);
			}
		}
		if(node.getChildNode() == null || node.getChildNode().size() == 0){//叶子节点
			return;
		}
		for(int i = 0 ;i <  node.getChildNode().size();i++){
			WfFlowNodeVo n = node.getChildNode().get(i);
			recursionNodeShow(n,list);
		}
	}
	
	/**
	 * 把list构建树结构
	 * @param taskId
	 * @param node
	 * @param list
	 */
	public void buildTree(BigDecimal taskId,WfFlowNodeVo node,List<WfFlowNodeVo> list,PrpLWfMainVo prpLWfMainVo,SaaUserPowerVo saaUserPowerVo){
	    String userCode = saaUserPowerVo.getUserCode();
	    List<String> roleList = saaUserPowerVo.getRoleList();
	    Map<String,List<SaaFactorPowerVo>> permitFactorMap = saaUserPowerVo.getPermitFactorMap();
		String handelStatus = node.getHandlerStatus();
	    String workStatus = node.getWorkStatus();
	    String nodeStatus = "";//节点状态翻译
	    String nodeColor = "";//节点颜色 不同状态颜色不同
	    String assignUser = node.getAssignUser();
	    //String comCode = node.getComCode();
	    String handlerUser = node.getHandlerUser();
	    String taskOutUser = node.getTaskOutUser();
	    String taskInUser = node.getTaskInUser();
	    String userName = assignUser;
	    StringBuffer url = new StringBuffer();
	    url.append(TaskQueryUtils.getTaskHandUrl(node.getSubNodeCode().split("_")[0],null));
		url.append("?flowId=").append(node.getNodeCode());
		url.append("&flowTaskId=").append(node.getTaskId());
		url.append("&taskInKey=").append(StringUtils.trim(node.getTaskInKey()));// 工作流流入的时的业务号，可能是报案号，立案号，计算书号
		url.append("&registNo=").append(node.getRegistNo());
		url.append("&handlerIdKey=").append(StringUtils.trim(node.getHandlerIdKey()));
		url.append("&claimNo=").append(StringUtils.trim(node.getClaimNo()));
		url.append("&handlerStatus=").append(node.getHandlerStatus());
		url.append("&workStatus=").append(node.getWorkStatus());
		url.append("&subNodeCode=").append(node.getSubNodeCode());
		if(FlowNode.ReCanVrf_LV1.equals(node.getSubNodeCode())||FlowNode.ReCanVrf_LV2.equals(node.getSubNodeCode()) 
				|| FlowNode.CancelVrf_LV1.equals(node.getSubNodeCode())|| FlowNode.CancelVrf_LV2.equals(node.getSubNodeCode())){
			url.append("&types=").append("1");
		} else if(FlowNode.ReCanLVrf_LV11.equals(node.getSubNodeCode()) || FlowNode.CancelLVrf_LV1.equals(node.getSubNodeCode())){
			url.append("&types=").append("2");
		} else if(FlowNode.CancelApp.equals(node.getSubNodeCode())||FlowNode.ReCanApp.equals(node.getSubNodeCode())
	||FlowNode.CancelAppJuPei.equals(node.getSubNodeCode())){
			url.append("&taskId=").append(node.getTaskId());
		}
		node.setUrl(url.toString());
	    if(workStatus.equals(CodeConstants.WorkStatus.VIRT)){//虚拟节点
	    	nodeStatus = "未处理";nodeColor=colors[4];node.setUrl("cancel");
	    }else{
	    	switch(Integer.valueOf(handelStatus)){
		    case 0:switch(Integer.valueOf(workStatus)){
				    case 0:nodeStatus = "未处理";nodeColor=colors[4];break;
				    case 6:nodeStatus = "被退回";nodeColor=colors[2];break;
				    case 4:nodeStatus = "被挂起";nodeColor=colors[3];node.setUrl("cancel");break;
	         }break;
		    case 1:nodeStatus = "已接收未处理";nodeColor=colors[1];userName=handlerUser;break;
		    case 2:switch(Integer.valueOf(workStatus)){
				    case 2:nodeStatus = "处理中";nodeColor=colors[1];
						   if(FlowNode.Regis.name().equals(node.getNodeCode()) ){
							   userName=taskInUser;//报案环节正在处理显示首次处理人
						   }else{
							   userName=handlerUser;
						   }break;
				    case 4:nodeStatus = "被挂起";nodeColor=colors[3];userName=handlerUser;node.setUrl("cancel");break;
            }break;
		    case 3:switch(Integer.valueOf(workStatus)){
		             case 3:nodeStatus = "已处理";nodeColor=colors[5];userName=taskOutUser;break;
		             //case 4:nodeStatus = "被挂起";nodeColor=colors[1];userName=handlerUser;break;
		             case 5:nodeStatus = "已回退";nodeColor=colors[2];userName=taskOutUser;break;
		             case 8:nodeStatus = "已改派";nodeColor=colors[6];node.setUrl("cancel");break;
		             case 7:nodeStatus = "已移交";nodeColor=colors[6];node.setUrl("cancel");
		     }break;
		    case 9:
		       userName = taskOutUser;
			   nodeStatus = "已注销";nodeColor=colors[3];
			  if(FlowNode.CancelApp.name().equals(node.getSubNodeCode()) || FlowNode.CancelAppJuPei.name().equals(node.getSubNodeCode())){
				   nodeStatus = "已放弃";
			   }
			   //注销的报案节点和立案节点可以点击
			   if(!FlowNode.Regis.name().equals(node.getNodeCode()) && !FlowNode.Claim.name().equals(node.getNodeCode())){
				   node.setUrl("cancel");
			   }else{
				   //报案注销的立案节点，立案节点不能点击
				   if(FlowNode.Claim.name().equals(node.getNodeCode()) && FlowNode.Cancel.name().equals(node.getTaskOutNode())){
					   node.setUrl("cancel");
				   }else{
					   node.setFlag("rcCancel");
				   }
				   
			   }
		    }
	    }
	    //流程图不能点的，并且不是已处理状态的进行权限控制,并且不是报案注销和立案注销的节点
    	if(!"cancel".equals(node.getUrl()) && (!HandlerStatus.END.equals(handelStatus) && !WorkStatus.END.equals(workStatus)) && !"rcCancel".equals(node.getFlag())){
	    	String user = "";
	    	if(HandlerStatus.DOING.equals(handelStatus)){
	    		user = node.getHandlerUser();
			}else{
				user = node.getAssignUser();
			}
	    	if(StringUtils.isNotBlank(user)){
	    		if(!FlowNode.Regis.name().equals(node.getNodeCode())&&!user.equals(userCode)){
	    			// 报案正在处理允许其他报案工号点击进入
		    		node.setUrl("noPermission");
		    	}
	    	}else{//为空是未指定人员  需要判断是否在这个环节是否有权限
	    		FlowNode flowNode = Enum.valueOf(FlowNode.class, node.getSubNodeCode());
	    		String gradeId = flowNode.getRoleCode();
	    		if(!roleList.contains(gradeId)){//没有权限
	    			node.setUrl("noPermission");
	    		}else{//有权限，还需判断下指定处理机构 是否在权限内
	    			List<SaaFactorPowerVo> comCodePower = permitFactorMap.get("FF_COMCODE");
	    			//到岗的需要判断，指定机构是否和权限机构匹配
	    			String assignCom = node.getAssignCom();
	    			if(StringUtils.isNotBlank(assignCom)){
	    				boolean iscomCodePower = false;
		    		    if(comCodePower != null && !comCodePower.isEmpty()){
		    				for(SaaFactorPowerVo saaFactorPowerVo:comCodePower){
			    				String dataValue = saaFactorPowerVo.getDataValue();
			    				if(dataValue.contains("%")){
			    					String comCodeSplit = dataValue.split("%")[0];
			    					if(assignCom.startsWith(comCodeSplit)){//是权限机构开头的指定处理机构
			    						iscomCodePower = true;
			    						break;
			    					}
			    				}else{
			    					if(dataValue.equals(assignCom)){//权限机构和指定机构相等
			    						iscomCodePower = true;
			    						break;
			    					}
			    				}
		    			    }
			    			if(!iscomCodePower){
			    				node.setUrl("noPermission");
			    			}
	    			  }
	    			}else{
	    				node.setUrl("noPermission");
	    			}
	    		}
	    	}
	    	
	    }
	    
	    if(FlowStatus.CANCEL.equals(prpLWfMainVo.getFlowStatus())){
	    	nodeColor = colors[6];
	    }
	    node.setNodeColor(nodeColor);
	    node.setNodeStatus(nodeStatus);
	    if(userName == null){
	    	node.setUserName("未指定人员");
	    }else{
	    	node.setUserName(CodeTranUtil.transCodeBigData("UserCode", userName));
	    }
		for(int i = 0;i< list.size();i++){
			WfFlowNodeVo wfFlowNodeVo = list.get(i);
			BigDecimal nodeTaskId= wfFlowNodeVo.getTaskId();
			BigDecimal nodeUpperTaskId = wfFlowNodeVo.getUpperTaskId();
			if(taskId.equals(nodeUpperTaskId)){
		         List<WfFlowNodeVo> childList = new ArrayList<WfFlowNodeVo>();
		         wfFlowNodeVo.setChildNode(childList);
		         wfFlowNodeVo.setParentNode(node);
		         node.getChildNode().add(wfFlowNodeVo);
		         buildTree(nodeTaskId,wfFlowNodeVo,list,prpLWfMainVo,saaUserPowerVo);
			}
		}
	}

}
