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

``Es importante destacar también que el host y el puerto para trabajar
en local host **SIN DOCKER** ES localhost:5432, EN EL PROPERTIES HACEMOS
EL AJUSTE PARA TRABAJAR, LA IDEA ES GENERAR LAS IMÁGENES DE DOCKER Y AL
TENERLAS DESPLEGADAS SOLO TRABAJAR CON ELLAS Y EN LOCAL EL MS QUE SE ESTE
ELABORANDO EN EL MOMENTO DETERMINADO``\
**Nota adicional:** Tener en cuenta que el puerto puede variar según las configuraciones de las 
properties, lo mismo el index.html según las configuraciones aplicadas. Tenga en cuenta
esto a la hora de la aplicación de Kubernets, Docker y MiniKube.


---------------------------------------------------------------------------------------
### Construcción de imágenes de Docker
`NOTA IMPORTANTE, ANTES DE LEER ESTE APARTADO, VERIFIQUE PRIMERO EL APARTADO SIGUIENTE
DONDE HABLAMOS DE LAS OPTIMIZACIONES, PUESTO QUE, A MEDIDA QUE SE FUE AVANZANDO EN EL 
DESARROLLO, SE FUERON OPTIMIZANDO LAS IMAGENES ASÍ COMO LOS ARCHIVOS DE DOCKER Y POR TANTO, 
LO QUE TOMABA 2 O 3 PASOS O INCLUSO MÁS, SE HA REDUCIDO A MÁS POCOS PASOS Y COMANDOS MÁS 
PERSONALIZADOS PARA ESO.
ENTONCES, ANTES DE CONTINUAR EN ESTE PUNTO REVISE LAS OPTIMIZACIONES, EN PRELACIÓN A ELLO,
SE DEJA ESTE APARTADO COMO DOCUMENTACIÓN ÚTIL.`
---------------------------------------------------------------------------------------
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
en este punto debemos ejecutar el comando para construir la imagen:
````dockerfile
docker build -t liceoyarah-ms-persons:latest .
````
Lo que queremos decir es que construya una imagen a partir del Dockerfile, el . nos indica
que estamos en la raíz del proyecto en ubicación. El -t más el liceoyarah-ms-persons:latest 
nos indica el nombre que le daremos a la imagen, así como también el latest es para referir 
la última versión de la imagen generada.
Ahora, debemos construir el contenedor a partir de la imagen usando el comando:
````dockerfile
docker run -p 18881:18881 --name liceoyarah-ms-persons-container 04381f1995bf
````
Ten en cuenta que ``04381f1995bf`` es el ID de la imagen que se nos creo anteriormente, este
valor podría variar por supuesto entre máquinas. Debemos definir el puerto con ``-p`` para
poder comunicarnos desde el exterior y con ``--name`` le damos un nombre para que Docker no lo
asigne de manera aleatoria, quedando más personalizada.
`Recordemos en el paso anterior que en vez de un ID de imagen como 04381f1995bf podemos usar
el nombre de la imagen, por ejemplo, para el MS Persons usamos el nombre de tag cuando se
generó la imagen de liceoyarah-ms-persons, podemos usar este nombre y también obtendríamos
el mismo resultado, más legible incluso`.
---------------------------------------------------------------------------------------
### Optimizaciones de Imágenes/Contenedores Docker (**VERSIÓN 1**)
``NOTA RECORDATORIA: Recordemos ubicarnos dentro del respectivo micro servicio``
1. Primeramente, requeriamos generar primero el JAR antes de hacer los pasos de Docker, pues
el JAR contenía el proyecto en pocas palabras, ahora, no necesitamos dos comandos sino solo
1, el cual genera el JAR y al mismo tiempo generará la imagen correspondiente gracias a unos
ajustes realizados en el Dockerfile, ahora, el comando es (**No olvide pararse en el proyecto
correspondiente (MS Correspondiente) para realizar la ejecución del comando**):
````dockerfile
docker build -t liceoyarah-ms-persons-image:latest . -f .\Dockerfile
````
El anterior comando construye la imagen, el ``-t`` me permite dar un nombre, el ``.`` permite
generar a nivel de raíz la locación y el ``-f`` apuntar al archivo de configuración, que como
estamos trabajando los micro servicios independientes a pesar de usar un mono repo, decimos
que es el Dockerfile de la raíz el que usaremos. Ahora, una vez generada correctamente la
imagen, podríamos ejecutar el comando para crear el contenedor y asociarlo a una red.

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\
:::::::::::::::::::::::::::::::::: INFORMACIÓN IMPORTANTE ::::::::::::::::::::::::::::::::::\
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

``
NOTA 1: Tenemos que tener la red creada para tirarla de entrada con un solo comando, la
idea es tener una red llamada liceo_yarah. Si dado el caso no está creada aún, por favor
primero ejecute el comando docker network create liceo_yarah y luego ejecute el comando
de creación del contenedor.
``

``
NOTA 2: Cuando estemos trabajando sobre la red de docker, debemos tener MUCHO CUIDADO con
el nombre que le damos al aplicar el Docker Run al contenedor, si mi application.properties 
me dice que el valor de la propiedad spring.application.name es yarah-ms-persons, entonces 
ese es el nombre que le debo colocar al contenedor al correr el Docker Run, sino, no podrá 
comunicarse entre los micro servicios, mucho cuidado con esta configuración porque es fácil 
olvidarla colocando otro nombre.
``

En este orden de ideas, corra el comando:
````dockerfile
docker run -p 18881:18881 --name yarah-ms-persons --network liceo_yarah liceoyarah-ms-persons-image
````
Con ``-p`` asignamos el puerto, con ``--name`` damos el nombre al contenedor, con ``--network`` definimos en cual red
lo vamos a ubicar y al final se coloca la imagen que se asociará.



--------------------------------------------------------------------------------------
### Creación de base de datos para Docker

Para el proceso de dockerización de la base de datos, necesitamos tener un contenedor para la base
de datos, este proyecto manejará 100% PostgreSQL pero diferentes bases de datos para cada MS. Para
crear cada BD, habiendo descargado la imagen de postgres a usar, usamos el siguiente comando:
````dockerfile
docker run -p 5555:5432 --name yarah-db-ms-persons --network liceo_yarah -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=yarah_ms_persons_db -e POSTGRES_USER=postgres -d postgres:16.4
````
Explicación, el ``-p`` define el puerto externo (el cual definimos otro porque ya tenemos ocupado el 5432),
con el ``--name`` definimos como llamaremos este contenedor, importante definir el ``--network`` para usarla sobre la 
misma red de contenedores de Docker, y los ``-e`` son las variables de entorno que requiere postgres para poder
funcionar según las especificaciones de docker hub; definimos el ``-d`` para que arranque en modo detach y,
finalmente, colocamos la imagen que usaremos, que, como mencionamos sera una imagen de postgres, versión 16.4,
es decir, la imagen de Docker ``postgres:16.4``.

Credenciales del ejemplo:
* **Host**: localhost
* **Database**:  yarah_ms_persons_db
* **Port**: 5555
* **Usuario**: postgres
* **Contraseña**: 1234

ENTONCES, PARA PODER QUE LA BASE DE DATOS SE ESTÉ CONECTANDO A LA APLICACIÓN, DEBEMOS GARANTIZAR QUE LA BASE DE DATOS
YA ESTÁ LEVANTADA Y CORRIENDO ANTES DE APLICAR docker run.

--------------------------------------------------------------------------------------
### Notas de actualización
* **Última actualización:** Septiembre 12/2024.
* **Desarrollador:** Juan Sebastian Medina Toro.
* **Contexto trabajo:** Elaboración Micro Servicios.

