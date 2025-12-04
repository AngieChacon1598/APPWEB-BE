package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.model.SalesTicket;
import pe.edu.vallegrande.restLosPinos.dto.SalesTicketDTO;
import pe.edu.vallegrande.restLosPinos.service.SalesTicketService;
import pe.edu.vallegrande.restLosPinos.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesTicketRest {

    private final SalesTicketService salesTicketService;
    private final ReportService reportService;
    
    /**
     * Obtiene todas las ventas con paginación
     */
    @GetMapping("/page")
    public ResponseEntity<Page<SalesTicketDTO>> getAllPaginated(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<SalesTicketDTO> page = salesTicketService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Obtiene todas las ventas activas
     */
    @GetMapping
    public ResponseEntity<List<SalesTicketDTO>> getAllActive() {
        List<SalesTicketDTO> sales = salesTicketService.findAllActive();
        return ResponseEntity.ok(sales);
    }

    /**
     * Obtiene todas las ventas (activas e inactivas)
     */
    @GetMapping("/all")
    public ResponseEntity<List<SalesTicketDTO>> getAll() {
        List<SalesTicketDTO> sales = salesTicketService.findAll();
        return ResponseEntity.ok(sales);
    }

    /**
     * Obtiene todas las ventas inactivas
     */
    @GetMapping("/inactive")
    public ResponseEntity<List<SalesTicketDTO>> getInactive() {
        List<SalesTicketDTO> sales = salesTicketService.findAllInactive();
        return ResponseEntity.ok(sales);
    }

    /**
     * Obtiene una venta por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalesTicketDTO> getById(@PathVariable Long id) {
        return salesTicketService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene las ventas de un usuario específico
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SalesTicketDTO>> getByUserId(@PathVariable Long userId) {
        List<SalesTicketDTO> sales = salesTicketService.findByUserId(userId);
        return ResponseEntity.ok(sales);
    }

    /**
     * Obtiene las ventas por estado
     */
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<SalesTicketDTO>> getByStatus(@PathVariable Long statusId) {
        List<SalesTicketDTO> sales = salesTicketService.findByStatus(statusId);
        return ResponseEntity.ok(sales);
    }

    /**
     * Obtiene las ventas en un rango de fechas
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<SalesTicketDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<SalesTicketDTO> sales = salesTicketService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    /**
     * Obtiene las ventas con entrega a domicilio
     */
    @GetMapping("/delivery")
    public ResponseEntity<List<SalesTicketDTO>> getDeliverySales() {
        List<SalesTicketDTO> sales = salesTicketService.findDeliverySales();
        return ResponseEntity.ok(sales);
    }

    /**
     * Crea una nueva venta
     */
    @PostMapping
    public ResponseEntity<SalesTicketDTO> create(@RequestBody SalesTicket salesTicket) {
        SalesTicketDTO createdSalesTicket = salesTicketService.save(salesTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSalesTicket);
    }

    /**
     * Actualiza una venta existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalesTicketDTO> update(
            @PathVariable Long id, 
            @RequestBody SalesTicket salesTicket) {
        salesTicket.setTicketId(id);
        SalesTicketDTO updatedSalesTicket = salesTicketService.update(salesTicket);
        return ResponseEntity.ok(updatedSalesTicket);
    }

    /**
     * Eliminación lógica de una venta
     */
    @DeleteMapping("/logical/{id}")
    public ResponseEntity<Void> logicalDelete(@PathVariable Long id) {
        salesTicketService.logicalDelete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Eliminación física de una venta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salesTicketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura una venta eliminada lógicamente
     */
    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        salesTicketService.restore(id);
        return ResponseEntity.noContent().build();
    }

    // ========== ENDPOINTS ESPECÍFICOS PARA CLIENTES ==========

    /**
     * Endpoint para que los clientes realicen una compra
     */
    @PostMapping("/compra")
    public ResponseEntity<SalesTicketDTO> realizarCompra(@RequestBody SalesTicket salesTicket) {
        SalesTicketDTO createdSalesTicket = salesTicketService.save(salesTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSalesTicket);
    }

    /**
     * Endpoint para que los clientes vean sus propias compras
     * Nota: Este endpoint debería filtrar por el usuario autenticado
     */
    @GetMapping("/mis-compras")
    public ResponseEntity<List<SalesTicketDTO>> getMisCompras(
            @RequestParam Long userId) {
        List<SalesTicketDTO> sales = salesTicketService.findByUserId(userId);
        return ResponseEntity.ok(sales);
    }

    /**
     * Genera un reporte PDF de ventas
     * @param ticketId ID de la venta específica (opcional, si no se proporciona muestra todas las ventas)
     * @return PDF del reporte
     */
    @GetMapping("/report")
    public ResponseEntity<byte[]> generateReport(
            @RequestParam(required = false) Long ticketId) {
        try {
            byte[] pdfBytes = reportService.generateSalesTicketReport(ticketId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            
            String filename = ticketId != null 
                ? String.format("reporte_venta_%d.pdf", ticketId)
                : String.format("reporte_ventas_%d.pdf", System.currentTimeMillis());
            
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
