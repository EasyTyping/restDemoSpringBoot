# SpringBoot Rest Service Demo
Academic project consisting of a rest service demo with SpringBoot and SpringDataJPA.

To run the service locally the system will need:

1. Microsoft Windows (64 bits). Versions 7, 8, 8.1 and 10.
2. JDK version 1.8 or greater must be installed. 
3. PostgreSQL as DBMS

The service is in the folder called "src/main/ilerna/projectdam", which contains the maven project. The application boot file is ProyectoFinalApplication.java.
PostgreSQL (v. 12.5) has been used, as DBMS. The necessary configuration for connecting the service to the DBMS is located in the application.properties file of the resources package. We can migrate from DBMS by changing the configuration parameters of said file and adding the necessary dependencies of Maven.

The default configuration is:

      ## Connection - PostgreSQL
      spring.datasource.url = jdbc: postgresql: // localhost: 5432 / ProyectoDAM
      spring.datasource.username = 
      spring.datasource.password = 
      # spring.jpa.database = POSTGRESQL
      # spring.datasource.driver-class-name = org.postgresql.Driver

The project initially loads the tables created with a small SQL script with initial sample data.

The following configuration parameter has been set to create in the application.properties:

	  productionspring.jpa.hibernate.ddl-auto = create
    
It is advisable to change the value to update after the first execution, and / or when making changes in the data model code, so that it only updates the tables in case of changes in the model. Also, it is advisable to comment on this line in case of production.
Spring Boot is already configured with an embedded TomCat so it is not necessary to perform the deployment.
To get the service up quickly, we can run it with Maven from the console.

To run it, from the project folder, which contains the pom.xml file:

1. If we don't have the Maven environment variables added to the path, we can run:

	C: \… \ proyectdam \ mvnw.cmd spring-boot: run
  
2. If we have the Maven environment variables added to the path we can execute the command:

	C: \… \ proyectdam \ mvn spring-boot: run
  
If we want to compile it with Maven, we open the command prompt and enter the following command in the folder that contains the Spring Boot files: 

  	C: \… \ proyectdam \ mvn clean install

With this we will have generated the file

	C: \ ... \ proyectdam \ target \ proyectdam-1.0.jar

Now from the terminal:

	java –jar C: \ ... \ projectdam \ target \ projectdam-1.0.jar
