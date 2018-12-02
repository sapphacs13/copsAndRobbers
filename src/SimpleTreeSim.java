import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import java.util.Collection;
import java.util.function.Supplier;
import javax.swing.JFrame;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class SimpleTreeSim extends JFrame {

    public SimpleTreeSim(){

        JGraphXAdapter<String, DefaultEdge> jgxAdapter;
        SimpleGraph<String, DefaultEdge> graph =
                new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);


        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v4");
        graph.addVertex("v5");
        graph.addVertex("v6");
        graph.addVertex("v7");
        graph.addVertex("v8");

        graph.addEdge("v1", "v2");
        graph.addEdge("v1","v3");
        graph.addEdge("v3", "v4");
        graph.addEdge("v4", "v5");
        graph.addEdge("v4", "v6");
        graph.addEdge("v4", "v7");
        graph.addEdge("v3", "v8");


        jgxAdapter = new JGraphXAdapter <String, DefaultEdge>(graph);
        mxGraphComponent graphComponent = new mxGraphComponent(jgxAdapter);
        mxGraphModel graphModel  =       (mxGraphModel)graphComponent.getGraph().getModel();
        Collection<Object> cells =  graphModel.getCells().values();
        //formatting
        //remove direction
        mxUtils.setCellStyles(graphComponent.getGraph().getModel(),
                cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
        getContentPane().add(graphComponent);
        //remove edge labels
        graphComponent.getGraph().getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");

        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());

    }

    /*
    public void uncolorSingleVertex(String label) {
        for(int i=0; i<nodes.size(); i++) {
            // keeps all the vertices
            Object o = nodes.get(i);
            if(graph.getModel().isVertex(o) && graph.getLabel(o).equals(label) ) {
                mxCell vertex = (mxCell)o;
                graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#ffffff", new Object[]{vertex});
            }
        }
    }*/

    public static void main(String[] args) {

        SimpleTreeSim g = new SimpleTreeSim();

        g.setTitle(" undirected graph ");
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        g.pack();
        g.setVisible(true);
    }

}
