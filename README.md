# ✈️ DeustoTrips

> Gestión profesional de reservas de alojamientos y viajes.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing-orange?style=for-the-badge)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Status](https://img.shields.io/badge/Estado-Finalizado-success?style=for-the-badge)

## 📖 Descripción

**DeustoTrips** es una aplicación de escritorio diseñada para facilitar la gestión y reserva de alojamientos y viajes. Desarrollada en **Java** utilizando **Swing** para una interfaz gráfica intuitiva, el sistema ha evolucionado hacia una **arquitectura Cliente-Servidor**.

La aplicación se conecta a un servidor remoto **MariaDB** optimizado, permitiendo la gestión centralizada de datos, concurrencia de usuarios y manejo eficiente de recursos multimedia (imágenes) en tiempo real.

Este proyecto fue desarrollado como parte de la asignatura de Programación III por el **Equipo 11**.

---

## ✨ Características Principales

* **Arquitectura Cliente-Servidor:** Conexión remota optimizada con MariaDB, sustituyendo la antigua persistencia local.
* **Búsqueda Avanzada:** Filtrado inteligente de Viajes y Alojamientos por múltiples criterios (precio, ubicación, disponibilidad, etc.).
* **Optimización de Rendimiento:** Índices SQL y gestión de memoria ajustados para una navegación fluida incluso con grandes volúmenes de datos.
* **Interfaz Gráfica:** UI completa en Java Swing con carga dinámica de recursos visuales.
* **Gestión de Usuarios:** Sistema preparado para la concurrencia de múltiples clientes.

---

## 🚀 Instalación y Ejecución

Para utilizar la aplicación, no necesitas configurar el entorno de desarrollo.

### 📥 Descarga y Ejecución
1.  Ve a la sección de **Releases** (a la derecha) o usa el enlace de abajo.
2.  Descarga el archivo `.zip` de la última versión.
3.  **Importante:** Descomprime el archivo completamente (necesitas la carpeta `resources` junto al ejecutable).
4.  Sigue los pasos del `README.txt` que encontrarás ahí dentro.

*(Nota: No ejecutar el .jar directamente, usar el script `.bat` para asegurar la carga correcta de recursos y memoria).*

[👉 **DESCARGAR ÚLTIMA VERSIÓN (GITHUB RELEASES)**](https://github.com/aritzgsc/DeustoTrips-Equipo-11/releases/latest)

---

## 📂 Documentación del Proyecto

El desarrollo de DeustoTrips ha seguido una metodología estructurada en varias fases. A continuación se adjunta la documentación oficial del Equipo 11:

| Fase | Documento | Descripción |
| :--- | :--- | :--- |
| **1. Ideación** | [📄 Idea de proyecto](https://github.com/user-attachments/files/24475333/Prog.3.-.Idea.de.proyecto.-.Equipo.11.pdf) | Concepto inicial y alcance. |
| **2. Planificación** | [📊 Planificación (Excel)](https://github.com/user-attachments/files/24475441/Prog.3.-.Planificacion.-.Equipo.11.xlsx) | Dedicación y gestión de tareas. |
| **3. Base de Datos** | [🗄️ Estructura BD](https://github.com/user-attachments/files/24475349/Prog.3.-.Base.de.Datos.-.Equipo.11.pdf) | Modelo E-R y diseño relacional. |
| **4. Desarrollo** | [📝 Informe Final](https://github.com/user-attachments/files/24475339/Prog.3.-.Informe.de.desarrollo.-.Equipo.11.pdf) | Cambios, mejoras y commits. |

---

## 🔮 Estado del Proyecto y Mejoras

El proyecto ha alcanzado su versión 1.0 estable.

* [x] **Migración a Servidor:** Implementación exitosa de base de datos remota MariaDB.
* [x] **Optimización de Consultas:** Reducción de carga de CPU mediante índices y pool de conexiones.
* [ ] **Sistema de Pagos Real:** Integración con pasarela de pagos (actualmente simulado).
* [ ] **Versión Web/Móvil:** Futura expansión a otras plataformas.

---

## 👥 Autores - Equipo 11

* 👤 **Ander González García**
* 👤 **Iker González García**
* 👤 **Aritz González Santa Cruz**
* 👤 **Oier Unamunzaga Caujape**
