This document is a personal guide to keep track of important notes during the development of this fork.
It is not intended for public consumption, but rather as a private reference to record key decisions, challenges, and solutions encountered throughout the development process.

===== CONTENTS =======
1. Development Notes
   - Current understanding of the code.
   - Areas for improvement in the code.
   - Maintainability, scalability, and comprehension of the code.
   - Key dependencies and libraries used.
   - Design patterns and architectural decisions.
   
2. Bugs and Issues
   - Known bugs and unexpected behaviors.
   - Steps to reproduce and potential fixes.
   - Workarounds for known issues.

3. Ideas and Enhancements
   - Proposed improvements or new features.
   - Experiments and tests to explore.

-----------
#  1. Development Notes (ENGLISH)

- Backend Analysis:

    - Topic: Spring, Security and CORS:
        1. As seen in the code, specifically in `SecurityConfig.java`, some routes are allowed without authentication and certain CORS origins are permitted.
            > Review each route to confirm it is necessary and secure. Consider splitting routes (for example, `/api/public` and `/api/private`).
        2. Line 26 in `SecurityConfig.java` contains: `@Value("${cors.allowed-origins:https://writook.danielvflores.xyz}")` — a default CORS origin is set.
            > Decide whether to keep this default or move allowed origins to environment-specific configuration.
        3. Security requires a deeper review due to limited experience. Inspect routes, controllers and the JWT authentication flow.

    - Backend behavior:
        - Controller:
            1. Most controllers use a `@RequestMapping` prefix like `/api/v1/`. Consider whether to keep it or introduce public/private grouping.
                > A `BaseController` could centralize common mappings if appropriate.
            2. Check the use of `@Autowired` (field injection). Prefer constructor injection for better testability and clearer dependencies.
            3. Controllers are generally OK but some contain quick fixes; review how DTOs, services and JWT are used.
        - DTO:
            1. Some DTOs are poorly structured (use `Object` wrappers, mutable). Prefer generic, immutable DTOs and avoid exposing JPA entities directly.
                > Study when a DTO is needed and implement mappers for entity-to-DTO conversions.
        - Entity:
            1. Study what an entity is, how it maps to the database, and how to map correctly with JPA/Hibernate.
        - Model:
            1. Models are mostly well-structured and tend toward immutability; review mutable fields and validate design choices.
        - Repository:
            1. Review the repository pattern and how repositories relate to entities and the database (Spring Data JPA usage).
        - Security:
            1. Security works at a basic level; verify how JWT is handled with the frontend (token storage, expiration, claims).
        - Service:
            1. Services are functional but may contain duplicated or poorly structured business logic — consider refactoring.
            2. Review exception handling and consider adding a global exception handler.
            3. Check transaction boundaries where services interact with the database and ensure critical operations are transactional.

    - Database


-----------
#  1. Development Notes (ESPAÑOL)

- Backend Analysis:

    - Tema Spring, Security y CORS:
        1. Según se ve en el código, específicamente en `SecurityConfig.java` doy permisos para el acceso desde ciertos orígenes / CORS y permito algunas rutas sin autenticación.
            > Debo revisar bien cada ruta para confirmar que es necesaria y segura. También considerar separar rutas (por ejemplo, `/api/public` y `/api/private`).
        2. Línea 26 en `SecurityConfig.java` contiene: `@Value("${cors.allowed-origins:https://writook.danielvflores.xyz}")` — aquí se permite CORS desde un dominio específico.
            > Decidir si mantener ese valor por defecto o pasarlo a variable de entorno para cada entorno (local, staging, producción).
        3. En general, la seguridad necesita una revisión más profunda ya que tengo experiencia limitada. Revisar rutas, controladores y el flujo de JWT.

    - Comportamiento del Backend:
        - Controller:
            1. La mayoría de los controladores usan un prefijo `@RequestMapping` como `/api/v1/`. Revisar si mantenerlo o introducir una agrupación public/private.
                > Considerar una `BaseController` para evitar repetir el prefijo si encaja en el diseño.
            2. Revisar el uso de `@Autowired` (inyección en campos) y plantear cambiar a constructor injection para mejorar testabilidad y claridad.
            3. Los controladores tienen una estructura aceptable, pero parte de la lógica está hecha rápido; revisar el uso de DTOs, services y JWT.
        - DTO:
            1. Algunos DTOs están mal estructurados: usan wrappers `Object` y son mutables. Preferir DTOs genéricos e inmutables y evitar exponer entidades JPA directamente.
                > Estudiar cuándo se necesita un DTO e implementar mappers en lugar de pasar entidades a la capa de presentación.
        - Entity:
            1. ¿Qué es una entity? ¿Cómo se mapea a la base de datos? Estudiar JPA/Hibernate y el mapeo ORM correcto.
        - Model:
            1. Los modelos están bien estructurados y tienden a ser inmutables; revisar campos mutables y validar si la elección de diseño es adecuada.
        - Repository:
            1. Entender el patrón repository y cómo se relacionan con entities y BD (uso de Spring Data JPA).
        - Security:
            1. La seguridad funciona a nivel básico; verificar cómo el JWT interactúa con el frontend (manejo del token, expiración, claims).
        - Service:
            1. Los servicios funcionan pero pueden tener lógica duplicada o mal estructurada; considerar refactorizar.
            2. Revisar el manejo de excepciones en servicios e implementar un manejador global si hace falta.
            3. Comprobar los límites de transacción en servicios que interactúan con la BD; asegurar que operaciones críticas sean transaccionales.

    - Database