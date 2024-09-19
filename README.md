# LICEO YARAH #
### Desarrollado por: ###
* **Desarrollador de Backend**: Juan Sebastian Medina Toro.
* **Enlaces Comunicación**: [Enlace a Linkedin](https://www.linkedin.com/in/juan-sebastian-medina-toro-887491249/).
* **Portafolio Trabajo**: [Mi Portafolio](https://github.com/SebasMedina95).
* **Enlace Aplicación**: [Liceo Yarah](https://github.com/SebasMedina95/AppLiceoYarah.git).
* **Asistencias de Desarrollo**: No.
* **Frontend de comunicación**: No.

-------------------------------

### APLICACIÓN ACADÉMICA TIPO MASTER ###
Esta aplicación es el desarrollo de un sistema de administración de
estudiates para el Liceo Yarah de una reconocida municipalidad, lo cual,
el objetivo será construir esta aplicación BACKEND en una arquitectura
de micro servicios (aplicación API RESTFul), generando una aplicación
más mantenible; hasta el momento no se ha definido de cuantos micro
servicios tendrémos, pero se lista al final de este enunciado los MS
tentativos para la aplicación.

La aplicación trabajará con bases de datos independizadas, es decir, cada
micro servicio tendrá su base de datos especializada, y para los procesos
de comunicación donde se requiere referencias estaremos usando campos de
referencia (cómo el número de documento) y tecnologías que permitan la 
comunicación entre micro servicios bajo una estructura dockerizada y
la misma red de trabajo.

### Tecnologías y herramientas dispuestas para el desarrollo:
| Tecnología      | Descripción y Versiones                                                    |
|-----------------|----------------------------------------------------------------------------|
| Spring Boot     | Backend en su versión 3.3.3                                                |
| PostgreSQL      | Base de datos relacional, versión 16.4                                     |                                                                                                                                           | 
| JPA / Hibernate | Manejo ORM para las consultas a la base de datos                           |
| JUnit y Mockito | Para las pruebas unitarias de los micro servicios                          |
| JDK             | Ejecución Java, versión 21                                                 |                                                                                                                                           | 
| Docker          | Para Dockerizar la aplicación y manejo tanto de contenedores como imágenes |                                                                                                                                          |
| Swagger         | Documentación Swagger, versión 2 (Dependencia Maven)                       |
| Feign           | Comunicación entre micro servicios (Dependencia Maven)                     |
| Git             | Manejador de versiones                                                     |
| Kubernets       | Para el manejo de las imágenes que iremos generando                        |
| MiniKube        | Para el manejo de Kubernets en la aplicación                               |
| Postman         | Prueba de End Points                                                       |
| DBeaver         | Para el manejo de las bases de datos                                       |

* **NOTA 1**:
  Se está usando un ``monorepo`` solamente para poder manejar mejor el tema
  de legibilidad, pero no hay un caso técnico asociado, también, pensando en
  las personas que verán esta parte, para que no sea caótico navegar entre
  los diferentes proyectos.
* **NOTA 2**:
  La aplicación a medida que se trabaja se irá dockerizando, por lo que ciertos
  maneras de correr las aplicaciones variaran un poco a lo tradicional.

-------------------------------

### MÓDULOS (MICRO SERVICIOS) ###
Los módulos se irán creando en un orden especifico para ir comunicandolos de manera respectiva,
sin embargo, algunas funcionalidades quedarán pendientes dado el caso de dependencias no previstas
al inicio del desarrollo, también se hace acotación de que el módulo de autenticación será acomodado
tentativamente en el micro de usuarios, sin embargo, es una idea que podrá organizarse sobre marcha 
dados los temas de OAuth2 y Spring Security; a continuación, están los micro servicios tentativos:

| Micros      | Estado                    | Proyecto      | Descripción                                                                                                                                                         |
|-------------|---------------------------|---------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Personas    | `Finalizado`              | ms-persons    | Micro servicio para la gestión de las diferentes personas, tales como estudiantes, usuarios, acudientes, profesores, empleados, entre otros.                        |
| Usuarios    | `Parcialmente_Finalizado` | ms-users      | Micro servicio para la gestión de los usuarios, los usuarios son los que tendrán acceso al sistema y podrán ser estudiantes y profesores en primera instancia.      | 
| Estudiantes | `Finalizado`              | ms-students   | Micro servicio para la gestión de los estudiantes, los estudiantes son usuarios, por lo que enlazamos este MS con el de Usuarios.                                   |
| Profesores  | `Finalizado`              | ms-professors | Micro servicio para la gestión de los profesores, los profesores son usuarios del sistema y no pueden ser estudiantes.                                              |
| Asignaturas | `Sin_Empezar`             | -             | Micro servicio para la gestión de las asignaturas, estará enlazada con los profesores para tomar apunte de que asignaturas puede dar cada docente.                  |
| Documentos  | `Sin_Empezar`             | -             | Micro servicio para manejar la documentación, tal como documentos de identidad, fotografías, sanciones y comprobantes.                                              |
| Grupos      | `Sin_Empezar`             | -             | Micro servicio para gestionar los grupos y los estudiantes que pertenencen a los grupos, a este punto, nos comunicamos con el micro de estudiantes y de profesores. |
| Planillas   | `Sin_Empezar`             | -             | Micro servicio para la consolidación de las notas de los estudiantes por periodo académico, nos enlazamos con el micro de grupos                                    |
| Informes    | `Sin_Empezar`             | -             | Micro servicio para generar el boletín de notas por periodo, enlazado directamente con el micro de planillas                                                        |

-------------------------------

### LEVANTAMIENTO DE LA APLICACIÓN ###

``Es importante destacar que la aplicación la iremos documentando y actualizando a medida surjan temas de noción``, 
por lo cual, para el levantamiento de la aplicación lo haremos todo en miras de trabajar ``directamente con Docker``,
por lo que usaremos las imagenes y configuraciones dispuestas en los diferentes archivos Dockerfile y el archivo de
docker-compose.yml que hay en cada micro servicio, a continuación, se denotan los pasos a la fecha **(13/09/2024)** para
levantar cada una de las aplicaciones en ambiente de desarrollo **LOCAL**.

#### Creación/verificación de existencia de RED (Network Docker) de trabajo
* Para poder trabajar con los micros dockerizados, debemos estar bajo la misma red de trabajo para que se puedan
comunicar tanto los micros entre si como las bases de datos dentro del contexto de una red claramente definida, 
por tanto, verificamos con el comando ``docker network ls`` si tenemos la red **liceo_yarah**, en caso de no
tenerla tenemos que creala antes de ejecutar los siguientes comandos, por lo que ejecutaríamos para crear la red
el comando ``docker network create liceo_yarah``; si dado el caso ya la tienes puedes pasar al siguiente paso.

#### Micro servicio de personas:
* Para levantar ``ms-persons`` nos ``ubicamos dentro del proyecto en su raíz`` en el proyecto y ejecutamos:
````dockerfile
docker-compose --project-name yarah_ms_persons up --build -d
````

#### Micro servicio de usuarios:
* Para levantar ``ms-users`` nos ``ubicamos dentro del proyecto en su raíz`` en el proyecto y ejecutamos:
````dockerfile
docker-compose --project-name yarah_ms_users up --build -d
````

#### Micro servicio de estudiantes:
* Para levantar ``ms-students`` nos ``ubicamos dentro del proyecto en su raíz`` en el proyecto y ejecutamos:
````dockerfile
docker-compose --project-name yarah_ms_students up --build -d
````

#### Micro servicio de profesores:
* Para levantar ``ms-students`` nos ``ubicamos dentro del proyecto en su raíz`` en el proyecto y ejecutamos:
````dockerfile
docker-compose --project-name yarah_ms_professors up --build -d
````
El comando ejecutará el ``docker-compose`` con las configuraciones requeridas (usará como referencia el Dockerfile creado
para cada aplicación en concreto), levantará la imágen, usará el volumen de datos en caso de haber, construira el contenedor 
y lo levantará de manera detach (desacoplada).\

Le bandera del comando anterior ``--project-name`` le dará un nombre al contenedor, el ``up`` levantará el comando, el ``--build`` re construirá la
configuración si dado el caso hicimos algún cambio en el código y el ``-d``es para ejecutarlo desacopladamente.

``Debe verificar que los micro servicios quedaron creados dentro de la red de liceo_yarah``; aunque la configuración de
nuestro docker-compose y Dockerfile ya hace esta parte, no está de más verificar adecuadamente que tanto la base de datos
que se crea como el proyecto estén en la misma red, para ello, ejecutemos el comando ``docker network inspect liceo_yarah``
y con esto lo verificamos.

-------------------------------

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

* Microservicio de profesores (MS Professors) -> **ms-professors**.
  Ingrese a la siguiente URL:
````dockerfile
http://localhost:18884/business/swagger-ui/index.html
````
-------------------------------

### Notas útiles del desarrollo:

> Como las aplicaciones ya se encuentran Dockerizadas y configuradas en el Dockerfile y también en el docker-compose.yml,
> es importante destacar que, los nombres que estamos dando a nivel de contenedores o a nivel de las aplicaciones en 
> otras palabras, debe corresponder al nombre que le dimos en el archivo de application.properties en la propiedad de
> name, esta propiedad es la que estamos usando cuando hacemos referencias con feign a otros micro servicios, no usamos
> una URL sino el "host" por así decirlo, el host es el nombre, entonces, debemos tener especial atención porque si
> cambiamos esto podríamos tener muchos problemas de comunicación. Por tanto, cuando ejecutemos el comando docker-compose
> que ya vimos en la sección superior, al aplicar el --project-name coloquemos como nombre el del proyecto, es decir,
> el nombre que tiene el application.properties.name para así tener la comunicación lógicamente enlazada.

> Para el manejo de los volumenes de datos, que es donde esta la data, recordemos que la información en desarrollo se
> está guardando localmente en una configuración especifica de la máquina, es decir, que si ejecuto la aplicación en 
> otro computador, es claro que no tendremos data, se creará el volumen pero estará vacío por justa causa, por lo cual,
> si vamos a ejecutar en otra máquina, no estará demás crear un Backup de los INSERT SQL para poblar la base de datos, 
> esta incomodidad nos toca porque no tenemos la BD en una nube.

> Tengamos en cuenta que, a la fecha, 19 de Septiembre del 2024 no estamos haciendo Testing a la aplicación, una de las
> configuraciones que hicimos para el Dockerfile omite este tema de las pruebas para poder generar el JAR, entonces, 
> debemos ajustar más adelante este tema por buenas prácticas, todo lo que desarrollamos debe tener pruebas unitarias
> pero solamente en la implementación del servicio, no vamos a probar utilidades ni controladores.






