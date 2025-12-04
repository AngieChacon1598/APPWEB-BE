package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.model.ProductDetail;
import pe.edu.vallegrande.restLosPinos.dto.ProductDetailDTO;
import pe.edu.vallegrande.restLosPinos.service.ProductDetailMapperService;
import pe.edu.vallegrande.restLosPinos.service.ProductDetailService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductDetailRest {

    private final ProductDetailService productDetailService;
    private final ProductDetailMapperService productDetailMapperService;

    // GET /api/product-details - Obtener todos los detalles
    @GetMapping
    public ResponseEntity<List<ProductDetailDTO>> getAll() {
        List<ProductDetail> productDetails = productDetailService.findAll();
        List<ProductDetailDTO> productDetailDTOs = productDetailMapperService.toDTOList(productDetails);
        return ResponseEntity.ok(productDetailDTOs);
    }

    // GET /api/product-details/{id} - Obtener detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getById(@PathVariable Long id) {
        Optional<ProductDetail> productDetail = productDetailService.findById(id);
        return productDetail.map(detail -> ResponseEntity.ok(productDetailMapperService.toDTO(detail)))
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/product-details/ticket/{ticketId} - Obtener detalles por ticket
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<ProductDetailDTO>> getByTicketId(@PathVariable Long ticketId) {
        List<ProductDetail> productDetails = productDetailService.findByTicketId(ticketId);
        List<ProductDetailDTO> productDetailDTOs = productDetailMapperService.toDTOList(productDetails);
        return ResponseEntity.ok(productDetailDTOs);
    }

    // GET /api/product-details/menu/{menuId} - Obtener detalles por producto
    @GetMapping("/menu/{menuId}")
    public ResponseEntity<List<ProductDetailDTO>> getByMenuId(@PathVariable Long menuId) {
        List<ProductDetail> productDetails = productDetailService.findByMenuId(menuId);
        List<ProductDetailDTO> productDetailDTOs = productDetailMapperService.toDTOList(productDetails);
        return ResponseEntity.ok(productDetailDTOs);
    }

    // GET /api/product-details/most-sold - Obtener productos más vendidos
    @GetMapping("/most-sold")
    public ResponseEntity<List<Object[]>> getMostSoldProducts() {
        List<Object[]> mostSoldProducts = productDetailService.findMostSoldProducts();
        return ResponseEntity.ok(mostSoldProducts);
    }

    // POST /api/product-details - Crear nuevo detalle
    @PostMapping
    public ResponseEntity<ProductDetail> create(@RequestBody ProductDetail productDetail) {
        ProductDetail createdProductDetail = productDetailService.create(productDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDetail);
    }

    // PUT /api/product-details/{id} - Actualizar detalle
    @PutMapping("/{id}")
    public ResponseEntity<ProductDetail> update(@PathVariable Long id, @RequestBody ProductDetail productDetail) {
        productDetail.setIdDetailProduct(id);
        ProductDetail updatedProductDetail = productDetailService.update(productDetail);
        return ResponseEntity.ok(updatedProductDetail);
    }

    // DELETE /api/product-details/{id} - Eliminar detalle
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
