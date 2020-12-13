package ins.sino.claimcar.longrise.services;

public class IleapProxy implements ins.sino.claimcar.longrise.services.Ileap {
  private String _endpoint = null;
  private ins.sino.claimcar.longrise.services.Ileap ileap = null;
  
  public IleapProxy() {
    _initIleapProxy();
  }
  
  public IleapProxy(String endpoint) {
    _endpoint = endpoint;
    _initIleapProxy();
  }
  
  private void _initIleapProxy() {
    try {
      ileap = (new ins.sino.claimcar.longrise.services.LeapLocator()).getleapPort();
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
  
  public ins.sino.claimcar.longrise.services.Ileap getIleap() {
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
  
  
}