import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class KnownEDN {
    /**
     * 已知e，d，n，分解n，参考https://www.di-mgt.com.au/rsa_factorize_n.html
     * <p>
     * 算法简介：
     * 初始化k=de-1
     * 选择随机数g，1<g<n
     * 计算t，k=2^t * r，r为奇数
     * 依次计算x = g^(k/2), g^(k/4),……,g^(k/2^t) mod n
     * 直到x>1且y=gcd(x−1,n)>1
     * 此时p=y，q=n/p
     * 如果找不到这样的y，就重新选择随机数g
     *
     * @param e 公钥e
     * @param d 私钥d
     * @param n 模数n
     * @return p，q
     */
    public static BigInteger[] attack(BigInteger e, BigInteger d, BigInteger n) {
        // p,q
        BigInteger[] result = new BigInteger[2];
        // k=de-1
        BigInteger k = d.multiply(e).subtract(BigInteger.ONE);
        Random random = new Random();
        while (true) {
            BigInteger g = new BigInteger(n.bitLength(), random);
            // 选择随机数g，1<g<n
            while (g.compareTo(BigInteger.ONE) <= 0 || g.compareTo(n) >= 0) {
                g = new BigInteger(n.bitLength(), random);
            }
            BigInteger k1 = k;
            // 计算t和g^(k/2^i)的过程合在一起
            while (k1.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                // 如果k为偶数，就除以2
                k1 = k1.shiftRight(1);
                // 此时g^(k/2^i)=g^k1
                BigInteger x = g.modPow(k1, n);
                // 计算y=gcd(x−1,n)，直接赋值给p(result[0])
                result[0] = x.subtract(BigInteger.ONE).gcd(n);
                // 如果x>1且y=gcd(x−1,n)>1
                if (x.compareTo(BigInteger.ONE) > 0 && result[0].compareTo(BigInteger.ONE) > 0) {
                    result[1] = n.divide(result[0]);
                    return result;
                }
            }
        }
    }

    public static void main(String[] args) {
        // BigInteger e = new BigInteger("65537");
        // BigInteger d = new
        // BigInteger("13085102850405329895940153649208766646679432053055210927814587187939575969334380946175257120108607885616731724467899991987963542311668962802613624160423864736904359544115910805381019345850330276964971412664144174157825068713331109139842487999112829255389047329358923488846912437792391102853729375052922599258215311601018992134762683570752403675370812499995354701024990414541327012769030147878934713424171374951602988478984432403148854042370903764361797455965930292322795814453835335323397068237664481359506461188857661605832041501219728374514303209642746672993156029099655958158717907546664548938973389857200804582177");
        // BigInteger n = new
        // BigInteger("21378032245774894186324720788457768851857953294637267751313371903474996018902810092972224806315945430514626988743400353365786674788848003569698586194388463460699531620948408197942261177369324808332585418351947368544183614904162658914539989430070735676083960582943505227316151479174351490943931346982185481068889458087344890907035731467000101100009111593455801160870652558847164438348031498067369123608755518383163346962891967964682970251625764813457371461595048927486942272152822570449609158417324070867001419543608370026546341531367233199832189762919523227554947408242727690461831545997600374969434706782376320559561");
        // BigInteger[] pq = attack(e, d, n);
        Scanner scanner = new Scanner(System.in);
        System.out.println("e:");
        BigInteger e = new BigInteger(scanner.next());
        System.out.println("d:");
        BigInteger d = new BigInteger(scanner.next());
        System.out.println("n:");
        BigInteger n = new BigInteger(scanner.next());
        BigInteger[] pq = attack(e, d, n);
        scanner.close();
        System.out.println("p:\n" + pq[0] + "\nq:\n" + pq[1]);
    }
}
