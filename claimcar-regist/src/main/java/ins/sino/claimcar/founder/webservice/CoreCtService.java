/**
 * CoreCtService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.founder.webservice;

public class CoreCtService  implements java.io.Serializable {
    private ins.sino.claimcar.founder.webservice.CoreClient coreClientImpl;

    private java.lang.Object deptList;

    private java.lang.Object surveyorList;

    public CoreCtService() {
    }

    public CoreCtService(
           ins.sino.claimcar.founder.webservice.CoreClient coreClientImpl,
           java.lang.Object deptList,
           java.lang.Object surveyorList) {
           this.coreClientImpl = coreClientImpl;
           this.deptList = deptList;
           this.surveyorList = surveyorList;
    }


    /**
     * Gets the coreClientImpl value for this CoreCtService.
     * 
     * @return coreClientImpl
     */
    public ins.sino.claimcar.founder.webservice.CoreClient getCoreClientImpl() {
        return coreClientImpl;
    }


    /**
     * Sets the coreClientImpl value for this CoreCtService.
     * 
     * @param coreClientImpl
     */
    public void setCoreClientImpl(ins.sino.claimcar.founder.webservice.CoreClient coreClientImpl) {
        this.coreClientImpl = coreClientImpl;
    }


    /**
     * Gets the deptList value for this CoreCtService.
     * 
     * @return deptList
     */
    public java.lang.Object getDeptList() {
        return deptList;
    }


    /**
     * Sets the deptList value for this CoreCtService.
     * 
     * @param deptList
     */
    public void setDeptList(java.lang.Object deptList) {
        this.deptList = deptList;
    }


    /**
     * Gets the surveyorList value for this CoreCtService.
     * 
     * @return surveyorList
     */
    public java.lang.Object getSurveyorList() {
        return surveyorList;
    }


    /**
     * Sets the surveyorList value for this CoreCtService.
     * 
     * @param surveyorList
     */
    public void setSurveyorList(java.lang.Object surveyorList) {
        this.surveyorList = surveyorList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CoreCtService)) return false;
        CoreCtService other = (CoreCtService) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.coreClientImpl==null && other.getCoreClientImpl()==null) || 
             (this.coreClientImpl!=null &&
              this.coreClientImpl.equals(other.getCoreClientImpl()))) &&
            ((this.deptList==null && other.getDeptList()==null) || 
             (this.deptList!=null &&
              this.deptList.equals(other.getDeptList()))) &&
            ((this.surveyorList==null && other.getSurveyorList()==null) || 
             (this.surveyorList!=null &&
              this.surveyorList.equals(other.getSurveyorList())));
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
        if (getCoreClientImpl() != null) {
            _hashCode += getCoreClientImpl().hashCode();
        }
        if (getDeptList() != null) {
            _hashCode += getDeptList().hashCode();
        }
        if (getSurveyorList() != null) {
            _hashCode += getSurveyorList().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CoreCtService.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://interfaces.dh.cc.order.com/xsd", "CoreCtService"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coreClientImpl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://interfaces.dh.cc.order.com/xsd", "coreClientImpl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://svc.client.cc.order.com/xsd", "CoreClient"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deptList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://interfaces.dh.cc.order.com/xsd", "deptList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("surveyorList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://interfaces.dh.cc.order.com/xsd", "surveyorList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
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
