package sample;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class MainGraph {

    static int MaxVerts = 20;

    private static Vertex[] vertexArray;

    static int[][] adjMat;

    private static int nVerts;

    StackX theStack = new StackX(MaxVerts);

    Queue theQueue = new Queue(MaxVerts);

    static String engAlf = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static char[] engChar = engAlf.toCharArray();

    public MainGraph(int MV){

        MaxVerts = MV;

        vertexArray = new Vertex[MaxVerts];

        adjMat = new int[MaxVerts][MaxVerts];
        nVerts = 0;
        for (int i = 0; i < MaxVerts; i++) {
            for (int j = 0; j < MaxVerts; j++) {
                adjMat[j][i] = 0;
            }
        }
    }

    public void loadGraph(String name){

        JSONParser parser = new JSONParser();

        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader(name + ".json"));
            int c = Integer.parseInt((String) json.get("CountVertex"));
            String edg = (String) json.get("Edges");
            setMaxVerts(Math.toIntExact(c));
            vertexArray = new Vertex[Math.toIntExact(c)];
            nVerts = 0;
            loadVertex(Math.toIntExact(c));
            loadEdges(edg);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void loadEdges(String edgeFromJson){

        char[] edges = edgeFromJson.toCharArray();

        for (int i = 0; i <= edges.length - 2; i+=2) {
            addEdge(getIndex(edges[i]), getIndex(edges[i+1]));
        }

    }

    public int getIndex(char j){

        for (int i = 0; i < engChar.length; i++) {
            if (j == engChar[i])
                return i;
        }
        return -1;
    }

    public void loadVertex(int count){

        for (int i = 0; i < count; i++) {
            addVertex(engChar[i]);
        }

    }

    public int sumDegreesVertexGraph(int[][] adj){

        int sum = 0;

        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj.length; j++) {
                sum+=adj[i][j];
            }
        }

        return sum;
    }

    public int[][] getAdjMat() {
        return adjMat;
    }

    public Boolean isFullGraph(int [][] adj){

        int count = 0;

        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj.length; j++) {
                if (adj[i][j] == 0){
                    count++;
                }
            }
        }

        if (count == adj.length){
            return true;
        } else {
            return false;
        }
    }

    public void setMaxVerts(int maxVerts) {
        MaxVerts = maxVerts;
    }

    public void displayEdge(){

        for (int i = 0; i < adjMat.length; i++) {

            for (int j = 0; j < adjMat.length; j++) {

                System.out.print(adjMat[j][i] + " ");

            }
            System.out.println();
        }
    }

    public void addVertex(char lab){

        vertexArray[nVerts++] = new Vertex(lab);

    }

    public void deleteVertex(int delVert) {

        if( delVert != nVerts-1 ) {

            for(int j = delVert; j<nVerts-1; j++)

                vertexArray[j] = vertexArray[j+1];

            for(int row = delVert; row<nVerts-1; row++)

                moveRowUp(row, nVerts);

            for(int col = delVert; col<nVerts-1; col++)

                moveColLeft(col, nVerts-1);
        }
        nVerts--;
    }

    private void moveRowUp(int row, int length) {

        for(int col=0; col<length; col++)
            adjMat[row][col] = adjMat[row+1][col];
    }

    private void moveColLeft(int col, int length) {

        for(int row=0; row<length; row++)
            adjMat[row][col] = adjMat[row][col+1];
    }

    public void addEdge(int start, int end){

        adjMat[start][end] = 1;
        adjMat[end][start] = 1;

    }

    public void delEdge(int start, int end){

        adjMat[start][end] = 0;
        adjMat[end][start] = 0;
    }

    public void displayVertex(int v){

        System.out.println(vertexArray[v].label);

    }

    public String dfs() {

        String res = "";
        vertexArray[0].wasVisited = true;
        res+= vertexArray[0].label;
        theStack.push(0); //exp

        while(!theStack.isEmpty()) {

            int v = getAdjUnvisitedVertex(theStack.peek());
            if(v == -1)
                theStack.pop();
            else {
                vertexArray[v].wasVisited = true;
                res+= vertexArray[v].label;
                theStack.push(v);
            }
        }

        for(int j=0; j<nVerts; j++)
            vertexArray[j].wasVisited = false;

        return res;
    }

    public String bfs() {

        String res = "";

        vertexArray[0].wasVisited = true;
        res+= vertexArray[0].label;
        theQueue.insert(0); //exp
        int v2;

        while(!theQueue.isEmpty()){
            int v1 = theQueue.remove();
            while( (v2 = getAdjUnvisitedVertex(v1)) != -1 )
            {
                vertexArray[v2].wasVisited = true;
                res+= vertexArray[v2].label;
                theQueue.insert(v2);
            }
        }

        for(int j=0; j<nVerts; j++)
            vertexArray[j].wasVisited = false;

        return res;
    }

    public int getAdjUnvisitedVertex(int v) {
        for(int j=0; j<nVerts; j++)

            if(adjMat[v][j]==1 && vertexArray[j].wasVisited==false)
                return j;

        return -1;
    }

}

class StackX {

    int maxSize;
    private int[] stackArray;
    private int top;

    public StackX(int s) {
        maxSize = s;
        stackArray = new int[maxSize];
        top = -1;
    }

    public void push(int p) { stackArray[++top] = p; }

    public int pop() { return stackArray[top--]; }

    public int peek() { return stackArray[top]; }

    public boolean isEmpty() { return (top == -1); }

}

class Queue {

    private final int SIZE;
    private int[] queArray;
    private int front;
    private int rear;

    public Queue(int s)
    {
        SIZE = s;
        queArray = new int[SIZE];
        front = 0;
        rear = -1;
    }

    public void insert(int j)
    {
        if(rear == SIZE-1)
            rear = -1;
        queArray[++rear] = j;
    }

    public int remove()
    {
        int temp = queArray[front++];
        if(front == SIZE)
            front = 0;
        return temp;
    }

    public boolean isEmpty()
    {
        return ( rear+1==front || (front+SIZE-1==rear) );
    }
}
