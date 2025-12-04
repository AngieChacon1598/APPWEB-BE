package pe.edu.vallegrande.restLosPinos.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pe.edu.vallegrande.restLosPinos.model.ProductDetail;
import pe.edu.vallegrande.restLosPinos.dto.ProductDetailDTO;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {
    ProductDetailMapper INSTANCE = Mappers.getMapper(ProductDetailMapper.class);

    @Mapping(target = "menuName", source = "menu.name")
    @Mapping(target = "menuPrice", source = "menu.price")
    ProductDetailDTO toDTO(ProductDetail productDetail);
}
