## APLICACIÓN ACADÉMICA ##
Esta aplicación es el desarrollo de un sistema de administración de 
estudiates para el Liceo Yarah de una reconocida municipalidad, lo cual, 
el objetivo será construir esta aplicación BACKEND en una arquitectura 
de micro servicios (aplicación API RESTFul), generando una aplicación 
más mantenible; hasta el momento no se ha definido de cuantos micro 
servicios tendrémos, pero se lista al final de este enunciado los MS 
tentativos para la aplicación. 

Efectivamente, la aplicación está desarrollada en Spring Boot versión 
3.3.2 y estamos manejando diferentes bases de datos Postgres para cada 
MS (cada MS tendrá su BD independiente). Para la comunicación entre los 
micros estaremos usando Feign así como ORM base será JPA e Hibernate para 
el tema de las consultas SQL, las entidades estarán manejadas por Lombok 
y tendremos adecuado en cada uno de los controladores la respectiva 
documentación Swagger.

Las diferentes bases de datos están manejadas con bases de datos locales,
sin embargo, como una mejora en la versión se propondrá trabajar también
con imágenes de docker para el manejo de la base de datos y uso de semillas.

* NOTA:
Se está usando un monorepo solamente para poder manejar mejor el tema
de legibilidad, pero no hay un caso técnico asociado, también, pensando en
las personas que verán esta parte, para que no sea caótico navegar entre
los diferentes proyectos.

-------------------------------------------------------------------------------

## MÓDULOS (MICROS) TENTATIVOS: 
    * Personas
    * Usuarios
    * Estudiantes
    * Empleados
    * Planillas Históricas
    * Eventos
    * Alimentación
    * Activos Fíjos

---------------------------------------------------------------------------------------
# Desarrollado por: #
Desarrollador de Backend: [Juan Sebastian Medina Toro](https://www.linkedin.com/in/juan-sebastian-medina-toro-887491249/).

---------------------------------------------------------------------------------------

### Levantamiento de la aplicación:
Descargada la aplicación debe descargar las dependencias requeridas.
* Ejecute la estructura maven:
````dockerfile
clean
compile
install
````
Asegurese que las dependencias están instaladas en el POM

---------------------------------------------------------------------------------------

### Documentación Swagger
* Microservicio de personas (MS Persons) -> **ms-persons**.
Ingrese a la siguiente URL:
````dockerfile
http://localhost:18881/business/swagger-ui/index.html
````

* Microservicio de usuarios (MS Users) -> **ms-users**.
  Ingrese a la siguiente URL:
````dockerfile
http://localhost:18882/business/swagger-ui/index.html
````

* Microservicio de estudiantes (MS Students) -> **ms-students**.
  Ingrese a la siguiente URL:
````dockerfile
http://localhost:18883/business/swagger-ui/index.html
````

**Nota:** Tener en cuenta que el puerto puede variar según las configuraciones de las 
properties, lo mismo el index.html según las configuraciones aplicadas. Tenga en cuenta
esto a la hora de la aplicación de Kubernets, Docker y MiniKube.

