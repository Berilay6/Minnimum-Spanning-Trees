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

        String inputFile = args[0];
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
            int edgesNum = Integer.parseInt(br.readLine().trim);

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
        }
    }

    public MultiwayTreeNode Prim(Map<String, Vertex> graph, String root){

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
        MultiwayTreeNode r = treeMap.get(root);
        key.put(r, 0f);
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
                        vNode.parent = null; 
                    }
                }
                uNode.addChild(vNode);
            }
        }
        return r;
    }
}
