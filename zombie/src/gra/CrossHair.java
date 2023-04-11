package gra;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

public class CrossHair implements MouseMotionListener, MouseListener {

    DrawPanel parent;
    List<CrossHairListener> listeners = new ArrayList<CrossHairListener>();

    CrossHair(DrawPanel parent) {
        this.parent = parent;
    }

    /* x, y to współrzedne celownika
       activated - flaga jest ustawiana po oddaniu strzału (naciśnięciu przyciku myszy)
    */
    int x;
    int y;
    boolean activated = false;



    void draw(Graphics g){
        if(activated)g.setColor(Color.RED);
        else g.setColor(Color.WHITE);
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        g.drawLine(x+19, y, x-19, y);
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        g.drawLine(x, y+19, x, y-19);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.drawOval(x-20, y-20, 40, 40);

        //g.drawString("xD" ,20, 20);

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x=e.getX();
        y=e.getY();
        parent.repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {


        mouseMoved(e);
        activated=true;

        try {
            notifyListeners();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }


        Timer timer = new Timer("Timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activated=false;
                parent.repaint();
            }
        },300);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //---------------------------------------------------------------------
    void addCrossHairListener(CrossHairListener e){
        listeners.add(e);
    }

    void notifyListeners() throws InterruptedException {
        for(var e:listeners)
            e.onShotsFired(x,y);
    }
}
