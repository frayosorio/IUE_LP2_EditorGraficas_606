import java.awt.Graphics;
import java.util.List;
import java.awt.Color;

import javax.swing.JPanel;

import com.fasterxml.jackson.core.type.TypeReference;

import entidades.TrazoDTO;

public class Dibujo {

    private Nodo cabeza;

    public Dibujo() {
        cabeza = null;
    }

    public void agregar(Nodo nodo) {
        if (cabeza == null) {
            cabeza = nodo;
        } else {
            // recorrer la lista
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nodo;
        }
        nodo.siguiente = null;
    }

    public int getLongitud() {
        int totalNodos = 0;
        Nodo actual = cabeza;
        while (actual != null) {
            totalNodos++;
            actual = actual.siguiente;
        }
        return totalNodos;
    }

    public void dibujar(JPanel pnl) {
        limpiarPanel(pnl);
        // obtener el objeto graficador
        Graphics g = pnl.getGraphics();
        // recorrer la lista
        Nodo actual = cabeza;
        while (actual != null) {
            actual.getTrazo().dibujar(g, actual.getColor());
            actual = actual.siguiente;
        }
    }

    public boolean guardarJSON(String nombreArchivo){
        TrazoDTO[] trazos=new TrazoDTO[getLongitud()];
        // recorrer la lista
        Nodo actual = cabeza;
        int fila=0;
        while (actual != null) {
            trazos[fila]=actual.toDTO();
            fila++;
            actual = actual.siguiente;
        }
        return Archivo.guardarJson(nombreArchivo, trazos);

    }

    public void desdeJSON(String nombreArchivo){
        List<TrazoDTO> trazos=Archivo.leerJson(nombreArchivo,new TypeReference<List<TrazoDTO>>() {} );
        cabeza=null;
        for(TrazoDTO dto :trazos){
            Trazo trazo = null;
            switch (TipoTrazo.valueOf(dto.getTipo())) {
                case LINEA:
                    trazo = new Linea(dto.getX1(), dto.getY1(),dto.getX2(),dto.getY2());
                    break;
                case RECTANGULO:
                    trazo = new Rectangulo(dto.getX1(), dto.getY1(),dto.getX2(),dto.getY2());
                    break;
                case OVALO:
                    trazo = new Ovalo(dto.getX1(), dto.getY1(),dto.getX2(),dto.getY2());
                    break;
            }
            if (trazo != null) {
                agregar(new Nodo(trazo, new Color(dto.getRed(), dto.getGreen(), dto.getBlue())));
            }
        }
    }

    // ********** Metodos Estaticos **********

    public static void limpiarPanel(JPanel pnl) {
        Graphics g = pnl.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, pnl.getWidth(), pnl.getHeight());
    }

}
