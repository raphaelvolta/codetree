package com.codetree.codetree;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler {

	private static final String IMPORT = "import ";
	private static final String PACKAGE = "package";
	private static final String CLASS = "class";
	private static final String INTERFACE = "interface";
	private static final String ENUM = "enum";
	private static final String EXTENDS = "extends";
	private static final String IMPLEMENTS = "implements";
	private static final String PRIVATE = "private ";
	private static final String PROTECTED = "protected ";
	private static final String DEFAULT = "default ";
	private static final String PUBLIC = "public ";
	private static final String VOID = "void";
	private static final String GT = ">";
	private static final String LT = "<";

	public JavaFile fileAnalyze(List<String> arquivoTexto, boolean tracert, String fileName) {
		JavaFile clazz = new JavaFile();
		clazz.setFileName(fileName);

		boolean isPackageSet = false;

		for(String linha : arquivoTexto) {
			if(tracert)System.out.println(linha);

			String linhaCorrente = linha.trim();
			if(linhaCorrente.isEmpty()) continue;
			if(!isPackageSet && isPackage(linha)){clazz.set_package(linhaCorrente);isPackageSet=true;continue;}
			if(isImport(linha)) {clazz.get_imports().add(linhaCorrente);continue;}
			setIfClass(linha, clazz);
			setIfInterface(linha, clazz);
			setIfEnum(linha, clazz);
			setIfExtends(linha, clazz);
			setIfImplements(linha, clazz);

			if(setIfConstrutor(linha, clazz)) {continue;}
			
			if(isAtributoPrivado(linha)) 	{clazz.getPrivateAttributes().add(linhaCorrente);continue;}
			if(isAtributoPadrao(linha)) 	{clazz.getDefaultAttributes().add(linhaCorrente);continue;}
			if(isAtributoProtegido(linha)) 	{clazz.getProtectedAttributes().add(linhaCorrente);continue;}
			if(isAtributoPublico(linha)) 	{clazz.getPublicAttributes().add(linhaCorrente);continue;}
			
			if(isMetodoPrivado(linha)) 		{clazz.getPrivateMethods().add(linhaCorrente);continue;}
			if(isMetodoPadrao(linha)) 		{clazz.getDefaultMethods().add(linhaCorrente);continue;}
			if(isMetodoProtegido(linha))	{clazz.getProtectedMethods().add(linhaCorrente);continue;}
			if(isMetodoPublico(linha)) 		{clazz.getPublicMethods().add(linhaCorrente);continue;}
			
		}
		//System.out.println(clazz.toString());
		return clazz;
	}

	private boolean isPackage(String linha) {
		List<String> segmentos = Arrays.asList(linha.split(" "));
		if(segmentos.size() == 2 && linha.contains(PACKAGE)) {return true;}
		return false;
	}

	private boolean isImport(String linha) {
		List<String> segmentos = Arrays.asList(linha.split(" "));
		if(segmentos.size() > 0 && segmentos.get(0).contains(IMPORT)) {return true;}
		return false;
	}

	private boolean setIfClass(String linha, JavaFile clazz) {
		List<String> segments = Arrays.asList(linha.split(" "));
		boolean hasClass = segments.contains(CLASS);
		int classNameIndex = 0;
		boolean result = hasClass && linha.contains(" " + CLASS + " ") && isBloco(linha);
		if(hasClass) {
			classNameIndex = segments.indexOf(CLASS) +1;
			clazz.getClasses().add(segments.get(classNameIndex));
		}
		return result;
	}

	private boolean setIfInterface(String linha, JavaFile clazz) {
		List<String> segments = Arrays.asList(linha.split(" "));
		boolean hasInterface = segments.contains(INTERFACE);
		int interfaceNameIndex = 0;
		if(hasInterface) {interfaceNameIndex = segments.indexOf(INTERFACE) +1;clazz.getClasses().add(segments.get(interfaceNameIndex));}
		return hasInterface && linha.contains(" " + INTERFACE + " ") && isBloco(linha);
	}

//	private boolean hasGeneric(String linha) {
//		List<String> segments = Arrays.asList(linha.split(" "));
//		boolean hasGeneric = segments.stream().anyMatch(seg -> seg.contains(LT) && seg.contains(GT));
//		int classNameIndex = 0;
//		if(hasGeneric) {classNameIndex = segments.indexOf(CLASS) +1;clazz.getClasses().add(segments.get(classNameIndex));}
//		return hasGeneric && linha.contains(" " + CLASS + " ") && isBloco(linha);
//	}

	private boolean setIfEnum(String linha, JavaFile clazz) {
		List<String> segments = Arrays.asList(linha.split(" "));
		boolean hasEnum = segments.contains(ENUM);
		int enumNameIndex = 0;
		if(hasEnum) {enumNameIndex = segments.indexOf(ENUM) +1;clazz.getClasses().add(segments.get(enumNameIndex));}
		return hasEnum && linha.contains(" " + ENUM + " ") && isBloco(linha);
	}

	private boolean setIfExtends(String linha, JavaFile clazz) {
		List<String> segments = Arrays.asList(linha.split(" "));
		boolean hasExtends = segments.contains(EXTENDS);
		int extendsIndex = 0;
		if(hasExtends){
			extendsIndex = segments.indexOf(EXTENDS) +1;
			String extendsClassName = segments.get(extendsIndex).replace("{", "");
			clazz.getExtendsClasses().add(extendsClassName);
		}
		return hasExtends && linha.contains(" " + EXTENDS + " ") && isBloco(linha);
	}

	private boolean setIfImplements(String linha, JavaFile clazz){
		List<String> segments = Arrays.asList(linha.split(" "));
		boolean hasImplements = segments.contains(IMPLEMENTS);
		int implementsIndex = 0;
		if(hasImplements){
			implementsIndex = segments.indexOf(IMPLEMENTS) +1;
			String implementsClassName = segments.get(implementsIndex).replace("{", "");
			clazz.getImplementsClasses().add(implementsClassName);
		}
		return hasImplements && linha.contains(" " + IMPLEMENTS + " ") && isBloco(linha);
	}


	//Arrumar -> quando o metodo retorna o nome da classe
	//		  -> esta caindo como construtor

	public boolean setIfConstrutor(String linha, JavaFile clazz) {
		List<String> segmentos = Arrays.asList(linha.split("(?=\\s|\\()"));
		//String parametros = obtemParametros(linha);
		boolean possuiBloco = isBloco(linha);
		boolean possuiParametros = possuiParametros(linha);
		boolean possuiNomeDaClasse = false;
		for(String segmento : segmentos) {
			if(clazz.getClasses().contains(segmento.trim())) {
				possuiNomeDaClasse = true;
				break;
			}
		}
		if(possuiBloco && possuiNomeDaClasse && possuiParametros) {
			if(linha.contains(PRIVATE)) {
				clazz.getPrivateConstructors().add(linha);
			} else if(linha.contains(PUBLIC)){
				clazz.getPublicConstructors().add(linha);
			}
			return true;
		}	
		return false;
	}

	public boolean isAtributoPrivado(String linha){return linha.contains(PRIVATE) && isComando(linha);}
	public boolean isAtributoPadrao(String linha){
		return (!possuiModificadorDeAcessoExplicito(linha) && linha.contains(DEFAULT) && isComando(linha));
	}
	public boolean isAtributoProtegido(String linha){return linha.contains(PROTECTED) && isComando(linha);}
	public boolean isAtributoPublico(String linha){return linha.contains(PRIVATE) && isComando(linha);}
	
	public boolean isMetodoPrivado(String linha){return linha.contains(PRIVATE) && isBloco(linha);}
	public boolean isMetodoPadrao(String linha){
		return (!possuiModificadorDeAcessoExplicito(linha) && linha.contains(DEFAULT) && isBloco(linha));
	}
	public boolean isMetodoProtegido(String linha){return linha.contains(PROTECTED) && isBloco(linha);}
	public boolean isMetodoPublico(String linha){return linha.contains(PUBLIC) && isBloco(linha);}
	
	public boolean isBloco(String linha) {return linha.contains("{");}
	public boolean isComando(String linha) {return linha.contains(";");}
	public boolean possuiParametros(String linha) {return linha.contains("(") && linha.contains(")");}
	public boolean possuiModificadorDeAcessoExplicito(String linha) {return linha.contains(PRIVATE) || linha.contains(PROTECTED) || linha.contains(PUBLIC);}
	
	private String obtemParametros(String linha) {
		Pattern filtroParametros = Pattern.compile("(\\(.*\\))");
		Matcher matcher = filtroParametros.matcher(linha);
		if (matcher.find()){
			return matcher.group();
		}
		return null;
	}
}