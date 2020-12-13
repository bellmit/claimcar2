/**
 * DistributionService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ins.sino.claimcar.founder.webservice;

public class DistributionService  implements java.io.Serializable {
    private ins.sino.claimcar.founder.webservice.DispatchService dispatchService;

    private ins.sino.claimcar.founder.webservice.ReportService reportService;

    private ins.sino.claimcar.founder.webservice.SurveyService surveyService;

    public DistributionService() {
    }

    public DistributionService(
           ins.sino.claimcar.founder.webservice.DispatchService dispatchService,
           ins.sino.claimcar.founder.webservice.ReportService reportService,
           ins.sino.claimcar.founder.webservice.SurveyService surveyService) {
           this.dispatchService = dispatchService;
           this.reportService = reportService;
           this.surveyService = surveyService;
    }


    /**
     * Gets the dispatchService value for this DistributionService.
     * 
     * @return dispatchService
     */
    public ins.sino.claimcar.founder.webservice.DispatchService getDispatchService() {
        return dispatchService;
    }


    /**
     * Sets the dispatchService value for this DistributionService.
     * 
     * @param dispatchService
     */
    public void setDispatchService(ins.sino.claimcar.founder.webservice.DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }


    /**
     * Gets the reportService value for this DistributionService.
     * 
     * @return reportService
     */
    public ins.sino.claimcar.founder.webservice.ReportService getReportService() {
        return reportService;
    }


    /**
     * Sets the reportService value for this DistributionService.
     * 
     * @param reportService
     */
    public void setReportService(ins.sino.claimcar.founder.webservice.ReportService reportService) {
        this.reportService = reportService;
    }


    /**
     * Gets the surveyService value for this DistributionService.
     * 
     * @return surveyService
     */
    public ins.sino.claimcar.founder.webservice.SurveyService getSurveyService() {
        return surveyService;
    }


    /**
     * Sets the surveyService value for this DistributionService.
     * 
     * @param surveyService
     */
    public void setSurveyService(ins.sino.claimcar.founder.webservice.SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DistributionService)) return false;
        DistributionService other = (DistributionService) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dispatchService==null && other.getDispatchService()==null) || 
             (this.dispatchService!=null &&
              this.dispatchService.equals(other.getDispatchService()))) &&
            ((this.reportService==null && other.getReportService()==null) || 
             (this.reportService!=null &&
              this.reportService.equals(other.getReportService()))) &&
            ((this.surveyService==null && other.getSurveyService()==null) || 
             (this.surveyService!=null &&
              this.surveyService.equals(other.getSurveyService())));
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
        if (getDispatchService() != null) {
            _hashCode += getDispatchService().hashCode();
        }
        if (getReportService() != null) {
            _hashCode += getReportService().hashCode();
        }
        if (getSurveyService() != null) {
            _hashCode += getSurveyService().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DistributionService.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://svc.server.cc.order.com/xsd", "DistributionService"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dispatchService");
        elemField.setXmlName(new javax.xml.namespace.QName("http://svc.server.cc.order.com/xsd", "dispatchService"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://svc.dispatch.platform.cc.order.com/xsd", "DispatchService"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportService");
        elemField.setXmlName(new javax.xml.namespace.QName("http://svc.server.cc.order.com/xsd", "reportService"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://svc.report.platform.cc.order.com/xsd", "ReportService"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("surveyService");
        elemField.setXmlName(new javax.xml.namespace.QName("http://svc.server.cc.order.com/xsd", "surveyService"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://svc.survey.platform.cc.order.com/xsd", "SurveyService"));
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
