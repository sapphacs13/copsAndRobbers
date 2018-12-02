import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;

import java.awt.*;
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

    //Constants
    //initial state variables
    private final int numCops = 3;
    //color variables
    private final String defaultColor = "white";
    private final String copColor = "green";
    private final String robberColor = "red";
    private final String nullString = "null";
    private final String styleColor="fillColor=";
    //Win Text Variables
    private final int winTextWidth = 80;
    private final int winTextHeight = 30;
    private final int radius = 300;

    //Variables
    //initial state
    private boolean initState = true;
    private int copCount = 0;
    //color variables
    private String nextColor = nullString;
    //win text variables
    private mxCell winText = null;


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



        jgxAdapter = new JGraphXAdapter <String, DefaultEdge>(graph) {
            @Override
            public boolean isCellSelectable(Object cell){
                mxCell c = (mxCell) cell;
                if(c.isEdge()){
                    return false;
                }
                return super.isCellSelectable(cell);
            }
        };
        mxRectangle r = new mxRectangle();
        r.setRect(0,0,1000,750);
        jgxAdapter.setMinimumGraphSize(r);
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
        initializeVertices(jgxAdapter, true);
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        layout.setRadius(radius);
        //jgxAdapter.setPreferred
        layout.execute(jgxAdapter.getDefaultParent());

        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null && cell instanceof mxCell) {
                    Set<String> vertices = graph.vertexSet();
                    if(vertices.contains(((mxCell)cell).getValue().toString())) {
                        if(initState){
                            //remove win text from last game
                            if(winText != null){
                                initializeVertices(jgxAdapter, false);
                                jgxAdapter.removeCells(new Object[]{winText});
                                winText = null;
                            }
                            //color cops
                            if(copCount < numCops){
                                jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, copColor, new Object[]{cell});
                                copCount++;
                            } else { //color robber
                                jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, robberColor, new Object[]{cell});
                                initState = false;
                            }
                        } else {
                            //if first click
                            if(nextColor.equals(nullString)) {
                                String style = ((mxCell) cell).getStyle();
                                //to handle transition between style and color variable
                                int beginIndexColor = style.indexOf(styleColor) + styleColor.length();
                                //grab color of vertex
                                nextColor = style.substring(beginIndexColor);
                                //reset this vertex
                                jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, defaultColor, new Object[]{cell});
                            } else {
                                String oldColor = ((mxCell) cell).getStyle();
                                jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, nextColor, new Object[]{cell});
                                if(oldColor.contains(robberColor) && nextColor.equals(copColor)){
                                    double xCenter = jgxAdapter.getGraphBounds().getCenterX()-winTextWidth/2;
                                    double yCenter = jgxAdapter.getGraphBounds().getCenterY()-winTextHeight/2;
                                    winText = (mxCell) jgxAdapter.insertVertex(jgxAdapter.getDefaultParent(), "winText", "Cop wins!", xCenter, yCenter, winTextWidth, winTextHeight);
                                    //reset for new game
                                    initState = true;
                                    copCount=0;
                                }
                                nextColor = nullString; //reset next color
                            }
                        }

                        graphComponent.refresh();
                        //System.out.println("click"); //testing
                    }
                }

            }
        });
        /*graphModel.

        for (String n : vertices){

        }*/

    }

    public void initializeVertices(JGraphXAdapter jgraphx, boolean setSize) {
        Object[] vertices = jgraphx.getChildVertices(jgraphx.getDefaultParent());
        for (Object v : vertices){
            mxCell cell = (mxCell) v;
            if(setSize) {
                mxGeometry geo = new mxGeometry();
                geo.setHeight(25);
                geo.setWidth(25);
                cell.setGeometry(geo);
            }
            jgraphx.setCellStyles(mxConstants.STYLE_FILLCOLOR, defaultColor, new Object[]{cell});
        }
    }

    public static void main(String[] args) {

        SimplePetersenSim g = new SimplePetersenSim();

        g.setTitle(" Petersen Graph ");
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*Dimension d = new Dimension();
        d.setSize(1000,750);
        g.setMinimumSize(d);*/
        g.pack();
        g.setVisible(true);
    }

}
