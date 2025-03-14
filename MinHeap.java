import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//priority queue elements which store the vertices and key values
class NodePQ{
    public String vertex;
    public float key;

    public NodePQ(String vertex, float key){
        this.vertex=vertex;
        this.key=key;
    }
}

//MinHeap structure with PQ elements
public class MinHeap{
    public List<NodePQ> heap;
    //trackMap will store indexes of nodes in the heap for easy finding
    public Map<String, Integer> trackMap; 

    public MinHeap(){
        heap = new ArrayList<>();
        trackMap = new HashMap<>();
    }

    public boolean isEmpty(){
        return heap.isEmpty();
    }

    //decreases the key value of the given vertex and rearrange the place of the vertex in heap
    public void decreaseKey(String vertex, float newKey){
        int i=trackMap.get(vertex);
        heap.get(i).key = newKey;

        while (i > 0 && heap.get((i - 1) / 2).key > heap.get(i).key) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    public void swap(int i, int j) {
        NodePQ temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);

        trackMap.put(heap.get(i).vertex, i);
        trackMap.put(heap.get(j).vertex, j);
    }

    public void insert(String vertex, float key){
        NodePQ node = new NodePQ(vertex, key);
        heap.add(node);
        int i=heap.size()-1;
        trackMap.put(vertex, i);
        decreaseKey(vertex, key);
    }

    public NodePQ extractMin(){
        if(heap.isEmpty()) 
            return null;

        NodePQ min = heap.get(0);
        NodePQ last = heap.remove(heap.size()-1);

        //move the last node to the start and call heapify to regenerate
        if(!heap.isEmpty()){
            heap.set(0,last);
            trackMap.put(last.vertex, 0);
            minHeapify(0);
        }

        trackMap.remove(min.vertex);
        return min;
    }

    public void minHeapify(int i) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = i;

        if (left < heap.size() && heap.get(left).key < heap.get(smallest).key)
            smallest = left;

        if (right < heap.size() && heap.get(right).key < heap.get(smallest).key)
            smallest = right;

        if (smallest != i) {
            swap(i, smallest);
            minHeapify(smallest);
        }
    }
}