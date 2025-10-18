# 🚀 Writook Deployment Guide - Render

Esta guía te llevará paso a paso para deployar Writook en producción usando **Render** (backend + database) y **Vercel** (frontend).

## 📋 Pre-requisitos

- [ ] Cuenta en GitHub (ya tienes ✅)
- [ ] Cuenta en Render (gratis)
- [ ] Cuenta en Vercel

## 🎯 Plan de Deploy - Arquitectura Elegida

### Render: Backend + Database 🎯
### Vercel: Frontend ⚡

**¿Por qué esta combinación?**
- **Vercel:** Especializado en frontend, deploy automático, CDN global
- **Render:** Excelente plan gratuito, PostgreSQL incluido, fácil configuración

---

## 🗄️ Paso 1: Render - Backend + Database

### 1.1 Crear cuenta en Render

1. **Ir a [render.com](https://render.com)**
   - Sign up with GitHub
   - Conectar tu repositorio `danielvflores/Writook`

### 1.2 Crear PostgreSQL Database

1. **En Render Dashboard:**
   ```
   New → PostgreSQL
   Name: writook-database
   Database: writook
   User: writook_user
   Region: Oregon (US West) - gratis
   Plan: Free
   ```

2. **Render automáticamente genera:**
   - Internal Database URL (para el backend)
   - External Database URL (para conexiones externas)
   - Credenciales automáticas

### 1.3 Crear Web Service (Backend)

1. **En Render Dashboard:**
   ```
   New → Web Service
   Repository: danielvflores/Writook
   Name: writook-backend
   Region: Oregon (US West)
   Branch: main (o tu branch principal)
   Root Directory: apps/backend
   ```

2. **Build & Deploy Settings:**
   ```
   Build Command: ./mvnw clean package -DskipTests
   Start Command: java -jar target/*.jar
   ```

### 1.4 Variables de Entorno en Render

En el Web Service, agregar estas variables de entorno:

```bash
# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# JWT Secret (genera uno nuevo)
JWT_SECRET=tu_super_secreto_jwt_de_256_bits_minimo_aqui

# CORS (actualizarás después con la URL de Vercel)
CORS_ALLOWED_ORIGINS=https://writook.vercel.app

# Database (Render lo genera automáticamente)
DATABASE_URL=[Se copia de la database creada anteriormente]
```

**📝 Importante:** Para la `DATABASE_URL`, usa la **Internal Database URL** que Render genera automáticamente.

---

## 🌐 Paso 2: Vercel - Frontend

### 2.1 Crear proyecto en Vercel

1. **Ir a [vercel.com](https://vercel.com)**
   - Login con GitHub
   - New Project → Import Git Repository
   - Seleccionar `danielvflores/Writook`

### 2.2 Configurar build

```
Framework Preset: Vite
Root Directory: apps/frontend
Build Command: npm run build
Output Directory: dist
Install Command: npm install
```

### 2.3 Variable de entorno

En Vercel, agregar:

```bash
VITE_API_BASE_URL=https://writook-backend.onrender.com/api/v1
```

*(Reemplaza `writook-backend` con el nombre exacto que le diste a tu servicio en Render)*

---

## 🔧 Paso 3: Configuración Final

### 3.1 Actualizar CORS
Una vez tengas la URL de Vercel, actualizar en Render:
```bash
CORS_ALLOWED_ORIGINS=https://tu-app.vercel.app
```

### 3.2 Verificar URLs
- **Backend Render:** `https://writook-backend.onrender.com`
- **Frontend Vercel:** `https://writook.vercel.app`
- **Database:** Solo accesible internamente desde Render

### 3.3 Probar el deploy
1. ✅ Registrar usuario
2. ✅ Crear historia  
3. ✅ Crear capítulo
4. ✅ Comentar y calificar

---

## 🆚 Diferencias clave con Railway

| Aspecto | Railway | Render |
|---------|---------|--------|
| Plan gratuito | 5$ crédito mensual | 750h/mes gratis |
| Database | Incluida | PostgreSQL gratis incluido |
| Sleep policy | Sí (después de inactividad) | Sí (después de 15 min) |
| Custom domains | ✅ | ✅ |
| Build time | Rápido | Medio |

---

## 🐛 Troubleshooting

### Render Backend
- **Build fails:** Verificar que Java 21 esté configurado
- **No inicia:** Verificar variables de entorno y logs en Render dashboard
- **DB connection:** Usar Internal Database URL, no External
- **First deploy:** Puede tardar 10-15 minutos la primera vez

### Render Database
- **Connection timeout:** Verificar que usas Internal URL
- **Migrations:** Flyway se ejecuta automáticamente al iniciar

### Vercel Frontend  
- **Build fails:** Verificar dependencias en `package.json`
- **API calls fail:** Verificar `VITE_API_BASE_URL` y CORS
- **404 en rutas:** Vercel maneja SPA automáticamente

---

## 💡 Consejos para Render

1. **El primer deploy tarda más:** Render compila todo desde cero
2. **Sleep mode:** El servicio gratuito "duerme" después de 15 min de inactividad
3. **Logs en tiempo real:** Render dashboard → Service → Logs
4. **Scaling:** Puedes upgradear a plan pagado cuando necesites

---

## 🎉 ¡Listo para Deploy con Render!

### Orden recomendado:
1. 🗄️ Crear database en Render
2. 🚀 Crear web service (backend) en Render
3. ⚡ Deployar frontend en Vercel
4. 🔧 Ajustar CORS y variables finales

¿Empezamos con Render? Te guío paso a paso.