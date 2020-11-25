package m1graf2020;

public class Node implements Comparable<Node>{

    private final int id;
    private String label;
    private static int biggestId = 0;

    public Node() {
        this.id = ++biggestId;
    }

    public Node(int id) {
        this.id = id;
        if(this.id > biggestId) biggestId = this.id;
    }

    public Node(int id, String label) {
        this.id = id;
        if(this.id > biggestId) biggestId = this.id;
        this.label = label;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;

        if (obj == null) return false;

        if( ! (obj instanceof Node) ) return false;

        Node other = (Node) obj;

        return Integer.compare(this.id, other.id) == 0;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static int getBiggestId() {
        return biggestId;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.getId(), o.getId());
    }

    @Override
    public String toString() {
        return "(" + this.getId() + ")";
    }
}
