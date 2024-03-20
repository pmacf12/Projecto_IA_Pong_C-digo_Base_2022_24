package breakout;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("fitness.txt"))) {
        for (int pp = 0; pp < DIMPOPULATION; pp++) {
            BreakoutBoard breakoutBoard = new BreakoutBoard(new PredictNextMove
            (new FeedforwardNeuralNetwork
            (Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS)), false, 1);
            population.add(breakoutBoard);
            breakoutBoard.runSimulation();
            writer.write("Weights: " + breakoutBoard.getPredictor().getNetwork().getWeights() + "\nFitness: " + breakoutBoard.getFitness() + "\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
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

	public ArrayList<BreakoutBoard> crossover(BreakoutBoard parentOne, BreakoutBoard parentTwo) {
		int length = parentOne.getPredictor().getNetwork().getWeightsLength();
		int crosspoint = (int) (Math.random() * length);

		ArrayList<BreakoutBoard> children = new ArrayList<>();
		
		ArrayList<Double> weightsOne = parentOne.getPredictor().getNetwork().getWeights();
		ArrayList<Double> weightsTwo = parentTwo.getPredictor().getNetwork().getWeights();

		ArrayList<Double> childrenWeightsOne = new ArrayList<>(length);
		ArrayList<Double> childrenWeightsTwo = new ArrayList<>(length);
		
		for (int ii = 0; ii <= crosspoint; ii++) {
			childrenWeightsOne.add(weightsOne.get(ii));
			childrenWeightsTwo.add(weightsTwo.get(ii));
		}
		
		// Copie os elementos após o ponto de corte (crosspoint)
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
		BreakoutBoard childTwo = new BreakoutBoard
								(new PredictNextMove
								(new FeedforwardNeuralNetwork
								(Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS, childrenWeightsTwo)), false, 1);						
		childTwo.runSimulation();
		children.add(childTwo);
		return children;
	}
	
	public boolean checkPopulation() {
		for(BreakoutBoard b : getPopulation()) {
			if(b.getFitness() == 28)
				return true;
		}
		return false;
	}

	public void start(){
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
			 
            for (int index = 0; index < (int) Genetic.DIMPOPULATION * 0.8; index++) {
                if (Math.random() <= 0.05) {
                    newPopulation.getPopulation().add(this.getPopulation().get(index).mutate());
                } else {
                    newPopulation.getPopulation().add(this.getPopulation().get(index));
                }
            }
            this.setPopulation(newPopulation.getPopulation());
			
        }
		
        
	}


	
}
