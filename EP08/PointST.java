import edu.princeton.cs.algs4.StdIn; 
import edu.princeton.cs.algs4.StdOut; 
import java.util.Iterator;
import java.util.Comparator; 
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.MinPQ;

/** This is an implementation of a symbol table whose keys are comparable.
 * The keys are kept in increasing order in an linked list.
 * Following our usual convention for symbol tables, 
 * the keys are pairwise distinct.
 * <p>
 * For additional documentation, see 
 * <a href="http://algs4.cs.princeton.edu/31elementary/">Section 3.1</a> 
 * of "Algorithms, 4th Edition" (p.378 of paper edition), 
 * by Robert Sedgewick and Kevin Wayne.
 *
 */

public class PointST<Value> {
    // atributos de estado 
    
    /** Constructor.
    * Creates an empty symbol table with default initial capacity.
    */
    private RedBlackBST <Point2D,Value> table;
    
    public PointST() {
        table = new RedBlackBST<Point2D,Value>();
    }   
    
    /** Is the key in this symbol table?
      */
    public boolean contains(Point2D key) {
        if (key == null) throw new NullPointerException();
        // escreva seu método a seguir
        return table.contains(key);
    }
    
    /** Returns the number of (key,value) pairs in this symbol table.
      */
    public int size() {
        return table.size();
        // escreva seu método a seguir
    }
    
    /** Is this symbol table empty?
      */
    public boolean isEmpty() {
        return table.isEmpty();
    }
    
    /** Returns the value associated with the given key, 
      *  or null if no such key.
      *  Argument key must be nonnull.
      */
    public Value get(Point2D key) {
        if (key == null) throw new NullPointerException();
        return table.get(key);
    }
    
    
    /** Search for key in this symbol table. 
      * If key is in the table, update the corresponing value.
      * Otherwise, add the (key,val) pair to the table.
      * Argument key must be nonnull.
      * If argument val is null, the key must be deleted from the table.
      */
    public void put(Point2D key, Value val){
        if (key == null) throw new NullPointerException();
        table.put(key,val);
    } 
    
    public Iterable<Point2D> points(){
        return table.keys();
    }
    
    public Iterable<Point2D> range(RectHV rect){
        if (rect == null) throw new NullPointerException();
        Queue<Point2D> q = new Queue<Point2D>();
        for(Point2D p : table.keys()){
            if(((p.x() <= rect.xmax() && p.x() >= rect.xmin()) && 
                 (p.y() <= rect.ymax() && p.y() >= rect.ymin()))){
                q.enqueue(p);
            }
        }
        return q;
    }
    
    public Iterable<Point2D> nearest(Point2D p, int i){
        if (p == null) throw new NullPointerException();
        MinPQ<Point2D> q = new MinPQ<Point2D>(i, new DistComparator(p));
        for(Point2D ponto : table.keys()){
            q.insert(ponto);
            if(q.size()>i){
                q.delMin();
            }
        }        
        return q;
    }
    
     static class DistComparator implements Comparator<Point2D>
    {    
         Point2D  p;
         public DistComparator(Point2D ponto){
             p=ponto;
         }
         
         public int compare(Point2D p1, Point2D p2)
         {
             double v1 = p1.distanceSquaredTo(p);
             double v2 = p2.distanceSquaredTo(p);
             if(v1>v2){return -1;}
             if(v1<v2){return 1;}
             return 0;
         }
     }
     
    public static void main(String[] args) {
        Point2D p;
        p = new Point2D(2,5);
        PointST st;
        st = new PointST<Integer>();
        int i = 4;
        st.put(p,i);
        p = new Point2D(1,5);
        st.put(p,i);
        p = new Point2D(2.5,5);
        st.put(p,i);
        p = new Point2D(3,5);
        st.put(p,i);
        p = new Point2D(4,5);
        st.put(p,i);
        Iterable<Point2D> it = st.nearest(p,2);
        for(Point2D ponto : it){
            StdOut.print(ponto.y());
            StdOut.print(ponto.x());
            StdOut.print("\n");
        }  
    }
}
