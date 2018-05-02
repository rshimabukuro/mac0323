/*****************************
 * Ep2 - MAC0323
 * 
 * Rafael Simões Shimabukuro 
 * Numero Usp : 9293202
 *
 *****************************/
/******************************************************************************
 *  Compilation:  javac BulgingSquares.java
 *  Execution:    java BulgingSquares
 *  Dependencies: StdDraw.java, java.awt.Color
 *
 *  Program draws an optical illusion from Akiyoshi Kitaoka. The center appears 
 *  to bulge outwards even though all squares are the same size. 
 *
 *  meu_prompt > java BulgingSquares
 *
 *  Exercise 14 http://introcs.cs.princeton.edu/java/15inout/
 * 
 ******************************************************************************/

// Standard draw. This class provides a basic capability for creating
// drawings with your programs. It uses a simple graphics model that
// allows you to create drawings consisting of points, lines, and
// curves in a window on your computer and to save the drawings to a
// file.
// https://www.ime.usp.br/~pf/sedgewick-wayne/stdlib/documentation/index.html
// http://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html
import edu.princeton.cs.algs4.StdDraw; // StdDraw.setXscale, StdDraw.setYscale, ...

import java.awt.Color; // StdDraw.WHITE, StdDraw.BLACK

public class BulgingSquares {
    // constantes... vixe! use se desejar
    private static final double XMIN   = -75;
    private static final double XMAX   =  75;
    private static final double YMIN   = -75;
    private static final double YMAX   =  75;
    private static final double MARGIN =   2;
    private static final double RADIUS_MAX =   5;
    private static final double DIAM_MAX   = 2*RADIUS_MAX;
    private static final double RADIUS_MIN = 1.5;
    private static final double DIAM_MIN   = 2*RADIUS_MIN;
       
    public static void miniSquares(double x, double y, int color){
        //não desenha mini quadrados em alguns casos
        if(Math.sqrt(x*x+y*y)>60 && Math.abs(x)>10 && Math.abs(y)>10){
            return;
        }
        if(Math.abs(x)>60||Math.abs(y)>60){
            return;
        }
        
        //decisão da cor dos mini quadrados
        if(color%2==0){
            StdDraw.setPenColor(StdDraw.BLACK);
        }else{
            StdDraw.setPenColor(StdDraw.WHITE);
        }
       
        
        if(x==0||y==0){              //mini quadrados no eixo x ou eixo y
                                     //usando +-3 para deixar uma borda entre 
                                     //o quadrado e o mini quadrado de 0.5
            if(y>0){
                StdDraw.filledSquare(x+3,y-3,1.5);
                StdDraw.filledSquare(x-3,y-3,1.5);
            }else{
                if(y<0){
                    StdDraw.filledSquare(x+3,y+3,1.5);
                    StdDraw.filledSquare(x-3,y+3,1.5);
                }else{
                    if(x>0){
                        StdDraw.filledSquare(x-3,y-3,1.5);
                        StdDraw.filledSquare(x-3,y+3,1.5);
                    }else{
                        if(x<0){
                            StdDraw.filledSquare(x+3,y-3,1.5);
                            StdDraw.filledSquare(x+3,y+3,1.5);
                        }
                    }
                }
            }
        }else{
            if((x>0&&y>0)||(x<0&&y<0)){    //1 ou 3 quadrante
                StdDraw.filledSquare(x+3,y-3,1.5);
                StdDraw.filledSquare(x-3,y+3,1.5);
            }else{                         //2 ou 4 quadrante
                StdDraw.filledSquare(x+3,y+3,1.5);
                StdDraw.filledSquare(x-3,y-3,1.5);
            }
        } 
    }
    
    public static void main(String[] args) {
        // set the scale of the coordinate system
        StdDraw.setXscale(XMIN-MARGIN, XMAX+MARGIN);
        StdDraw.setYscale(YMIN-MARGIN, YMAX+MARGIN);
        StdDraw.enableDoubleBuffering();
        
        // clear the background
        StdDraw.clear(StdDraw.WHITE);

        //cria uma borda preta ao redor do eixo x y
        StdDraw.square(0,0,75);
        
        /*desenha os quadrados pretos (os quadrados brancos serão criados 
        *                              automaticamente já que o fundo é branco)
        */
        int var = -1;         //variavel para intercalar preto e branco
        for(double x=-70; x <= 70; x+=10){
            var++;
            for(double y = 70 - (10*(var%2)); y >= -70; y-=20){
                StdDraw.filledSquare(x,y,5.0);
            }
        }
        
        //miniSquares de cada quadrado grande
        int cor = 1;
        for(double x=-70; x <= 70; x+=10){
            for(double y = 70; y >= -70; y-=10){
                miniSquares(x,y,cor);
                cor++;
            }
        }
        // copy offscreen buffer to onscreen
        StdDraw.show();
    }

} 