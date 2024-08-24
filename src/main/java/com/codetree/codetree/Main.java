package com.codetree.codetree;

import java.io.IOException;

public class Main {


	public static void main(String[] args) throws IOException {
		String projectPath = "e:\\Repositorios\\ControleFinanceiro\\src\\main\\java"; //change here
		Node node = new NodeService().read(projectPath);
		System.out.println("classDiagram");
		System.out.println(node.toMermaid());
	}
}
