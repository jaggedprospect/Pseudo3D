package pkg3dengine;

/**
 * Camera Class
 * 
 * Keeps track of player location in 2D map and updates
 * player position.
 *
 * @author jagged_prospect
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class Camera extends KeyAdapter{
    
    private static final double MOVE_SPEED=0.05;
    private static final double ROTATION_SPEED=0.045;
    private static final double RUN_MULT=1.5;
    
    private static boolean[] keyDown=new boolean[256];
    
    /*
    Pos=player position, Dir=player direction (facing)
    Plane=farthest edge of FOV (perpendicular to Dir)
    Dir+Plane=FOV
    */
    public double xPos,yPos,xDir,yDir,xPlane,yPlane; // make private
    
    public Camera(double x,double y,double xd,double yd,double xp,double yp){
        xPos=x;
        yPos=y;
        xDir=xd;
        yDir=yd;
        xPlane=xp;
        yPlane=yp;
    }
    
    public void update(int[][] map){
        double run=1;

        if(isKeyPressed(KeyEvent.VK_SHIFT))
            run=RUN_MULT;
        
        if(isKeyPressed(KeyEvent.VK_W)){
            if(map[(int)(xPos+xDir*MOVE_SPEED*run)][(int)yPos]==0)
                xPos+=xDir*MOVE_SPEED*run;
            if(map[(int)xPos][(int)(yPos+yDir*MOVE_SPEED*run)]==0)
                yPos+=yDir*MOVE_SPEED*run;
        }else if(isKeyPressed(KeyEvent.VK_S)){
            if(map[(int)(xPos-xDir*MOVE_SPEED*run)][(int)yPos]==0)
                xPos-=xDir*MOVE_SPEED*run;
            if(map[(int)xPos][(int)(yPos-yDir*MOVE_SPEED*run)]==0)
                yPos-=yDir*MOVE_SPEED*run;
        }
        if(isKeyPressed(KeyEvent.VK_D)){
            double oldxDir=xDir;
            xDir=xDir*Math.cos(-ROTATION_SPEED)-yDir*Math.sin(-ROTATION_SPEED);
            yDir=oldxDir*Math.sin(-ROTATION_SPEED)+yDir*Math.cos(-ROTATION_SPEED);
            double oldxPlane=xPlane;
            xPlane=xPlane*Math.cos(-ROTATION_SPEED)-yPlane*Math.sin(-ROTATION_SPEED);
            yPlane=oldxPlane*Math.sin(-ROTATION_SPEED)+yPlane*Math.cos(-ROTATION_SPEED);
        }else if(isKeyPressed(KeyEvent.VK_A)){
            double oldxDir=xDir;
            xDir=xDir*Math.cos(ROTATION_SPEED)-yDir*Math.sin(ROTATION_SPEED);
            yDir=oldxDir*Math.sin(ROTATION_SPEED)+yDir*Math.cos(ROTATION_SPEED);
            double oldxPlane=xPlane;
            xPlane=xPlane*Math.cos(ROTATION_SPEED)-yPlane*Math.sin(ROTATION_SPEED);
            yPlane=oldxPlane*Math.sin(ROTATION_SPEED)+yPlane*Math.cos(ROTATION_SPEED);
        }
    }

    @Override
    public void keyPressed(KeyEvent event){
        keyDown[event.getKeyCode()]=true;
    }
    
    @Override
    public void keyReleased(KeyEvent event){
        keyDown[event.getKeyCode()]=false;
    }
    
    public boolean isKeyPressed(int key){
        return keyDown[key];
    }
    
    public boolean isKeyReleased(int key){
        return !keyDown[key];
    }
}
