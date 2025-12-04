package pe.edu.vallegrande.restLosPinos.model.mapper;

import org.mapstruct.*;
import pe.edu.vallegrande.restLosPinos.model.ProductDetail;
import pe.edu.vallegrande.restLosPinos.model.SalesTicket;
import pe.edu.vallegrande.restLosPinos.dto.ProductDetailDTO;
import pe.edu.vallegrande.restLosPinos.dto.SalesTicketDTO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SalesMapper {

    @Mapping(target = "productDetails", expression = "java(mapProductDetails(salesTicket.getProductDetails()))")
    @Mapping(target = "orderStatus", expression = "java(getOrderStatusName(salesTicket))")
    @Mapping(target = "paymentType", expression = "java(getPaymentTypeName(salesTicket))")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "stateName", expression = "java(getOrderStatusName(salesTicket))")
    @Mapping(target = "paymentTypeName", expression = "java(getPaymentTypeName(salesTicket))")
    public abstract SalesTicketDTO toDTO(SalesTicket salesTicket);

    @Mapping(target = "menuId", expression = "java(getMenuId(detail))")
    @Mapping(target = "menuName", expression = "java(getMenuName(detail))")
    @Mapping(target = "menuPrice", expression = "java(getMenuPrice(detail))")
    @Mapping(target = "idDetailProduct", source = "idDetailProduct")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "ticketId", source = "ticketId")
    @Mapping(target = "menu", ignore = true)
    protected abstract ProductDetailDTO toProductDetailWithMenuDTO(ProductDetail detail);

    protected Long getMenuId(ProductDetail detail) {
        return Optional.ofNullable(detail)
                .map(ProductDetail::getMenu)
                .map(menu -> menu.getMenuId())
                .orElse(null);
    }

    protected String getMenuName(ProductDetail detail) {
        return Optional.ofNullable(detail)
                .map(ProductDetail::getMenu)
                .map(menu -> menu.getName())
                .orElse(null);
    }

    protected BigDecimal getMenuPrice(ProductDetail detail) {
        return Optional.ofNullable(detail)
                .map(ProductDetail::getMenu)
                .map(menu -> menu.getPrice())
                .orElse(null);
    }

    protected String getOrderStatusName(SalesTicket salesTicket) {
        return Optional.ofNullable(salesTicket)
                .map(SalesTicket::getOrderStatusType)
                .map(status -> status.getName())
                .orElse(null);
    }

    protected String getPaymentTypeName(SalesTicket salesTicket) {
        return Optional.ofNullable(salesTicket)
                .map(SalesTicket::getPaymentType)
                .map(payment -> payment.getName())
                .orElse(null);
    }

    protected List<ProductDetailDTO> mapProductDetails(List<ProductDetail> details) {
        if (details == null) {
            return Collections.emptyList();
        }
        return details.stream()
                .map(this::toProductDetailWithMenuDTO)
                .collect(Collectors.toList());
    }

    @AfterMapping
    protected void afterMapping(SalesTicket source, @MappingTarget SalesTicketDTO target) {
        // Additional mappings if needed
        if (source.getOrderStatusType() != null) {
            target.setOrderStatus(source.getOrderStatusType().getName());
        }
        if (source.getPaymentType() != null) {
            target.setPaymentType(source.getPaymentType().getName());
        }
    }
}
