/**
 * JplanFeeDetailVo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sinosoft.sff.interf;

public class JplanFeeDetailVo  implements java.io.Serializable {
    private java.lang.String kindCode;

    private java.lang.Double planfee;

    public JplanFeeDetailVo() {
    }

    public JplanFeeDetailVo(
           java.lang.String kindCode,
           java.lang.Double planfee) {
           this.kindCode = kindCode;
           this.planfee = planfee;
    }


    /**
     * Gets the kindCode value for this JplanFeeDetailVo.
     * 
     * @return kindCode
     */
    public java.lang.String getKindCode() {
        return kindCode;
    }


    /**
     * Sets the kindCode value for this JplanFeeDetailVo.
     * 
     * @param kindCode
     */
    public void setKindCode(java.lang.String kindCode) {
        this.kindCode = kindCode;
    }


    /**
     * Gets the planfee value for this JplanFeeDetailVo.
     * 
     * @return planfee
     */
    public java.lang.Double getPlanfee() {
        return planfee;
    }


    /**
     * Sets the planfee value for this JplanFeeDetailVo.
     * 
     * @param planfee
     */
    public void setPlanfee(java.lang.Double planfee) {
        this.planfee = planfee;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JplanFeeDetailVo)) return false;
        JplanFeeDetailVo other = (JplanFeeDetailVo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.kindCode==null && other.getKindCode()==null) || 
             (this.kindCode!=null &&
              this.kindCode.equals(other.getKindCode()))) &&
            ((this.planfee==null && other.getPlanfee()==null) || 
             (this.planfee!=null &&
              this.planfee.equals(other.getPlanfee())));
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
        if (getKindCode() != null) {
            _hashCode += getKindCode().hashCode();
        }
        if (getPlanfee() != null) {
            _hashCode += getPlanfee().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JplanFeeDetailVo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "jplanFeeDetailVo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kindCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "kindCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("planfee");
        elemField.setXmlName(new javax.xml.namespace.QName("", "planfee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
