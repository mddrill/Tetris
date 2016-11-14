/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.tetremino;

import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author mddrill
 */
public class Block extends ImageView{
    public static final int BLOCK_WIDTH = 40;
    private static final Random GENERATOR = new Random();
    public Block(int x, int y, Image image ) {
        super(image);
        setX(x);
        setY(y);
        setId(String.valueOf(GENERATOR.nextLong()));
    }
    public Block(Block b) {
        super(b.getImage());
        setX(b.getX());
        setY(b.getY());
        setId(b.getId());
    }
///////Movements/////////////////////////////////////////////////////////
    public void xMove(int direction, int xStep){
        setX(getX()+direction*xStep);
    }
    public void yMove(int yStep){
        setY(getY()+yStep);
    }
}