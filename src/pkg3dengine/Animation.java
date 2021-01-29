package pkg3dengine;

/**
 * Animation Class
 *
 * @author jagged_prospect
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public final class Animation{
    
    private ArrayList<BufferedImage> frames;
    private String loc;
    private int width,height,rows,columns;
    private int currentFrame;
    private boolean reverse;
    
    public Animation(String loc,int w,int h,int r,int c){
        this.loc=loc;
        this.width=w;
        this.height=h;
        this.rows=r;
        this.columns=c;
        frames=new ArrayList<>();
        currentFrame=0;
        reverse=false;
        
        addFrames();
    }
    
    public void addFrames(){
        try{
            BufferedImage image=ImageIO.read(new File(loc));
            
            for(int i=0;i<rows;i++){
                for(int j=0;j<columns;j++){
                    frames.add(image.getSubimage(
                            j*width,
                            i*height,
                            width,
                            height
                    ));
                }
            }
            
        }catch(IOException e){
            System.err.print(e);
        }
    }
    
    public BufferedImage getCurrentFrame(){        
        return frames.get(currentFrame);
    }
    
    public void nextFrame(){
        if(currentFrame>=frames.size()){
            currentFrame-=2;
            reverse=true;
        }else if(currentFrame<0){
            currentFrame+=2;
            reverse=false;
        }
        
        if(reverse)
            currentFrame--;
        else
            currentFrame++;
    }
}
