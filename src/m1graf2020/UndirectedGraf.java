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

    /**
     * Builds an undirected graph from an int array representing a Successor Array Formalism
     * @param sa an int array in the Successor Array formalism
     */
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

    /**
     * Builds an undirected graph from a Dot file
     * @param file file to be read, must be .dot file
     * @throws IOException if the file was not found or if it is not .dot
     */
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

    /**
     * Adds an edge fromm one node to another, adds the nodes to the graph if needed
     * @param from a node
     * @param to another node
     */
    void addEdge(Node from, Node to) {
        if(!existsNode(from)) addNode(from);
        if(!existsNode(to)) addNode(to);
        if(!edgeList.contains(new Edge(from, to)) && !edgeList.contains(new Edge(to, from))) edgeList.add(new Edge(from, to));
        if (!getSuccessors(from).contains(to)) getSuccessors(from).add(to);
    }

    /**
     * Adds an edge fromm one node to another, adds the nodes to the graph if needed
     * @param from_id int id representing a node
     * @param to_id int id representing another node
     */
    void addEdge(int from_id, int to_id) {
        if(getNode(from_id) == null) addNode(from_id);
        if(getNode(to_id) == null) addNode(to_id);
        if(!edgeList.contains(new Edge(from_id, to_id)) && !edgeList.contains(new Edge(to_id, from_id))) edgeList.add(new Edge(new Node(from_id), new Node(to_id)));
        if (!getSuccessors(from_id).contains(new Node(to_id))) getSuccessors(from_id).add(new Node(to_id));
    }

    /**
     * Adds an edge fromm one node to another, adds the nodes to the graph if needed
     * @param e edge to be added
     */
    void addEdge(Edge e) {
        if(getNode(e.getFrom().getId()) == null) addNode(e.getFrom());
        if(getNode(e.getTo().getId()) == null) addNode(e.getTo());
        if(!edgeList.contains(e) && !edgeList.contains(new Edge(e.getTo(), e.getFrom()))) edgeList.add(e);
        getSuccessors(e.getFrom()).add(e.getTo());
    }

    /**
     * Gets a list of all the edges leaving a node in the graph
     * @param n a node in the graph
     * @return the list of all the edges leaving the node
     */
    public List<Edge> getOutEdges(Node n) {
        return getIncidentEdges(n);
    }

    /**
     * Gets a list of all the edges leaving a node in the graph
     * @param id int id representing a node in the graph
     * @return the list of all the edges leaving the node
     */
    public List<Edge> getOutEdges(int id) {
        return getIncidentEdges(id);
    }

    /**
     * Gets a list of all the edges coming to a node in the graph
     * @param n a node in the graph
     * @return the list of all the edges coming to the node
     */
    public List<Edge> getInEdges(Node n) {
        return getIncidentEdges(n);
    }

    /**
     * Gets a list of all the edges coming to a node in the graph
     * @param id int id representing a node in the graph
     * @return the list of all the edges coming to the node
     */
    public List<Edge> getInEdges(int id) {
        return getIncidentEdges(id);
    }

    /**
     * Gets a list of all the edges leaving and coming to a node in the graph
     * @param n a node in the graph
     * @return the list of all the edges leaving and coming to the node
     */
    public List<Edge> getIncidentEdges(Node n) {
        return getIncidentEdges(n.getId());
    }

    /**
     * Gets a list of all the edges leaving and coming to a node in the graph
     * @param id int id representing a node in the graph
     * @return the list of all the edges leaving and coming to the node
     */
    public List<Edge> getIncidentEdges(int id) {
        List<Edge> incidentEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (e.getTo().getId() == id || e.getFrom().getId() == id) {
                incidentEdges.add(e);
            }
        }
        return incidentEdges;
    }

    /**
     * Gets the number of edges coming to a node
     * @param n a node
     * @return an int representing the number of edges coming to a node
     */
    public int inDegree(Node n) {
        return getSuccessors(n).size();
    }

    /**
     * Gets the number of edges coming to a node
     * @param id int id representing a node
     * @return an int representing the number of edges coming to a node
     */
    public int inDegree(int id) {
        return getSuccessors(id).size();
    }

    /**
     * Gets the number of edges both leaving and coming to a node
     * @param n a node
     * @return an int representing the number of edges both leaving and coming to a node
     */
    public int degree(Node n) {
        return getSuccessors(n).size();
    }

    /**
     * Gets the number of edges both leaving and coming to a node
     * @param id int id representing a node
     * @return an int representing the number of edges both leaving and coming to a node
     */
    public int degree(int id) {
        return getSuccessors(id).size();
    }

    /**
     * Builds a two-dimensional int array representing the graph in the adjacency matrix formalism, reversed
     * @return a two-dimensional int array representing the graph in the adjacency matrix formalism, reversed
     */
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

    /**
     * Returns a String representing the graph in the DOT formalism
     * @return a String representing the graph in the DOT formalism
     */
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

    /**
     * Exports the graph in a .dot file in the DOT formalism
     */
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
