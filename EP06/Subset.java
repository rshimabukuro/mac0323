import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
public class Subset {
    public static void main(String[] args){
        int N = Integer.valueOf(args[0]);int cont;
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        // read and enqueue all strings
        String[] str = StdIn.readAllStrings();
        for (cont=0; cont < str.length; cont++){
            q.enqueue(str[cont]);
        }
        //print N strings
        Iterator<String> it = q.iterator();
        for (cont=0;cont<N;cont++){
            StdOut.print(it.next()+"\n");
        }
    }
}