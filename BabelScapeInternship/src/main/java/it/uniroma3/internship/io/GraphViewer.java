package it.uniroma3.internship.io;

import java.util.Collection;

import javax.swing.JFrame;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

//Da implementare
/**
 * 
 * @author Silvio Severino
 * @param <T>
 *
 */
public class GraphViewer<T> extends JFrame
{
	private static final long serialVersionUID = 6456817480197525257L;
	
	private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
	private Graph<String, DefaultEdge> graph;
	private mxGraphComponent graphComponent;
	private mxGraphModel graphModel;
	private Collection<Object> cells;
	private mxCircleLayout layout;
	
	public GraphViewer(Graph<String, DefaultEdge> graph)
	{
		this.graph = graph;
	}
	

	@SuppressWarnings("deprecation")
	public void view()
	{
		this.jgxAdapter = new JGraphXAdapter<String, DefaultEdge>(this.graph);
		this.graphComponent = new mxGraphComponent(this.jgxAdapter);
		this.graphModel = (mxGraphModel)graphComponent.getGraph().getModel(); 
		this.cells = graphModel.getCells().values(); 
		mxUtils.setCellStyles(graphComponent.getGraph().getModel(), 
				cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
		getContentPane().add(graphComponent);
		this.layout = new mxCircleLayout(jgxAdapter);
		layout.execute(jgxAdapter.getDefaultParent());
		
		this.setTitle("Internship");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
}
