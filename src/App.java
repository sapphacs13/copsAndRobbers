import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import org.jgrapht.traverse.*;

import java.io.*;
import java.net.*;
import java.util.*;
public class App {
    public static void main(String[] args){
        Graph<URL, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);

       /* URL google = new URL("http://www.google.com");
        URL wikipedia = new URL("http://www.wikipedia.org");
        URL jgrapht = new URL("http://www.jgrapht.org");

        // add the vertices
        g.addVertex(google);
        g.addVertex(wikipedia);
        g.addVertex(jgrapht);

        // add edges to create linking structure
        g.addEdge(jgrapht, wikipedia);
        g.addEdge(google, jgrapht);
        g.addEdge(google, wikipedia);
        g.addEdge(wikipedia, google);*/
    }
}
