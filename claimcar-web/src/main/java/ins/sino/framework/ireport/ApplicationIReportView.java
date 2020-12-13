/******************************************************************************
* CREATETIME : 2016年3月21日 下午8:00:00
******************************************************************************/
package ins.sino.framework.ireport;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsCsvView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsHtmlView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsXlsView;


/**
 * <pre></pre>
 * @author ★XMSH
 */
public class ApplicationIReportView extends JasperReportsMultiFormatView {
	private JasperReport jasperReport;  
    
    public ApplicationIReportView() {  
        super();  
        
        Map<String, Class<? extends AbstractJasperReportsView>> formatMappings = new HashMap<String, Class<? extends AbstractJasperReportsView>>();
		formatMappings.put("csv", JasperReportsCsvView.class);
		formatMappings.put("html", JasperReportsHtmlView.class);
		formatMappings.put("pdf", JasperReportsPdfView.class);
		formatMappings.put("xls", JasperReportsXlsView.class);
		formatMappings.put("docx", JasperReportsDocxView.class);
		formatMappings.put("doc", JasperReportsDocView.class);
		
        super.setFormatMappings(formatMappings);
    }  
  
    protected JasperPrint fillReport(Map<String, Object> model) throws Exception {  
        if (model.containsKey("url")) {  
            setUrl(String.valueOf(model.get("url")));  
            this.jasperReport = loadReport();  
        }  
          
        return super.fillReport(model);  
    }  
      
    protected JasperReport getReport() {  
        return this.jasperReport;  
    }  
}
