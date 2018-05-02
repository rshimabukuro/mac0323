import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
public class Deque<Item> implements Iterable<Item> {
  private class Node{
    Item item;
    Node next,b4;
  }
  private Node last,first;
  private int size;
  // construct an empty deque
  public Deque(){
    size = 0;
    last = null;
    first = null;
  }
  public boolean isEmpty(){
    return size==0;
  }
  public int size(){
    return size;
  }
  public void addFirst(Item item){
    if (item == null)throw new java.lang.NullPointerException();
    Node no = new Node();
    no.item = item;
    if(first!=null){
      first.b4 = no;
    }
    no.next = first;
    no.b4 = null;
    first = no;
    if (size==0)last=no;
    size++;
  }
  public void addLast(Item item){
    if (item == null)throw new java.lang.NullPointerException();
    Node no = new Node();
    no.item = item;
    if(last!=null){
      last.next = no;
    }
    no.b4=last;
    no.next=null;
    last=no;
    if (size==0)first=no;
    size++;
  }
   public Item removeFirst(){
     if(first==null)throw new java.util.NoSuchElementException();
     Item i = first.item;
     first.item = null;
     first=first.next;
     if(first!=null)first.b4 = null;
     size--;
     return i;
   }
   public Item removeLast(){
     if(first==null)throw new java.util.NoSuchElementException();
     Item i = last.item;
     last.item=null;
     last = last.b4;
     if(last!=null)last.next=null;
     size--;
     return i;
   }
   public Iterator<Item> iterator(){
     return new ListItems();
   }
   
   private class ListItems implements Iterator<Item> {
     private Node current = first;
     
     public boolean hasNext() {
       return current!=null;
     }
     
     public Item next() {
       if(current==null) throw new java.util.NoSuchElementException();
       Item i = current.item;
       current = current.next;
       return i;
     }
     
     public void remove() { 
       throw new UnsupportedOperationException(); 
     }
   }
   public static void main(String[] args){
       //Testes em casos chave
       Deque<String> d = new Deque<String>();
       //insere no inicio remove do inicio
       StdOut.print("size: ");StdOut.print(d.size());
       d.addFirst("a");StdOut.print("\n Adding a\n");
       StdOut.print("size: ");StdOut.print(d.size());
       StdOut.print("\n"+" remove: "+d.removeFirst()+"\n");
       StdOut.print("size: ");StdOut.print(d.size());
       //insere no fim remove do fim
       d.addLast("b");StdOut.print("\n Adding b\n");
       StdOut.print("size: ");StdOut.print(d.size());
       StdOut.print("\n"+" remove: "+d.removeLast()+"\n");
       StdOut.print("size: ");StdOut.print(d.size());
       //insere no inicio remove do fim
       d.addFirst("c");StdOut.print("\n Adding c\n");
       StdOut.print("size: ");StdOut.print(d.size());
       StdOut.print("\n"+" remove: "+d.removeLast()+"\n");
       StdOut.print("size: ");StdOut.print(d.size());
       //insere no fim remove do inicio
       d.addLast("d");StdOut.print("\n Adding d\n");
       StdOut.print("size: ");StdOut.print(d.size());
       StdOut.print("\n"+" remove: "+d.removeFirst()+"\n");
       StdOut.print("size: ");StdOut.print(d.size());
   }
}