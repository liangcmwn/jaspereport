package cn.cmwn.jasperreport;

import cn.cmwn.jasperreport.service.ReportService;
import com.wz.parts.constant.PartsConstant;
import com.wz.parts.dto.delivery.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author liangyx
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class SampleController {

    private final ReportService reportService;

    @GetMapping("report")
    public void getReportPdf(@RequestParam(value = "type", required = false) String type, HttpServletResponse res) {
        if(! StringUtils.hasText(type)) {
            type = "pdf";
        }

        DeliveryOrderPrint orderPrint = prepareData();
        Map<String, Object> paras = new HashMap<>(3);
        paras.put("order", orderPrint);

        reportService.setResponsePdf(res, "发运单");
        try (OutputStream out = res.getOutputStream()) {
            reportService.export("jasperreports/parts_delivery_order_table.jrxml", type, paras, Arrays.asList(orderPrint), out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private DeliveryOrderPrint prepareData() {
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

        List<DeliveryOrderItemPrint> items = new ArrayList<>();
        for (int i = 0; i <11 ; i++) {
            items.add(getDeliveryOrderItem(i + 1));

        }

        order.setItems(new JRBeanCollectionDataSource(items));
        return order;
    }

    private DeliveryOrderItemPrint getDeliveryOrderItem(int idx) {
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


