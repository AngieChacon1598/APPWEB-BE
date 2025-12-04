package pe.edu.vallegrande.restLosPinos.service;

import org.springframework.stereotype.Service;
import pe.edu.vallegrande.restLosPinos.model.ProductDetail;
import pe.edu.vallegrande.restLosPinos.dto.ProductDetailDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductDetailMapperService {

    public ProductDetailDTO toDTO(ProductDetail productDetail) {
        if (productDetail == null) {
            return null;
        }

        return ProductDetailDTO.builder()
                .idDetailProduct(productDetail.getIdDetailProduct())
                .amount(productDetail.getAmount())
                .ticketId(productDetail.getTicketId())
                .menuId(productDetail.getMenuId())
                .menuName(productDetail.getMenu() != null ? productDetail.getMenu().getName() : null)
                .menuPrice(productDetail.getMenu() != null ? productDetail.getMenu().getPrice() : null)
                .build();
    }

    public List<ProductDetailDTO> toDTOList(List<ProductDetail> productDetails) {
        return productDetails.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
