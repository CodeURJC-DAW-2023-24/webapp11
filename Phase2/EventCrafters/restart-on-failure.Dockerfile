#################################################
# Imagen base para el contenedor de compilación
#################################################
FROM --platform=linux/amd64 ubuntu:jammy
FROM maven:3.9.6-eclipse-temurin-17 as builder


# Define el directorio de trabajo donde ejecutar comandos
WORKDIR /project

# Copia las dependencias del proyecto
COPY pom.xml /project/

# Descarga las dependencias del proyecto
#RUN mvn clean verify

# Copia el código del proyecto
COPY /src /project/src

# Compila proyecto
RUN mvn clean package -DskipTests=true

#################################################
# Imagen base para el contenedor de la aplicación
#################################################
FROM openjdk:17.0.2

# Define el directorio de trabajo donde se encuentra el JAR
WORKDIR /usr/src/app

# Copia el JAR del contenedor de compilación
COPY --from=builder /project/target/*.jar /usr/src/app/app.jar

# Indica el puerto que expone el contenedor
EXPOSE 5000

# Comando que se ejecuta al hacer docker run
CMD [ "java", "-jar", "app.jar" ]