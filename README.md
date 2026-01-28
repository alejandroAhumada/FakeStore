# FakeStore Android App

## Descripción general
Este proyecto es una aplicación Android que consume la API pública FakeStore y muestra un listado de productos con su respectivo detalle.  
El objetivo principal es demostrar buenas prácticas de desarrollo Android, una arquitectura clara y mantenible, manejo de estado reactivo y pruebas unitarias.

---

## Funcionalidades
- Listado de productos
- Pull to refresh
- Pantalla de detalle de producto
- Marcado de productos como favoritos
- Persistencia local para funcionamiento offline
- Manejo de estados de carga y error
- Tests unitarios para la lógica de negocio

---

## Tecnologías utilizadas
- Kotlin
- Jetpack Compose (Material 2)
- Arquitectura MVVM
- Coroutines y Flow
- Retrofit + Moshi
- Room Database
- Hilt para inyección de dependencias
- JUnit, Coroutines Test y Turbine para pruebas unitarias

---

## Arquitectura
El proyecto está estructurado siguiendo el patrón MVVM con una clara separación de responsabilidades:

- **Capa UI**  
  Contiene las pantallas en Compose y los ViewModels. La UI es reactiva y se renderiza en función del estado expuesto por los ViewModels.

- **Capa Domain**  
  Define los modelos de dominio, los casos de uso y las interfaces de los repositorios. No depende de frameworks Android.

- **Capa Data**  
  Incluye el consumo de la API remota, la base de datos local con Room, los mappers y la implementación concreta del repositorio.

El repositorio actúa como *single source of truth*, exponiendo los datos mediante `Flow` y permitiendo un enfoque *offline-first*.

---

## Manejo de estado
Los ViewModels exponen un `StateFlow` que representa el estado de la pantalla (loading, error y success).  
La UI observa este estado y se actualiza automáticamente ante cualquier cambio, sin lógica de negocio en los composables.

---

## Persistencia y cache
Los datos obtenidos desde la API se almacenan localmente en Room.  
Si la red no está disponible, la aplicación puede mostrar la información previamente cacheada.

---

## Tests
Se incluyen tests unitarios para:
- Repositorio (flujo de datos y cache)
- ViewModels (manejo de estado y eventos)
- Mappers (transformación entre DTO, Entity y Domain)

Los tests se ejecutan en JVM utilizando fakes en lugar de dependencias reales, lo que permite validarlos de forma rápida y determinística.

---

## Ejecución del proyecto
1. Clonar el repositorio.
2. Abrir el proyecto en Android Studio.
3. Sincronizar dependencias con Gradle.
4. Ejecutar la app en un emulador o dispositivo físico.

Para ejecutar los tests unitarios:
```bash
./gradlew :app:testDebugUnitTest
