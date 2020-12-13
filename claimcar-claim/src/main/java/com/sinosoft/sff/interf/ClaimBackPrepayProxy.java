package com.sinosoft.sff.interf;

public class ClaimBackPrepayProxy implements com.sinosoft.sff.interf.ClaimBackPrepay {
  private String _endpoint = null;
  private com.sinosoft.sff.interf.ClaimBackPrepay claimBackPrepay = null;
  
  public ClaimBackPrepayProxy() {
    _initClaimBackPrepayProxy();
  }
  
  public ClaimBackPrepayProxy(String endpoint) {
    _endpoint = endpoint;
    _initClaimBackPrepayProxy();
  }
  
  private void _initClaimBackPrepayProxy() {
    try {
      claimBackPrepay = (new com.sinosoft.sff.interf.ClaimBackPrepayServiceLocator()).getClaimBackPrepayPort();
      if (claimBackPrepay != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)claimBackPrepay)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)claimBackPrepay)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (claimBackPrepay != null)
      ((javax.xml.rpc.Stub)claimBackPrepay)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sinosoft.sff.interf.ClaimBackPrepay getClaimBackPrepay() {
    if (claimBackPrepay == null)
      _initClaimBackPrepayProxy();
    return claimBackPrepay;
  }
  
  public java.lang.String backPrepayXml(java.lang.String arg0) throws java.rmi.RemoteException{
    if (claimBackPrepay == null)
      _initClaimBackPrepayProxy();
    return claimBackPrepay.backPrepayXml(arg0);
  }
  
  public java.lang.String backPrepay(com.sinosoft.sff.interf.ClaimMainVo arg0) throws java.rmi.RemoteException{
    if (claimBackPrepay == null)
      _initClaimBackPrepayProxy();
    return claimBackPrepay.backPrepay(arg0);
  }
  
  
}