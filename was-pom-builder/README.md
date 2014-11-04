#WebSphere Liberty Profile pom.xml Builder
This application is a simple command line tool that will create two POM files for installation into a Maven repository.  The first POM file automates the installation of all of the libraries provided by the Liberty Profile and the second POM is used to add these libraries to a Maven project.

##Building the JAR
To build the executable JAR file, run the following command from the project root directory:
```
mvn clean install
```

##Running the Tool
The tool is run using the following command:
```
java -jar was_pom_builder-VERSION.jar WLP_VERSION WLP_INSTALL_DIR
```
where VERSION is the version of the JAR file being executed, WLP_VERSION is the installed version of the Liberty profile and WLP_INSTALL_DIR is the root directory of the installation itself.  For example:
```
java -jar was_pom_builder-1.0.0.jar 8.5.5.3 /opt/IBM/WebSphere/Liberty
```

##Installing the POM Files into Maven
The result of running the command in the previous section will be two POM files:

*   was_installer_8.5.5.3.pom
*   was_development_8.5.5.3.pom

The **was_installer_8.5.5.3.pom** POM file installs all of the individual JAR files packaged with the Liberty Profile.  To execute this POM file, run the following command:
```
mvn install -f was_development_8.5.5.3.pom
```

The **was_development_8.5.5.3.pom** POM file can be used within Eclipse (or any Maven project) to pull all of the Liberty Profile dependencies into a project.  This POM must also be installed using the command below:
```
mvn install -f was_development_8.5.5.3.pom
```

##Using the POM files
Once the POM files have been generated and installed into the Maven repository, they can be included into a Maven project using the following dependency:
```XML
<dependency>
	<groupId>com.ibm.tools.target</groupId>
	<artifactId>was-liberty</artifactId>
	<version>8.5.5.3</version>
	<type>pom</type>
	<scope>provided</scope>
</dependency>
```

**Note:  if using a Liberty version pother then v8.5.5.3, replace 8.5.5.3 in the instructions above with the appropriate value.**