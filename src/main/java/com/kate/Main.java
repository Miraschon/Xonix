package com.kate;

import com.kate.IntValue;
import com.kate.LongValue;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.kate.BrickType.*;

// Collect the Money Bags!
@SuppressWarnings("Duplicates")
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Xonix");

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);


        final double width = Screen.getPrimary().getBounds().getWidth() / 2;
        final double height = Screen.getPrimary().getBounds().getHeight() / 2;

        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);


        final GraphicsContext gc = canvas.getGraphicsContext2D();

        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(theFont);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        Sprite xonix = new Sprite();

        xonix.setImage("xonix.png");
        xonix.setPosition(200, 200);

        final Set<Sprite> bricks = new HashSet<Sprite>();
        final Set<Sprite> xonixes = new HashSet<Sprite>();
        xonixes.add(xonix);

        Sprite xonix2 = new Sprite();

        xonix2.setImage("xonix.png");
        xonix2.setPosition(300, 300);

        xonixes.add(xonix2);



        for (int y = 0; y < height - 16; y += 16) {
            Sprite brick = new Sprite();
            brick.setImage("brick.png");
            double px = 0;
            double py = y;
            brick.setPosition(px, py);
            bricks.add(brick);
            if (y == 0 || y == height - 32) {
                brick.setType(CORNER);
            } else {
                brick.setType(VER);
            }
        }
        for (int y = 0; y < height - 16; y += 16) {
            Sprite brick = new Sprite();
            brick.setImage("brick.png");
            double px = width - 16;
            double py = y;
            brick.setPosition(px, py);
            bricks.add(brick);
            if (y == 0 || y == height - 32) {
                brick.setType(CORNER);
            } else {
                brick.setType(VER);
            }
        }
        for (int x = 0; x < width; x += 16) {
            Sprite brick = new Sprite();
            brick.setImage("brick.png");
            double px = x;
            double py = 0;
            brick.setPosition(px, py);
            bricks.add(brick);
            if (x == 0 || x == width - 16) {
                brick.setType(CORNER);
            } else {
                brick.setType(HOR);
            }
        }
        for (int x = 0; x < width; x += 16) {
            Sprite brick = new Sprite();
            brick.setImage("brick.png");
            double px = x;
            double py = height - 16;
            brick.setPosition(px, py);
            bricks.add(brick);
            if (x == 0 || x == width - 16) {
                brick.setType(CORNER);
            } else {
                brick.setType(HOR);
            }
        }
        final LongValue lastNanoTime = new LongValue(System.nanoTime());

        final Velocity vel = new Velocity();
        for(Sprite x: xonixes) {
            x.setVelocity(vel.hs, vel.vs);
        }
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                gc.clearRect(0, 0, width, height);


                for (Sprite brick : bricks) {
                    brick.render(gc);
                }

                for(Sprite xonix: xonixes) {
                    xonix.update(elapsedTime);
                    collision(elapsedTime, bricks, xonix, vel, gc);
                    xonix.render(gc);
                }

            }
        }.start();

        theStage.show();
    }

    private void collision(double elapsedTime, Set<Sprite> bricks, Sprite xonix, Velocity vel, GraphicsContext gc) {
        for (Sprite brick : bricks) {
            if (xonix.intersects(brick)) {
                if (brick.getType() == HOR) {
                    vel.vs = -vel.vs;
                } else if (brick.getType() == VER) {
                    vel.hs = -vel.hs;
                } else if (brick.getType() == CORNER) {
                    vel.hs = -vel.hs;
                    vel.vs = -vel.vs;
                }
                xonix.setVelocity(vel.hs, vel.vs);
                xonix.update(elapsedTime);
                xonix.update(elapsedTime);
                xonix.update(elapsedTime);
                break;
            }
        }
    }
}
