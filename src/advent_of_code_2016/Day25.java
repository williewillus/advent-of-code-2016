package advent_of_code_2016;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day25 {
    private static int a = 0;
    private static int b = 0;
    private static int c = 0;
    private static int d = 0;

    private static void sub() {
        do {
            c = 2;
            do {
                if (b == 0) // jnz b 2
                    return; // jnz 1 6
                b--;
                c--;
            } while (c != 0); // jnz c -4
            a++;
        } while (true); // jnz 1 -7
    }

    private static List<Integer> buildList(int initA) {
        System.out.println("Building " + initA);
        List<Integer> ret = new ArrayList<>();
        a = initA;
        d = a;
        d += (182 * 14);
        b = 0;
        c = 0;

        do {
            a = d;
            do {
                b = a;
                a = 0;

                sub();

                b = 2;
                do {
                    if (c == 0) // jnz c 2
                        break; // jnz 1 4
                    b--;
                    c--;
                } while (true);
                ret.add(b);
                if (ret.size() >= 100)
                    return ret;
            } while (a != 0); // jnz a -19
        } while (true); // jnz 1 -21

    }

    private static boolean valid(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            int elem = list.get(i);
            if (i % 2 == 0 && elem != 0) {
                return false;
            } else if (i % 2 != 0 && elem != 1){
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println(IntStream.range(0, Integer.MAX_VALUE).mapToObj(Day25::buildList).filter(Day25::valid).findFirst().get());
    }

}
