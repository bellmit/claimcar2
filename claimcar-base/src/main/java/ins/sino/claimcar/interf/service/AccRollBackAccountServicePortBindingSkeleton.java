/**
 * AccRollBackAccountServicePortBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.interf.service;

public class AccRollBackAccountServicePortBindingSkeleton implements ins.sino.claimcar.interf.service.AccRollBackAccountService, org.apache.axis.wsdl.Skeleton {
    private ins.sino.claimcar.interf.service.AccRollBackAccountService impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "arg0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("saveAccRollBackAccountForXml", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://service.interf.claimcar.sino.ins/", "saveAccRollBackAccountForXml"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("saveAccRollBackAccountForXml") == null) {
            _myOperations.put("saveAccRollBackAccountForXml", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("saveAccRollBackAccountForXml")).add(_oper);
    }

    public AccRollBackAccountServicePortBindingSkeleton() {
        this.impl = new ins.sino.claimcar.interf.service.AccRollBackAccountServicePortBindingImpl();
    }

    public AccRollBackAccountServicePortBindingSkeleton(ins.sino.claimcar.interf.service.AccRollBackAccountService impl) {
        this.impl = impl;
    }
    public java.lang.String saveAccRollBackAccountForXml(java.lang.String arg0) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.saveAccRollBackAccountForXml(arg0);
        return ret;
    }

}
