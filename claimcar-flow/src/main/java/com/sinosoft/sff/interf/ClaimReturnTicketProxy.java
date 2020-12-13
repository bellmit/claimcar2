package com.sinosoft.sff.interf;

public class ClaimReturnTicketProxy implements com.sinosoft.sff.interf.ClaimReturnTicket {
  private String _endpoint = null;
  private com.sinosoft.sff.interf.ClaimReturnTicket claimReturnTicket = null;
  
  public ClaimReturnTicketProxy() {
    _initClaimReturnTicketProxy();
  }
  
  public ClaimReturnTicketProxy(String endpoint) {
    _endpoint = endpoint;
    _initClaimReturnTicketProxy();
  }
  
  private void _initClaimReturnTicketProxy() {
    try {
      claimReturnTicket = (new com.sinosoft.sff.interf.ClaimReturnTicketServiceLocator()).getClaimReturnTicketPort();
      if (claimReturnTicket != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)claimReturnTicket)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)claimReturnTicket)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (claimReturnTicket != null)
      ((javax.xml.rpc.Stub)claimReturnTicket)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sinosoft.sff.interf.ClaimReturnTicket getClaimReturnTicket() {
    if (claimReturnTicket == null)
      _initClaimReturnTicketProxy();
    return claimReturnTicket;
  }
  
  public java.lang.String transPoliceForVo(com.sinosoft.sff.interf.AccMainVo arg0) throws java.rmi.RemoteException{
    if (claimReturnTicket == null)
      _initClaimReturnTicketProxy();
    return claimReturnTicket.transPoliceForVo(arg0);
  }
  
  public java.lang.String transPoliceDataForXml(java.lang.String arg0) throws java.rmi.RemoteException{
    if (claimReturnTicket == null)
      _initClaimReturnTicketProxy();
    return claimReturnTicket.transPoliceDataForXml(arg0);
  }
  
  
}