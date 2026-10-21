/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.arbolplus_grupo;
import javax.swing.JOptionPane;

/**
 *
 * @author camil
 */

public class ArbolPlus_Grupo {
    
    public static void main(String[] args) {
        int tamanio;

        // Pedir grado mínimo del árbol
        String entrada = JOptionPane.showInputDialog(null, "Ingrese el grado mínimo (d) del Árbol B+:");
        if (entrada == null) {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
            return;
        }

        ArbolBPlus arbol;

        try {
            tamanio = Integer.parseInt(entrada);
            if (tamanio > 1) {
                arbol = new ArbolBPlus(tamanio);
            } else {
                JOptionPane.showMessageDialog(null, "El grado mínimo debe ser mayor que 1.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada no válida.");
            return;
        }

        int opcion;
        do {
            String menu = "MENÚ DEL ÁRBOL B+ (Grado mínimo d = " + tamanio + ")\n"
                        + "1. Insertar clave\n"
                        + "2. Mostrar árbol (por niveles)\n"
                        + "3. Eliminar clave\n"
                        + "4. Buscar clave\n"
                        + "5. Salir";
            try {
                String seleccion = JOptionPane.showInputDialog(null, menu, "Árbol B+", JOptionPane.PLAIN_MESSAGE);
                if (seleccion == null) break; // Usuario canceló
                opcion = Integer.parseInt(seleccion);

                switch (opcion) {
                    case 1:
                        String claveInsertar = JOptionPane.showInputDialog("Ingrese la clave a insertar:");
                        if (claveInsertar != null) {
                            try {
                                int clave = Integer.parseInt(claveInsertar);
                                arbol.insertar(clave);
                                JOptionPane.showMessageDialog(null, "Clave insertada.");
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "La clave debe ser un número entero.");
                            }
                        }
                        break;
                    case 2:
                        String arbolStr = arbol.mostrar();
                        JOptionPane.showMessageDialog(null, 
                            arbolStr.isEmpty() ? "El árbol está vacío." : arbolStr, 
                            "Árbol B+ (por niveles)", 
                            JOptionPane.INFORMATION_MESSAGE);
                        break;
            
                    case 3:
                        String claveEliminar = JOptionPane.showInputDialog("Ingrese la clave a eliminar:");
                        if (claveEliminar != null) {
                            try {
                                int clave = Integer.parseInt(claveEliminar);
                                arbol.eliminar(clave);
                                JOptionPane.showMessageDialog(null, "Clave eliminada.");
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "La clave debe ser un número entero.");
                            }
                        }
                        break;
                    case 4:
                        String claveBuscar = JOptionPane.showInputDialog("Ingrese la clave a buscar:");   //10, 27, 29, 17, 25, 21, 15, 31, 13, 51
                        if (claveBuscar != null) {
                            try {
                                int clave = Integer.parseInt(claveBuscar);
                                String resultadoBusqueda = arbol.buscar(clave); 
                                JOptionPane.showMessageDialog(null, resultadoBusqueda, 
                                    "Resultado de Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "La clave debe ser un número entero.");
                            }
                        }
                        break;
                    case 5:
                        JOptionPane.showMessageDialog(null, "Programa finalizado.");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción inválida.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada no válida.");
                opcion = 0; // para repetir el menú
            }
        } while (opcion != 5);
    }
}


