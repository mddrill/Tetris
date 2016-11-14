/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.tetremino;

import java.awt.Point;


/**
 *
 * @author mddrill
 */
public enum Shapes {
    I(0, new Point[]{new Point(0,1), new Point(0,0), new Point(0,-1), new Point(0,-2)}, "res/images/Tetromino_I.png"),
    J(1, new Point[]{new Point(0,-1), new Point(0,0), new Point(0,1), new Point(-1,1)}, "res/images/Tetromino_J.png"),
    L(2, new Point[]{new Point(0,-1), new Point(0,0), new Point(0,1), new Point(1,1)}, "res/images/Tetromino_L.png"),
    O(3, new Point[]{new Point(1,0), new Point(0,0), new Point(0,1), new Point(1,1)}, "res/images/Tetromino_O.png"),
    S(4, new Point[]{new Point(1,0), new Point(0,0), new Point(0,1), new Point(-1,1)}, "res/images/Tetromino_S.png"),
    T(5, new Point[]{new Point(-1, 0), new Point(0,0), new Point(1,0), new Point(0,1)}, "res/images/Tetromino_T.png"),
    Z(6, new Point[]{new Point(-1, 0), new Point(0,0), new Point(0,1), new Point(1,1)}, "res/images/Tetromino_Z.png");
    
    private final int shapeKey;
    private final Point[] shapeMap;
    private final String shapeImagePath;

    Shapes(int key, Point[] map, String imagePath){
        shapeKey = key;

        shapeMap = map;
        shapeImagePath = imagePath;
    }
    public static Shapes keyToShape(int i){
        for(Shapes shape : Shapes.values())
            if(shape.getShapeKey() == i)
                return shape;
        return null;
    }

    public String getShapeImagePath() {
        return shapeImagePath;
    }

   
    
    public static Shapes getI() {
        return I;
    }

    public static Shapes getJ() {
        return J;
    }

    public static Shapes getL() {
        return L;
    }

    public static Shapes getO() {
        return O;
    }

    public static Shapes getS() {
        return S;
    }

    public static Shapes getT() {
        return T;
    }

    public static Shapes getZ() {
        return Z;
    }

    public int getShapeKey() {
        return shapeKey;
    }

    public Point[] getShapeMap() {
        return shapeMap;
    }
    
}

