package gra;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;

public class DrawPanel  extends JPanel implements CrossHairListener{
    BufferedImage background;
    //Zombie zombie = new Zombie(900, 500, 1);
    List<Sprite> sprites = new ArrayList<>();

    SpriteFactory factory;

    Semaphore mutex=new Semaphore(1);

    CrossHair cel=new CrossHair(this);

    boolean zakoncz=false;


    //--------------------------------------------------------------

    DrawPanel(URL backgroundImagageURL, SpriteFactory factory) {
        try {
            background = ImageIO.read(backgroundImagageURL);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.factory = factory;
        (new AnimationThread()).start();

        addMouseMotionListener(cel);
        addMouseListener(cel);

        cel.addCrossHairListener(this);

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(Sprite i:sprites) {

            i.draw(g, this);

        }
        mutex.release();
        cel.draw(g);

    }




    class AnimationThread extends Thread {
        public void run() {
            for (int i=1; ; i++) {
                try {
                    mutex.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for(int j=0; j<sprites.size(); ) {

                    if(sprites.get(j).isVisible()) {
                        sprites.get(j).next();
                        j++;
                    }else {


                        sprites.remove(j);

                        //j--;
                    }

                }
                mutex.release();
                repaint();
                try {
                    sleep(1000/30);  // 30 klatek na sekundę
                    //System.out.println("xxD\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //System.out.println("xD");
                }

                if(i%30==0) {
                    try {
                        mutex.acquire();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sprites.add(factory.newSprite(getWidth(),(int)(0.7*getHeight())));
                    sprites.sort(new Comparator<Sprite>() {
                        @Override
                        public int compare(Sprite o1, Sprite o2) {
                            if(o1.isCloser(o2))return 1;
                            return -1;
                        }
                    });
                    mutex.release();

                    i=0;
                }
                //System.out.println("XD");

                if(zakoncz) return;
                //System.out.println("xD");
            }


        }
    }

    @Override
    public void onShotsFired(int x, int y) throws InterruptedException {
        mutex.acquire();
        for(int i=sprites.size()-1; i>=0; i-- ){

            if(sprites.get(i).isHit(x, y)){
                sprites.remove(i);
                mutex.release();
                return;
            }

        }
        mutex.release();
    }
}