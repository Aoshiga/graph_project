package m1graf2020;

import java.util.*;

public class Graf {
    Map<Node, List<Node>> adjList;
    List<Edge> edgeList;

    Graf() {
        adjList = new HashMap<>();
        edgeList = new ArrayList<>();
    }

    Graf(int ... sa) {
        adjList = new HashMap<>();
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
        Set<Edge> toDelete = new HashSet<Edge>();
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
        if(from == null) addNode(from);
        if(to == null) addNode(to);
        edgeList.add(new Edge(from, to));
        getSuccessors(from).add(to);
    }

    void addEdge(int from_id, int to_id) {
        if(getNode(from_id) == null) addNode(from_id);
        if(getNode(to_id) == null) addNode(to_id);
        edgeList.add(new Edge(new Node(from_id), new Node(to_id)));
        getSuccessors(from_id).add(new Node(to_id));
    }

    void addEdge(Edge e)
    {
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
            for (Node n : entry.getValue()) {
                sa_list.add(n.getId());
            }
            sa_list.add(0);
        }
        sa_list.remove(sa_list.size() -1);
        int[] sa_array = new int[sa_list.size()];
        for (int i = 0; i < sa_array.length; i++) {
            sa_array[i] = sa_list.get(i);
        }
        return sa_array;
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

    public String toDotString() {
        StringBuilder dot = new StringBuilder("digraph name {\n");
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            dot.append("\t").append(entry.getKey().getId());
            if(!entry.getValue().isEmpty()) dot.append(" -> ");
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

    public static void main(String[] args) {
        System.out.println(">>>>>>>> Creating the subject example graph in G");
        Graf g = new Graf(2, 4, 0, 0, 6, 0, 2, 3, 5, 8, 0, 0, 4, 7, 0, 3, 0, 7, 0);
        System.out.println(">>>> Graph information");
        System.out.println(">> DOT representation\n"+g.toDotString());
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

        System.out.println("");
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

        //System.out.println(">>>>>>>>>>    Reverse graph");
        //System.out.println(g.getReverse().toDotString());

        //System.out.println(">>>>>>>>>>    Transitive Closure");
        //System.out.println(g.getTransitiveClosure().toDotString());

        System.out.println(">>>>>>>>>>    Emptying the graph by removing all its nodes");
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

        System.out.println(">>>> Searching for edge (4, 2)");
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
    }
}
