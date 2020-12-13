/**
 * Ws.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.founder.webservice;

public interface Ws extends javax.xml.rpc.Service {
    public java.lang.String getwsHttpSoap11EndpointAddress();

    public ins.sino.claimcar.founder.webservice.WsPortType getwsHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException;

    public ins.sino.claimcar.founder.webservice.WsPortType getwsHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getwsHttpSoap12EndpointAddress();

    public ins.sino.claimcar.founder.webservice.WsPortType getwsHttpSoap12Endpoint() throws javax.xml.rpc.ServiceException;

    public ins.sino.claimcar.founder.webservice.WsPortType getwsHttpSoap12Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
