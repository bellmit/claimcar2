package ins.sino.claimcar.interf.service;

public class AccRollBackAccountServiceProxy implements ins.sino.claimcar.interf.service.AccRollBackAccountService {
  private String _endpoint = null;
  private ins.sino.claimcar.interf.service.AccRollBackAccountService accRollBackAccountService = null;
  
  public AccRollBackAccountServiceProxy() {
    _initAccRollBackAccountServiceProxy();
  }
  
  public AccRollBackAccountServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initAccRollBackAccountServiceProxy();
  }
  
  private void _initAccRollBackAccountServiceProxy() {
    try {
      accRollBackAccountService = (new ins.sino.claimcar.interf.service.AccRollBackAccountLocator()).getAccRollBackAccountServicePort();
      if (accRollBackAccountService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)accRollBackAccountService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)accRollBackAccountService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (accRollBackAccountService != null)
      ((javax.xml.rpc.Stub)accRollBackAccountService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ins.sino.claimcar.interf.service.AccRollBackAccountService getAccRollBackAccountService() {
    if (accRollBackAccountService == null)
      _initAccRollBackAccountServiceProxy();
    return accRollBackAccountService;
  }
  
  public java.lang.String saveAccRollBackAccountForXml(java.lang.String arg0) throws java.rmi.RemoteException{
    if (accRollBackAccountService == null)
      _initAccRollBackAccountServiceProxy();
    return accRollBackAccountService.saveAccRollBackAccountForXml(arg0);
  }
  
  
}