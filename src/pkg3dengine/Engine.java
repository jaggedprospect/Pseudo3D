package pkg3dengine;

/**
 * A 3D Game Engine
 * 
 * Implements ray-casting to simulate a 3D environment similar
 * to that found in the classic shooter game, DOOM.
 *
 * @author jagged_prospect
 * 
 * Based on source code provided by sheeptheelectric
 */

import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;
import javax.swing.JFrame;

public class Engine extends JFrame implements Runnable{
    
    private static final long SERIAL_VERSION_UID=1L;
    private static final int WIDTH=1280,HEIGHT=760;
    
    private Thread thread;
    private boolean running;
    private BufferedImage image,hand;
    private Animation a;
    
    // make private
    public int mapWidth=15;
    public int mapHeight=15;
    public int[] pixels;
    public ArrayList<Texture> textures;
    public Camera camera;
    public Screen screen;
    
    public static int[][] map1={
        {3,3,3,3,3,3,3,3,3,3,3,3,3,3,3},
        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
        {3,5,0,5,5,5,5,5,0,5,5,5,5,5,3},
        {3,0,0,0,0,0,0,5,0,0,0,0,0,0,3},
        {3,0,0,0,0,0,0,5,0,0,6,0,0,0,3},
        {3,0,0,0,0,0,0,5,0,0,0,0,0,0,3},
        {3,5,5,0,0,0,5,5,5,5,5,0,5,5,3},
        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
        {3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
        {3,0,0,5,0,0,0,5,5,5,5,5,0,5,3},
        {3,0,0,5,0,0,0,0,0,0,0,0,0,0,3},
        {3,0,0,5,0,0,0,5,0,0,0,0,0,0,3},
        {3,3,3,3,3,3,3,3,3,3,3,3,3,3,3}
    };

    public Engine(){
        thread=new Thread(this);
        image=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
        pixels=((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        
        textures=new ArrayList<>();
        textures.add(Texture.wood);
        textures.add(Texture.brick);
        textures.add(Texture.blue);
        textures.add(Texture.stone);
        textures.add(Texture.tech);
        textures.add(Texture.mob);
        
        camera=new Camera(4.5,4.5,1,0,0,-0.66);
        screen=new Screen(map1,mapWidth,mapHeight,textures,WIDTH,HEIGHT);
        a=new Animation("res/test_hand+gun_sheet.png",100,100,1,5);
        
        addKeyListener(camera);
        
        setSize(new Dimension(WIDTH,HEIGHT));
        setResizable(false);
        setTitle("3D Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.GREEN);
        setLocationRelativeTo(null);
        setVisible(true);
        
        start();
    }
    
    private synchronized void start(){
        running=true;
        thread.start();
    }
    
    public synchronized void stop(){
        running=false;
        
        try{
            thread.join();
        }catch(InterruptedException e){
            System.err.print(e);
        }
    }
    
    /*
    BufferStrategy used when rendering so that screen updates
    are smoother -- graphics are obtained from buffer strategy
    to draw image on screen.
    */
    public void render(boolean animateHand){
        BufferStrategy bs=getBufferStrategy();
        
        if(bs==null){
            createBufferStrategy(3);
            return;
        }
        
        Graphics g=bs.getDrawGraphics();
        g.drawImage(image,0,0,image.getWidth(),image.getHeight(),null);
        
        g.setColor(Color.WHITE);
        g.drawOval((WIDTH/2)-10,(HEIGHT/2)-10,20,20);
        g.fillOval((WIDTH/2)-2,(HEIGHT/2)-2,4,4);
        
        if(animateHand){
            hand=a.getCurrentFrame();
            hand=resize(hand,500,500);
            g.drawImage(hand,WIDTH/2-250,HEIGHT/2-125,null);
        }
        
        // drawing camera variables (testing)
        g.drawString("xPos, yPos: "+camera.xPos+", "+camera.yPos,40,50);
        g.drawString("xDir, yDir: "+camera.xDir+", "+camera.yDir,40,70);
        g.drawString("xPlane, yPlane: "+camera.xPlane+", "+camera.yPlane,40,90);
        
        // ^^^^ clean up these variables ^^^^^
        
        bs.show();
    }
    
    private static BufferedImage resize(BufferedImage img,int width,int height){
        Image tmp=img.getScaledInstance(width,height,Image.SCALE_SMOOTH);
        BufferedImage resized=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d=resized.createGraphics();
        g2d.drawImage(tmp,0,0,null);
        g2d.dispose();
        
        return resized;
    }
    
    @Override
    public void run(){
        long lastTime=System.nanoTime();
        final double ns=1000000000.0/60.0; // 60 times per second
        double delta=0;
        int upsilon=6000;
        double frameCount=0;
        boolean animateHand=true;
        
        MapLoader ml=new MapLoader();
        //ml.load();
        
        requestFocus();
        
        while(running){
            long now=System.nanoTime();
            delta=delta+((now-lastTime)/ns);
            lastTime=now;
            
            // make sure update only happens 60 times per second
            while(delta>=1){
                screen.update(camera,pixels);
                camera.update(map1);
                delta--;
            }
            
            if(frameCount<=60)
                animateHand=true;
            else{
                animateHand=false;
            }
            
            render(animateHand); // displays to screen unrestricted time
            
            frameCount++;
            
            if(frameCount>60){
                frameCount=0;
            }
        }
    }
    
    public static void main(String[] args){
        Engine engine=new Engine();
    }
}
