/**
 * DhCoreSmsService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.founder.webservice;

public class DhCoreSmsService  implements java.io.Serializable {
    private ins.sino.claimcar.founder.webservice.CoreCtService coreCtService;

    private ins.sino.claimcar.founder.webservice.IDhCoreSmsDAO dhCoreSmsDAO;

    public DhCoreSmsService() {
    }

    public DhCoreSmsService(
           ins.sino.claimcar.founder.webservice.CoreCtService coreCtService,
           ins.sino.claimcar.founder.webservice.IDhCoreSmsDAO dhCoreSmsDAO) {
           this.coreCtService = coreCtService;
           this.dhCoreSmsDAO = dhCoreSmsDAO;
    }


    /**
     * Gets the coreCtService value for this DhCoreSmsService.
     * 
     * @return coreCtService
     */
    public ins.sino.claimcar.founder.webservice.CoreCtService getCoreCtService() {
        return coreCtService;
    }


    /**
     * Sets the coreCtService value for this DhCoreSmsService.
     * 
     * @param coreCtService
     */
    public void setCoreCtService(ins.sino.claimcar.founder.webservice.CoreCtService coreCtService) {
        this.coreCtService = coreCtService;
    }


    /**
     * Gets the dhCoreSmsDAO value for this DhCoreSmsService.
     * 
     * @return dhCoreSmsDAO
     */
    public ins.sino.claimcar.founder.webservice.IDhCoreSmsDAO getDhCoreSmsDAO() {
        return dhCoreSmsDAO;
    }


    /**
     * Sets the dhCoreSmsDAO value for this DhCoreSmsService.
     * 
     * @param dhCoreSmsDAO
     */
    public void setDhCoreSmsDAO(ins.sino.claimcar.founder.webservice.IDhCoreSmsDAO dhCoreSmsDAO) {
        this.dhCoreSmsDAO = dhCoreSmsDAO;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DhCoreSmsService)) return false;
        DhCoreSmsService other = (DhCoreSmsService) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.coreCtService==null && other.getCoreCtService()==null) || 
             (this.coreCtService!=null &&
              this.coreCtService.equals(other.getCoreCtService()))) &&
            ((this.dhCoreSmsDAO==null && other.getDhCoreSmsDAO()==null) || 
             (this.dhCoreSmsDAO!=null &&
              this.dhCoreSmsDAO.equals(other.getDhCoreSmsDAO())));
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
        if (getCoreCtService() != null) {
            _hashCode += getCoreCtService().hashCode();
        }
        if (getDhCoreSmsDAO() != null) {
            _hashCode += getDhCoreSmsDAO().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DhCoreSmsService.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://svc.sms.platform.cc.order.com/xsd", "DhCoreSmsService"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coreCtService");
        elemField.setXmlName(new javax.xml.namespace.QName("http://svc.sms.platform.cc.order.com/xsd", "coreCtService"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://interfaces.dh.cc.order.com/xsd", "CoreCtService"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dhCoreSmsDAO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://svc.sms.platform.cc.order.com/xsd", "dhCoreSmsDAO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://idao.sms.platform.cc.order.com/xsd", "IDhCoreSmsDAO"));
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
