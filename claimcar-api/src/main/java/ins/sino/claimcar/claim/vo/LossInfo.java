package ins.sino.claimcar.claim.vo;


import ins.framework.lang.Datas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LossInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 保单号
	 */
	private String policyNo;

	/**
	 * 报案号
	 */
	private String registNo;

	/**
	 * 对LossItem的管理类
	 * 未分组的损失项目
	 */
	private List<LossItem> lossItems;

	/**
	 * 保单的承保险别
	 */
	private List<String> kindCodes;

	public LossInfo(){
		lossItems = new ArrayList<LossItem>(0);
	}

	/**
	 * 增加损失项目
	 * @param item
	 * @return
	 */
	public void addItem(LossItem item){
		lossItems.add(item);
	}

	/**
	 * 增加损失项列表
	 * @param item
	 * @return
	 */
	public void addItem(List<LossItem> items){
		for(LossItem item : items){
			addItem(item);
		}
	}

	/**
	 * 增加损失项目，如果主键已经存在则不增加
	 * 避免查勘和定损中都存在的损失项重复
	 * @param item
	 */
	public void addItemWithCheck(LossItem lossItem){
		//boolean find = false;
		for(LossItem item : lossItems){
			if(item.getItemKey().equals(lossItem.getItemKey())){
				return;
			}
		}
		lossItems.add(lossItem);
	}

	/**
	 * 增加损失项目，如果主键已经存在则不增加
	 * 避免查勘和定损中都存在的损失项重复
	 * @param item
	 */
	public void addItemWithCheck(List<LossItem> items){
		for(LossItem item : items){
			addItemWithCheck(item);
		}
	}

	/**
	 * 判断在损失项中是否已经存在相同的key
	 * @param key
	 * @return
	 */
	public boolean isExistKey(Map key){
		for(LossItem item : lossItems){
			if(item.getItemKey().equals(key)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 对损失项分类汇总
	 * 按照损失类型进行GROUP，获取分类合计金额
	 * 注：分组后flag失去意义；不同币种未考虑
	 * @return
	 */
	public List<LossItem> getLossGroupByType(){
		List<LossItem>	items = new	ArrayList<LossItem>(0);
		for(LossItem item : lossItems){
			int index = indexOfLossItemType(items,item.getLossItemType());
			if(index == -1){
				if(item.getSumClaim()==null){
					item.setSumClaim(new BigDecimal("0.00"));
				}
				if(item.getRejectFee()==null){
					item.setRejectFee(new BigDecimal("0.00"));
				}
				if(item.getRescueFee()==null){
					item.setRescueFee(new BigDecimal("0.00"));
				}
				LossItem tmpItem = new LossItem();
				tmpItem.setLossItemType(item.getLossItemType());
				tmpItem.setSumClaim(item.getSumClaim());
				tmpItem.setRescueFee(item.getRescueFee());
				tmpItem.setRejectFee(item.getRejectFee());
				items.add(tmpItem);
			}else{
				LossItem tItem= items.get(index);
				//估损金额
				if(item.getSumClaim() != null){
					tItem.setSumClaim(BigDecimal.valueOf(Datas.round(tItem.getSumClaim().doubleValue()+item.getSumClaim().doubleValue(),2)));
				}
				//残值
				if(item.getRejectFee() != null){
					tItem.setRejectFee(BigDecimal.valueOf(Datas.round(tItem.getRejectFee().doubleValue()+item.getRejectFee().doubleValue(),2)));
				}
				//施救费
				if(item.getRescueFee() != null){
					tItem.setRescueFee(BigDecimal.valueOf(Datas.round(tItem.getRescueFee().doubleValue()+item.getRescueFee().doubleValue(),2)));
				}
				//名称是否连接待考虑......
				tItem.setLossName(tItem.getLossName()+","+item.getLossName());
			}
		}
		return items;
	}

	/**
	 * 确定lossItems中是否存在损失类别lossItemType的记录，存在则返回位置，否则返回-1
	 * @param lossItems
	 * @param lossItemType
	 * @return 未找到返回-1，找到则返回记录位置
	 */
	public int indexOfLossItemType(List<LossItem> items, String lossItemType){
		for(int i=0;i<items.size();i++){
			LossItem item = items.get(i);
			String type = item.getLossItemType();
			if(type!=null){
				if(type.equals(lossItemType)){
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 根据主键查找损失项
	 * @param key
	 * @return
	 */
	public LossItem findItemByPK(Map key){
		for(LossItem lossItem : lossItems){
			if(lossItem.getItemKey().equals(key)){
				return lossItem;
			}
		}
		return null;
	}

	public List<String> getKindCodes() {
		return kindCodes;
	}

	public void setKindCodes(List<String> kindCodes) {
		this.kindCodes = kindCodes;
	}

	public List<LossItem> getLossItems() {
		return lossItems;
	}

	public void setLossItems(List<LossItem> lossItems) {
		this.lossItems = lossItems;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

}

