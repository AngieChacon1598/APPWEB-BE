# Manual de Despliegue Frontend React en Render

## Paso 1: Preparar el Repositorio Frontend

### 1.1 Verificar que tienes un archivo de configuración para variables de entorno

Crea o verifica que tengas un archivo `.env.production` o `.env` en tu proyecto React con:

```env
REACT_APP_API_URL=https://appweb-be.onrender.com
```

**Importante**: Las variables de entorno en React deben comenzar con `REACT_APP_` para que sean accesibles.

### 1.2 Actualizar tu código para usar la variable de entorno

En tu código React, usa la variable así:

```javascript
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081';
```

## Paso 2: Crear el Servicio en Render

1. Ve a [Render Dashboard](https://dashboard.render.com)
2. Haz clic en **"New +"** → **"Static Site"**
3. Conecta tu repositorio Git del frontend
4. Selecciona el repositorio y la rama (ej: `main` o `develop`)

## Paso 3: Configurar el Servicio

### Configuración Básica:

- **Name**: `appweb-fe` (o el nombre que prefieras)
- **Branch**: `main` (o la rama que uses)
- **Root Directory**: Déjalo vacío (a menos que tu React esté en una subcarpeta)
- **Build Command**: 
  ```
  npm install && npm run build
  ```
  O si usas yarn:
  ```
  yarn install && yarn build
  ```
- **Publish Directory**: 
  ```
  build
  ```
  (Para Create React App, el directorio de salida es `build`. Si usas Vite, sería `dist`)

### Variables de Entorno:

En la sección **"Environment"**, agrega:

```
REACT_APP_API_URL=https://appweb-be.onrender.com
```

**Nota**: Render reconstruirá la aplicación cuando cambies variables de entorno.

## Paso 4: Configurar CORS en el Backend

Necesitas actualizar el backend para permitir peticiones desde tu frontend desplegado.

### Opción A: Actualizar WebConfig.java (Recomendado)

Actualiza el archivo `src/main/java/pe/edu/vallegrande/restLosPinos/config/WebConfig.java`:

```java
.allowedOrigins(
    "https://vallegrande.github.io",
    "http://localhost:4200",
    "http://localhost:3000",  // React por defecto
    "https://appweb-fe.onrender.com",  // Tu URL de Render (ajusta el nombre)
    "*"  // Temporal para desarrollo, considera remover en producción
)
```

### Opción B: Usar Variable de Entorno

Mejor aún, usa una variable de entorno para el origen permitido:

```java
.allowedOrigins(
    System.getenv("ALLOWED_ORIGINS") != null 
        ? System.getenv("ALLOWED_ORIGINS").split(",")
        : new String[]{"http://localhost:3000", "http://localhost:4200"}
)
```

Y agrega en Render (backend):
```
ALLOWED_ORIGINS=https://appweb-fe.onrender.com,http://localhost:3000
```

## Paso 5: Desplegar

1. Haz clic en **"Create Static Site"**
2. Render comenzará a construir tu aplicación
3. El proceso puede tardar varios minutos la primera vez
4. Una vez completado, tendrás una URL como: `https://appweb-fe.onrender.com`

## Paso 6: Verificar el Despliegue

1. Visita la URL proporcionada por Render
2. Abre la consola del navegador (F12) para verificar que no hay errores
3. Prueba hacer una petición al backend desde el frontend
4. Verifica que las peticiones se estén haciendo a `https://appweb-be.onrender.com`

## Solución de Problemas

### Error: "Failed to build"

- Verifica que el comando de build sea correcto
- Revisa los logs de build en Render Dashboard
- Asegúrate de que `package.json` tenga el script `build`

### Error de CORS en el navegador

- Verifica que hayas actualizado `WebConfig.java` en el backend
- Asegúrate de que la URL del frontend esté en `allowedOrigins`
- Reinicia el servicio del backend después de actualizar CORS

### Las variables de entorno no funcionan

- Asegúrate de que las variables comiencen con `REACT_APP_`
- Reinicia el build después de agregar variables de entorno
- Verifica que estés usando `process.env.REACT_APP_API_URL` en tu código

### El frontend no se actualiza

- Render reconstruye automáticamente cuando haces push
- Si no se actualiza, haz clic en **"Manual Deploy"** en Render Dashboard

## Estructura de Archivos Recomendada

```
tu-frontend-react/
├── .env.production          # Variables para producción
├── .env.development         # Variables para desarrollo
├── package.json
├── public/
└── src/
    └── config/
        └── api.js          # Configuración de API
```

### Ejemplo de `src/config/api.js`:

```javascript
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081';

export const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});
```

## Notas Importantes

1. **Plan Gratuito**: En el plan gratuito, el sitio estático se "duerme" después de inactividad, pero se activa rápidamente.

2. **Actualizaciones Automáticas**: Render reconstruye automáticamente cuando haces push al repositorio.

3. **Variables de Entorno**: Las variables de entorno se inyectan durante el build, no en runtime. Si cambias una variable, necesitas reconstruir.

4. **HTTPS**: Render proporciona HTTPS automáticamente.

5. **Custom Domain**: Puedes agregar un dominio personalizado en la configuración del servicio.

## Resumen Rápido

1. ✅ Crear `.env.production` con `REACT_APP_API_URL=https://appweb-be.onrender.com`
2. ✅ En Render: New → Static Site
3. ✅ Build Command: `npm install && npm run build`
4. ✅ Publish Directory: `build`
5. ✅ Variable de entorno: `REACT_APP_API_URL=https://appweb-be.onrender.com`
6. ✅ Actualizar CORS en backend
7. ✅ Deploy!

¡Listo! Tu frontend React estará desplegado en Render.

