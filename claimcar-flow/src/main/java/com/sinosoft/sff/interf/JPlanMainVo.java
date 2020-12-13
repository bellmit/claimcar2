/**
 * JPlanMainVo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sinosoft.sff.interf;

public class JPlanMainVo  implements java.io.Serializable {
    private java.lang.String certiNo;

    private java.lang.String certiType;

    private java.lang.String claimNo;

    private com.sinosoft.sff.interf.JlinkAccountVo[] jlinkAccountVos;

    private com.sinosoft.sff.interf.JplanFeeVo[] jplanFeeVos;

    private java.lang.String operateCode;

    private java.lang.String operateComCode;

    private java.lang.String payComCode;

    private java.lang.String policyNo;

    private java.lang.String registNo;

    public JPlanMainVo() {
    }

    public JPlanMainVo(
           java.lang.String certiNo,
           java.lang.String certiType,
           java.lang.String claimNo,
           com.sinosoft.sff.interf.JlinkAccountVo[] jlinkAccountVos,
           com.sinosoft.sff.interf.JplanFeeVo[] jplanFeeVos,
           java.lang.String operateCode,
           java.lang.String operateComCode,
           java.lang.String payComCode,
           java.lang.String policyNo,
           java.lang.String registNo) {
           this.certiNo = certiNo;
           this.certiType = certiType;
           this.claimNo = claimNo;
           this.jlinkAccountVos = jlinkAccountVos;
           this.jplanFeeVos = jplanFeeVos;
           this.operateCode = operateCode;
           this.operateComCode = operateComCode;
           this.payComCode = payComCode;
           this.policyNo = policyNo;
           this.registNo = registNo;
    }


    /**
     * Gets the certiNo value for this JPlanMainVo.
     * 
     * @return certiNo
     */
    public java.lang.String getCertiNo() {
        return certiNo;
    }


    /**
     * Sets the certiNo value for this JPlanMainVo.
     * 
     * @param certiNo
     */
    public void setCertiNo(java.lang.String certiNo) {
        this.certiNo = certiNo;
    }


    /**
     * Gets the certiType value for this JPlanMainVo.
     * 
     * @return certiType
     */
    public java.lang.String getCertiType() {
        return certiType;
    }


    /**
     * Sets the certiType value for this JPlanMainVo.
     * 
     * @param certiType
     */
    public void setCertiType(java.lang.String certiType) {
        this.certiType = certiType;
    }


    /**
     * Gets the claimNo value for this JPlanMainVo.
     * 
     * @return claimNo
     */
    public java.lang.String getClaimNo() {
        return claimNo;
    }


    /**
     * Sets the claimNo value for this JPlanMainVo.
     * 
     * @param claimNo
     */
    public void setClaimNo(java.lang.String claimNo) {
        this.claimNo = claimNo;
    }


    /**
     * Gets the jlinkAccountVos value for this JPlanMainVo.
     * 
     * @return jlinkAccountVos
     */
    public com.sinosoft.sff.interf.JlinkAccountVo[] getJlinkAccountVos() {
        return jlinkAccountVos;
    }


    /**
     * Sets the jlinkAccountVos value for this JPlanMainVo.
     * 
     * @param jlinkAccountVos
     */
    public void setJlinkAccountVos(com.sinosoft.sff.interf.JlinkAccountVo[] jlinkAccountVos) {
        this.jlinkAccountVos = jlinkAccountVos;
    }

    public com.sinosoft.sff.interf.JlinkAccountVo getJlinkAccountVos(int i) {
        return this.jlinkAccountVos[i];
    }

    public void setJlinkAccountVos(int i, com.sinosoft.sff.interf.JlinkAccountVo _value) {
        this.jlinkAccountVos[i] = _value;
    }


    /**
     * Gets the jplanFeeVos value for this JPlanMainVo.
     * 
     * @return jplanFeeVos
     */
    public com.sinosoft.sff.interf.JplanFeeVo[] getJplanFeeVos() {
        return jplanFeeVos;
    }


    /**
     * Sets the jplanFeeVos value for this JPlanMainVo.
     * 
     * @param jplanFeeVos
     */
    public void setJplanFeeVos(com.sinosoft.sff.interf.JplanFeeVo[] jplanFeeVos) {
        this.jplanFeeVos = jplanFeeVos;
    }

    public com.sinosoft.sff.interf.JplanFeeVo getJplanFeeVos(int i) {
        return this.jplanFeeVos[i];
    }

    public void setJplanFeeVos(int i, com.sinosoft.sff.interf.JplanFeeVo _value) {
        this.jplanFeeVos[i] = _value;
    }


    /**
     * Gets the operateCode value for this JPlanMainVo.
     * 
     * @return operateCode
     */
    public java.lang.String getOperateCode() {
        return operateCode;
    }


    /**
     * Sets the operateCode value for this JPlanMainVo.
     * 
     * @param operateCode
     */
    public void setOperateCode(java.lang.String operateCode) {
        this.operateCode = operateCode;
    }


    /**
     * Gets the operateComCode value for this JPlanMainVo.
     * 
     * @return operateComCode
     */
    public java.lang.String getOperateComCode() {
        return operateComCode;
    }


    /**
     * Sets the operateComCode value for this JPlanMainVo.
     * 
     * @param operateComCode
     */
    public void setOperateComCode(java.lang.String operateComCode) {
        this.operateComCode = operateComCode;
    }


    /**
     * Gets the payComCode value for this JPlanMainVo.
     * 
     * @return payComCode
     */
    public java.lang.String getPayComCode() {
        return payComCode;
    }


    /**
     * Sets the payComCode value for this JPlanMainVo.
     * 
     * @param payComCode
     */
    public void setPayComCode(java.lang.String payComCode) {
        this.payComCode = payComCode;
    }


    /**
     * Gets the policyNo value for this JPlanMainVo.
     * 
     * @return policyNo
     */
    public java.lang.String getPolicyNo() {
        return policyNo;
    }


    /**
     * Sets the policyNo value for this JPlanMainVo.
     * 
     * @param policyNo
     */
    public void setPolicyNo(java.lang.String policyNo) {
        this.policyNo = policyNo;
    }


    /**
     * Gets the registNo value for this JPlanMainVo.
     * 
     * @return registNo
     */
    public java.lang.String getRegistNo() {
        return registNo;
    }


    /**
     * Sets the registNo value for this JPlanMainVo.
     * 
     * @param registNo
     */
    public void setRegistNo(java.lang.String registNo) {
        this.registNo = registNo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JPlanMainVo)) return false;
        JPlanMainVo other = (JPlanMainVo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.certiNo==null && other.getCertiNo()==null) || 
             (this.certiNo!=null &&
              this.certiNo.equals(other.getCertiNo()))) &&
            ((this.certiType==null && other.getCertiType()==null) || 
             (this.certiType!=null &&
              this.certiType.equals(other.getCertiType()))) &&
            ((this.claimNo==null && other.getClaimNo()==null) || 
             (this.claimNo!=null &&
              this.claimNo.equals(other.getClaimNo()))) &&
            ((this.jlinkAccountVos==null && other.getJlinkAccountVos()==null) || 
             (this.jlinkAccountVos!=null &&
              java.util.Arrays.equals(this.jlinkAccountVos, other.getJlinkAccountVos()))) &&
            ((this.jplanFeeVos==null && other.getJplanFeeVos()==null) || 
             (this.jplanFeeVos!=null &&
              java.util.Arrays.equals(this.jplanFeeVos, other.getJplanFeeVos()))) &&
            ((this.operateCode==null && other.getOperateCode()==null) || 
             (this.operateCode!=null &&
              this.operateCode.equals(other.getOperateCode()))) &&
            ((this.operateComCode==null && other.getOperateComCode()==null) || 
             (this.operateComCode!=null &&
              this.operateComCode.equals(other.getOperateComCode()))) &&
            ((this.payComCode==null && other.getPayComCode()==null) || 
             (this.payComCode!=null &&
              this.payComCode.equals(other.getPayComCode()))) &&
            ((this.policyNo==null && other.getPolicyNo()==null) || 
             (this.policyNo!=null &&
              this.policyNo.equals(other.getPolicyNo()))) &&
            ((this.registNo==null && other.getRegistNo()==null) || 
             (this.registNo!=null &&
              this.registNo.equals(other.getRegistNo())));
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
        if (getCertiNo() != null) {
            _hashCode += getCertiNo().hashCode();
        }
        if (getCertiType() != null) {
            _hashCode += getCertiType().hashCode();
        }
        if (getClaimNo() != null) {
            _hashCode += getClaimNo().hashCode();
        }
        if (getJlinkAccountVos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJlinkAccountVos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJlinkAccountVos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getJplanFeeVos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJplanFeeVos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJplanFeeVos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOperateCode() != null) {
            _hashCode += getOperateCode().hashCode();
        }
        if (getOperateComCode() != null) {
            _hashCode += getOperateComCode().hashCode();
        }
        if (getPayComCode() != null) {
            _hashCode += getPayComCode().hashCode();
        }
        if (getPolicyNo() != null) {
            _hashCode += getPolicyNo().hashCode();
        }
        if (getRegistNo() != null) {
            _hashCode += getRegistNo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JPlanMainVo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "jPlanMainVo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certiNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certiNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certiType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certiType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("claimNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "claimNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jlinkAccountVos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "jlinkAccountVos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "jlinkAccountVo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jplanFeeVos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "jplanFeeVos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "jplanFeeVo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operateCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "operateCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operateComCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "operateComCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payComCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payComCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("policyNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "policyNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registNo"));
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
