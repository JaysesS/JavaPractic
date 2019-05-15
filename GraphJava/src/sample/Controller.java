package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Controller {

    @FXML
    private ComboBox<String> switchGraphCombo = new ComboBox<String>();

    @FXML
    private Button createButton;

    @FXML
    private TextField nodeTextField;

    @FXML
    private TextField edgeTextField;

    @FXML
    private ImageView image;

    @FXML
    private Button workbutton;

    @FXML
    private Text textwork;

    @FXML
    private RadioButton rbDfs;

    @FXML
    private RadioButton rbBfs;

    @FXML
    private RadioButton rbD;

    @FXML
    private RadioButton rbIsF;

    @FXML
    private TextField delEdgeTextField;

    @FXML
    private Button delEdgeButton;

    @FXML
    private TextField addEdgeTextField;

    @FXML
    private Button addEdgeButton;

    int pressCreate = 0;

    Boolean dF = false;

    Boolean aF = false;


    private ArrayList<String> arrayPathGraph = new ArrayList<>();


    public void Init() {

        Settings();

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                pressCreate++;

                if(pressCreate %2 == 1 ){
                    unHide();
                } else {
                    Hide();
                }

                CreateGraph();

            }
        });

        workbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Work(switchGraphCombo.getValue());
            }
        });

        switchGraphCombo.setOnAction(event -> {

            textwork.setText("");
            if (new File(switchGraphCombo.getValue()  + ".json").exists())
                    Paint();
        });

        delEdgeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                delEdge();
                dF = false;
                delEdgeTextField.clear();
                textwork.setText("");
            }
        });

        delEdgeTextField.textProperty().addListener((observable -> {
            dF = true;
            showEdge();
        }));

        addEdgeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addEdge();
                aF = true;
                addEdgeTextField.clear();
                textwork.setText("");
            }
        });

        addEdgeTextField.textProperty().addListener((observable -> {
            aF = true;
            showEdge();
        }));

    }

    public void Work(String name) {

        String res = "";

        JSONParser parser = new JSONParser();

        int v = 0;
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader(name + ".json"));

            v = Integer.parseInt((String) json.get("CountVertex"));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        MainGraph WorkGraph = new MainGraph(Math.toIntExact(v));
        WorkGraph.loadGraph(name);

        if (rbDfs.isSelected()){
            res+="DFS(G): " + WorkGraph.dfs() + " ";
        }

        if (rbBfs.isSelected()){
            res+="BFS(G): " + WorkGraph.bfs() + " ";
        }

        if (rbD.isSelected()){
            res+="| D(G): " + WorkGraph.sumDegreesVertexGraph(WorkGraph.getAdjMat()) + " ";
        }
        if (rbIsF.isSelected()){
            res+="| isFull(G): " + WorkGraph.isFullGraph(WorkGraph.getAdjMat());
        }

        textwork.setText(res);
    }

    public void CreateGraph(){

        if(pressCreate %2 == 0) {

            if (!nodeTextField.getText().equals("")) {

                String nodes = nodeTextField.getText().replace(",", "");
                String edges = edgeTextField.getText().replace(",", "").toUpperCase();
                nodes = nodes.replace(" ", "");
                edges = edges.replace(" ","");

                JSONObject json = new JSONObject();
                json.put("CountVertex", String.valueOf(nodes.length()));

                json.put("Edges", edges);

                saveJson(json, "Graph" + (arrayPathGraph.size() + 1) + ".json");

                switchGraphCombo.getItems().clear();
                arrayPathGraph.clear();
                SearchGraph();
                switchGraphCombo.getItems().addAll(arrayPathGraph);
                if (arrayPathGraph.size() > 0)
                    switchGraphCombo.setValue(arrayPathGraph.get(arrayPathGraph.size() - 1));

            }
        }

    }

    public void showEdge(){

        if (dF) {

            String Edges = getInfoFromJson(switchGraphCombo.getValue())[1];

            char[] edgChar = Edges.toCharArray();

            if (edgChar.length >= 2) {

                String forInfo = "В этом графе связи: ";

                for (int i = 0; i <= edgChar.length - 2; i += 2) {
                    forInfo += String.valueOf(edgChar[i]) + String.valueOf(edgChar[i + 1]) + ", ";
                }

                textwork.setText(forInfo.substring(0, forInfo.length()-2));

            } else {

                textwork.setText("В этом графе нет связей..");
            }
        }

        if (aF) {

            String Edges = getInfoFromJson(switchGraphCombo.getValue())[1];

            char[] edgChar = Edges.toCharArray();

            if (edgChar.length >= 2) {

                String forInfo = "В этом графе связи: ";

                for (int i = 0; i <= edgChar.length - 2; i += 2) {
                    forInfo += String.valueOf(edgChar[i]) + String.valueOf(edgChar[i + 1]) + ", ";
                }

                textwork.setText(forInfo.substring(0, forInfo.length()-2));

            } else {

                textwork.setText("В этом графе нет связей..");
            }
        }
    }

    public void addEdge(){

        if (!addEdgeTextField.getText().equals("") && addEdgeTextField.getText().length() > 1) {

            String AEdges = addEdgeTextField.getText().replace(",", "").toUpperCase();

            AEdges = AEdges.replace(" ", "");

            String NEdges[] = getInfoFromJson(switchGraphCombo.getValue());

            NEdges[1] =  NEdges[1] + AEdges;

            JSONObject json = new JSONObject();

            json.put("CountVertex", NEdges[0]);

            json.put("Edges", NEdges[1]);

            saveJson(json, switchGraphCombo.getValue() + ".json");

            Paint();
        }
    }

    public void delEdge(){

        if (!delEdgeTextField.getText().equals("") && delEdgeTextField.getText().length() > 1) {

            String DEdges = delEdgeTextField.getText().replace(",", "").toUpperCase();
            DEdges = DEdges.replace(" ", "");

            String NEdges[] = getInfoFromJson(switchGraphCombo.getValue());

            NEdges[1] =  NEdges[1].replace(DEdges, "");

            JSONObject json = new JSONObject();

            json.put("CountVertex", NEdges[0]);

            json.put("Edges", NEdges[1]);

            saveJson(json, switchGraphCombo.getValue() + ".json");

            Paint();
        }
    }

    public String[] getInfoFromJson(String name){

        JSONParser parser = new JSONParser();

        String edg[] = new String[2];

        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader(name + ".json"));

            edg[0] = (String) json.get("CountVertex");

            edg[1] = (String) json.get("Edges");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return edg;
    }

    public void saveJson(JSONObject object, String name){

        FileWriter writer = null;
        try {
            writer = new FileWriter(name);
            writer.write(object.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SearchGraph(){

        Path directory  = Paths.get("");
        int Number = 0;
        String start = "Graph";
        String end = ".json";
        Boolean check = true;

        while (check) {

            String fileNameToFind = start + Number + end;

            try {
                Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {
                        if (path.toFile().getName().equals(fileNameToFind)) {
                            arrayPathGraph.add(String.valueOf(path.getFileName()).replace(".json", ""));
                            return FileVisitResult.TERMINATE;
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            Number++;
            if (Number > 20) {
                check = false;
            }
        }
    }

    public void unHide(){

        nodeTextField.setDisable(false);
        edgeTextField.setDisable(false);

    }

    public void Hide(){

        nodeTextField.setDisable(true);
        edgeTextField.setDisable(true);

    }

    public void Paint(){
        if (switchGraphCombo.getValue() != null) {
            GraphViz g = new GraphViz();
            g.CreateImg(switchGraphCombo.getValue());
            SetPic();
        }
    }

    public void SetPic(){

        File file = new File("pic.png");
        Image img = new Image(file.toURI().toString());
        image.setImage(img);

    }

    public void Settings(){

        Hide();

        SearchGraph();

        ToggleGroup rb = new ToggleGroup();
        rbBfs.setToggleGroup(rb);
        rbDfs.setToggleGroup(rb);

        rbBfs.setSelected(true);

        switchGraphCombo.getItems().addAll(arrayPathGraph);
        if (arrayPathGraph.size() > 0)
            switchGraphCombo.setValue(arrayPathGraph.get(0));

        Paint();

    }

}