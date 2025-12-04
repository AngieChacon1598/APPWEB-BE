# Valores Exactos para Variables de Entorno en Render

Copia y pega estos valores exactos en el Dashboard de Render en la sección **"Environment"**.

## Variables de Entorno Requeridas

### 1. PORT
```
8081
```
**Nota**: Render asigna automáticamente un puerto, pero esta variable debe estar definida.

---

### 2. DB_USERNAME
```
DEVELOPER_ANGI
```

---

### 3. DB_PASSWORD
```
ResLosPinos_Angi_01
```

---

### 4. DATABASE_URL

**Opción A: Usando Oracle Wallet (Recomendado - SSL habilitado)**

El wallet ya está incluido en la imagen Docker en `/app/wallet`. Usa este formato:

```
jdbc:oracle:thin:@restaurantelospinos_low?TNS_ADMIN=/app/wallet
```

**Opciones disponibles según el nivel de servicio:**
- **Low (recomendado para desarrollo)**: `jdbc:oracle:thin:@restaurantelospinos_low?TNS_ADMIN=/app/wallet`
- **Medium**: `jdbc:oracle:thin:@restaurantelospinos_medium?TNS_ADMIN=/app/wallet`
- **High**: `jdbc:oracle:thin:@restaurantelospinos_high?TNS_ADMIN=/app/wallet`

**Opción B: Conexión directa (sin wallet)**

Si prefieres no usar wallet (no recomendado para producción):
```
jdbc:oracle:thin:@adb.us-chicago-1.oraclecloud.com:1522/g65f8c09571c594_restaurantelospinos_low.adb.oraclecloud.com
```

---

### 5. JWT_SECRET
```
ZXN0YSBlcyBtaWMgY2xhdmUgc2VjcmV0YSBubyBsYSBwdWVkZXMgaGFrZWFy
```

---

### 6. JWT_SECRET_INCRIPT
```
Y29uIGVzdGFzIHBhbGFicmFz
```

---

### 7. JWT_EXPIRATION
```
604800000
```
**Nota**: Este valor está en milisegundos (604800000 ms = 7 días)

---

## Resumen Rápido para Copiar

Aquí están todos los valores en formato lista para copiar fácilmente:

```
PORT=8081
DB_USERNAME=DEVELOPER_ANGI
DB_PASSWORD=ResLosPinos_Angi_01
DATABASE_URL=jdbc:oracle:thin:@restaurantelospinos_low?TNS_ADMIN=/app/wallet
JWT_SECRET=ZXN0YSBlcyBtaWMgY2xhdmUgc2VjcmV0YSBubyBsYSBwdWVkZXMgaGFrZWFy
JWT_SECRET_INCRIPT=Y29uIGVzdGFzIHBhbGFicmFz
JWT_EXPIRATION=604800000
```

---

## Cómo Agregar en Render Dashboard

1. Ve a tu servicio en [Render Dashboard](https://dashboard.render.com)
2. Haz clic en **"Environment"** en el menú lateral
3. Haz clic en **"Add Environment Variable"**
4. Agrega cada variable una por una:
   - **Key**: `PORT` → **Value**: `8081`
   - **Key**: `DB_USERNAME` → **Value**: `DEVELOPER_ANGI`
   - **Key**: `DB_PASSWORD` → **Value**: `ResLosPinos_Angi_01`
   - **Key**: `DATABASE_URL` → **Value**: `jdbc:oracle:thin:@restaurantelospinos_low?TNS_ADMIN=/app/wallet`
   - **Key**: `JWT_SECRET` → **Value**: `ZXN0YSBlcyBtaWMgY2xhdmUgc2VjcmV0YSBubyBsYSBwdWVkZXMgaGFrZWFy`
   - **Key**: `JWT_SECRET_INCRIPT` → **Value**: `Y29uIGVzdGFzIHBhbGFicmFz`
   - **Key**: `JWT_EXPIRATION` → **Value**: `604800000`
5. Guarda los cambios
6. Render reiniciará automáticamente el servicio con las nuevas variables

---

## ⚠️ Importante

- **Nunca compartas estas credenciales públicamente**
- Si cambias alguna contraseña en Oracle, actualiza `DB_PASSWORD` en Render
- El wallet de Oracle está incluido en la imagen Docker, no necesitas subirlo por separado
- Si tienes problemas de conexión, verifica que el nombre del servicio TNS (`restaurantelospinos_low`) coincida con el de tu base de datos

---

## Verificación

Después de configurar las variables, verifica que la aplicación esté funcionando:

1. Visita: `https://tu-app.onrender.com/v1/api/hola`
2. Deberías ver: `"Hola Mundo"`
3. Si ves un error, revisa los logs en Render Dashboard → **"Logs"**

