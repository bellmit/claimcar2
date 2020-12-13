/**
 * ClaimMainVo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sinosoft.sff.interf;

public class ClaimMainVo  implements java.io.Serializable {
    private java.lang.String claimNo;

    private java.lang.String comCode;

    private java.lang.String compensateNo;

    private java.util.Calendar endCaseDate;

    private java.lang.String operatorCode;

    private java.lang.String policyNo;

    private java.lang.String registNo;

    private java.lang.String riskCode;

    private java.util.Calendar underWriteEndDate;

    public ClaimMainVo() {
    }

    public ClaimMainVo(
           java.lang.String claimNo,
           java.lang.String comCode,
           java.lang.String compensateNo,
           java.util.Calendar endCaseDate,
           java.lang.String operatorCode,
           java.lang.String policyNo,
           java.lang.String registNo,
           java.lang.String riskCode,
           java.util.Calendar underWriteEndDate) {
           this.claimNo = claimNo;
           this.comCode = comCode;
           this.compensateNo = compensateNo;
           this.endCaseDate = endCaseDate;
           this.operatorCode = operatorCode;
           this.policyNo = policyNo;
           this.registNo = registNo;
           this.riskCode = riskCode;
           this.underWriteEndDate = underWriteEndDate;
    }


    /**
     * Gets the claimNo value for this ClaimMainVo.
     * 
     * @return claimNo
     */
    public java.lang.String getClaimNo() {
        return claimNo;
    }


    /**
     * Sets the claimNo value for this ClaimMainVo.
     * 
     * @param claimNo
     */
    public void setClaimNo(java.lang.String claimNo) {
        this.claimNo = claimNo;
    }


    /**
     * Gets the comCode value for this ClaimMainVo.
     * 
     * @return comCode
     */
    public java.lang.String getComCode() {
        return comCode;
    }


    /**
     * Sets the comCode value for this ClaimMainVo.
     * 
     * @param comCode
     */
    public void setComCode(java.lang.String comCode) {
        this.comCode = comCode;
    }


    /**
     * Gets the compensateNo value for this ClaimMainVo.
     * 
     * @return compensateNo
     */
    public java.lang.String getCompensateNo() {
        return compensateNo;
    }


    /**
     * Sets the compensateNo value for this ClaimMainVo.
     * 
     * @param compensateNo
     */
    public void setCompensateNo(java.lang.String compensateNo) {
        this.compensateNo = compensateNo;
    }


    /**
     * Gets the endCaseDate value for this ClaimMainVo.
     * 
     * @return endCaseDate
     */
    public java.util.Calendar getEndCaseDate() {
        return endCaseDate;
    }


    /**
     * Sets the endCaseDate value for this ClaimMainVo.
     * 
     * @param endCaseDate
     */
    public void setEndCaseDate(java.util.Calendar endCaseDate) {
        this.endCaseDate = endCaseDate;
    }


    /**
     * Gets the operatorCode value for this ClaimMainVo.
     * 
     * @return operatorCode
     */
    public java.lang.String getOperatorCode() {
        return operatorCode;
    }


    /**
     * Sets the operatorCode value for this ClaimMainVo.
     * 
     * @param operatorCode
     */
    public void setOperatorCode(java.lang.String operatorCode) {
        this.operatorCode = operatorCode;
    }


    /**
     * Gets the policyNo value for this ClaimMainVo.
     * 
     * @return policyNo
     */
    public java.lang.String getPolicyNo() {
        return policyNo;
    }


    /**
     * Sets the policyNo value for this ClaimMainVo.
     * 
     * @param policyNo
     */
    public void setPolicyNo(java.lang.String policyNo) {
        this.policyNo = policyNo;
    }


    /**
     * Gets the registNo value for this ClaimMainVo.
     * 
     * @return registNo
     */
    public java.lang.String getRegistNo() {
        return registNo;
    }


    /**
     * Sets the registNo value for this ClaimMainVo.
     * 
     * @param registNo
     */
    public void setRegistNo(java.lang.String registNo) {
        this.registNo = registNo;
    }


    /**
     * Gets the riskCode value for this ClaimMainVo.
     * 
     * @return riskCode
     */
    public java.lang.String getRiskCode() {
        return riskCode;
    }


    /**
     * Sets the riskCode value for this ClaimMainVo.
     * 
     * @param riskCode
     */
    public void setRiskCode(java.lang.String riskCode) {
        this.riskCode = riskCode;
    }


    /**
     * Gets the underWriteEndDate value for this ClaimMainVo.
     * 
     * @return underWriteEndDate
     */
    public java.util.Calendar getUnderWriteEndDate() {
        return underWriteEndDate;
    }


    /**
     * Sets the underWriteEndDate value for this ClaimMainVo.
     * 
     * @param underWriteEndDate
     */
    public void setUnderWriteEndDate(java.util.Calendar underWriteEndDate) {
        this.underWriteEndDate = underWriteEndDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ClaimMainVo)) return false;
        ClaimMainVo other = (ClaimMainVo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.claimNo==null && other.getClaimNo()==null) || 
             (this.claimNo!=null &&
              this.claimNo.equals(other.getClaimNo()))) &&
            ((this.comCode==null && other.getComCode()==null) || 
             (this.comCode!=null &&
              this.comCode.equals(other.getComCode()))) &&
            ((this.compensateNo==null && other.getCompensateNo()==null) || 
             (this.compensateNo!=null &&
              this.compensateNo.equals(other.getCompensateNo()))) &&
            ((this.endCaseDate==null && other.getEndCaseDate()==null) || 
             (this.endCaseDate!=null &&
              this.endCaseDate.equals(other.getEndCaseDate()))) &&
            ((this.operatorCode==null && other.getOperatorCode()==null) || 
             (this.operatorCode!=null &&
              this.operatorCode.equals(other.getOperatorCode()))) &&
            ((this.policyNo==null && other.getPolicyNo()==null) || 
             (this.policyNo!=null &&
              this.policyNo.equals(other.getPolicyNo()))) &&
            ((this.registNo==null && other.getRegistNo()==null) || 
             (this.registNo!=null &&
              this.registNo.equals(other.getRegistNo()))) &&
            ((this.riskCode==null && other.getRiskCode()==null) || 
             (this.riskCode!=null &&
              this.riskCode.equals(other.getRiskCode()))) &&
            ((this.underWriteEndDate==null && other.getUnderWriteEndDate()==null) || 
             (this.underWriteEndDate!=null &&
              this.underWriteEndDate.equals(other.getUnderWriteEndDate())));
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
        if (getClaimNo() != null) {
            _hashCode += getClaimNo().hashCode();
        }
        if (getComCode() != null) {
            _hashCode += getComCode().hashCode();
        }
        if (getCompensateNo() != null) {
            _hashCode += getCompensateNo().hashCode();
        }
        if (getEndCaseDate() != null) {
            _hashCode += getEndCaseDate().hashCode();
        }
        if (getOperatorCode() != null) {
            _hashCode += getOperatorCode().hashCode();
        }
        if (getPolicyNo() != null) {
            _hashCode += getPolicyNo().hashCode();
        }
        if (getRegistNo() != null) {
            _hashCode += getRegistNo().hashCode();
        }
        if (getRiskCode() != null) {
            _hashCode += getRiskCode().hashCode();
        }
        if (getUnderWriteEndDate() != null) {
            _hashCode += getUnderWriteEndDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ClaimMainVo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://interf.sff.sinosoft.com/", "claimMainVo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("claimNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "claimNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compensateNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "compensateNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endCaseDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endCaseDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operatorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "operatorCode"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("riskCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "riskCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("underWriteEndDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "underWriteEndDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
