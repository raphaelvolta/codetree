package com.codetree.codetree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileReader {
	
	private List<File> folders = new ArrayList<>();
	private List<File> files = new ArrayList<>();
	private List<File> javaFiles = new ArrayList<>();

	public FileReader(String path) throws IOException {
		extract(path, new ArrayList<>());
	}

	private FileReader extract(String folderPath, List<File> recursive) throws IOException {
	    File scan = new File(folderPath);
	    File[] directoryContent = scan.listFiles();
	    if(directoryContent != null) {
	        for (File element : directoryContent) {
				System.out.print("Processing: " + element.getAbsolutePath());
	            if (element.isFile()) {
	            	if(element.getCanonicalPath().endsWith(".java")){
	            		this.javaFiles.add(element);
	            	} else if(!element.getCanonicalPath().endsWith(".java")){
						System.out.println("[Ignoring]");
						continue;
					}
	            	this.files.add(element);
	            	recursive.add(element);
					System.out.println();
	            } else if (element.isDirectory()) {
					System.out.println("[Folder - skip]");
					if(isMarkedAsIgnore(element.getCanonicalPath())){
						continue;
					}
	            	folders.add(element);
					extract(element.getAbsolutePath(), recursive);
	            }
	        }
		}
	    return this;
    }

	boolean isMarkedAsIgnore(String folder){
		List<String> ignoredFolders = List.of(
				"src/main/resources",
				"src/test",
				"/target/",
				".git",
				".idea"
		);

		boolean result = ignoredFolders.stream().anyMatch(folder::contains);
		if(result) System.out.println("[Marked as ignored]");
		return result;
	}

	public List<File> getFolders() {return folders;}
	public void setFolders(List<File> folders) {this.folders = folders;}

	public List<File> getFiles() {return files;}
	public void setFiles(List<File> files) {this.files = files;}

	public List<File> getJavaFiles() {return javaFiles;}
	public void setJavaFiles(List<File> javaFiles) {this.javaFiles = javaFiles;}
}
