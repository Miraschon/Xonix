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
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

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


        final Set<Sprite> bricks = new HashSet<Sprite>();
        final Set<Sprite> xonixes = new HashSet<Sprite>();

        for (int i = 0; i < 200; i++) {
            Sprite xonix = new Sprite();
            xonix.setImage("xonix.png");
            xonixes.add(xonix);
            xonix.setPosition(RandomUtils.nextDouble(16, width - 31), RandomUtils.nextDouble(16, height - 31));
            xonix.setVelocity(200*sign(), 200*sign());
            //RandomUtils.nextInt()
        }

        for (double y = 0; y < height; y += 16) {
            Sprite brick = new Sprite();
            brick.setImage("brick.png");
            brick.setPosition(0, y);
            bricks.add(brick);
            if (y == 0) {
                brick.setType(UP_LEFT);
            } else if (y == height - 32) {
                brick.setType(DOWN_LEFT);
            } else {
                brick.setType(LEFT);
            }
        }
        for (int y = 0; y < height; y += 16) {
            Sprite brick = new Sprite();
            brick.setImage("brick.png");
            double px = width - 16;
            double py = y;
            brick.setPosition(px, py);
            bricks.add(brick);
            if (y == 0) {
                brick.setType(UP_RIGHT);
            } else if (y == height - 32) {
                brick.setType(DOWN_LEFT);
            } else {
                brick.setType(RIGHT);
            }
        }
        for (int x = 16; x < width - 16; x += 16) {
            Sprite brick = new Sprite();
            brick.setImage("brick.png");
            double px = x;
            double py = 0;
            brick.setPosition(px, py);
            bricks.add(brick);
            brick.setType(DOWN);
        }

        for (int x = 16; x < width - 16; x += 16) {
            Sprite brick = new Sprite();
            brick.setImage("brick.png");
            double px = x;
            double py = height - 16;
            brick.setPosition(px, py);
            bricks.add(brick);
            brick.setType(UP);
        }


        final LongValue lastNanoTime = new LongValue(System.nanoTime());


        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                gc.clearRect(0, 0, width, height);


                for (Sprite brick : bricks) {
                    brick.render(gc);
                }

                for (Sprite xonix : xonixes) {
                    xonix.update(elapsedTime);
                    collision(bricks, xonix);
                    xonix.render(gc);
                }

            }
        }.start();

        theStage.show();
    }

    private int sign() {
        if (RandomUtils.nextInt(0, 100) % 2 == 0) {
            return 1;
        } else {
            return -1;
        }
    }

    private void collision(Set<Sprite> bricks, Sprite xonix) {
        double hs = xonix.getVelocityX();
        double vs = xonix.getVelocityY();

        for (Sprite brick : bricks) {
            if (xonix.intersects(brick)) {
                switch (brick.getType()) {
                    case UP:
                        xonix.setVelocityY(Math.abs(vs) * -1);
                        break;
                    case DOWN:
                        xonix.setVelocityY(Math.abs(vs));
                        break;
                    case LEFT:
                        xonix.setVelocityX(Math.abs(hs));
                        break;
                    case RIGHT:
                        xonix.setVelocityX(Math.abs(hs) * -1);
                        break;
                    case UP_LEFT:
                        xonix.setVelocityY(Math.abs(vs));
                        xonix.setVelocityX(Math.abs(hs));
                        break;
                    case UP_RIGHT:
                        xonix.setVelocityY(Math.abs(vs));
                        xonix.setVelocityX(Math.abs(hs) * -1);
                        break;
                    case DOWN_LEFT:
                        xonix.setVelocityX(Math.abs(hs));
                        xonix.setVelocityY(Math.abs(vs) * -1);
                        break;
                    case DOWN_RIGHT:
                        xonix.setVelocityX(Math.abs(hs) * -1);
                        xonix.setVelocityY(Math.abs(vs) * -1);
                }
                break;
            }

        }
    }

}



