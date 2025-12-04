package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.restLosPinos.model.SalesTicket;
import pe.edu.vallegrande.restLosPinos.dto.SalesTicketDTO;
import pe.edu.vallegrande.restLosPinos.model.mapper.SalesMapper;
import pe.edu.vallegrande.restLosPinos.repository.SalesTicketRepository;
import pe.edu.vallegrande.restLosPinos.repository.OrderStatusTypeRepository;
import pe.edu.vallegrande.restLosPinos.repository.PaymentTypeRepository;
import pe.edu.vallegrande.restLosPinos.repository.MenuRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesTicketService {

    private final SalesTicketRepository salesTicketRepository;
    private final SalesMapper salesMapper;
    private final OrderStatusTypeRepository orderStatusTypeRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final MenuRepository menuRepository;

    /**
     * Obtiene todas las ventas activas
     * @return Lista de DTOs de ventas activas
     */
    public List<SalesTicketDTO> findAllActive() {
        return salesTicketRepository.findActiveSales().stream()
                .map(salesMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las ventas inactivas
     * @return Lista de DTOs de ventas inactivas
     */
    public List<SalesTicketDTO> findAllInactive() {
        return salesTicketRepository.findInactiveSales().stream()
                .map(salesMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las ventas
     * @return Lista de DTOs de todas las ventas
     */
    public List<SalesTicketDTO> findAll() {
        return salesTicketRepository.findAll().stream()
                .map(salesMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca una venta por su ID
     * @param id ID de la venta
     * @return DTO de la venta si existe
     */
    public Optional<SalesTicketDTO> findById(Long id) {
        return salesTicketRepository.findById(id)
                .map(salesMapper::toDTO);
    }

    /**
     * Busca ventas por ID de usuario
     * @param userId ID del usuario
     * @return Lista de DTOs de ventas del usuario
     */
    public List<SalesTicketDTO> findByUserId(Long userId) {
        return salesTicketRepository.findByUserId(userId).stream()
                .map(salesMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca ventas por estado
     * @param statusId ID del estado
     * @return Lista de DTOs de ventas con el estado especificado
     */
    public List<SalesTicketDTO> findByStatus(Long statusId) {
        return salesTicketRepository.findByStatusId(statusId).stream()
                .map(salesMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las ventas con entrega a domicilio
     */
    public List<SalesTicketDTO> findDeliverySales() {
        return salesTicketRepository.findDeliverySales().stream()
                .map(salesMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las ventas con entrega a domicilio con paginación
     */
    public Page<SalesTicketDTO> findDeliverySales(Pageable pageable) {
        return salesTicketRepository.findDeliverySales(pageable)
                .map(salesMapper::toDTO);
    }

    /**
     * Obtiene las ventas en un rango de fechas
     */
    public List<SalesTicketDTO> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return salesTicketRepository.findByDateRange(startDate, endDate).stream()
                .map(salesMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las ventas en un rango de fechas con paginación
     */
    public Page<SalesTicketDTO> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return salesTicketRepository.findByDateRange(startDate, endDate, pageable)
                .map(salesMapper::toDTO);
    }

    /**
     * Busca ventas con paginación
     * @param pageable Configuración de paginación
     * @return Página de DTOs de ventas
     */
    public Page<SalesTicketDTO> findAll(Pageable pageable) {
        return salesTicketRepository.findAll(pageable)
                .map(salesMapper::toDTO);
    }

    // Crear nueva venta
    @Transactional
    public SalesTicketDTO save(SalesTicket salesTicket) {
        // Establecer las relaciones antes de guardar
        if (salesTicket.getIdTypeState() != null) {
            orderStatusTypeRepository.findById(salesTicket.getIdTypeState())
                .ifPresent(salesTicket::setOrderStatusType);
        }
        
        if (salesTicket.getIdPaymentType() != null) {
            paymentTypeRepository.findById(salesTicket.getIdPaymentType())
                .ifPresent(salesTicket::setPaymentType);
        }
        
        // Establecer relaciones para ProductDetails
        if (salesTicket.getProductDetails() != null) {
            salesTicket.getProductDetails().forEach(detail -> {
                if (detail.getMenuId() != null) {
                    menuRepository.findById(detail.getMenuId())
                        .ifPresent(detail::setMenu);
                }
                detail.setSalesTicket(salesTicket);
            });
        }
        
        SalesTicket savedTicket = salesTicketRepository.save(salesTicket);
        return salesMapper.toDTO(savedTicket);
    }

    // Actualizar venta
    @Transactional
    public SalesTicketDTO update(SalesTicket salesTicket) {
        // Establecer las relaciones antes de actualizar (igual que en save)
        if (salesTicket.getIdTypeState() != null) {
            orderStatusTypeRepository.findById(salesTicket.getIdTypeState())
                .ifPresent(salesTicket::setOrderStatusType);
        }
        
        if (salesTicket.getIdPaymentType() != null) {
            paymentTypeRepository.findById(salesTicket.getIdPaymentType())
                .ifPresent(salesTicket::setPaymentType);
        }
        
        // Establecer relaciones para ProductDetails
        if (salesTicket.getProductDetails() != null) {
            salesTicket.getProductDetails().forEach(detail -> {
                if (detail.getMenuId() != null) {
                    menuRepository.findById(detail.getMenuId())
                        .ifPresent(detail::setMenu);
                }
                detail.setSalesTicket(salesTicket);
            });
        }
        
        SalesTicket updatedTicket = salesTicketRepository.save(salesTicket);
        return salesMapper.toDTO(updatedTicket);
    }

    // Eliminación lógica
    @Transactional
    public void logicalDelete(Long id) {
        salesTicketRepository.findById(id).ifPresent(salesTicket -> {
            salesTicket.setState("I");
            salesTicketRepository.save(salesTicket);
        });
    }

    // Restaura una venta eliminada lógicamente
    @Transactional
    public void restore(Long id) {
        salesTicketRepository.restore(id);
    }

    // Eliminación física
    @Transactional
    public void physicalDelete(Long id) {
        salesTicketRepository.deleteById(id);
    }

    // Eliminación lógica de una venta
    @Transactional
    public void delete(Long id) {
        salesTicketRepository.softDelete(id);
    }

    /**
     * Obtiene las ventas con sus detalles para generar reportes
     * @param ticketId ID de la venta específica (null para todas las ventas)
     * @return Lista de ventas con sus detalles cargados
     */
    public List<SalesTicket> findSalesForReport(Long ticketId) {
        return salesTicketRepository.findSalesForReport(ticketId);
    }
}
