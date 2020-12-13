/**
 * LeapLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.longrise.gxService;

public class LeapLocator extends org.apache.axis.client.Service implements ins.sino.claimcar.longrise.gxService.Leap {

    public LeapLocator() {
    }


    public LeapLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LeapLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for leapPort
    private java.lang.String leapPort_address = "http://59.173.241.186:21980/GXISTEST/services/com.longrise.services.leap";

    public java.lang.String getleapPortAddress() {
        return leapPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String leapPortWSDDServiceName = "leapPort";

    public java.lang.String getleapPortWSDDServiceName() {
        return leapPortWSDDServiceName;
    }

    public void setleapPortWSDDServiceName(java.lang.String name) {
        leapPortWSDDServiceName = name;
    }

    public ins.sino.claimcar.longrise.gxService.Ileap getleapPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(leapPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getleapPort(endpoint);
    }

    public ins.sino.claimcar.longrise.gxService.Ileap getleapPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ins.sino.claimcar.longrise.gxService.LeapSoapBindingStub _stub = new ins.sino.claimcar.longrise.gxService.LeapSoapBindingStub(portAddress, this);
            _stub.setPortName(getleapPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setleapPortEndpointAddress(java.lang.String address) {
        leapPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ins.sino.claimcar.longrise.gxService.Ileap.class.isAssignableFrom(serviceEndpointInterface)) {
                ins.sino.claimcar.longrise.gxService.LeapSoapBindingStub _stub = new ins.sino.claimcar.longrise.gxService.LeapSoapBindingStub(new java.net.URL(leapPort_address), this);
                _stub.setPortName(getleapPortWSDDServiceName());
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
        if ("leapPort".equals(inputPortName)) {
            return getleapPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://services.longrise.com/", "leap");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://services.longrise.com/", "leapPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("leapPort".equals(portName)) {
            setleapPortEndpointAddress(address);
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
