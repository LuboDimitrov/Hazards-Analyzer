package mainPackage;

import java.io.*;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.*;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import static mainPackage.Hazard.findHazards;
import static mainPackage.Hazard.printHazardsGrouped;

public class Main {

    public static ArrayList<Instruction> instructions;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";


    public static void main(String[] args) throws IOException, InvalidOperandException, InvalidOpcodeException, LabelDoesntExistException {

        //Get all txt files in root directory
        File[] listOfFiles = new File("./")
                .listFiles((dir, name) -> name.endsWith(".txt"));

        System.out.println("*************************************");
        System.out.println("LIST OF AVAILABLE FILES TO TEST:");

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            System.out.println(ANSI_GREEN + file.getName() + ANSI_RESET);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Choose which file u want to test: " + ANSI_RED + "(Type the name without the file extension!)" + ANSI_RESET);
        String filename = br.readLine();

        System.out.println("*************************************");

        CodeFile srcFile = new CodeFile(filename+".txt");
        instructions = srcFile.parseInstructions();
        printHazardsGrouped(instructions);

        final Graph<String, String> graph = createGraph();
        final VisualizationImageServer<String, String> server = createServer(graph);

        JFrame frame = new JFrame();
        frame.getContentPane().add(server);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private static Graph<String, String> createGraph() {
        final Graph<String, String> graph = new DirectedSparseMultigraph<>();

        ArrayList<Hazard> hazs = findHazards(instructions);
        //Create all the vertexes
        for (Instruction i : instructions){
            String vertex = String.valueOf(i.getPosition());
            graph.addVertex(vertex);
        }

        //Create all the edges
        for(int i = 0; i < hazs.size(); i++){
            //Source
            String vi = String.valueOf(hazs.get(i).getSource().getPosition());
            //Destination
            String vd = String.valueOf(hazs.get(i).getDest().getPosition());
            //edge name
            String aux = hazs.get(i).getHazardtype()+String.valueOf(i);

            graph.addEdge(aux, vi, vd, EdgeType.DIRECTED);
        }

        return graph;
    }

    private static VisualizationImageServer<String, String> createServer(
        final Graph<String, String> aGraph){
        final Layout<String, String> layout = new CircleLayout<>(aGraph);

        layout.setSize(new Dimension(700, 700));
        final VisualizationImageServer<String, String> vv =
                new VisualizationImageServer<>(
                        layout, new Dimension(700, 700));
        vv.getRenderContext().setVertexLabelTransformer(
                new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(
                new ToStringLabeller());

        return vv;
    }
}
