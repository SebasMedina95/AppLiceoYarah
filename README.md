# APLICACIÓN LICEO YARAH #

-------------------------------

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
### Envío de imágenes a Docker Hub
Para el envío de imágenes a Docker Hub, teniendo la cuenta correspondiente, debemos crear el repositorio
para cada uno de los proyectos (Microservicios), por tanto, para cada uno ejecutaremos los comandos 
correspondientes para enviar las imágenes. Es importante destacar que, cada repositorio nos va quedar
con una estructura ``usuario-docker/nombre-repo``, por tanto, las imágenes que tenemos deben tener este
nombre para que al hacer el push tome el repo que corresponde, por ende, tenemos que generar una especie
de "copia" de la imagen pero con el nombre con el esquema mencionado, a continuación, se  muestra más
claro este punto

* Microservicio de personas (MS Persons) -> **ms-persons**.
Nos paramos en el proyecto y ejecutamos el comando:
````dockerfile
docker tag liceoyarah-ms-persons-image sebasmedina95/yarah-ms-persons
````
Explicación: liceoyarah-ms-persons-image es la imagen que hemos generado localmente (la copia por así decirlo), 
esta será el tag que usaremos, y sebasmedina95/yarah-ms-persons representa al usuario de docker y el nombre que 
le dimos al repositorio (es decir que cuando generamos el repo en Docker Hub nos quedo ese nombre). 
**En conclusión, hemos creado una imagen copia con el nombre ``sebasmedina95/yarah-ms-persons``**.
Una vez generada la copia, ejecutamos el comando:
````dockerfile
docker push sebasmedina95/yarah-ms-persons
````
Nota importante: Si dado el caso nos dice que no tenemos acceso porque no estamos logeados, debemos ejecutar
el comando ``docker login`` y allí damos el usuario y la contraseña de nuestra cuenta de Docker para poder
proceder.

Repetimos el proceso con los demás proyectos:
````dockerfile
docker tag liceoyarah-ms-users-image sebasmedina95/yarah-ms-users
docker tag liceoyarah-ms-students-image sebasmedina95/yarah-ms-students
docker tag liceoyarah-ms-professors-image sebasmedina95/yarah-ms-professors
````

````dockerfile
docker push sebasmedina95/yarah-ms-users
docker push sebasmedina95/yarah-ms-students
docker push sebasmedina95/yarah-ms-professors
````

-------------------------------
### Revisión para trabajar en Kubernets
Para trabajar usando una interfaz los temas de Kubernets (K8s) debemos primeramente iniciar, para el 
caso local de este proyecto el minikube, para ello, debemos ejecutar el comando:

``Nota:`` No esta de más que revisemos el estado de minikube con el comando ``minikube status``

````dockerfile
minikube start --driver=hyperv
````
Aunque podemos trabajar con la de docker, a veces tenemos problemas para la generación artificial
de URLs para el tema de las pruebas, por tanto, usemos hyperv, podemos usarlo sin problema por la
naturaleza de la instalación de la aplicación de Docker y la configuración moderna de mi máquina
que soporta esto.\
Ahora, para tener una interfaz para trabajar un poco más amigable el tema debemos ejecutar el comando:
````dockerfile
minikube dashboard --url
````
Este comando me va a generar una URL que se verá como la siguiente:
````dockerfile
http://127.0.0.1:57616/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/
````
Debe estar corriendo para poder mantener la URL, la pegamos en un navegador y tenemos un dashboard para
trabajar todo el tema de Kubernets.

<<**INSTRUCTIVO DE COMANDOS PARA TRABAJO CON KUBERNETS (K8s)**>>

Debemos generar el Deployment para la base de datos y también para la aplicación, para este caso, se
hacen dos pasos diferentes pero que se empalman para trabajar en conjunto con base a las configuraciones
internas de las aplicaciones (**Acá empieza a tener mucho sentido la rigidez de los nombres que le
dimos a todo el proceso y configuraciones, los K8s actuan con un DNS interno y reglas de nombres como
si fueran Hosts, esto ayudará mucho a la hora de comunicar las aplicaciones**).

A continuación haremos el paso a paso para ir generando cada estructura, el ejemplo de la documentación
se hará con el microservicio de personas, pero, como se implemente este no habrá pierde para los demás
cuando se estén haciendo:

--------------------------------
**CREACIÓN Y CONFIGURACIÓN DE DEPLOYMENT PARA LA BD (Imperativamente)**

De manera imperactiva, debemos crear el archivo de deployment, en cada microservicio debemos de crear
una carpeta de deployments donde ubicaremos el deployment de desarrolo y el de producción, por tanto,
ubicados en el de desarrollo para la documentación ejecutamos el comando:

````dockerfile
kubectl create deployment yarah-db-ms-persons --image=postgres:16.4 --port=5432 --dry-run=client -o yaml > deployment-yarah-db-ms-persons.yaml
````
Con el comando creamos un deployment y le damos el nombre que le dimos en términos de base de datos al proyecto, es importantísimo esta parte
porque ya tenemos configuraciones internas en las properties con eso, le damos el nombre de ``yarah-db-ms-persons`` para el caso del micro de
personas, luego con ``--image`` definimos la imagen de docker a usar, con el ``--port`` definimos el puerto, con el ``--dry-run`` hacemos la
separación para generar el archivo y con el ``-o`` defino el tipo de archivo, que será YAML y le damos el nombre al archivo de despliegue, para
este caso, le colocamos el nombre de ``deployment-yarah-db-ms-persons.yaml``.

En este archivo generamos las configuraciones generales así como las configuraciones de variables de entorno si dado el caso son requeridas, 
ahora, una vez configurado el archivo, debemos aplicarlo, para aplicarlo usamos el comando:

````dockerfile
kubectl apply -f .\deployment-yarah-db-ms-persons.yaml
````
Donde con el comando ``apply`` generamos la aplicación, el ``-f`` definimos el archivo y luego el nombre del archivo.
Luego de ejecutar el comando anterior, mirando los pods deberíamos observar que todo está OK, en la interfaz veremos la señal verde de que todo
está bien, también, ejecutando el comando ``kubectl get pods`` veo que en la columna READY 1/1. Podemos verificar en los logs si todo está bien
usando el comando:
````dockerfile
kubectl logs yarah-db-ms-persons-4redf548b0-mmt4z
````
Asumiendo que el NAME del pod se llama kubectl logs ``yarah-db-ms-persons-4redf548b0-mmt4z``
Ahora, debemos generar el servicio de exposición para poder usar lo anteriormente realizado, poder exponerlo tanto al exterior en el caso
de consumirlo por fuera de Kubernets como en la red interna, entonces, ejecutamos el comando:
````dockerfile
kubectl expose deployment yarah-db-ms-persons --port=5432 --type=NodePort
````
En este comando, exponemos el deployment ``yarah-db-ms-persons`` por el puerto de trabajo para BD Postgres que es ``5432`` y el tipo,
acá es importante, le decimos ``NodePort``, dentro de la configuración del deployment definimos unos puertos de servicio para comunicación
externa, esto lo hacemos por temas de desarrollo para generar conexión de BD por fuera y ver la base de datos, entonces usamos puertos
por nodo.

Literalmente, el yarah-db-ms-persons nos va quedar como una especie de hostname. Es importante que coloquemos el nombre que le dimos en el properties
en la propertie spring.datasource.url=jdbc:postgresql://yarah-db-ms-persons:5432/yarah_ms_persons_db.

Verifiquemos que si se creo con el comando:
````dockerfile
kubectl get services
````
Como podemos ver, se nos generó como una especie de IP "Estática" en la columna CLUSTER-IP, esta IP se mantendrá así A MENOS de que eliminemos
el servicio y lo volvamos a crear, importante a tener en cuenta esto. También tenemos el hostname que se representa como la columna NAME.

-------------------------------
**CREACIÓN Y CONFIGURACIÓN DE DEPLOYMENT PARA EL MICRO SERVICIO (Imperativamente)**

* **NOTA 1 ===> LA IMAGEN YA TIENE QUE ESTAR MONTADA EN EL DOCKER HUB.**\
* **NOTA 2 ===> Debemos estar parados en la carpeta donde colocaremos los archivos.**\
* **NOTA 3 ===> Comunicación con Docker Hub, debemos estar logeados, repo: sebasmedina95/yarah-ms-persons:latest.**

Aunque podemos generar un comando directo para el tema, vamos a crear también un YAML de configuración para este tema, para mantener la misma
estructura que la base de datos, para ello, ejecutamos el comando:
````dockerfile
kubectl create deployment yarah-ms-persons --image=sebasmedina95/yarah-ms-persons:latest --port=18881 --dry-run=client -o yaml > deployment-yarah-ms-persons.yaml
````
Al igual que con la base de datos, creamos ahora el despliegue y le damos el nombre de yarah-ms-persons respetando el nombre que le dimos
al proyecto en las properties, con el --image definimos la imagen que se usará, **USAREMOS UNA IMAGEN QUE SE ENCUENTRA YA SUBIDA EN DOCKER HUB**,
debemos especificar la versión. Luego definimos el --port que sería el puerto que le dimos en las propiedades de la aplicación y generamos
los otros campos para el archivo. Luego de lo anterior aplicamos con el comando:
````dockerfile
kubectl apply -f .\deployment-yarah-ms-persons.yaml
````
Ahora, debemos exponer el servicio así como lo hicimos con la base de datos, usamos el comando.
````dockerfile
kubectl expose deployment yarah-ms-persons --port=18881 --type=LoadBalancer
````
Para este caso, a diferencia del anterior, usamos el balanceador de cargas LoadBalancer. Confirmamdos revisando los servicios usando
el comando:
````dockerfile
kubectl get services
````
Como podemos ver, ahora nos aparece el microservicio allí, pero, con un detalle particular, si vemos la columna EXTERNAL-IP, 
tenemos al micro con un <pending>, y en la parte de PORTS tenemos el puerto asignado con : otro puerto especial. Ahora, lo 
que requerimos es generar una IP que se conecte a lo que ya tenemos desplegado en Kubernets, para ello, ejecutamos el comando:
````dockerfile
minikube service yarah-ms-persons --url
````
Al ejecutar el comando anterior, nos va generar una URL muy parecida a esta: http://172.24.7.212:31991, toda la ejecución es 
"local", nos da la IP para conectarnos a nivel de Kubernets y el puerto, ahora probando nuevamente los End Point de MS Persons 
pero con esta URL debería poder obtener resultados.
``NOTA => Para acceder a la base de datos de Kubernets desde fuera, desde el deployment debimos de configurar la exposición del puerto``, 
luego, cuando obtenemos la URL de la aplicación, también podemos obtener la URL de la base de datos, de esta manera, podemos realizar 
la configuración y conectarnos, el comando sería similar:
````dockerfile
minikube service yarah-db-ms-persons --url
````
Con base a lo anterior, para la base de datos, entonces nos quedaría algo como:
* Host: 172.24.7.212
* Port: 30001
* Database: yarah_ms_persons_db
* Nombre de usuario: postgres
* Contraseña: 1234
El Host podría variar según la IP que nos dé el Kubernets.

--------------------------
**¿CÓMO ACTUALIZAMOS LAS IMÁGENES Y LAS USAMOS EN KUBERNETS? - (Imperativamente)**

Antes de comenzar, asegurarnos de estar logueados a Docker Hub.
Ocurrirá posiblemente que, tengamos que actualizar alguna funcionalidad de algún micro que tengamos desarrollado, el 
proceso deberá dividirse en 5 etapas claves para no perdernos durante el flujo, las 5 etapas son las siguientes:

1. **Ajuste de cambios**
   Ajustamos los cambios y los probamos para no tener inconvenientes luego.
2. **Actualización de imagen local**
   Aplicamos el comando:
   ````dockerfile
   docker build -t liceoyarah-ms-persons-image . -f .\Dockerfile
   ````
3. Debemos etiquetar nuevamente. Generación de copia con el nombre de la imagen en repositorio Docker Hub
   ````dockerfile
   docker tag liceoyarah-ms-persons-image sebasmedina95/yarah-ms-persons:v1.0.1
   ````
   **NOTA IMPORTANTÍSIMA**: Para poder aplicar el proceso de actualización en Kubernets, necesitamos "forzar" que tome 
el nuevo cambio, para ello, necesitamos definir un nombre distintivo y asegurarnos de que no nos quede la última versión, 
para esto le colocamos el ``:v1.0.1``. Recordemos que la imagen la habíamos llamado ``inicialmente liceoyarah-ms-persons-image``,
el ``sebasmedina95/yarah-ms-persons`` es como se llama en el repo de Docker Hub y debemos adjuntar la versión.

4. Actualización de imagen en el repositorio Docker Hub
   Ejecutamos el comando para subir la imagen actualizada:
   ````dockerfile
   docker push sebasmedina95/yarah-ms-persons:v1.0.1
   ````
   ``NOTA:`` Debemos respetar el tema de la versión, para que nos aparezca la diferencia en el repo de Docker Hub.
5. Revisión del pod que está usando actualmente en el Kubernets y actualización:
   * Antes de actualizar, revisemos que versión se está ejecutando (Asumamos que la última versión se llamaba v1.0.0)
     Ejecutamos el comando para detectar los pods:
     ````dockerfile
     kubectl get pods
     ````
   * Tomamos el NAME del pod requerido y vemos los detalles con el comando (Asuma yarah-ms-persons-548f447475-8lclz 
     como el NAME):
     ````dockerfile
     kubectl describe pod yarah-ms-persons-548f447475-8lclz
     ````
   * Nos dirigimos al apartado Containers y la propiedad Image. Al revisar, veremos la última versión, algo del tipo 
     **usuario-docker/nombre-imagen:v1.0.0** Ahora, sabiendo que tenemos la imagen actualizada en Docker Hub con una etiqueta 
     diferente, ejecutamos el comando de actualización:
     ````dockerfile
     kubectl set image deployment yarah-ms-persons yarah-ms-persons=sebasmedina95/yarah-ms-persons:v1.0.1
     ````
     El comando lo que hace es que actualizamos la imagen, le digo el nombre de actualización que vendría siendo ``yarah-ms-persons`` 
     (conservemos el mismo nombre para más adelante, en futuras actualizaciones, no se nos vuelva un caos el tema de identificación, 
     lo más importante es distinguir que el elemento distintivo principal será el tag que se asignará, esta parte del tag se ve un 
     poco más adelante en este mismo apartado, pero para consideración), luego digo el nombre del contenedor, que viene siendo en 
     la parte del ``Containers cuando le di describe anteriormente el nombre donde esta la imagen de apuntamiento a Docker Hub``, en 
     este caso se llama ``yarah-ms-persons``, luego la igualación es al Docker Hub con la imagen y tag actualizado que para este caso
     sería **sebasmedina95/yarah-ms-persons:v1.0.1**. Si todo sale bien, debe aparecernos en la consola la respuesta: 
     ``deployment.apps/yarah-ms-persons image updated``.
   * Revisamos colocando los comandos:
     ````dockerfile
     kubectl get pods
     ````
   * Si observamos bien, el NAME del pod incluso cambió, es una buena señal, ahora, tenemos el NAME: yarah-ms-persons-7cb5d7ffd-r7qxr, 
     ahora, verificamos en la usando el comando de describe pero esta vez con el nuevo pod:
     ````dockerfile
     kubectl describe pod yarah-ms-persons-7cb5d7ffd-r7qxr
     ````
     Podemos apreciar ahora que, en el apartado de Containers (Donde conservamos el nombre yarah-ms-persons) en la parte de Image 
     ahora tenemos el apuntamiento a la imagen actualizada en nuestro Docker Hub, **el sebasmedina95/yarah-ms-persons:v1.0.1**. Incluso, 
     en el apartado de Events vemos que se dercargo la imagen actualizada del Docker Hub, **_esto también nos indica que la actualización 
     se realizó de manera correcta_**.
     

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

> **Sobre Kubernets**: Si observamos en nuestro Docker Desktop, vemos en la opción de imágenes que se nos creo una nueva con 
> el nombre asociado para el Docker Hub, esto no genera conflicto, sin embargo, vemos que la que tenemos en local y la que 
> se creo quedan en un estado de Unused, para evitar esta parte, eliminemos la que creamos para subir a Docker Hub y podríamos 
> ejecutar el docker-compose.yml para volver a generar la imagen actualizada junto con el proyecto y su contenedor,
> esto con la finalidad de que nos quede actualizado el ambiente local. Sin embargo, al hacer pruebas, por medio de la red 
> interna y la conexión de nombres en las properties y archivos de configuración, aún sigue funcionando.






