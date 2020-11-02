package m1graf2020;

import java.io.*;
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

    UndirectedGraf(File file) throws IOException {
        String extension = "";
        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i+1);
        }
        if (!extension.equals("dot")) throw new IOException("File is not .dot");
        adjList = new TreeMap<>();
        edgeList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.readLine();
        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            if (line.contains("}")) break;
            else {
                int node_id = Integer.parseInt(String.valueOf(line.charAt(1)));
                addNode(node_id);
                int index = 6;
                while (index < line.length()) {
                    addEdge(node_id, Integer.parseInt(String.valueOf(line.charAt(index))));
                    index += 3;
                }
                line = reader.readLine();
            }
        }
    }

    void addEdge(Node from, Node to) {
        if(!existsNode(from)) addNode(from);
        if(!existsNode(to)) addNode(to);
        if(!edgeList.contains(new Edge(from, to)) && !edgeList.contains(new Edge(to, from))) edgeList.add(new Edge(from, to));
        if (!getSuccessors(from).contains(to)) getSuccessors(from).add(to);
    }

    void addEdge(int from_id, int to_id) {
        if(getNode(from_id) == null) addNode(from_id);
        if(getNode(to_id) == null) addNode(to_id);
        if(!edgeList.contains(new Edge(from_id, to_id)) && !edgeList.contains(new Edge(to_id, from_id))) edgeList.add(new Edge(new Node(from_id), new Node(to_id)));
        if (!getSuccessors(from_id).contains(new Node(to_id))) getSuccessors(from_id).add(new Node(to_id));
    }

    void addEdge(Edge e) {
        if(getNode(e.getFrom().getId()) == null) addNode(e.getFrom());
        if(getNode(e.getTo().getId()) == null) addNode(e.getTo());
        if(!edgeList.contains(e) && !edgeList.contains(new Edge(e.getTo(), e.getFrom()))) edgeList.add(e);
        getSuccessors(e.getFrom()).add(e.getTo());
    }

    public List<Edge> getOutEdges(Node n) {
        return getIncidentEdges(n);
    }

    public List<Edge> getOutEdges(int id) {
        return getIncidentEdges(id);
    }

    public List<Edge> getInEdges(Node n) {
        return getIncidentEdges(n);
    }

    public List<Edge> getInEdges(int id) {
        return getIncidentEdges(id);
    }

    public List<Edge> getIncidentEdges(Node n) {
        return getIncidentEdges(n.getId());
    }

    public List<Edge> getIncidentEdges(int id) {
        List<Edge> incidentEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (e.getTo().getId() == id || e.getFrom().getId() == id) {
                incidentEdges.add(e);
            }
        }
        return incidentEdges;
    }

    public int inDegree(Node n) {
        return getSuccessors(n).size();
    }

    public int inDegree(int id) {
        return getSuccessors(id).size();
    }

    public int degree(Node n) {
        return getSuccessors(n).size();
    }

    public int degree(int id) {
        return getSuccessors(id).size();
    }

    public UndirectedGraf getReverse() {
        return this;
    }

    public UndirectedGraf getTransitiveClosure() {
        UndirectedGraf g = this;
        UndirectedGraf reverse = getReverse();
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            List<Node> predecessors = reverse.getSuccessors(entry.getKey());
            for (Node p : predecessors) {
                for (Node s : entry.getValue()) {
                    g.addEdge(p, s);
                }
            }
        }
        return g;
    }
    // TO DO : getTransitive

    public String toDotString() {
        StringBuilder dot = new StringBuilder("graph {\n");
        boolean once = true;

        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            dot.append("\t").append(entry.getKey().getId());
            Collections.sort(entry.getValue());

            for (Node n : entry.getValue()) {
                if(!(n.getId() < entry.getKey().getId())){
                    if(!entry.getValue().isEmpty() && once){
                        dot.append(" -- ");
                        once = false;
                    }
                    dot.append(n.getId()).append(", ");
                }
            }
            once = true;
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
