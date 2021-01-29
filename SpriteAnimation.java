package pkg3dengine;

/**
 *
 * @author Nate Heppard
 * >> Not my original code <<
 */

import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SpriteAnimation extends Application{

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("JavaFX Example");
        
        Group root=new Group();
        Scene scene=new Scene(root);
        stage.setScene(scene);
        
        final double WIDTH=64;
        final double HEIGHT=64; 
        final double CANVAS_WIDTH=300;
        final double CANVAS_HEIGHT=300;
        
        Canvas canvas=new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);
        root.getChildren().add(canvas);
        
        final GraphicsContext gc=canvas.getGraphicsContext2D();
        final Image monster=new Image(new File("monster_main_1.png").toURI().toString());
        
        new AnimationTimer(){
            double frameNumber=0;
            double x=0;
            double y=0;
            boolean reverse=false;
            
            @Override
            public void handle(long currentNanoTime){
                if(frameNumber>24){
                    frameNumber-=2;
                    reverse=true;
                }else if(frameNumber<0){
                    frameNumber+=2;
                    reverse=false;
                }
                
                x=frameNumber*WIDTH;
                
                if(reverse==true)
                    frameNumber--;
                else
                    frameNumber++;
                
                // Clear canvas
                gc.setFill(Color.BLACK);
                gc.fillRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);
                
                // Draw next image
                gc.drawImage(monster,x,y,WIDTH,HEIGHT,CANVAS_WIDTH/2-WIDTH/2
                        ,CANVAS_HEIGHT/2-HEIGHT/2,WIDTH,HEIGHT);
            }
        }.start();
        
        stage.show();
    }
    
    public void run(String[] args){
        launch(args);
    }
}