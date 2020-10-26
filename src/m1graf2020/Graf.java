package m1graf2020;

import java.io.FileWriter;
import java.io.IOException;
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

    Graf(int ... sa) {
        adjList = new TreeMap<>();
        //adjList = new HashMap<>();
        edgeList = new ArrayList<>();
        int from = 1;
        addNode(from);
        for (int i = 0; i < sa.length; i++)  {
            if (i == sa.length -1) break;
            if (sa[i] != 0) {
                getSuccessors(from).add(new Node(sa[i]));
                edgeList.add(new Edge(new Node(from), new Node(sa[i])));
            } else {
                addNode(++from);
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
        if (adjList.containsKey(n)) return n;
        else return null;
    }

    public List<Node> getSuccessors(Node n) {
        return adjList.get(n);
    }

    public List<Node> getSuccessors(int id) {
        return adjList.get(new Node(id));
    }

    public int nbNodes() { return adjList.keySet().size(); }

    public boolean existsNode(Node n) {
        return adjList.containsKey(n);
    }

    public boolean existsNode(int id) {
        return adjList.containsKey(new Node(id));
    }

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

    public void removeNode(Node n) {
        removeNode(n.getId());
    }

    public boolean adjacent(Node u, Node v) {
        return adjList.get(u).contains(v) || adjList.get(v).contains(u);
    }

    public boolean adjacent(int u_id, int v_id) {
        return getSuccessors(new Node(u_id)).contains(new Node(v_id)) || getSuccessors(new Node(v_id)).contains(new Node(u_id));
    }

    public List<Node> getAllNodes() {
        Set<Node> keys = adjList.keySet();
        return new ArrayList<>(keys);
    }

    public int nbEdges() { return edgeList.size(); }

    public boolean existsEdge(Node u, Node v) {
        if (getNode(u.getId()) == null || getNode(v.getId()) == null) return false;

        return adjacent(u, v);
    }

    public boolean existsEdge(int u_id, int v_id) {
        if (getNode(u_id) == null || getNode(v_id) == null) return false;
        return adjacent(u_id, v_id);
    }

    public boolean existsEdge(Edge e) {
        if (getNode(e.getFrom().getId()) == null || getNode(e.getTo().getId()) == null) return false;

        return edgeList.contains(e);
    }

    void addEdge(Node from, Node to) {
        if(existsNode(from)) addNode(from);
        if(existsNode(to)) addNode(to);
        edgeList.add(new Edge(from, to));
        if (!getSuccessors(from).contains(to)) getSuccessors(from).add(to);
    }

    void addEdge(int from_id, int to_id) {
        if(getNode(from_id) == null) addNode(from_id);
        if(getNode(to_id) == null) addNode(to_id);
        edgeList.add(new Edge(new Node(from_id), new Node(to_id)));
        if (!getSuccessors(from_id).contains(new Node(to_id))) getSuccessors(from_id).add(new Node(to_id));
    }

    void addEdge(Edge e) {
        if(getNode(e.getFrom().getId()) == null) addNode(e.getFrom());
        if(getNode(e.getTo().getId()) == null) addNode(e.getTo());
        edgeList.add(e);
        getSuccessors(e.getFrom()).add(e.getTo());
    }

    public void removeEdge(Node from, Node to) {
        getSuccessors(from).remove(to);
        edgeList.removeIf(e -> e.getFrom() == from && e.getTo() == to);
    }

    public void removeEdge(int from_id, int to_id) {
        if (getNode(from_id) != null && getNode(to_id) != null) {
            getSuccessors(from_id).remove(new Node(to_id));
            edgeList.removeIf(e -> e.getFrom().getId() == from_id && e.getTo().getId() == to_id);
        }
    }

    public void removeEdge(Edge e) {
        getSuccessors(e.getFrom()).remove(e.getTo());
        edgeList.remove(e);
    }

    public List<Edge> getOutEdges(Node n) {
        return getOutEdges(n.getId());
    }

    public List<Edge> getOutEdges(int id) {
        List<Edge> outEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (e.getFrom().getId() == id) {
                outEdges.add(e);
            }
        }
        return outEdges;
    }

    public List<Edge> getInEdges(Node n) {
        return getInEdges(n.getId());
    }

    public List<Edge> getInEdges(int id) {
        List<Edge> inEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (e.getTo().getId() == id) {
                inEdges.add(e);
            }
        }
        return inEdges;
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

    public List<Edge> getAllEdges() {
        return edgeList;
    }

    public int inDegree(Node n) {
        int inDegree = 0;
        for (Edge e : edgeList) {
            if (e.getTo().getId() == n.getId()) {
                inDegree++;
            }
        }
        return inDegree;
    }

    public int inDegree(int id) {
        int inDegree = 0;
        for (Edge e : edgeList) {
            if (e.getTo().getId() == id) inDegree++;
        }
        return inDegree;
    }

    public int outDegree(Node n) {
        return getSuccessors(n).size();
    }

    public int outDegree(int id) {
        return getSuccessors(id).size();
    }

    public int degree(Node n) {
        return inDegree(n) + outDegree(n);
    }

    public int degree(int id) {
        return inDegree(id) + outDegree(id);
    }

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

    public Graf getTransitiveClosure() {
        Graf g = this;
        Graf reverse = getReverse();
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

    public enum color{WHITE, GREY, BLACK}

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

    public static void main(String[] args) {

        boolean stop = false;
        Map<String, Graf> grafCreate = new HashMap<>();
        String grafName;

        Scanner reader = new Scanner(System.in);  // Reading from System.in

        //reader.close();

        while(!stop) {
            System.out.println("Enter the number of the instruction you want to follow: ");
            System.out.println("1 : Create a graph");
            System.out.println("2 : Modify an existing graph");
            System.out.println("3 : Get information about a graph");
            System.out.println("4 : Print a graf");
            System.out.println("10 : Quit the application");

            int n = reader.nextInt();

            switch (n) {
                case 1 :
                    System.out.println("Choose a kind of graph:");
                    System.out.println("1 : Empty graph");
                    System.out.println("2 : Random graph");
                    System.out.println("3 : Return to main menu");
                    int kindOfGraf = reader.nextInt();
                    while(!stop) {
                        stop=true;
                        switch (kindOfGraf) {
                            case 1:
                                System.out.println("Give the graf name:");
                                grafName = reader.next();
                                grafCreate.put(grafName, new Graf());
                                break;
                            case 2:
                                System.out.println("Give the graf name:");
                                grafName = reader.next();
                                System.out.println("Give the number of edges:");
                                int nbrOfEdges = reader.nextInt();
                                List<Integer> sa = new ArrayList<>();
                                int rand;
                                for (int i = 0; i < nbrOfEdges; ++i) {
                                    rand = 1 + (int) (Math.random() * ((nbrOfEdges - 1) + 1));
                                    for (int j = 1; j < rand; j++) {
                                        sa.add(1 + (int) (Math.random() * ((nbrOfEdges - 1) + 1)));
                                    }
                                    sa.add(0);
                                }
                                int[] array = sa.stream().mapToInt(i -> i).toArray();
                                grafCreate.put(grafName, new Graf(array));
                                break;

                            case 3:
                                break;

                            default:
                                System.out.println("Unknown command, enter a valid command : ");
                                //stop=false;
                                break;
                        }
                    }
                    stop = false;
                    break;

                case 2 :
                    System.out.println("Enter the name of the graf you want to modify:");
                    grafName = reader.next();
                    if(!grafCreate.containsKey(grafName)){
                        System.out.println("This graf doesn't exist. Return to main menu.");
                        stop = true;
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

                case 3 :
                    System.out.println("Enter the name of the graf you want to modify:");
                    grafName = reader.next();
                    if(!grafCreate.containsKey(grafName)){
                        System.out.println("This graf doesn't exist. Return to main menu.");
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
                                System.out.println("Graf " + grafName + " has " + grafCreate.get(grafName).nbNodes() + " node(s).");
                                break;
                            case 2:
                                System.out.println("Nodes of graf " + grafName + ":");
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
                                if(grafCreate.get(grafName).existsNode(reader.nextInt())) System.out.println("Node exist");
                                else System.out.println("Node doesn't exist");
                                break;
                            case 5:
                                System.out.println("Number of the first node:");
                                node = reader.nextInt();
                                System.out.println("Number of the second node:");
                                if(grafCreate.get(grafName).adjacent(node, reader.nextInt())) System.out.println("Node are adjacent");
                                else System.out.println("Node are not adjacent");
                            case 6:
                                System.out.println("Graf " + grafName + " has " + grafCreate.get(grafName).nbEdges() + " edges");
                                break;
                            case 7:
                                System.out.println("Nodes of graf " + grafName + ":");
                                System.out.println(grafCreate.get(grafName).getAllEdges());
                                break;
                            case 8:
                                System.out.println("Number of the first node:");
                                node = reader.nextInt();
                                System.out.println("Number of the second node:");
                                if(grafCreate.get(grafName).existsEdge(node, reader.nextInt())) System.out.println("Edge exist");
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
                    System.out.println("Enter the name of the graf you want to print:");
                    grafName = reader.next();
                    System.out.println("Show the graph in the DOT file (1) or export the graph to a DOT file (2)");
                    if(reader.nextInt() == 1) System.out.println(grafCreate.get(grafName).toDotString());
                    else if(reader.nextInt() == 2) {
                        System.out.println("Enter a file name:");
                        grafCreate.get(grafName).toDotFile(reader.next());
                    } else System.out.println("Unknown command, return to main menu");
                    break;

                case 10 :
                    System.out.println("Goodbye!");
                    stop = true;
                    break;

                default:
                    System.out.println("Unknown command, try again :");
            }
        }

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
