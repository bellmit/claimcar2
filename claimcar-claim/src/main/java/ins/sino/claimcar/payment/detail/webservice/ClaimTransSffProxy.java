package ins.sino.claimcar.payment.detail.webservice;

public class ClaimTransSffProxy implements ins.sino.claimcar.payment.detail.webservice.ClaimTransSff {
  private String _endpoint = null;
  private ins.sino.claimcar.payment.detail.webservice.ClaimTransSff claimTransSff = null;
  
  public ClaimTransSffProxy() {
    _initClaimTransSffProxy();
  }
  
  public ClaimTransSffProxy(String endpoint) {
    _endpoint = endpoint;
    _initClaimTransSffProxy();
  }
  
  private void _initClaimTransSffProxy() {
    try {
      claimTransSff = (new ins.sino.claimcar.payment.detail.webservice.ClaimTransSffServiceLocator()).getClaimTransSffPort();
      if (claimTransSff != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)claimTransSff)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)claimTransSff)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (claimTransSff != null)
      ((javax.xml.rpc.Stub)claimTransSff)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ins.sino.claimcar.payment.detail.webservice.ClaimTransSff getClaimTransSff() {
    if (claimTransSff == null)
      _initClaimTransSffProxy();
    return claimTransSff;
  }
  
  public java.lang.String transDataForVo(ins.sino.claimcar.payment.detail.vo.JPlanMainVo arg0) throws java.rmi.RemoteException{
    if (claimTransSff == null)
      _initClaimTransSffProxy();
    return claimTransSff.transDataForVo(arg0);
  }
  
  public java.lang.String transDataForXml(java.lang.String arg0) throws java.rmi.RemoteException{
    if (claimTransSff == null)
      _initClaimTransSffProxy();
    return claimTransSff.transDataForXml(arg0);
  }
  
  
}