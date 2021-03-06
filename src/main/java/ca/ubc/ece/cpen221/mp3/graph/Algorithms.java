package ca.ubc.ece.cpen221.mp3.graph;

import java.util.*;

import ca.ubc.ece.cpen221.mp3.staff.Graph;
import ca.ubc.ece.cpen221.mp3.staff.Vertex;

public class Algorithms {

	/**
	 * *********************** Algorithms ****************************
	 *
	 * Please see the README for this machine problem for a more detailed
	 * specification of the behavior of each method that one should implement.
	 */

	/**
	 * This is provided as an example to indicate that this method and other methods
	 * should be implemented here.
	 *
	 * You should write the specs for this and all other methods.
	 * 
	 * Perform a complete breadth first search of the given graph starting at a
	 * Returns the smallest x for which there is a path from a to b using x edges of
	 * the graph If no path exists, it throws an exception
	 *
	 * @param graph,
	 *            the graph for which the distance of the shortest path between two
	 *            of it vertices is desired requires: graph is not null and
	 *            non-empty
	 * @param a,
	 *            the start vertex of the path requires: a is a vertex of graph
	 * @param b,
	 *            the end of the path requires: b is a vertex of graph
	 * @return minimum x such that it is possible to get from a to b in a path
	 *         formed with x different edges of the graph for any value y less than
	 *         x it is impossible to get from a to b in a path formed with y
	 *         different edges of the graph
	 * @throws NotFoundException
	 *             if there is no path that gets from a to b
	 */
	public static int shortestDistance(Graph graph, Vertex a, Vertex b) throws NotFoundException {
		// TODO: Implement this method and others
		// Do a BFS starting at vertex a and recording at what depth is each vertex
		Map<Vertex, Integer> distances = Algorithms.bfsForVertex(graph, a);
		if (!distances.containsKey(b)) {
			// Condition is only true if b was not visited by the BFS of a
			throw new NotFoundException();
		}
		return distances.get(b);
	}

	/**
	 * Perform a complete depth first search of the given graph. Start with the
	 * search at each vertex of the graph and create a list of the vertices visited.
	 * Return a set where each element of the set is a list of elements seen by
	 * starting a DFS at a specific vertex of the graph (the number of elements in
	 * the returned set should correspond to the number of graph vertices).
	 *
	 * @param graph,
	 *            the graph to do the DFS requires: graph is not null
	 * @return a set of list of vertices. Each list represents the vertices visited
	 *         by carrying a DFS in each of graph vertices
	 */
	public static Set<List<Vertex>> depthFirstSearch(Graph graph) {
		Set<List<Vertex>> result = new LinkedHashSet<List<Vertex>>();
		for (Vertex vertex : graph.getVertices()) {
			// Do a DFS starting at each vertex of the graph
			result.add(Algorithms.dfsForVertex(graph, vertex));
		}
		return result;
	}

	/**
	 * Performs a depth first search of the given graph starting at the indicated
	 * vertex
	 * 
	 * @param graph,
	 *            the graph to do the DFS requires: graph is not null and non-empty
	 * @param start,
	 *            the vertex at which the DFS is going to start requires: start is a
	 *            vertex of graph
	 * @return a list of the vertices visited by carrying a DFS starting at start
	 *         The number of elements in this list is equal to the number of
	 *         vertices visited in the DFS
	 */
	private static List<Vertex> dfsForVertex(Graph graph, Vertex start) {
		Stack<Vertex> queue = new Stack<Vertex>();
		List<Vertex> result = new ArrayList<Vertex>();
		queue.push(start);
		while (!queue.isEmpty()) {
			Vertex up = queue.pop();
			// Take the next element and add its downstream neighbors to the stack so that
			// they are the next ones on which to carry the search
			if (!result.contains(up)) {
				result.add(up);
				for (Vertex down : graph.getDownstreamNeighbors(up)) {
					queue.push(down);
				}
			}
		}
		return result;
	}

	/**
	 * Perform a complete breadth first search of the given graph. Start with the
	 * search at each vertex of the graph and create a list of the vertices visited.
	 * Return a set where each element of the set is a list of elements seen by
	 * starting a BFS at a specific vertex of the graph (the number of elements in
	 * the returned set should correspond to the number of graph vertices).
	 *
	 * @param graph,
	 *            the graph to do the BFS requires: graph is not null
	 * @return a set of list of vertices. Each list represents the vertices visited
	 *         by carrying a BFS in each of graph vertices
	 */
	public static Set<List<Vertex>> breadthFirstSearch(Graph graph) {
		// TODO: Implement this method
		Set<List<Vertex>> result = new LinkedHashSet<List<Vertex>>();
		for (Vertex vertex : graph.getVertices()) {
			// Get a map with each vertex visited and the depth of it in the bfs starting
			// from vertex
			Map<Vertex, Integer> depths = Algorithms.bfsForVertex(graph, vertex);
			List<Vertex> visitedVertices = new ArrayList<Vertex>();
			// Add all the vertices visited into a list
			for (Map.Entry<Vertex, Integer> entry : depths.entrySet()) {
				visitedVertices.add(entry.getKey());
			}
			result.add(visitedVertices);
		}
		return result;
	}

	/**
	 * Performs a breadth first search of the given graph starting at the indicated
	 * vertex
	 * 
	 * @param graph,
	 *            the graph to do the BFS requires: graph is not null and non-empty
	 * @param start,
	 *            the vertex at which the BFS should start requires: start is a
	 *            vertex of graph
	 * @return a map of the vertices visited by carrying a BFS starting at start and
	 *         the depth at which they were found The number of elements in this map
	 *         is equal to the number of vertices visited in the BFS
	 */
	private static Map<Vertex, Integer> bfsForVertex(Graph graph, Vertex start) {
		Map<Vertex, Integer> result = new LinkedHashMap<Vertex, Integer>();
		result.put(start, 0);
		Queue<Vertex> queue = new LinkedList<Vertex>();
		queue.add(start);
		while (!queue.isEmpty()) {
			// Take the first element in the queue
			// Add all of its downstream neighbors to the queue to carry the BFS on them
			// after the vertices in the current level are done
			Vertex up = queue.remove();
			// Add all of its downstream neighbors to the map with a depth one bigger than
			// the depth of up
			for (Vertex down : graph.getDownstreamNeighbors(up)) {
				if (!result.containsKey(down)) {
					queue.add(down);
					result.put(down, result.get(up) + 1);
				}
			}
		}
		return result;
	}

	/**
	 * Calculates the eccentricity for each vertex of the graph. Returns a vertex
	 * for which the eccentricity is minimum
	 * 
	 * @param graph,
	 *            the graph for which it is wanted to calculate the center requires:
	 *            graph is not null and non-empty
	 * @return a vertex v for which the eccentricity of v is less or equal to the
	 *         eccentricity of any other vertex of graph
	 */
	public static Vertex center(Graph graph) {
		// TODO: Implement this method
		Vertex result = graph.getVertices().get(0);
		// The eccentricity of each vertex is always less than the number of vertices +
		// 1, except when it is infinity
		int minEccentricity = graph.getVertices().size() + 1;
		for (Vertex vertex : graph.getVertices()) {

			// Compute eccentricity for the vertex and check if it could be the center
			// unless it is infinity
			int eccentricity = bfsWithMaxDepth(graph, vertex, minEccentricity);
			if (eccentricity < minEccentricity && eccentricity != 0) {
				result = vertex;
				minEccentricity = eccentricity;
			}

		}
		return result;
	}

	/**
	 * Does a BFS until the depth of the vertices is bigger or equal to max. Returns
	 * the minimum between max and the eccentricity of start
	 * 
	 * @param graph,
	 *            the graph of which start is part from requires: graph is non-empty
	 * @param start,
	 *            the vertex for which x wants to be calculated requires start is a
	 *            vertex of graph
	 * @param max,
	 *            the maximum depth at which to stop the BFS requires: max is
	 *            non-negative
	 * @return biggest value x such that x is less or equal to the eccentricity of
	 *         start and x is less or equal to max if x is equal to 0
	 */
	private static int bfsWithMaxDepth(Graph graph, Vertex start, int max) {
		Queue<Vertex> thisLevel = new LinkedList<Vertex>();
		Queue<Vertex> nextLevel = new LinkedList<Vertex>();
		Set<Vertex> visited = new HashSet<Vertex>();
		thisLevel.add(start);
		visited.add(start);
		boolean flag = true;
		int maxDepth = 0;
		while (flag && maxDepth < max) {
			// while there are still vertices in this level keep computing the vertices of
			// the next one
			while (!thisLevel.isEmpty()) {
				Vertex up = thisLevel.remove();
				for (Vertex down : graph.getDownstreamNeighbors(up)) {
					if (visited.add(down)) {
						nextLevel.add(down);
					}
				}
			}
			// if a next level exist, increase the depth and prepare everything to keep the
			// bfs in this new level
			if (!nextLevel.isEmpty()) {
				maxDepth++;
				thisLevel.addAll(nextLevel);
				nextLevel.clear();
			} else {
				flag = false;
			}
		}
		return maxDepth;
	}

	/**
	 * Computes the eccentricity of every vertex and returns the maximum finite
	 * value of these. Returns x such that for any pair of vertices a,b in graph the
	 * distance from a to b is less than x.
	 * 
	 * @param graph,
	 *            the graph for which its diameter is to be calculated require:
	 *            graph to be non-null
	 * @return x such that for any pair of vertices a,b in graph the distance from a
	 *         to b is less than x
	 * @throws InfiniteDiameterException
	 *             if eccentricities of all vertexes are infinite
	 */
	public static int diameter(Graph graph) throws InfiniteDiameterException {
		// TODO: Implement this method
		int diameter = 0;
		int maxDepthPossible = graph.getVertices().size();
		// Calculate the eccentricty of each vertex and compare it to see if its the
		// maximum
		int count = 0;
		for (Vertex vertex : graph.getVertices()) {

			if(count%100==0){
				System.out.println(count);
				Date d = new Date();
				System.out.println(d.getTime());
			}
			count++;
			int eccentricity = Algorithms.bfsWithMaxDepth(graph, vertex, maxDepthPossible);
			// int eccentricity = Algorithms.calculateEccentricity(graph, vertex);
			if (eccentricity > diameter) {
				diameter = eccentricity;
			}
		}
		// if all eccentricites are 0, then the diameter is also 0
		// there is no edges in the graph so the diameter is actually infinity
		if (diameter == 0) {
			throw new InfiniteDiameterException();
		}
		return diameter;
	}

	/**
	 * Takes two vertices a and b from a graph and returns all the vertices u such
	 * that there is an edge from u to a and an edge from u to b
	 * 
	 * @param graph,
	 *            the graph on which the operation is going to take place requires
	 *            requires: graph is non-empty
	 * @param a,
	 *            requires: a is a vertex of graph
	 * @param b,
	 *            requires: a is a vertex of graph
	 * @return a list of vertex list for which if list contains u, then there is an
	 *         edge from u to a and an edge from u to b
	 */
	public static List<Vertex> commonUpstreamVertices(Graph graph, Vertex a, Vertex b) {
		// TODO: Implement this method
		List<Vertex> upstreamVerticesA = graph.getUpstreamNeighbors(a);
		List<Vertex> upstreamVerticesB = graph.getUpstreamNeighbors(b);
		return commonVertices(upstreamVerticesA, upstreamVerticesB); // this should be changed
	}

	/**
	 * Takes two list of vertices and gives the intersection of their elements
	 * 
	 * @param list1,
	 *            one of the list in which the operation is going to take place
	 *            requires: list1 is not null
	 * @param list1,
	 *            one of the list in which the operation is going to take place
	 *            requires: list1 is not null
	 * @return a list list3 such that if e is in list3 then e is in list1 and list2
	 */
	private static List<Vertex> commonVertices(List<Vertex> list1, List<Vertex> list2) {
		// loop through one list and check it each element of this list is contained in
		// the other one
		List<Vertex> result = new ArrayList<Vertex>();
		for (int i = 0; i < list1.size(); i++) {
			Vertex v = list1.get(i);
			if (list2.contains(v)) {
				result.add(v);
			}
		}
		return result;
	}

	/**
	 * Takes two vertices a and b from a graph and returns all the vertices u such
	 * that there is an edge from a to u and an edge from b to u
	 * 
	 * @param graph,
	 *            the graph on which the operation is going to take place requires
	 *            requires: graph is non-empty
	 * @param a,
	 *            requires: a is a vertex of graph
	 * @param b,
	 *            requires: a is a vertex of graph
	 * @return a list of vertex list for which if list contains u, then there is an
	 *         edge from a to u and an edge from b to u
	 */
	public static List<Vertex> commonDownstreamVertices(Graph graph, Vertex a, Vertex b) {
		// TODO: Implement this method
		List<Vertex> downstreamVerticesA = graph.getDownstreamNeighbors(a);
		List<Vertex> downstreamVerticesB = graph.getDownstreamNeighbors(b);
		return commonVertices(downstreamVerticesA, downstreamVerticesB);
	}

}
