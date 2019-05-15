package sample;

import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class GraphViz {

    static String engAlf = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static char[] engChar = engAlf.toCharArray();

    int v;

    String e;


    public void CreateImg(String name){

        DefaultGraph g = new DefaultGraph("mainGraph");
        FileSinkImages pic = new FileSinkImages(FileSinkImages.OutputType.PNG, FileSinkImages.Resolutions.VGA);

        pic.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);

        g.addAttribute("ui.stylesheet",
        "node {" +
                "size: 10px;" +
                "fill-color: red; " +
                "}" +
                "edge {" +
                "size: 5px;" +
                "fill-color: green; " +
                "}" +
                "node #A{" +
                "fill-color: black;}");

        g.addAttribute("ui.quality");
        g.addAttribute("ui.antialias");

        JSONParser parser = new JSONParser();

        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader(name + ".json"));
            v = Integer.parseInt((String) json.get("CountVertex"));
            e = (String) json.get("Edges");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < v; i++) {
            g.addNode(String.valueOf(engChar[i]));
        }

        char[] eChar = e.toCharArray();

        for (int i = 0; i <= eChar.length - 2; i+=2) {
            g.addEdge(String.valueOf(i), String.valueOf(eChar[i]), String.valueOf(eChar[i+1]));
        }

        try {
            pic.writeAll(g, "pic.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}