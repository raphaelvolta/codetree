package com.codetree.codetree;

import java.util.ArrayList;
import java.util.List;

public class NameSpaceGroup {
    private String nameSpace;
    private List<JavaFile> javaFiles;

    public NameSpaceGroup(String nameSpace, List<JavaFile> javaFiles){
        this.nameSpace = nameSpace;
        this.javaFiles = javaFiles;
    }

    public NameSpaceGroup(String nameSpace, JavaFile javaFile){
        this.nameSpace = nameSpace;
        this.javaFiles = new ArrayList<>();
        this.javaFiles.add(javaFile);
    }

    public void add(JavaFile javaFile){
        this.javaFiles.add(javaFile);
    }

    public String getNameSpace(){
        return this.nameSpace;
    }

    public List<JavaFile> getJavaFiles(){
        return this.javaFiles;
    }

    public String toMermaid(){
        //1 Linha para mostrar herança
        //1 Bloco de classe para mostrar metodos e variaveis
        //1 Bloco para mostrar classes do namespace

        String packageName = this.nameSpace
                .replaceAll("package", "")
                .replaceAll(";", "");
        String result = "";
        String first = "namespace " + packageName + "{\n";
        String last = "\n}";
        result += first;
        for(JavaFile j : javaFiles) {
            result += j.toMermaid() + "\n";
        }
        result += last;
        return result;
    }
}
