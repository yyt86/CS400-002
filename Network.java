package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


/**
 * Filename:   	Network.java
 * Project:    	Ateam87 Final Project - Social Network
 * Due Date:	12/11/2019
 * 
 * Authors:    	Chien-Ming Chen (cchen556@wisc.edu)
 * 
 * Semester:   	Fall 2019
 * Course: 		CS400
 * Lecture: 	#002 
 * 
 * Desc: 
 * 
 * Construct a network of people by the garph data structure
 * 
 */

public class Network implements GraphADT {

	
	/*
	 *  Use Adjacent Lists (A HashTable of Linked Lists)to record vertices and edges
	 *  HashTable[tony] : steve....wanda
	 *  HashTable[steve] : tony....thor
	 *  HashTable[thor] : steve....hulk
	 */
	private Map<String, List<String>> vertexADJLists;
	  
	/*
	 * Default no-argument constructor
	 */ 
	public Network() {
		vertexADJLists = new HashMap<String, List<String>>();
	}

	/**
     * Add new vertex to the graph.
     *
     * If vertex is null or already exists,
     * method ends without adding a vertex or 
     * throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
	public void addVertex(String vertex) {
		//check if valid vertices first
	  	if (vertex == null || hasVertex(vertex)) 
	  		return;
	  	//System.out.println("add vetex : " + vertex);
	  	vertexADJLists.put(vertex, new ArrayList<String>());
	}
	
	/**
	 * Check whether a given vertex exists or not
	 * 
	 * @return true if vertexADJLists contains the given vertex
	 */
	private boolean hasVertex(String vertex) {
		return vertexADJLists.containsKey(vertex);
    }

	/**
     * Remove a vertex and all associated 
     * edges from the graph.
     * 
     * If vertex is null or does not exist,
     * method ends without removing a vertex, edges, 
     * or throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     */
	public void removeVertex(String vertex) {
		//System.out.println("remove user : %s" + vertex);
		if(vertex == null || !hasVertex(vertex))
			return;
		else {
			//Based on HashTable feature, we should only remove the vertex by its name which is the key
			//Then Garbege Collection will help to collect the list
			vertexADJLists.remove(vertex);
			for(String key : vertexADJLists.keySet())
			{
				//System.out.println("check " + key + " friendlist");
				if(vertexADJLists.get(key).contains(vertex))
					vertexADJLists.get(key).remove(vertex);
			}
		}
	}

	/**
     * Add the edge from vertex1 to vertex2
     * to this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * add vertex, and add edge, no exception is thrown.
     * If the edge exists in the graph,
     * no edge is added and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge is not in the graph
	 */
	public void addEdge(String vertex1, String vertex2) {
		
		//System.out.println("trying to add edge : " + vertex1 + vertex2);
	  	// check nulls
	    if (vertex1 == null || vertex2 == null)
	      return;
	    
	    if(vertex1 == vertex2)
	      return;
	    
	    // check if node exists
	    //if (!hasVertex(vertex1) || !hasVertex(vertex2))
	      //return;
	    
	    // check if node exists
	    if (!hasVertex(vertex1))
	    		addVertex(vertex1);
	    if (!hasVertex(vertex2))
	    		addVertex(vertex2);
	    // check if duplicated edge
	    // vertexADJLists.get(vertex1) means vertex1's adjacent list
	    if (vertexADJLists.get(vertex1).contains(vertex2))
	      return;
	    
	    //System.out.println("add edge : " + vertex1 + vertex2);
	    vertexADJLists.get(vertex1).add(vertex2);
	    vertexADJLists.get(vertex2).add(vertex1);
	}
	
	/**
     * Remove the edge from vertex1 to vertex2
     * from this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * or if an edge from vertex1 to vertex2 does not exist,
     * no edge is removed and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge from vertex1 to vertex2 is in the graph
     */
	public void removeEdge(String vertex1, String vertex2) {
	  	// check nulls
	    if (vertex1 == null || vertex2 == null)
	      return;

	    // check node exists
	    if (!hasVertex(vertex1) || !hasVertex(vertex2))
	      return;

	    // check if edge is existed	
	    if (!vertexADJLists.get(vertex1).contains(vertex2))
	      return;

	    vertexADJLists.get(vertex1).remove(vertex2);
	    vertexADJLists.get(vertex2).remove(vertex1);
	}	

	/**
     * Returns a Set that contains all the vertices
     * 
	 */
	public Set<String> getAllVertices() {
		return vertexADJLists.keySet();
	}

	/**
     * Get all the neighbor (adjacent) vertices of a vertex
     *
	 */
	public List<String> getAdjacentVerticesOf(String vertex) {
		return vertexADJLists.get(vertex);
	}
	
	/**
     * Returns the number of edges in this graph.
     */
    public int size() {
        int size = 0;
        for (String key: vertexADJLists.keySet()) 
          size += vertexADJLists.get(key).size();
        return size/2;
    }

	/**
     * Returns the number of vertices in this graph.
     */
	public int order() {
        return vertexADJLists.keySet().size();
    }
	
    public List<String> getShortestPathOf(String source, String end) {
    	//System.out.println("get short path");
    	
	    List<String> route = new ArrayList<>();
	    Map<String, String> path =  BFS(source);
	    String cur;
	    
	    if(path.containsKey(end)) {
	    	cur = path.get(end) ;
	    	route.add(end);
	    }
	    else
	    {
	    	return null;
	    }

	    for (cur = path.get(end); cur.equals(source) != true ; cur = path.get(cur)) {
	    	route.add(cur);
	    	//System.out.println("," + cur);
	    }
	    
	    route.add(source);
	    
	    Collections.reverse(route);
	    //System.out.println(route);
	    
        return route;
    }

	/*
	 * Use BFS to find the shortest path
	 */
    private Map<String, String> BFS(String sourceVertex){
        
    	List<String> visitedVertex = new ArrayList<>();
    	Map<String, String> path = new HashMap<>();
    	Queue<String> queue = new LinkedList<>();
        visitedVertex.add(sourceVertex);
        queue.add(sourceVertex);
        while(!queue.isEmpty()){
            String ver = queue.poll();  
            List<String> toBeVisitedVertex = vertexADJLists.get(ver);
            for (String v : toBeVisitedVertex) {      
                if (!visitedVertex.contains(v)) {    
                	queue.add(v);                   
                	visitedVertex.add(v);           
                    path.put(v, ver);                
                }
            }
        }
        return path; 
    }

	
	public void printGraph() {
		for ( String vertex : vertexADJLists.keySet() ) {
		    if ( vertexADJLists.get(vertex).size() != 0) {
		        for (String neighbor : vertexADJLists.get(vertex)) {
		            System.out.println(vertex + " -> " + neighbor + " ");
		        }
		    } else {
		        System.out.println(vertex + " -> " + " " );
		    }
		}
	}
}
