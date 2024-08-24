package com.codetree.codetree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
    private NodeType nodeType;
    private String name;
    private String fullPath;
    private JavaFile javaFile;
    private List<String> lines;
    private List<Node> children = new ArrayList<>();

    public void addChildren(List<Node> nodes){
        this.children.addAll(nodes);
    }

    public String toMermaid(){
        String result = new String();
        if(nodeType == NodeType.FOLDER){
            //cria seu namespace e quais são seus children
            //depois, cada children faz o mesmo
            result += String.format("\nnamespace %s {", this.name);
            for(Node child : children){
                result  += String.format("\n  class %s", child.name);
            }
            result  += "\n}\n";

            for(Node child : children){
                result += child.toMermaid();
            }
        }

        if(nodeType == NodeType.FILE){
            result += this.javaFile.toMermaid();
            result += "\n";
        }
        return result;
    }
}
