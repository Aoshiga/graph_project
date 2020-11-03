package m1graf2020;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Graf {
    //Map<Node, List<Node>> adjList;
    TreeMap<Node, List<Node>> adjList;
    List<Edge> edgeList;

    Graf() {
        adjList = new TreeMap<>();
        //adjList = new HashMap<>();
        edgeList = new ArrayList<>();
    }

    /**
     * Builds a directed graph from an int array representing a Successor Array Formalism
     * @param sa an int array in the Successor Array formalism
     */
    Graf(int ... sa) {
        adjList = new TreeMap<>();
        //adjList = new HashMap<>();
        edgeList = new ArrayList<>();
        int from = 1;
        addNode(from);
        for (int i = 0; i < sa.length; i++)  {
            if (i == sa.length -1) break;
            if (sa[i] != 0) {
                //if(!getSuccessors(from).contains(new Node(sa[i]))) getSuccessors(from).add(new Node(sa[i]));
                if(!adjList.get(new Node(from)).contains(new Node(sa[i]))) adjList.get(new Node(from)).add(new Node(sa[i]));
                //edgeList.add(new Edge(new Node(from), new Node(sa[i])));
                addEdge(from, sa[i]);
            } else {
                addNode(++from);
            }
        }
    }

    /**
     * Builds a directed graph from a Dot file
     * @param file file to be read, must be .dot file
     * @throws IOException if the file was not found or if it is not .dot
     */
    Graf(File file) throws IOException {
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
        if(!edgeList.contains(new Edge(from, to))) edgeList.add(new Edge(from, to));
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
        if(!edgeList.contains(new Edge(from_id, to_id))) edgeList.add(new Edge(new Node(from_id), new Node(to_id)));
        if (!getSuccessors(from_id).contains(new Node(to_id))) getSuccessors(from_id).add(new Node(to_id));
    }

    /**
     * Adds an edge fromm one node to another, adds the nodes to the graph if needed
     * @param e edge to be added
     */
    void addEdge(Edge e) {
        if(getNode(e.getFrom().getId()) == null) addNode(e.getFrom());
        if(getNode(e.getTo().getId()) == null) addNode(e.getTo());
        if(!edgeList.contains(e)) edgeList.add(e);
        getSuccessors(e.getFrom()).add(e.getTo());
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
        getSuccessors(e.getFrom()).remove(e.getTo());
        edgeList.remove(e);
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
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            for (Node n : entry.getValue()) {
                if (!reverse.adjList.containsKey(n)) {
                    reverse.addNode(n);
                }
                reverse.addEdge(n, entry.getKey());
            }
            if (!reverse.adjList.containsKey(entry.getKey())) {
                reverse.addNode(entry.getKey());
            }
        }
        return reverse;
    }

    /**
     * Builds a copy of the graph where all currently mutually reachable nodes have a direct edge between them
     * @return a copy of the graph where all currently mutually reachable nodes have a direct edge between them
     */
    public Graf getTransitiveClosure() {
        /*Graf g = this;
        Graf reverse = getReverse();
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            List<Node> predecessors = reverse.getSuccessors(entry.getKey());
            for (Node p : predecessors) {
                for (Node s : entry.getValue()) {
                    g.addEdge(p, s);
                }
            }
        }
        return g;*/
        Graf g = this;
        int nbNodes = nbNodes();

        for(int i=1; i<=nbNodes; ++i) {
            for (int j = 1; j <= nbNodes; ++j) {
                for (int k = 1; k <= nbNodes; ++k) {
                    if (edgeList.contains(new Edge(i, j)) || (edgeList.contains(new Edge(i, k)) && edgeList.contains(new Edge(k, j)))) {
                        g.addEdge(i, j);
                    }
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
    void toDotFile(String fileName){
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
     * @param nbrOfEdges
     * @return
     */
    private static int[] randomGraph(int nbrOfEdges) {
        List<Integer> sa = new ArrayList<>();
        List<Integer> sa_edge = new ArrayList<>(); //eliminate double
        int rand;
        int rand_edge;
        for (int i = 0; i < nbrOfEdges; ++i) {
            rand = 1 + (int) (Math.random() * ((nbrOfEdges - 1) + 1));
            for (int j = 1; j < rand; j++) {
                rand_edge = 1 + (int) (Math.random() * ((nbrOfEdges - 1) + 1));
                if (!sa_edge.contains(rand_edge)) sa_edge.add(rand_edge);
            }
            sa.addAll(sa_edge);
            sa_edge.clear();
            sa.add(0);
        }
        return sa.stream().mapToInt(i -> i).toArray();
    }

    public static void main(String[] args) {

        boolean stop = false;
        Map<String, Graf> grafCreate = new HashMap<>();
        String grafName;
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        List<Integer> sa_edge = new ArrayList<>();
        int rand_edge;

        do {
            reader = new Scanner(System.in);

            System.out.println("Enter the number of the instruction you want to follow: ");
            System.out.println("0 : Quit the application");
            System.out.println("1 : Create a graph");
            System.out.println("2 : Modify an existing graph");
            System.out.println("3 : Get information about a graph");
            System.out.println("4 : Graph export");
            System.out.println("5 : Graph representation");
            System.out.println("6 : Graph transformation");
            System.out.println("7 : Graph traversal");

            try {
                int n = -1;
                if(reader.hasNext()) n = reader.nextInt();

                switch (n) {
                    case 0:
                        System.out.println("Goodbye!");
                        stop = true;
                        break;

                    case 1:
                        System.out.println("Choose a kind of graph:");
                        System.out.println("0 : Return to main menu");
                        System.out.println("1 : Empty graph");
                        System.out.println("2 : From SA");
                        System.out.println("3 : From DOT file");
                        System.out.println("4 : Random graph");
                        System.out.println("5 : Random connected graph");
                        System.out.println("6 : Random dense graph");
                        System.out.println("7 : Random sparse graph");

                        int kindOfGraf = reader.nextInt();
                        while (!stop) {
                            stop = true;

                            /* Definition of variable use in the switch */
                            List<Integer> sa = new ArrayList<>();
                            int nbrOfEdges;
                            int[] array;
                            int min;
                            int max;
                            int rand;

                            switch (kindOfGraf) {
                                case 0:
                                    break;

                                case 1:
                                    System.out.println("Give the graph name:");
                                    grafName = reader.next();

                                    System.out.println("Enter 1 : directed graph || Enter 2 : undirected graph");
                                    n = reader.nextInt();
                                    if (n == 1) grafCreate.put(grafName, new Graf());
                                    else if (n == 2) grafCreate.put(grafName, new UndirectedGraf());
                                    else System.out.println("Unknown command : graph not created:");

                                    break;

                                case 2:
                                    System.out.println("Give the graph name:");
                                    grafName = reader.next();

                                    System.out.println("Enter 1 : directed graph || Enter 2 : undirected graph");
                                    n = reader.nextInt();
                                    if (n != 1 && n != 2) {
                                        System.out.println("Unknown command : graph not created:");
                                        break;
                                    }

                                    System.out.println("Give sa with a space between each element. Ex : x x x");
                                    System.out.println("(EOF or non-integer to terminate): ");

                                    while (reader.hasNextInt()) {
                                        sa.add(reader.nextInt());
                                    }

                                    int[] saTab = sa.stream().mapToInt(i -> i).toArray();
                                    if (n == 1) grafCreate.put(grafName, new Graf(saTab));
                                    else if (n == 2) grafCreate.put(grafName, new UndirectedGraf(saTab));
                                    break;

                                case 3:
                                    System.out.println("Give the graph name:");
                                    grafName = reader.next();

                                    System.out.println("Give the file path:");
                                    String path = reader.next();
                                    grafCreate.put(grafName, new Graf(new File(path)));
                                    break;

                                case 4:
                                    System.out.println("Give the graph name:");
                                    grafName = reader.next();
                                    System.out.println("Give the number of edges:");
                                    nbrOfEdges = reader.nextInt();
                                    array = randomGraph(nbrOfEdges);
                                    System.out.println("Enter 1 : directed graph || Enter 2 : undirected graph");
                                    n = reader.nextInt();
                                    if (n == 1) grafCreate.put(grafName, new Graf(array));
                                    else if (n == 2) grafCreate.put(grafName, new UndirectedGraf(array));
                                    else System.out.println("Unknown command : graph not created:");

                                    break;

                                case 5:
                                    System.out.println("Give the graph name:");
                                    grafName = reader.next();
                                    System.out.println("Give the number of edges:");
                                    nbrOfEdges = reader.nextInt();
                                    array = randomGraph(nbrOfEdges);
                                    System.out.println("Enter 1 : directed graph || Enter 2 : undirected graph");
                                    n = reader.nextInt();
                                    if (n == 1) grafCreate.put(grafName, new Graf(array));
                                    else if (n == 2) grafCreate.put(grafName, new UndirectedGraf(array));
                                    else {
                                        System.out.println("Unknown command : graph not created:");
                                        break;
                                    }

                                    List<Node> S = grafCreate.get(grafName).getAllNodes();

                                    rand = (new Random()).nextInt(nbrOfEdges);
                                    Node current_node = S.get(rand);
                                    S.remove(current_node);

                                    while (!S.isEmpty()) {
                                        rand = (new Random()).nextInt((--nbrOfEdges));
                                        Node neighbor_node = S.get(rand);
                                        grafCreate.get(grafName).addEdge(current_node.getId(), neighbor_node.getId());
                                        S.remove(neighbor_node);
                                        current_node = neighbor_node;
                                    }

                                    break;

                                case 6:
                                    System.out.println("Give the graph name:");
                                    grafName = reader.next();
                                    System.out.println("Give the number of edges:");
                                    nbrOfEdges = reader.nextInt();
                                    System.out.println("Enter 1 : directed graph || Enter 2 : undirected graph");
                                    n = reader.nextInt();

                                    sa_edge.clear();
                                    if(n==1) min = (int) Math.ceil(0.75 * nbrOfEdges * nbrOfEdges / nbrOfEdges);
                                    else min = (int) Math.ceil((0.75 * nbrOfEdges * nbrOfEdges)/(2*nbrOfEdges));
                                    max = nbrOfEdges;

                                    for (int i = 0; i < nbrOfEdges; ++i) {
                                        rand = (new Random()).nextInt(max - min + 1) + min;
                                        for (int j = 1; j <= rand; j++) {
                                            rand_edge = (new Random()).nextInt(nbrOfEdges) + 1;
                                            if (!sa_edge.contains(rand_edge)) sa_edge.add(rand_edge);
                                            else --j;
                                        }
                                        sa.addAll(sa_edge);
                                        sa_edge.clear();
                                        sa.add(0);
                                    }
                                    array = sa.stream().mapToInt(i -> i).toArray();

                                    if (n == 1) grafCreate.put(grafName, new Graf(array));
                                    else if (n == 2) grafCreate.put(grafName, new UndirectedGraf(array));
                                    else System.out.println("Unknown command : graph not created:");
                                    break;

                                case 7:
                                    System.out.println("Give the graph name:");
                                    grafName = reader.next();
                                    System.out.println("Give the number of edges:");
                                    nbrOfEdges = reader.nextInt();
                                    System.out.println("Enter 1 : directed graph || Enter 2 : undirected graph");
                                    n = reader.nextInt();

                                    sa_edge.clear();
                                    min = 0;
                                    if(n==1) max = (int) Math.floor(0.25 * nbrOfEdges * nbrOfEdges / nbrOfEdges);
                                    else max = (int) Math.floor((0.25 * nbrOfEdges * nbrOfEdges)/(2*nbrOfEdges));

                                    for (int i = 0; i < nbrOfEdges; ++i) {
                                        rand = (new Random()).nextInt(max - min + 1) + min;
                                        for (int j = 1; j <= rand; j++) {
                                            rand_edge = (new Random()).nextInt(nbrOfEdges) + 1;
                                            if (!sa_edge.contains(rand_edge)) sa_edge.add(rand_edge);
                                            else --j;
                                        }
                                        sa.addAll(sa_edge);
                                        sa_edge.clear();
                                        sa.add(0);
                                    }
                                    array = sa.stream().mapToInt(i -> i).toArray();

                                    if (n == 1) grafCreate.put(grafName, new Graf(array));
                                    else if (n == 2) grafCreate.put(grafName, new UndirectedGraf(array));
                                    else System.out.println("Unknown command : graph not created:");
                                    break;


                                default:
                                    System.out.println("Unknown command, enter a valid command : ");
                                    //stop=false;
                                    break;
                            }
                        }
                        stop = false;
                        break;

                    case 2:
                        System.out.println("Enter the name of the graph you want to modify:");
                        grafName = reader.next();
                        if (!grafCreate.containsKey(grafName)) {
                            System.out.println("This graph doesn't exist. Return to main menu.");
                            break;
                        }

                        while (!stop) {
                            System.out.println("What you want to do:");
                            System.out.println("1 : Add a node");
                            System.out.println("2 : Remove a node");
                            System.out.println("3 : Add an edge");
                            System.out.println("4 : Remove an edge");
                            System.out.println("5 : Return to main menu");
                            int grafToModify = reader.nextInt();
                            int from;

                            switch (grafToModify) {
                                case 1:
                                    System.out.println("Enter the node number. Be careful, if the node already exist, it will be erase and replace!");
                                    grafCreate.get(grafName).addNode(reader.nextInt());
                                    break;
                                case 2:
                                    System.out.println("Enter the number of the node. This action is irreversible!");
                                    grafCreate.get(grafName).removeNode(reader.nextInt());
                                    break;
                                case 3:
                                    System.out.println("Enter the first node:");
                                    from = reader.nextInt();
                                    System.out.println("Enter the second node:");
                                    grafCreate.get(grafName).addEdge(from, reader.nextInt());
                                    break;
                                case 4:
                                    System.out.println("Enter the first node:");
                                    from = reader.nextInt();
                                    System.out.println("Enter the second node:");
                                    grafCreate.get(grafName).removeEdge(from, reader.nextInt());
                                    break;
                                case 5:
                                    stop = true;
                                    break;
                                default:
                                    System.out.println("Unknown command, enter a valid command : ");
                                    break;
                            }
                            System.out.println();
                        }
                        stop = false;
                        break;

                    case 3:
                        System.out.println("Enter the name of the graph:");
                        grafName = reader.next();
                        if (!grafCreate.containsKey(grafName)) {
                            System.out.println("This graph doesn't exist. Return to main menu.");
                            stop = true;
                            break;
                        }

                        while (!stop) {
                            System.out.println("What you want to do:");
                            System.out.println("1 : Get the number of nodes");
                            System.out.println("2 : Get all nodes (list)");
                            System.out.println("3 : Get successor (list)");
                            System.out.println("4 : Check if a node exist");
                            System.out.println("5 : Check if two nodes are adjacent");
                            System.out.println("6 : Get the number of edges");
                            System.out.println("7 : Get all edges (list)");
                            System.out.println("8 : Check if an edge exist");
                            System.out.println("9 : Get the list of all edges leaving node");
                            System.out.println("10 : Get the list of all edges entering node");
                            System.out.println("11 : Get incident edges. This is the union of the out and in edge");
                            System.out.println("12 : For knowing the in-degree of node");
                            System.out.println("13 : For knowing the out-degree of node");
                            System.out.println("14 : For knowing the degree of node");
                            System.out.println("15 : Return to main menu");

                            int grafToModify = reader.nextInt();
                            int node;

                            switch (grafToModify) {
                                case 1:
                                    System.out.println("Graph " + grafName + " has " + grafCreate.get(grafName).nbNodes() + " node(s).");
                                    break;
                                case 2:
                                    System.out.println("Nodes of graph " + grafName + ":");
                                    System.out.println(grafCreate.get(grafName).getAllNodes());
                                    break;
                                case 3:
                                    System.out.println("Enter a node number:");
                                    node = reader.nextInt();
                                    System.out.println("Successor of node " + node + ":");
                                    System.out.println(grafCreate.get(grafName).getSuccessors(node));
                                    break;
                                case 4:
                                    System.out.println("Enter a node number:");
                                    if (grafCreate.get(grafName).existsNode(reader.nextInt()))
                                        System.out.println("Node exist");
                                    else System.out.println("Node doesn't exist");
                                    break;
                                case 5:
                                    System.out.println("Number of the first node:");
                                    node = reader.nextInt();
                                    System.out.println("Number of the second node:");
                                    if (grafCreate.get(grafName).adjacent(node, reader.nextInt()))
                                        System.out.println("Nodes are adjacent");
                                    else System.out.println("Nodes are not adjacent");
                                    break;
                                case 6:
                                    System.out.println("Graph " + grafName + " has " + grafCreate.get(grafName).nbEdges() + " edges");
                                    break;
                                case 7:
                                    System.out.println("Edges of graph " + grafName + ":");
                                    System.out.println(grafCreate.get(grafName).getAllEdges());
                                    break;
                                case 8:
                                    System.out.println("Number of the first node:");
                                    node = reader.nextInt();
                                    System.out.println("Number of the second node:");
                                    if (grafCreate.get(grafName).existsEdge(node, reader.nextInt()))
                                        System.out.println("Edge exist");
                                    else System.out.println("Edge doesn't exist");
                                    break;
                                case 9:
                                    System.out.println("Enter a node number:");
                                    System.out.println("The out-edges are : " + grafCreate.get(grafName).getOutEdges(reader.nextInt()));
                                    break;
                                case 10:
                                    System.out.println("Enter a node number:");
                                    System.out.println("The in-edges are : " + grafCreate.get(grafName).getInEdges(reader.nextInt()));
                                    break;
                                case 11:
                                    System.out.println("Enter a node number:");
                                    System.out.println("The incident edge are: " + grafCreate.get(grafName).getIncidentEdges(reader.nextInt()));
                                    break;
                                case 12:
                                    System.out.println("Enter a node number:");
                                    node = reader.nextInt();
                                    System.out.println("The in-degree of node: " + node + "is:" + grafCreate.get(grafName).inDegree(node));
                                    break;
                                case 13:
                                    System.out.println("Enter a node number:");
                                    node = reader.nextInt();
                                    System.out.println("The out-degree of node: " + node + "is:" + grafCreate.get(grafName).outDegree(node));
                                    break;
                                case 14:
                                    System.out.println("Enter a node number:");
                                    node = reader.nextInt();
                                    System.out.println("The degree of node: " + node + "is:" + grafCreate.get(grafName).degree(node));
                                    break;
                                case 15:
                                    stop = true;
                                    break;
                                default:
                                    System.out.println("Unknown command, enter a valid command : ");
                                    break;
                            }

                            System.out.println();
                        }
                        stop = false;
                        break;

                    case 4:
                        System.out.println("Enter the name of the graph you want to print:");
                        grafName = reader.next();
                        if (!grafCreate.containsKey(grafName)) {
                            System.out.println("This graph doesn't exist. Return to main menu.");
                            break;
                        }

                        System.out.println("Enter 1 to show the graph in the DOT file or 2 to export the graph to a DOT file");
                        n = reader.nextInt();
                        if (n == 1) System.out.println(grafCreate.get(grafName).toDotString());
                        else if (n == 2) {
                            grafCreate.get(grafName).toDotFile(grafName);
                        } else System.out.println("Unknown command, return to main menu");
                        break;

                    case 5:
                        System.out.println("Enter the name of the graph you want to print:");
                        grafName = reader.next();
                        if (!grafCreate.containsKey(grafName)) {
                            System.out.println("This graph doesn't exist. Return to main menu.");
                            break;
                        }

                        System.out.println("Enter 1 to get the successor array or 2 to get the adjacency matrix");
                        n = reader.nextInt();
                        if (n == 1) System.out.println(Arrays.toString(grafCreate.get(grafName).toSuccessorArray()));
                        else if (n == 2) {
                            int[][] matrix = grafCreate.get(grafName).toAdjMatrix();
                            for (int[] ints : matrix) {
                                System.out.println(Arrays.toString(ints));
                            }
                        } else System.out.println("Unknown command, return to main menu");
                        break;

                    case 6:
                        System.out.println("Enter the name of the origin graph:");
                        grafName = reader.next();
                        if (!grafCreate.containsKey(grafName)) {
                            System.out.println("This graph doesn't exist. Return to main menu.");
                            break;
                        }
                        System.out.println("Enter the name of the final graph:");
                        String finalGrafName = reader.next();

                        System.out.println("Enter 1 to get the reverse graph of " + grafName + " or 2 to get the transitive closure of " + grafName);
                        n = reader.nextInt();
                        if (n == 1) {
                            grafCreate.put(finalGrafName, grafCreate.get(grafName).getReverse());
                            System.out.println("Graph successfully created");
                        } else if (n == 2) {
                            grafCreate.put(finalGrafName, grafCreate.get(grafName).getTransitiveClosure());
                            System.out.println("Graph successfully created");
                        } else System.out.println("Unknown command, return to main menu");
                        break;

                    case 7:
                        System.out.println("Enter the name of the graph to traversal:");
                        grafName = reader.next();
                        if (!grafCreate.containsKey(grafName)) {
                            System.out.println("This graph doesn't exist. Return to main menu.");
                            break;
                        }

                        System.out.println("Enter 1 to get the DFS traversal of " + grafName + " or 2 to get the BFS traversal of " + grafName);
                        n = reader.nextInt();
                        if (n == 1) System.out.println("DFS : " + grafCreate.get(grafName).getDFS());
                        else if (n == 2) System.out.println("BFS : " + grafCreate.get(grafName).getBFS());
                        else System.out.println("Unknown command, return to main menu");
                        break;

                    default:
                        System.out.println("Unknown command, try again :");
                }
            } catch (InputMismatchException | IOException e) {
                System.out.println("Unknown command, try again :");
            }

        } while(!stop);
        reader.close();





        /*System.out.println(">>>>>>>> Creating the subject example graph in G");
        Graf g = new Graf(2, 4, 0, 0, 6, 0, 2, 3, 5, 8, 0, 0, 4, 7, 0, 3, 0, 7, 0);

        System.out.println(">>>> Graph SA representation");
        System.out.println(Arrays.toString(g.toSuccessorArray()));

        System.out.println(">>>> Graph matrix representation");
        int[][] matrix = g.toAdjMatrix();
        for (int[] ints : matrix) {
            System.out.println(Arrays.toString(ints));
        }

        System.out.println(">>>> Graph information");
        System.out.println(">> DOT representation\n"+g.toDotString());
        g.toDotFile("");
        System.out.println(""+g.nbNodes()+" nodes, "+g.nbEdges()+" edges");
        System.out.println(">> Nodes: ");
        List<Node> nodes = g.getAllNodes();
        Collections.sort(nodes);
        for (Node n: nodes)
            System.out.println("Node "+n+": degree "+g.degree(n)+" (in: "+g.inDegree(n)+", out: "+g.outDegree(n)+")");

        List<Edge> edges;
        System.out.println(">> Edges: ");
        System.out.println("---------------------------");
        System.out.println("As out-edges");
        for (Node n: nodes) {
            edges = g.getOutEdges(n);
            Collections.sort(edges);
            System.out.println(""+n+": "+edges);
        }

        System.out.println("As in-edges");
        for (Node n: nodes) {
            edges = g.getInEdges(n);
            Collections.sort(edges);
            System.out.println(""+n+": "+edges);
        }

        /////////////////////////////////////////////////////

        System.out.println("\n>>>>>>>> creating isolated node 12");
        g.addNode(12);
        System.out.println("Graph now:");
        System.out.println(g.toDotString());
        System.out.println(""+g.nbNodes()+" nodes, "+g.nbEdges()+" edges");
        nodes = g.getAllNodes();
        Collections.sort(nodes);
        System.out.println("Nodes list: "+nodes);

        //// ----------------
        System.out.println("\n>>>>>>>> Removing node 3");
        g.removeNode(3);
        System.out.println("Graph now:");
        System.out.println(g.toDotString());
        System.out.println(""+g.nbNodes()+" nodes, "+g.nbEdges()+" edges");
        nodes = g.getAllNodes();
        Collections.sort(nodes);
        System.out.println("Nodes list: "+nodes);

        System.out.println(">> Edges: ");
        System.out.println("---------------------------");
        System.out.println("As out-edges");
        for (Node n: nodes) {
            edges = g.getOutEdges(n);
            Collections.sort(edges);
            System.out.println(""+n+": "+edges);
        }

        System.out.println("As in-edges");
        for (Node n: nodes) {
            edges = g.getInEdges(n);
            Collections.sort(edges);
            System.out.println(""+n+": "+edges);
        }

        System.out.println("\n>>>>>>>> Recreating edges (4, 3), (3, 6), (7, 3), adding edge (12, 3), creating edge (3, 25)");
        g.addEdge(4, 3);
        g.addEdge(g.getNode(3), g.getNode(6));
        g.addEdge(new Edge(7, 3));
        g.addEdge(new Edge(12, 3));
        g.addEdge(3, 25);
        System.out.println("Graph now:");
        System.out.println(g.toDotString());
        System.out.println(""+g.nbNodes()+" nodes, "+g.nbEdges()+" edges");
        nodes = g.getAllNodes();
        Collections.sort(nodes);
        System.out.println("Nodes list: "+nodes);

        System.out.println("\n>>>>>>>>  Edges removal");
        System.out.println(">>>> Removing existing edges (7, 3) and (4, 8)");
        g.removeEdge(7, 3);
        g.removeEdge(4, 8);
        System.out.println(">>>> Removing absent edge (3, 4)");
        g.removeEdge(3, 4);
        System.out.println(">>>> Removing edges whith 1 or 2 not existing end-points: (-3, 4), (6, 0), (4, 11), (-1, -2), (13, 3), (9, 10)");
        g.removeEdge(-3, 4);
        g.removeEdge(new Edge(6, 0));
        g.removeEdge(new Node(4), new Node(11));
        g.removeEdge(-1, -2);
        g.removeEdge(13, 3);
        g.removeEdge(9, 10);

        System.out.println("Graph now:");
        System.out.println(g.toDotString());
        System.out.println(""+g.nbNodes()+" nodes, "+g.nbEdges()+" edges");
        nodes = g.getAllNodes();
        Collections.sort(nodes);
        System.out.println("Nodes list: "+nodes);

        System.out.println("\\n>>>>>>>> adding a self-loop on node 6, and a second edge (1, 4)\"");
        g.addEdge(6, 6);
        g.addEdge(1, 4);
        System.out.println("Graph now:");
        System.out.println(g.toDotString());
        System.out.println(""+g.nbNodes()+" nodes, "+g.nbEdges()+" edges");
        nodes = g.getAllNodes();
        Collections.sort(nodes);
        System.out.println("Nodes list: "+nodes);
        System.out.println("Degree of node 6: "+g.degree(6)+" (in: "+g.inDegree(6)+", out: "+g.outDegree(6)+")");

        System.out.println(">> Edges: ");
        System.out.println("---------------------------");
        System.out.println("As out-edges");
        for (Node n: nodes) {
            edges = g.getOutEdges(n);
            Collections.sort(edges);
            System.out.println(""+n+": "+edges);
        }

        System.out.println("As in-edges");
        for (Node n: nodes) {
            edges = g.getInEdges(n);
            Collections.sort(edges);
            System.out.println(""+n+": "+edges);
        }

        System.out.println(">>>>>>>>>>    Reverse graph");
        System.out.println(g.getReverse().toDotString());

        System.out.println(">>>>>>>>>>    Transitive Closure");
        System.out.println(g.getTransitiveClosure().toDotString());

        System.out.println("\n>>>> Testing incidentEdges");
        System.out.println(g.getIncidentEdges(1));
        System.out.println(g.getIncidentEdges(new Node(4)));

        System.out.println("\n>>>> Get all edges");
        System.out.println(g.getAllEdges());

        System.out.println("\n>>>>>>>>>>    Emptying the graph by removing all its nodes");
        nodes = g.getAllNodes();
        for (Node u: nodes)
            g.removeNode(u);
        System.out.println("Graph now:");
        System.out.println(g.toDotString());

        System.out.println(">>>> Searching for node 7");
        if (g.existsNode(7))
            System.out.println("Node 7 exists");
        else
            System.out.println("There is no Node 7");

        if (g.existsNode(new Node(1)))
            System.out.println("Node 1 exists");
        else
            System.out.println("There is no Node 1");

        System.out.println("\n>>>> Searching for edge (4, 2)");
        if (g.existsEdge(4, 2))
            System.out.println("Edge (4, 2) exists");
        else
            System.out.println("There is no edge (4, 2)");

        if (g.existsEdge(new Node(1), new Node(2)))
            System.out.println("Edge (1, 2) exists");
        else
            System.out.println("There is no edge (1, 2)");

        if (g.existsEdge(new Edge(6, 7)))
            System.out.println("Edge (6, 7) exists");
        else
            System.out.println("There is no edge (6, 7)");

        System.out.println("\n>>>> ----- New graph for bfs and dfs");
        Graf g_bis = new Graf(2, 4, 0, 3, 1, 0, 1, 0, 4, 0);
        System.out.println(">>>> Graph information");
        System.out.println(">> DOT representation\n"+g_bis.toDotString());
        List<Node> l_bfs = g_bis.getBFS();
        System.out.println("BFS : " + Arrays.toString(l_bfs.toArray()));

        List<Node> l_dfs = g_bis.getDFS();
        System.out.println("DFS : " + Arrays.toString(l_dfs.toArray()));



        // -------------------------------------------------
        System.out.println(">>>>>>>> Creating un undirected graphs");
        UndirectedGraf ug = new UndirectedGraf(2, 4, 0, 0, 6, 0, 2, 3, 5, 8, 0, 0, 4, 7, 0, 3, 0, 7, 0);
        System.out.println(">> DOT representation\n"+ug.toDotString());
        ug.toDotFile("UndirectedGraph");*/
    }
}
