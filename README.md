# MajoKids Backend (majo-kids-auth)

API de autenticación y gestión de la tienda MajoKids. Spring Boot 3.2 / Java 17 / MariaDB.

> Nota: el paquete Java todavía se llama `com.chancla.chancla_lite_auth` porque el proyecto se creó a partir de Chancla Lite. No es código de otro proyecto.

## Ramas de Git

| Rama | Uso |
|---|---|
| `develop` | Rama de **desarrollo**. Aquí se trabaja y se prueban los cambios. |
| `main` | Rama de **producción**. Render redespliega automáticamente con cada push a `main`. |

Flujo habitual: trabajar en `develop` → probar → hacer merge (o PR) a `main` → push → Render redespliega.

```
develop  ──▶  main  ──▶  Render (producción)
```

## Ejecutar localmente

```bash
mvnw spring-boot:run
```

Arranca en `http://localhost:8080` con el perfil `dev` activo.

## URLs de producción

- Backend (Render): https://majo-kids-auth.onrender.com
- Frontend (Render): https://majo-kids-front.onrender.com

## Variables de entorno (Render)

- `DB_URL` / `DB_USERNAME` / `DB_PASSWORD` — base de datos Filess.io
- `JWT_SECRET` — nunca se commitea al repo
- `FRONTEND_URL` — para CORS
- `CLOUDINARY_CLOUD_NAME` / `CLOUDINARY_API_KEY` / `CLOUDINARY_API_SECRET`
