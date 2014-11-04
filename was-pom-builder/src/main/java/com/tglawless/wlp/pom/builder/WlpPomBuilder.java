package com.tglawless.wlp.pom.builder;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class WlpPomBuilder {
	
	private static final String DEFAULT_WAS_BASE_DIR = "/opt/IBM/WebSphere/Liberty";
	
	private static final String DEV_API_IBM_DIR = "/dev/api/ibm";
	
	private static final String DEV_API_SPEC_DIR = "/dev/api/spec";
	
	private static final String DEV_API_THRID_PARTY_DIR = "/dev/api/third-party";
	
	private static final String DEV_SPI_IBM_DIR = "/dev/spi/ibm";
	
	private static final String DEV_SPI_SPEC_DIR = "/dev/spi/spec";
	
	private static final String DEV_SPI_THRID_PARTY_DIR = "/dev/spi/third-party";
	
	private static final String INSTALLER_POM_TEMPLATE = "wlp_installer_pom.ftl";
	
	private static final String DEVELOPMENT_POM_TEMPLATE = "wlp_development_pom.ftl";
	
	private String wasVersion = "8.5.5.3";
	
	private String baseDir = DEFAULT_WAS_BASE_DIR;
	
	private Collection<Dependency> dependencies;
	
	private Configuration cfg;
	
	public WlpPomBuilder() throws IOException {
		cfg = new Configuration();
		
		//URL template = Thread.currentThread().getContextClassLoader().getResource(".");
		//cfg.setDirectoryForTemplateLoading(new File(template.toString().substring(5)));
		cfg.setClassForTemplateLoading(this.getClass(), "/templates");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
	}
	
	private Dependency parseFileName(File dep){
		
		Dependency dependency = new Dependency();
		dependency.setFile(dep.getAbsolutePath());
		
		String fileName = dep.getName();		
		String[] parts = fileName.split("_");
		
		String groupId = parts[0].substring(0, parts[0].lastIndexOf("."));
		String artifactId = parts[0];
		
		String version = wasVersion;
		String packaging = "jar";
		
		if(parts.length >= 2){
			version = parts[1].substring(0, parts[1].lastIndexOf("."));
			packaging = parts[1].substring(parts[1].lastIndexOf(".") + 1);
		}
		
		dependency.setGroupId(groupId);
		dependency.setArtifactId(artifactId);
		dependency.setPackaging(packaging);
		dependency.setVersion(version);
		
		return dependency;
	}
	
	private List<Dependency> getDependenciesFromDir(String dir){
		
		List<Dependency> deps = new ArrayList<Dependency>();
		
		File folder = new File(dir);
		File[] jars = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				
				if(pathname.toString().endsWith(".jar")){
					return true;
				}
				
				return false;
			}
		});
		
		for(File jar : jars){
			deps.add(parseFileName(jar));
		}
		
		return deps;
	}
	
	private void generateInstallerPom() throws Exception {
		
		Map<String, Object> root = new HashMap<String, Object>();
        root.put("version", wasVersion);
        root.put("dependencies", dependencies);
        
        /* Get the template */
        Template temp = cfg.getTemplate(INSTALLER_POM_TEMPLATE);

        /* Merge data-model with template */
        FileOutputStream pom = new FileOutputStream("was_installer_" + wasVersion + ".pom", false);
        Writer out = new OutputStreamWriter(pom);
        temp.process(root, out);
        pom.flush();
        pom.close();
	}
	
	private void generateDevelopmentPom() throws Exception {
		
		Map<String, Object> root = new HashMap<String, Object>();
        root.put("version", wasVersion);
        root.put("dependencies", dependencies);
        
        /* Get the template */
        Template temp = cfg.getTemplate(DEVELOPMENT_POM_TEMPLATE);

        /* Merge data-model with template */
        FileOutputStream pom = new FileOutputStream("was_development_" + wasVersion + ".pom", false);
        Writer out = new OutputStreamWriter(pom);
        temp.process(root, out);
        pom.flush();
        pom.close();
	}
	
	
	
	public void execute() throws Exception {
		
		dependencies = new ArrayList<Dependency>();
		dependencies.addAll(getDependenciesFromDir(DEFAULT_WAS_BASE_DIR + DEV_API_IBM_DIR));
		dependencies.addAll(getDependenciesFromDir(DEFAULT_WAS_BASE_DIR + DEV_API_SPEC_DIR));
		dependencies.addAll(getDependenciesFromDir(DEFAULT_WAS_BASE_DIR + DEV_API_THRID_PARTY_DIR));
		dependencies.addAll(getDependenciesFromDir(DEFAULT_WAS_BASE_DIR + DEV_SPI_IBM_DIR));
		dependencies.addAll(getDependenciesFromDir(DEFAULT_WAS_BASE_DIR + DEV_SPI_SPEC_DIR));
		dependencies.addAll(getDependenciesFromDir(DEFAULT_WAS_BASE_DIR + DEV_SPI_THRID_PARTY_DIR));
				
		generateInstallerPom();	
		generateDevelopmentPom();
		
	}	
	
	public String getWasVersion() {
		return wasVersion;
	}

	public void setWasVersion(String wasVersion) {
		this.wasVersion = wasVersion;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public static void main(String[] args) throws Exception {
		
		if(args == null || args.length <= 1){
			System.out.println("Usage: java -jar wlp_pom_builder-VERSION.jar WLP_VERSION WLP_INSTALL_DIR");
			System.exit(1);
		}
		
		WlpPomBuilder builder = new WlpPomBuilder();
		builder.setWasVersion(args[0]);
		builder.setBaseDir(args[1]);
		builder.execute();
	}
	
}
