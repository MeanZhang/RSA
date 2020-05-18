import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSA {
    /**
     * 生成密钥
     * 其实可以直接用BigInteger probablePrime(int bitLength, Random rnd)产生可能的素数
     * 或用boolean isProbablePrime(int certainty)进行素性检测
     * 但是为了用到第一次实验，就重新写了
     *
     * @param length 密钥长度
     * @return (e, p, q, d, n)
     */
    public static BigInteger[] generateKey(int length) {
        BigInteger[] key = new BigInteger[5];
        BigInteger p = getPrime(length / 2);
        BigInteger q = getPrime(length - length / 2);
        //n=p*q
        BigInteger n = p.multiply(q);
        //φ(n)=(p-1)(q-1)
        BigInteger faiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        Random random = new Random();
        BigInteger e = new BigInteger(faiN.bitLength(), random);
        //随机产生e，使得1<e<φ(n)且gcd(φ(n),e)=1
        while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(faiN) >= 0
                || ExtendedEuclideanAlgorithm.gcd(faiN, e).compareTo(BigInteger.ONE) != 0)
            e = new BigInteger(faiN.bitLength(), random);
        //d=1/e mod φ(n)
        BigInteger d = ExtendedEuclideanAlgorithm.ex_gcd(e, faiN)[1].mod(faiN);
        key[0] = e;
        key[1] = p;
        key[2] = q;
        key[3] = d;
        key[4] = n;
        return key;
    }

    /**
     * RSA加密，该方法仅计算，不生成密钥，不分组
     *
     * @param m 明文
     * @param e 公钥e
     * @param n n
     * @return 密文
     * @throws IllegalArgumentException m >= n时，抛出非法参数异常
     */
    public static BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n) {
        //如果m >= n，抛出非法参数异常
        if (m.compareTo(n) >= 0)
            throw new IllegalArgumentException();
        //c=m^e mod n，可以用m.powMod(e, n)
        return FastPowMod.fastPowMod(m, e, n);
    }

    /**
     * RSA解密
     * 使用了更快的MMRC算法，普通CRT方法在注释中
     *
     * @param c 密文
     * @param d 私钥d
     * @param p p
     * @param q q
     * @return 明文
     */
    public static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger p, BigInteger q) {
        //m1 ≡ c^d ≡ (c mod p)^(d mod (p-1))(mod p)
        BigInteger m1 = c.mod(p).modPow(d.mod(p.subtract(BigInteger.ONE)), p);
        //m2 ≡ c^d ≡ (c mod q)^(d mod (q-1))(mod q)
        BigInteger m2 = c.mod(q).modPow(d.mod(q.subtract(BigInteger.ONE)), q);
        BigInteger invP = ExtendedEuclideanAlgorithm.ex_gcd(p, q)[1].mod(q);
//        //CRT
//        BigInteger invQ = ExtendedEuclideanAlgorithm.ex_gcd(q, p)[1].mod(p);
//        return m1.multiply(q).multiply(invQ).add(m2.multiply(p).multiply(invP)).mod(p.multiply(q));

        //MMRC算法，速度更快
        //t = p^(-1) * (m2-m1) mod q
        BigInteger t = invP.multiply(m2.subtract(m1)).mod(q);
        return m1.add(t.multiply(p));
    }

    /**
     * 生成指定长度的可能的素数
     * boolean isProbablePrime(int certainty)也可以进行素性检测
     *
     * @param length 长度
     * @return 可能的素数
     */
    private static BigInteger getPrime(int length) {
        Random random = new Random();
        BigInteger result;
        boolean flag;
        //检测次数
        int time;
        while (true) {
            //产生一个指定长度的随机数
            result = new BigInteger(length, random);
            flag = true;
            //检测ln(n)≈ln(2^length)≈0.7*length次
            time = (int) (0.7 * length);
            for (int i = 0; i < time; i++)
                //如果检测不是素数
                if (!MillerRabin.millerRabin(result)) {
                    flag = false;
                    break;
                }
            //通过time次测试就认为是合数
            if (flag)
                return result;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. generate key\n2. generate key & encrypt\n3. decrypt");
        int op = scanner.nextInt();
        if (op == 1 || op == 2) {
            System.out.print("length of key: ");
            int length = scanner.nextInt();
            BigInteger[] key = generateKey(length);
            System.out.println("e: " + key[0].toString());
            System.out.println("p: " + key[1].toString());
            System.out.println("q: " + key[2].toString());
            System.out.println("d: " + key[3].toString());
            System.out.println("n: " + key[4].toString());
            if (op == 2) {
                System.out.print("plain: ");
                BigInteger m = new BigInteger(scanner.next());
                System.out.println("cipher: " + encrypt(m, key[0], key[4]).toString());
            }
        }
        if (op == 3) {
            System.out.print("cipher: ");
            BigInteger c = new BigInteger(scanner.next());
            System.out.print("d: ");
            BigInteger d = new BigInteger(scanner.next());
            System.out.print("p: ");
            BigInteger p = new BigInteger(scanner.next());
            System.out.print("q: ");
            BigInteger q = new BigInteger(scanner.next());
            System.out.println("plain: " + decrypt(c, d, p, q).toString());
        }
        scanner.close();
    }
}
