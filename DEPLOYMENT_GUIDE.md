# 🚀 Writook Deployment Guide

Esta guía te llevará paso a paso para deployar Writook en producción usando **Railway** (backend + database) y **Vercel** (frontend).

## 📋 Pre-requisitos

- [ ] Cuenta en GitHub (ya tienes ✅)
- [ ] Cuenta en Railway
- [ ] Cuenta en Vercel

## 🎯 Plan de Deploy - Arquitectura Elegida

### Railway: Backend + Database 🚂
### Vercel: Frontend ⚡

**¿Por qué esta combinación?**
- **Vercel:** Especializado en frontend, deploy automático, CDN global
- **Railway:** Perfecto para backend + DB, networking interno, fácil configuración

---

## 🗄️ Paso 1: Railway - Backend + Database

### 1.1 Crear proyecto en Railway

1. **Ir a [railway.app](https://railway.app)**
   - Login con GitHub
   - New Project → Deploy from GitHub repo
   - Seleccionar `danielvflores/Writook`

2. **Configurar el servicio backend:**
   ```
   Service Name: writook-backend
   Root Directory: apps/backend
   Build Command: ./mvnw clean package -DskipTests
   Start Command: java -jar target/*.jar
   ```

### 1.2 Agregar PostgreSQL

1. **En el mismo proyecto:**
   ```
   Add Service → Database → PostgreSQL
   ```

2. **Railway automáticamente genera:**
   - `DATABASE_URL` (variable de entorno)
   - Networking interno entre backend y DB

### 1.3 Variables de Entorno

En Railway, agregar estas variables:

```bash
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=super_secreto_jwt_de_256_bits_minimo
CORS_ALLOWED_ORIGINS=https://writook.vercel.app
```

*(Nota: La `DATABASE_URL` se genera automáticamente)*

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
```

### 2.3 Variable de entorno

En Vercel, agregar:

```bash
VITE_API_BASE_URL=https://tu-backend.railway.app/api/v1
```

*(Reemplaza con la URL real que te dé Railway)*

---

## 🔧 Paso 3: Configuración Final

### 3.1 Actualizar CORS
Una vez tengas la URL de Vercel, actualizar en Railway:
```bash
CORS_ALLOWED_ORIGINS=https://tu-app.vercel.app
```

### 3.2 Probar el deploy
1. ✅ Registrar usuario
2. ✅ Crear historia  
3. ✅ Crear capítulo
4. ✅ Comentar y calificar

---

## 🐛 Troubleshooting

### Railway Backend
- **No inicia:** Verificar variables de entorno y logs
- **DB connection:** La `DATABASE_URL` debe estar presente
- **Port binding:** Railway maneja el puerto automáticamente

### Vercel Frontend  
- **Build fails:** Verificar dependencias en `package.json`
- **API calls fail:** Verificar `VITE_API_BASE_URL` y CORS
- **404 en rutas:** Vercel maneja SPA automáticamente

---

## 🎉 ¡Listo para Deploy!

¿Empezamos con Railway? Te guío paso a paso.