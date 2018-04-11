﻿package sasmac;

import java.math.BigDecimal;

public class CalcArea {

	static{
		System.loadLibrary("CalcArea");//系统环境变量中或者jre/bin中dll文件
	}
	
	public native static double CalcArea(String LinkString,String strStatment,String searchGeoWkt,String ClipgeoWkt);
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CalcArea calcArea=new CalcArea();
		String LinkString="MySQL:test-mycat,user=mycat,password=mycat,host=192.168.1.177,port=8066";
		//String strStatment="select FID,F_SCENEID,F_SCENEPATH,F_SCENEROW,F_ORBITID,F_SCENEDATE,F_OVERALLDATAQUALITY from tbs_zy3_bm";
		String strStatment="select * from tbs_zy3_bm";
		String searchGeoWkt="POLYGON ((114.61099000001786 30.938560000239647,114.60793999971747 30.941448000266917,114.60701999957678 30.94440099969313,114.60629999966216 30.948735999872326,114.60419000042441 30.95440300029827,114.60001999975725 30.95862999999565,114.59584000042787 30.962855999646933,114.58979000033332 30.967255000078353,114.58356000003539 30.969582000325772,114.57923000008668 30.97082099988279,114.57623000029253 30.9725610000512,114.57240000000968 30.974735000440432,114.56887000006611 30.976229000060926,114.56608999962134 30.97866399989084,114.5616999996048 30.981737000352098,114.55758000034314 30.984590999711372,114.55368000043086 30.98836500011072,114.54868999973598 30.99279200003417,114.54421999962904 30.997468999790613,114.54111000016019 31.001499000299532,114.53641999980448 31.005247000399663,114.53097999994998 31.007825999672946,114.52051999981632 31.015061999579302,114.51669000043285 31.01723700001463,114.51286000015011 31.019407000219474,114.50769999971271 31.02153400024082,114.50361999959694 31.023469999551594,114.49979000021347 31.025639999756436,114.49665999982278 31.03012799979382,114.49565999959157 31.034681000129524,114.49577000016563 31.038355000415834,114.49640000042814 31.04227300025849,114.498560000172 31.048072999620672,114.50028999987944 31.051109000175018,114.50252999971372 31.055079999762825,114.50500000025772 31.059515000055057,114.50951000040993 31.066540000127645,114.51128000016251 31.06888999963661,114.51432000000193 31.07265300042826,114.5175899996517 31.07757399974105,114.5211700001015 31.081126999845594,114.52392999962433 31.085341999889124,114.52539000037552 31.08859999988499,114.52603999976134 31.09228699987125,114.52527000023986 31.09776699977101,114.52480000015817 31.10256799984751,114.5241200002888 31.105757000259985,114.52371000027495 31.10918399995228,114.52463999997735 31.11265199973593,114.52474000009033 31.116324999976143,114.52312000005759 31.122923000150536,114.52143999995701 31.13112599966621,114.51823000037518 31.137444999570164,114.51431000044033 31.141676000351254,114.51072000042882 31.144313999646215,114.50689999970768 31.14625700017899,114.501980000441 31.148623000425573,114.49714000036533 31.149384999578274,114.49384999979372 31.151341999857095,114.48890000004349 31.154627000198275,114.47843999990982 31.161629000109883,114.47406000035426 31.164011000194648,114.46908000012036 31.167977999598122,114.46520999979236 31.17083499999501,114.45999000018651 31.174108999829116,114.45558999970899 31.176945000157332,114.45383000041738 31.180329999712228,114.45509000004313 31.182202999716253,114.45662999998535 31.18362999984555,114.45818000038867 31.185054999882652,114.45941999999184 31.187158999743076,114.4595499996891 31.190373999555447,114.45905999958484 31.19540200020333,114.45704999956081 31.198776000150474,114.45347000001038 31.201182999589037,114.44961999970496 31.203584999696375,114.4452400001494 31.20573599992474,114.43927000014537 31.207835999600775,114.43432999995673 31.21088599990111,114.42796000039982 31.215724999930615,114.42247000003908 31.218985000018677,114.41721000038797 31.2231709996247,114.41272999982004 31.227612000193517,114.40710000020033 31.233849999960967,114.40144999965867 31.24077599996656,114.40047000034951 31.244868999782284,114.40056999956323 31.248771999832798,114.40115000021888 31.253836000341494,114.40281999985848 31.258247000426763,114.40449999995906 31.262888000322334,114.40564000034851 31.267279000384974,114.4070499996941 31.2716850002397,114.40849000042272 31.275397999625795,114.4104400003788 31.279818000125942,114.41319000034002 31.284264000025928,114.41779000014435 31.289229999614406,114.42214999967723 31.29349700025631,114.42591999989224 31.299121999645536,114.42862000024661 31.304714000211504,114.43188000033467 31.309633000331473,114.43496000021923 31.312716000354328,114.43752999997696 31.315090000070313,114.43982999987907 31.317686000127253,114.44048999972586 31.320916999777808,114.44140000030495 31.324615000271137,114.44179000029612 31.32806800026259,114.4413699998214 31.33149499995494,114.44040999963545 31.334901999624606,114.43947999993304 31.337851999811903,114.43802999964282 31.340327999733177,114.43603000007988 31.343471999869962,114.43324000007351 31.346134000271263,114.43014999972786 31.349244999786208,114.42686000005551 31.35097300030077,114.42349999985447 31.35430300001832,114.4222699998129 31.35793300007498,114.42257999971378 31.362986000076603,114.42382000021621 31.365319999747385,114.42585000026281 31.367907000288767,114.42735000015989 31.370480000184727,114.42776999973535 31.37301400008164,114.42897000019252 31.37626299966263,114.42989000033333 31.379502999728118,114.43133999972417 31.38321700005963,114.43441999960874 31.386299000036388,114.43732000018917 31.387307999783104,114.43972000020437 31.387615000444953,114.44263999990812 31.388167999856023,114.44473000002245 31.389381000013202,114.44651999979772 31.391501999757907,114.44799000011062 31.39453099998957,114.44897000031915 31.3968540000526,114.44942000037815 31.398702999849604,114.44983999995361 31.401467999602914,114.44912000003899 31.405570999879558,114.44868000044096 31.409455999999693,114.4477299998166 31.412862999669414,114.44840999968608 31.415635999791505,114.45209000024897 31.417360000121676,114.45504000043627 31.41722700028606,114.45648000026551 31.41498000012905,114.45872999966139 31.412530999653825,114.46069999964016 31.41030100028047,114.46376000040152 31.40787700005825,114.46834999974476 31.406875999780993,114.4723600002311 31.407234000095798,114.47470000017836 31.408913000150278,114.47620000007544 31.41125500018984,114.47712999977784 31.414494000209174,114.47691000042846 31.419762000229014,114.4764299998858 31.42478899993148,114.47572000043215 31.428663000443862,114.4753100004184 31.4318600003258,114.47544000011567 31.435076000184267,114.47533000044098 31.437821999961102,114.47620000007544 31.442436000410737,114.47741999965604 31.444995999707487,114.47835999981942 31.448008000054756,114.48034000025905 31.45196699998877,114.48128999998403 31.4549770002439,114.4827099997907 31.459150000150032,114.4836200003698 31.462848999790083,114.48430000023916 31.465850999676434,114.48633999984736 31.4682080004074,114.48877999990782 31.46759800016747,114.4907200003023 31.466055000086897,114.49320999996951 31.464298000034205,114.49597000039171 31.462552999635307,114.49975999972992 31.46175599976857,114.50322999960576 31.462095000106842,114.50664999987475 31.46357900016568,114.5081600002328 31.46569299958776,114.509350000229 31.469397000357617,114.50921999963236 31.47237399999085,114.50824999988481 31.47624000013451,114.50695999977552 31.481242999629956,114.5060599996574 31.48350699967125,114.50484999963851 31.486679000199445,114.50363999961962 31.48984899973607,114.50158999955045 31.49391199996751,114.49954000038053 31.497744000342493,114.49831000033907 31.501373000353112,114.49788999986424 31.50502799976357,114.49798999997734 31.508927999675848,114.49731999966957 31.51188900037016,114.49768000007646 31.51602600041548,114.49839000042948 31.518343000201924,114.4995999995491 31.521357999788222,114.50192000037316 31.52349700036268,114.50502999984201 31.52611499963507,114.50730000015983 31.529397999884054,114.50880999961862 31.53173799983142,114.51031999997667 31.534078999824885,114.5113000001852 31.53617500021585,114.51304000035361 31.53943999963508,114.51366000015514 31.54358500004912,114.5140799997306 31.546348999756333,114.5144499996992 31.550259000129586,114.51427000039507 31.55460700000873,114.51653999981363 31.55788800016552,114.52029000000596 31.55800800030113,114.52406000022097 31.557897999727118,114.52866000002518 31.55689400021089,114.53380000044001 31.55590999981797,114.53893000039375 31.555157000180827,114.54296999956489 31.554825000165238,114.54776000003369 31.555890999841495,114.55161000033911 31.560138999607545,114.55730000002654 31.558713999570443,114.5646499997921 31.5561920002267,114.56780999976706 31.557664999778524,114.5710099997873 31.557996999794113,114.57545000031007 31.56111300043881,114.57990000039445 31.563771999802555,114.58220999985815 31.566139000095177,114.5864100001096 31.568331000414787,114.59211999981972 31.572864999874696,114.5985999999507 31.578339000397193,114.6040300002436 31.583549000441394,114.60684999983425 31.586616999772843,114.6104999999136 31.589250999782735,114.61339999959466 31.590487000100836,114.61737000003575 31.59175500009536,114.62034999980722 31.591159999647573,114.62276000028339 31.59123400036077,114.62986999977761 31.588243000082173,114.6337400001056 31.58538200040016,114.63956999995139 31.58074799992795,114.64344999984098 31.577885000153742,114.64625999987004 31.574988999757693,114.64873999997565 31.5732329997511,114.65075999956127 31.569858999803955,114.65249999972968 31.566701999967222,114.65399000006505 31.563309000043546,114.65491000020575 31.56035399962576,114.65610000020206 31.55787100028118,114.65751000044713 31.556079999560495,114.65953000003265 31.55270200032828,114.66316000008942 31.54914700013154,114.66560000014977 31.548761000324703,114.66882000019268 31.548629999681964,114.67385999959504 31.55015900001655,114.67676999973708 31.550935999860712,114.68133999995712 31.5508440001164,114.68597000024499 31.549379000034094,114.68866000013827 31.54900399983501,114.6925200000054 31.546598000442543,114.69184999969764 31.543368999984807,114.69198999985588 31.539707000251667,114.69050999998137 31.536680000112256,114.68954000023382 31.53413000037716,114.6906799997239 31.53255799985908,114.69366000039474 31.53173399964703,114.69830000024433 31.530040999846506,114.7018300001879 31.529000999570144,114.70614999967563 31.52844199988249,114.71266999985187 31.52634799958372,114.7162300002791 31.52439100020422,114.71896999977923 31.52310000004877,114.72273000043253 31.5229820000053,114.72803999969051 31.52428799995289,114.7357899999082 31.525208000093585,114.74136000035935 31.526751000174215,114.744540000357 31.527760999966972,114.74717999974416 31.528528000249537,114.75066999964258 31.528630999601546,114.75554999976339 31.527173999887964,114.75880000028985 31.526582999624566,114.7650499997111 31.52470600033547,114.76828000021499 31.524342999790235,114.77023999973267 31.522336999950653,114.77247000000534 31.520341999718823,114.7744699995684 31.51719300025087,114.77382999974418 31.513045999744577,114.77302000017744 31.506603000419943,114.77370000004692 31.50318500024315,114.77653999966014 31.49914200003434,114.77720999996791 31.495725999949798,114.7775999999592 31.49275599973987,114.77852000009989 31.49003200007786,114.77967999961254 31.487774000313152,114.78217999974083 31.485557999786522,114.78469000032999 31.48310900021056,114.78851000015186 31.481616999782943,114.79204999965714 31.480117999931963,114.7950399998897 31.47928800034265,114.7982599999325 31.47915300041484,114.80388999955221 31.47909200030091,114.81036000012148 31.4781360002994,114.8131100000827 31.476610000103108,114.81696000038812 31.47397400000102,114.82195999974522 31.46976500023402,114.82478000023525 31.46618099959983,114.82753999975807 31.46442599963933,114.82328999989977 31.458791999835228,114.81955999973002 31.455102999756832,114.81746999961558 31.450277000326537,114.81563000023357 31.445452000043076,114.81405999980768 31.441544999808173,114.8127400001141 31.4383299999958,114.81169000027603 31.435344999993788,114.8114500000047 31.432135000411847,114.81173999988289 31.429156999833197,114.8131100000827 31.425728000048707,114.81528999984914 31.4216180003487,114.81639000019334 31.41772799999808,114.81723000024363 31.413838999693553,114.81807000029391 31.409033000285945,114.81811000033917 31.404449000319858,114.81707999962441 31.400086999795406,114.81549999963693 31.39664100012658,114.8133799999382 31.393418999991525,114.81206999980634 31.390890000325157,114.81128999982388 31.3867609997493,114.81103999999084 31.384923999606144,114.80946000000336 31.381703999563285,114.80788000001587 31.379403999661122,114.80574999985629 31.37755599991027,114.80229000044142 31.375244000354257,114.79963000013231 31.373623000275472,114.79671000042856 31.370165000053476,114.79620999986332 31.36649700004375,114.79489000016974 31.36396600028519,114.79144000031647 31.360965999591713,114.78878999956908 31.358195999607915,114.7874699998755 31.356584999990048,114.78480999956639 31.35450400029049,114.780820000002 31.351728000030107,114.77708999983224 31.349640000007923,114.77390000027299 31.347788000072626,114.77123999996388 31.34547599961735,114.76887999999394 31.340649000141013,114.76865000018358 31.33606300008273,114.76922000037825 31.331715000203644,114.77035000030673 31.325533000319638,114.77144999975167 31.321874999871568,114.77203000040731 31.316836999662087,114.77259000014101 31.313401999601012,114.77343999975301 31.308596000193347,114.77374000009206 31.304702999704432,114.7748299999754 31.30127099978165,114.7762099997368 31.297152999712864,114.77942999977961 31.294654999676823,114.78402000002222 31.29078900043254,114.78804999963188 31.288295999727666,114.79477999959568 31.282841000080964,114.7988100001046 31.28011500032676,114.80096999984846 31.27806700034978,114.80234000004828 31.27417900009135,114.80236000007085 31.271657999894444,114.80158000008839 31.26890399974883,114.799739999807 31.264537999939307,114.79501999986701 31.254652000184763,114.78785999986667 31.247270999889622,114.78389999988656 31.24243399995231,114.78098000018292 31.23897599973037,114.77833999989639 31.236666000266553,114.77435000033199 31.233888999960072,114.77169000002289 31.23226699983519,114.76822999970875 31.23018299999734,114.76637000030405 31.22902299958531,114.76317999984542 31.22762699998566,114.76026999970338 31.2246280002376,114.76083999989805 31.22027800026632,114.76167999994834 31.216156000013143,114.76145000013798 31.21179999976522,114.75989000017307 31.20720700028363,114.758029999869 31.2051299998692,114.75458999957743 31.201440999790748,114.75142999960246 31.197525000040287,114.74904999960984 31.194527000338326,114.74614000036718 31.19084000035207,114.74376999993615 31.18784499988908,114.74138999994364 31.185078000043575,114.73902000041198 31.180479000285402,114.73718999969219 31.17656899991215,114.73589000002119 31.172892000386867,114.73457999988932 31.169903000200463,114.73410000024603 31.166882000337637,114.73000000010757 31.160633000063115,114.73002000013025 31.155568999554475,114.7341299998302 31.146447999990244,114.73178000032124 31.135285999872394,114.72955000004856 31.12694000006013,114.7293200002382 31.123272000050406,114.732090000222 31.11505299979723,114.75072000014882 31.0968950000443,114.75154999973813 31.09682500041481,114.75049999990006 31.091400000352394,114.74678999975299 31.089080999574435,114.74173000032795 31.087213999846995,114.7380099997199 31.08535599963517,114.7334900000061 31.084177000145928,114.72948999998073 31.083004000033952,114.72577000027195 31.08182899982978,114.72310999996284 31.08043700041452,114.71912999996016 31.078803999782565,114.71648000011203 31.077180999611528,114.71274999994228 31.07577899973529,114.70901000021092 31.075752000289356,114.7060800000462 31.07664700017699,114.70019999969418 31.07798199966271,114.69645999996283 31.07841299974524,114.69243999991488 31.079757999691935,114.68683999987945 31.079717999646675,114.6831099997097 31.0783139996783,114.6791499997297 31.07393099998444,114.67602999979988 31.065885999658065,114.67688000031114 31.060618999684323,114.67877000019939 31.057884000414617,114.68041999981642 31.053082000292022,114.68259000002126 31.049431000166635,114.68716000024119 31.04648400011763,114.69169999997769 31.045143000355324,114.69545000017001 31.04310800007829,114.6981400000634 31.04060599985786,114.69764999995914 31.036704999899484,114.69582000013861 31.03302400018987,114.69290999999657 31.029794999732133,114.68893999955549 31.026557999804936,114.6855000001633 31.024238999926297,114.68232999972724 31.020778999612105,114.6794199995852 31.018234000107498,114.67731000034757 31.015699000164545,114.67709000009881 31.010883000295962,114.67875000017682 31.00493599955348,114.68066000008764 31.000364000140564,114.68388999969216 30.996952000240412,114.68712000019605 30.99422500044011,114.69034000023885 30.991727999550847,114.69327999996517 30.990374000088593,114.69863999972938 30.987661000034336,114.7002799997847 30.98400499967846,114.70084000041777 30.98034299994532,114.70191999984002 30.97804600018145,114.69912999983364 30.974523999707287,114.69736000008095 30.97194699962688,114.69617000008463 30.968700000138085,114.6954700001927 30.9661559997802,114.69506999974044 30.962933999645145,114.69496000006575 30.95903199964073,114.69504999971787 30.956741000153443,114.69707000020276 30.953135000303746,114.69826999976067 30.949960999683356,114.70078999991154 30.94682499991535,114.70224999976335 30.94389199961239,114.69801999992774 30.94284399986651,114.69559000032825 30.943457000244734,114.69368999997903 30.94454800017411,114.68983999967361 30.947180000091805,114.68794000022365 30.948040000164724,114.68361999983665 30.949513999762587,114.67986000008261 30.949858000331346,114.6753499999304 30.949260999791363,114.67142000043395 30.94776299998648,114.66753000008327 30.945123999746045,114.66201000013837 30.94311899995256,114.65752000000873 30.941835000119852,114.65278999960788 30.94031300010795,114.64777000022809 30.939241000155107,114.64246999963245 30.938388999551705,114.63530999963211 30.937480999964123,114.63027000022976 30.936635999683347,114.625240000389 30.935793000394142,114.61886000037111 30.935367999688822,114.61099000001786 30.938560000239647))";
		String ClipgeoWkt=searchGeoWkt;
		double dblarea=calcArea.CalcArea(LinkString, strStatment, searchGeoWkt, ClipgeoWkt);
		System.out.println(dblarea);
		BigDecimal bDecimal=new BigDecimal(dblarea);
		System.out.println(bDecimal.toString());

	}

}