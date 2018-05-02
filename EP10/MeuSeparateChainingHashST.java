/*************************************************************************
 *  Compilation:  javac MeuSeparateChainingHashST.java
 *  Execution:    java MeuSeparateChainingHashST <alfa inf> <alf sup> arquivo
 *
 *  A symbol table implemented with a separate-chaining hash table.
 * 
 *  % java SeparateChainingHashST
 *
 ************************************************************************/

/**
    The SeparateChainingHashST class represents a symbol table of generic
    key-value pairs. 
                                                                                                                                        
    This uses a separate chaining hash table. It requires that the key type
    overrides the equals() and hashCode() methods. The expected time per
    put, contains, or remove operation is constant, subject to the uniform
    hashing assumption. 

    The size, and is-empty operations take constant time. 
    Construction takes constant time.
*/
import edu.princeton.cs.algs4.SeparateChainingHashST; 

// The SequentialSearchST class represents an (unordered) symbol table of generic key-value pairs. 
import edu.princeton.cs.algs4.SequentialSearchST;

// The Queue class represents a first-in-first-out (FIFO) queue of generic items.
import edu.princeton.cs.algs4.Queue;

// Input. This class provides methods for reading strings and numbers from standard input,
// file input, URLs, and sockets.
// https://www.ime.usp.br/~pf/sedgewick-wayne/stdlib/documentation/index.html
// http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/In.html
import edu.princeton.cs.algs4.In; // arquivo

// This class provides methods for printing strings and numbers to standard output.
// https://www.ime.usp.br/~pf/sedgewick-wayne/stdlib/documentation/index.html
// http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdOut.html
import edu.princeton.cs.algs4.StdOut; 

// Stopwatch. This class is a data type for measuring the running time (wall clock) of a program.
// https://www.ime.usp.br/~pf/sedgewick-wayne/algs4/documentation/index.html
import edu.princeton.cs.algs4.Stopwatch; // arquivo


/** This is an implementation of a symbol table using a hash table.
 * The collisions are resolved using linked lists.
 * Following our usual convention for symbol tables, 
 * the keys are pairwise distinct.
 * <p>
 * Esta Ã© uma implementaÃ§Ã£o de tabela de sÃ­mbolos que usa uma 
 * tabela de espalhamento. As colisÃµes sÃ£o resolvidas por meio 
 * de listas ligadas.
 * Seguindo a convenÃ§Ã£o usual para tabelas de sÃ­mbolos,
 * as chaves sÃ£o distintas duas a duas.
 * <p>
 * For additional documentation, see 
 * <a href="http://algs4.cs.princeton.edu/34hash/">Section 3.4</a> 
 * of "Algorithms, 4th Edition" (p.458 of paper edition), 
 * by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class MeuSeparateChainingHashST<Key, Value> {
    // largest prime <= 2^i for i = 3 to 31
    // not currently used for doubling and shrinking
    // NOTA: Esses valores sÃ£o todas as possÃ­veis dimensÃµes da tabela de hash.
    private static final int[] PRIMES = {
        7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
        32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
        8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
        536870909, 1073741789, 2147483647
    };

    // capacidade inicial
    private static final int INIT_CAPACITY = PRIMES[0];

    // limite inferior default para o fator de carga
    private static final double ALFAINF_DEFAULT = 2;
    
    // limite superior default para o fator de carga
    private static final double ALFASUP_DEFAULT = 10;
    
    // NOTA: indice na tabela de primos correspondente ao valor de 'm'
    private int iPrimes = 0;

    // NOTA: alfa Ã© o fator de carga (= load factor) n/m
    //       no caso do tratamento de colisÃ£o por encadeamento alfa Ã©
    //       o comprimento mÃ©dio das listas.
    //       alfaSup Ã© o limite superior para o fator de carga.
    //       Usado no mÃ©todo put().
    private final double alfaSup;

    // NOTA: alfa Ã© o fator de carga (= load factor) n/m
    //       no caso do tratamento de colisÃ£o por encadeamento alfa Ã©
    //       o comprimento mÃ©dio das listas.
    //       alfaSup Ã© o limite superior para o fator de carga.
    //       Usado no mÃ©todo delete().
    private final double alfaInf;
    

    private int n; // number of key-value pairs
    private int m; // hash table size

    // NOTA: sinta-se Ã  vontade para implementar as listas usando
    // da maneira que vocÃª preferir.
    private SequentialSearchST<Key, Value>[] st; // array of linked-list symbol tables


   /** 
    * Construtor: cria uma tabela de espalhamento 
    * com resoluÃ§Ã£o de colisÃµes por encadeamento. 
    */
    public MeuSeparateChainingHashST() {
        this(INIT_CAPACITY, ALFAINF_DEFAULT, ALFASUP_DEFAULT);
    } 

   /** 
    * Construtor: cria uma tabela de espalhamento 
    * com (pelo menos) m listas.
    */
    public MeuSeparateChainingHashST(int m) {
        this(m, ALFAINF_DEFAULT, ALFASUP_DEFAULT);
    } 

   /** 
    * Construtor: cria uma tabela de espalhamento 
    * com (pelo menos) m listas.
    */
    public MeuSeparateChainingHashST(double alfaInf, double alfaSup) {
        this(INIT_CAPACITY, alfaInf, alfaSup);
    } 
    
    /** 
     * Construtor.
     *
     * Cria uma tabela de hash vazia com PRIMES[iPrimes] listas sendo
     * que iPrimes >= 0 e
     *         PRIMES[iPrimes-1] < m <= PRIMES[iPrimes]
     * (suponha que PRIMES[-1] = 0).
     * 
     * AlÃ©m disso a tabela criada serÃ¡ tal que o fator de carga alfa
     * respeitarÃ¡
     *
     *            alfaInf <= alfa <= alfaSup
     *
     * A primeira desigualdade pode nÃ£o valer quando o tamanho da tabela
     * Ã© INIT_CAPACITY.
     *
     * PrÃ©-condiÃ§Ã£o: o mÃ©todo supÃµe que alfaInf < alfaSup.
     */
    public MeuSeparateChainingHashST(int m2, double alfaInf2, double alfaSup2) {
        // TAREFA: veja o mÃ©todo original e faÃ§a adaptaÃ§Ãµes necessÃ¡rias
        int mfinal=0,i=0;
        for(i=0;mfinal < m2;i++){
            mfinal=PRIMES[i];
        }
        m  = mfinal;
        n=0;
        iPrimes = i-1;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[m];
        for (i = 0; i < m; i++){
            st[i] = new SequentialSearchST<Key, Value>();
        }
        alfaInf = alfaInf2;
        alfaSup = alfaSup2;
    } 
   

    /** 
     *
     * Redimensiona a tabela de hash de modo que ela tenha PRIMES[k]
     * listas e reinsere todos os itens na nova tabela.
     *
     * Assim, o Ã­ndice k corresponde ao valor PRIMES[k] que serÃ¡ o novo 
     * tamanho da tabela.
     */
    private void resize(int k) {
        // TAREFA: veja o mÃ©todo original e faÃ§a adaptaÃ§Ã£o para que
        //         o tamanho da nova tabela seja PRIMES[k].
        if(k<0)return;
        if(k>=PRIMES.length)return;
        MeuSeparateChainingHashST<Key, Value> temp = new MeuSeparateChainingHashST<Key, Value>(PRIMES[k]);
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.iPrimes = temp.iPrimes;
        this.m  = temp.m;
        this.n  = temp.n;
        this.st = temp.st;
    }

    // hash function: returns a hash value between 0 and M-1
    // funÃ§Ã£o de espalhamento: devolve um valor hash entre 0 e M-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    } 

    // return number of key-value pairs in symbol table
    public int size() {
        return n;
    } 

    
    // is the symbol table empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // is the key in the symbol table?
    public boolean contains(Key key) {
        return get(key) != null;
    } 

    // return value associated with key, null if no such key
    public Value get(Key key) {
        int i = hash(key);
        return st[i].get(key);
    } 

    // insert key-value pair into the table
    public void put(Key key, Value val) {
        // TAREFA: veja o mÃ©todo original e faÃ§a adaptaÃ§Ã£o para que
        //         a tabela seja redimensionada se o fator de carga
        //  
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }
        int i = hash(key);
        if (!st[i].contains(key)) n++;
        st[i].put(key, val);
        if((double)n/(double)m > alfaSup){
            resize(iPrimes+1);
        }
    }

    // delete key (and associated value) if key is in the table
    public void delete(Key key) {
        // TAREFA: veja o mÃ©todo original e adapte para que a tabela 
        //         seja redimensionada sempre que o fator de carga for menor que
        //         alfaInf.
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");

        int i = hash(key);
        if (st[i].contains(key)) n--;
        st[i].delete(key);
        if((double)n/m < alfaInf){
            resize(iPrimes-1);
        }        
    } 

    // return keys in symbol table as an Iterable
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }
        return queue;
    } 

    
    /********************************************************************
     *  Alguns mÃ©todos novos
     *
     */
    
    // retorna o tamanha da tabela de hash
    public int sizeST() {
        return st.length;
    } 

    // retorna o maior comprimeno de uma lista
    public int maxLista() {
        int max = 0;
        for(int i=0; i<st.length ;i++){
            if(st[i].size() > max){
                max = st[i].size();
            }
        }
        return max;
    }

    /** ExercÃ­cio 3.4.30 S&W
     *  TAREFA
     *  Teste do Chi quadrado (chi-square statistic).
     *  Este mÃ©todo deve retorna o valor de chi^2 dado por
     *
     *  (m/n)((f_0-n/m)^2 + (f_1-n/m)^2 + ... + (f_{m-1}-n/m)^2)
     * 
     *  onde f_i Ã© o nÃºmero de chaves na tabela com valor de hash i.
     *  Este mecanismo fornece uma maneira de testarmos a hipÃ³tese 
     *  de que a funÃ§Ã£o de hash produz valores aleatÃ³rios. 
     *  Se isto for verdade, para n > c*m, o valor calculado deveria 
     *  estar no intervalo [m-sqrt(n),m+sqrt(n)] com probabilidade 1-1/c  
     */
    public double chiSquare() {
        double chi = 0;
        for(int i=0; i<st.length; i++){
            chi += (((double)st[i].size()) - ((double)n/(double)m))*(((double)st[i].size()) - ((double)n/(double)m));
        }
        chi*=(double)m/(double)n;
        return chi;
    }
    
   /***********************************************************************
    *  Unit test client.
    *  Altere Ã  vontade, pois este mÃ©todo nÃ£o serÃ¡ corrigido.
    ***********************************************************************/
    public static void main(String[] args) {
        //if (args.length != 3) {
        //    showUse();
        //    return;
        //}
        
        String s;
        //double alfaInf = Double.parseDouble(args[0]);
        //double alfaSup = Double.parseDouble(args[1]);
        //String fileName = args[2];

        //=========================================================
        // Testa SeparateChainingHashST
        //In in = new In(fileName);
        
        // crie a ST
        MeuSeparateChainingHashST<String, Integer> st = new MeuSeparateChainingHashST<String, Integer>();
        
        // dispare o cronometro
        Stopwatch sw = new Stopwatch();

        // povoe a ST com palavras do arquivo
        //StdOut.println("Criando a SeparateChainingHashST com as palavras do arquivo '" + args[2] + "' ...");
        /*while (!in.isEmpty()) {
            // Read and return the next line.
            String linha = in.readLine();
            String[] chaves = linha.split("\\W+");
            for (int i = 0; i < chaves.length; i++) {
                if (!st.contains(chaves[i])) {
                    st.put(chaves[i], 1);
                }
                else {
                    st.put(chaves[i], st.get(chaves[i])+1);
                }
            }
        }*/
        StdOut.println("ST contÃ©m " + st.size() + " itens");
        StdOut.println("ST contÃ©m " + st.sizeST() + " itens");
        for(int i=0;i<71;i++){
            st.put(Integer.toString(i), 1);
        }
        StdOut.println("ST contÃ©m " + st.sizeST() + " itens");
        StdOut.println("ST contÃ©m " + st.size() + " itens");
        
        StdOut.println("Hashing com SeparateChainingHashST");
        StdOut.println("ST criada em " + sw.elapsedTime() + " segundos");
        StdOut.println("ST contÃ©m " + st.size() + " itens");
        //in.close();

        //=================================================================================
        StdOut.println("\n=============================================");
        
        // reabra o arquivo
        //in = new In(fileName);
        
        // crie uma ST
        //MeuSeparateChainingHashST<String, Integer> meuST = new MeuSeparateChainingHashST<String, Integer>(alfaInf, alfaSup);

        // dispare o cronometro
        sw = new Stopwatch();

        // povoe  a ST com palavras do arquivo
        //StdOut.println("Criando a MeuSeparateChainingHashST com as palavras do arquivo '" + args[2] + "' ...");
        /*while (!in.isEmpty()) {
            // Read and return the next line.
            String linha = in.readLine();
            String[] chaves = linha.split("\\W+");
            for (int i = 0; i < chaves.length; i++) {
                if (!meuST.contains(chaves[i])) {
                    meuST.put(chaves[i], 1);
                }
                else {
                    meuST.put(chaves[i], meuST.get(chaves[i])+1);
                }
            }
        }*/
        
        // sw.elapsedTime(): returns elapsed time (in seconds) since
        // this object was created.
        /*int n = meuST.size();
        int m = meuST.sizeST();
        double chi2 = meuST.chiSquare();    
        StdOut.println("Hashing com MeuSeparateChainingHashST");
        StdOut.println("ST criada em " + sw.elapsedTime() + " segundos");
        StdOut.println("ST contÃ©m " + n + " itens");
        StdOut.println("Tabela hash tem " + m + " listas");
        StdOut.println("Maior comprimento de uma lista Ã© " + meuST.maxLista());
        StdOut.println("Fator de carga (= n/m) = " + (double) n/m);
        StdOut.printf("Chi^2 = %.2f, [m-sqrt(m),m+sqrt(m)] = [%.2f, %.2f]\n",
                       chi2, (m-Math.sqrt(m)), (m+Math.sqrt(m)));

        in.close();
        
        // Hmm. NÃ£o custa dar uma verificada ;-)
        for (String key: st.keys()) {
            if (!st.get(key).equals(meuST.get(key))) {
                StdOut.println("Opss... " + key + ": " + st.get(key) + " != " + meuST.get(key));
            }
        }
        */
    }


    private static void showUse() {
        String msg = "Uso: meu_prompt> java MeuSeparateChainingHashST <alfa inf> <alfa sup> <nome arquivo>\n"
            + "    <alfa inf>: limite inferior para o comprimento mÃ©dio das listas (= fator de carga)\n"
            + "    <alfa sup>: limite superior para o comprimento mÃ©dio das listas (= fator de carga)\n"
            + "    <nome arquivo>: nome de um arquivo com um texto\n"
            + "          um ST serÃ¡ criada com as palavras do texto.";
        StdOut.println(msg);
    }
}
