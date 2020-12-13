/**
 * WsPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.founder.webservice;

public interface WsPortType extends java.rmi.Remote {
    public void setDistributionService(ins.sino.claimcar.founder.webservice.DistributionService distributionService) throws java.rmi.RemoteException;
    public void setInterfaceLogInfo(ins.sino.claimcar.founder.webservice.InterfaceLogInfo interfaceLogInfo) throws java.rmi.RemoteException;
    public java.lang.String send(java.lang.String requestXml) throws java.rmi.RemoteException;
}
