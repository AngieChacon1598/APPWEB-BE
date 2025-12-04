package pe.edu.vallegrande.restLosPinos.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupInfoPrinter implements ApplicationListener<ApplicationReadyEvent> {

  private final Environment env;

  public StartupInfoPrinter(Environment env) {
    this.env = env;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    // Prefer HOST_PORT (host mapping), luego PORT, luego server.port
    String hostPort = System.getenv("HOST_PORT");
    if (hostPort == null || hostPort.isEmpty()) {
      hostPort = System.getenv("PORT");
    }
    if (hostPort == null || hostPort.isEmpty()) {
      String serverPort = env.getProperty("server.port");
      if (serverPort != null && !serverPort.isEmpty()) {
        hostPort = serverPort;
      } else {
        hostPort = "8081";
      }
    }

    String endpoint = String.format("http://localhost:%s/v1/api/hola", hostPort);
    System.out.println("=== Servicio RestLosPinos iniciado ===");
    System.out.println("Endpoint disponible: " + endpoint);
  }
}
