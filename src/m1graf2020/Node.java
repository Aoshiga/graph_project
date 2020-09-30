package m1graf2020;

public class Node {
    
    //test

    private int id;
    private static int biggestId = 0;

    Node() {
        this.id = ++biggestId;
    }

    Node(int id) {
        this.id = id;
        if(this.id > biggestId) biggestId = this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if( ! (obj instanceof Node) ) return false;

        Node other = (Node) obj;

        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }

    public static int getBiggestId() {
        return biggestId;
    }
}
