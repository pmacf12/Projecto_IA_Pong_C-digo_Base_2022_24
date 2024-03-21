package breakout;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import utils.Commons;
import utils.GameController;

public class Breakout extends JFrame {

	private static final long serialVersionUID = 1L;

	public Breakout(GameController network, int i) {
		add(new BreakoutBoard(network, true, i));
		setTitle("Breakout");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		pack();
		setVisible(true);
	}

	public static void test(double[] values) {
		ArrayList<Double> valuesarray = new ArrayList<>();
		for(int i = 0; i < values.length; i++) {
			valuesarray.add(values[i]);
		}
		Breakout breakout = new Breakout(new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS, valuesarray)), 1);
	}

	public static void main(String[] args) throws IOException {
		//double[] values = {0.3847589925292282, 0.7319430682167991, 0.5670870741906301, 0.2807869898083827, 0.7926237621313436, 0.4035374271674208, 0.5563901530096655, 0.6044965424894632, 0.6250078151325305, 0.7012363339705211, 0.5986878453476437, 0.19261577157348708, 0.7349259014421464, 0.2870048669138937, 0.657091368642962, 0.8828343185052704, 0.5953491107906045, 0.3660156813883476, 0.6917210739065589, 0.12181792840554861, 0.8286570880475791, 0.3556923153247744, 0.8250243610986904, 0.274086889742276, 0.37416721726808055, 0.7891877788772645, 0.127748822642753, 0.7819313886628859, 0.9667361388794621, 0.8996532135724935, 0.4617662685997189, 0.8662758173813611, 0.6950640132130909, 0.11384265777953528, 0.8211687279301303, 0.13654299935856884, 0.9427524465845276, 0.3245428323076234, 0.2527813977211112, 0.8655122680707059, 0.4411643300451029, 0.22937499394794747, 0.23011462977975317, 0.11348740038070859, 0.17853949062814967, 0.004611360743448922, 0.9988597407025583, 0.6924980236312221, 0.60797191356741, 0.6527568650404367, 0.3938874845482979, 0.8141607672710679, 0.12718999184477187, 0.7402457178848773, 0.41723613113803837, 0.6958475656349062, 0.42998086936229574, 0.6230112674826288, 0.18527768657801702, 0.874842575564471, 0.5479418936733255, 0.9859145309490163, 0.7157453546830789, 0.34615440107576334, 0.5870278721312644, 0.41218843701077135, 0.7019409078509081, 0.1440435825274784, 0.36703734472023297, 0.06371038907196425, 0.04714625900978564, 0.7184559590812846, 0.5287572647784727, 0.9401976098570366, 0.09494019152473765, 0.34008211090440565, 0.2424883556214803, 0.005324435383794568, 0.9928928893173476, 0.8542958220263841, 0.23007378540654466, 0.14707149165236166, 0.4660955585294446, 0.13309328464806325, 0.09636676961567325, 0.7569828720103773, 0.4775446608182434, 0.8260385525458304, 0.35516062997177944, 0.21691054113139385, 0.38006905009735625, 0.9468327529935355, 0.22331853139935354, 0.5743447396964694, 0.10421276904292975, 0.9892755863575466, 0.5368611594514313, 0.48384072848110793, 0.7713077989952398, 0.6633136215655587, 0.2376129541363976, 0.7789458140454246, 0.6285117798923718, 0.9579934970656497, 0.6938457329522634, 0.6999834750245203, 0.21934891813510882, 0.5107744830626958, 0.8346061350028926, 0.24210017730762945, 0.7508284087495897, 0.7165410868282825, 0.42228164260730927, 0.5089106055033364, 0.7178728704026556, 0.28333647658186445, 0.32320007972005615, 0.21155780836560234, 0.11461181066428716, 0.3284851855281554, 0.2459304014714967, 0.9656942759083292, 0.46203813067664035, 0.6232583008891794, 0.8559863339514736, 0.31516270235384924, 0.225442418374026, 0.983683786598966, 0.5646760587071815, 0.509691161554503, 0.7094532696088564, 0.9729007769671282, 0.2221535135333954, 0.14414711457788953, 0.49210252931568155, 0.7671079173832106, 0.13401269446246644, 0.5272197529589203, 0.6974636041807614, 0.454561375697381, 0.39059476893807765, 0.2665770758839745, 0.4600172693577741, 0.9956716820777409, 0.9733482748729216, 0.09027096031244197, 0.9048323779483399, 0.05300834478535865, 0.16459882914718738, 0.295068703088849, 0.17982403593321317, 0.9947530295009638, 0.6032875815437914, 0.8798438620232121, 0.9665891315762632, 0.01691406769012005, 0.09330649892997367, 0.519747305983865, 0.1941859526012205, 0.18975478444073468, 0.8020998035219016, 0.8014814418985219, 0.7107070686407869, 0.9871753651166717, 0.36667085096654617, 0.33091709938244385, 0.802672580282057, 0.8964840002697279, 0.055362131568039485, 0.49960637482208325, 0.38027799724689915, 0.5297993188162736, 0.31517432033425197, 0.07274498451592526, 0.23069086667094807, 0.6409295467122671, 0.16646878166285084, 0.5414595154044679, 0.8095098152082733, 0.6510457385646523, 0.09911306726427394, 0.7969984069218402, 0.5721174017134306, 0.93317539650041, 0.6994641962580981, 0.22574849572195277, 0.6455650081886004, 0.8534739033888298, 0.6628540385360743, 0.3084070890474545, 0.06899332581083373, 0.06550646345280664, 0.15410202756593905, 0.21845076088768411, 0.9604101901396449, 0.5317175664884066, 0.8213328801473302, 0.599069264284308, 0.210183244977463, 0.42133500007145486, 0.8570607285135572, 0.768944816505736, 0.7946033521319958, 0.8410794253249685, 0.6284605858776166, 0.750843801490543, 0.21010132816684868, 0.8802708367285719, 0.7592776012791623, 0.05106282497639314, 0.6101141284740381, 0.47992052710105804, 0.19217205362522283, 0.746034279389591, 0.6531298482332277, 0.5699779340881684, 0.5174006193950021, 0.2259910769756367, 0.4809480397264284, 0.652890699168418, 0.7820707879012718, 0.8743543663862345, 0.5538255348073641, 0.582447137080728, 0.6716849533174305, 0.22268937907696684, 0.9890050566022818, 0.5541889075585067, 0.515608214074164, 0.888139735811496, 0.8242084955932218, 0.24984607330046216, 0.09237230760169513, 0.44587268255913604, 0.8307383824283587, 0.8745004335085346, 0.6739274252733057, 0.8829127214471387, 0.1567507833514158, 0.7416278331708741, 0.5494138100630886, 0.8501147967801344, 0.516621902968229, 0.30858127013001957, 0.3310779466091962, 0.03621829224458817, 0.20569180232173667, 0.5579884748363508, 0.526898196913997, 0.7888057415346172, 0.4514473347791339, 0.7736627261595458, 0.051080299341005, 0.10219569670991757, 0.6468919799669196, 0.5212759393659728, 0.8169386184216805, 0.9878918222284728, 0.11741179717839534, 0.07869622481578953, 0.22459562684546053, 0.7612222526577985, 0.1932758137094569, 0.6489953558427113, 0.8030289789723672, 0.5671511298072509, 0.5168847480153456, 0.1513968712787681, 0.3144020540420388, 0.4171971927403608, 0.47467795121449463, 0.42010548756808486, 0.4335261502343306, 0.22066659611976203, 0.06534940990636373, 0.8304559201576897, 0.5052594902318217, 0.3834896204688172, 0.5770768693957145, 0.2954251462997399, 0.289806375602886, 0.19570221301213897, 0.798951740688039, 0.6768584570460314, 0.6627277072633985, 0.27239287242293064, 0.5980948933938186, 0.2818717789676537, 0.5983307377875673, 0.6697612098354004, 0.8009735484736075, 0.15597080680102027, 0.16813286258669724, 0.8091624876113125, 0.4497973550812967, 0.12545260127070001, 0.5475611564743095, 0.7375545350117, 0.73208148411277, 0.06984304851055989, 0.43047312032821405, 0.6882158663077973};
		//test(values);
		Genetic genetic = new Genetic();
		genetic.start();
	}
}