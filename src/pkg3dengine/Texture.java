package pkg3dengine;

/**
 * Texture Class
 *
 * @author jagged_prospect
 */

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public final class Texture{
    
    public final int SIZE;
    
    private String loc;
    
    public int[] pixels; // make private
    public String name;
    
    public Texture(String location,String name,int size){
        this.loc=location;
        this.SIZE=size;
        pixels=new int[SIZE*SIZE];
        this.name=name;
        load();
    }
    
    public int getSize(){
        return SIZE;
    }
    
    public void load(){
        try{
            BufferedImage image=ImageIO.read(new File(loc));
            int w=image.getWidth();
            int h=image.getHeight();
            image.getRGB(0,0,w,h,pixels,0,w);
        }catch(IOException e){
            System.err.print(e);
        }
    }
    
    public static Texture wood=new Texture("res/wood.png","wood",64);
    public static Texture brick=new Texture("res/bricks.png","bricks",64);
    public static Texture blue=new Texture("res/blue.png","blue",64);
    public static Texture stone=new Texture("res/stone.png","stone",64);
    public static Texture tech=new Texture("res/tech.png","tech",64);
    public static Texture mob=new Texture("res/test_mob.png","test_mob",64);
}
