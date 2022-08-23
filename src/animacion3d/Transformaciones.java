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
public class Transformaciones {
    private float anguloxx;
    private float anguloyy;
    private float angulozz;
    private float zoom;
    private float x;
    private float y;
    double [][][] configuraciones;

    public Transformaciones() {
        this.anguloxx = 0;
        this.anguloyy = 0;
        this.angulozz = 0;
        this.zoom = 1;
        this.x = 0;
        this.y = 0;
        setConfiguraciones();
    }
    public void limpiarTransformaciones() {
        this.anguloxx = 0;
        this.anguloyy = 0;
        this.angulozz = 0;
        this.zoom = 1;
        this.x = 0;
        this.y = 0;
        setConfiguraciones();
    }
    public void setConfiguraciones(){
        configuraciones =new double[][][] {
           {
           { 1, 0, 0, 0 }, 
           { 0, Math.cos(Math.toRadians(anguloxx)),  Math.sin(Math.toRadians(anguloxx))*-1, 0 },
           { 0,Math.sin(Math.toRadians(anguloxx)), Math.cos(Math.toRadians(anguloxx)), 0 },
           { 0, 0, 0, 1 } 
           },
           {
           { Math.cos(Math.toRadians(anguloyy)),0, Math.sin(Math.toRadians(anguloyy)), 0 },
           { 0, 1, 0, 0 }, 
           { -1* Math.sin(Math.toRadians(anguloyy)),0, Math.cos(Math.toRadians(anguloyy)), 0 },
           { 0, 0, 0, 1 } 
           },{
           { Math.cos(Math.toRadians(angulozz)), -1*Math.sin(Math.toRadians(angulozz)),0, 0 },
           { -1* Math.sin(Math.toRadians(angulozz)), Math.cos(Math.toRadians(angulozz)),0, 0 },
           { 0, 0, 1, 0 }, 
           { 0, 0, 0, 1 } 
           },{ 
            { zoom, 0, 0, 0 }, 
            { 0, zoom, 0, 0 }, 
            { 0, 0, zoom, 0 }, 
            { 0, 0, 0, 1 } 
           },{
            { 1, 0, 0, x }, 
            { 0, 1, 0, y }, 
            { 0, 0, 1, 0 }, 
            { 0, 0, 0, 1 }  
        }
    };
    }
    

    public float getAnguloxx() {
        return anguloxx;
    }
    public void setAnguloxx(float anguloxx) {
        this.anguloxx = anguloxx;
        this.configuraciones[0][1][1]=Math.cos(Math.toRadians(anguloxx));
        this.configuraciones[0][1][2]=Math.sin(Math.toRadians(anguloxx))*-1;
        this.configuraciones[0][2][1]=Math.sin(Math.toRadians(anguloxx));
        this.configuraciones[0][2][2]=Math.cos(Math.toRadians(anguloxx));
    }
    public float getAnguloyy() {
        return anguloyy;
    }
    public void setAnguloyy(float anguloyy) {
        this.anguloyy = anguloyy;
        this.configuraciones[1][0][0]=Math.cos(Math.toRadians(anguloyy));
        this.configuraciones[1][0][2]=Math.sin(Math.toRadians(anguloyy));
        this.configuraciones[1][2][0]=-1*Math.sin(Math.toRadians(anguloyy));
        this.configuraciones[1][2][2]=Math.cos(Math.toRadians(anguloyy));
    }

    public float getAngulozz() {
        return angulozz;
    }

    public void setAngulozz(float angulozz) {
        this.angulozz = angulozz;
        this.configuraciones[2][0][0]=Math.cos(Math.toRadians(angulozz));
        this.configuraciones[2][0][1]=-1*Math.sin(Math.toRadians(angulozz));
        this.configuraciones[2][1][0]=-1*Math.sin(Math.toRadians(angulozz));
        this.configuraciones[2][1][1]=Math.cos(Math.toRadians(angulozz));
        
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        this.configuraciones[3][0][0]=this.zoom;
        this.configuraciones[3][1][1]=this.zoom;
        this.configuraciones[3][2][2]=this.zoom;
    }

    public float getX() {
        return x;
        
    }

    public void setX(float x) {
        this.x = x;
        this.configuraciones[4][0][3]=this.x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        this.configuraciones[4][1][3]=this.y;
    }

    public double[][][] getConfiguraciones() {
        return configuraciones;
    }
    
    
}
