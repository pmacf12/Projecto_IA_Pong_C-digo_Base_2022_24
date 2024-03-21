package breakout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import utils.Commons;

public class Genetic {
	private ArrayList<BreakoutBoard> population;
	public static final int DIMPOPULATION = 100;
    private static final int GENERATIONS = 1000;
	private static final int MUTATIONS = 20;

	public Genetic() {
		this.population = new ArrayList<>();
	}
	
	public ArrayList<BreakoutBoard> getPopulation() {
		return population;
	}

	public void setPopulation(ArrayList<BreakoutBoard> population) {
		this.population = population;
	}
	
	public void initPopulation() {
        for (int pp = 0; pp < DIMPOPULATION; pp++) {
            BreakoutBoard breakoutBoard = new BreakoutBoard(new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS)), true, 3);
            population.add(breakoutBoard);
            breakoutBoard.runSimulation();
        }
}
	public BreakoutBoard tournamentSelection(int tournamentSize) {
	    BreakoutBoard bestCandidate = null;
	    for (int i = 0; i < tournamentSize; i++) {
	        int random = (int) (Math.random() * population.size());
	        BreakoutBoard candidate = population.get(random);
	        if (bestCandidate == null || candidate.getFitness() > bestCandidate.getFitness()) 
	            bestCandidate = candidate;   
	    }
	    return bestCandidate;
	}


	//public ArrayList<BreakoutBoard> bestBreakoutBoard(ArrayList<BreakoutBoard> population) {
	//	ArrayList<BreakoutBoard> bestCandidates = null;
	//	BreakoutBoard atual = null;
	//	for (int i = 0; i < population.size(); i++) {
	//		atual = population.get(i);
	//		if (atual.getFitness() > bestCandidates.get(0).getFitness()) {
	//			bestCandidates.add(atual);
	//		}
	//	}
	//	return bestCandidate;
	//}

	public ArrayList<BreakoutBoard> uniformCrossover(BreakoutBoard parentOne, BreakoutBoard parentTwo, int generation) throws IOException {
		ArrayList<Double> weightsOne = parentOne.getPredictor().getNetwork().getWeights();
		ArrayList<Double> weightsTwo = parentTwo.getPredictor().getNetwork().getWeights();
	
		ArrayList<Double> childrenWeightsOne = new ArrayList<>();
		ArrayList<Double> childrenWeightsTwo = new ArrayList<>();
		
		int length = weightsOne.size();
		for (int i = 0; i < length; i++) {
			if (Math.random() < 0.5) {
				childrenWeightsOne.add(weightsOne.get(i));
				childrenWeightsTwo.add(weightsTwo.get(i));
			} else {
				childrenWeightsOne.add(weightsTwo.get(i));
				childrenWeightsTwo.add(weightsOne.get(i));
			}
		}

		BreakoutBoard childOne = new BreakoutBoard(new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS, childrenWeightsOne)), false, 3);
		childOne.runSimulation();
		write(childOne, generation);
		
		BreakoutBoard childTwo = new BreakoutBoard(new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS, childrenWeightsTwo)), false, 3);                        
		childTwo.runSimulation();
		write(childTwo, generation);
	
		ArrayList<BreakoutBoard> children = new ArrayList<>();
		children.add(childOne);
		children.add(childTwo);
		return children;
	}
	
	
	public BreakoutBoard mutate(BreakoutBoard inputBoard, int generation) throws IOException {
		ArrayList<Double> weights = inputBoard.getPredictor().getNetwork().getWeights();
		int length = inputBoard.getPredictor().getNetwork().getWeightsLength();
		for(int mutate = 0; mutate < MUTATIONS; mutate++){
			int position = (int) (Math.random() * length); 
			weights.set(position, Math.random());
		}
		BreakoutBoard breakoutBoard = new BreakoutBoard(new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE,Commons.BREAKOUT_HIDDEN_DIM,Commons.BREAKOUT_NUM_ACTIONS, weights)), false, 3);
		breakoutBoard.runSimulation();
		write(breakoutBoard, generation);
		return breakoutBoard;
	}

	public void start() throws IOException{
		File fitness = new File("fitness.txt");
		fitness.delete();
		fitness.createNewFile();
		
        initPopulation();
        double best_fitness = 0;
        BreakoutBoard best_individual;
        
        for (int generation = 0; generation <  GENERATIONS; generation++) {
            ArrayList<BreakoutBoard> newPopulation = new ArrayList<>();
            for (int index = 0; index < (int) DIMPOPULATION * 0.25; index++) {
                BreakoutBoard parentOne = tournamentSelection(2);
                BreakoutBoard parentTwo = tournamentSelection(2);
                ArrayList<BreakoutBoard> children = uniformCrossover(parentOne, parentTwo, generation);
				newPopulation.add(parentOne);
				newPopulation.add(parentTwo);
				
                if (Math.random() <=  0.20)
					newPopulation.add(mutate(children.get(0), generation));
                 else 
                    newPopulation.add(children.get(0));
                
				if (Math.random() <=  0.20) 
					newPopulation.add(mutate(children.get(1), generation));
                 else 
                    newPopulation.add(children.get(1));
				
				
            }
			setPopulation(newPopulation);
        }

        }
	
	
	public void write(BreakoutBoard breakoutBoard, int generation) throws IOException {
		FileWriter writer = new FileWriter(new File("fitness.txt"), true);
		writer.write("Generation: " + generation + "\ndouble[] values = {" + breakoutBoard.getPredictor().getNetwork().getWeights() + "};\nFitness: " + breakoutBoard.getFitness() + "\n");
		writer.close();
	}
	
}
