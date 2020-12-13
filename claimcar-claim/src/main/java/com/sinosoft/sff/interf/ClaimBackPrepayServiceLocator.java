/**
 * ClaimBackPrepayServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sinosoft.sff.interf;

public class ClaimBackPrepayServiceLocator extends org.apache.axis.client.Service implements com.sinosoft.sff.interf.ClaimBackPrepayService {

    public ClaimBackPrepayServiceLocator() {
    }


    public ClaimBackPrepayServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ClaimBackPrepayServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ClaimBackPrepayPort
    private java.lang.String ClaimBackPrepayPort_address = "http://10.236.0.215:8080/payment/service/claimBackPrepay";

    public java.lang.String getClaimBackPrepayPortAddress() {
        return ClaimBackPrepayPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ClaimBackPrepayPortWSDDServiceName = "ClaimBackPrepayPort";

    public java.lang.String getClaimBackPrepayPortWSDDServiceName() {
        return ClaimBackPrepayPortWSDDServiceName;
    }

    public void setClaimBackPrepayPortWSDDServiceName(java.lang.String name) {
        ClaimBackPrepayPortWSDDServiceName = name;
    }

    public com.sinosoft.sff.interf.ClaimBackPrepay getClaimBackPrepayPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ClaimBackPrepayPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getClaimBackPrepayPort(endpoint);
    }

    public com.sinosoft.sff.interf.ClaimBackPrepay getClaimBackPrepayPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sinosoft.sff.interf.ClaimBackPrepayPortBindingStub _stub = new com.sinosoft.sff.interf.ClaimBackPrepayPortBindingStub(portAddress, this);
            _stub.setPortName(getClaimBackPrepayPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setClaimBackPrepayPortEndpointAddress(java.lang.String address) {
        ClaimBackPrepayPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sinosoft.sff.interf.ClaimBackPrepay.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sinosoft.sff.interf.ClaimBackPrepayPortBindingStub _stub = new com.sinosoft.sff.interf.ClaimBackPrepayPortBindingStub(new java.net.URL(ClaimBackPrepayPort_address), this);
                _stub.setPortName(getClaimBackPrepayPortWSDDServiceName());
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
        if ("ClaimBackPrepayPort".equals(inputPortName)) {
            return getClaimBackPrepayPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "ClaimBackPrepayService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "ClaimBackPrepayPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ClaimBackPrepayPort".equals(portName)) {
            setClaimBackPrepayPortEndpointAddress(address);
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
