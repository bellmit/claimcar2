package ins.sino.claimcar.jiangxicourt.web.action;



import ins.sino.claimcar.jiangxicourt.service.JiangXiCourtQueryService;
import ins.sino.claimcar.jiangxicourt.vo.Data;
import ins.sino.claimcar.jiangxicourt.vo.ListSearchParam;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 江西法院查询请求接口
 */
public class JiangXiCourtQueryServlet extends BaseServlet {
	
	private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(JiangXiCourtQueryServlet.class);    
    @Autowired
    private JiangXiCourtQueryService jiangXiCourtQueryService;
 
    
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{		
		PrintWriter out = response.getWriter();
		JSONArray dataArray = new JSONArray();
		JSONObject responseJsonObj = new JSONObject();
		
		
		JSONObject json = getRequestJsonObject(request);
		JSONObject rootObject = new JSONObject(json);		
		JSONObject message = rootObject.getJSONObject("Message");
		JSONObject bdy = message.getJSONObject("Body");
		JSONArray li = bdy.getJSONArray("listSearchParam");
		List<ListSearchParam> searchParam = new ArrayList<ListSearchParam>();
		for(int i = 0;i < li.toArray().length; i++){
			JSONObject sonObject = li.getJSONObject(i);
			ListSearchParam param = new ListSearchParam();
			param.setFY_RWLSH(sonObject.getString("FY_RWLSH")); //获取流水号
			param.setBX_BDH(sonObject.getString("BX_BDH"));     //获取保单号
			searchParam.add(param);
		}
		
		try {
				if(searchParam != null && searchParam.size() > 0){
					for(ListSearchParam sp : searchParam){
						if(sp.getBX_BDH() != null && !sp.getBX_BDH().isEmpty() && sp.getFY_RWLSH() != null && !sp.getFY_RWLSH().isEmpty()){
							List<Data> dataList = jiangXiCourtQueryService.findByPolicyNo(sp.getFY_RWLSH(), sp.getBX_BDH());													
							for(Data data : dataList){
								JSONObject jsons = new JSONObject();
								jsons.put("FY_RWLSH", data.getFY_RWLSH());
								jsons.put("BX_BDH", data.getBX_BDH());
								jsons.put("BX_LPQK_SFYJ", data.getBX_LPQK_SFYJ());
								jsons.put("BX_LPQK_PFSJ", data.getBX_LPQK_PFSJ());
								jsons.put("BX_LPQK_PFJE", data.getBX_LPQK_PFJE());
								jsons.put("BX_LPQK_ZFFS", data.getBX_LPQK_ZFFS());
								jsons.put("BX_LPQK_KHYH", data.getBX_LPQK_KHYH());
								jsons.put("BX_LPQK_KHHZH", data.getBX_LPQK_KHHZH());
								jsons.put("BX_XTMC", data.getBX_XTMC());
								dataArray.add(jsons);
							}
							responseJsonObj.put("data", dataArray);							
						}else{
							responseJsonObj.put("status", "500");
							responseJsonObj.put("statusText", "流水号或保单号为空！");
							throw new IllegalArgumentException(" 流水号或保单号为空！ "); 
						}						
					}
					responseJsonObj.put("status", "200");
					responseJsonObj.put("statusText", "Success");
					
				}else{
					responseJsonObj.put("status", "500");
					responseJsonObj.put("statusText", "请求体为空！");
					throw new IllegalArgumentException("请求体为空！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");
				out.println(responseJsonObj.toJSONString());
				out.close();			
			}
}
			
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.doPost(request, response);
	}

   
    public static JSONObject getRequestJsonObject(HttpServletRequest req) throws IOException{	
    	String json = getRequestJsonString(req);
    	return JSONObject.parseObject(json);
    }
    
    /**
     * 获取 request 中 json 字符串的内容
     * @param req
     * @return
     * @throws IOException
     */
    public static String getRequestJsonString(HttpServletRequest req) throws IOException{
    	String submitMethod = req.getMethod();	
    	if(submitMethod.equals("GET")){
    		return new String(req.getQueryString().getBytes("iso-8859-1"),"utf-8").replaceAll("%22", "\"");
    	}else{
    		return getRequestPostStr(req);
    	}
    }
    
    /**
     * 获取 post 请求内容
     * @param req
     * @return
     * @throws IOException
     */
    public static String getRequestPostStr(HttpServletRequest req) throws IOException{   	
    	byte buffer[] = getRequestPostBytes(req);
    	String charEncoding = req.getCharacterEncoding();
    	if(charEncoding == null){
    		charEncoding = "UTF-8";
    	}   	
    	return new String(buffer,charEncoding);   	
    }
    /**
     * 获取 post 请求的byte[]数组
     * @param req
     * @return
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest req) throws IOException{
    	int contentLength = req.getContentLength();
    	if(contentLength < 0){
    		return null;
    	}
    	byte buffer[] = new byte[contentLength];
    	for(int i = 0;i < contentLength;){   		
    		int readLen = req.getInputStream().read(buffer, i, contentLength - i);
    		if(readLen == -1){
    			break;
    		}
    		i += readLen;
    	}
    	return buffer;
    }
    
 /*   private void init(){
    	if(jiangXiCourtQueryService==null){
    		jiangXiCourtQueryService=(JiangXiCourtQueryService)Springs.getBean(JiangXiCourtQueryService.class);
    	}
    	
    }
*/
}
