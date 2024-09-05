#Imagen base que vamos a usar
#Usamos una base de maven para empaquetar la estructura inicial del proyecto Maven
FROM maven:3.9.8-amazoncorretto-21 AS builder

#Directorio dentro de la imagen donde ubicaremos el proyecto
WORKDIR /app

#Copiamos el POM específico del proyecto para empaquetarlo y descargar las dependencias (Y lo pegamos en la raíz)
#También copiamos el ejecutor mvnw para ejecutar comandos Maven (Y lo pegamos en la raíz)
#También copiamos la carpeta mvn que contiene el wrapper de maven y lo copiamos en una carpeta con ese nombre.
COPY ./pom.xml .
COPY ./mvnw .
COPY ./.mvn ./.mvn

#Debemos "escapear" el comando inicial mvnw, en Unix a veces lo toma como un comando de fin de linea
#entonces, es mejor escapearlo de esta manera para no modificarlo internamente.
RUN sed -i 's/\r$//' mvnw

#Corremos el comando para generar la imagen, saltamos los test así como su compilación (-Dmaven.test.skip) y removemos
#el target porque ya no lo necesitariamos esta es la primera parte, para la compatibilidad de comandos Maven.
#también bloqueamos la ejecución del código fuente (-Dmaven.main.skip) y una bandera Spring adicional para
#los temas de empaquetamiento (-Dspring-boot.repackage.skip) y finalmente eliminamos el target (rm -r ./target/)
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

#Copiamos la carpeta del proyecto
#Tanto el proyecto como los test acá los copiamos
COPY ./src ./src

#Volvemos a generar el JAR y saltamos los test para empaquetar
RUN ./mvnw clean package -DskipTests

#Ahora el resultado anterior lo trasladamos a donde se debe en el contenedor
#aquí si usaremos la image base de salida final
FROM amazoncorretto:21

#Copiamos el resultado anterior a la carpeta
WORKDIR /app

#De la carpeta target de mi proyecto extraigo el JAR y lo coloco en el Workdir
#este JAR sale de la primera construcción anterior de comandos que le llamamos con el AS builder
COPY --from=builder /app/target/ms-students-0.0.1-SNAPSHOT.jar .

#Puerto en el que funcionara, NOTA, acá podríamos tener otro, pero usaremos el mismo en local
#que definimos en los properties, pero con otro podríamos tener implicitamente 2 instancias
#de la misma aplicación pero en diferentes puertos de trabajo
EXPOSE 18883

#Punto de entrada, accion de ejecucion separa
ENTRYPOINT ["java", "-jar", "ms-students-0.0.1-SNAPSHOT.jar"]