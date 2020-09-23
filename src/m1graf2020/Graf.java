package m1graf2020;

import java.util.List;
import java.util.Map;

public class Graf {
    Map<Node, List<Node>> adjList;

    Graf() {

    }

    Graf(int ... sa) {
        int cpt = 1;
        Node n = new Node(cpt);

        for (int i : sa) {
            if(i != 0) {
               //adjList.put(cpt, )
            }
            else {
                //Node n = new Node(++cpt);
                //addNode()
            }
        }
    }
}
