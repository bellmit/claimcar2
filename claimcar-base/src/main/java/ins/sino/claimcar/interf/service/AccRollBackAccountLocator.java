/**
 * AccRollBackAccountLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.interf.service;

public class AccRollBackAccountLocator extends org.apache.axis.client.Service implements ins.sino.claimcar.interf.service.AccRollBackAccount {

    public AccRollBackAccountLocator() {
    }


    public AccRollBackAccountLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AccRollBackAccountLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AccRollBackAccountServicePort
    private java.lang.String AccRollBackAccountServicePort_address = "http://10.236.0.161:8080/claimcar-claim/webservice/accRollBackAccount";

    public java.lang.String getAccRollBackAccountServicePortAddress() {
        return AccRollBackAccountServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AccRollBackAccountServicePortWSDDServiceName = "AccRollBackAccountServicePort";

    public java.lang.String getAccRollBackAccountServicePortWSDDServiceName() {
        return AccRollBackAccountServicePortWSDDServiceName;
    }

    public void setAccRollBackAccountServicePortWSDDServiceName(java.lang.String name) {
        AccRollBackAccountServicePortWSDDServiceName = name;
    }

    public ins.sino.claimcar.interf.service.AccRollBackAccountService getAccRollBackAccountServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AccRollBackAccountServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAccRollBackAccountServicePort(endpoint);
    }

    public ins.sino.claimcar.interf.service.AccRollBackAccountService getAccRollBackAccountServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ins.sino.claimcar.interf.service.AccRollBackAccountServicePortBindingStub _stub = new ins.sino.claimcar.interf.service.AccRollBackAccountServicePortBindingStub(portAddress, this);
            _stub.setPortName(getAccRollBackAccountServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAccRollBackAccountServicePortEndpointAddress(java.lang.String address) {
        AccRollBackAccountServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ins.sino.claimcar.interf.service.AccRollBackAccountService.class.isAssignableFrom(serviceEndpointInterface)) {
                ins.sino.claimcar.interf.service.AccRollBackAccountServicePortBindingStub _stub = new ins.sino.claimcar.interf.service.AccRollBackAccountServicePortBindingStub(new java.net.URL(AccRollBackAccountServicePort_address), this);
                _stub.setPortName(getAccRollBackAccountServicePortWSDDServiceName());
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
        if ("AccRollBackAccountServicePort".equals(inputPortName)) {
            return getAccRollBackAccountServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.interf.claimcar.sino.ins/", "accRollBackAccount");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.interf.claimcar.sino.ins/", "AccRollBackAccountServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AccRollBackAccountServicePort".equals(portName)) {
            setAccRollBackAccountServicePortEndpointAddress(address);
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
