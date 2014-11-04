<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<licenses>
		<license>
			<name>IBM International License Agreement for Non-Warranted Programs</name>
			<url>http://public.dhe.ibm.com/ibmdl/export/pub/software/websphere/wasdev/maven/licenses/L-JTHS-8SZMHX/HTML/</url>
			<distribution>repo</distribution>
			<comments>Additional notices http://public.dhe.ibm.com/ibmdl/export/pub/software/websphere/wasdev/maven/licenses/L-JTHS-8SZMHX/HTML/notices.html</comments>
		</license>
	</licenses>

	<modelVersion>4.0.0</modelVersion>
	<groupId>com.ibm.tools.target</groupId>
	<artifactId>was.liberty.installer</artifactId>
	<version>${version}</version>
	<packaging>pom</packaging>

	<build>
		<plugins>
			<plugin>
				<artifactId>maven-install-plugin</artifactId>
				<version>2.3.1</version>
				<inherited>false</inherited>
				<executions>
					<#list dependencies as dep>
					<execution>
						<id>install-artifacts-${dep.artifactId}</id>
						<goals>
							<goal>install-file</goal>
						</goals>
						<phase>install</phase>
						<configuration>
							<file>${dep.file}</file>
							<groupId>${dep.groupId}</groupId>
							<artifactId>${dep.artifactId}</artifactId>
							<version>${dep.version}</version>
							<packaging>${dep.packaging}</packaging>
						</configuration>
					</execution>
					</#list>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>