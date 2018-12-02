import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.function.Supplier;
import javax.swing.JFrame;


import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.generate.GeneralizedPetersenGraphGenerator;
import org.jgrapht.util.SupplierUtil;

public class SimplePetersenSim extends JFrame {

    public SimplePetersenSim(){

        JGraphXAdapter<String, DefaultEdge> jgxAdapter;
        GeneralizedPetersenGraphGenerator<String, DefaultEdge> gen =
                new GeneralizedPetersenGraphGenerator<>(5, 2);

        SimpleGraph<String, DefaultEdge> graph =
                new SimpleGraph<>(DefaultEdge.class);
        graph.setVertexSupplier(SupplierUtil.createStringSupplier());
        Supplier v = graph.getVertexSupplier();
        graph.setVertexSupplier(v);
        gen.generateGraph(graph, null);



        jgxAdapter = new JGraphXAdapter <>(graph);
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

        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Set<String> vertices = graph.vertexSet();
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null && cell instanceof mxCell) {
                    //listening to vertices
                    String v = ((mxCell)cell).getValue().toString();
                    if(vertices.contains(((mxCell)cell).getValue().toString())) {
                        System.out.println(((mxCell) cell).getStyle());
                        jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "green", new Object[]{cell});
                        graphComponent.refresh();
                        //switch between colors for cop v. robber
                        //if switching from robber to cop color: game over
                        System.out.println("click");
                    }
                }

            }
        });
        /*graphModel.

        for (String n : vertices){

        }*/

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

        SimplePetersenSim g = new SimplePetersenSim();

        g.setTitle(" undirected graph ");
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        g.pack();
        g.setVisible(true);
    }

}
