package ins.sino.framework.ireport;

import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

@SuppressWarnings({"deprecation", "rawtypes"})
public class JasperReportsDocView extends AbstractJasperReportsSingleFormatView  {
	
		public JasperReportsDocView() {
			setContentType("application/vnd.ms-word");
		}

		@Override
		protected net.sf.jasperreports.engine.JRExporter createExporter() {
			return new JRRtfExporter();
		}

		@Override
		protected boolean useWriter() {
			return false;
		}


}
