import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSA {
    /**
     * 生成密钥
     *
     * @param length 密钥长度
     * @return (e, p, q, d, n)
     */
    public static BigInteger[] generateKey(int length) {
        BigInteger[] key = new BigInteger[5];
        Random randomP = new Random();
        BigInteger p = BigInteger.probablePrime(length / 2, randomP);
        Random randomQ = new Random();
        BigInteger q = BigInteger.probablePrime(length - length / 2, randomQ);
        // n=p*q
        BigInteger n = p.multiply(q);
        // φ(n)=(p-1)(q-1)
        BigInteger faiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        Random randomE = new Random();
        BigInteger e = new BigInteger(faiN.bitLength(), randomE);
        // 随机产生e，使得1<e<φ(n)且gcd(φ(n),e)=1
        while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(faiN) >= 0 || !faiN.gcd(e).equals(BigInteger.ONE))
            e = new BigInteger(faiN.bitLength(), randomE);
        // d=1/e mod φ(n)
        BigInteger d = e.modInverse(faiN);
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
        // 如果m >= n，抛出非法参数异常
        if (m.compareTo(n) >= 0)
            throw new IllegalArgumentException();
        // c=m^e mod n
        return m.modPow(e, n);
    }

    /**
     * RSA解密
     *
     * @param c 密文
     * @param d 私钥d
     * @param p p
     * @param q q
     * @return 明文
     */
    public static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger p, BigInteger q) {
        // m1 ≡ c^d ≡ (c mod p)^(d mod (p-1))(mod p)
        BigInteger m1 = c.mod(p).modPow(d.mod(p.subtract(BigInteger.ONE)), p);
        // m2 ≡ c^d ≡ (c mod q)^(d mod (q-1))(mod q)
        BigInteger m2 = c.mod(q).modPow(d.mod(q.subtract(BigInteger.ONE)), q);
        BigInteger invP = p.modInverse(q);
        // t = p^(-1) * (m2-m1) mod q
        BigInteger t = invP.multiply(m2.subtract(m1)).mod(q);
        return m1.add(t.multiply(p));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. generate key\n2. generate key & encrypt\n3. encrypt\n4. decrypt");
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
        } else if (op == 3) {
            System.out.print("e: ");
            BigInteger e = new BigInteger(scanner.next());
            System.out.print("n: ");
            BigInteger n = new BigInteger(scanner.next());
            System.out.print("plain: ");
            BigInteger m = new BigInteger(scanner.next());
            System.out.println("cipher: " + encrypt(m, e, n).toString());
        } else {
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
