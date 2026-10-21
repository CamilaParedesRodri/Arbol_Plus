/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arbolplus;

import javax.swing.JOptionPane;

/**
 *
 * @author camil
 */

// Clase ArbolBPlus
public class ArbolBPlus {
  NodoBPlus raiz; // Nodo raíz del árbol B
    int d;      // Grado mínimo del árbol (cada nodo puede tener hasta 2*d claves)

    public ArbolBPlus(int gradoMinimo) {
        this.d = gradoMinimo;                        // Se guarda el grado mínimo
        this.raiz = new NodoBPlus(gradoMinimo, true);    // Se crea la raíz como nodo hoja vacío
    }

   
    //IMPLEMENTACION -------------------------------------------------------------------------------
public void insertar(int clave) {
    if (raiz == null) {
        raiz = new NodoBPlus(d, true);
        raiz.claves[0] = clave;
        raiz.numClaves = 1;
    } else {
        raiz.insertar(clave);

        if (raiz.numClaves > 2 * d) {
            NodoBPlus nuevaRaiz = new NodoBPlus(d, false);
            nuevaRaiz.hijos[0] = raiz;
            nuevaRaiz.dividirHijo(0, raiz);

            // 👇 Actualiza correctamente la clave guía del nuevo nodo raíz
            nuevaRaiz.claves[0] = nuevaRaiz.hijos[1].claves[0];
            nuevaRaiz.numClaves = 1;

            raiz = nuevaRaiz;
        }
    }
}



    //ALGORITMO DE ELIMINACION-----------------------------------------------------------------------
    /*
    ALGORITMO EliminarArbolB (clave: entero)
    LLAMAR eliminarrArbolBRecursivo (raiz, clave)
        // Si la raíz queda vacía (solo puede pasar si tenía 1 clave)
        Si raiz.n == 0 Y NO raiz.esHoja Entonces
                HACER raiz ← raiz.hijos[0] // El único hijo se convierte en la nueva raíz
        Fin Si
    FinFinAlgoritmo
    */
    /*
    ALGORITMO eliminarrArbolBRecursivo (nodo: BTreeNode, clave: entero)
        HACER idx ← nodo.findKey(clave)  // Encuentra posición de la clave
            // **Caso 1: La clave está en este nodo**
            Si (idx < nodo.n Y nodo.claves[idx] == clave) Entonces
                    Si (nodo.esHoja) Entonces
                // **Caso 1a: Eliminación directa en hoja**
                    Para i desde idx+1 hasta nodo.n-1 
                                HACER nodo.claves[i-1] ← nodo.claves[i]//Desplaza claves
                        Fin Para
                        HACER nodo.n ← nodo.n – 1
        Sino
                        // **Caso 1b: Eliminación en nodo interno**
                        LLAMAR eliminarDeNodoInterno(nodo, idx)
                Fin Si
                // **Caso 2: La clave no está en este nodo (continuar búsqueda)**
                Sino
                    Si (nodo.esHoja) Entonces
                        Retornar // La clave no existe en el árbol
                    Fin Si
                    // Verificar si el hijo donde buscar tiene suficientes claves
                    HACER ultimaPagina ← (idx == nodo.n) // ¿Es el último hijo?
                    Si nodo.hijos[idx].n < d Entonces
                        LLAMAR llenar(nodo, idx)// Asegura que el hijo tenga al menos 'd' claves
                    Fin Si
                    // Ajustar índice si hubo fusión
                    Si ultimaPagina Y idx > nodo.n Entonces     
                        LLAMAR eliminarrArbolBRecursivo (nodo.hijos[idx-1], clave)                
                    Sino
                        LLAMAR eliminarrArbolBRecursivo nodo.hijos[idx], clave)
                    Fin Si
                Fin Si
            Fin
    FinAlgoritmo
    */
    /*
    ALGORITMO EliminarArbolBInterno (nodo: BTreeNode, idx: entero)
        HACER clave ← nodo.claves[idx] // Clave a eliminar
            //Caso 3a: Reemplazar con predecesor (máximo del subárbol izquierdo)
            Si (nodo.hijos[idx].n >= d) Entonces
                    HACER predecesor ← obtenerPredecesor(nodo, idx)
                HACER nodo.claves[idx] ← predecesor // Reemplaza clave
        // Elimina predecesor
                HACER eliminarrArbolBRecursivo (nodo.hijos[idx], predecesor)  
            // Caso 3b: Reemplazar con sucesor (mínimo del subárbol derecho)
        Sino 
        Si (nodo.hijos[idx+1].n >= d) Entonces
                    HACER sucesor ← obtenerSucesor(nodo, idx)
                    HACER nodo.claves[idx] ← sucesor // Reemplaza clave
                    // Elimina sucesor
                    LLAMAR eliminarrArbolBRecursivo (nodo.hijos[idx+1], sucesor)  

                // **Caso 3c: Fusión de nodos (ningún hijo tiene suficientes claves)**
                    Sino
                        LLAMAR fusionar(nodo, idx) //Combina nodo.hijos idx y nodo.hijosidx+1
                    LLAMAR eliminarrArbolBRecursivo (nodo.hijos[idx], clave)  // Elimina clave en el nodo fusionado
                    Fin Si
        Fin Si
    FinFinFinAlgoritmo
    */

    //IMPLEMENTACION --------------------------------------------------------------------------------------
    public void eliminar(int clave) {
        // Si el árbol está vacío, muestra un mensaje y sale
        if (raiz == null) {
            JOptionPane.showMessageDialog(null, "El árbol está vacío.");
            return;
        }

        // Llama al método de eliminación en la raíz
        raiz.eliminar(clave);

        // Si después de eliminar la raíz quedó vacía...
        if (raiz.numClaves == 0) {
            if (!raiz.esHoja) {
                raiz = raiz.hijos[0];  // Se baja el árbol: el primer hijo se convierte en la nueva raíz
            } else {
                raiz = null;           // Si no tiene hijos, el árbol queda vacío
            }
        }
    }

    //ALGORITMO DE BUSCAR----------------------------------------------------------------------------------
    /*
    ALGORITMO BuscarArbolBRecursivo (nodo: BTreeNode, clave: entero)
        DEFINIR i  entero
    HACER i <- 0
        // Buscar la posición donde podría estar la clave
        Mientras i < nodo.n Y clave > nodo.claves[i] Hacer
                HACER i <- i + 1
    Fin Mientras
        // Si encontramos la clave, retornar el nodo
        Si (i < nodo.n Y clave == nodo.claves[i]) 
            Entonces
    Retornar nodo
    Fin Si
        // Si es hoja y no encontramos la clave, retornar nulo
        Si (nodo.esHoja) 
            Entonces
            Retornar NULO
        Fin Si
        // Buscar recursivamente en el hijo apropiado
        Retornar BuscarArbolBRecursivo (nodo.hijos[i], clave)
    FinAlgoritmo
    */

    //IMPLEMENTACION--------------------------------------------------------------------------------
    public String buscar(int clave) {
        StringBuilder resultado = new StringBuilder();           // Crea un StringBuilder para construir el resultado de forma eficiente
        boolean encontrado = buscarRecursivo(raiz, clave, resultado);
        // Prepara el mensaje final basado en si se encontró o no la clave
        if (encontrado) {                                       
            resultado.insert(0, "Clave " + clave + " encontrada en el árbol B+.\n\n");// Si se encontró, añade un mensaje de éxito al inicio del resultado
        } else {                                                
            resultado.insert(0, "Clave " + clave + " no encontrada en el árbol B+.\n\n");// Si no se encontró, añade un mensaje de fallo al inicio del resultado
        }
        return resultado.toString();    // Convierte el StringBuilder a String y lo devuelve
    }
   
    public boolean buscarRecursivo(NodoBPlus nodo, int clave, StringBuilder resultado) {
        resultado.append("Buscando en nodo: [");                    // Muestra las claves del nodo actual que se está examinando
        for (int i = 0; i < nodo.numClaves; i++) {
            resultado.append(nodo.claves[i]);
            if (i < nodo.numClaves - 1) resultado.append(", ");
        }
        resultado.append("]\n");

        int i = 0;                                                  // Busca la posición donde debería estar la clave en este nodo
        while (i < nodo.numClaves && clave > nodo.claves[i]) {      //Avanza mientras la clave buscada sea mayor que la clave actual
            i++;
        }

        if (i < nodo.numClaves && clave == nodo.claves[i]) {        // Verifica si encontramos la clave en este nodo
            if (nodo.esHoja) {
                resultado.append("→ Encontrado en hoja.\n");        // La clave está en una hoja
            } else {
                resultado.append("→ Encontrado en nodo interno.\n");// La clave está en un nodo interno
            }
            return true;
        }

        if (nodo.esHoja) {                                          // Si llegamos a una hoja y no encontramos la clave
            resultado.append("→ Llegado a hoja sin encontrar la clave.\n");
            return false;
        } else {                                                    // Si no es hoja, sigue buscando recursivamente en el hijo apropiado
            resultado.append("→ Descendiendo al hijo ").append(i).append("\n");
            return buscarRecursivo(nodo.hijos[i], clave, resultado);
        }
    }
    //IMPLEMENTACION MOSTRAR--------------------------------------------------------------------------
    public String mostrar() {
        return raiz != null ? raiz.toString(0) : "Árbol vacío."; // Imprime desde nivel 0 si hay raíz
    }
}
