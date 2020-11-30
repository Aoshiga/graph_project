package m1graf2020;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Classe de gestion de graphs
 * @author Baulard Guillaume - Buronfosse Titouan
 */
public class Graf {
    //Map<Node, List<Node>> adjList;
    TreeMap<Node, List<Node>> adjList;
    List<Edge> edgeList;

    public Graf() {
        adjList = new TreeMap<>();
        edgeList = new ArrayList<>();
    }

    /**
     * Builds a directed graph from an int array representing a Successor Array Formalism
     * @param sa an int array in the Successor Array formalism
     */
    public Graf(int... sa) {
        adjList = new TreeMap<>();
        edgeList = new ArrayList<>();
        int from = 1;
        addNode(from);
        for (int i = 0; i < sa.length; i++)  {
            if (i == sa.length -1) break;
            if (sa[i] != 0) {
                //if(!getSuccessors(from).contains(new Node(sa[i]))) getSuccessors(from).add(new Node(sa[i]));
                if(!adjList.get(new Node(from)).contains(new Node(sa[i]))) adjList.get(new Node(from)).add(new Node(sa[i]));
                edgeList.add(new Edge(new Node(from), new Node(sa[i])));
            } else {
                addNode(++from);
            }
        }
    }

    /**
     * Builds a directed graph or a flow network from a Dot file
     * @param file file to be read, must be .dot file
     * @throws IOException if the file was not found or if it is not .dot
     */
    public Graf(File file) throws IOException {
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

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public TreeMap<Node, List<Node>> getAdjList() {
        return adjList;
    }

    /**
     * Adds a node to the graph
     * @param n node to be added
     */
    public void addNode(Node n) {
        adjList.put(n, new ArrayList<>());
    }

    /**
     * Adds a node to the graph, creates it from the id parameter
     * @param id number of the node to be added
     */
    public void addNode(int id) {
        adjList.put(new Node(id), new ArrayList<>());
    }

    /**
     * Gets a node in the graph from its int id
     * @param id the int id representing the node
     * @return the node found or null if not
     */
    public Node getNode(int id) {
        Node n = new Node(id);
        if (adjList.containsKey(n)) return n;
        else return null;
    }

    /**
     * Gets the list of all the successors of a node n in the graph, as in the nodes with an edge from n
     * @param n node whose successors we want
     * @return the list of successors
     */
    public List<Node> getSuccessors(Node n) {
        return adjList.get(n);
    }

    /**
     * Gets the list of all the successors of a node n in the graph (from an int id), as in the nodes with an edge from n
     * @param id int representing the node whose successors we want
     * @return the list of successors
     */
    public List<Node> getSuccessors(int id) {
        return adjList.get(new Node(id));
    }

    /**
     * Gets the number of nodes in the graph
     * @return the
     */
    public int nbNodes() { return adjList.keySet().size(); }

    /**
     * Tests if the graph contains a node
     * @param n node we search for in the graph
     * @return true if the graph contains the node, false if not
     */
    public boolean existsNode(Node n) {
        return adjList.containsKey(n);
    }

    /**
     * Tests if the graph contains a node
     * @param id int id representing the node we search for in the graph
     * @return true if the graph contains the node, false if not
     */
    public boolean existsNode(int id) {
        return adjList.containsKey(new Node(id));
    }

    /**
     * Removes a node from the graph
     * @param id int id representing the node to be removed
     */
    public void removeNode(int id) {
        Set<Edge> toDelete = new HashSet<>();
        for (Edge e : edgeList) {
            if (e.getTo().getId() == id || e.getFrom().getId() == id) {
                toDelete.add(e);
            }
        }
        //Must created a second iteration to avoid ConcurrentModificationException
        for (Edge e : toDelete) {
            removeEdge(e);
        }

        adjList.remove(new Node(id));

        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            entry.getValue().remove(new Node(id));
        }
    }

    /**
     * Removes a node from the graph
     * @param n node to be removed
     */
    public void removeNode(Node n) {
        removeNode(n.getId());
    }

    /**
     * Tests the adjacency of two nodes, as in if the graph contains an edge from one to the other
     * @param u a node
     * @param v another node
     * @return true if the nodes are adjacent, false if not
     */
    public boolean adjacent(Node u, Node v) {
        return adjList.get(u).contains(v) || adjList.get(v).contains(u);
    }

    /**
     * Tests the adjacency of two nodes, as in if the graph contains an edge from one to the other
     * @param u_id an int id representing a node
     * @param v_id an int id representing another node
     * @return true if the nodes are adjacent, false if not
     */
    public boolean adjacent(int u_id, int v_id) {
        return getSuccessors(new Node(u_id)).contains(new Node(v_id)) || getSuccessors(new Node(v_id)).contains(new Node(u_id));
    }

    /**
     * Returns a List of all the nodes in the graph
     * @return a List of all the nodes in the graph
     */
    public List<Node> getAllNodes() {
        Set<Node> keys = adjList.keySet();
        return new ArrayList<>(keys);
    }

    /**
     * Gets the total number of edges in the graph
     * @returnthe total number of edges in the graph
     */
    public int nbEdges() { return edgeList.size(); }

    /**
     * Tests if the graph contains an edge from one node to another
     * @param u a node
     * @param v another node
     * @return true if there is an edge between the two nodes, false if not
     */
    public boolean existsEdge(Node u, Node v) {
        if (getNode(u.getId()) == null || getNode(v.getId()) == null) return false;

        return adjacent(u, v);
    }

    /**
     * Tests if the graph contains an edge from one node to another
     * @param u_id int id representing a node
     * @param v_id int id representing another node
     * @return true if there is an edge between the two nodes, false if not
     */
    public boolean existsEdge(int u_id, int v_id) {
        if (getNode(u_id) == null || getNode(v_id) == null) return false;
        return adjacent(u_id, v_id);
    }

    /**
     * Tests if the graph contains an edge from one node to another
     * @param e an edge between two nodes
     * @return true if the graph contains the edge, false if not
     */
    public boolean existsEdge(Edge e) {
        if (getNode(e.getFrom().getId()) == null || getNode(e.getTo().getId()) == null) return false;

        return edgeList.contains(e);
    }

    /**
     * Adds an edge fromm one node to another, adds the nodes to the graph if needed
     * @param from a node
     * @param to another node
     */
    void addEdge(Node from, Node to) {
        if(existsNode(from)) addNode(from);
        if(existsNode(to)) addNode(to);
        edgeList.add(new Edge(from, to));
        getSuccessors(from.getId()).add(to);
    }

    /**
     * Adds an edge fromm one node to another, adds the nodes to the graph if needed
     * @param from_id int id representing a node
     * @param to_id int id representing another node
     */
    public void addEdge(int from_id, int to_id) {
        if(getNode(from_id) == null) addNode(from_id);
        if(getNode(to_id) == null) addNode(to_id);
        edgeList.add(new Edge(new Node(from_id), new Node(to_id)));
        getSuccessors(from_id).add(new Node(to_id));
    }

    /**
     * Adds an edge fromm one node to another, adds the nodes to the graph if needed
     * @param from_id int id representing a node
     * @param to_id int id representing another node
     */
    public void addEdge(int from_id, int to_id, int weight) {
        if(getNode(from_id) == null) addNode(from_id);
        if(getNode(to_id) == null) addNode(to_id);
        edgeList.add(new Edge(new Node(from_id), new Node(to_id), weight));
        getSuccessors(from_id).add(new Node(to_id));
    }

    /**
     * Adds an edge fromm one node to another, adds the nodes to the graph if needed
     * @param e edge to be added
     */
    void addEdge(Edge e) {
        if(getNode(e.getFrom().getId()) == null) addNode(e.getFrom());
        if(getNode(e.getTo().getId()) == null) addNode(e.getTo());
        edgeList.add(e);
        getSuccessors(e.getFrom().getId()).add(e.getTo());
    }

    /**
     * Removes an edge from a node to another from the graph, does not remove the nodes
     * @param from a node
     * @param to another node
     */
    public void removeEdge(Node from, Node to) {
        getSuccessors(from).remove(to);
        edgeList.removeIf(e -> e.getFrom() == from && e.getTo() == to);
    }

    /**
     * Removes an edge from a node to another from the graph, does not remove the nodes
     * @param from_id int id representing a node
     * @param to_id int id representing another node
     */
    public void removeEdge(int from_id, int to_id) {
        if (getNode(from_id) != null && getNode(to_id) != null) {
            getSuccessors(from_id).remove(new Node(to_id));
            edgeList.removeIf(e -> e.getFrom().getId() == from_id && e.getTo().getId() == to_id);
        }
    }

    /**
     * Removes an edge from a node to another from the graph, does not remove the nodes
     * @param e edge to be removed
     */
    public void removeEdge(Edge e) {
        if(getSuccessors(e.getFrom()) != null) {
            getSuccessors(e.getFrom()).remove(e.getTo());
            edgeList.remove(e);
        }
    }

    /**
     * Gets a list of all the edges leaving a node in the graph
     * @param n a node in the graph
     * @return the list of all the edges leaving the node
     */
    public List<Edge> getOutEdges(Node n) {
        return getOutEdges(n.getId());
    }

    /**
     * Gets a list of all the edges leaving a node in the graph
     * @param id int id representing a node in the graph
     * @return the list of all the edges leaving the node
     */
    public List<Edge> getOutEdges(int id) {
        List<Edge> outEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (e.getFrom().getId() == id) {
                outEdges.add(e);
            }
        }
        return outEdges;
    }

    /**
     * Gets a list of all the edges coming to a node in the graph
     * @param n a node in the graph
     * @return the list of all the edges coming to the node
     */
    public List<Edge> getInEdges(Node n) {
        return getInEdges(n.getId());
    }

    /**
     * Gets a list of all the edges coming to a node in the graph
     * @param id int id representing a node in the graph
     * @return the list of all the edges coming to the node
     */
    public List<Edge> getInEdges(int id) {
        List<Edge> inEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (e.getTo().getId() == id) {
                inEdges.add(e);
            }
        }
        return inEdges;
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

    public Edge getEdge(int from_id, int to_id) {
        for (Edge e : edgeList) {
            if (e.getTo().getId() == to_id && e.getFrom().getId() == from_id) {
                return e;
            }
        }
        return null;
    }

    /**
     * Gets all the edges in the graph
     * @return a list of all the edges in the graph
     */
    public List<Edge> getAllEdges() {
        return edgeList;
    }

    /**
     * Gets the number of edges coming to a node
     * @param n a node
     * @return an int representing the number of edges coming to a node
     */
    public int inDegree(Node n) {
        int inDegree = 0;
        for (Edge e : edgeList) {
            if (e.getTo().getId() == n.getId()) {
                inDegree++;
            }
        }
        return inDegree;
    }

    /**
     * Gets the number of edges coming to a node
     * @param id int id representing a node
     * @return an int representing the number of edges coming to a node
     */
    public int inDegree(int id) {
        int inDegree = 0;
        for (Edge e : edgeList) {
            if (e.getTo().getId() == id) inDegree++;
        }
        return inDegree;
    }

    /**
     * Gets the number of edges leaving a node
     * @param n a node
     * @return an int representing the number of edges leaving a node
     */
    public int outDegree(Node n) {
        return getSuccessors(n).size();
    }

    /**
     * Gets the number of edges leaving a node
     * @param id int id representing a node
     * @return an int representing the number of edges leaving a node
     */
    public int outDegree(int id) {
        return getSuccessors(id).size();
    }

    /**
     * Gets the number of edges both leaving and coming to a node
     * @param n a node
     * @return an int representing the number of edges both leaving and coming to a node
     */
    public int degree(Node n) {
        return inDegree(n) + outDegree(n);
    }

    /**
     * Gets the number of edges both leaving and coming to a node
     * @param id int id representing a node
     * @return an int representing the number of edges both leaving and coming to a node
     */
    public int degree(int id) {
        return inDegree(id) + outDegree(id);
    }

    /**
     * Builds an int array representing the graph in the Successor Array formalism
     * @return an int array representing the graph in the Successor Array formalism
     */
    public int[] toSuccessorArray() {
        List<Integer> sa_list = new ArrayList<>();
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            Collections.sort(entry.getValue());
            for (Node n : entry.getValue()) {
                sa_list.add(n.getId());
            }
            sa_list.add(0);
        }

        return sa_list.stream().mapToInt(i->i).toArray();
    }

    /**
     * Builds a two-dimensional int array representing the graph in the adjacency matrix formalism
     * @return a two-dimensional int array representing the graph in the adjacency matrix formalism
     */
    public int[][] toAdjMatrix() {
        int[][] adjMatrix = new int[nbNodes()][nbNodes()];
        for (int[] matrix : adjMatrix) {
            Arrays.fill(matrix, 0);
        }
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            int node_id = entry.getKey().getId();
            for (Node n : entry.getValue()) {
                adjMatrix[node_id -1][n.getId() -1] = 1;
            }
        }
        return adjMatrix;
    }

    /**
     * Builds a two-dimensional int array representing the graph in the adjacency matrix formalism, reversed
     * @return a two-dimensional int array representing the graph in the adjacency matrix formalism, reversed
     */
    public Graf getReverse() {
        Graf reverse = new Graf();
        for(Node n : getAllNodes()) {
            reverse.addNode(n);
        }

        List<Node> success;
        for(Node n : this.getAllNodes()) {
            success = this.getSuccessors(n);
            for (Node s: success) {
                reverse.addEdge(s.getId(), n.getId());
            }
        }
        return reverse;
    }

    /**
     * Builds a copy of the graph where all currently mutually reachable nodes have a direct edge between them
     * @return a copy of the graph where all currently mutually reachable nodes have a direct edge between them
     */
    public Graf getTransitiveClosure() {
        Graf g = new Graf(this.toSuccessorArray());
        List<Node> nodes = getAllNodes();
        for (Node u : nodes) {
            List<Node> predecessors = new ArrayList<>();
            for (Edge e: getInEdges(u)) {
                predecessors.add(e.getFrom());
            }

            for (Node p : predecessors) {
                for (Node s : getSuccessors(u)) {
                    g.addEdge(p.getId(), s.getId());
                }
            }
        }
        return g;
    }

    public enum color{WHITE, GREY, BLACK}

    /**
     * Computes a breadth-first-search of the graph
     * @return a list of nodes representing a breadth-first-search of the graph in order
     */
    public List<Node> getBFS() {
        List<Node> bfs = new ArrayList<>();
        Map<Node, Integer> index = new HashMap<>();
        color[] color = new color[adjList.size()];

        int cpt = 0;
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            index.put(entry.getKey(), cpt);
            color[cpt] = Graf.color.WHITE;
            cpt++;
        }

        color[0] = Graf.color.GREY;
        //PriorityQueue doesn't conserve the order : use Linked instead
        LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>();
        queue.add(new Node(1));

        while (!queue.isEmpty()) {
            Node u = queue.poll();
            for (Node n : getSuccessors(u)) {
                if (color[index.get(n)] == Graf.color.WHITE) {
                    color[index.get(n)] = Graf.color.GREY;
                    queue.add(n);
                }
            }
            color[index.get(u)] = Graf.color.BLACK;
            bfs.add(u);
        }

        return bfs;
    }

    /**
     * Computes a depth-first-search of the graph
     * @return a list of nodes representing a depth-first-search of the graph in order
     */
    public List<Node> getDFS() {
        List<Node> dfs = new ArrayList<>();
        Map<Node, Integer> index = new HashMap<>();
        color[] color = new color[adjList.size()];

        int cpt = 0;
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            index.put(entry.getKey(), cpt);
            color[cpt] = Graf.color.WHITE;
            cpt++;
        }

        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            if (color[index.get(entry.getKey())] == Graf.color.WHITE) {
                dfs_visit(dfs, entry.getKey(), color, index);
            }
        }
        return dfs;
    }

    /**
     * Recursively searches (depth-first) from a node
     */
    public void dfs_visit(List<Node> dfs, Node u, color[] color, Map<Node, Integer> index) {
        color[index.get(u)] = Graf.color.GREY;
        for (Node v : getSuccessors(u)) {
            if (color[index.get(v)] == Graf.color.WHITE) {
                dfs_visit(dfs, v, color, index);
            }
        }
        color[index.get(u)] = Graf.color.BLACK;
        dfs.add(u);
    }

    /**
     * Returns a String representing the graph in the DOT formalism
     * @return a String representing the graph in the DOT formalism
     */
    public String toDotString() {
        //TreeMap<Node, List<Node>> sorted = new TreeMap<>(adjList);

        StringBuilder dot = new StringBuilder("digraph {\n");
        //for (Map.Entry<Node, List<Node>> entry : sorted.entrySet()) {
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
                dot.append("\t").append(entry.getKey().getId());
            if(!entry.getValue().isEmpty()) dot.append(" -> ");
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

    /**
     * Exports the graph in a .dot file in the DOT formalism
     */
    public void toDotFile(String fileName){
        /* if filename is empty (""), give a default filename */
        if(fileName.equals("")) fileName = "digraph";

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

    /**
     *
     * @param nbrOfVertices
     * @return an array of int in SA format
     */
    public static int[] randomGraph(int nbrOfVertices) {
        List<Integer> sa = new ArrayList<>();
        List<Integer> sa_edge = new ArrayList<>(); //eliminate double
        int rand;
        int rand_edge;
        for (int i = 0; i < nbrOfVertices; ++i) {
            rand = 1 + (int) (Math.random() * ((nbrOfVertices - 1) + 1));
            for (int j = 1; j < rand; j++) {
                rand_edge = 1 + (int) (Math.random() * ((nbrOfVertices - 1) + 1));
                if (!sa_edge.contains(rand_edge)) sa_edge.add(rand_edge);
            }
            sa.addAll(sa_edge);
            sa_edge.clear();
            sa.add(0);
        }
        return sa.stream().mapToInt(i -> i).toArray();
    }


}