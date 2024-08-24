package com.codetree.codetree;

import java.util.List;

public class GroupManager {
    public static void add(List<NameSpaceGroup> group, JavaFile javaFile) {
        if(group.isEmpty()){
            group.add(new NameSpaceGroup(javaFile.get_package(), javaFile));
        }
        boolean namespaceExists = false;
        for (NameSpaceGroup g : group) {
            if (g.getNameSpace().equals(javaFile.get_package())) {
                g.getJavaFiles().add(javaFile);
                namespaceExists = true;
            }
        }
        if(!namespaceExists){
            group.add(new NameSpaceGroup(javaFile.get_package(), javaFile));
        }
    }
}
