import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

/**
 * Miller-Rabin prime test 素性检测
 */
public class MillerRabin {
    /**
     * Miller-Rabin prime test 素性检测
     *
     * @param n number to be tested 待测数
     * @return false: composite, true: probable prime
     */
    public static boolean millerRabin(BigInteger n) {
        // 2
        if (n.compareTo(BigInteger.TWO) == 0)
            return true;
            // 0 or 1 or even number
        else if (n.and(BigInteger.ONE).compareTo(BigInteger.ONE) != 0 || n.compareTo(BigInteger.ONE) == 0)
            return false;
        else {
            int k = 1;
            BigInteger q = n.subtract(BigInteger.ONE).shiftRight(1);
            // n - 1 = 2^k * q
            while (q.and(BigInteger.ONE).compareTo(BigInteger.ONE) != 0) {
                q = q.shiftRight(1);
                k++;
            }
            Random random = new Random();
            BigInteger a;
            a = new BigInteger(n.bitLength(), random);
            // 1 < a < n - 1
            while (a.compareTo(BigInteger.ONE) <= 0 || a.compareTo(n.subtract(BigInteger.ONE)) >= 0)
                a = new BigInteger(n.bitLength(), random);
            // a^q mod n = 1
            if (fastPowMod(a, q, n).compareTo(BigInteger.ONE) == 0)
                return true;
            for (int j = 0; j < k; j++)
                // a^(2^j * q) mod n = n-1
                if (fastPowMod(a, BigInteger.TWO.pow(j).multiply(q), n).compareTo(n.subtract(BigInteger.ONE)) == 0)
                    return true;
            return false;
        }
    }

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
            //该位为1
            if (n.mod(BigInteger.TWO).compareTo(BigInteger.ONE) == 0) {
                //结果*当前位的幂
                d = d.multiply(x).mod(m);
                //去掉当前位
                n = n.subtract(BigInteger.ONE).divide(BigInteger.TWO);
            } else//该位为0，*2^0省略
                ////去掉当前位
                n = n.divide(BigInteger.TWO);
            //下一位的幂
            x = x.multiply(x).mod(m);
        }
        return d;
    }

    public static void main(String[] args) {
        boolean flag = true;
        Scanner scan = new Scanner(System.in);
        System.out.print("test times: ");
        int t = scan.nextInt();
        System.out.print("test number: ");
        BigInteger n = new BigInteger(scan.next());
        scan.close();
        for (int i = 0; i < t; i++)
            if (!millerRabin(n)) {
                flag = false;
                break;
            }
        System.out.println("is prime?\n" + flag);
    }
}
