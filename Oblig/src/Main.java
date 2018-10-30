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


    }

}
