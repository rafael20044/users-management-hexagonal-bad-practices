# Informe de Refactorización y Solución de Violaciones a las Reglas de Código

Este documento detalla el progreso y la metodología utilizada para solucionar las violaciones a las buenas prácticas de desarrollo definidas en los documentos base del proyecto (`Reglas 1.md` y `Reglas 2.md`). 

El trabajo se ha organizado sistemáticamente mediante el uso de ramas de Git bajo la nomenclatura `fix/regla{N}/violacion{M}`, aislando cada problema y asegurando una refactorización progresiva y segura.

---

## Parte 1: Arquitectura Hexagonal y Clean Code (`Reglas 1.md`)

A continuación, se describen las soluciones aplicadas a las reglas principales de arquitectura y diseño:

### Regla 1: Arquitectura (Enfoque Hexagonal)
- **Ramas:** `fix/regla1/*`
- **Solución:** Se corrigió el flujo de dependencias para asegurar que la capa de **Dominio** no dependa de frameworks ni detalles de infraestructura. Se eliminaron acoplamientos indebidos en el punto de entrada (`Main`) y se configuró correctamente la inyección de dependencias hacia los puertos.

### Regla 2: Modelado y Tipos
- **Ramas:** `fix/regla2/*`
- **Solución:** Se refactorizaron las entidades y objetos de valor (Value Objects) para asegurar la protección de sus invariantes. Se pasó de utilizar tipos primitivos indiscriminados a tipos de dominio fuertemente tipados.

### Regla 3: Lombok y Validaciones
- **Ramas:** `fix/regla3/*`
- **Solución:** Se limitó el uso de Lombok a lo estrictamente necesario (ej. `@RequiredArgsConstructor` o `@Builder`). Se movieron las validaciones para que no dependan de `@Valid` en tipos simples dentro del dominio, garantizando que el dominio valide sus propias reglas de negocio en los constructores.

### Regla 4: Estilo y Naming
- **Ramas:** `fix/regla4/*`
- **Solución:** Esta regla tuvo una alta incidencia. Se renombraron variables, métodos y clases para que fueran descriptivos y revelaran intención. Se reemplazó el uso de `==` para comparar objetos por `Objects.equals()`.

### Regla 5: Manejo de Strings
- **Ramas:** `fix/regla5/*`
- **Solución:** Se evitó el retorno de valores `null` o strings vacíos como indicadores de estado. En su lugar, se implementaron `Optional` y excepciones expresivas del dominio.

### Regla 6: Excepciones, Logging y Telemetría
- **Ramas:** `fix/regla6/*`
- **Solución:** Se limpió el dominio de trazas de logging y dependencias de monitoreo. Las excepciones ahora capturan el contexto adecuado y se lanzan hacia las capas externas, donde un manejador global (o los Adapters) se encarga de loguear la información sin exponer PII (Datos de Identificación Personal).

### Reglas 8, 9 y 10: Diseño, Resiliencia y Calidad de Código
- **Ramas:** `fix/regla8/*`, `fix/regla9/*`, `fix/regla10/*`
- **Solución:** Se eliminaron "Magic Numbers" y cadenas hardcodeadas, reemplazándolos por constantes con significado semántico. Se resolvieron advertencias de calidad de código (tipo SonarQube) especialmente en las clases de excepciones (`UserNotFoundException`, `EmailSenderException`, etc.).

### Regla 11: Mejores Prácticas en Pruebas (Tests)
- **Ramas:** `fix/regla11/*`
- **Solución:** Se mejoraron sustancialmente las clases de pruebas (ej. `UserNameTest`, `LoginServiceTest`, `EmailNotificationServiceTest`):
  - Inclusión de `@DisplayName` para todos los métodos y clases.
  - Organización bajo la estructura **Arrange-Act-Assert** (AAA).
  - Sustitución de `assertTrue` genéricos por aserciones específicas (`assertEquals`, `assertNotNull`, `assertSame`).
  - Documentación en JavaDoc para describir los escenarios de cada prueba.

---

## Parte 2: Diseño de Funciones y Mantenibilidad (`Reglas 2.md`)

Las reglas extendidas de Clean Code se atendieron en ramas subsecuentes, con especial atención a la cohesión y el acoplamiento:

### Regla 13: Clases Utilitarias Innecesarias
- **Ramas:** `fix/regla13/*`
- **Solución:** Se eliminaron clases genéricas tipo `Utils` y se reubicó la lógica estática dentro de los contextos de dominio a los que realmente pertenecían.

### Reglas 14 y 15: Ley de Deméter y Preferencia por Inmutabilidad
- **Ramas:** `fix/regla14/*`, `fix/regla15/*`
- **Solución:** Se rompieron encadenamientos profundos de llamadas a métodos (violaciones a Deméter) y se protegió el estado interno de los objetos promoviendo variables `final` y eliminando `setters` innecesarios.

### Reglas 19, 20 y 21: Acoplamiento Temporal y Tipos de Dominio
- **Ramas:** `fix/regla19/*`, `fix/regla20/*`, `fix/regla21/*`
- **Solución:** Se diseñaron APIs más seguras que no obligan al consumidor a invocar métodos en un orden mágico (Acoplamiento temporal). Se reemplazaron códigos de error ambiguos (como retornar `-1`) por excepciones descriptivas.

### Reglas 22, 23 y 24: Facilidad de Refactorización y Semántica
- **Ramas:** `fix/regla22/*`, `fix/regla23/*`, `fix/regla24/*`
- **Solución:** Se centralizaron reglas de negocio dispersas en múltiples clases. Se unificó el glosario del código para que un mismo concepto se denomine de la misma forma en toda la aplicación, mejorando la legibilidad.

---

## Conclusión

El trabajo realizado a través de estas ramas ha transformado el proyecto, llevándolo a un alto estándar de **Clean Code** y garantizando que la **Arquitectura Hexagonal** se respete. Cada corrección ha dejado el sistema más desacoplado, más fácil de probar y altamente mantenible para futuros requerimientos.
