package pe.edu.vallegrande.restLosPinos.dto;

public record ApiResponse<T>(boolean status, String mensaje, T content) {

    public static <T> ApiResponse<T> success(String mensaje, T content) {
        return new ApiResponse<>(Boolean.TRUE, mensaje, content);
    }

    public static <T> ApiResponse<T> error(String mensaje, T content) {
        return new ApiResponse<>(Boolean.FALSE, mensaje, content);
    }
}


