# PanalSuite Mobile

MVP académico de PanalSuite para Android (DSY1105 · Desarrollo de Aplicaciones Móviles, Duoc UC).
Desarrollado en Kotlin con Jetpack Compose y arquitectura MVVM.

## Avance 1: Inicio de sesión (13-10-2026)

### Qué se hizo hoy
- **Modelo** (`model/Usuario.kt`): clase `Usuario` y enum `Rol` (Solicitante, Resolutor, Supervisor, Gerencia, Administrador).
- **Repositorio** (`repository/AuthRepository.kt`): validación de credenciales con usuarios ficticios. Está pensado para reemplazarlo por la API REST cuando exista.
- **ViewModel** (`viewmodel/LoginViewModel.kt`): estado de la pantalla (correo, contraseña, carga, error) y lógica del login.
- **Pantalla** (`ui/LoginScreen.kt`): formulario de login con Jetpack Compose.
- **MainActivity**: muestra el login y, si es exitoso, un mensaje con el nombre y el rol del usuario.

### Usuarios de prueba (datos ficticios)
Contraseña para todos: `1234`

| Correo | Rol |
|---|---|
| solicitante@panalbit.cl | Solicitante |
| resolutor@panalbit.cl | Resolutor |
| supervisor@panalbit.cl | Supervisor |
| admin@panalbit.cl | Administrador |

### Estado actual
El login está casi terminado, pero **el proyecto aún no inicia de forma completamente correcta**:
- La app a veces abre directo en la pantalla de bienvenida, sin pasar por el login.
- Hubo errores de compilación por `viewBinding` y un XML sobrante de un proyecto anterior (`activity_main.xml`).
- El emulador (API 37) se desconecta al instalar la app (`device offline`).

### Próximos pasos
1. Corregir los errores anteriores y verificar el flujo completo del login.
2. Mejorar el diseño visual del login (colores, estilos).
3. Continuar con las demás pantallas (bandejas según rol), navegación y persistencia de sesión.