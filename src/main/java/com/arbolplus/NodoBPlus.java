/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arbolplus;

/**
 *
 * @author camil
 */
public class NodoBPlus {
    
     int[] claves;
    NodoBPlus[] hijos;
    boolean esHoja;
    int numClaves;
    int d;
    NodoBPlus siguiente; // ✅ Puntero a siguiente hoja

    public NodoBPlus(int d, boolean esHoja) {
        this.d = d;
        this.esHoja = esHoja;
        this.claves = new int[2 * d + 1]; // Espacio temporal
        this.hijos = new NodoBPlus[2 * d + 2];
        this.numClaves = 0;
        this.siguiente = null;
    }

    void insertar(int clave) {
    if (esHoja) {
        int i = numClaves - 1;
        while (i >= 0 && claves[i] > clave) {
            claves[i + 1] = claves[i];
            i--;
        }
        claves[i + 1] = clave;
        numClaves++;
    } else {
        int i = numClaves - 1;
        while (i >= 0 && claves[i] > clave) i--;
        i++;

        hijos[i].insertar(clave);

        if (hijos[i].numClaves > 2 * d) {
            dividirHijo(i, hijos[i]);

            // ✅ Solo actualizar la guía si hubo división
            claves[i] = hijos[i + 1].claves[0];
        }
    }
}

    public void dividirHijo(int i, NodoBPlus y) {
    NodoBPlus z = new NodoBPlus(d, y.esHoja);

    // CASO HOJA
    if (y.esHoja) {
        // Copiar las últimas d+1 claves a z
        for (int j = 0; j < d + 1; j++) {
            z.claves[j] = y.claves[j + d];
        }
        z.numClaves = d + 1;

        // Enlazar hojas
        z.siguiente = y.siguiente;
        y.siguiente = z;

        y.numClaves = d;
    } else {
        // Copiar claves al nuevo nodo z (solo la mitad)
        for (int j = 0; j < d; j++) {
            z.claves[j] = y.claves[j + d + 1];
        }

        for (int j = 0; j <= d; j++) {
            z.hijos[j] = y.hijos[j + d + 1];
        }

        y.numClaves = d;
        z.numClaves = d;
    }

    // Mover hijos del padre
    for (int j = numClaves; j >= i + 1; j--) {
        hijos[j + 1] = hijos[j];
    }
    hijos[i + 1] = z;

    // Mover claves guía
    for (int j = numClaves - 1; j >= i; j--) {
        claves[j + 1] = claves[j];
    }

    // Insertar clave guía en el padre
    claves[i] = z.claves[0]; // En B+, se sube la primera clave de z
    numClaves++;
}



    public void eliminar(int clave) {
        int idx = encontrarIndice(clave);
        if (esHoja) {
            if (idx < numClaves && claves[idx] == clave) {
                for (int i = idx; i < numClaves - 1; i++) {
                    claves[i] = claves[i + 1];
                }
                numClaves--;
            }
            return;
        }

        boolean ultima = (idx == numClaves);
        if (hijos[idx].numClaves < d + 1) {
            llenar(idx);
        }

        if (ultima && idx > numClaves) {
            hijos[idx - 1].eliminar(clave);
        } else {
            hijos[idx].eliminar(clave);
        }
    }

    private int encontrarIndice(int clave) {
        int idx = 0;
        while (idx < numClaves && claves[idx] < clave) idx++;
        return idx;
    }

    private void llenar(int idx) {
        if (idx != 0 && hijos[idx - 1].numClaves >= d + 1) {
            tomarDeAnterior(idx);
        } else if (idx != numClaves && hijos[idx + 1].numClaves >= d + 1) {
            tomarDeSiguiente(idx);
        } else {
            if (idx != numClaves) {
                fusionar(idx);
            } else {
                fusionar(idx - 1);
            }
        }
    }

    private void tomarDeAnterior(int idx) {
        NodoBPlus child = hijos[idx];
        NodoBPlus sibling = hijos[idx - 1];

        for (int i = child.numClaves - 1; i >= 0; i--) {
            child.claves[i + 1] = child.claves[i];
        }

        if (!child.esHoja) {
            for (int i = child.numClaves; i >= 0; i--) {
                child.hijos[i + 1] = child.hijos[i];
            }
        }

        child.claves[0] = claves[idx - 1];
        if (!child.esHoja) {
            child.hijos[0] = sibling.hijos[sibling.numClaves];
        }

        claves[idx - 1] = sibling.claves[sibling.numClaves - 1];
        child.numClaves++;
        sibling.numClaves--;
    }

    private void tomarDeSiguiente(int idx) {
        NodoBPlus child = hijos[idx];
        NodoBPlus sibling = hijos[idx + 1];

        child.claves[child.numClaves] = claves[idx];
        if (!child.esHoja) {
            child.hijos[child.numClaves + 1] = sibling.hijos[0];
        }

        claves[idx] = sibling.claves[0];

        for (int i = 1; i < sibling.numClaves; i++) {
            sibling.claves[i - 1] = sibling.claves[i];
        }

        if (!sibling.esHoja) {
            for (int i = 1; i <= sibling.numClaves; i++) {
                sibling.hijos[i - 1] = sibling.hijos[i];
            }
        }

        child.numClaves++;
        sibling.numClaves--;
    }

    private void fusionar(int idx) {
        NodoBPlus child = hijos[idx];
        NodoBPlus sibling = hijos[idx + 1];

        for (int i = 0; i < sibling.numClaves; i++) {
            child.claves[child.numClaves + i] = sibling.claves[i];
        }

        if (!child.esHoja) {
            for (int i = 0; i <= sibling.numClaves; i++) {
                child.hijos[child.numClaves + i] = sibling.hijos[i];
            }
        }

        child.numClaves += sibling.numClaves;

        if (child.esHoja) {
            child.siguiente = sibling.siguiente;
        }

        for (int i = idx + 1; i < numClaves; i++) {
            claves[i - 1] = claves[i];
        }

        for (int i = idx + 2; i <= numClaves; i++) {
            hijos[i - 1] = hijos[i];
        }

        numClaves--;
    }

    public String toString(int nivel) {
        StringBuilder sb = new StringBuilder();
        sb.append("│   ".repeat(nivel)).append("[");
        for (int i = 0; i < numClaves; i++) {
            sb.append(claves[i]);
            if (i < numClaves - 1) sb.append(" | ");
        }
        sb.append("]\n");

        for (int i = 0; i <= numClaves; i++) {
            if (hijos[i] != null) {
                sb.append(hijos[i].toString(nivel + 1));
            }
        }
        return sb.toString();
    }
}
