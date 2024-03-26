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
		for (int index = 0; index < values.length; index++) {
			valuesarray.add(values[index]);
		}
		Breakout breakout = new Breakout(new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS,valuesarray)), 7);
	}

	public static void main(String[] args) throws IOException {
		//double[] values = {-0.8305045639962174, -0.8391605920096306, 0.15573896802273612, 0.48925544156484935, -0.15249056336546363, -0.13809415318355023, 0.6172173249006601, -0.012994856801678, -0.23209210497504884, -0.2047812296354401, 0.7945980087183355, 0.18412476632428065, -0.5086744015195297, -0.39875235189943736, -0.6333435992952094, -0.762321472821486, -0.11006958046782023, -0.3114837974310334, 0.2355092297047754, -0.17259505234109684, 0.9268456429328813, -0.45969364749588837, 0.8554810215416775, -0.960022319873264, 0.2619390297916113, 0.6580879891224383, -0.12511483801312195, 0.49962502116556085, 0.9404381046646926, 0.8281776386244077, 0.23876592859566226, 0.22363769820350687, 0.13641003978365784, -0.15118018747633677, -0.42656664756822105, 0.16567976589226507, 0.23278254366369344, 0.5781127185479293, 0.41299806996975263, -0.12802498212942748, -0.07890311046555043, 0.4518496322993528, -0.09455004483618157, 0.3608807161596792, -0.49585787691772043, -0.12300350900000678, -0.3093329139296177, -0.8753174554709477, 0.17850276375411722, 0.04840806475170667, 0.9883961256141731, -0.13001264779308386, 0.5000062964413641, 0.7122010130338183, -0.4711941531094874, -0.6864248361642411, -0.16099560022173454, 0.04814121882540823, 0.4828465535901729, 0.6834320203503179, -0.5435409834926834, 0.8333752827775156, 0.7210297025680139, 0.9422086858223517, 0.9112803389143105, -0.41479376826147263, 0.973808566741178, 0.049965943775490906, -0.9119437722545807, 0.29596642438644905, 0.29325783307785613, 0.4306398543810206, 0.48846595710805807, 0.026366099532772802, 0.23025869911378227, 0.32906018277910687, 0.5202764500624222, 0.7609494785137587, 0.7768968430139334, -0.8951208142316398, 0.9099864092128485, 0.14373607191587623, -0.35319381218378654, -0.3254156898592515, 0.48362921783808277, -0.14735525944337513, -0.6337760058897646, 0.950275221878325, -0.7657425388583987, -0.6130201785032969, -0.789508007083473, 0.48872893206408796, -0.37650181496969215, -0.48682974609579976, 0.7069440370834128, 0.6985743391871029, -0.22068831948712897, 0.7339075618670032, 0.04800153403567964, 0.5259456594143508, 0.8038385835789503, -0.5164758768835462, 0.39252873515796294, 0.28898704639305217, 0.6468523158472173, 0.3309261247130313, -0.3614238303014359, -0.4385494308249456, 0.02003652047035609, -0.6044450677604407, 0.4408016402025461, 0.6204047256237251, 0.03920090521952857, -0.8729768992549962, -0.13726214981957208, -0.23097412807457207, 0.09507166934611777, 0.19445837544643396, 0.9906175568266451, 0.11491302893918576, -0.805219117419482, 0.28437369508342947, 0.5613892628832946, -0.7365470930095583, 0.8325948210401872, -0.07841859863732514, -0.22870748832080268};
		//test(values);
		Genetic genetic = new Genetic();
		genetic.start();
	}
}