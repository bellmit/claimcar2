/**
 * JplanFeeVo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sinosoft.sff.interf;

public class JplanFeeVo  implements java.io.Serializable {
    private java.lang.String appliCode;

    private java.lang.String appliName;

    private java.lang.String currency;

    private com.sinosoft.sff.interf.JplanFeeDetailVo[] jplanFeeDetailVos;

    private java.lang.String payRefReason;

    private java.lang.Double planFee;

    private int serialNo;

    private java.util.Calendar underWriteDate;

    private java.lang.String voucherNo2;

    public JplanFeeVo() {
    }

    public JplanFeeVo(
           java.lang.String appliCode,
           java.lang.String appliName,
           java.lang.String currency,
           com.sinosoft.sff.interf.JplanFeeDetailVo[] jplanFeeDetailVos,
           java.lang.String payRefReason,
           java.lang.Double planFee,
           int serialNo,
           java.util.Calendar underWriteDate,
           java.lang.String voucherNo2) {
           this.appliCode = appliCode;
           this.appliName = appliName;
           this.currency = currency;
           this.jplanFeeDetailVos = jplanFeeDetailVos;
           this.payRefReason = payRefReason;
           this.planFee = planFee;
           this.serialNo = serialNo;
           this.underWriteDate = underWriteDate;
           this.voucherNo2 = voucherNo2;
    }


    /**
     * Gets the appliCode value for this JplanFeeVo.
     * 
     * @return appliCode
     */
    public java.lang.String getAppliCode() {
        return appliCode;
    }


    /**
     * Sets the appliCode value for this JplanFeeVo.
     * 
     * @param appliCode
     */
    public void setAppliCode(java.lang.String appliCode) {
        this.appliCode = appliCode;
    }


    /**
     * Gets the appliName value for this JplanFeeVo.
     * 
     * @return appliName
     */
    public java.lang.String getAppliName() {
        return appliName;
    }


    /**
     * Sets the appliName value for this JplanFeeVo.
     * 
     * @param appliName
     */
    public void setAppliName(java.lang.String appliName) {
        this.appliName = appliName;
    }


    /**
     * Gets the currency value for this JplanFeeVo.
     * 
     * @return currency
     */
    public java.lang.String getCurrency() {
        return currency;
    }


    /**
     * Sets the currency value for this JplanFeeVo.
     * 
     * @param currency
     */
    public void setCurrency(java.lang.String currency) {
        this.currency = currency;
    }


    /**
     * Gets the jplanFeeDetailVos value for this JplanFeeVo.
     * 
     * @return jplanFeeDetailVos
     */
    public com.sinosoft.sff.interf.JplanFeeDetailVo[] getJplanFeeDetailVos() {
        return jplanFeeDetailVos;
    }


    /**
     * Sets the jplanFeeDetailVos value for this JplanFeeVo.
     * 
     * @param jplanFeeDetailVos
     */
    public void setJplanFeeDetailVos(com.sinosoft.sff.interf.JplanFeeDetailVo[] jplanFeeDetailVos) {
        this.jplanFeeDetailVos = jplanFeeDetailVos;
    }

    public com.sinosoft.sff.interf.JplanFeeDetailVo getJplanFeeDetailVos(int i) {
        return this.jplanFeeDetailVos[i];
    }

    public void setJplanFeeDetailVos(int i, com.sinosoft.sff.interf.JplanFeeDetailVo _value) {
        this.jplanFeeDetailVos[i] = _value;
    }


    /**
     * Gets the payRefReason value for this JplanFeeVo.
     * 
     * @return payRefReason
     */
    public java.lang.String getPayRefReason() {
        return payRefReason;
    }


    /**
     * Sets the payRefReason value for this JplanFeeVo.
     * 
     * @param payRefReason
     */
    public void setPayRefReason(java.lang.String payRefReason) {
        this.payRefReason = payRefReason;
    }


    /**
     * Gets the planFee value for this JplanFeeVo.
     * 
     * @return planFee
     */
    public java.lang.Double getPlanFee() {
        return planFee;
    }


    /**
     * Sets the planFee value for this JplanFeeVo.
     * 
     * @param planFee
     */
    public void setPlanFee(java.lang.Double planFee) {
        this.planFee = planFee;
    }


    /**
     * Gets the serialNo value for this JplanFeeVo.
     * 
     * @return serialNo
     */
    public int getSerialNo() {
        return serialNo;
    }


    /**
     * Sets the serialNo value for this JplanFeeVo.
     * 
     * @param serialNo
     */
    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }


    /**
     * Gets the underWriteDate value for this JplanFeeVo.
     * 
     * @return underWriteDate
     */
    public java.util.Calendar getUnderWriteDate() {
        return underWriteDate;
    }


    /**
     * Sets the underWriteDate value for this JplanFeeVo.
     * 
     * @param underWriteDate
     */
    public void setUnderWriteDate(java.util.Calendar underWriteDate) {
        this.underWriteDate = underWriteDate;
    }


    /**
     * Gets the voucherNo2 value for this JplanFeeVo.
     * 
     * @return voucherNo2
     */
    public java.lang.String getVoucherNo2() {
        return voucherNo2;
    }


    /**
     * Sets the voucherNo2 value for this JplanFeeVo.
     * 
     * @param voucherNo2
     */
    public void setVoucherNo2(java.lang.String voucherNo2) {
        this.voucherNo2 = voucherNo2;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JplanFeeVo)) return false;
        JplanFeeVo other = (JplanFeeVo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.appliCode==null && other.getAppliCode()==null) || 
             (this.appliCode!=null &&
              this.appliCode.equals(other.getAppliCode()))) &&
            ((this.appliName==null && other.getAppliName()==null) || 
             (this.appliName!=null &&
              this.appliName.equals(other.getAppliName()))) &&
            ((this.currency==null && other.getCurrency()==null) || 
             (this.currency!=null &&
              this.currency.equals(other.getCurrency()))) &&
            ((this.jplanFeeDetailVos==null && other.getJplanFeeDetailVos()==null) || 
             (this.jplanFeeDetailVos!=null &&
              java.util.Arrays.equals(this.jplanFeeDetailVos, other.getJplanFeeDetailVos()))) &&
            ((this.payRefReason==null && other.getPayRefReason()==null) || 
             (this.payRefReason!=null &&
              this.payRefReason.equals(other.getPayRefReason()))) &&
            ((this.planFee==null && other.getPlanFee()==null) || 
             (this.planFee!=null &&
              this.planFee.equals(other.getPlanFee()))) &&
            this.serialNo == other.getSerialNo() &&
            ((this.underWriteDate==null && other.getUnderWriteDate()==null) || 
             (this.underWriteDate!=null &&
              this.underWriteDate.equals(other.getUnderWriteDate()))) &&
            ((this.voucherNo2==null && other.getVoucherNo2()==null) || 
             (this.voucherNo2!=null &&
              this.voucherNo2.equals(other.getVoucherNo2())));
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
        if (getAppliCode() != null) {
            _hashCode += getAppliCode().hashCode();
        }
        if (getAppliName() != null) {
            _hashCode += getAppliName().hashCode();
        }
        if (getCurrency() != null) {
            _hashCode += getCurrency().hashCode();
        }
        if (getJplanFeeDetailVos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJplanFeeDetailVos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJplanFeeDetailVos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPayRefReason() != null) {
            _hashCode += getPayRefReason().hashCode();
        }
        if (getPlanFee() != null) {
            _hashCode += getPlanFee().hashCode();
        }
        _hashCode += getSerialNo();
        if (getUnderWriteDate() != null) {
            _hashCode += getUnderWriteDate().hashCode();
        }
        if (getVoucherNo2() != null) {
            _hashCode += getVoucherNo2().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JplanFeeVo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "jplanFeeVo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appliCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "appliCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appliName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "appliName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currency");
        elemField.setXmlName(new javax.xml.namespace.QName("", "currency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jplanFeeDetailVos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "jplanFeeDetailVos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "jplanFeeDetailVo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payRefReason");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payRefReason"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("planFee");
        elemField.setXmlName(new javax.xml.namespace.QName("", "planFee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serialNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serialNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("underWriteDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "underWriteDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("voucherNo2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "voucherNo2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
