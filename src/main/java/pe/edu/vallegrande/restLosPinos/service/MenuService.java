package pe.edu.vallegrande.restLosPinos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.restLosPinos.model.Menu;
import pe.edu.vallegrande.restLosPinos.repository.MenuRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    // Listar todos los productos activos
    public List<Menu> findAllActive() {
        return menuRepository.findActiveProducts();
    }

    // Listar todos los productos inactivos
    public List<Menu> findAllInactive() {
        return menuRepository.findInactiveProducts();
    }

    // Listar todos los productos
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    // Buscar producto por ID
    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    // Buscar productos por categoría
    public List<Menu> findByCategory(Long categoryId) {
        return menuRepository.findByCategoryId(categoryId);
    }

    // Crear nuevo producto
    @Transactional
    public Menu create(Menu menu) {
        menu.setCreatedAt(LocalDateTime.now());
        menu.setState(1);
        return menuRepository.save(menu);
    }

    // Actualizar producto
    @Transactional
    public Menu update(Menu menu) {
        // Buscar el producto existente para preservar datos importantes
        Optional<Menu> existingMenuOpt = menuRepository.findById(menu.getMenuId());
        
        if (existingMenuOpt.isPresent()) {
            Menu existingMenu = existingMenuOpt.get();
            
            // Preservar categoryId si viene null en la actualización
            if (menu.getCategoryId() == null) {
                menu.setCategoryId(existingMenu.getCategoryId());
            }
            
            // Preservar createdAt si no se proporciona
            if (menu.getCreatedAt() == null) {
                menu.setCreatedAt(existingMenu.getCreatedAt());
            }
            
            // Preservar state si no se proporciona
            if (menu.getState() == null) {
                menu.setState(existingMenu.getState());
            }
        }
        
        menu.setUpdatedAt(LocalDateTime.now());
        return menuRepository.save(menu);
    }

    // Eliminación lógica
    @Transactional
    public void logicalDelete(Long id) {
        menuRepository.findById(id).ifPresent(menu -> {
            menu.setState(0);
            menu.setUpdatedAt(LocalDateTime.now());
            menuRepository.save(menu);
        });
    }

    // Restaurar producto
    @Transactional
    public void restore(Long id) {
        menuRepository.findById(id).ifPresent(menu -> {
            menu.setState(1);
            menu.setUpdatedAt(LocalDateTime.now());
            menuRepository.save(menu);
        });
    }

    // Eliminación física
    @Transactional
    public void physicalDelete(Long id) {
        menuRepository.deleteById(id);
    }

    // Buscar por nombre
    public List<Menu> findByName(String name) {
        return menuRepository.findByNameContainingIgnoreCase(name);
    }

    // Buscar por rango de precios
    public List<Menu> findByPriceRange(Double minPrice, Double maxPrice) {
        return menuRepository.findByPriceRange(minPrice, maxPrice);
    }

    // Filtrar productos por estado: 1=activos, 0=inactivos, -1=todos
    public List<Menu> findByStateFilter(Integer stateFilter) {
        return menuRepository.findByStateFilter(stateFilter);
    }
}
