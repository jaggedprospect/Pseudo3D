package pkg3dengine;

/**
 * Screen Class
 * 
 * Performs most of the calculations to present the screen
 * accurately relative to the player's current position
 * on the map.
 *
 * @author jagged_prospect
 */

import java.util.ArrayList;
import java.awt.Color;

public class Screen{
    
    // make private
    public int[][] map;
    public int mapWidth,mapHeight,width,height;
    public ArrayList<Texture> textures;
    
    public Screen(int[][] m,int mapW,int mapH,ArrayList<Texture> t,int w,int h){
        this.map=m;
        this.mapWidth=mapW;
        this.mapHeight=mapH;
        this.textures=t;
        this.width=w;
        this.height=h;
    }
    
    public int[] update(Camera camera,int[] pixels){
        for(int n=0;n<pixels.length/2;n++){
            if(pixels[n]!=Color.DARK_GRAY.getRGB())
                pixels[n]=Color.DARK_GRAY.getRGB();
        }
        
        for(int j=pixels.length/2;j<pixels.length;j++){
            if(pixels[j]!=Color.GRAY.getRGB())
                pixels[j]=Color.GRAY.getRGB();
        }
        
        for(int x=0;x<width;x+=1){
            double cameraX=2*x/(double)(width)-1;
            double rayDirX=camera.xDir+camera.xPlane*cameraX;
            double rayDirY=camera.yDir+camera.yPlane*cameraX;
            
            // Map Position
            int mapX=(int)camera.xPos;
            int mapY=(int)camera.yPos;
            
            // Length of ray from current position to next x or y side
            double sideDistX;
            double sideDistY;
            
            // Length of ray from one side to next in map
            double deltaDistX=Math.sqrt(1+(rayDirY*rayDirY)/(rayDirX*rayDirX));
            double deltaDistY=Math.sqrt(1+(rayDirX*rayDirX)/(rayDirY*rayDirY));
            double perpWallDist;
            
            // Direction to go in x and y
            int stepX,stepY;
            boolean hit=false; // was a wall hit?
            int side=0; // was the wall vertical or horizontal?
            
            // Figure out step direction and initial distance to a side
            if(rayDirX<0){
                stepX=-1;
                sideDistX=(camera.xPos-mapX)*deltaDistX;
            }else{
                stepX=1;
                sideDistX=(mapX+1.0-camera.xPos)*deltaDistX;
            }
            
            if(rayDirY<0){
                stepY=-1;
                sideDistY=(camera.yPos-mapY)*deltaDistY;
            }else{
                stepY=1;
                sideDistY=(mapY+1.0-camera.yPos)*deltaDistY;
            }
            
            // Loop to find where ray hits a wall
            while(!hit){
                // Jump to next square
                if(sideDistX<sideDistY){
                    sideDistX+=deltaDistX;
                    mapX+=stepX;
                    side=0;
                }else{
                    sideDistY+=deltaDistY;
                    mapY+=stepY;
                    side=1;
                }
                
                // Check if ray has hit wall
                if(map[mapX][mapY]>0)
                    hit=true;
            }
                        
            // Calculate distance ot point of impact
            if(side==0)
                perpWallDist=Math.abs((mapX-camera.xPos+(1-stepX)/2)/rayDirX);
            else
                perpWallDist=Math.abs((mapY-camera.yPos+(1-stepY)/2)/rayDirY);

            // Calculate height of wall based on distance from camera
            int lineHeight;
            if(perpWallDist>0)
                lineHeight=Math.abs((int)(height/perpWallDist));
            else
                lineHeight=height;

            // Calculate lowest and highest pixel to fill in current stripe
            int drawStart=-lineHeight/2+height/2;

            if(drawStart<0)
                drawStart=0;

            int drawEnd=lineHeight/2+height/2;

            if(drawEnd>=height)
                drawEnd=height-1;
            
            // Add a texture
            int texNum=map[mapX][mapY]-1;
            double wallX; // exact position of where wall was hit
            
            if(side==1) // y axis wall
                wallX=(camera.xPos+((mapY-camera.yPos+(1-stepY)/2)
                        /rayDirY)*rayDirX);
            else // x axis wall
                wallX=(camera.yPos+((mapX-camera.xPos+(1-stepX)/2)
                        /rayDirX)*rayDirY);
            
            wallX-=Math.floor(wallX);
            
            // x coordinate on texture
            int texX=(int)(wallX*(textures.get(texNum).SIZE));
            
            if(side==0 && rayDirX>0)
                texX=textures.get(texNum).SIZE-texX-1;
            if(side==1 && rayDirY<0)
                texX=textures.get(texNum).SIZE-texX-1;
            
            // y coordinate on texture
            for(int y=drawStart;y<drawEnd;y++){
                int texY=(((y*2-height+lineHeight)<<6)/lineHeight)/2;
                int color;
                
                if(side==0)
                    color=textures.get(texNum).pixels[texX+
                            (texY*textures.get(texNum).SIZE)];
                else // make y sides darker
                    color=(textures.get(texNum).pixels[texX+
                            (texY*textures.get(texNum).SIZE)]>>1) & 8355711;
                
                pixels[x+y*(width)]=color;
            }
        }

        return pixels;
    }
}
