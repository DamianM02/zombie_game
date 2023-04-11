package gra;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public enum ZombieFactory implements SpriteFactory{
    INSTANCE();


    final BufferedImage tape;

    ZombieFactory(){
        try {
            this.tape=ImageIO.read(getClass().getResource("/resources/walkingdead.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Sprite newSprite(int x,int y){
        Random r=new Random();

        double scale = 0.2 + 1 * r.nextDouble();// wylosuj liczbę z zakresu 0.2 do 2.0
        Zombie z = new Zombie(x,y,scale,tape);
        return z;
    }


}
