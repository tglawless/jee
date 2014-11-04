package com.tglawless.wlp.pom.builder;

public class Dependency {

	private String file;
	
	private String groupId;
	
	private String artifactId;
	
	private String version;
	
	private String packaging;
	
	public Dependency() { }

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	@Override
	public String toString() {
		return "Dependency [groupId=" + groupId
				+ ", artifactId=" + artifactId + ", version=" + version
				+ ", packaging=" + packaging + ", file=" + file + "]";
	}
	
}
