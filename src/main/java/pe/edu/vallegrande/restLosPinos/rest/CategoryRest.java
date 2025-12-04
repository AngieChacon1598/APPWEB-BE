package pe.edu.vallegrande.restLosPinos.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.restLosPinos.model.Category;
import pe.edu.vallegrande.restLosPinos.service.CategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryRest {

    private final CategoryService categoryService;

    // GET /api/categories - Obtener todas las categorías activas
    @GetMapping
    public ResponseEntity<List<Category>> getAllActive() {
        List<Category> categories = categoryService.findAllActive();
        return ResponseEntity.ok(categories);
    }

    // GET /api/categories/all - Obtener todas las categorías
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    // GET /api/categories/inactive - Obtener categorías inactivas
    @GetMapping("/inactive")
    public ResponseEntity<List<Category>> getInactive() {
        List<Category> categories = categoryService.findAllInactive();
        return ResponseEntity.ok(categories);
    }

    // GET /api/categories/{id} - Obtener categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/categories/search?name={name} - Buscar categorías por nombre
    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchByName(@RequestParam String name) {
        List<Category> categories = categoryService.findByName(name);
        return ResponseEntity.ok(categories);
    }

    // POST /api/categories - Crear nueva categoría
    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        Category createdCategory = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    // PUT /api/categories/{id} - Actualizar categoría
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category) {
        category.setCategoryId(id);
        Category updatedCategory = categoryService.update(category);
        return ResponseEntity.ok(updatedCategory);
    }

    // DELETE /api/categories/logical/{id} - Eliminación lógica
    @DeleteMapping("/logical/{id}")
    public ResponseEntity<Void> logicalDelete(@PathVariable Long id) {
        categoryService.logicalDelete(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/categories/physical/{id} - Eliminación física
    @DeleteMapping("/physical/{id}")
    public ResponseEntity<Void> physicalDelete(@PathVariable Long id) {
        categoryService.physicalDelete(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/categories/restore/{id} - Restaurar categoría
    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        categoryService.restore(id);
        return ResponseEntity.ok().build();
    }
}
