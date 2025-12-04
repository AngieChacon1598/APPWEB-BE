package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.restLosPinos.model.Menu;
import pe.edu.vallegrande.restLosPinos.model.SalesTicket;
import pe.edu.vallegrande.restLosPinos.model.ProductDetail;
import pe.edu.vallegrande.restLosPinos.service.SalesTicketService;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final MenuService menuService;
    private final SalesTicketService salesTicketService;


    public static class ReportDataRow {
        private BigDecimal MENU_ID;
        private String NAME;
        private String DESCRIPTION;
        private BigDecimal PRICE;
        private Timestamp CREATED_AT;
        private String IMAGE_URL;
        private Long CATEGORY_I;
        private BigDecimal STATE;
        private String CATEGORY_NAME;

        public BigDecimal getMENU_ID() { return MENU_ID; }
        public String getNAME() { return NAME; }
        public String getDESCRIPTION() { return DESCRIPTION; }
        public BigDecimal getPRICE() { return PRICE; }
        public Timestamp getCREATED_AT() { return CREATED_AT; }
        public String getIMAGE_URL() { return IMAGE_URL; }
        public Long getCATEGORY_I() { return CATEGORY_I; }
        public BigDecimal getSTATE() { return STATE; }
        public String getCATEGORY_NAME() { return CATEGORY_NAME; }

        public void setMENU_ID(BigDecimal MENU_ID) { this.MENU_ID = MENU_ID; }
        public void setNAME(String NAME) { this.NAME = NAME; }
        public void setDESCRIPTION(String DESCRIPTION) { this.DESCRIPTION = DESCRIPTION; }
        public void setPRICE(BigDecimal PRICE) { this.PRICE = PRICE; }
        public void setCREATED_AT(Timestamp CREATED_AT) { this.CREATED_AT = CREATED_AT; }
        public void setIMAGE_URL(String IMAGE_URL) { this.IMAGE_URL = IMAGE_URL; }
        public void setCATEGORY_I(Long CATEGORY_I) { this.CATEGORY_I = CATEGORY_I; }
        public void setSTATE(BigDecimal STATE) { this.STATE = STATE; }
        public void setCATEGORY_NAME(String CATEGORY_NAME) { this.CATEGORY_NAME = CATEGORY_NAME; }
    }

    /**
     * Genera un reporte PDF de productos según el filtro de estado
     * @param stateFilter 
     * @return byte array del PDF generado
     * @throws Exception si hay error al generar el reporte
     */
    public byte[] generateProductosReport(Integer stateFilter) throws Exception {
        try {
            // Validar el parámetro de estado
            if (stateFilter == null) {
                stateFilter = -1; // Por defecto mostrar todos
            }
            if (stateFilter != -1 && stateFilter != 0 && stateFilter != 1) {
                throw new IllegalArgumentException("El parámetro stateFilter debe ser 1 (activos), 0 (inactivos) o -1 (todos)");
            }

            List<Menu> menus = menuService.findByStateFilter(stateFilter);

            ClassPathResource resource = new ClassPathResource("reports/Productos.jrxml");
            InputStream reportStream = resource.getInputStream();

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ESTADO_FILTER", stateFilter);

            List<ReportDataRow> dataList = new ArrayList<>();
            for (Menu menu : menus) {
                ReportDataRow row = new ReportDataRow();
                
                row.setMENU_ID(menu.getMenuId() != null ? BigDecimal.valueOf(menu.getMenuId()) : null);
                row.setNAME(menu.getName());
                row.setDESCRIPTION(menu.getDescription());
                row.setPRICE(menu.getPrice());
               
                row.setCREATED_AT(menu.getCreatedAt() != null ? Timestamp.valueOf(menu.getCreatedAt()) : null);
                row.setIMAGE_URL(menu.getImagenUrl());
                row.setCATEGORY_I(menu.getCategoryId());
                
                row.setSTATE(menu.getState() != null ? BigDecimal.valueOf(menu.getState()) : null);
                
                if (menu.getCategory() != null) {
                    row.setCATEGORY_NAME(menu.getCategory().getName());
                } else {
                    row.setCATEGORY_NAME("");
                }
                dataList.add(row);
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            log.error("Error al generar el reporte de productos", e);
            throw new Exception("Error al generar el reporte: " + e.getMessage(), e);
        }
    }


    public static class SalesReportDataRow {
        private BigDecimal TICKET_ID;
        private Timestamp SALE_DATE;
        private BigDecimal TOTAL_PAYMENT;
        private String DELIVERY;
        private String DELIVERY_ADDRESS;
        private String NOTE;
        private BigDecimal USER_ID;
        private BigDecimal ID_TYPE_STATE;
        private BigDecimal ID_PAYMENT_TYPE;
        private String STATE;
        private BigDecimal AMOUNT;
        private BigDecimal ID_DETAIL_PRODUCT;
        private BigDecimal MENU_ID;

        public BigDecimal getTICKET_ID() { return TICKET_ID; }
        public Timestamp getSALE_DATE() { return SALE_DATE; }
        public BigDecimal getTOTAL_PAYMENT() { return TOTAL_PAYMENT; }
        public String getDELIVERY() { return DELIVERY; }
        public String getDELIVERY_ADDRESS() { return DELIVERY_ADDRESS; }
        public String getNOTE() { return NOTE; }
        public BigDecimal getUSER_ID() { return USER_ID; }
        public BigDecimal getID_TYPE_STATE() { return ID_TYPE_STATE; }
        public BigDecimal getID_PAYMENT_TYPE() { return ID_PAYMENT_TYPE; }
        public String getSTATE() { return STATE; }
        public BigDecimal getAMOUNT() { return AMOUNT; }
        public BigDecimal getID_DETAIL_PRODUCT() { return ID_DETAIL_PRODUCT; }
        public BigDecimal getMENU_ID() { return MENU_ID; }

        public void setTICKET_ID(BigDecimal TICKET_ID) { this.TICKET_ID = TICKET_ID; }
        public void setSALE_DATE(Timestamp SALE_DATE) { this.SALE_DATE = SALE_DATE; }
        public void setTOTAL_PAYMENT(BigDecimal TOTAL_PAYMENT) { this.TOTAL_PAYMENT = TOTAL_PAYMENT; }
        public void setDELIVERY(String DELIVERY) { this.DELIVERY = DELIVERY; }
        public void setDELIVERY_ADDRESS(String DELIVERY_ADDRESS) { this.DELIVERY_ADDRESS = DELIVERY_ADDRESS; }
        public void setNOTE(String NOTE) { this.NOTE = NOTE; }
        public void setUSER_ID(BigDecimal USER_ID) { this.USER_ID = USER_ID; }
        public void setID_TYPE_STATE(BigDecimal ID_TYPE_STATE) { this.ID_TYPE_STATE = ID_TYPE_STATE; }
        public void setID_PAYMENT_TYPE(BigDecimal ID_PAYMENT_TYPE) { this.ID_PAYMENT_TYPE = ID_PAYMENT_TYPE; }
        public void setSTATE(String STATE) { this.STATE = STATE; }
        public void setAMOUNT(BigDecimal AMOUNT) { this.AMOUNT = AMOUNT; }
        public void setID_DETAIL_PRODUCT(BigDecimal ID_DETAIL_PRODUCT) { this.ID_DETAIL_PRODUCT = ID_DETAIL_PRODUCT; }
        public void setMENU_ID(BigDecimal MENU_ID) { this.MENU_ID = MENU_ID; }
    }

    /**
     * Genera un reporte PDF de ventas (Sales Ticket)
     * @param ticketId ID de la venta específica
     * @return byte array del PDF generado
     * @throws Exception si hay error al generar el reporte
     */
    public byte[] generateSalesTicketReport(Long ticketId) throws Exception {
        try {

            List<SalesTicket> salesTickets = salesTicketService.findSalesForReport(ticketId);

            if (salesTickets.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron ventas para generar el reporte");
            }

            ClassPathResource resource = new ClassPathResource("reports/Sales_Ticket.jrxml");
            InputStream reportStream = resource.getInputStream();

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            Map<String, Object> parameters = new HashMap<>();
            if (ticketId != null) {
                parameters.put("P_TICKET_ID", BigDecimal.valueOf(ticketId));
            } else {
                parameters.put("P_TICKET_ID", null);
            }

            List<SalesReportDataRow> dataList = new ArrayList<>();
            
            for (SalesTicket ticket : salesTickets) {
                
                if (ticket.getProductDetails() == null || ticket.getProductDetails().isEmpty()) {
                    SalesReportDataRow row = new SalesReportDataRow();
                    mapSalesTicketToRow(ticket, row);
                    
                    row.setAMOUNT(null);
                    row.setID_DETAIL_PRODUCT(null);
                    row.setMENU_ID(null);
                    dataList.add(row);
                } else {
                    
                    for (ProductDetail detail : ticket.getProductDetails()) {
                        SalesReportDataRow row = new SalesReportDataRow();
                        mapSalesTicketToRow(ticket, row);
                       
                        row.setAMOUNT(detail.getAmount());
                        row.setID_DETAIL_PRODUCT(detail.getIdDetailProduct() != null ? 
                            BigDecimal.valueOf(detail.getIdDetailProduct()) : null);
                        row.setMENU_ID(detail.getMenuId() != null ? 
                            BigDecimal.valueOf(detail.getMenuId()) : null);
                        dataList.add(row);
                    }
                }
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            log.error("Error al generar el reporte de ventas", e);
            throw new Exception("Error al generar el reporte: " + e.getMessage(), e);
        }
    }

    private void mapSalesTicketToRow(SalesTicket ticket, SalesReportDataRow row) {
        row.setTICKET_ID(ticket.getTicketId() != null ? BigDecimal.valueOf(ticket.getTicketId()) : null);
        row.setSALE_DATE(ticket.getSaleDate() != null ? Timestamp.valueOf(ticket.getSaleDate()) : null);
        row.setTOTAL_PAYMENT(ticket.getTotalPayment());
        row.setDELIVERY(ticket.getDelivery());
        row.setDELIVERY_ADDRESS(ticket.getDeliveryAddress());
        row.setNOTE(ticket.getNote());
        row.setUSER_ID(ticket.getUserId() != null ? BigDecimal.valueOf(ticket.getUserId()) : null);
        row.setID_TYPE_STATE(ticket.getIdTypeState() != null ? BigDecimal.valueOf(ticket.getIdTypeState()) : null);
        row.setID_PAYMENT_TYPE(ticket.getIdPaymentType() != null ? BigDecimal.valueOf(ticket.getIdPaymentType()) : null);
        row.setSTATE(ticket.getState());
    }
}

