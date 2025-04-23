import java.awt.Color;
import java.awt.Graphics;

public class Rectangulo extends Trazo {

    public Rectangulo(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void dibujar(Graphics g, Color color) {
        g.setColor(color);
        g.drawRect(getXMinimo(), getYMinimo(), getAncho(), getAlto());
    }

    @Override
    public boolean cercano(int x, int y) {
        int minX = Math.min(getX1(), getX2());
        int maxX = Math.max(getX1(), getX2());
        int minY = Math.min(getY1(), getY2());
        int maxY = Math.max(getY1(), getY2());

        
    }

}
