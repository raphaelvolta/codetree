package com.codetree.codetree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeService {

    Compiler compiler = new Compiler();
    public Node read(String path) throws IOException {
        File file = new File(path);
        return fromFile(file);
    }

    public Node fromFile(File file) throws IOException {
        NodeType nodeType = getNodeType(file);
        Node node = new Node();
        node.setNodeType(nodeType);
        node.setName(file.getName().replace(".java", ""));
        node.setFullPath(file.getAbsolutePath());
        if(nodeType == NodeType.FILE){
            List<String> lines = readLines(file);
            node.setLines(lines);
            node.setJavaFile(compiler.fileAnalyze(lines, false, file.getName()));
        }

        if(file.listFiles() != null) {
            node.addChildren(fromFiles(file.listFiles()));
        }

        return node;
    }

    public List<Node> fromFiles(File[] files) throws IOException {
        List<Node> result = new ArrayList<>();
        for (File file : files) {
            result.add(fromFile(file));
        }
        return result;
    }

    private static NodeType getNodeType(File scan){
        if(scan.isFile()) return NodeType.FILE;
        if(scan.isDirectory()) return NodeType.FOLDER;
        throw new RuntimeException("Could not determine NodeType of: " + scan);
    }

    public List<String> readLines(File file) throws IOException {
        List<String> allLines = Files.readAllLines(Paths.get(file.getPath()));
        String text = new String();
        for(String line : allLines) text += line;
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(text.split("(?<=;|\\{|\\})")));
        return result;
    }
}
