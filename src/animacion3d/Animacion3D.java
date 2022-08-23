/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animacion3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author maryf
 */
public class Animacion3D extends JFrame implements Runnable {

    private BufferedImage buffer, bufferImg;
    private Graphics2D graphics;
    private int STEP=2;
    private Thread hilo;
    private Transformaciones transformaciones;
    private Matriz matriz;
    private double[] []temp ;
    private double[][][]tempConf, tempFig, fondo;
    private Figuras figuras;
    private int tipoDibujo, xd1, yd1, xd2, yd2;
    private int xOrigen, yOrigen, figura=0, contador=0;
    private final int CUBOSIMPLE=0, ELE=1, LINEA=2, CUBO=3, ZETA=4, TE=5;
    private Color[] colores={Color.CYAN,Color.YELLOW, Color.BLUE,Color.GREEN, new Color(128, 0, 128), Color.YELLOW, Color.BLACK, Color.PINK};
    private Color[] coloresFondo;
    private int[] longitudesFondo;
    private int bandera;
    public Animacion3D(){
        setSize(440,600);
        setTitle("Animación 3D");
        setLayout(null);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        buffer=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        bufferImg=new BufferedImage(600,800,BufferedImage.TYPE_INT_ARGB_PRE);
        graphics=bufferImg.createGraphics();
        transformaciones=new Transformaciones();
        hilo=new Thread(this);
        xOrigen=this.getWidth()/2;
        yOrigen=0;
        temp=new double[4][4];
        tipoDibujo=0;
        coloresFondo=new Color[10];
        bandera=0;
        longitudesFondo=new int[10];
        fondo=new double[100][100][4];
        tempConf=new double[5][4][4];
        tempFig=new double[5][4][4];
        matriz=new Matriz(4,4);
        figuras=new Figuras(3);
        setVisible(true);
    }
    public static void main(String[] args) {
        Animacion3D animacion=new Animacion3D();
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        transformaciones.limpiarTransformaciones();
        bufferImg.getGraphics().clearRect(0, 0, this.getWidth(), this.getHeight());
       
        hilo.start();
        
        this.getGraphics().drawImage(bufferImg,0, 0, this);
    }
    @Override
    public void update(Graphics g){
        bufferImg.getGraphics().clearRect(0, 0, this.getWidth(), this.getHeight());
        if(bandera>6){
        if(bandera==8) dibujarFondo();
        dibujar(figura);
        }else{   
            dibujarMalla(0,230,440,368,10,Color.WHITE, bandera);
            dibujarTetris(bandera);
        }
        
        this.getGraphics().drawImage(bufferImg,0, 0, this);
    }
    public void pasarTemp(){
        for (int i = 0; i < 4; i++)
               {
                   for (int j = 0; j < 4; j++)
                   {
                       matriz.setDato(temp[i][j]); 
                       matriz.llenarmatriz(i, j);
                   }
               }
    }
    private void dibujar(int f) {
        switch(f){
            case CUBOSIMPLE: tempFig=figuras.getCuboSimple(); break;
            case ELE: tempFig=figuras.getEle(); break;
            case LINEA: tempFig=figuras.getLinea(); break;
            case CUBO: tempFig=figuras.getCubo(); break;
            case ZETA: tempFig=figuras.getZeta(); break;
            case TE: tempFig=figuras.getTe(); break;
        }
        
        tempConf=transformaciones.getConfiguraciones();
        System.out.println("config"+tempConf[4][1][3]);
        for (int i = 0; i < 4; i++)
           {
               for (int j = 0; j < 4; j++)
               {
                   matriz.setDato(tempConf[0][i][j]); 
                   matriz.llenarmatriz(i, j);
               }
           }
        try {
            temp = matriz.productoMatrices(tempConf[1]);
        } catch (Exception ex) {
            System.out.println("Nel, no se pudo "+ex.getMessage());
        }
        for (int k = 2; k < tempConf.length; k++)
           {
               pasarTemp();
               try {
                   temp = matriz.productoMatrices(tempConf[k]);
               } catch (Exception ex) {
                   Logger.getLogger(Animacion3D.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
        
        for (int k = 0; k < tempFig.length; k++)
           {
               pasarTemp();
               if(tipoDibujo==0){
               xd1 = (int) (temp[0][3] - temp[2][3] * Math.cos(Math.toRadians(30))+xOrigen);
                yd1 = (int) (yOrigen+temp[1][3] - temp[2][3] * Math.sin(Math.toRadians(30)) );
               }else{
                   
                   xd1 = xOrigen+(int) (temp[0][3]);
                    yd1 = (int) (yOrigen+temp[1][3] );
                    fondo[contador][k][0]=xd1;
                    fondo[contador][k][1]=yd1;
                    coloresFondo[contador]=colores[f];
                    longitudesFondo[contador]=tempFig.length;
               }
               try {
                   temp = matriz.productoMatrices(tempFig[k]);
               } catch (Exception ex) {
                   Logger.getLogger(Animacion3D.class.getName()).log(Level.SEVERE, null, ex);
               }
               if(tipoDibujo==0){
               xd2 = (int) (temp[0][3] - temp[2][3] * Math.cos(Math.toRadians(30))+xOrigen);
                yd2 = (int) (yOrigen+temp[1][3] - temp[2][3] * Math.sin(Math.toRadians(30)) );
               }else{
                   xd2 = xOrigen+(int) (temp[0][3]);
                    yd2 = (int) (yOrigen+temp[1][3] );
                    fondo[contador][k][2]=xd2;
                    fondo[contador][k][3]=yd2;
               }
                System.out.println("x1:"+xd1+" y1:"+yd1+" x2:"+xd2+" y2:"+yd2);
                bresenhamLine(xd1,yd1,xd2,yd2,colores[f]);
           }
    }
    public void putPixel(int x, int y, Color c){
        if(x<0||y>=this.getHeight()) return;
        buffer.setRGB(0, 0, c.getRGB());
        bufferImg.getGraphics().drawImage(buffer,x, y, this);
    }
    public void bresenhamLine(int x1, int y1, int x2, int y2, Color c){
        int d1, d2, p, xk, yk;
        int dx, dy, stepy, stepx;
        if(x2<x1&&y2<y1){
            xk=x1; x1=x2; x2=xk;
            yk=y1; y1=y2; y2=yk;
        }
        dx=x2-x1;
        dy=y2-y1;
        if (dy < 0) { 
            dy = -dy; stepy = -1; 
          } 
        else stepy = 1;
          if (dx < 0) {  
            dx =-dx; stepx = -1; 
          } 
          else stepx=1;
        if(dx>dy){
                d1=2*dy;
                d2=2*(dy-dx);
                putPixel(x1, y1, c);
                p=2*dy-dx;
                xk=x1; 
                yk=y1;
                while(xk!=x2){
                    if(p>0){
                        xk+=stepx; yk+=stepy;
                        putPixel(xk,yk,c);
                        p=p+d2;
                    }else{
                        xk+=stepx; 
                        putPixel(xk,yk,c);
                        p=p+d1;
                    }
                }
        }else{
                //System.out.println("Hola");
                d1=2*dx;
                d2=2*(dx-dy);
                putPixel(x1, y1, c);
                p=2*dy-dx;
                xk=x1; 
                yk=y1;
                while(yk!=y2){
                    if(p>0){
                        xk+=stepx; yk+=stepy;
                        putPixel(xk,yk,c);
                        p=p+d2;
                    }else{
                        yk+=stepy; 
                        putPixel(xk,yk,c);
                        p=p+d1;
                    }
                } 
        }
          
    }
    
    @Override
    public void run() {
        while(true){
        xOrigen=this.getWidth()/2;
        yOrigen=0;
        tipoDibujo=0;
        bandera=0;
        transformaciones.limpiarTransformaciones();
            contador=0;
        for(int i=0; i<7; i++){
            actualiza(600);
            bandera++;
        }
        
        yOrigen=0;
        primeraFigura();
        bandera=8;
        septimaFigura();
        segundaFigura();
        terceraFigura();
        cuartaFigura();
        sextaFigura();
        quintaFigura();
        
        eliminarFila();
        bandera=10;
        yOrigen=this.getHeight()/2;
        cubo();
        }
    }
    private void cubo() {
        figura=CUBOSIMPLE;
        transformaciones.setX(0);
        transformaciones.setY(-300);
        for(int i=0; i<this.getHeight()/2; i+=20){
            actualiza(50);
            transformaciones.setY(transformaciones.getY()+20);
        }
        transformaciones.setZoom(2);
        transformaciones.setX(0);
        transformaciones.setY(0);
        for(int i=0; i<360; i+=10){
            actualiza(50);
            transformaciones.setAngulozz(i);
        }
        transformaciones.setZoom((float) 2.5);
        for(int i=0; i<360; i+=10){
            actualiza(50);
            transformaciones.setAnguloyy(i);
        }
        transformaciones.setZoom(3);
        for(int i=0; i<360; i+=10){
            actualiza(50);
            transformaciones.setAnguloxx(i);
        }
        transformaciones.limpiarTransformaciones();
    }
    private void primeraFigura(){
        figura=TE;
        transformaciones.setX(0);
        transformaciones.setY(60);
        transformaciones.setAnguloxx(-180);
        for(int i=0; i<10; i++){
            actualiza(200);
            transformaciones.setY(transformaciones.getY()-20);
        }
        transformaciones.setX(transformaciones.getY());
        transformaciones.setY(0);
        transformaciones.setAngulozz(-90);
        for(int i=0; i<7; i++){
            actualiza(200);
            transformaciones.setX(transformaciones.getX()-20);
        }
        transformaciones.setAnguloxx(0);
        transformaciones.setAngulozz(0);
        transformaciones.setY(230);
        transformaciones.setX(0);
        for(int i=0; i<15; i++){
            actualiza(200);
            transformaciones.setY(transformaciones.getY()+20);
            if(i%2==0)
                transformaciones.setX(transformaciones.getX()-20);
        }
        tipoDibujo=1;
        contador++;
        actualiza(500);
    }
    private void segundaFigura(){
        figura=ZETA;
        tipoDibujo=0;
        transformaciones.setAnguloxx(0);
        transformaciones.setAngulozz(0);
        transformaciones.setY(60);
        transformaciones.setX(0);
        for(int i=0; i<10; i++){
            actualiza(200);
            transformaciones.setY(transformaciones.getY()+20);
        }
        transformaciones.setAngulozz(90);
        transformaciones.setY(0);
        transformaciones.setX(transformaciones.getY()-250);
        for(int i=0; i<14; i++){
            actualiza(200);
            if(i==12){ transformaciones.setX(transformaciones.getX()-19);
                transformaciones.setY(transformaciones.getY()+10);
            }
            else{ transformaciones.setX(transformaciones.getX()-20);
            if(i%2==0)
                
                transformaciones.setY(transformaciones.getY()+20);
            }
        }
        tipoDibujo=2;
        
        contador++;
        actualiza(300);
        dibujarFondo();
    }
    private void terceraFigura(){
        figura=LINEA;
        tipoDibujo=0;
        transformaciones.setAnguloxx(0);
        transformaciones.setAngulozz(0);
        transformaciones.setY(60);
        transformaciones.setX(0);
        for(int i=0; i<22; i++){
            actualiza(200);
            if(i==8)
                transformaciones.setX(transformaciones.getX()-2);
                
            transformaciones.setY(transformaciones.getY()+20);
        }
        
        tipoDibujo=2;
        contador++;
        actualiza(300);
        dibujarFondo();
    }
    private void quintaFigura(){
        figura=ELE;
        tipoDibujo=0;
        transformaciones.setAnguloxx(0);
        transformaciones.setAngulozz(0);
        transformaciones.setY(30);
        transformaciones.setX(0);
        for(int i=0; i<9; i++){
            actualiza(200);
            transformaciones.setY(transformaciones.getY()+20);
        }
        transformaciones.setY(0);
        transformaciones.setX(transformaciones.getY()+200);
        transformaciones.setAngulozz(-90);
        for(int i=0; i<7; i++){
            actualiza(200);
            transformaciones.setX(transformaciones.getX()+20);
        }
        transformaciones.setAngulozz(180);
        transformaciones.setY(-360);
        transformaciones.setX(-50);
        for(int i=0; i<11; i++){
            actualiza(200);
            if(i%3==0) transformaciones.setX(transformaciones.getX()+15);
            transformaciones.setY(transformaciones.getY()-20);
            if(i==5){
                transformaciones.setY(transformaciones.getY()-9);
                transformaciones.setX(transformaciones.getX()-3);
            }
        }
        tipoDibujo=2;
        contador++;
        actualiza(300);
        dibujarFondo();
    }
    private void cuartaFigura(){
        figura=CUBO;
        tipoDibujo=0;
        transformaciones.setAnguloxx(0);
        transformaciones.setAngulozz(0);
        transformaciones.setY(60);
        transformaciones.setX(0);
        for(int i=0; i<23; i++){
            actualiza(200);
            if(i==10||i==15||i==5)
                transformaciones.setX(transformaciones.getX()-20);
            if(i==8){
                transformaciones.setX(transformaciones.getX()-37);
                transformaciones.setY(transformaciones.getY()+10);}
            transformaciones.setY(transformaciones.getY()+20);
        }
        
        tipoDibujo=2;
        contador++;
        actualiza(300);
        dibujarFondo();
    }
    private void sextaFigura(){
        figura=CUBO;
        tipoDibujo=0;
        transformaciones.setAnguloxx(0);
        transformaciones.setAngulozz(0);
        transformaciones.setY(60);
        transformaciones.setX(0);
        for(int i=0; i<23; i++){
            actualiza(200);
            if(i==10||i==15||i==5)
                transformaciones.setX(transformaciones.getX()+10);
            if(i==8){
                //transformaciones.setX(transformaciones.getX()+10);
                transformaciones.setY(transformaciones.getY()+10);}
            transformaciones.setY(transformaciones.getY()+20);
        }
        
        tipoDibujo=2;
        contador++;
        actualiza(300);
        dibujarFondo();
    }
    private void septimaFigura(){
        figura=LINEA;
        tipoDibujo=0;
        transformaciones.setAnguloxx(0);
        transformaciones.setAngulozz(0);
        transformaciones.setY(60);
        transformaciones.setX(0);
        for(int i=0; i<10; i++){
            actualiza(200);
            if(i%2==0) transformaciones.setX(transformaciones.getX()+20);
            transformaciones.setY(transformaciones.getY()+20);
            
        }
        transformaciones.setAnguloxx(0);
        transformaciones.setAngulozz(90);
        transformaciones.setY(-200);
        transformaciones.setX(-300);
        for(int i=0; i<13; i++){
            actualiza(200);
            if(i==5){transformaciones.setX(transformaciones.getX()-28);
                transformaciones.setY(transformaciones.getY()+15);
            }
            transformaciones.setX(transformaciones.getX()-20);
        }
        tipoDibujo=2;
        contador++;
        actualiza(300);
        dibujarFondo();
    }
    public void dibujarFondo(){
        for(int i=1; i<=contador;i++){
            for(int j=0; j<longitudesFondo[contador]; j++){
                    bresenhamLine((int)fondo[i][j][0],(int)fondo[i][j][1],
                            (int)fondo[i][j][2],(int)fondo[i][j][3],coloresFondo[i]);
                
            }
        }
    }
    public void eliminarFila(){
        for(int i=1; i<=contador;i++){
            for(int j=0; j<longitudesFondo[contador]; j++){
                fondo[i][j][1]+=31;
                fondo[i][j][3]+=31;
            }
        }
        actualiza(500);
    }
    public void dibujarMalla(int x1, int y1, int x2, int y2, int paso, Color c, int cont){
        int temp=x2;
        for(int i=x1; i<=x2; i+=paso){
            bresenhamLine(i,y1,temp,y2, c);
            temp-=paso;
        }
        temp=y2;
        for(int i=y1; i<=y2; i+=(paso/2)){
            bresenhamLine(x1,i,x2,temp, c);
            temp-=(paso/2);
        }
        graficarPuntos2Dx(funcionParametrica2D(0, (float) (14*Math.PI),300, 0,1), colores[cont], -200, 50);
        graficarPuntos2Dx(funcionParametrica2D(0, (float) (14*Math.PI),300, 0,1), colores[cont], 200, 50);
        graficarPuntos2Dx(funcionParametrica2D(0, (float) (14*Math.PI),300, 0,1), colores[cont], 0, 50);
        graficarPuntos2Dx(funcionParametrica2D(0, (float) (14*Math.PI),300, 0,1), colores[cont], -200, 600);
        graficarPuntos2Dx(funcionParametrica2D(0, (float) (14*Math.PI),300, 0,1), colores[cont], 200, 600);
        graficarPuntos2Dx(funcionParametrica2D(0, (float) (14*Math.PI),300, 0,1), colores[cont], 0, 600);
        
    }
    public void dibujarTetris(int tipo){
        switch (tipo) {
            case 0:
                fillRectangle(25, 262, 85, 292, Color.CYAN);
                fillRectangle(40, 292, 70, 337, Color.CYAN);
                break;
            case 1:
                fillRectangle(100, 262, 130, 337, Color.YELLOW);
                fillRectangle(130, 262, 145, 285, Color.YELLOW);
                fillRectangle(130, 300, 145, 308, Color.YELLOW);
                fillRectangle(130, 323, 145, 337, Color.YELLOW);
                break;
            case 2:
                fillRectangle(160, 262, 220, 292, Color.BLUE);
                fillRectangle(175, 292, 205, 337, Color.BLUE);
                break;
            case 3:
                fillRectangle(235, 262, 265, 337, Color.GREEN);
                fillRectangle(265, 262, 280, 277, Color.GREEN);
                fillRectangle(280, 262, 295, 300, Color.GREEN);
                fillRectangle(265, 292, 280, 308, Color.GREEN);
                fillRectangle(275, 308, 295, 337, Color.GREEN);
                break;
            case 4:
                fillRectangle(310, 262, 340, 337, colores[4]);
                break;
            case 5:
                fillRectangle(355, 262, 415, 277, Color.ORANGE);
                fillRectangle(355, 277, 385, 298, Color.ORANGE);
                fillRectangle(355, 298, 385, 313, Color.ORANGE);
                fillRectangle(385, 298, 415, 337, Color.ORANGE);
                fillRectangle(355, 322, 385, 337, Color.ORANGE);
                break;
            case 6:
                fillRectangle(25, 262, 85, 292, Color.CYAN);
                fillRectangle(40, 292, 70, 337, Color.CYAN);
                
                fillRectangle(100, 262, 130, 337, Color.YELLOW);
                fillRectangle(130, 262, 145, 285, Color.YELLOW);
                fillRectangle(130, 300, 145, 308, Color.YELLOW);
                fillRectangle(130, 323, 145, 337, Color.YELLOW);
                
                fillRectangle(160, 262, 220, 292, Color.BLUE);
                fillRectangle(175, 292, 205, 337, Color.BLUE);
                
                fillRectangle(235, 262, 265, 337, Color.GREEN);
                fillRectangle(265, 262, 280, 277, Color.GREEN);
                fillRectangle(280, 262, 295, 300, Color.GREEN);
                fillRectangle(265, 292, 280, 308, Color.GREEN);
                fillRectangle(275, 308, 295, 337, Color.GREEN);
                
                fillRectangle(310, 262, 340, 337, colores[4]);
                
                fillRectangle(355, 262, 415, 277, Color.ORANGE);
                fillRectangle(355, 277, 385, 298, Color.ORANGE);
                fillRectangle(355, 298, 385, 313, Color.ORANGE);
                fillRectangle(385, 298, 415, 337, Color.ORANGE);
                fillRectangle(355, 322, 385, 337, Color.ORANGE);
                break;
        }
    }
    public void actualiza(int tiempo){
        try {
            update(getGraphics());
            hilo.sleep(tiempo);
        } catch (InterruptedException ex) {
            Logger.getLogger(Animacion3D.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void fillRectangle(int x1, int y1, int x2, int y2, Color c){
        bresenhamLine(x1, y1, x2, y1, c);
        bresenhamLine(x1, y2, x2, y2, c);
        bresenhamLine(x1, y1, x1, y2, c);
        bresenhamLine(x2, y1, x2, y2, c);
        if (x2 < x1 && y2 < y1) {
            int t;
            t = x1;
            x1 = x2;
            x2 = t;
            t = y1;
            y1 = y2;
            y2 = t;
        }
        for (int i = x1 + 1; i <= x2; i++) {
            bresenhamLine(i, y1 + 1, i, y2 - 1, c);
        }
    }
    public float[][] funcionParametrica2D(float inicio, float fin, int puntos, int tipoX, int tipoY){
        int cont=0;
        float t=0;
        float paso=(fin-inicio)/puntos;
        System.out.println("paso:"+paso);
        float [][] res=new float [puntos+1][2];
        while(cont<=puntos){
            t=(inicio+(cont*paso));
            System.out.print(" t:"+t);
            res[cont][0]=(int)calculo(t,tipoX);;
            
            res[cont][1]=(int)calculo(t,tipoY);
            cont++;
        }
        return res;
    }
    public float calculo(float x, int tipo){
        float y=0;
        switch(tipo){
            
            case 0:
                y=(float)((17*Math.cos(x))+(7*Math.cos(2.428571*x)))*3;
                break;
            case 1:
                y=(float)((17*Math.sin(x))-(7*Math.sin(2.428571*x)))*3;
                break;
        }
        return y;
    }
    public void graficarPuntos2Dx(float [][]matriz, Color c, int x, int y){
        for(int i=0; i<matriz.length-1; i++){
            bresenhamLine(xOrigen+x+(int)matriz[i][0],yOrigen+y+(int)matriz[i][1],xOrigen+x+(int)matriz[i+1][0],yOrigen+y+(int)matriz[i+1][1], c);
        }
    }

    
}
