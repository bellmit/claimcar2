package ins.sino.claimcar.longrise.hnServices;


public class IleapProxy implements ins.sino.claimcar.longrise.hnServices.Ileap {
  private String _endpoint = null;
  private ins.sino.claimcar.longrise.hnServices.Ileap ileap = null;
  
  public IleapProxy() {
    _initIleapProxy();
  }
  
  public IleapProxy(String endpoint) {
    _endpoint = endpoint;
    _initIleapProxy();
  }
  
  private void _initIleapProxy() {
    try {
      ileap = (new ins.sino.claimcar.longrise.hnServices.LeapLocator()).getleapPort();
      if (ileap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)ileap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)ileap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (ileap != null)
      ((javax.xml.rpc.Stub)ileap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ins.sino.claimcar.longrise.hnServices.Ileap getIleap() {
    if (ileap == null)
      _initIleapProxy();
    return ileap;
  }
  
  public byte[] importCaseinfo(byte[] arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException{
    if (ileap == null)
      _initIleapProxy();
    return ileap.importCaseinfo(arg0, arg1, arg2);
  }
  
  public java.lang.Integer app_login(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.Integer arg3, java.lang.String arg4) throws java.rmi.RemoteException{
    if (ileap == null)
      _initIleapProxy();
    return ileap.app_login(arg0, arg1, arg2, arg3, arg4);
  }
  
//  public static void main(String[] args) throws Exception {
//		
//		StringBuffer sb = new StringBuffer("");
//		FileReader reader = new FileReader("E:\\Noname1.txt");
//		BufferedReader br = new BufferedReader(reader);
//		String str = null;
//		String url = "http://59.173.241.186:8042/HNIS/services/com.longrise.services.leap";
//		//����ַString url = "http://220.231.252.30:/ISXS/services/com.longrise.services.leap";
//		String user = "410000003065";
//		String pwd = "410000003065";
//		while(( str = br.readLine() )!=null){
//			String[] strs = str.split(",");
//			CaseBean caseBean = new CaseBean();
//
////			caseBean.setInstype("1");// ����
////			caseBean.setPhase(strs[0]);
////			caseBean.setAreaid(strs[1]);
////			caseBean.setCarbrandno(strs[2]);
////			caseBean.setCarframeno(strs[3]);
////			caseBean.setDutybrandno(strs[4]);
////			caseBean.setDutyframeno(strs[5]);
////			caseBean.setInssort(strs[6]);
////			caseBean.setPolicyno(strs[7]);
////			caseBean.setStarttime(strs[8]);
//		//	String url = "http://220.231.252.30:8005/ISXSTEST/services/com.longrise.services.leap";
//			IleapProxy ileapProxy = new IleapProxy(url);
//			
//			String data = "[{\"phase\":\"1\",\"inssort\":\"������\",\"insuredname\":\"����1\",\"endorsementno\":\"\",\"carbrandno\":\"ԥAH123\",\"insuredmobile\":\"\",\"instype\":\"1\",\"policyno\":\"20160615TEST8\",\"effectivedate\":\"2016-06-14\",\"insuredidcard\":\"142233199402052012\",\"carframeno\":\"LHGNC566422020002\",\"areaid\":\"����ʡ������\",\"moneys\":\"\"}]";
//			BASE64Encoder base64 = new BASE64Encoder();
//			user = base64.encode(Str.rightTrim(user).getBytes());
//			pwd = base64.encode(Str.rightTrim(pwd).getBytes());
//			//System.out.println(jArray.toString());
//			System.out.println(data);
//			byte[] reBytes = ileapProxy.importCaseinfo(data.getBytes("UTF-8"),user,pwd);
//			String s = new String(reBytes);
//			System.out.println(s);
//			String sql = "insert into CiPlatJsonLog(jsonid,phase,caseno,mobile,dotime,doaddress,comcode,senddate,returndate,istatus,imessage) values('"+strs[0]+strs[1]+"','"+strs[0]
//					+"','"+strs[1]+"','"+strs[2]+"','"+strs[3]+"','"+strs[4]+"','1300',sysdate,sysdate,'"+caseBean.getIstatus()+"','"+caseBean.getImessage()+"');";
//			sb.append(sql+"\r\n");
//			System.out.println(sql);
//		}
//		br.close();
//		reader.close();
//		FileWriter writer = new FileWriter("E:\\Noname1.sql");
//      BufferedWriter bw = new BufferedWriter(writer);
//      bw.write(sb.toString());
//      bw.close();
//      writer.close();
//	}
}