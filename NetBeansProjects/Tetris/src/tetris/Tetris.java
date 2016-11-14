/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import res.sound.Sound;
import tetris.tetremino.Block;
import static tetris.tetremino.Block.BLOCK_WIDTH;
import tetris.tetremino.Tetremino;


/**
 *
 * @author mddrill
 */
public class Tetris extends Application {
    private static Pane root;
    private Scene scene;
    public static final int WIDTH_IN_BLOCKS = 10, HEIGHT_IN_BLOCKS = 15;
    public static final int SCREEN_WIDTH = BLOCK_WIDTH * WIDTH_IN_BLOCKS, SCREEN_HEIGHT = BLOCK_WIDTH * HEIGHT_IN_BLOCKS;
    private AnimationTimer timer;
    private int score = 0;
    private final int[] numSquaresInEachRow = new int[HEIGHT_IN_BLOCKS];
    private Collection<Integer> rowsToBeRemoved;
    private final int POINTS_PER_SCORE = 100;
    private Text showScore;
    private GridPane menu;
    private boolean gameOver;
    private static Pane gamePane;
    public static List<Block> blocks = new ArrayList<>();
    private boolean movingRowsDown;
    private static boolean increaseSpeed;
    private static Level level = Level.ONE;
    private Text showLevel;
    private final String SIGNATURE = "Tetris by\nMatt Drill";
    private GridPane top;
    private int rowMovingDown;
    
    
    private void createContent()
    {
        root = new StackPane();
        root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        gamePane = new Pane();
        gamePane.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        menu = new GridPane();
        createStartMenu();
        top = new GridPane();
        createTopText();
        root.getChildren().addAll(gamePane,menu,top);

    }
    
    private void update(long now){
        if(!isGameOver()){
            System.out.println(now);
            Tetremino.update(now);
            if(areRowsMovingDown()){
                moveRowsDown(rowMovingDown);
            }else{
                checkIfFullRow();
            }
        }else{
            gameOver();
        }
    }
    private void createTopText(){
        showScore = new Text(BLOCK_WIDTH, BLOCK_WIDTH*2, String.valueOf(score));
        showScore.getStyleClass().add("show-score");
        showLevel = new Text(BLOCK_WIDTH, BLOCK_WIDTH*2, level.getText());
        Text signature = new Text(BLOCK_WIDTH, BLOCK_WIDTH*2, SIGNATURE);
        signature.getStyleClass().add("signature");

        GridPane.setMargin(signature, new Insets(BLOCK_WIDTH/4));
        GridPane.setMargin(showScore, new Insets(BLOCK_WIDTH/4));
        GridPane.setMargin(showLevel, new Insets(BLOCK_WIDTH/4));

        top.setAlignment(Pos.TOP_LEFT);
        top.setVgap(BLOCK_WIDTH/2);
        top.getChildren().addAll(signature, showScore, showLevel);
        GridPane.setRowIndex(signature, 0);
        GridPane.setRowIndex(showScore, 1);
        GridPane.setRowIndex(showLevel, 2);
        
        top.setMouseTransparent(true);
    }
    private void createStartMenu(){
        Button play = new Button("Play");
        //To be implemented later
        //Button highScores = new Button("High Scores");
        //Button Options = new Button("Options");
        
        play.setOnAction(e->{
            startGame();
        });
        //To be implemented later
        /*highScores.setOnAction(e->{
        viewHighScores();
        });
        options.setOnAction(e->{
        viewOptions();
        });*/
        GridPane.setHalignment(play, HPos.CENTER);
        //To be implemented later
        /*GridPane.setHalignment(highScores, HPos.CENTER);
        GridPane.setHalignment(options, HPos.CENTER);*/


        menu.getChildren().add(play);
        //To be implemeted later
        //menu.getChildren().addAll(highScores, options);
        menu.setAlignment(Pos.CENTER);
        GridPane.setRowIndex(play, 0);
        //To be implemented later
        /*GridPane.setRowIndex(highScores, 1);
        GridPane.setRowIndex(playAgain, 2);*/

        menu.setVgap(20);
    }
    private void startGame(){
        Sound.playGameSong();
        menu.getChildren().clear();
        Tetremino.reGenerate();
        timer = new AnimationTimer() {
            private long before = 0;
            private final int fps = 60;
            private final long DELTA = 1000000000/fps;
            @Override
            public void handle(long now) {
                if(now-before >= DELTA){
                    update(now);
                    before = now;
                }
            }
        };
       timer.start();
    }
    private boolean isGameOver(){
        return blocks.stream().anyMatch((b) -> (b.getY() <= 0));
    }
    private void gameOver(){
        createGameOverMenu();
    }
    private void createGameOverMenu(){
        timer.stop();
        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font(null, FontWeight.BOLD, BLOCK_WIDTH));
        Text youScored = new Text("You Scored: " + String.valueOf(score));
        youScored.setFont(Font.font(null, FontWeight.BOLD, BLOCK_WIDTH/2));

        Button playAgain = new Button("Play Again");
        playAgain.setOnAction(e->{
            menu.getChildren().removeAll(gameOverText, youScored, playAgain);

            resetGame();
        });
        //playAgain.requestFocus();

        GridPane.setHalignment(gameOverText, HPos.CENTER);
        GridPane.setHalignment(youScored, HPos.CENTER);
        GridPane.setHalignment(playAgain, HPos.CENTER);


        menu.getChildren().addAll(gameOverText, youScored, playAgain);
        menu.setAlignment(Pos.CENTER);
        GridPane.setRowIndex(gameOverText, 0);
        GridPane.setRowIndex(youScored, 1);
        GridPane.setRowIndex(playAgain, 2);

        menu.setVgap(20);
    }     
    private void resetGame(){
        gamePane.getChildren().clear();
        blocks.clear();
        score = 0;
        timer.start();
    }
    private void checkIfFullRow(){
        //The method removes the lowest row that is full
        //it only removes one row for the sake of animation
        blocks.forEach(b->{
            numSquaresInEachRow[(int)b.getY()/BLOCK_WIDTH]++;
        });
        System.out.println(Arrays.toString(numSquaresInEachRow));
        //count down in this loop so that rows at the bottom are removed first
        for(int i = HEIGHT_IN_BLOCKS-1; i>=0; i--){
            if(numSquaresInEachRow[i] >= WIDTH_IN_BLOCKS){
                removeRow(i);
                break;
            }
        }
        Arrays.fill(numSquaresInEachRow, 0);  
    }  
    private void removeRow(int row){
        System.out.println("removeRow called");
        score += POINTS_PER_SCORE;
        level = Level.setLevel(score);
        showScore.setText(String.valueOf(score));
        blocks.forEach(b->{
            if(b.getY()/BLOCK_WIDTH == row){ 
                gamePane.getChildren().remove(b);
            }
        });
        blocks.removeIf(s->(s.getY()/BLOCK_WIDTH==row));
        moveRowsDown(row);
        rowMovingDown = row;
        Sound.playClearRowSound();
    }
    private void moveRowsDown(int row){
        //Move all rows above parameter row down 1
        blocks.forEach(b->{
            if(b.getY()/BLOCK_WIDTH <= row){
                b.setY(b.getY() + 1);
            }
        });  
    }
    private boolean areRowsMovingDown(){
        return blocks.stream().anyMatch(b->b.getY()%BLOCK_WIDTH !=0);
    }
    public enum Level{
        ONE("Level One", 750000000),
        TWO("Level Two", 500000000),
        THREE("Level Three", 250000000),
        FOUR("Level Four", 125000000),
        FIVE("Level Five", 62500000);
        
        private final String text;
        //This is the score needed to achieve this level
        private final int speed;
        
        
        Level(String text, int speed){
            this.text = text;
            this.speed = speed;
        }
        public static Level setLevel(int score){
            if(score >= 4000){
                return FIVE;
            }else if(score >= 3000){
                return FOUR;
            }else if(score >= 2000){
                return THREE;
            }else if(score >= 1000){
                return TWO;
            }else {
                return ONE;
            } 
        }

        public String getText() {
            return text;
        }

        public int getDelta(){
            if(increaseSpeed){
                return speed / 2;
            }else{
                return speed;         
            }
        }
        
    }

    @Override
    public void start(Stage primaryStage) {
        
        
        
        createContent();
        
        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        scene.getStylesheets().add("res/css/Styles.css");
        
        scene.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.LEFT){
                Tetremino.xMove(Tetremino.LEFT);
            }
            if(e.getCode() == KeyCode.RIGHT){
                Tetremino.xMove(Tetremino.RIGHT);
            }
            if(e.getCode() == KeyCode.DOWN){
                increaseSpeed = true;
            }
            if(e.getCode() == KeyCode.SHIFT){
                Tetremino.rotate(Tetremino.LEFT);
            }
            if(e.getCode() == KeyCode.SPACE){
                Tetremino.rotate(Tetremino.RIGHT);
            }
            if(e.getCode() == KeyCode.P){
                timer.stop();
            }
            if(e.getCode() == KeyCode.S){
                timer.start();
            }
        });
        scene.setOnKeyReleased(e->{
            if(e.getCode()==KeyCode.DOWN)
                increaseSpeed = false;
        });
        
        //timer.start();
        primaryStage.setTitle("Tetris");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    //
    //GETTERS AND SETTERS ***************************************************
    //

    public static Pane getRoot() {
        return root;
    }

    public static Pane getGamePane() {
        return gamePane;
    }

    public static Level getLevel() {
        return level;
    }

    
}