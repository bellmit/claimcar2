/**
 * ClaimTransSffServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.payment.webservice;

public class ClaimTransSffServiceLocator extends org.apache.axis.client.Service implements ins.sino.claimcar.payment.webservice.ClaimTransSffService {

    public ClaimTransSffServiceLocator() {
    }


    public ClaimTransSffServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ClaimTransSffServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ClaimTransSffPort
    private java.lang.String ClaimTransSffPort_address = "http://10.0.47.101:7012/payment/service/claimTransSff";

    public java.lang.String getClaimTransSffPortAddress() {
        return ClaimTransSffPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ClaimTransSffPortWSDDServiceName = "ClaimTransSffPort";

    public java.lang.String getClaimTransSffPortWSDDServiceName() {
        return ClaimTransSffPortWSDDServiceName;
    }

    public void setClaimTransSffPortWSDDServiceName(java.lang.String name) {
        ClaimTransSffPortWSDDServiceName = name;
    }

    public ins.sino.claimcar.payment.webservice.ClaimTransSff getClaimTransSffPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ClaimTransSffPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getClaimTransSffPort(endpoint);
    }

    public ins.sino.claimcar.payment.webservice.ClaimTransSff getClaimTransSffPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ins.sino.claimcar.payment.webservice.ClaimTransSffPortBindingStub _stub = new ins.sino.claimcar.payment.webservice.ClaimTransSffPortBindingStub(portAddress, this);
            _stub.setPortName(getClaimTransSffPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setClaimTransSffPortEndpointAddress(java.lang.String address) {
        ClaimTransSffPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ins.sino.claimcar.payment.webservice.ClaimTransSff.class.isAssignableFrom(serviceEndpointInterface)) {
                ins.sino.claimcar.payment.webservice.ClaimTransSffPortBindingStub _stub = new ins.sino.claimcar.payment.webservice.ClaimTransSffPortBindingStub(new java.net.URL(ClaimTransSffPort_address), this);
                _stub.setPortName(getClaimTransSffPortWSDDServiceName());
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
        if ("ClaimTransSffPort".equals(inputPortName)) {
            return getClaimTransSffPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "ClaimTransSffService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "ClaimTransSffPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ClaimTransSffPort".equals(portName)) {
            setClaimTransSffPortEndpointAddress(address);
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
