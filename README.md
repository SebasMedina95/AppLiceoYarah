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

---------------------------------------------------------------------------------------
### Construcción de imágenes de Docker
Antes de comenzar, debemos tener a consideración que con Docker debemos manejar el
tema del localhost diferente. Recordemos que ahora, trabajando entre los contenedores de 
Docker ya no usamos directamente al localhost, sino que debemos conectarnos al local de 
los contenedores, por eso, en el código tenemos que configurar las URLs en vez de localhost 
con ``host.docker.internal``.

Ahora, para continuar con el proceso de dockerización de las aplicaciones debemos tener 
los JAR generados. En este caso, para poder generarlos sin complicaciones en vista de por 
local nos tira problemas al hacer clean-compile-install por las imagenes/contenedores de 
Docker, nos saltamos la parte de las pruebas y solo empaquetamos el JAR, para ello, 
ubicados en el proyecto donde veamos el ``mvnw`` usamos el comando:
````dockerfile
.\mvnw clean package -DskipTests
````
Este comando lo que hará es empaquetar el JAR y tenerlo listo para usar.
Ahora, nos ubicamos en la proyecto, donde podemos ver que en la raíz se encuentra el Dockerfile,
en este punto debemos ejecutar el comando:
````dockerfile
docker build -t liceoyarah-ms-persons:latest .
````
Lo que queremos decir es que construya una imagen a partir del Dockerfile, el . nos indica
que estamos en la raíz del proyecto en ubicación. El -t más el liceoyarah-ms-persons:latest 
nos indica el nombre que le daremos a la imagen, así como también el latest es para referir 
la última versión de la imagen generada.
Ahora, debemos correr la imagen usando el comando:
````dockerfile
docker run -p 18881:18881 --name liceoyarah-ms-persons-container 04381f1995bf
````
Ten en cuenta que ``04381f1995bf`` es el ID de la imagen que se nos creo anteriormente, este
valor podría variar por supuesto entre máquinas. Debemos definir el puerto con ``-p`` para
poder comunicarnos desde el exterior y con ``--name`` le damos un nombre para que Docker no lo
asigne de manera aleatoria, quedando más personalizada.
---------------------------------------------------------------------------------------

### Notas de actualización
* **Última actualización:** Agosto 23/2024.
* **Desarrollador:** Juan Sebastian Medina Toro.
* **Contexto trabajo:** Elaboración Micro Servicios.

