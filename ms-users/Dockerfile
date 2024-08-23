#Imagen base que vamos a usar
FROM amazoncorretto:21

#Directorio dentro de la imagen donde ubicaremos el proyecto
WORKDIR /app

#De la carpeta target de mi proyecto extraigo el JAR y lo coloco en el Workdir
COPY ./target/ms-users-0.0.1-SNAPSHOT.jar .

#Puerto en el que funcionara, NOTA, acá podríamos tener otro, pero usaremos el mismo en local
#que definimos en los properties, pero con otro podríamos tener implicitamente 2 instancias
#de la misma aplicación pero en diferentes puertos de trabajo
EXPOSE 18882

#Punto de entrada, accion de ejecucion separa
ENTRYPOINT ["java", "-jar", "ms-users-0.0.1-SNAPSHOT.jar"]