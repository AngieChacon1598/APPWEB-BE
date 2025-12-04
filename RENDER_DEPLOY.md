# Guía de Despliegue en Render

Esta guía te ayudará a desplegar la aplicación RestLosPinos en Render.

## Requisitos Previos

1. Cuenta en [Render](https://render.com)
2. Repositorio Git (GitHub, GitLab, o Bitbucket) con el código
3. Acceso a la base de datos Oracle (con credenciales y URL de conexión)

## Pasos para Desplegar

### 1. Conectar el Repositorio

1. Inicia sesión en [Render Dashboard](https://dashboard.render.com)
2. Haz clic en **"New +"** y selecciona **"Web Service"**
3. Conecta tu repositorio Git
4. Selecciona el repositorio y la rama (recomendado: `develop` o `main`)

### 2. Configurar el Servicio

Render detectará automáticamente el archivo `render.yaml`. Si prefieres configurar manualmente:

- **Name**: `rest-los-pinos` (o el nombre que prefieras)
- **Environment**: `Docker`
- **Region**: Selecciona la región más cercana a tus usuarios
- **Branch**: `develop` (o la rama que uses)
- **Root Directory**: Dejar vacío (raíz del proyecto)
- **Dockerfile Path**: `./Dockerfile`
- **Docker Context**: `.`

### 3. Configurar Variables de Entorno

En la sección **"Environment"** del servicio, agrega las siguientes variables:

#### Variables Requeridas:

```
PORT=8081
```

```
DB_USERNAME=tu_usuario_oracle
```

```
DB_PASSWORD=tu_contraseña_oracle
```

```
DATABASE_URL=jdbc:oracle:thin:@tu_servidor_oracle:puerto/servicio
```

**Nota**: Si usas Oracle Wallet, el formato de `DATABASE_URL` debe incluir el parámetro `TNS_ADMIN`:
```
DATABASE_URL=jdbc:oracle:thin:@nombre_tns?TNS_ADMIN=/app/wallet
```

#### Variables JWT:

```
JWT_SECRET=ZXN0YSBlcyBtaWMgY2xhdmUgc2VjcmV0YSBubyBsYSBwdWVkZXMgaGFrZWFy
```

```
JWT_SECRET_INCRIPT=Y29uIGVzdGFzIHBhbGFicmFz
```

```
JWT_EXPIRATION=604800000
```

### 4. Configuración de Base de Datos Oracle

#### Opción A: Conexión Directa (sin Wallet)

Si tu base de datos Oracle permite conexiones directas sin wallet SSL:

```
DATABASE_URL=jdbc:oracle:thin:@host:puerto:servicio
```

Ejemplo:
```
DATABASE_URL=jdbc:oracle:thin:@mi-servidor.com:1521:XE
```

#### Opción B: Con Oracle Wallet (SSL)

Si necesitas usar Oracle Wallet para SSL:

1. El wallet ya está incluido en la imagen Docker (`/app/wallet`)
2. Usa el formato de URL con `TNS_ADMIN`:
```
DATABASE_URL=jdbc:oracle:thin:@nombre_tns?TNS_ADMIN=/app/wallet
```

Donde `nombre_tns` es el nombre del servicio definido en `tnsnames.ora`

### 5. Desplegar

1. Haz clic en **"Create Web Service"**
2. Render comenzará a construir la imagen Docker
3. El proceso puede tardar varios minutos la primera vez
4. Una vez completado, verás la URL de tu aplicación (ej: `https://rest-los-pinos.onrender.com`)

### 6. Verificar el Despliegue

1. Visita la URL proporcionada por Render
2. Prueba el endpoint de health check: `https://tu-app.onrender.com/v1/api/hola`
3. Deberías ver: `"Hola Mundo"`

## Solución de Problemas

### Error de Conexión a Base de Datos

- Verifica que las credenciales de `DB_USERNAME` y `DB_PASSWORD` sean correctas
- Verifica que el formato de `DATABASE_URL` sea correcto
- Si usas Oracle Wallet, asegúrate de que el nombre TNS coincida con el de `tnsnames.ora`

### Error en el Build

- Verifica los logs de build en Render Dashboard
- Asegúrate de que todas las dependencias en `pom.xml` sean accesibles
- Verifica que el Dockerfile esté en la raíz del proyecto

### La Aplicación No Inicia

- Revisa los logs del servicio en Render Dashboard
- Verifica que todas las variables de entorno estén configuradas
- Asegúrate de que el puerto esté configurado correctamente (Render asigna el puerto automáticamente)

### Timeout en Health Check

- El endpoint de health check está configurado en `/v1/api/hola`
- Si este endpoint no responde, Render marcará el servicio como no saludable
- Verifica que el endpoint esté accesible y responda rápidamente

## Costos

- **Plan Starter**: Gratis (con limitaciones)
  - 750 horas/mes gratis
  - El servicio se "duerme" después de 15 minutos de inactividad
  - Tarda ~30-60 segundos en "despertar"
  
- **Plan Standard**: Desde $7/mes
  - Sin tiempo de inactividad
  - Mejor rendimiento

## Notas Importantes

1. **Tiempo de Inactividad**: En el plan gratuito, el servicio se duerme después de 15 minutos sin tráfico. La primera solicitud después de dormir puede tardar 30-60 segundos.

2. **Variables Sensibles**: Nunca commitees credenciales en el código. Usa siempre variables de entorno en Render.

3. **Base de Datos**: Render no ofrece bases de datos Oracle. Necesitarás una base de datos Oracle externa (Oracle Cloud, AWS RDS, etc.).

4. **Logs**: Puedes ver los logs en tiempo real en el Dashboard de Render.

## Actualizaciones Futuras

Para actualizar la aplicación:

1. Haz push de tus cambios al repositorio Git
2. Render detectará automáticamente los cambios (si `autoDeploy` está habilitado)
3. O manualmente haz clic en **"Manual Deploy"** en el Dashboard

## Soporte

Si tienes problemas:
1. Revisa los logs en Render Dashboard
2. Consulta la [documentación de Render](https://render.com/docs)
3. Verifica que todas las variables de entorno estén configuradas correctamente

