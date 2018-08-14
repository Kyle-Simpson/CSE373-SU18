package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.exceptions.NoPathExistsException;
/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    private IDictionary<V, IList<KVPair<V, E>>> adjacencyMatrix = new ChainedHashDictionary<>();
    private ISet<V> adjacencyList = new ChainedHashSet<>();
    private IList<E> edges = new DoubleLinkedList<>();
    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        for (V vertex: vertices) {
            adjacencyMatrix.put(vertex, new DoubleLinkedList<>());
            adjacencyList.add(vertex);
        }
        for (E edge : edges) {
            if (!adjacencyList.contains(edge.getVertex1())) {
                throw new IllegalArgumentException("Vertex1:" + edge.getVertex1() + " is invalid");
            }
            if (!adjacencyList.contains(edge.getVertex2())) {
                throw new IllegalArgumentException("vertex2:" + edge.getVertex2() + " is invalid");
            }
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Edges have a negative weight: " + edge);
            }
            adjacencyMatrix.get(edge.getVertex1()).add(new KVPair<>(edge.getVertex2(), edge));
            adjacencyMatrix.get(edge.getVertex2()).add(new KVPair<>(edge.getVertex1(), edge));
            this.edges.add(edge);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return this.adjacencyList.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return this.edges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        IPriorityQueue<E> priorityQueue = new ArrayHeap<>();
        IDisjointSet<V> disjointSet = new ArrayDisjointSet<>();
        ISet<E> mst = new ChainedHashSet<>();
        
        for (E edge: this.edges) {
            priorityQueue.insert(edge);
        }
        
        for (V vertex: this.adjacencyList) {
            disjointSet.makeSet(vertex);
        }
        
        while (mst.size() < this.adjacencyList.size() - 1) {
            E edge = priorityQueue.removeMin();
            
            int repU = disjointSet.findSet(edge.getVertex1());
            int repV = disjointSet.findSet(edge.getVertex2());
            
            if (repU != repV) {
                mst.add(edge);
                disjointSet.union(edge.getVertex1(), edge.getVertex2());
            }
        } 
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        IPriorityQueue<PriorityVertex<V, E>> queue = new ArrayHeap<>();
        IDictionary<V, PriorityVertex<V, E>> dict = new ChainedHashDictionary<>();
        
        for (V v: adjacencyList) {
            PriorityVertex<V, E> vertex;
            if (v.equals(start)) {
                vertex = new PriorityVertex<>(v, 0);
            } else {
                vertex = new PriorityVertex<>(v, Double.POSITIVE_INFINITY);
            }
            
            dict.put(v, vertex);
            queue.insert(vertex);
        }
        while (!queue.isEmpty()) {
            if (queue.peekMin().getVertex().equals(end)) {
                break;
            }
            
            PriorityVertex<V, E> minVertex = queue.removeMin();
            IList<KVPair<V, E>> neighbours = this.adjacencyMatrix.get(minVertex.getVertex());
            
            for (KVPair<V, E> neighbour: neighbours) {
                
                double alter = minVertex.getDistance() + neighbour.getValue().getWeight();
                PriorityVertex<V, E> neighbourWithDistance = dict.get(neighbour.getKey());
                
                if (alter < neighbourWithDistance.getDistance()) {
                    
                    PriorityVertex<V, E> updateVertex = new PriorityVertex<>(neighbour.getKey(), alter);
                    
                    updateVertex.addEdges(minVertex.getPath());
                    updateVertex.getPath().add(neighbour.getValue());
                    
                    dict.put(neighbour.getKey(), updateVertex);
                    queue.insert(updateVertex);
                }
            }
        }
        
        // if all nodes are processed but never reached the end, 
        // or end remains POSITIVE_INFINITY, we never find a path
        if (queue.isEmpty() || queue.peekMin().getDistance() == Double.POSITIVE_INFINITY) {
            throw new NoPathExistsException();
        }
        
        return queue.peekMin().getPath();
    }
    
    // get the minimum path from the source to the room; room can be observed as the vertex
    private static class PriorityVertex<V, E> implements Comparable<PriorityVertex<V, E>> {
        
        private double distance;
        private IList<E> pathFromSource = new DoubleLinkedList<>();
        private V vertex;
        
        public PriorityVertex(V vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(PriorityVertex<V, E> other) {
            if (this.getDistance() < other.getDistance()) {
                return -1;
            } else {
                return 1;
            }
        }
        
        public double getDistance() {
            return this.distance;
        }
        
        public void setDistance(double distance) {
            this.distance = distance;
        }
        
        public void addEdges(IList<E> edges) {
            pathFromSource = new DoubleLinkedList<>();
            
            for (E edge: edges) {
                pathFromSource.add(edge);
            }
        }
        
        public IList<E> getPath(){
            return this.pathFromSource;
        }
        
        public V getVertex() {
            return this.vertex;
        }
    }
}
