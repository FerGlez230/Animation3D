/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animacion3d;

/**
 *
 * @author maryf
 */
public class Matriz {
     private double[][] matriz;
    private int filas;
    private int columnas;
    private double dato;
    private boolean verificacion;

    public Matriz(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new double[this.filas][this.columnas];
    }

    public boolean isVerificacion() {
        return verificacion;
    }
    public void setVerificacion(boolean verificacion) {
        this.verificacion = verificacion;
    }
    public double[][] getMatriz() {
        return matriz;
    }
    public void setMatriz(double[][] matriz) {
        this.matriz = matriz;
    }
    public int getFilas() {
        return filas;
    }
    public void setFilas(int filas) {
        this.filas = filas;
    }
    public int getColumnas() {
        return columnas;
    }
    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }
    public double getDato() {
        return dato;
    }
    public void setDato(double dato) {
        this.dato = dato;
    }
   
    public void llenarmatriz(int i, int j){
        this.matriz[i][j] = this.dato;
    }
    
    public void verificarMultiplicacion(double[][] matriz2)
    {
        //columnas matriz2 x filas matriz
      if (matriz2[0].length != this.matriz.length)
        this.verificacion = false;
      else
        this.verificacion = true;
    }
    public double[][] productoMatrices(double[][] b) throws Exception //bxMatriz
    {
            double[][] result = new double[matriz.length][b[0].length];
            
            for (int i = 0; i < result.length; i++){
                for (int j = 0; j < result[0].length; j++)
                    for (int k = 0; k < matriz[0].length; k++)
                    {
                        result[i][j]+= matriz[i][k] * b[k][j];
                    }
            }
            return result;
    }
}
