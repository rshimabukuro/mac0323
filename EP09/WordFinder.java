import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut; 
public class WordFinder{
    SeparateChainingHashST<String,Value> st;
    int nMax;
    String maxS;
    public WordFinder(String[] vetorStrings){
        st = new SeparateChainingHashST<String,Value>();
        int n = vetorStrings.length;
        Value v;
        nMax = 0;
        for(int i=0; i < n; i++){
            String[] splited = (vetorStrings[i]).split("\\s+");
            for(int j=0; j<splited.length; j++){
                v = st.get(splited[j]);
                if(v==null){
                    v = new Value();
                    v.n = 1;
                    v.indice = new Lista(i);
                    st.put(splited[j],v);
                }else{
                    if(!(v.indice.contains(i))){v.n += 1;}
                    v.indice.put(i);
                }
                if(v.n > nMax){
                    nMax = v.n;
                    maxS = splited[j];
                }
            }
        }
    }
    
    public String getMax(){
        return maxS;
    }
    
    public String containedIn(int a, int b){
        Value v;
        for(String s : st.keys()){
            v = st.get(s);
            if(v.indice.contains(a)&&v.indice.contains(b)){
                return s;
            }
        }
        return null;
    }
    
    public int[] appearsIn(String s){
        Value v = st.get(s);
        if(v==null)return new int[0];
        return v.indice.vec();
        
    }
    
    private class Value{
        int n;
        Lista indice;
    }
       
    private class Lista{
        int size;
        Node first;
        
        private class Node{
            int i;
            Node next;
            public Node(int n){
                i = n;
                next = null;
            }
        }
        
        public Lista(int n){
            size = 1;
            first = new Node(n);
        }
        
        public boolean contains(int n){
            Node no = first;
            while(no!=null){
                if(n == no.i){
                    return true;
                }
                no = no.next;
            }
            return false;
        }
        
        public int[] vec(){
            
            int[] v = new int[size];
            int i=size-1;
            Node no = first;
            while(no!=null){
                v[i]=no.i;
                no = no.next;
                i--;
            }
            return v;
        }
        
        //coloca na lista caso ainda não esteja
        public void put(int n){
            if(!contains(n)){
                Node no = new Node(n);
                no.next = first;
                first = no;
                size++;
            }
        }
    }
}
