import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Set;
import javax.swing.JFrame;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class SimpleTreeSim extends JFrame {

    private boolean robberChoose = false;
    private String defaultColor = "white";
    private String copColor = "green";
    private String robberColor = "red";
    private int winTextWidth = 80;
    private int winTextHeight = 30;
    private mxCell winText = null;

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
        initializeVertices(jgxAdapter);

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
                        if (robberChoose) {
                            uncolorVertex(jgxAdapter, robberColor);
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, robberColor, new Object[]{cell});
                        }
                        else{ //cop choose
                            if(winText != null){
                                jgxAdapter.removeCells(new Object[]{winText});
                            }
                            uncolorVertex(jgxAdapter, copColor);
                            String oldColor = ((mxCell) cell).getStyle();
                            jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, copColor, new Object[]{cell});
                            //if cop wins
                            if(oldColor.contains(robberColor)){
                                robberChoose = true;
                                double xCenter = jgxAdapter.getGraphBounds().getCenterX()-winTextWidth/2;
                                double yCenter = jgxAdapter.getGraphBounds().getCenterY()-winTextHeight/2;
                                winText = (mxCell) jgxAdapter.insertVertex(jgxAdapter.getDefaultParent(), "winText", "Cop wins!", xCenter, yCenter, winTextWidth, winTextHeight);
                            }
                        }
                        robberChoose = !robberChoose;
                        //uncolor vertices
                        graphComponent.refresh();
                        //switch between colors for cop v. robber
                        //if switching from robber to cop color: game over
                        System.out.println("click");
                    }
                }

            }
        });

    }

    public void initializeVertices(JGraphXAdapter jgraphx) {
        Object[] vertices = jgraphx.getChildVertices(jgraphx.getDefaultParent());
        for (Object v : vertices){
            mxCell cell = (mxCell) v;
            jgraphx.setCellStyles(mxConstants.STYLE_FILLCOLOR, defaultColor, new Object[]{cell});
        }
    }

    public void uncolorVertex(JGraphXAdapter jgraphx, String color) {
        Object[] vertices = jgraphx.getChildVertices(jgraphx.getDefaultParent());
        for (Object v : vertices){
            mxCell cell = (mxCell) v;
            if(cell.getStyle().contains(color))
                jgraphx.setCellStyles(mxConstants.STYLE_FILLCOLOR, defaultColor, new Object[]{cell});
        }
    }

    public static void main(String[] args) {

        SimpleTreeSim g = new SimpleTreeSim();

        g.setTitle(" undirected graph ");
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        g.pack();
        g.setVisible(true);
    }

}
