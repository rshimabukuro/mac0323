import edu.princeton.cs.algs4.StdIn; 
import edu.princeton.cs.algs4.StdOut; 
import java.util.Iterator;
import java.util.Comparator; 
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.MinPQ;
import java.util.NoSuchElementException;

public class KdTreeST<Value> {
    
    /** Constructor.
    * Creates an empty symbol table with default initial capacity.
    */
    private Kd <Value> table;
    
    public KdTreeST() {
        table = new Kd<Value>();
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
        return table.range(rect);
    }
    
    public Iterable<Point2D> nearest(Point2D p, int i){
        if (p == null) throw new NullPointerException();
        return table.nearest(p,i);
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
        RectHV r = new RectHV(0,1,3,6); 
        //Iterable<Point2D> it = st.range(r);
        //Iterable<Point2D> it = st.nearest(p,2);
        Iterable<Point2D> it = st.points();
        for(Point2D ponto : it){
            StdOut.print(ponto.x());
            StdOut.print(ponto.y());
            StdOut.print("\n");
        }  
    }
    
    public class Kd<Value> {
        private Node root;             // root of BST
        
        private class Node {
            private Point2D key;           // sorted by key
            private Value val;         // associated data
            private Node left, right;  // left and right subtrees
            private boolean color;
            private int size;          // number of nodes in subtree
            
            public Node(Point2D key, Value val, int size,boolean color) {
                this.key = key;
                this.val = val;
                this.size = size;
                this.color = color;
            }
        }
        
        /**
         * Initializes an empty symbol table.
         */
        public Kd() {
        }
        
        /**
         * Returns true if this symbol table is empty.
         * @return {@code true} if this symbol table is empty; {@code false} otherwise
         */
        public boolean isEmpty() {
            return size() == 0;
        }
        
        /**
         * Returns the number of key-value pairs in this symbol table.
         * @return the number of key-value pairs in this symbol table
         */
        public int size() {
            return size(root);
        }
        
        // return number of key-value pairs in BST rooted at x
        private int size(Node x) {
            if (x == null) return 0;
            else return x.size;
        }
        
        /**
         * Does this symbol table contain the given key?
         *
         * @param  key the key
         * @return {@code true} if this symbol table contains {@code key} and
         *         {@code false} otherwise
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public boolean contains(Point2D key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            return get(key) != null;
        }
        
        /**
         * Returns the value associated with the given key.
         *
         * @param  key the key
         * @return the value associated with the given key if the key is in the symbol table
         *         and {@code null} if the key is not in the symbol table
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public Value get(Point2D key) {
            return get(root, key);
        }
        
        private int pcomp(Node n,Point2D a, Point2D b){
            if(n.color){
                if(a.x()>b.x()){return 1;}
                if(a.x()<b.x()){return -1;}
                return 0;
            }else{
                if(a.y()>b.y()){return 1;}
                if(a.y()<b.y()){return -1;}
                return 0;
            }
        }
        
        private Value get(Node x, Point2D key) {
            if (key == null) throw new IllegalArgumentException("called get() with a null key");
            if (x == null) return null;
            int cmp = pcomp(x,key,x.key);
            if      (cmp < 0) return get(x.left, key);
            else if (cmp > 0) return get(x.right, key);
            else              return x.val;
        }
        
        /**
         * Inserts the specified key-value pair into the symbol table, overwriting the old 
         * value with the new value if the symbol table already contains the specified key.
         * Deletes the specified key (and its associated value) from this symbol table
         * if the specified value is {@code null}.
         *
         * @param  key the key
         * @param  val the value
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void put(Point2D key, Value val) {
            if (key == null) throw new IllegalArgumentException("calledput() with a null key");
            if (val == null) {
                return;
            }
            root = put(root, key, val, true);
        }
        
        private Node put(Node x, Point2D key, Value val, boolean color) {
            if (x == null) return new Node(key, val, 1,color);
            int cmp = pcomp(x,key,x.key);
            if (key.x()==x.key.x()&&key.y()==x.key.y()){    
                x.val   = val;
                x.size = 1 + size(x.left) + size(x.right);
                return x;
            }
            if      (cmp < 0) x.left  = put(x.left,  key, val,!color);
            else if (cmp >= 0) x.right = put(x.right, key, val,!color);
            x.size = 1 + size(x.left) + size(x.right);
            return x;
        }

        //NEAREST
        public Iterable<Point2D> nearest(Point2D p,int i){
            MinPQ<Point2D> q = new MinPQ<Point2D>(i, new DistComparator(p));
            nearest(root,p,i,q);
            while(q.size() > i){
                q.delMin();
            }
            return q;
        }
        public void nearest(Node x,Point2D p,int i,MinPQ<Point2D> q){
            if(x==null)return;
            q.insert(x.key);
            nearest(x.left,p,i,q);
            nearest(x.right,p,i,q);
        }
        //RANGE
        private int rcomp(Node x, RectHV rec){
            if(x.color){
                if (x.key.x() < rec.xmin()){return 1;}
                if (x.key.x() > rec.xmax()){return -1;}
                return 0;
            }else{
                if (x.key.y() < rec.ymin()){return 1;}
                if (x.key.y() > rec.ymax()){return -1;}
                return 0;
            }            
        }
        
        public Iterable<Point2D> range(RectHV rec){
            Queue<Point2D> q = new Queue<Point2D>();
            range(root,rec,q);
            return q;
        }
        
        private boolean inrec(Point2D p,RectHV rect){
            if(((p.x() <= rect.xmax() && p.x() >= rect.xmin()) && 
                (p.y() <= rect.ymax() && p.y() >= rect.ymin()))){
                return true;
            }
            return false;
        }        
        
        public void range(Node x, RectHV rec,Queue<Point2D> q){
            if(x==null)return;
            int r=rcomp(x,rec);
            if(r==1)range(x.right, rec, q);
            if(r==-1)range(x.left, rec, q);
            if(r==0){
                if(inrec(x.key,rec))q.enqueue(x.key);
                range(x.right, rec, q);
                range(x.left, rec, q);
            }            
        }
        /**
         * Returns all keys in the symbol table as an {@code Iterable}.
         * To iterate over all of the keys in the symbol table named {@code st},
         * use the foreach notation: {@code for (Point2D key : st.keys())}.
         *
         * @return all keys in the symbol table
         */
        public Iterable<Point2D> keys() {
            Queue<Point2D> q = new Queue<Point2D>();
            keys(root,q);
            return q;
        }
        public void keys(Node x,Queue<Point2D> q){
            if(x==null)return;
            q.enqueue(x.key);
            keys(x.left,q);
            keys(x.right,q);
        }
    }
    
}
