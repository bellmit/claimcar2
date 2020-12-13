/**
 * WsLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.founder.webservice;

public class WsLocator extends org.apache.axis.client.Service implements ins.sino.claimcar.founder.webservice.Ws {

    public WsLocator() {
    }


    public WsLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WsLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for wsHttpSoap11Endpoint
    private java.lang.String wsHttpSoap11Endpoint_address = "http://10.156.69.200:7001/webservice/services/ws.wsHttpSoap11Endpoint/";

    public java.lang.String getwsHttpSoap11EndpointAddress() {
        return wsHttpSoap11Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String wsHttpSoap11EndpointWSDDServiceName = "wsHttpSoap11Endpoint";

    public java.lang.String getwsHttpSoap11EndpointWSDDServiceName() {
        return wsHttpSoap11EndpointWSDDServiceName;
    }

    public void setwsHttpSoap11EndpointWSDDServiceName(java.lang.String name) {
        wsHttpSoap11EndpointWSDDServiceName = name;
    }

    public ins.sino.claimcar.founder.webservice.WsPortType getwsHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(wsHttpSoap11Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getwsHttpSoap11Endpoint(endpoint);
    }

    public ins.sino.claimcar.founder.webservice.WsPortType getwsHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ins.sino.claimcar.founder.webservice.WsSoap11BindingStub _stub = new ins.sino.claimcar.founder.webservice.WsSoap11BindingStub(portAddress, this);
            _stub.setPortName(getwsHttpSoap11EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setwsHttpSoap11EndpointEndpointAddress(java.lang.String address) {
        wsHttpSoap11Endpoint_address = address;
    }


    // Use to get a proxy class for wsHttpSoap12Endpoint
    private java.lang.String wsHttpSoap12Endpoint_address = "http://10.156.69.200:7001/webservice/services/ws.wsHttpSoap12Endpoint/";

    public java.lang.String getwsHttpSoap12EndpointAddress() {
        return wsHttpSoap12Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String wsHttpSoap12EndpointWSDDServiceName = "wsHttpSoap12Endpoint";

    public java.lang.String getwsHttpSoap12EndpointWSDDServiceName() {
        return wsHttpSoap12EndpointWSDDServiceName;
    }

    public void setwsHttpSoap12EndpointWSDDServiceName(java.lang.String name) {
        wsHttpSoap12EndpointWSDDServiceName = name;
    }

    public ins.sino.claimcar.founder.webservice.WsPortType getwsHttpSoap12Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(wsHttpSoap12Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getwsHttpSoap12Endpoint(endpoint);
    }

    public ins.sino.claimcar.founder.webservice.WsPortType getwsHttpSoap12Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ins.sino.claimcar.founder.webservice.WsSoap12BindingStub _stub = new ins.sino.claimcar.founder.webservice.WsSoap12BindingStub(portAddress, this);
            _stub.setPortName(getwsHttpSoap12EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setwsHttpSoap12EndpointEndpointAddress(java.lang.String address) {
        wsHttpSoap12Endpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ins.sino.claimcar.founder.webservice.WsPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                ins.sino.claimcar.founder.webservice.WsSoap11BindingStub _stub = new ins.sino.claimcar.founder.webservice.WsSoap11BindingStub(new java.net.URL(wsHttpSoap11Endpoint_address), this);
                _stub.setPortName(getwsHttpSoap11EndpointWSDDServiceName());
                return _stub;
            }
            if (ins.sino.claimcar.founder.webservice.WsPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                ins.sino.claimcar.founder.webservice.WsSoap12BindingStub _stub = new ins.sino.claimcar.founder.webservice.WsSoap12BindingStub(new java.net.URL(wsHttpSoap12Endpoint_address), this);
                _stub.setPortName(getwsHttpSoap12EndpointWSDDServiceName());
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
        if ("wsHttpSoap11Endpoint".equals(inputPortName)) {
            return getwsHttpSoap11Endpoint();
        }
        else if ("wsHttpSoap12Endpoint".equals(inputPortName)) {
            return getwsHttpSoap12Endpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.server.cc.order.com", "ws");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.server.cc.order.com", "wsHttpSoap11Endpoint"));
            ports.add(new javax.xml.namespace.QName("http://ws.server.cc.order.com", "wsHttpSoap12Endpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("wsHttpSoap11Endpoint".equals(portName)) {
            setwsHttpSoap11EndpointEndpointAddress(address);
        }
        else 
if ("wsHttpSoap12Endpoint".equals(portName)) {
            setwsHttpSoap12EndpointEndpointAddress(address);
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
