import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        ObligSBinTre<Character> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        char[] verdier = "IATBHJCRSOFELKGDMPQN".toCharArray();
        for (char c : verdier) tre.leggInn(c);
        System.out.println(tre.høyreGren() + " " + tre.lengstGren());
        // Utskrift: [I, T, J, R, S] [I, A, B, H, C, F, E, D]
        String[] s = tre.grener();
        for (String gren : s) System.out.println(gren);
        // Utskrift:
        // [I, A, B, H, C, F, E, D]
        // [I, A, B, H, C, F, G]
        // [I, T, J, R, O, L, K]
        // [I, T, J, R, O, L, M, N]
        // [I, T, J, R, O, P, Q]
        // [I, T, J, R, S]
        System.out.println(tre.bladnodeverdier());// Utskrift: [D, G, K, N, Q, S]
        // En for-alle-løkke bruker iteratoren implisitt
        for (Character c : tre) {
            System.out.print(c + " "); // D G K N Q S
        }


        while (!tre.tom()) {
            System.out.println(tre);
            tre.fjernHvis(x -> true);
        }


 /*
 [A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
 [A, B, C, E, F, H, I, J, L, M, O, P, R, T]
 [A, B, C, F, H, I, J, L, O, R, T]
 [A, B, C, H, I, J, O, R, T]
*/
    }

}
