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

    //global variable graph
    public static Map<String, Vertex> graph = new HashMap<>();
    public static void main(String[] args){

         String inputFile = args[0];
        //read the file and create adjacency list
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            //read the number of vertices in the first line
            int verticesNum = Integer.parseInt(br.readLine().trim());

            //we will use the first vertex for MTS
            String firstVertex= br.readLine().trim();
            graph.put(firstVertex, new Vertex(firstVertex));

            //add other vertices to list
            for(int i=1; i<verticesNum; i++){
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

            //Create MST
            Map<String, MultiwayTreeNode> treeMap = PrimMST(graph, firstVertex);

            //directives
            String line;
            while((line = br.readLine()) != null){
                line=line.trim();

                //if the line is quit, the process is finished
                if(line.equals("quit")) break;

                //directives
                String[] command = line.split(" ");
                switch (command[0]){
                    case "print-mst":
                        printMST(command[1],treeMap);
                        break;
                    
                    case "path":
                        path(command[1], command[2],treeMap);
                        break;
                    
                    case "insert-edge":
                        insertEdge(command[1], command[2], Float.       parseFloat(command[3]), treeMap);
                        break;

                    case "decrease-weight":
                        decreaseWeight(command[1], command[2], Float.parseFloat(command[3]), treeMap);
                        break;
                    
                    default:
                        System.out.println("Invalid command: " + command[0]);

                }
            }

            br.close();

        }catch(IOException e){
            System.out.println("Can not read the file: " + e.getMessage());
        }
        
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

        //if the given vertices does not exist it is invalid
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

    public static void insertEdge(String u, String v, float w, Map<String, MultiwayTreeNode> treeMap){

        MultiwayTreeNode uNode = treeMap.get(u);
        MultiwayTreeNode vNode = treeMap.get(v);

        System.out.println("Directive-----------------> insert-edge " + u + " " + v + " " + w);

        //if the given vertices does not exist it is invalid
        if (u == null || v == null) {
            System.out.println("Invalid Operation");
            return;
        }

        //if the edge already exists, it is invalid operation
        Edge target = null;
        for(Edge edge : graph.get(u).edges){
            if(edge.destV.equals(v)){
                target = edge;
                break;
            }
        }
        if(target != null){
            System.out.println("Invalid Operation");
            return;
        }

        //add the edge to graph
        graph.get(u).edges.add(new Edge(u, v, w));
        graph.get(v).edges.add(new Edge(v, u, w));

        updateMST(uNode, vNode, w);
    }
    
    //this method is for updating the path between u and v when there is a edge change in MST without calling Prim()

    public static void updateMST(MultiwayTreeNode uNode, MultiwayTreeNode vNode, float w){

        //we should find the max weighted edge in the path of u to v
        //if the weight of the new edge is smaller than the edge that we found, update the MST

        evert(uNode); 

        MultiwayTreeNode maxNode = null;
        float maxWeight = Float.MIN_VALUE;
        MultiwayTreeNode current = vNode;

        while(current.parent != null){
            float edgeWeight = getEdgeWeight(current.parent, current);

            if (edgeWeight > maxWeight) {
                maxWeight = edgeWeight;
                maxNode = current;
            }
            current = current.parent;
        }

        //if the weight is smaller than maxWeight, update
        if(w < maxWeight && maxNode != null){
            cut(maxNode);
            link(uNode, vNode);
        }
    }

    //this is the helper method for getting wanted edge weight
    public static float getEdgeWeight(MultiwayTreeNode src, MultiwayTreeNode dest) {
        for (Edge edge : graph.get(src.id).edges) {
            if (edge.destV.equals(dest.id)) {
                return edge.weight;
            }
        }
        return Float.MAX_VALUE;
    }

    public static void decreaseWeight(String u, String v, float w, Map<String, MultiwayTreeNode> treeMap){

        MultiwayTreeNode uNode = treeMap.get(u);
        MultiwayTreeNode vNode = treeMap.get(v);

        System.out.println("Directive-----------------> decrease-weight " + u + " " + v + " " + w);

        //if the given vertices does not exist it is invalid
        if (u == null || v == null) {
            System.out.println("Invalid Operation");
            return;
        }

        //find the edge between u and v
        Edge target = null;
        for(Edge edge : graph.get(u).edges){
            if(edge.destV.equals(v)){
                target = edge;
                break;
            }
        }
        //if the edge does not exist it is invalid operation
        if(target == null){
            System.out.println("Invalid Operation");
            return;
        }

        //calculate and update the new weight
        float newWeight = target.weight - w;
        target.weight = newWeight;

        updateMST(uNode, vNode, newWeight);

    }

}
