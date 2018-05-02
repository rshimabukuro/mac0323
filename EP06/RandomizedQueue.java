import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
public class RandomizedQueue<Item> implements Iterable<Item> {
    private class Node{
    Item item;
    Node next;
  }
  private Node first;
  private int size;
  // construct an empty deque
  public  RandomizedQueue(){
    size = 0;
    first = null;
  }
  public boolean isEmpty(){
    return size==0;
  }
  public int size(){
    return size;
  }
  public void enqueue(Item item){
    if (item == null)throw new java.lang.NullPointerException();
    Node no = new Node();
    no.item = item;
    no.next = first;
    first = no;
    size++;
  }
  public Item dequeue(){
    if(size==0)throw new java.util.NoSuchElementException();
    
    int N = StdRandom.uniform(size);int i;
    Node n=first,b4=null;
    
    for(i=0;i<N;i++){
      b4 = n;
      n = n.next;
    }
    Item it = n.item;
    n.item = null;
    if(b4!=null){
        b4.next=n.next;
    }
    else{
        first = n.next;
    }
    size--;
    return it;
  }
   
  public Item sample(){
   if(size==0)throw new java.util.NoSuchElementException();
   Node n=first;
   int N = StdRandom.uniform(size);int i;
    for(i=0;i<N;i++){
      n = n.next;
    }
    return n.item;
  }
   
  public Iterator<Item> iterator(){
    return new ListItems();
  }
   
  private class ListItems implements Iterator<Item> {
    private RandomArray vetorAleatorio = new RandomArray();
    
    private class RandomArray{
      int []vetor;
      int c;
      
      public RandomArray(){
        int i,N,troca;
        vetor = new int[size];
        for(i=0;i<size;i++){
          vetor[i] = i;
        }
        for(i=0;i<size;i++){
          N = StdRandom.uniform(i,size);
          troca = vetor[i];
          vetor[i] = vetor[N];
          vetor[N] = troca;
        }
      }
      public int next(){
        if(!hasNext())throw new java.util.NoSuchElementException();
        int ret = vetor[c];
        c+=1;
        return ret;
      }
      public boolean hasNext() {
        return size!=c;
      }
    }
    
    public boolean hasNext() {
      return vetorAleatorio.hasNext();
    }
    
    public Item next() {
      if(!this.hasNext()) throw new java.util.NoSuchElementException();
      Node n=first;
      int N = vetorAleatorio.next();int i;
      for(i=0;i<N;i++){
        n = n.next;
      }
      return n.item;
    }
    
    public void remove() { 
      throw new UnsupportedOperationException(); 
    }
  }
  public static void main(String[] args){
        int N = Integer.valueOf(args[0]);int cont;
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        // read and enqueue all strings
        String[] str = StdIn.readAllStrings();
        for (cont=0; cont < str.length; cont++){
            q.enqueue(str[cont]);
        }
        //remove and print N strings
        for (cont=0;cont<N;cont++){
            StdOut.print(q.dequeue()+"\n");
            StdOut.print(q.size());
            StdOut.print("\n");
        }
    }
}