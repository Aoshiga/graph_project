package m1graf2020;

public class Edge implements Comparable<Edge> {

    private Node from;
    private Node to;
    private int weight;
    private String label;

    Edge() {}

    Edge(int from_id, int to_id) {
        this.from = new Node(from_id);
        this.to = new Node(to_id);
    }

    Edge(int from_id, int to_id, int weight) {
        this.from = new Node(from_id);
        this.to = new Node(to_id);
        this.weight = weight;
    }

    Edge(int from_id, int to_id, String label) {
        this.from = new Node(from_id);
        this.to = new Node(to_id);
        this.label = label;
    }

    Edge(int from_id, int to_id, int weight, String label) {
        this.from = new Node(from_id);
        this.to = new Node(to_id);
        this.weight = weight;
        this.label = label;
    }

    Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    Edge(Node from, Node to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    Edge(Node from, Node to, String label) {
        this.from = from;
        this.to = to;
        this.label = label;
    }

    Edge(Node from, Node to, int weight, String label) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.label = label;
    }

    public Node getFrom() { return from; }

    public Node getTo() { return to; }

    public int getWeight() { return weight; }

    public String getLabel() { return label; }

    @Override
    public int compareTo(Edge o) {
        if (this.getFrom().getId() > o.getFrom().getId()) return 1;
        else if (this.getFrom().getId() == o.getFrom().getId()) {
            return Integer.compare(this.getTo().getId(), o.getTo().getId());
        } else return -1;
    }

    @Override
    public String toString() {
        return "(" + from + ")->(" + to + ")";
    }
}
