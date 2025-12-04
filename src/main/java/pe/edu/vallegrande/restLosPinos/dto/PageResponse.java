package pe.edu.vallegrande.restLosPinos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private int page;
    private int size;
    private List<T> content;
    private long totalElements;
    private int totalPages;
}
