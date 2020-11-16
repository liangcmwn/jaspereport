package cn.cmwn.jasperreport.service;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liangyx
 */
@Slf4j
@Service
public class ReportService {

    private Map<String, JasperReport> reportMap = new HashMap<>();

    public static final String CONTENT_DISPOSITION="Content-Disposition";

    public static final String CONTENT_DISPOSITION_VALUE="attachment; filename=";

    public static final String CONTENT_TYPE_PDF="application/pdf; charset=utf-8";

    public static final String CONTENT_TYPE_HTML="application/html; charset=utf-8";

    private synchronized JasperReport getReport(String template) throws JRException {
        if(reportMap.containsKey(template)) {
            return reportMap.get(template);
        }
        InputStream jasper = getClass().getClassLoader().getResourceAsStream(template);
        JasperReport report = JasperCompileManager.compileReport(jasper);
        reportMap.put(template, report);
        return report;
    }

    public void setResponsePdf(HttpServletResponse res, String name) {
        name = name.trim().replaceAll(" ", "_");
        name = new String(name.getBytes(), Charsets.ISO_8859_1);
        res.setContentType(CONTENT_TYPE_PDF);
        res.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_VALUE + name + ".pdf");
    }

    public void export(String template, String type, Map<String, Object> paras, List data, OutputStream out) {
        log.info("export {} as {}", template, type);
        try {
            JasperReport report = getReport(template);
            JasperPrint print = JasperFillManager.fillReport(report, paras, new JRBeanCollectionDataSource(data));
            if("pdf".equals(type)) {
                exportPdf(print, out);
            } else {
                exportHtml(print, out);
            }
        } catch (JRException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 导出pdf
     * @param print
     * @param output
     * @throws JRException
     */
    private void exportPdf(JasperPrint print, OutputStream output) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
        exporter.exportReport();
    }

    /**
     * 导出html
     */
    private void exportHtml(JasperPrint print, OutputStream output) throws JRException {
        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.setExporterInput(new SimpleExporterInput(print));
        htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(output));
        htmlExporter.exportReport();
    }

}
