package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.restLosPinos.model.ProductDetail;
import pe.edu.vallegrande.restLosPinos.repository.ProductDetailRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;

    // Listar todos los detalles
    public List<ProductDetail> findAll() {
        return productDetailRepository.findAll();
    }

    // Buscar detalle por ID
    public Optional<ProductDetail> findById(Long id) {
        return productDetailRepository.findById(id);
    }

    // Buscar detalles por ticket
    public List<ProductDetail> findByTicketId(Long ticketId) {
        return productDetailRepository.findByTicketId(ticketId);
    }

    // Buscar detalles por producto
    public List<ProductDetail> findByMenuId(Long menuId) {
        return productDetailRepository.findByMenuId(menuId);
    }

    // Crear nuevo detalle
    @Transactional
    public ProductDetail create(ProductDetail productDetail) {
        return productDetailRepository.save(productDetail);
    }

    // Actualizar detalle
    @Transactional
    public ProductDetail update(ProductDetail productDetail) {
        return productDetailRepository.save(productDetail);
    }

    // Eliminar detalle
    @Transactional
    public void delete(Long id) {
        productDetailRepository.deleteById(id);
    }

    // Obtener productos más vendidos
    public List<Object[]> findMostSoldProducts() {
        return productDetailRepository.findMostSoldProducts();
    }
}
