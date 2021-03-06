////////////////// ObligSBinTre /////////////////////////////////
import java.util.*;
public class ObligSBinTre<T> implements Beholder<T> {
    private static final class Node<T> // en indre nodeklasse
    {
        private T verdi; // nodens verdi
        private Node<T> venstre, høyre; // venstre og høyre barn
        private Node<T> forelder; // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder) // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }
    } // class Node

    private Node<T> rot; // peker til rotnoden
    private int antall; // antall noder
    private int endringer; // antall endringer
    private final Comparator<? super T> comp; // komparator

    public ObligSBinTre(Comparator<? super T> c) // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi, p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q


        endringer++;
        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null) return false;
        Node<T> p = rot;
        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }
        return false;
    }



    @Override
    public boolean fjern(T verdi) {

        if (verdi == null) {
            return false;
        }
        Node<T> p = rot;
        Node<T> q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi, p.verdi);      // sammenligner
            // går til venstre
            if (cmp < 0) {
                q = p;
                p = p.venstre;
            }
            // går til høyre
            else if (cmp > 0) {
                q = p;
                p = p.høyre;
            } else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {

            if (p.venstre == null && p.høyre == null) {
                if (p == rot) {

                    rot = null;

                } else if (q.høyre == p) {

                    q.høyre = null;

                } else if (q.venstre == p) {

                    q.venstre = null;
                }
            } else {
                Node<T> b;

                if (p.venstre != null) {
                    b = p.venstre;
                } else {
                    b = p.høyre;
                }
                //Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
                if (p == rot) {
                    rot = b;

                } else if (p == q.venstre) {
                    q.venstre = b;
                    b.forelder = q;

                } else {
                    q.høyre = b;
                    b.forelder = q;


                }
            }
        } else  // Tilfelle 3)
        {
            Node<T> s = p;
            Node<T> r = p.høyre;   // finner neste i inorden
            while (r.venstre != null) {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) {
                s.venstre = r.høyre;

            } else {
                s.høyre = r.høyre;

            }
        }

        endringer++;
        antall--;// det er nå én node mindre i treet
        return true;


    }

    public int fjernAlle(T verdi) {
        boolean fjernet = true;
        int antallfjernet = 0;
        while (fjernet) {

            fjernet = fjern(verdi);
            antallfjernet++;


        }

        antallfjernet--;

        return antallfjernet;
    }


    public int antall() {
        return antall;
    }

    public int antall(T verdi) {

        int n = 0;
        if (tom()) {
            return 0;
        }

        if (verdi == null) {
            return 0;
        }


        Stakk<Node<T>> stakk = new TabellStakk<>();

        Node<T> p = rot;
        while (true) {

            if (verdi.equals(p.verdi)) {
                n++;
            }

            if (p.venstre != null) {
                if (p.høyre != null) {
                    stakk.leggInn(p.høyre);
                }
                p = p.venstre;
            } else if (p.høyre != null)  // her er p.venstre lik null
            {
                p = p.høyre;
            } else if (!stakk.tom())     // her er p en bladnode
            {
                p = stakk.taUt();
            } else                       // p er en bladnode og stakken er tom
                break;                   // traverseringen er ferdig
        }

        return n;

    }

    //public int antall(T verdi){


    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public void nullstill() {

        if (tom()) return;            // tomt tre

        Stakk<Node<T>> stakk = new TabellStakk<>();
        Node<T> p = rot;   // starter i roten og går til venstre
        for (; p.venstre != null; p = p.venstre) stakk.leggInn(p);

        while (true) {
            fjern(p.verdi);

            if (p.høyre != null)          // til venstre i høyre subtre
            {
                for (p = p.høyre; p.venstre != null; p = p.venstre) {
                    stakk.leggInn(p);
                }
            } else if (!stakk.tom()) {
                p = stakk.taUt();   // p.høyre == null, henter fra stakken
            } else break;          // stakken er tom - vi er ferdig

        } // while

    }

    private static <T> Node<T> nesteInorden(Node<T> p) {

        if (p == null) {
            return null;
        }

        Node<T> q = p;


        //Dersom  noden er roten, og ikke har hoyre barn
        if (q.forelder == null && q.høyre == null) {
            return null;
        }


        if (q.høyre != null) {

            q = q.høyre;

            while (q.venstre != null) {
                q = q.venstre;
            }

            return q;

            //Dersom q ikke har høyrebarn og er et venstrebarn
        } else if (q.høyre == null && (q == q.forelder.venstre)) {

            return q.forelder;

        } else {
            while (q.forelder != null) {

                //Sjekker om noden er et venstrebarn

                if (q == q.forelder.venstre) {
                    return q.forelder;
                }


                q = q.forelder;

            }
        }

        return null;


    }

    @Override
    public String toString() {
        if (tom()) {
            return "[]";
        }

        if (rot.høyre == null && rot.venstre == null) {
            return "[" + rot + "]";
        }


        StringBuilder stringBuilder = new StringBuilder("[");

        // Finner første Inorden

        Node<T> curr = rot;

        while (curr.venstre != null) {

            curr = curr.venstre;
        }

        // Legger inn første verdien.

        while (nesteInorden(curr) != null) {

            stringBuilder.append(curr.verdi + ", ");

            curr = nesteInorden(curr);

        }

        stringBuilder.append(curr.verdi + "]");

        return stringBuilder.toString();
    }

    public String omvendtString() {
        if (tom()) {
            return "[]";
        }
        if (antall == 1) {
            return "[" + rot + "]";
        }

        int teller = antall;
        TabellStakk<Node<T>> stakk = new TabellStakk<>();

        StringBuilder stringBuilder = new StringBuilder("[");


        //Finner den siste inorden
        Node<T> p = rot;
        for (; p.høyre != null; p = p.høyre) {
            stakk.leggInn(p);
        }

        while (true) {

            if (teller == 1) {
                stringBuilder.append(p.verdi + "]");
            } else {
                stringBuilder.append(p.verdi + ", ");
                teller--;

            }

            if (p.venstre != null)          // til venstre i høyre subtre
            {
                for (p = p.venstre; p.høyre != null; p = p.høyre) {
                    stakk.leggInn(p);
                }
            } else if (!stakk.tom()) {
                p = stakk.taUt();   // p.høyre == null, henter fra stakken
            } else break;          // stakken er tom - vi er ferdig

        } // while

        return stringBuilder.toString();


    }

    public String høyreGren() {
        if (tom()) {
            return "[]";
        }

        if (antall == 1) {
            return "[" + rot.verdi + "]";
        }
        StringBuilder stringBuilder = new StringBuilder("[");


        Node<T> currNode = rot;

        //Hvis roten ikke har et høyre subtre

        if (rot.høyre == null) {

            while (currNode.høyre == null && currNode.venstre != null) {

                stringBuilder.append(currNode + ", ");

                currNode = currNode.venstre;


            }

        }

        while (currNode.høyre != null) {

            stringBuilder.append(currNode.verdi + ", ");

            currNode = currNode.høyre;

            if (currNode.høyre == null && currNode.venstre != null) {

                while (currNode.venstre != null) {


                    stringBuilder.append(currNode.verdi + ", ");

                    currNode = currNode.venstre;

                }
            }

        }


        stringBuilder.append(currNode + "]");

        return stringBuilder.toString();

    }

    public String lengstGren() {

        if (tom()) {
            return "[]";
        }

        Stakk<Node<T>> stakk = new TabellStakk<>();

        Node<T> currNode = rot;

        //finner første inorden;
        while (currNode.venstre != null) {
            currNode = currNode.venstre;
        }

        while (currNode != null) {

            if (currNode.venstre == null && currNode.høyre == null) {

                stakk.leggInn(currNode);

            }

            currNode = nesteInorden(currNode);


        }


        Stakk<Node<T>> stakk1 = new TabellStakk<>();
        Stakk<Node<T>> maxStakk = new TabellStakk<>();


        int maks = 0;

        Node<T> maksNode = null;


        int length = 0;

        while (!stakk.tom()) {


            length = 0;

            currNode = stakk.taUt();
            Node<T> temp = currNode;


            while (currNode.forelder != null) {


                currNode = currNode.forelder;
                antall++;

            }

            //Legger til siste

            stakk1.leggInn(currNode);


            if (length >= maks) {

                maks = length;
                maksNode = temp;
            }

        }

        StringBuilder stringBuilder = new StringBuilder("[");

        maxStakk = new TabellStakk<>();

        //Fyller opp en ny stakk


        while (maksNode != null) {

            maxStakk.leggInn(maksNode);
            maksNode = maksNode.forelder;

        }


        while (!maxStakk.tom()) {

            if (maxStakk.antall() == 1) {
                stringBuilder.append(maxStakk.taUt().verdi + "]");
            } else {

                stringBuilder.append(maxStakk.taUt().verdi + ", ");
            }

        }

        return stringBuilder.toString();

    }

    public String[] grener() {

        if (tom()) {
            return new String[]{};
        }
        if (antall == 1) {
            return new String[]{"[" + rot.verdi + "]"};
        }

        //Finner antall grener ved å finne antall bladnoder.

        int antallBlader = 0;

        //Stakk som skal holde på bladnodene

        Deque<Node<T>> bladNoder = new ArrayDeque<>();

        Node<T> currNode = rot;


        //finner første node inorden

        while (currNode.venstre != null) {

            currNode = currNode.venstre;


        }

        //Finner og teller opp alle bladnoder. Legger de også på stacken.
        while (currNode != null) {

            currNode = nesteInorden(currNode);

            if (currNode == null) {
                break;
            }
            if (currNode.venstre == null && currNode.høyre == null) {

                antallBlader++;
                bladNoder.push(currNode);

            }


        }


        //Opretter en String tabell


        String[] grener = new String[antallBlader];


        int i = 0;

        Node<T> tmp;
        while (!bladNoder.isEmpty()) {

            Deque<Node<T>> deque = new ArrayDeque<>();


            grener[i] = "[";

            tmp = bladNoder.removeLast();

            while (tmp != null) {


                deque.push(tmp);

                tmp = tmp.forelder;
            }

            while (!deque.isEmpty()) {

                if (deque.size() == 1) {

                    grener[i] += deque.pop().verdi + "]";

                } else {

                    grener[i] += deque.pop().verdi + ", ";
                }


            }


            i++;

        }

        return grener;

    }

    public String bladnodeverdier() {
        if (tom()) {
            return "[]";
        }

        if (antall == 1) {
            return "[" + rot + "]";
        }

        StringBuilder stringBuilder = new StringBuilder("[");


        //Finner første inorden

        Node<T> currNode = rot;

        while (currNode.venstre != null) {

            currNode = currNode.venstre;

        }


        StringBuilder stringBuilder1 = addNodes(stringBuilder, currNode);

        return stringBuilder1.replace(stringBuilder1.length() - 2, stringBuilder1.length(), "]").toString();


        // return addNodes(stringBuilder, currNode).toString();


    }

    public StringBuilder addNodes(StringBuilder stringBuilder, Node<T> node) {
        if (node != null) {


            //Sjekker om noden er en bladnode.
            if (node.venstre == null && node.høyre == null) {

                //Sjekker om det er siste bladnoden


                //  stringBuilder.append(node + "]");


                stringBuilder.append(node.verdi + ", ");


            }
 
            node = nesteInorden(node);
            addNodes(stringBuilder, node);
        }

        return stringBuilder;
    }


    public String postString() {

        if (tom()) {
            return "[]";
        }

        if (antall == 1) {
            return "[" + rot + "]";
        }


        Stakk<Node<T>> stakk = new TabellStakk<>();
        Stakk<Node<T>> stakk1 = new TabellStakk<>();

        Node<T> currNode = rot;
        Node<T> p;

        StringBuilder stringBuilder = new StringBuilder("[");

        stakk.leggInn(currNode);

        while (!stakk.tom()) {

            p = stakk.taUt();
            stakk1.leggInn(p);

            if (p.venstre != null) {

                stakk.leggInn(p.venstre);
            }
            if (p.høyre != null) {

                stakk.leggInn(p.høyre);
            }
        }

        while (!stakk1.tom()) {

            if (stakk1.antall() == 1) {

                stringBuilder.append(stakk1.taUt().verdi + "]");
            } else {

                stringBuilder.append(stakk1.taUt().verdi + ", ");
            }


        }

        return stringBuilder.toString();


    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() // konstruktør
        {
            if (!tom()) {

                //Finner førsteinorden
                Node<T> curr = rot;

                while (curr.venstre != null) {
                    curr = curr.venstre;
                }

                while (true) {

                    //Dersom noden er en bladnode
                    if (curr.høyre == null && curr.venstre == null) {

                        break;
                    }

                    curr = nesteInorden(curr);

                }

                p = curr;


            }
        }

        @Override
        public boolean hasNext() {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next() {

            removeOK = true;

            if (endringer != iteratorendringer) {
                throw new ConcurrentModificationException();
            }

            //Sjekker om det ikke er flere igjen
            Node<T> currNode = p;

            if (!hasNext()) {
                throw new NoSuchElementException();
            }


            if (p == null)
                throw new NoSuchElementException("Elementet finnes ikke");


            T value = p.verdi;
            q = p;





            p = nesteInorden(p);


            while (hasNext()) {



                p = nesteInorden(p);

                if(p == null){
                    return value;
                }

                if (p.venstre == null && p.høyre == null) {

                    return value;

                }


            }

            //q = p;

            return value;

        }


        /*

        Tanken bak remove var at

         */

        @Override
        public void remove()
        {

            if(!removeOK){
                throw new IllegalStateException("kan ikke fjernes nå");
            }
            removeOK = false;


            if(q.forelder != null){

            //Sjekker om noden er venstre eller høyre barn
            if(q.forelder.venstre == q){

                q.forelder.venstre = null;

                q.forelder = null;
            } else{

                q.forelder.høyre = null;

                q.forelder = null;
            }
            } else {
                rot = null;


                System.out.println("");
            }

            antall--;
            iteratorendringer++;
            endringer++;
        }
    }







    }
