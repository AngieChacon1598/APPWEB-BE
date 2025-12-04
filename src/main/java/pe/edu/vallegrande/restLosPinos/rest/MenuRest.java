package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.model.Menu;
import pe.edu.vallegrande.restLosPinos.service.MenuService;
import pe.edu.vallegrande.restLosPinos.service.ReportService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MenuRest {

    private final MenuService menuService;
    private final ReportService reportService;

    // GET /api/menu - Obtener todos los productos activos
    @GetMapping
    public ResponseEntity<List<Menu>> getAllActive() {
        List<Menu> menus = menuService.findAllActive();
        return ResponseEntity.ok(menus);
    }

    // GET /api/menu/all - Obtener todos los productos
    @GetMapping("/all")
    public ResponseEntity<List<Menu>> getAll() {
        List<Menu> menus = menuService.findAll();
        return ResponseEntity.ok(menus);
    }

    // GET /api/menu/inactive - Obtener productos inactivos
    @GetMapping("/inactive")
    public ResponseEntity<List<Menu>> getInactive() {
        List<Menu> menus = menuService.findAllInactive();
        return ResponseEntity.ok(menus);
    }

    // GET /api/menu/{id} - Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Menu> getById(@PathVariable Long id) {
        Optional<Menu> menu = menuService.findById(id);
        return menu.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/menu/category/{categoryId} - Obtener productos por categoría
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Menu>> getByCategory(@PathVariable Long categoryId) {
        List<Menu> menus = menuService.findByCategory(categoryId);
        return ResponseEntity.ok(menus);
    }

    // GET /api/menu/search?name={name} - Buscar productos por nombre
    @GetMapping("/search")
    public ResponseEntity<List<Menu>> searchByName(@RequestParam String name) {
        List<Menu> menus = menuService.findByName(name);
        return ResponseEntity.ok(menus);
    }

    // GET /api/menu/price-range?minPrice={min}&maxPrice={max} - Buscar por rango de precios
    @GetMapping("/price-range")
    public ResponseEntity<List<Menu>> getByPriceRange(
            @RequestParam Double minPrice, 
            @RequestParam Double maxPrice) {
        List<Menu> menus = menuService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(menus);
    }

    // POST /api/menu - Crear nuevo producto
    @PostMapping
    public ResponseEntity<Menu> create(@RequestBody Menu menu) {
        Menu createdMenu = menuService.create(menu);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    // PUT /api/menu/{id} - Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Menu> update(@PathVariable Long id, @RequestBody Menu menu) {
        menu.setMenuId(id);
        Menu updatedMenu = menuService.update(menu);
        return ResponseEntity.ok(updatedMenu);
    }

    // DELETE /api/menu/logical/{id} - Eliminación lógica
    @DeleteMapping("/logical/{id}")
    public ResponseEntity<Void> logicalDelete(@PathVariable Long id) {
        menuService.logicalDelete(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/menu/physical/{id} - Eliminación física
    @DeleteMapping("/physical/{id}")
    public ResponseEntity<Void> physicalDelete(@PathVariable Long id) {
        menuService.physicalDelete(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/menu/restore/{id} - Restaurar producto
    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        menuService.restore(id);
        return ResponseEntity.ok().build();
    }

    // GET /api/menu/image/{filename} - Servir imágenes de menús
    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // Ruta donde se almacenan las imágenes (ajusta según tu estructura)
            Path imagePath = Paths.get("src/main/resources/static/images/menu").resolve(filename);
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Determinar el tipo de contenido basado en la extensión del archivo
                String contentType = determineContentType(filename);
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/menu/report?stateFilter={filter} - Generar reporte PDF de productos
    // stateFilter: 1=activos, 0=inactivos, -1=todos (por defecto)
    @GetMapping("/report")
    public ResponseEntity<byte[]> generateReport(
            @RequestParam(required = false, defaultValue = "-1") Integer stateFilter) {
        try {
            byte[] pdfBytes = reportService.generateProductosReport(stateFilter);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_productos.pdf");
            headers.setContentLength(pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método auxiliar para determinar el tipo de contenido
    private String determineContentType(String filename) {
        String extension = filename.toLowerCase();
        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (extension.endsWith(".png")) {
            return "image/png";
        } else if (extension.endsWith(".gif")) {
            return "image/gif";
        } else if (extension.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "application/octet-stream";
        }
    }
}
