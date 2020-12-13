/**
 * SetInterfaceLogInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.founder.webservice;

public class SetInterfaceLogInfo  implements java.io.Serializable {
    private ins.sino.claimcar.founder.webservice.InterfaceLogInfo interfaceLogInfo;

    public SetInterfaceLogInfo() {
    }

    public SetInterfaceLogInfo(
           ins.sino.claimcar.founder.webservice.InterfaceLogInfo interfaceLogInfo) {
           this.interfaceLogInfo = interfaceLogInfo;
    }


    /**
     * Gets the interfaceLogInfo value for this SetInterfaceLogInfo.
     * 
     * @return interfaceLogInfo
     */
    public ins.sino.claimcar.founder.webservice.InterfaceLogInfo getInterfaceLogInfo() {
        return interfaceLogInfo;
    }


    /**
     * Sets the interfaceLogInfo value for this SetInterfaceLogInfo.
     * 
     * @param interfaceLogInfo
     */
    public void setInterfaceLogInfo(ins.sino.claimcar.founder.webservice.InterfaceLogInfo interfaceLogInfo) {
        this.interfaceLogInfo = interfaceLogInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SetInterfaceLogInfo)) return false;
        SetInterfaceLogInfo other = (SetInterfaceLogInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.interfaceLogInfo==null && other.getInterfaceLogInfo()==null) || 
             (this.interfaceLogInfo!=null &&
              this.interfaceLogInfo.equals(other.getInterfaceLogInfo())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getInterfaceLogInfo() != null) {
            _hashCode += getInterfaceLogInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SetInterfaceLogInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.server.cc.order.com", ">setInterfaceLogInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interfaceLogInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.server.cc.order.com", "interfaceLogInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://exception.dh.cc.order.com/xsd", "InterfaceLogInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
