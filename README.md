# Desglose Balance Web (BICE) — plataforma de desglose financiero

> **Proyecto de ejemplo / portafolio.**  
> Este repositorio existe para **visualizar y revisar el código** de una aplicación corporativa de desglose de balances y proyecciones financieras. No es un entorno listo para producción ni un dump redeployable del sistema original: es una muestra técnica del trabajo realizado (JHipster + Angular + Spring Boot + JasperReports + Oracle).

---

## Contexto

Aplicación web monolítica generada con **JHipster 5.4.2** para el flujo de **desglose de balance** (analistas, ejecutores, revisores y jefes). Permite:

- Cargar y editar desgloses por cliente / tipo / subtipo / moneda / plantilla
- Trabajar fórmulas y valores en grillas tipo Excel (**Handsontable**)
- Enviar a revisión, observar, corregir y dejar definitivo
- Consultar históricos y generar **PDFs** (ingreso, resumen de balance, proyecciones)
- Administrar firmas, grupos, reemplazos, cargos y parámetros

El núcleo de negocio vive en Oracle (esquemas tipo `CIF_ADMIN`, `BCT_ADMIN`, integración con operadores **Turbine/SF**). La app se empaqueta como **WAR**.

---

## Qué puedes ver en este código

| Módulo (ruta) | Qué hace |
|---------------|----------|
| **Desglose** `/breakdown` | Alta/edición del desglose: cliente, plantilla, períodos, conceptos, fórmulas, cuadres |
| **Consulta** `/query` | Búsqueda y lectura de desgloses existentes |
| **Revisión** `/revision` | Flujo de revisión / observación / aprobación |
| **Proyecciones** `/projections` | Proyecciones históricas y reportes asociados |
| **Mantenimiento** | Reemplazos, cargos, grupos, revisores, clientes sin RUT, catálogos (`descripcion-codigo`), conceptos, etc. |
| **Admin JHipster** `/admin` | Usuarios, métricas y utilidades estándar |
| **Reportes** `/api/report/...` | Generación de PDF vía JasperReports |

### Roles (JWT / authorities)

| Rol | Uso típico |
|-----|------------|
| `ROLE_PLAT_DESGLOSE` | Acceso base a la plataforma |
| `ROLE_PLAT_DESGLOSE_EJECUTOR` | Digita / edita desgloses (asignado a grupos) |
| `ROLE_PLAT_DESGLOSE_REVISOR` | Revisa, observa, firma |
| `ROLE_PLAT_DESGLOSE_JEFE` | Supervisión + mantenimiento |
| `ROLE_PLAT_DESGLOSE_ADMINISTRADOR` | Administración completa |

### Estados del flujo de desglose

Definidos en `Constants.java`:

```text
ED   → En edición
BEP  → Borrador en proceso
BV   → Borrador validado
BT   → Borrador terminado
OBS  → Observación
REV  → En revisión
DEF  → Definitivo
DEFM → Corregido (definitivo modificado)
```

Flujo conceptual:

```text
Login (JWT)
  → Desglose (Handsontable + fórmulas de conceptos)
       → validar / grabar (ED → BEP → BV → BT)
       → enviar a revisión (REV)
            → observación (OBS) → corrección
            → definitivo (DEF / DEFM)
  → Consulta / Proyecciones
  → PDF Jasper (ingreso | resumen balance | proyección | log)
```

---

## Stack

| Área | Tecnología |
|------|------------|
| Generador | JHipster 5.4.2 (monolito) |
| Frontend | Angular 6.1, Bootstrap 4, ng-bootstrap, ngx-datatable, Handsontable, SweetAlert2, i18n (es/en) |
| Backend | Spring Boot 2.0.5, Java 8, Maven WAR |
| Auth | JWT (JHipster) |
| Cache | Ehcache |
| Persistencia | Oracle (Hibernate 5.2, dialect Oracle10g) · Liquibase deshabilitado en perfiles típicos |
| Reportes | JasperReports 6.7 (plantillas `.jasper` en la raíz del proyecto) |
| Integraciones | Operadores SF / Turbine, catálogos BCT, datos CIF |
| Empaquetado | `desglose` WAR · Docker / scripts bat de build |

**Puerto local típico:** `8080`  
**Paquete base:** `cl.tutorial.desglose`

---

## Estructura (resumen)

```text
bice-desglose-web/
├── src/main/java/cl/tutorial/desglose/
│   ├── config/          # Spring, seguridad JWT, datasources
│   ├── domain/          # Entidades JPA (DESGLO, PERIOD, CONCEP, …)
│   ├── repository/      # Spring Data
│   ├── service/         # Negocio (Desglose, Concepto, Report, Projection…)
│   ├── web/rest/        # API REST (+ ReportResource)
│   └── security/        # JWT / authorities
├── src/main/webapp/app/
│   ├── breakdown/       # UI principal del desglose
│   ├── query/           # Consulta
│   ├── revision/        # Revisión
│   ├── projections/     # Proyecciones
│   ├── maintenance/     # Mantención operativa
│   ├── entities/        # CRUDs JHipster de dominio
│   ├── account/ admin/  # Cuenta y admin
│   └── layouts/         # Navbar por rol
├── src/main/resources/config/
│   └── application-*.yml  # Perfiles (dev/prod/bice/tutorial…) — credenciales sanitizadas
├── sql/                 # Scripts DDL/DML de evolución Oracle (referencia)
├── jdl_model/           # Modelos JDL del dominio
├── *.jasper             # Plantillas compiladas de reportes
├── pom.xml
├── package.json
└── README.md
```

Reportes PDF expuestos en `ReportResource` (`/api/report`):

- `POST .../download/informe-ingreso`
- `POST .../download/informe-resumen-desglose-balance`
- `POST .../download/informe-proyeccion`
- `GET  .../download/informe-desglose/{cliente}/{tipo}/{subtipo}/{moneda}/{plantilla}/...`
- Logs de desglose por rango de fechas

---

## Datasources / perfiles

La app espera Oracle. Los YAML de ejemplo usan placeholders:

| Placeholder | Significado |
|-------------|-------------|
| `YOUR_DB_USER` / `YOUR_DB_PASSWORD` | Schema principal del desglose |
| `YOUR_SF_USER` / `YOUR_SF_PASSWORD` | Operadores / Turbine (SF) |
| `YOUR_BCT_USER` / `YOUR_BCT_PASSWORD` | Catálogos BCT |
| `YOUR_CIF_USER` / `YOUR_CIF_PASSWORD` | Datos CIF |
| `YOUR_BASE64_JWT_SECRET` | Secreto JWT (Base64) |

Perfiles de referencia en `src/main/resources/config/`:

- `application-dev.yml` / `application-prod.yml`
- `application-bice-local.yml` / `application-bice-spring.yml` / `application-bice-wls.yml`
- `application-tutorial-*.yml`

Los nombres de esquema en comentarios/JDL/SQL (`CIF_ADMIN`, `BCT_ADMIN`, etc.) son parte del **modelo de datos** documentado; no son credenciales.

---

## Qué se omitió / sanitizó a propósito

Para publicar un repo **público** y manejable:

- `node_modules/`, `node/`, `target/`, `dist/`
- Credenciales reales (JDBC, JWT, keystore) → **placeholders**
- Hosts internos de BD → `127.0.0.1:1521`
- Artefactos IDE pesados / outputs de build

**No incluido por licencia / entorno:** driver Oracle JDBC (`ojdbc`) según configuración Maven Oracle, bases institucionales, ni usuarios Turbine reales.

---

## Cómo se construía (referencia)

Entorno original a grandes rasgos:

1. **JDK 8** + Maven 3 + Node 8 / npm 6 (versiones del `pom.xml`)
2. Oracle accesible con los schemas de desglose / SF / BCT / CIF
3. Completar placeholders en `application-*.yml` (y JWT)
4. Frontend: `npm install` y build Angular vía pipeline JHipster / `frontend-maven-plugin`
5. Backend: `./mvnw -Pprod clean package` → WAR desplegable (Tomcat / WebLogic / embebido Spring Boot según perfil)

Sin Oracle y sin los schemas/partners, la UI arranca en modo limitado: las pantallas de desglose/consulta/revisión dependen de esas conexiones. Eso es esperado en este repositorio de ejemplo.

Comandos orientativos:

```bash
# dependencias front (histórico Node 8)
npm install

# empaquetado WAR (perfil prod JHipster)
./mvnw -Pprod clean verify
```

---

## Objetivo de este repositorio

Documentar y mostrar:

- Arquitectura **JHipster monolito** Angular + Spring Boot
- Flujo de negocio de **desglose → revisión → definitivo**
- Uso de **Handsontable** + motor de **fórmulas de conceptos**
- Generación de **PDF con JasperReports 6.7**
- Integración multi-esquema Oracle y roles de plataforma

Sirve como pieza de portafolio para lectura de código y arquitectura, no como producto oficial redeployable.
