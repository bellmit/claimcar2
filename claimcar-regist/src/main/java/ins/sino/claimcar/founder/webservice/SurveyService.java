/**
 * SurveyService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.founder.webservice;

public class SurveyService  implements java.io.Serializable {
    private ins.sino.claimcar.founder.webservice.CoreCtService coreCtService;

    private ins.sino.claimcar.founder.webservice.DhCoreSmsService dhCoreSmsService;

    private ins.sino.claimcar.founder.webservice.DispatchCtService dispatchCtService;

    private ins.sino.claimcar.founder.webservice.ISurveyDAO surveyDAO;

    public SurveyService() {
    }

    public SurveyService(
           ins.sino.claimcar.founder.webservice.CoreCtService coreCtService,
           ins.sino.claimcar.founder.webservice.DhCoreSmsService dhCoreSmsService,
           ins.sino.claimcar.founder.webservice.DispatchCtService dispatchCtService,
           ins.sino.claimcar.founder.webservice.ISurveyDAO surveyDAO) {
           this.coreCtService = coreCtService;
           this.dhCoreSmsService = dhCoreSmsService;
           this.dispatchCtService = dispatchCtService;
           this.surveyDAO = surveyDAO;
    }


    /**
     * Gets the coreCtService value for this SurveyService.
     * 
     * @return coreCtService
     */
    public ins.sino.claimcar.founder.webservice.CoreCtService getCoreCtService() {
        return coreCtService;
    }


    /**
     * Sets the coreCtService value for this SurveyService.
     * 
     * @param coreCtService
     */
    public void setCoreCtService(ins.sino.claimcar.founder.webservice.CoreCtService coreCtService) {
        this.coreCtService = coreCtService;
    }


    /**
     * Gets the dhCoreSmsService value for this SurveyService.
     * 
     * @return dhCoreSmsService
     */
    public ins.sino.claimcar.founder.webservice.DhCoreSmsService getDhCoreSmsService() {
        return dhCoreSmsService;
    }


    /**
     * Sets the dhCoreSmsService value for this SurveyService.
     * 
     * @param dhCoreSmsService
     */
    public void setDhCoreSmsService(ins.sino.claimcar.founder.webservice.DhCoreSmsService dhCoreSmsService) {
        this.dhCoreSmsService = dhCoreSmsService;
    }


    /**
     * Gets the dispatchCtService value for this SurveyService.
     * 
     * @return dispatchCtService
     */
    public ins.sino.claimcar.founder.webservice.DispatchCtService getDispatchCtService() {
        return dispatchCtService;
    }


    /**
     * Sets the dispatchCtService value for this SurveyService.
     * 
     * @param dispatchCtService
     */
    public void setDispatchCtService(ins.sino.claimcar.founder.webservice.DispatchCtService dispatchCtService) {
        this.dispatchCtService = dispatchCtService;
    }


    /**
     * Gets the surveyDAO value for this SurveyService.
     * 
     * @return surveyDAO
     */
    public ins.sino.claimcar.founder.webservice.ISurveyDAO getSurveyDAO() {
        return surveyDAO;
    }


    /**
     * Sets the surveyDAO value for this SurveyService.
     * 
     * @param surveyDAO
     */
    public void setSurveyDAO(ins.sino.claimcar.founder.webservice.ISurveyDAO surveyDAO) {
        this.surveyDAO = surveyDAO;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SurveyService)) return false;
        SurveyService other = (SurveyService) obj;
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
            ((this.dhCoreSmsService==null && other.getDhCoreSmsService()==null) || 
             (this.dhCoreSmsService!=null &&
              this.dhCoreSmsService.equals(other.getDhCoreSmsService()))) &&
            ((this.dispatchCtService==null && other.getDispatchCtService()==null) || 
             (this.dispatchCtService!=null &&
              this.dispatchCtService.equals(other.getDispatchCtService()))) &&
            ((this.surveyDAO==null && other.getSurveyDAO()==null) || 
             (this.surveyDAO!=null &&
              this.surveyDAO.equals(other.getSurveyDAO())));
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
        if (getDhCoreSmsService() != null) {
            _hashCode += getDhCoreSmsService().hashCode();
        }
        if (getDispatchCtService() != null) {
            _hashCode += getDispatchCtService().hashCode();
        }
        if (getSurveyDAO() != null) {
            _hashCode += getSurveyDAO().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SurveyService.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://svc.survey.platform.cc.order.com/xsd", "SurveyService"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coreCtService");
        elemField.setXmlName(new javax.xml.namespace.QName("http://svc.survey.platform.cc.order.com/xsd", "coreCtService"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://interfaces.dh.cc.order.com/xsd", "CoreCtService"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dhCoreSmsService");
        elemField.setXmlName(new javax.xml.namespace.QName("http://svc.survey.platform.cc.order.com/xsd", "dhCoreSmsService"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://svc.sms.platform.cc.order.com/xsd", "DhCoreSmsService"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dispatchCtService");
        elemField.setXmlName(new javax.xml.namespace.QName("http://svc.survey.platform.cc.order.com/xsd", "dispatchCtService"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://interfaces.dh.cc.order.com/xsd", "DispatchCtService"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("surveyDAO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://svc.survey.platform.cc.order.com/xsd", "surveyDAO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://idao.survey.platform.cc.order.com/xsd", "ISurveyDAO"));
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
