import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private static int N,opened;
    private WeightedQuickUnionUF u;
    private byte [][] grid;
    public Percolation(int n){                // create n-by-n grid, with all sites initially blocked
        if(n<=0)throw new IllegalArgumentException();
        N = n;
        int j;
        u = new WeightedQuickUnionUF((n*n)+2);
        opened = 0;
        grid = new byte[n][n];
        for(int i=0;i<n;i++){
            for(j=0;j<n;j++){
                grid[i][j] = 1;
            }
        }
   }
    public void open(int row, int col){      // open the site (row, col) if it is not open already
        if(row<0||col<0||row>=N||col>=N)throw new java.lang.IndexOutOfBoundsException();
        if(grid[row][col] != 0){
            grid[row][col] = 0;
            link(row,col);
            opened++;
        }
    }
    public boolean isOpen(int row, int col){
        if(row<0||col<0||row>=N||col>=N)throw new java.lang.IndexOutOfBoundsException();
        return grid[row][col] == 0;
    }
    public boolean isFull(int row, int col){
        if(row<0||col<0||row>=N||col>=N)throw new java.lang.IndexOutOfBoundsException();
        return u.connected(0,(row*N)+col+1);
    }
    public int numberOfOpenSites(){           // number of open sites
        return opened;
    }
    
    private void link(int row,int col){
        int n = (row*N)+col+1;
        if(row==N-1)u.union(n,(N*N)+1);
        if(row==0)u.union(n,0);
        if(row>0){
            if(grid[row-1][col]==0)u.union(n, ((row-1)*N)+col+1);
        }
        if(row<N-1){
            if(grid[row+1][col]==0)u.union(n, ((row+1)*N)+col+1);
        }
        if(col>0){
            if(grid[row][col-1]==0)u.union(n, (row*(N))+col);
        }
        if(col<N-1){
            if(grid[row][col+1]==0)u.union(n, (row*(N))+col+2);
        }
    }

    public boolean percolates(){
        return u.connected(0,(N*N)+1);
    }       
    public static void main(String[] args){// unit testing (required)
        int size = StdIn.readInt();
        int n;
        Percolation p = new Percolation(size);
        while(!p.percolates()){
            n = StdRandom.uniform(size*size);
            p.open(n/size,n%size);
        }
        StdOut.print("done");
        //PercolationVisualizer.draw(p,size);
    }
       
   private class UnionFind{
       private int[] id,sz; //primeira posição do vetor se conecta a toda primeira linha da matriz
       public UnionFind(int n){                      //ultima posição do vetor se conecta a toda a ultima linha da matriz
           id = new int[n];
           sz = new int[n];
           for(int i=0;i<n;i++){
               id[i]=i;
               sz[i]=1;
           }
           sz[0]=1;
           sz[n-1]=1;
       }       
       public boolean connected(int a,int b){
           return find(a)==find(b);
       }
       public int find(int a){
           if(id[a]==a)return a;
           return id[a]=find(id[a]);
       }
       public void union(int a,int b){
           int a1=find(a);
           int b1=find(b);
           if(a1==b1)return;
           if(sz[a1]<sz[b1]){
               int swap = a1;
               a1 = b1;
               b1=swap;
           }
           id[b1]=a1;
           sz[a1]+=sz[b1];           
       }
   }
}