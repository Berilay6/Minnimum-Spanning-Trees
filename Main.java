import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Edge{
    public String srcV;
    public String destV;
    public float weight;

    public Edge(String srcV, String destV, float weight){
        this.srcV = srcV;
        this.destV = destV;
        this.weight = weight;
    }
}

class Vertex{
    public String id;
    public List<Edge> edges;

    public Vertex(String id){
        this.id=id;
        this.edges=new ArrayList<>();
    }

}

public class Main{
    public static void main(String[] args){

       /*  String inputFile = args[0];
        //read the file and create adjacency list
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            //read the number of vertices in the first line
            int verticesNum = Integer.parseInt(br.readLine().trim());

            //add vertices to list
            Map<String, Vertex> graph = new HashMap<>();
            for(int i=0; i<verticesNum; i++){
                String vertexId= br.readLine().trim();
                graph.put(vertexId, new Vertex(vertexId));
            }

            //read the number of edges
            int edgesNum = Integer.parseInt(br.readLine().trim());

            //add edges to adjacency list
            for(int i= 0; i<edgesNum; i++){
                String[] data = br.readLine().split(" ");
                String vertex1=data[0];
                String vertex2=data[1];
                float weight= Float.parseFloat(data[2]);

                graph.get(vertex1).edges.add(new Edge(vertex1, vertex2, weight));
                graph.get(vertex2).edges.add(new Edge(vertex2, vertex1, weight));
            }


        }catch(IOException e){
            System.out.println("Can not read the file: " + e.getMessage());
        }*/
        Map<String, Vertex> graph = new HashMap<>();
        graph.put("a", new Vertex("a"));
        graph.put("b", new Vertex("b"));
        graph.put("c", new Vertex("c"));
        graph.put("d", new Vertex("d"));
        graph.put("e", new Vertex("e"));
        graph.put("f", new Vertex("f"));
        graph.put("g", new Vertex("g"));

        graph.get("a").edges.add(new Edge("a", "b", 4));
        graph.get("b").edges.add(new Edge("b", "a", 4));
        graph.get("a").edges.add(new Edge("a", "c", 8));
        graph.get("c").edges.add(new Edge("c", "a", 8));
        graph.get("b").edges.add(new Edge("b", "c", 9));
        graph.get("c").edges.add(new Edge("c", "b", 9));
        graph.get("b").edges.add(new Edge("b", "d", 8.5f));
        graph.get("d").edges.add(new Edge("d", "b", 8.5f));
        graph.get("b").edges.add(new Edge("b", "e", 10));
        graph.get("e").edges.add(new Edge("e", "b", 10));
        graph.get("c").edges.add(new Edge("c", "d", 2));
        graph.get("d").edges.add(new Edge("d", "c", 2));
        graph.get("c").edges.add(new Edge("c", "f", 1));
        graph.get("f").edges.add(new Edge("f", "c", 1));
        graph.get("d").edges.add(new Edge("d", "e", 7));
        graph.get("e").edges.add(new Edge("e", "d", 7));
        graph.get("d").edges.add(new Edge("d", "f", 9));
        graph.get("f").edges.add(new Edge("f", "d", 9));
        graph.get("e").edges.add(new Edge("e", "f", 5));
        graph.get("f").edges.add(new Edge("f", "e", 5));
        graph.get("e").edges.add(new Edge("e", "g", 6));
        graph.get("g").edges.add(new Edge("g", "e", 6));
        graph.get("f").edges.add(new Edge("f", "g", 2.5f));
        graph.get("g").edges.add(new Edge("g", "f", 2.5f));


        Map<String, MultiwayTreeNode> treeMap = PrimMST(graph, "a");
        for(String key : treeMap.keySet()){
            MultiwayTreeNode node = treeMap.get(key);
            System.out.print(node.id + ": ");
            MultiwayTreeNode child = node.firstChild;
            while(child != null){
                System.out.print(child.id + " ");
                child = child.nextSibling;
            }
            System.out.println();
        }

        evert(treeMap.get("c"));
        System.out.println("After evert:");
        for(String key : treeMap.keySet()){
            MultiwayTreeNode node = treeMap.get(key);
            System.out.print(node.id + ": ");
            MultiwayTreeNode child = node.firstChild;
            while(child != null){
                System.out.print(child.id + " ");
                child = child.nextSibling;
            }
            System.out.println();
        }

        printMST("a", treeMap);
        printMST("f", treeMap);
        path("f", "b", treeMap);
        path("f", "f", treeMap);
        path("a", "g", treeMap);
        printMST("b", treeMap);
    }

    public static Map<String, MultiwayTreeNode> PrimMST(Map<String, Vertex> graph, String root){

        Map<String, Float> key = new HashMap<>();
        Map<String, MultiwayTreeNode> treeMap = new HashMap<>();
        MinHeap PQ = new MinHeap();

        //initials
        for(String u : graph.keySet()){
            //key[u]=inf
            //pred[u]=null (new MultiwayTreeNode)
            //add all vertices to PQ based on keys

            key.put(u, Float.MAX_VALUE);
            treeMap.put(u, new MultiwayTreeNode(u));
            PQ.insert(u, Float.MAX_VALUE);
        }

        //root
        key.put(root, 0f);
        PQ.decreaseKey(root, 0f);

        while(!PQ.isEmpty()){
            NodePQ min = PQ.extractMin();
            String u = min.vertex;
            MultiwayTreeNode uNode= treeMap.get(u);

            for(Edge edge : graph.get(u).edges){
                String v = edge.destV;

                if(PQ.trackMap.containsKey(v) && edge.weight < key.get(v)){
                    key.put(v, edge.weight);
                    PQ.decreaseKey(v, edge.weight);

                    MultiwayTreeNode vNode = treeMap.get(v);
                    if (vNode.parent != null) {
                        // Remove vNode from its previous parent's children
                        if (vNode.prevSibling != null) {
                            vNode.prevSibling.nextSibling = vNode.nextSibling;
                        } else {
                            vNode.parent.firstChild = vNode.nextSibling;
                        }
                        if (vNode.nextSibling != null) {
                            vNode.nextSibling.prevSibling = vNode.prevSibling;
                        }
                    }
                    uNode.addChild(vNode);
                }
            }
        }
        return treeMap;
    }

    public static void evert(MultiwayTreeNode newRoot){
        //we should reverse the connections of the tree starting from newRoot until previous root
        //to do this we will use cut and link operations
        MultiwayTreeNode current = newRoot;
        MultiwayTreeNode prev = null;
        MultiwayTreeNode next = null;
        
        while(current != null){
            next = current.parent;
            cut(current);

            if(prev != null){
                link(current, prev);
            }

            prev = current;
            current = next;
        }
        newRoot.parent = null;
    }

    public static void cut (MultiwayTreeNode u){
        if(u.parent != null){
            // Remove u from its parent's children
            if (u.prevSibling != null) {
                u.prevSibling.nextSibling = u.nextSibling;
            } else {
                u.parent.firstChild = u.nextSibling;
            }
            if (u.nextSibling != null) {
                u.nextSibling.prevSibling = u.prevSibling;
            }
            //remove u from its parent
            u.parent = null;
            u.prevSibling = null;
            u.nextSibling = null;
        }

    }

    public static void link(MultiwayTreeNode v, MultiwayTreeNode u){
        if (v.parent != null) {
            System.out.println("Invalid Operation: " + v + " is already connected");
            return;
        }
        v.parent = u;
        if(u.firstChild == null){
            u.firstChild = v;
        }
        else{
            // we should link them with correct alphabetic order
            if(v.id.compareTo(u.firstChild.id) < 0){
                v.nextSibling = u.firstChild;
                u.firstChild.prevSibling = v;
                u.firstChild = v;
                return;
            
            }
            else{
                 MultiwayTreeNode sibling = u.firstChild;
                while(sibling.nextSibling != null){
                    sibling = sibling.nextSibling;
                }
                sibling.nextSibling = v;
                v.prevSibling = sibling;
            }
           
        }
    }

    public static void printMST(String root, Map<String, MultiwayTreeNode> treeMap){

        MultiwayTreeNode r = treeMap.get(root);
        if(r == null){
            System.out.println("Invalid Operation: Root not found");
            return;
        }

        //arrange the MST according to root
        evert(r);

        // print the MST using preorder traversal
        System.out.println("Directive-----------------> print-mst " + root);
        preorderTraversal(r, 0);
    }
    
    public static void preorderTraversal(MultiwayTreeNode root, int level) {
        if (root == null) {
            System.out.println("Invalid Operation: Root not found");
        }

        // Print the current node
        for (int i = 0; i < level; i++) {
            System.out.print(". ");
        }
        System.out.println(root.id);

        // Recursively traverse the children
        MultiwayTreeNode child = root.firstChild;
        while (child != null) {
            preorderTraversal(child, level + 1);
            child = child.nextSibling;
        }
    }

    public static void path(String u, String v, Map<String, MultiwayTreeNode> treeMap){

        MultiwayTreeNode uNode = treeMap.get(u);
        MultiwayTreeNode vNode = treeMap.get(v);

        if(u == null || v == null){
            System.out.println("Invalid Operation: Node not found");
        }

        //evert the tree and make u the root
        evert(uNode);

        //we will start from v in the everted tree and go upside until the root which is u
        List<String> path = new ArrayList<>();
        while(vNode != null){
            path.add(vNode.id);
            vNode=vNode.parent;
        }

        //print the list reverse
        System.out.println("Directive-----------------> path " + u + " " + v);
        for(int i=path.size()-1; i>0; i--){
            System.out.print(path.get(i) + ", ");
        }
        System.out.println(path.get(0));
    }
}
