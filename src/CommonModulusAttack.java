import java.math.BigInteger;
import java.util.Scanner;

public class CommonModulusAttack {

    /**
     * 多组数据共模攻击（可能有不互素的e）
     *
     * @param e 多个不同e
     * @param c 多个不同的密文
     * @param n 模数n
     * @return 明文
     */
    public static BigInteger attack(BigInteger[] e, BigInteger[] c, BigInteger n) {
        BigInteger[] s;
        int num = e.length;
        for (int i = 0; i < num - 1; i++)
            for (int j = i + 1; j < num; j++) {
                //找到s1和s2，s1*ei+s2*ej=1
                s = ExtendedEuclideanAlgorithm.ex_gcd(e[i], e[j]);
                //ei和ej要互素
                if (s[0].compareTo(BigInteger.ONE) == 0)
                    //ci^s1 * cj^s2≡m^(ei*s1) * m^(ej*s2)≡m (mod n)
                    return c[i].modPow(s[1], n).multiply(c[j].modPow(s[2], n)).mod(n);
            }
        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
//        BigInteger[] e = new BigInteger[2];
//        BigInteger[] c = new BigInteger[2];
//        BigInteger n = new BigInteger("14953675343867353426809060370006891177760987573732937371488768560187145296888650041003182946001095628989687061173031985216583916059762574269128737466587282372315819810249516946573044111283328982026499587609586839781730225917510353627078856397168637287759073411792948019154291414251989402563051827143513951034201270457074213649870630723249745292981533643403007903320536747157832568038460060349422511613978921687724357522883056079709905174793284735623555232990194070495865172272599488362296023436021218619172958312433629927109475109223294364852366506466389535524669550119036246185378321426839076994963607208081944039707");
//        c[0] = new BigInteger("14547056508539528406454817034594406211736709656537757670384239761555684470730284241676185110481675495393250182201033222324610191649967551648750411152736019966635762838073504101343153448596635084447519880793383076521236614930372193891128668248686108812231937872131311411022223333151760828704935123411889397217677936751551123368844319093735025357897704806801632395108857300097793988801676848882709308391869144230162872371158503851527419057639602188625588881822707899682572595123414348551799208580625379149093820180531675725298720444314718204842568472240076802053620149504633626285512551928021561892956355073617884414120");
//        c[1] = new BigInteger("12082714382090047013220996347775098234003897633206060846281363382798379528232103254675792945880744252919393485668426459976889567512505795428258177320476051966849759841660065075795191601150092967314029119701971506098011267022049564486240854157779789941397949073609134393991522283632452256838804191910607428491910852582525515065992353809725179390257692365772484879156775648406062231537497682142432706941483995776518805222605762125764405228901728773800248791028823299149502932916311256476520432279406058455810795032606418336326624976970615646409704708982338904996685772958779479436707249579048224950288134044082007494399");
//        e[0] = new BigInteger("65537");
//        e[1] = new BigInteger("92743");
        Scanner scanner = new Scanner(System.in);
        System.out.print("groups: ");
        int group = scanner.nextInt();
        BigInteger[] e = new BigInteger[group];
        BigInteger[] c = new BigInteger[group];
        System.out.println("n:");
        BigInteger n = new BigInteger(scanner.next());
        for (int i = 0; i < group; i++) {
            System.out.println("e[" + i + "]:");
            e[i] = new BigInteger(scanner.next());
            System.out.println("c[" + i + "]:");
            c[i] = new BigInteger(scanner.next());
        }
        scanner.close();
        try {
            System.out.println("m:\n" + attack(e, c, n).toString());
        } catch (IllegalArgumentException e1) {
            System.out.println("e's are not coprime");
        }
    }
}