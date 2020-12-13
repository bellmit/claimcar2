/**
 * ReinsTransSffServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.reinsurance.webservice;

public class ReinsTransSffServiceLocator extends org.apache.axis.client.Service implements ins.sino.claimcar.reinsurance.webservice.ReinsTransSffService {

    public ReinsTransSffServiceLocator() {
    }


    public ReinsTransSffServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ReinsTransSffServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ReinsTransSffPort
    private java.lang.String ReinsTransSffPort_address = "http://10.0.47.101:7011/reins/service/ReinsTransSff";

    public java.lang.String getReinsTransSffPortAddress() {
        return ReinsTransSffPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ReinsTransSffPortWSDDServiceName = "ReinsTransSffPort";

    public java.lang.String getReinsTransSffPortWSDDServiceName() {
        return ReinsTransSffPortWSDDServiceName;
    }

    public void setReinsTransSffPortWSDDServiceName(java.lang.String name) {
        ReinsTransSffPortWSDDServiceName = name;
    }

    public ins.sino.claimcar.reinsurance.webservice.ReinsTransSff getReinsTransSffPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ReinsTransSffPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getReinsTransSffPort(endpoint);
    }

    public ins.sino.claimcar.reinsurance.webservice.ReinsTransSff getReinsTransSffPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ins.sino.claimcar.reinsurance.webservice.ReinsTransSffPortBindingStub _stub = new ins.sino.claimcar.reinsurance.webservice.ReinsTransSffPortBindingStub(portAddress, this);
            _stub.setPortName(getReinsTransSffPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setReinsTransSffPortEndpointAddress(java.lang.String address) {
        ReinsTransSffPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ins.sino.claimcar.reinsurance.webservice.ReinsTransSff.class.isAssignableFrom(serviceEndpointInterface)) {
                ins.sino.claimcar.reinsurance.webservice.ReinsTransSffPortBindingStub _stub = new ins.sino.claimcar.reinsurance.webservice.ReinsTransSffPortBindingStub(new java.net.URL(ReinsTransSffPort_address), this);
                _stub.setPortName(getReinsTransSffPortWSDDServiceName());
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
        if ("ReinsTransSffPort".equals(inputPortName)) {
            return getReinsTransSffPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://interf.reins.sinosoft.com/", "ReinsTransSffService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://interf.reins.sinosoft.com/", "ReinsTransSffPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ReinsTransSffPort".equals(portName)) {
            setReinsTransSffPortEndpointAddress(address);
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
