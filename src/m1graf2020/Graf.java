package m1graf2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graf {
    Map<Node, List<Node>> adjList;

    Graf() {
        adjList = new HashMap<>();
    }

    Graf(int ... sa) {
        adjList = new HashMap<>();
        int cpt = 1;
        addNode(cpt);

        for (int i : sa) {
            if(i != 0) {
                getSuccessors(cpt).add(new Node(i));
            }
            else {
                addNode(++cpt);
            }
        }
    }

    public void addNode(Node n) {
        adjList.put(n, new ArrayList<>());
    }

    public void addNode(int id) {
        adjList.put(new Node(id), new ArrayList<>());
    }

    public Node getNode(int id) {
        Node n = new Node(id);
        if (adjList.containsKey(n)) return (Node) adjList.get(n);
        else return null;
    }

    public List<Node> getSuccessors(Node n) {
        return adjList.get(n);
    }

    public List<Node> getSuccessors(int id) {
        return adjList.get(new Node(id));
    }

    public boolean existsNode(Node n) {
        return adjList.containsKey(n);
    }

    public boolean existsNode(int id) {
        return adjList.containsKey(new Node(id));
    }

    public String toDotString() {
        StringBuilder dot = new StringBuilder("digraph name {\n");
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            for (Node n : entry.getValue()) {
                dot.append("\t").append(entry.getKey().getId()).append(" -> ").append(n.getId()).append(";\n");
            }
        }
        dot.append("}");
        return dot.toString();
    }

    public static void main(String[] args) {
        Graf g = new Graf(2, 4, 0, 0, 6, 0, 2, 3, 5, 8, 0, 0, 4, 7, 0, 3, 0, 7, 0);
        System.out.println(g.toDotString());
    }
}
