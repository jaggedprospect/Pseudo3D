package pkg3dengine;

/**
 * Map Loader Class
 *
 * @author Nate Heppard
 */

import java.util.ArrayList;
import java.io.*;

public class MapLoader{
    
    public static final String MAP_LOCATION="res/maps.txt";
    
    private BufferedReader br;
    private ArrayList<Map> maps;
    
    public MapLoader(){
        maps=new ArrayList<>();
    }
    
    public void load(){
        String line;
        
        try{
            br=new BufferedReader(new FileReader(MAP_LOCATION));
            
            while((line=br.readLine())!=null){
                System.out.println(line);
            }
        }catch(IOException e){
            System.err.print(e);
        }
    }
    
    private class Map{
        
        private String mapName;
        private int mapWidth;
        private int mapHeight;
        private int[][] grid;
        
        public Map(String mn,int mw,int mh,int[][] g){
            this.mapName=mn;
            this.mapWidth=mw;
            this.mapHeight=mh;
            this.grid=g;
        }
        
        public void setName(String mn){
            this.mapName=mn;
        }
        
        public String getName(){
            return mapName;
        }
        
        public int getWidth(){
            return mapWidth;
        }
        
        public int getHeight(){
            return mapHeight;
        }
        
        public int[][] getGrid(){
            return grid;
        }
    }
}
