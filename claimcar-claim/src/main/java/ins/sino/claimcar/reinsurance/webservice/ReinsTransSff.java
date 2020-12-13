/**
 * ReinsTransSff.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.reinsurance.webservice;

public interface ReinsTransSff extends java.rmi.Remote {
    public java.lang.String transDataForClaimVo(ins.sino.claimcar.reinsurance.vo.ClaimVo arg0) throws java.rmi.RemoteException;
    public java.lang.String transDataForClaimXml(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String transDataForCompensateVo(ins.sino.claimcar.reinsurance.vo.CompensateVo arg0) throws java.rmi.RemoteException;
    public java.lang.String transDataForcompensateXml(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String transDataForReinsCaseVo(ins.sino.claimcar.reinsurance.vo.ReinsCaseStatusVO arg0) throws java.rmi.RemoteException;
    public java.lang.String transDataForreinsCaseXml(java.lang.String arg0) throws java.rmi.RemoteException;
}
