
# RESUMEN DE COMANDOS GENERALES #
### Este documento tiene como finalidad referir los comandos usados para ir levantando gradualmente la aplicación, creando las imágenes, los contenedores y una data de prueba condicionada para el uso solo local de la aplicación.

* Comandos de creación de ``imagenes`` (El JAR se crea por defecto gracias a la configuración del Dockerfile).
``Nota: Recordemos que debemos estar parados en la carpeta del proyecto/micro servicio para hacer lectura de su
respectivo Dockerfile para la creación.``
````dockerfile
* Micro servicio de personas (ms-persons)
docker build -t liceoyarah-ms-persons-image:latest . -f .\Dockerfile

* Micro servicio de usuarios (ms-users)
docker build -t liceoyarah-ms-users-image:latest . -f .\Dockerfile

* Micro servicio de students (ms-students)
docker build -t liceoyarah-ms-students-image:latest . -f .\Dockerfile
````

* Comandos para crear ``contenedores`` a través de las imágenes creadas:
``Nota: No olvidar que antes de estos comandos debemos tener la red virtual
para que los contenedores convivan y se debe llamar liceo_yarah. También, no
olvidar en --name colocar el nombre del proyecto descrito en la propiedad de
spring.application.name dentro de cada application.properties``:
````dockerfile
* Micro servicio de personas (ms-persons)
docker run -d -p 18881:18881 --name yarah-ms-persons --network liceo_yarah liceoyarah-ms-persons-image

* Micro servicio de usuarios (ms-users)
docker run -d -p 18882:18882 --name yarah-ms-users --network liceo_yarah liceoyarah-ms-users-image

* Micro servicio de students (ms-students)
docker run -d -p 18883:18883 --name yarah-ms-students --network liceo_yarah liceoyarah-ms-students-image
````

* Comandos para crear la base de datos dockerizada dentro de la red para cada aplicacion asociada
````dockerfile
* Micro servicio de personas (ms-persons)
docker run -p 5555:5432 --name yarah-db-ms-persons --network liceo_yarah -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=yarah_ms_persons_db -e POSTGRES_USER=postgres -d -v data-ms-persons:/var/lib/postgresql/data postgres:16.4

* Micro servicio de usuarios (ms-users)
docker run -p 5556:5432 --name yarah-db-ms-users --network liceo_yarah -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=yarah_ms_users_db -e POSTGRES_USER=postgres -d -v data-ms-users:/var/lib/postgresql/data postgres:16.4

* Micro servicio de students (ms-students)
docker run -p 5557:5432 --name yarah-db-ms-students --network liceo_yarah -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=yarah_ms_students_db -e POSTGRES_USER=postgres -d -v data-ms-students:/var/lib/postgresql/data postgres:16.4
````

