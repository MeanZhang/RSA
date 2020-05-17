import java.math.BigInteger;
import java.util.Scanner;

/**
 * 快速幂模算法
 */
public class FastPowMod {
    /**
     * 快速幂模算法
     *
     * @param x base 底数
     * @param n index 指数
     * @param m mod 模数
     * @return remainder 余数
     */
    public static BigInteger fastPowMod(BigInteger x, BigInteger n, BigInteger m) {
        BigInteger d = new BigInteger("1");
        while (n.compareTo(BigInteger.ZERO) > 0) {
            // 该位为1
            if (n.mod(BigInteger.TWO).compareTo(BigInteger.ONE) == 0)
                // 结果*当前位的幂
                d = d.multiply(x).mod(m);
            // 去掉当前位
            n = n.shiftRight(1);
            // 下一位的幂
            x = x.multiply(x).mod(m);
        }
        return d;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("x: ");
        BigInteger x = new BigInteger(scan.next());
        System.out.print("n: ");
        BigInteger n = new BigInteger(scan.next());
        System.out.print("m: ");
        BigInteger m = new BigInteger(scan.next());
        scan.close();
        System.out.println("--------answer--------");
        long startTime = System.currentTimeMillis();
        System.out.println(fastPowMod(x, n, m));
        long endTime = System.currentTimeMillis();
        System.out.println("time: " + (endTime - startTime) + "ms");
    }
}
