/**
 * JlinkAccountVo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sinosoft.sff.interf;

public class JlinkAccountVo  implements java.io.Serializable {
    private java.lang.String accountNo;

    private java.lang.String centiCode;

    private java.lang.String centiType;

    private java.lang.String payObject;

    private java.lang.String payReason;

    private java.lang.String payReasonFlag;

    private java.lang.String payRefReason;

    private java.lang.Double planFee;

    private java.lang.String subPayObject;

    public JlinkAccountVo() {
    }

    public JlinkAccountVo(
           java.lang.String accountNo,
           java.lang.String centiCode,
           java.lang.String centiType,
           java.lang.String payObject,
           java.lang.String payReason,
           java.lang.String payReasonFlag,
           java.lang.String payRefReason,
           java.lang.Double planFee,
           java.lang.String subPayObject) {
           this.accountNo = accountNo;
           this.centiCode = centiCode;
           this.centiType = centiType;
           this.payObject = payObject;
           this.payReason = payReason;
           this.payReasonFlag = payReasonFlag;
           this.payRefReason = payRefReason;
           this.planFee = planFee;
           this.subPayObject = subPayObject;
    }


    /**
     * Gets the accountNo value for this JlinkAccountVo.
     * 
     * @return accountNo
     */
    public java.lang.String getAccountNo() {
        return accountNo;
    }


    /**
     * Sets the accountNo value for this JlinkAccountVo.
     * 
     * @param accountNo
     */
    public void setAccountNo(java.lang.String accountNo) {
        this.accountNo = accountNo;
    }


    /**
     * Gets the centiCode value for this JlinkAccountVo.
     * 
     * @return centiCode
     */
    public java.lang.String getCentiCode() {
        return centiCode;
    }


    /**
     * Sets the centiCode value for this JlinkAccountVo.
     * 
     * @param centiCode
     */
    public void setCentiCode(java.lang.String centiCode) {
        this.centiCode = centiCode;
    }


    /**
     * Gets the centiType value for this JlinkAccountVo.
     * 
     * @return centiType
     */
    public java.lang.String getCentiType() {
        return centiType;
    }


    /**
     * Sets the centiType value for this JlinkAccountVo.
     * 
     * @param centiType
     */
    public void setCentiType(java.lang.String centiType) {
        this.centiType = centiType;
    }


    /**
     * Gets the payObject value for this JlinkAccountVo.
     * 
     * @return payObject
     */
    public java.lang.String getPayObject() {
        return payObject;
    }


    /**
     * Sets the payObject value for this JlinkAccountVo.
     * 
     * @param payObject
     */
    public void setPayObject(java.lang.String payObject) {
        this.payObject = payObject;
    }


    /**
     * Gets the payReason value for this JlinkAccountVo.
     * 
     * @return payReason
     */
    public java.lang.String getPayReason() {
        return payReason;
    }


    /**
     * Sets the payReason value for this JlinkAccountVo.
     * 
     * @param payReason
     */
    public void setPayReason(java.lang.String payReason) {
        this.payReason = payReason;
    }


    /**
     * Gets the payReasonFlag value for this JlinkAccountVo.
     * 
     * @return payReasonFlag
     */
    public java.lang.String getPayReasonFlag() {
        return payReasonFlag;
    }


    /**
     * Sets the payReasonFlag value for this JlinkAccountVo.
     * 
     * @param payReasonFlag
     */
    public void setPayReasonFlag(java.lang.String payReasonFlag) {
        this.payReasonFlag = payReasonFlag;
    }


    /**
     * Gets the payRefReason value for this JlinkAccountVo.
     * 
     * @return payRefReason
     */
    public java.lang.String getPayRefReason() {
        return payRefReason;
    }


    /**
     * Sets the payRefReason value for this JlinkAccountVo.
     * 
     * @param payRefReason
     */
    public void setPayRefReason(java.lang.String payRefReason) {
        this.payRefReason = payRefReason;
    }


    /**
     * Gets the planFee value for this JlinkAccountVo.
     * 
     * @return planFee
     */
    public java.lang.Double getPlanFee() {
        return planFee;
    }


    /**
     * Sets the planFee value for this JlinkAccountVo.
     * 
     * @param planFee
     */
    public void setPlanFee(java.lang.Double planFee) {
        this.planFee = planFee;
    }


    /**
     * Gets the subPayObject value for this JlinkAccountVo.
     * 
     * @return subPayObject
     */
    public java.lang.String getSubPayObject() {
        return subPayObject;
    }


    /**
     * Sets the subPayObject value for this JlinkAccountVo.
     * 
     * @param subPayObject
     */
    public void setSubPayObject(java.lang.String subPayObject) {
        this.subPayObject = subPayObject;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JlinkAccountVo)) return false;
        JlinkAccountVo other = (JlinkAccountVo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.accountNo==null && other.getAccountNo()==null) || 
             (this.accountNo!=null &&
              this.accountNo.equals(other.getAccountNo()))) &&
            ((this.centiCode==null && other.getCentiCode()==null) || 
             (this.centiCode!=null &&
              this.centiCode.equals(other.getCentiCode()))) &&
            ((this.centiType==null && other.getCentiType()==null) || 
             (this.centiType!=null &&
              this.centiType.equals(other.getCentiType()))) &&
            ((this.payObject==null && other.getPayObject()==null) || 
             (this.payObject!=null &&
              this.payObject.equals(other.getPayObject()))) &&
            ((this.payReason==null && other.getPayReason()==null) || 
             (this.payReason!=null &&
              this.payReason.equals(other.getPayReason()))) &&
            ((this.payReasonFlag==null && other.getPayReasonFlag()==null) || 
             (this.payReasonFlag!=null &&
              this.payReasonFlag.equals(other.getPayReasonFlag()))) &&
            ((this.payRefReason==null && other.getPayRefReason()==null) || 
             (this.payRefReason!=null &&
              this.payRefReason.equals(other.getPayRefReason()))) &&
            ((this.planFee==null && other.getPlanFee()==null) || 
             (this.planFee!=null &&
              this.planFee.equals(other.getPlanFee()))) &&
            ((this.subPayObject==null && other.getSubPayObject()==null) || 
             (this.subPayObject!=null &&
              this.subPayObject.equals(other.getSubPayObject())));
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
        if (getAccountNo() != null) {
            _hashCode += getAccountNo().hashCode();
        }
        if (getCentiCode() != null) {
            _hashCode += getCentiCode().hashCode();
        }
        if (getCentiType() != null) {
            _hashCode += getCentiType().hashCode();
        }
        if (getPayObject() != null) {
            _hashCode += getPayObject().hashCode();
        }
        if (getPayReason() != null) {
            _hashCode += getPayReason().hashCode();
        }
        if (getPayReasonFlag() != null) {
            _hashCode += getPayReasonFlag().hashCode();
        }
        if (getPayRefReason() != null) {
            _hashCode += getPayRefReason().hashCode();
        }
        if (getPlanFee() != null) {
            _hashCode += getPlanFee().hashCode();
        }
        if (getSubPayObject() != null) {
            _hashCode += getSubPayObject().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JlinkAccountVo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "jlinkAccountVo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "accountNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("centiCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "centiCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("centiType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "centiType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payObject");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payObject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payReason");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payReason"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payReasonFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payReasonFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
        elemField.setFieldName("subPayObject");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subPayObject"));
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
