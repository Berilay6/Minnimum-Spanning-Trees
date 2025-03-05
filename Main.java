import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
