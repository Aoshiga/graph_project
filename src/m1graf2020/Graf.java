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
        for (int to : sa) {
            if(to != 0) {
                getSuccessors(from).add(new Node(to));
                edgeList.add(new Edge(new Node(from), new Node(to)));
            }
            else {
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
        if (adjList.containsKey(n)) return (Node) adjList.get(n);
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
        adjList.remove(new Node(id));
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            entry.getValue().remove(new Node(id));
        }
        for (Edge e : edgeList) {
            if (e.getTo().getId() == id || e.getFrom().getId() == id) {
                removeEdge(e);
            }
        }
    }

    public void removeNode(Node n) {
        adjList.remove(n);
        for (Map.Entry<Node, List<Node>> entry : adjList.entrySet()) {
            entry.getValue().remove(n);
        }
        for (Edge e : edgeList) {
            if (e.getTo() == n || e.getFrom() == n) {
                removeEdge(e);
            }
        }
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

    public boolean existsEdge(Node u, Node v) { return adjacent(u, v); }

    public boolean existsEdge(int u_id, int v_id) { return adjacent(u_id, v_id); }

    public boolean existsEdge(Edge e) { return edgeList.contains(e); }

    public void removeEdge(Node from, Node to) {
        getSuccessors(from).remove(to);
        edgeList.removeIf(e -> e.getFrom() == from && e.getTo() == to);
    }

    public void removeEdge(int from_id, int to_id) {
        getSuccessors(from_id).remove(new Node(to_id));
        edgeList.removeIf(e -> e.getFrom().getId() == from_id && e.getTo().getId() == to_id);
    }

    public void removeEdge(Edge e) {
        getSuccessors(e.getFrom()).remove(e.getTo());
        edgeList.remove(e);
    }

    public List<Edge> getOutEdges(Node n) {
        List<Edge> outEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (e.getFrom() == n) {
                outEdges.add(e);
            }
        }
        return outEdges;
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
        List<Edge> inEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (e.getTo() == n) {
                inEdges.add(e);
            }
        }
        return inEdges;
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
        List<Edge> incidentEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (e.getTo() == n || e.getFrom() == n) {
                incidentEdges.add(e);
            }
        }
        return incidentEdges;
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
            if (e.getTo() == n) inDegree++;
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
            for (Node n : entry.getValue()) {
                dot.append("\t").append(entry.getKey().getId()).append(" -> ").append(n.getId()).append(";\n");
            }
        }
        dot.append("}");
        return dot.toString();
    }

    public static void main(String[] args) {
        Graf g = new Graf(2, 4, 0, 0, 6, 0, 2, 3, 5, 8, 0, 0, 4, 7, 0, 3, 0, 7, 0);
        int[][] matrix = g.toAdjMatrix();
        System.out.println(g.nbNodes());
        for (int[] row : matrix) {
            System.out.print("row");
            for (int i : row) {
                System.out.print(i + " ");
            }
            System.out.print("\n");
        }
    }
}
