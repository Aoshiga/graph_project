package m1graf2020;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Use inEdge and outEdge
 */
public class UndirectedGraf extends Graf{

    UndirectedGraf() {
        super();
    }

    UndirectedGraf(int ... sa) {
        super(sa);

        int from = 1;
        for (int i = 0; i < sa.length; i++)  {
            if (i == sa.length -1) break;
            if (sa[i] != 0) {
                if(!adjList.get(new Node(sa[i])).contains(new Node(from))) adjList.get(new Node(sa[i])).add(new Node(from));
            } else ++from;
        }
    }

    public String toDotString() {
        StringBuilder dot = new StringBuilder("graph {\n");

        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            dot.append("\t").append(entry.getKey().getId());
            if(!entry.getValue().isEmpty()) dot.append(" -- ");
            Collections.sort(entry.getValue());
            for (Node n : entry.getValue()) {
                dot.append(n.getId()).append(", ");
            }
            if (dot.charAt(dot.length() -1) == ' ') {
                dot.setLength(dot.length() -2);
            }
            dot.append(";\n");
        }
        dot.append("}");
        return dot.toString();
    }

    void toDotFile(String fileName){
        //if filename is empty (""), give a default filename
        if(fileName.equals("")) fileName = "graph";

        try {
            String dot = this.toDotString();
            FileWriter fw = new FileWriter(fileName + ".dot");
            fw.write(dot);
            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
