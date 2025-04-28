import java.awt.Graphics;
import java.util.List;
import java.awt.Color;

import javax.swing.JPanel;

import com.fasterxml.jackson.core.type.TypeReference;

import entidades.TrazoDTO;

public class Dibujo {

    private Nodo cabeza;
    private Nodo nodoSeleccionado;

    public Nodo getNodoSeleccionado() {
        return nodoSeleccionado;
    }

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

    public void dibujar(JPanel pnl, Estado estado) {
        limpiarPanel(pnl);
        // obtener el objeto graficador
        Graphics g = pnl.getGraphics();
        // recorrer la lista
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual == nodoSeleccionado) {
                actual.getTrazo().dibujar(g, actual.getColor(), estado);
            } else {
                actual.getTrazo().dibujar(g, actual.getColor(), Estado.TRAZANDO);
            }
            actual = actual.siguiente;
        }
    }

    public boolean guardarJSON(String nombreArchivo) {
        TrazoDTO[] trazos = new TrazoDTO[getLongitud()];
        // recorrer la lista
        Nodo actual = cabeza;
        int fila = 0;
        while (actual != null) {
            trazos[fila] = actual.toDTO();
            fila++;
            actual = actual.siguiente;
        }
        return Archivo.guardarJson(nombreArchivo, trazos);

    }

    public void desdeJSON(String nombreArchivo) {
        List<TrazoDTO> trazos = Archivo.leerJson(nombreArchivo, new TypeReference<List<TrazoDTO>>() {
        });
        cabeza = null;
        for (TrazoDTO dto : trazos) {
            Trazo trazo = null;
            switch (TipoTrazo.valueOf(dto.getTipo())) {
                case LINEA:
                    trazo = new Linea(dto.getX1(), dto.getY1(), dto.getX2(), dto.getY2());
                    break;
                case RECTANGULO:
                    trazo = new Rectangulo(dto.getX1(), dto.getY1(), dto.getX2(), dto.getY2());
                    break;
                case OVALO:
                    trazo = new Ovalo(dto.getX1(), dto.getY1(), dto.getX2(), dto.getY2());
                    break;
            }
            if (trazo != null) {
                agregar(new Nodo(trazo, new Color(dto.getRed(), dto.getGreen(), dto.getBlue())));
            }
        }
    }

    public boolean seleccionar(int x, int y) {
        nodoSeleccionado = null;
        boolean seleccionado = false;
        Nodo actual = cabeza;

        while (actual != null && !seleccionado) {
            seleccionado = actual.getTrazo().cercano(x, y);
            if (seleccionado) {
                nodoSeleccionado = actual;
            }
            actual = actual.siguiente;
        }
        return seleccionado;
    }

    public void eliminar(Nodo nodo) {
        Nodo anterior = null;
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual == nodo) {
                if (anterior == null) {
                    cabeza = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                return;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
    }

    public void deseleccionar() {
        nodoSeleccionado = null;
    }

    // ********** Metodos Estaticos **********

    public static void limpiarPanel(JPanel pnl) {
        Graphics g = pnl.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, pnl.getWidth(), pnl.getHeight());
    }

}
