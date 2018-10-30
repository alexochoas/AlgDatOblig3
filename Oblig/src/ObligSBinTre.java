////////////////// ObligSBinTre /////////////////////////////////
import java.util.*;
public class ObligSBinTre<T> implements Beholder<T>
{
    private static final class Node<T> // en indre nodeklasse
    {
        private T verdi; // nodens verdi
        private Node<T> venstre, høyre; // venstre og høyre barn
        private Node<T> forelder; // forelder
        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
        {
            this.verdi = verdi;
            venstre = v; høyre = h;
            this.forelder = forelder;
        }
        private Node(T verdi, Node<T> forelder) // konstruktør
        {
            this(verdi, null, null, forelder);
        }
        @Override
        public String toString(){ return "" + verdi;}
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
    public boolean leggInn(T verdi)
    {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    @Override
    public boolean inneholder(T verdi)
    {
        if (verdi == null) return false;
        Node<T> p = rot;
        while (p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }
        return false;
    }

   /* @Override
    public boolean fjern(T verdi)
    {


        if(tom()){
            return false;
        }

        if(verdi == null){
            return false;
        }



        Node<T> currNode = rot;

        int cmp;
        //finner første inorden

        while(currNode.venstre != null){
            currNode = currNode.venstre;
        }

        while(currNode != null){

            cmp = comp.compare(verdi, currNode.verdi);

            //Dersom vi har funnet verdien
            if(verdi.equals(currNode.verdi)){

                //Dersom vi har en bladnode

                if(currNode.venstre == null && currNode.høyre == null){

                    //Sjekker om noden er venstre eller høyre barn
                    if(currNode == currNode.forelder.venstre){
                        //Setter referansen fra forelder til noden til null
                        currNode.forelder.venstre = null;

                        currNode.forelder = null;
                    } else{

                        currNode.forelder.høyre = null;
                        currNode.forelder = null;

                    }

                    antall--;
                    return true;

                }


                //Dersom noden har nøyaktig ett barn

                if((currNode.venstre != null || currNode.høyre != null) &&
                        (currNode.venstre == null || currNode.høyre == null)){

                    //Dersom noden er roten
                    if(currNode == rot){

                        //Sjekker om noden har høyre barn eller venstre barn
                        if(currNode.høyre != null){

                            currNode.høyre.forelder = null;
                            rot = currNode.høyre;
                            currNode.høyre = null;

                        } else{

                            currNode.venstre.forelder = null;
                            rot = currNode.venstre;
                            currNode.venstre = null;

                        }

                        antall--;
                        return true;


                    } else{


                        //Sjekker om noden er høyre eller venstre barn

                        if(currNode.høyre != null){

                            currNode.høyre.forelder = currNode.forelder;

                            currNode.forelder.høyre = currNode.høyre;

                            currNode.høyre = null;

                            currNode.forelder = null;



                        } else{

                            currNode.venstre.forelder = currNode.forelder;

                            currNode.forelder.venstre = currNode.venstre;

                            currNode.venstre= null;

                            currNode.forelder = null;

                        }

                        antall--;
                        return true;

                    }

                }


                //Dersom noden har to barn bytter vi nodens
                // verdi med verdien til neste node inorden.


                if((currNode.venstre != null) && (currNode.høyre != null)){

                    //Bytter verdiene
                    Node<T> r = nesteInorden(currNode);

                    T tmp = currNode.verdi;

                    currNode.verdi = r.verdi;
                    r.verdi = tmp;

                    //fjerner r. r kommer til å være noden legnst til venstre i høyre subtre
                    //til currNode.

                    //Sjekker om r er et venstrebarn eller et høyrebarn
                    //Hvis høyrebarnet til noden ikke har venstrebarn vil dette være nesteinorden

                    if(r == currNode.høyre){

                        if(r.høyre != null){
                        r.høyre.forelder = null;
                        r.høyre = null;

                        } else {

                            r.forelder.høyre = null;
                            r.forelder = null;
                        }



                    } else{

                        r.forelder.venstre = null;
                        r.forelder = null;

                    }

                    antall--;
                    return true;



                }




            }



            currNode = nesteInorden(currNode);

        }

        return false;


    }*/

    @Override
    public boolean fjern(T verdi) {

        if (verdi == null) {
            return false;
        }
        Node<T> p = rot;
        Node<T> q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            // går til venstre
            if (cmp < 0) {
                q = p; p = p.venstre;
            }
            // går til høyre
            else if (cmp > 0) {
                q = p; p = p.høyre;
            }
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
            {

                if(p.venstre == null && p.høyre == null){
                   if(p == rot){

                       rot = null;

                   }

                   else if(q.høyre == p){

                        q.høyre = null;

                    } else if(q.venstre == p){

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
        }
        else  // Tilfelle 3)
        {
            Node<T> s = p;
            Node<T> r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)
            {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p){
                s.venstre = r.høyre;

            }
            else{
                s.høyre = r.høyre;

            }
        }

        antall--;   // det er nå én node mindre i treet
        return true;


    }

    public int fjernAlle(T verdi)
    {
        boolean fjernet = true;
        int antallfjernet = 0;
        while(fjernet){

            fjernet = fjern(verdi);
            antallfjernet++;


        }

        antallfjernet--;

        return antallfjernet;
    }


    public int antall()
    {
        return antall;
    }

    public int antall(T verdi)
    {

        int n = 0;
       if(tom()){
           return 0;
       }

       if(verdi == null){
           return 0;
       }



       Stakk<Node<T>> stakk = new TabellStakk<>();

       Node<T> p = rot;
        while (true)
        {

            if(verdi.equals(p.verdi)){
                n++;
            }

            if (p.venstre != null)
            {
                if (p.høyre != null){
                    stakk.leggInn(p.høyre);
                }
                p = p.venstre;
            }
            else if (p.høyre != null)  // her er p.venstre lik null
            {
                p = p.høyre;
            }
            else if (!stakk.tom())     // her er p en bladnode
            {
                p = stakk.taUt();
            }
            else                       // p er en bladnode og stakken er tom
                break;                   // traverseringen er ferdig
        }

        return n;

    }

   //public int antall(T verdi){








    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public void nullstill()
    {

        if (tom()) return;            // tomt tre

        Stakk<Node<T>> stakk = new TabellStakk<>();
        Node<T> p = rot;   // starter i roten og går til venstre
        for ( ; p.venstre != null; p = p.venstre) stakk.leggInn(p);

        while (true)
        {
           fjern(p.verdi);

            if (p.høyre != null)          // til venstre i høyre subtre
            {
                for (p = p.høyre; p.venstre != null; p = p.venstre)
                {
                    stakk.leggInn(p);
                }
            }
            else if (!stakk.tom())
            {
                p = stakk.taUt();   // p.høyre == null, henter fra stakken
            }
            else break;          // stakken er tom - vi er ferdig

        } // while

    }

    private static <T> Node<T> nesteInorden(Node<T> p)
    {

        if(p == null){
            return null;
        }

        Node<T> q = p;


        //Dersom  noden er roten, og ikke har hoyre barn
        if(q.forelder == null && q.høyre == null){
            return null;
        }



        if(q.høyre != null){

            q = q.høyre;

            while(q.venstre != null){
                q = q.venstre;
            }

            return q;

            //Dersom q ikke har høyrebarn og er et venstrebarn
        } else if(q.høyre == null && (q == q.forelder.venstre)){

            return q.forelder;

        }

        else{
            while(q.forelder != null){

                //Sjekker om noden er et venstrebarn

                if(q == q.forelder.venstre){
                    return q.forelder;
                }


                q = q.forelder;

            }
        }

        return null;




    }

    @Override
    public String toString()
    {
        if(tom()){
            return "[]";
        }

        if(rot.høyre == null && rot.venstre == null ){
            return "[" + rot + "]";
        }


        StringBuilder stringBuilder = new StringBuilder("[");

        // Finner første Inorden

        Node <T> curr = rot;

        while(curr.venstre != null){

            curr = curr.venstre;
        }

        // Legger inn første verdien.

        while(nesteInorden(curr) != null){

            stringBuilder.append(curr.verdi + ", ");

            curr = nesteInorden(curr);

        }

        stringBuilder.append(curr.verdi +"]");

        return stringBuilder.toString();
    }

    public String omvendtString()
    {
        if(tom()){
            return "[]";
        }
        if(antall == 1) {
            return "[" + rot + "]";
        }

        int teller = antall;
        TabellStakk<Node<T>> stakk = new TabellStakk<>();

        StringBuilder stringBuilder = new StringBuilder("[");


        //Finner den siste inorden
        Node<T> p = rot;
        for ( ; p.høyre != null; p = p.høyre){
            stakk.leggInn(p);
        }

        while (true)
        {

            if(teller == 1){
                stringBuilder.append(p.verdi + "]");
            } else{
                stringBuilder.append(p.verdi + ", ");
                teller--;

            }

            if (p.venstre != null)          // til venstre i høyre subtre
            {
                for (p = p.venstre; p.høyre != null; p = p.høyre)
                {
                    stakk.leggInn(p);
                }
            }
            else if (!stakk.tom())
            {
                p = stakk.taUt();   // p.høyre == null, henter fra stakken
            }
            else break;          // stakken er tom - vi er ferdig

        } // while

        return stringBuilder.toString();



    }

    public String høyreGren()
    {
        if(tom()){
            return "[]";
        }

        if(antall == 1){
            return "[" + rot.verdi + "]";
        }
        StringBuilder stringBuilder = new StringBuilder("[");


        Node<T> currNode = rot;

        //Hvis roten ikke har et høyre subtre

        if(rot.høyre == null){

            while(currNode.høyre == null && currNode.venstre != null){

                stringBuilder.append(currNode + ", ");

                currNode = currNode.venstre;


            }

        }

        while(currNode.høyre != null){

            stringBuilder.append(currNode.verdi + ", ");

            currNode = currNode.høyre;

            if(currNode.høyre == null && currNode.venstre != null){

                while(currNode.venstre != null){


                    stringBuilder.append( currNode.verdi + ", ");

                    currNode = currNode.venstre;

                }
            }

        }



        stringBuilder.append(currNode + "]");

        return stringBuilder.toString();

    }

    public String lengstGren()
    {

        if(tom()){
            return "[]";
        }

        Stakk<Node<T>> stakk = new TabellStakk<>();

        Node<T> currNode = rot;

        //finner første inorden;
        while(currNode.venstre != null){
            currNode = currNode.venstre;
        }

        while(currNode != null){

            if(currNode.venstre == null && currNode.høyre == null){

                stakk.leggInn(currNode);

            }

            currNode = nesteInorden(currNode);


        }


        Stakk<Node<T>> stakk1 = new TabellStakk<>();
        Stakk<Node<T>> maxStakk = new TabellStakk<>();




        int maks = 0;

        Node<T> maksNode = null;




        while(!stakk.tom()){


            int length = 0;

            currNode = stakk.taUt();
            Node<T> temp = currNode;


            while(currNode.forelder != null){



                currNode = currNode.forelder;
                antall++;

            }

            //Legger til siste

            stakk1.leggInn(currNode);






            if(length>= maks){

              maks = length;
              maksNode = temp;
            }

        }

        StringBuilder stringBuilder = new StringBuilder("[");

        maxStakk = new TabellStakk<>();

        //Fyller opp en ny stakk


        while (maksNode!= null){

            maxStakk.leggInn(maksNode);
            maksNode = maksNode.forelder;

        }


        while(!maxStakk.tom()){

            if(maxStakk.antall() == 1){
                stringBuilder.append(maxStakk.taUt().verdi + "]");
            }else{

            stringBuilder.append(maxStakk.taUt().verdi + ", ");
            }

        }

        return stringBuilder.toString();

    }

    public String[] grener()
    {

        if(tom()){
            return new String[] {};
        }
        if(antall == 1){
            return new String[] {"[" +rot.verdi + "]"};
        }

        //Finner antall grener ved å finne antall bladnoder.

        int antallBlader = 0;

        //Stakk som skal holde på bladnodene

        Stakk<Node<T>> bladNoder = new TabellStakk<>();

        Node<T> currNode = rot;

        //finner første node inorden

        while(currNode.venstre != null){

            currNode = currNode.venstre;


        }

        //Finner og teller opp alle bladnoder. Legger de også på stacken.
        while(nesteInorden(currNode) != null){

            if(currNode.venstre == null && currNode.høyre == null){

                antallBlader++;
                bladNoder.leggInn(currNode);

            }
                currNode = nesteInorden(currNode);

        }


        //Opretter en String tabell



        String[] grener = new String[antallBlader];


        for(int i = grener.length-1; i >= 0; i--){

            StringBuilder stringBuilder = new StringBuilder("[");

            Node<T> p = bladNoder.taUt();

            while(p.forelder != null){

                stringBuilder.append(p.verdi + ", ");

                p = p.forelder;
            }

            stringBuilder.append(p.verdi + "]");

            grener[i] = stringBuilder.reverse().toString();
        }

        return grener;

    }

    public String bladnodeverdier()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }
    @Override
    public Iterator<T> iterator()
    {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T>
    {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() // konstruktør
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext()
        {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }
    } // BladnodeIterator
} // ObligSBinTre