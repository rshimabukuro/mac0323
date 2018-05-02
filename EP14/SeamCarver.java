import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;
import java.lang.Math;

public class SeamCarver {
    private Picture img;
    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if(picture == null){throw new NullPointerException();}
        img = picture;
    }
    public Picture picture()                          // current picture
    {
        return img;
    }
    public     int width()                            // width of current picture
    {
        return img.width();
    }
    public     int height()                           // height of current picture
    {
        return img.height();
    }
    
    private int mod(int value,int m){
        if(value>=0){return value%m;}
        else{
            return m + value;
        }
    }
    
    public  double energy(int x, int y)               // energy of pixel at column x and row y
    {
        double xgrad,ygrad;
        if(x<0||x>=width()||y<0||y>=height()){
            throw new IndexOutOfBoundsException();
        }
        xgrad=grad(img.get(mod((x-1),width()),y),img.get(mod((x+1),width()),y));
        ygrad=grad(img.get(x,mod((y-1),height())),img.get(x,mod((y+1),height())));
        return Math.sqrt(xgrad + ygrad);
    }
    
    private double grad(Color a, Color b){
        return Math.pow((b.getBlue()-a.getBlue()),2)+
            Math.pow((b.getRed()-a.getRed()),2)+
            Math.pow((b.getGreen()-a.getGreen()),2);
    }
    
    private double minimo(double a,double b,double c){
        return Math.min(Math.min(a,b),c);
    }
        
    private int minimoIndice(double a,double b,double c){
        double min;
        if(a==-1){min  = Math.min(b,c);}
        else{
            if(c==-1){min  = Math.min(a,b);}
            else{
                min  = Math.min(Math.min(a,b),c);
            }
        }
        if(min == a){return -1;}
        if(min == b){return 0;}
        if(min == c){return 1;}
        return 0;
    }
    
    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        double [][] m = new double[height()][width()];
        for(int j=0;j<height();j++){m[j][0]=energy(0,j);}
        for(int i=1;i<width();i++){
            for(int j=0;j<height();j++){
                if(j==0){
                    m[j][i] = energy(i,j) + Math.min(m[j][i-1],
                                                     m[j+1][i-1]);
                }
                else{
                    if(j==height()-1){
                        m[j][i] = energy(i,j) + Math.min(m[j-1][i-1],
                                                         m[j][i-1]);
                    }else{
                        m[j][i] = energy(i,j) + minimo(m[j-1][i-1],
                                                       m[j][i-1],
                                                       m[j+1][i-1]);
                    }
                }
            }
        }
        int [] ret = new int[width()];
        double minvalue = -1;
        int minIndice = -1;
        for(int j=0;j<height();j++){
            if(minvalue > m[j][width()-1] || minIndice == -1){
                minvalue = m[j][width()-1];
                minIndice = j;
            }
        }
        ret[width()-1] = minIndice;
        for(int i=width()-2; i>=0; i--){
            if(ret[i+1]==height()-1){
                    ret[i] = ret[i+1] + minimoIndice(m [mod((ret[i+1]-1),height())] [i],
                                             m [mod((ret[i+1]),height())] [i],
                                             -1);
            }else{
                if(ret[i+1]==0){
                    ret[i] = ret[i+1] + minimoIndice(-1,m [mod((ret[i+1]),height())] [i],
                                            m [mod((ret[i+1]+1),height())] [i] );
                }else{
                    ret[i] = ret[i+1] + minimoIndice(m [mod((ret[i+1]-1),height())] [i],
                                                     m [mod((ret[i+1]),height())] [i],
                                                     m [mod((ret[i+1]+1),height())] [i] );
                }
            }
        }
        return ret;
    }
    
    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        double [][] m = new double[height()][width()];
        for(int j=0;j<width();j++){m[0][j]=energy(j,0);}
        for(int i=1;i<height();i++){
            for(int j=0;j<width();j++){
                if(j==0){
                    m[i][j] = energy(j,i) + Math.min(m [i-1] [j],
                                                   m [i-1] [mod((j+1),width())]);
                }else{
                    if(j==width()-1){
                        m[i][j] = energy(j,i) + Math.min(m [i-1] [mod((j-1),width())],
                                                         m [i-1] [j]);
                    }else{
                        m[i][j] = energy(j,i) + minimo(m [i-1] [mod((j-1),width())],
                                                       m [i-1] [j],
                                                       m [i-1] [mod((j+1),width())]);
                    }
                }
            }
        }
        int [] ret = new int[height()];
        double minvalue = -1;
        int minIndice = -1;
        for(int j=0;j<width();j++){
            if(minvalue > m[height()-1][j] || minIndice == -1){
                minvalue = m[height()-1][j];
                minIndice = j;
            }
        }
        ret[height()-1] = minIndice;
        for(int i=height()-2; i>=0; i--){
            if(ret[i+1]==0){
                ret[i] = mod(ret[i+1] + minimoIndice(-1,
                         m[i][ret[i+1]],
                         m[i][mod((ret[i+1]+1),width())]),width());
            }else{
                if(ret[i+1]==width()-1){
                    ret[i] = mod(ret[i+1] + minimoIndice(m[i][mod((ret[i+1]-1),width())],
                         m[i][ret[i+1]],
                         -1),width());
                }else{
                    ret[i] = mod(ret[i+1] + minimoIndice(m[i][mod((ret[i+1]-1),width())],
                         m[i][ret[i+1]],
                         m[i][mod((ret[i+1]+1),width())]),width());
                }
            }
        }
        return ret;
    }
    
    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        if(seam == null){throw new NullPointerException();}
        if (seam.length != width()) {throw new IllegalArgumentException();}
        Picture novaImg = new Picture(width(),height()-1);
        for(int i=0; i<img.width(); i++){
            for(int j=0; j<seam[i]; j++){
                novaImg.set(i,j,img.get(i,j));
            }
            for(int j=seam[i]; j < novaImg.height(); j++){
                novaImg.set(i,j,img.get(i,j+1));
            }
        }
        img = novaImg;
    }
    
    public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        if(seam == null){throw new NullPointerException();}
        if (seam.length != height()) {throw new IllegalArgumentException();}
        Picture novaImg = new Picture(width()-1,height());
        for(int i=0; i<img.height(); i++){
            for(int j=0; j<seam[i]; j++){
                novaImg.set(j,i,img.get(j,i));
            }
            for(int j=seam[i]; j < novaImg.width(); j++){
                novaImg.set(j,i,img.get(j+1,i));
            }
        }
        img = novaImg;
    }
    public static void main(String[] args)            // do unit testing of this class
    {
        
    }
}