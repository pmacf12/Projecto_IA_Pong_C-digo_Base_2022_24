package breakout;
import java.util.ArrayList;

public class FeedforwardNeuralNetwork {
	
	private int inputDim;
	private int hiddenDim;
	private int outputDim;
	private double[][] hiddenWeights;
	private double[] hiddenBiases;
	private double[][] outputWeights;
	private double[] outputBiases;
	private int fitness;
	private ArrayList<Double> weights;
	
	public FeedforwardNeuralNetwork(int inputDim, int hiddenDim, int outputDim) {
		this.inputDim = inputDim;
		this.hiddenDim = hiddenDim;
		this.outputDim = outputDim;
		hiddenWeights= new double[inputDim][hiddenDim];
		hiddenBiases = new double[hiddenDim];
		outputWeights = new double[hiddenDim][outputDim];
		outputBiases = new double[outputDim];
		initializeParameters();
	}
	
	public FeedforwardNeuralNetwork(int inputDim, int hiddenDim, int outputDim, double[] values) {
		this.inputDim = inputDim;
		this.hiddenDim = hiddenDim;
		this.outputDim = outputDim;
		hiddenWeights= new double[inputDim][hiddenDim];
		hiddenBiases = new double[hiddenDim];
		outputWeights = new double[hiddenDim][outputDim];
		outputBiases = new double[outputDim];
		putValues(values);
	}
	
	public void initializeParameters() {
		weights = new ArrayList<>();
		for(int rh = 0; rh < hiddenWeights.length; rh++) {
			for(int ch = 0; ch < hiddenWeights[rh].length; ch ++) {
				double valor = Math.random();
				hiddenWeights[rh][ch] = valor;
				weights.add(valor);
			}
		}
		for(int hb = 0; hb < hiddenBiases.length; hb ++){
			double valor = Math.random();
			hiddenBiases[hb] = valor;
			weights.add(valor);
		}
		for(int rh = 0; rh < outputWeights.length; rh++) {
			for(int ch = 0; ch < outputWeights[rh].length; ch ++) {
				double valor = Math.random();
				outputWeights[rh][ch] = valor;
				weights.add(valor);
			}
		}
		for(int hb = 0; hb < outputBiases.length; hb ++){
			double valor = Math.random();
			outputBiases[hb] = valor;
			weights.add(valor);
		}
	}
	
	public ArrayList<Double> getWeights(){
		return weights;
	}
	
	public int getWeightsLength(){
		return weights.size();
	}

	public void putValues(double[] values) {
		int count = 0;
		for(int rh = 0; rh < hiddenWeights.length; rh++) {
			for(int ch = 0; ch < hiddenWeights[rh].length; ch ++) {
				hiddenWeights[rh][ch] = values[count];
				count++;
				
			}
		}
		for(int hb = 0; hb < hiddenBiases.length; hb ++) {
			hiddenBiases[hb] = values[count];
			count++;
			
		}
	
		for(int rh = 0; rh < outputWeights.length; rh++) {
			for(int ch = 0; ch < outputWeights[rh].length; ch ++) {
				outputWeights[rh][ch] = values[count];
				count++;
			}
		}
		for(int hb = 0; hb < outputBiases.length; hb ++) {
			outputBiases[hb] = values[count];
			count++;
		}
	}
	public double[] extractValues(int inputDim, int outputDim, double inputValues[] ,double[][] weights) {
		//Extração de valores para arraylist
		ArrayList<double[]> xValues = new ArrayList<double[]>();
		for(int ii = 0; ii < inputDim; ii++) {
			double[] values = new double[outputDim];
			for(int jj = 0; jj < outputDim; jj++) {
				values[jj] = weights[ii][jj];
			}
			xValues.add(values);
		}
		
		//Iteração por arraylist e depois cada elemento com a mesma posição
		double[] resultsHidden = new double[outputDim];
		for(int kk = 0; kk < outputDim; kk ++) {
			double calcs = 0;
			for(int ll = 0; ll < inputDim; ll++) {
				double[] values = xValues.get(ll);
				calcs += values[kk] * inputValues[ll];
			}
			resultsHidden[kk] = calcs;
		}
		return resultsHidden;
	}
	
	public double[] forward(double[] currentState) {
		double[] results = new double[outputDim];
		double[] resultsHidden = extractValues(inputDim, hiddenDim, currentState, hiddenWeights);
		
		double[] outputHidden = new double[hiddenDim];
		for(int mm = 0; mm < hiddenDim; mm++) {
			double result = resultsHidden[mm] + hiddenBiases[mm];
			outputHidden[mm] = sigmoid(result);
		}
		
		double[] resultsOutput = extractValues(hiddenDim, outputDim, outputHidden, outputWeights);
		for(int nn = 0; nn < results.length; nn++) {
			double resultOutput = resultsOutput[nn] + outputBiases[nn];
			results[nn] =  (sigmoid(resultOutput));
		}
		
		return results;
	}
	
	public double sigmoid(double value) {
		return 1 / (1 + Math.exp(-value));
	}
	@Override
	public String toString() {
		String result = "Neural Network: \nNumber of inputs: " + inputDim + "\n"
				+ "Weights between input and hidden layer with " + hiddenDim + "neurons: \n";
		String hidden = "";
		for (int input = 0; input < inputDim; input++) {
			for (int i = 0; i < hiddenDim; i++) {
				hidden += " input" + input + "-hidden" + i + ": " + hiddenWeights[input][i] + "\n";
			}
		}
		result += hidden;
		String biasHidden = "Hidden biases: \n";
		for (int i = 0; i < hiddenDim; i++) {
			biasHidden += " bias hidden" + i + ": " + hiddenBiases[i] + "\n";
		}
		result += biasHidden;
		String output = "Weights between hidden and output layer with " + outputDim + " neurons: \n";
		for (int hiddenw = 0; hiddenw < hiddenDim; hiddenw++) {
			for (int i = 0; i < outputDim; i++) {
				output += " hidden" + hiddenw + "-output" + i + ": " + outputWeights[hiddenw][i] + "\n";
			}
		}
		result += output;
		String biasOutput = "Ouput biases: \n";
		for (int i = 0; i < outputDim; i++) {
			biasOutput += " bias ouput" + i + ": " + outputBiases[i] + "\n";
		}
		result += biasOutput;
		return result;
	}

}
