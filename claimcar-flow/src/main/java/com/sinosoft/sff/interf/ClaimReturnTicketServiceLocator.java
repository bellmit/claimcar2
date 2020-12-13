/**
 * ClaimReturnTicketServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sinosoft.sff.interf;

public class ClaimReturnTicketServiceLocator extends org.apache.axis.client.Service implements com.sinosoft.sff.interf.ClaimReturnTicketService {

    public ClaimReturnTicketServiceLocator() {
    }


    public ClaimReturnTicketServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ClaimReturnTicketServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ClaimReturnTicketPort
    private java.lang.String ClaimReturnTicketPort_address = "http://localhost:8080/payment/service/ClaimReturnTicket";

    public java.lang.String getClaimReturnTicketPortAddress() {
        return ClaimReturnTicketPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ClaimReturnTicketPortWSDDServiceName = "ClaimReturnTicketPort";

    public java.lang.String getClaimReturnTicketPortWSDDServiceName() {
        return ClaimReturnTicketPortWSDDServiceName;
    }

    public void setClaimReturnTicketPortWSDDServiceName(java.lang.String name) {
        ClaimReturnTicketPortWSDDServiceName = name;
    }

    public com.sinosoft.sff.interf.ClaimReturnTicket getClaimReturnTicketPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ClaimReturnTicketPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getClaimReturnTicketPort(endpoint);
    }

    public com.sinosoft.sff.interf.ClaimReturnTicket getClaimReturnTicketPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sinosoft.sff.interf.ClaimReturnTicketPortBindingStub _stub = new com.sinosoft.sff.interf.ClaimReturnTicketPortBindingStub(portAddress, this);
            _stub.setPortName(getClaimReturnTicketPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setClaimReturnTicketPortEndpointAddress(java.lang.String address) {
        ClaimReturnTicketPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sinosoft.sff.interf.ClaimReturnTicket.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sinosoft.sff.interf.ClaimReturnTicketPortBindingStub _stub = new com.sinosoft.sff.interf.ClaimReturnTicketPortBindingStub(new java.net.URL(ClaimReturnTicketPort_address), this);
                _stub.setPortName(getClaimReturnTicketPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ClaimReturnTicketPort".equals(inputPortName)) {
            return getClaimReturnTicketPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "ClaimReturnTicketService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "ClaimReturnTicketPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ClaimReturnTicketPort".equals(portName)) {
            setClaimReturnTicketPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
