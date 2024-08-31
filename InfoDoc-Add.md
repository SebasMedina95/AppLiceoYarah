
# RESUMEN DE COMANDOS GENERALES #
### Este documento tiene como finalidad referir los comandos usados para ir levantando gradualmente la aplicación, creando las imágenes, los contenedores y una data de prueba condicionada para el uso solo local de la aplicación.

* Comandos de creación de ``imagenes`` (El JAR se crea por defecto gracias a la configuración del Dockerfile)
````dockerfile
* Micro servicio de personas (ms-persons)
docker build -t liceoyarah-ms-persons-image:latest . -f .\Dockerfile

* Micro servicio de usuarios (ms-users)
docker build -t liceoyarah-ms-users-image:latest . -f .\Dockerfile

* Micro servicio de students (ms-students)
docker build -t liceoyarah-ms-students-image:latest . -f .\Dockerfile
````

* Comandos para crear ``contenedores`` a través de las imágenes creadas
````dockerfile
* Micro servicio de personas (ms-persons)
docker run -p 18881:18881 --name liceoyarah-ms-persons-container liceoyarah-ms-persons-image

* Micro servicio de usuarios (ms-users)
docker run -p 18882:18882 --name liceoyarah-ms-users-container liceoyarah-ms-users-image

* Micro servicio de students (ms-students)
docker run -p 18883:18883 --name liceoyarah-ms-students-container liceoyarah-ms-students-image
````

