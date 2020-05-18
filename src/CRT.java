import java.math.BigInteger;
import java.util.Scanner;

/**
 * Chinese remainder theorem(CRT) 中国剩余定理
 */
public class CRT {
    /**
     * Chinese remainder theorem(CRT) 中国剩余定理
     *
     * @param a remainders 余数数组
     * @param m Mods 模数数组
     * @return x mod M
     */
    public static BigInteger[] crt(BigInteger[] a, BigInteger[] m) {
        // x, M
        BigInteger[] xm = new BigInteger[2];
        xm[0] = BigInteger.ZERO;
        xm[1] = BigInteger.ONE;
        BigInteger tmp;
        int k = a.length;
        for (int i = 0; i < k; i++)
            xm[1] = xm[1].multiply(m[i]);
        for (int i = 0; i < k; i++) {
            // M / mi
            tmp = xm[1].divide(m[i]);
            xm[0] = xm[0].add(tmp.multiply(ex_gcd(tmp, m[i])[1]).multiply(a[i]));
        }
        xm[0] = xm[0].mod(xm[1]);
        return xm;
    }

    /**
     * Extended Euclidean Algorithm 扩展欧几里得算法
     *
     * @param a first number 第一个数
     * @param b second number 第二个数
     * @return GCD, x, y(GCD = ax + by)
     */
    public static BigInteger[] ex_gcd(BigInteger a, BigInteger b) {
        BigInteger[] result = new BigInteger[3];
        if (b.compareTo(BigInteger.ZERO) == 0) {
            result[0] = a;
            result[1] = BigInteger.ONE;
            result[2] = BigInteger.ZERO;
        } else {
            BigInteger[] tmp = ex_gcd(b, a.remainder(b));
            result[0] = tmp[0];
            result[1] = tmp[2];
            result[2] = tmp[1].subtract(a.divide(b).multiply(tmp[2]));
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("k: ");
        int k = scan.nextInt();
        System.out.println("a[]:");
        BigInteger[] a = new BigInteger[k];
        for (int i = 0; i < k; i++)
            a[i] = new BigInteger(scan.next());
        System.out.println("b[]:");
        BigInteger[] b = new BigInteger[k];
        for (int i = 0; i < k; i++)
            b[i] = new BigInteger(scan.next());
        scan.close();
        BigInteger[] ans = crt(b, a);
        System.out.println("--------anwser--------");
        System.out.println("x = " + ans[0]);
        System.out.println("M = " + ans[1]);
    }
}
