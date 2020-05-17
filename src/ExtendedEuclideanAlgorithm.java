import java.math.BigInteger;
import java.util.Scanner;

public class ExtendedEuclideanAlgorithm {
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

    /**
     * Euclidean Algorithm 欧几里得算法
     *
     * @param a
     * @param b
     * @return GCD
     */
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.compareTo(BigInteger.ZERO) == 0)
            return a;
        return gcd(b, a.remainder(b));
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("a: ");
        BigInteger a = new BigInteger(scan.next());
        System.out.print("b: ");
        BigInteger b = new BigInteger(scan.next());
        scan.close();
        BigInteger[] result = ex_gcd(a, b);
        if (result[0].compareTo(BigInteger.ZERO) < 0) {
            result[0] = result[0].negate();
            result[1] = result[1].negate();
            result[2] = result[2].negate();
        }
        System.out.println("GCD: " + result[0]);
        System.out.println("x = " + result[1]);
        System.out.println("y = " + result[2]);
    }
}
