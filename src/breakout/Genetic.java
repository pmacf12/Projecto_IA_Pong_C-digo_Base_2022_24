package breakout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;

import utils.Commons;

public class Genetic {
	private ArrayList<BreakoutBoard> population;
	public static final int DIMPOPULATION = 5;
    private static final int GENERATIONS = 10;

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
            BreakoutBoard breakoutBoard = new BreakoutBoard(new PredictNextMove
            (new FeedforwardNeuralNetwork
            (Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS)), false, 1);
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

	public ArrayList<BreakoutBoard> crossover(BreakoutBoard parentOne, BreakoutBoard parentTwo) throws IOException {
		int length = parentOne.getPredictor().getNetwork().getWeightsLength();
		int crosspoint = (int) (Math.random() * length);

		ArrayList<BreakoutBoard> children = new ArrayList<>();
		
		ArrayList<Double> weightsOne = parentOne.getPredictor().getNetwork().getWeights();
		ArrayList<Double> weightsTwo = parentTwo.getPredictor().getNetwork().getWeights();

		ArrayList<Double> childrenWeightsOne = new ArrayList<>();
		ArrayList<Double> childrenWeightsTwo = new ArrayList<>();
		
		for (int ii = 0; ii <= crosspoint; ii++) {
			childrenWeightsOne.add(weightsOne.get(ii));
			childrenWeightsTwo.add(weightsTwo.get(ii));
		}
		
		for (int jj = crosspoint + 1; jj < length; jj++) {
			childrenWeightsOne.add(weightsTwo.get(jj));
			childrenWeightsTwo.add(weightsOne.get(jj));
		}

		BreakoutBoard childOne = new BreakoutBoard
								(new PredictNextMove
								(new FeedforwardNeuralNetwork
								(Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS, childrenWeightsOne)), false, 1);
		childOne.runSimulation();
		children.add(childOne);
		write(childOne);
		BreakoutBoard childTwo = new BreakoutBoard
								(new PredictNextMove
								(new FeedforwardNeuralNetwork
								(Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS, childrenWeightsTwo)), false, 1);						
		childTwo.runSimulation();
		children.add(childTwo);
		write(childTwo);
		return children;
	}
	
	public boolean checkPopulation() {
		for(BreakoutBoard b : getPopulation()) {
			if(b.getFitness() == 28)
				return true;
		}
		return false;
	}

	public void start() throws IOException{
        this.initPopulation();
        double best_fitness = 0;
        BreakoutBoard best_individual;
        
        for (int generation = 0; generation <  GENERATIONS; generation++) {
			System.err.println("Geração " + generation);
            Genetic newPopulation = new Genetic();

            for (int index = 0; index < (int) Genetic.DIMPOPULATION * 0.2; index++) {
                BreakoutBoard parentOne = this.tournamentSelection(2);
                BreakoutBoard parentTwo = this.tournamentSelection(2);
                ArrayList<BreakoutBoard> children = newPopulation.crossover(parentOne, parentTwo);
                newPopulation.getPopulation().add(children.get(0));
				newPopulation.getPopulation().add(children.get(1));
            }
			
			//Mutation
            for (int index = 0; index < (int) Genetic.DIMPOPULATION * 0.8; index++) {
                if (Math.random() <= 0.05) {
					ArrayList<Double> weights = this.getPopulation().get(index).getPredictor().getNetwork().getWeights();
					int length = this.getPopulation().get(index).getPredictor().getNetwork().getWeightsLength();
					int position = (int) Math.random() * length; 
					weights.set(position, Math.random());
					BreakoutBoard breakoutBoard = new BreakoutBoard(new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE,Commons.BREAKOUT_HIDDEN_DIM,Commons.BREAKOUT_NUM_ACTIONS, weights)), false, 1);
                    newPopulation.getPopulation().add(breakoutBoard);
					breakoutBoard.runSimulation();
					write(breakoutBoard);
                } else {
                    newPopulation.getPopulation().add(this.getPopulation().get(index));
                }
            }
            this.setPopulation(newPopulation.getPopulation());
			
        }
		
        
	}
	
	public void write(BreakoutBoard breakoutBoard) throws IOException {
		FileWriter writer = new FileWriter(new File("fitness.txt"), true);
		writer.write("Weights: " + breakoutBoard.getPredictor().getNetwork().getWeights() + "\nFitness: " + breakoutBoard.getFitness() + "\n");
		writer.close();
	}
	
}
