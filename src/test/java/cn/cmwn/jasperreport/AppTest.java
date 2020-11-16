package cn.cmwn.jasperreport;

import com.wz.parts.dto.delivery.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static void main(String[] args) throws JRException, IOException {
        Map<String, Object> paras = new HashMap<>(16);
//        paras.put("order", "中文名001");
        DeliveryOrderPrint order = new DeliveryOrderPrint();
        order.setId("DO00001");
        order.setDeliveryWarehouseName("仓库001");
        order.setDeliveryDate(new Date());
        order.setCustomerName("客户001");
        order.setReceiveUser("张三");
        order.setReceiveUserPhone("18928378090");
        order.setFree(true);
        order.setReceiveAddress("江苏南京001小落叶松嘶中成药江苏南京001小落叶松嘶中成药江苏南京001小落叶松嘶中成药");
        order.setCompanyName("汽车分公司");
        order.setCarrierInfo(new DeliveryCarrierInfoPrint());
        order.getCarrierInfo().setName("湖北公司");
        order.getCarrierInfo().setPhone("027-13827823");
        order.getCarrierInfo().setPackageNo("B150-05.09*02-01；B150-05.09*02-02；B150-05.09*02-01；B150-05.09*02-022；B150-05.09*02-01；B150-05.09*02-02");
        order.getCarrierInfo().setTrayQty(2);
        order.getCarrierInfo().setTrayType("木架");
        order.getCarrierInfo().setDeliveryMethod("托运");
        order.setRealTotalAmt(new BigDecimal("400000.34"));

        order.setPrintUserName("张三");
//        order.setSignForUserName("张三");

        List<DeliveryOrderItemPrint> items = new ArrayList<>();
        for (int i = 0; i <101 ; i++) {
            items.add(getDeliveryOrderItem(i + 1));

        }

        order.setItems(new JRBeanCollectionDataSource(items));
//        paras.put("order", "1234");
        paras.put("order", order);
//        String path = "reports/order1.jrxml";
//        String path = "reports/order1.jasper";
        String path = "classpath:jasperreports/parts_delivery_order_table2.jrxml";
        String output = "D:/wuzheng/delivery_order.pdf";

//        File file = new File(path);
//        System.out.println(file.exists());
//        InputStream jasper = new FileInputStream(file) ;
        InputStream jasper = ResourceUtils.getURL(path).openStream();
        JasperReport report = JasperCompileManager.compileReport(jasper);
        JasperPrint print = JasperFillManager.fillReport(report, paras, new JRBeanCollectionDataSource(Arrays.asList(order)));
        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.setExporterInput(new SimpleExporterInput(print));
        htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(new FileOutputStream("D:/wuzheng/order.html")));
        htmlExporter.exportReport();

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(output)));
        exporter.exportReport();
//        byte[] data = JasperRunManager.runReportToPdf(jasper, paras);
//        try (FileOutputStream fos = (new FileOutputStream("D:/sample.pdf"))) {
//            fos.write(data);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static DeliveryOrderItemPrint getDeliveryOrderItem(int idx) {
        return DeliveryOrderItemPrint.builder()
                    .id("" + idx)
                    .curator("保管员")
                    .partsNo("38928293")
                    .partsName("配件配件配件")
                    .partsDrawingNo("GB-12ijdsakfl")
                    .unit("个")
                    .packFlag("A")
                    .qty(10)
                    .realBasePrice(new BigDecimal("300.98"))
                    .realTotalAmt(new BigDecimal("6000.28"))
                    .build();
    }

}
