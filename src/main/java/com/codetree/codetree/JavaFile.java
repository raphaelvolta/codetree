package com.codetree.codetree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JavaFile {
	private String fileName;
	private Long id;
	private String _package;

	private List<String> _imports = new ArrayList<>();
	private List<String> classes = new ArrayList<>();
	private List<String> interfaces = new ArrayList<>();

	private List<String> extendsClasses = new ArrayList<>();
	private List<String> implementsClasses = new ArrayList<>();

	private List<String> publicConstructors = new ArrayList<>();
	private List<String> privateConstructors = new ArrayList<>();

	private List<String> privateAttributes = new ArrayList<>();
	private List<String> defaultAttributes = new ArrayList<>();
	private List<String> protectedAttributes = new ArrayList<>();
	private List<String> publicAttributes = new ArrayList<>();
	
	private List<String> privateMethods = new ArrayList<>();
	private List<String> defaultMethods = new ArrayList<>();
	private List<String> protectedMethods = new ArrayList<>();
	private List<String> publicMethods = new ArrayList<>();

	public String toMermaid(){
		String result = "";
		boolean isClass = false;
		boolean isInterface = false;
		boolean hasExtends = false;
		boolean hasImplements = false;

		if(!classes.isEmpty()) isClass = true;
		if(!interfaces.isEmpty()) isInterface = true;

		if(!extendsClasses.isEmpty()) hasExtends = true;
		if(!implementsClasses.isEmpty()) hasImplements = true;

		if((isClass || isInterface) && hasExtends){
			result += extendsClasses.get(0) + " <|.. " + classes.get(0);
		}

		if((isClass || isInterface) && hasImplements) {
			result += classes.get(0) + " <.. " + implementsClasses.get(0);
		}

		if(!hasExtends && !hasImplements) result += "class " + classes.get(0);

		return result;
	}
}