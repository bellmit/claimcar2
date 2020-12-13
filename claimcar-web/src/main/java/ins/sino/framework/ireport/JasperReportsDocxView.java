package ins.sino.framework.ireport;

import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

@SuppressWarnings({"deprecation", "rawtypes"})
public class JasperReportsDocxView extends AbstractJasperReportsSingleFormatView  {
	
		public JasperReportsDocxView() {
			setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			
		}

		@Override
		protected net.sf.jasperreports.engine.JRExporter createExporter() {
			return new JRDocxExporter();
		}

		@Override
		protected boolean useWriter() {
			return false;
		}


}
