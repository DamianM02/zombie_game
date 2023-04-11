package gra;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Zombie implements Sprite {
    BufferedImage tape;
    int x = 500;
    int y = 500;
    double scale = 1;



    int index = 0;  // numer wyświetlanego obrazka
    int HEIGHT = 312 ;// z rysunku;
    int WIDTH =  200 ;// z rysunku;

    Zombie(int x, int y, double scale) throws IOException {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.tape = ImageIO.read(getClass().getResource("/resources/walkingdead.jpg"));
    }

    Zombie(int x,int y, double scale, BufferedImage tape){
        this.x=x;
        this.y=y;
        this.scale=scale;
        this.tape=tape;
    }

    /**
     * Pobierz odpowiedni podobraz klatki (odpowiadającej wartości zmiennej index)
     * i wyświetl go w miejscu o współrzędnych (x,y)
     *
     * @param g
     * @param parent
     */

    public void draw(Graphics g, JPanel parent) {
        Image img = tape.getSubimage(index*200 ,0,200 , 312 ); // pobierz klatkę
        g.drawImage(img, x, y - (int) (HEIGHT * scale) / 2, (int) (WIDTH * scale), (int) (HEIGHT * scale), parent);
        g.setColor(Color.RED);
        //g.drawLine(x+(int) (WIDTH*scale), y, x+(int) (WIDTH*scale), y+20);
        //g.drawLine(x+ (int) (WIDTH*scale), y-(int) (HEIGHT * scale)/2, x+ (int) (WIDTH*scale) +20, y+(int) (HEIGHT * scale)/2);
    }

    /**
     * Zmień stan - przejdź do kolejnej klatki
     */

    public void next() {
        x -= 20* scale;
        index = (index + 1) % 10;
    }


    //--------------------------------------------------------------------------------
    @Override
    public boolean isVisible(){
        if(x<-(WIDTH*scale)) {
            //System.out.println("xD");
            return false;
        }
        return true;
    }
    @Override
    public boolean isHit(int _x, int _y){
        if(_x<x+(int) (WIDTH*scale) && _x>x && _y<y+(int) (HEIGHT * scale)/2 && _y>y-(int) (HEIGHT * scale)/2) return true;

        return false;
    }

    @Override
    public boolean isCloser(Sprite other){
        if(other instanceof Zombie z){
            if(scale>=z.scale) return true;
        }
        return false;
    }
}
