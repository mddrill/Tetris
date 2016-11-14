/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.tetremino;

import static tetris.tetremino.Block.BLOCK_WIDTH;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.scene.image.Image;
import res.sound.Sound;
import tetris.Tetris;
import static tetris.Tetris.HEIGHT_IN_BLOCKS;
import static tetris.Tetris.WIDTH_IN_BLOCKS;

/**
 *
 * @author mddrill
 */
public class Tetremino {
    private static Shapes shape;
    private static final int xStep = BLOCK_WIDTH, yStep = BLOCK_WIDTH;
    private static int x, y;
    private static final Random GENERATOR = new Random();
    public static final int LEFT = -1, RIGHT = 1;
    private static List<Point> shapeMap;
    private static Image image;
    private static List<Block> blocks = new ArrayList();
    private static boolean blockToTheLeft;
    private static boolean blockToTheRight;
    private static boolean blockBelow;
    private static boolean touchingLeftBoundary;
    private static boolean touchingRightBoundary;
    private static boolean touchingBottomBoundary;
    private static boolean canRotate = true;
    private static boolean overBottomBoundary;
    private static boolean overLeftBoundary;
    private static boolean overRightBoundary;
    private static boolean overTopBoundary;
    private static boolean onTopOfBlock;
    private static ArrayList<Point> checkShapeMap;
    private static ArrayList<Block> checkBlocks;
    private static final long NORMAL_SPEED_NANOS = 1000000000;
    private static final long FAST_SPEED_NANOS = NORMAL_SPEED_NANOS / 4;
    private static long before = 0;
    public static void reGenerate() {
        shape = Shapes.O;//Shapes.keyToShape(GENERATOR.nextInt(7));
        x = 2*BLOCK_WIDTH;//GENERATOR.nextInt(WIDTH_IN_BLOCKS) * BLOCK_WIDTH;
        y = -BLOCK_WIDTH;
        shapeMap = new ArrayList<>(Arrays.asList(shape.getShapeMap()));
        image = new Image(shape.getShapeImagePath());
        blocks = new ArrayList<>();
        //add blocks to the new tetremino
        shapeMap.forEach(p->{
            blocks.add( new Block((int)p.getX()*BLOCK_WIDTH + x, (int)p.getY()*BLOCK_WIDTH + y, image));
        });
                //adjust to make tetremino within boundaries
        checkForContact();
        //if the tetremino appears on top of a block or is stuck offscreen, redo the
        //generation
       
        if(onTopOfBlock || overLeftBoundary && blockToTheRight || overRightBoundary && blockToTheLeft){
            resetContact();
            reGenerate();
        }
        while(overLeftBoundary){
            xMove(RIGHT);
            resetContact();
            checkForContact();
        }
        while(overRightBoundary){
            xMove(LEFT);
            resetContact();
            checkForContact();
        }
        resetContact();
        //add Blocks the the Pane
        Tetris.getGamePane().getChildren().addAll(blocks);
    }
    private static void checkForContact(){
        blocks.forEach(b->{
            Tetris.blocks.forEach(tb->{
                if(tb.getX()==b.getX()+BLOCK_WIDTH && tb.getY()==b.getY()){
                    blockToTheRight = true;
                }
                if(tb.getX()==b.getX()-BLOCK_WIDTH && tb.getY()==b.getY()){
                    blockToTheLeft = true;
                }
                if(tb.getX()==b.getX() && tb.getY()==b.getY()+BLOCK_WIDTH){
                    blockBelow = true;
                }
                if(tb.getX()==b.getX() && tb.getY()==b.getY()){
                    onTopOfBlock = true;
                }
                
            });
            if(b.getY()+BLOCK_WIDTH>=HEIGHT_IN_BLOCKS*BLOCK_WIDTH){
                touchingBottomBoundary = true;
                if(b.getY()+BLOCK_WIDTH>HEIGHT_IN_BLOCKS*BLOCK_WIDTH){
                    overBottomBoundary = true;
                }
            }
            if(b.getX()<=0){
                touchingLeftBoundary = true;
                if(b.getX()<0){
                    overLeftBoundary = true;
                }
            }else if(b.getX()+BLOCK_WIDTH>=WIDTH_IN_BLOCKS*BLOCK_WIDTH){
                touchingRightBoundary = true;
                if(b.getX()+BLOCK_WIDTH>WIDTH_IN_BLOCKS*BLOCK_WIDTH){
                    overRightBoundary = true;
                }
            }
        });
    }
    private static void resetContact(){
        touchingLeftBoundary = false;
        touchingRightBoundary = false;
        touchingBottomBoundary = false;
        blockToTheRight = false;
        blockToTheLeft = false;
        blockBelow = false;
        overLeftBoundary = false;
        overRightBoundary = false;
        overBottomBoundary = false;
        overTopBoundary = false;
        onTopOfBlock = false;
        canRotate = true;
    }
    public static void checkRotation(int direction){
        //this method simulates the tetremino rotating, to check if it will
        //come in contact with anything
        
        checkShapeMap = new ArrayList<>();
        shapeMap.forEach(p->{
            checkShapeMap.add(new Point(p));
        });
        checkBlocks = new ArrayList<>();
        blocks.forEach(b->{
            checkBlocks.add(new Block(b));
        });
        checkShapeMap.forEach(p->{
            //switch x and y
            p.setLocation(p.getY(), p.getX());
            
            //if x and y have same sign, switch sign of x, otherwise switch sign of y
            if(x<0 == y<0){
                p.setLocation(p.getX() * -direction, p.getY()*direction);  
            }else{
                p.setLocation(p.getX() * direction, p.getY()* -direction);                  
            }
            
        });
        for(int i=0; i<4; i++){
            //squares.get(1) is used here for the absolute location of the block
            //because squares.get(1) is the pivot point of rotation (i should probably
            //clean this up later...)
            
            checkBlocks.get(i).setX(checkShapeMap.get(i).getX()*BLOCK_WIDTH+checkBlocks.get(1).getX());
            checkBlocks.get(i).setY(checkShapeMap.get(i).getY()*BLOCK_WIDTH+checkBlocks.get(1).getY());
        }
        checkBlocks.forEach(b->{
            Tetris.blocks.forEach(tb->{
                if(tb.getX()==b.getX() && tb.getY()==b.getY()){
                    canRotate = false;
                }
            });
            if(b.getX()<0 || b.getX()+BLOCK_WIDTH > WIDTH_IN_BLOCKS*BLOCK_WIDTH
                    || b.getY()> HEIGHT_IN_BLOCKS*BLOCK_WIDTH){
                canRotate = false;
            }
        });
    }
    public static void rotate(int direction){
        
        checkRotation(direction);
        
            if (canRotate == true){
                shapeMap.forEach(p->{
                    //switch x and y
                    p.setLocation(p.getY(), p.getX());
                    //if x and y have same sign, switch sign of x, otherwise switch sign of y
                    if(x<0 == y<0){
                        p.setLocation(p.getX() * -direction, p.getY()*direction);  
                    }else{
                        p.setLocation(p.getX() * direction, p.getY()* -direction);                  
                }
            });
            for(int i=0; i<4; i++){
                //squares.get(1) is used here for the absolute location of the block
                //because squares.get(1) is the pivot point of rotation (i should probably
                //clean this up later...)
                blocks.get(i).setX(shapeMap.get(i).getX()*BLOCK_WIDTH+blocks.get(1).getX());
                blocks.get(i).setY(shapeMap.get(i).getY()*BLOCK_WIDTH+blocks.get(1).getY());
            }
        }
        resetContact();
    }
    public static void xMove(int direction){
        checkForContact();
        if(direction == LEFT){
            if(!touchingLeftBoundary && !blockToTheLeft){
                blocks.forEach(b->{
                    b.xMove(direction, xStep);
                });
            }else{
                Sound.playErrorSound();
            }
        }else{
            if(!touchingRightBoundary && !blockToTheRight){
                blocks.forEach(b->{
                    b.xMove(direction, xStep);
                });
            }else{
                Sound.playErrorSound();
            }
        }
        resetContact();
    }
    public static void yMove(){
        blocks.forEach(b->{
                b.yMove(yStep);
            });
    }
    public static void update(long now){
        checkForContact();
        if(blockBelow || touchingBottomBoundary){
            Tetris.blocks.addAll(blocks);
            reGenerate();
        }else{
            if(now-Tetremino.getBefore() >= Tetris.getLevel().getDelta()){
                yMove();
                Tetremino.setBefore(now);
            }
        }
        resetContact();
    }
    
    //
    //GETTERS AND SETTERS
    //

    public static List<Block> getBlocks() {
        return blocks;
    }

    public static long getBefore() {
        return before;
    }

    public static void setBefore(long before) {
        Tetremino.before = before;
    }
    
}
