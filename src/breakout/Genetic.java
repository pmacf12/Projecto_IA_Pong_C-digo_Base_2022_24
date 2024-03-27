package breakout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import utils.Commons;

public class Genetic {
	private ArrayList<BreakoutBoard> population;
	public static final int DIMPOPULATION = 100;
	private static final int GENERATIONS = 1500;
	private static final int MUTATIONS = 150;
	private double bestfitness = 99333.1;
	private ArrayList<BreakoutBoard> bestIndividuals;

	public Genetic() throws IOException {
		this.population = new ArrayList<>();
		bestIndividuals = new ArrayList<BreakoutBoard>(10);
	}

	public ArrayList<BreakoutBoard> getPopulation() {
		return population;
	}

	public void setPopulation(ArrayList<BreakoutBoard> population) {
		this.population = population;
	}

	public void initPopulation() {
		for (int pp = 0; pp < DIMPOPULATION; pp++) {
			BreakoutBoard breakoutBoard = new BreakoutBoard(new PredictNextMove(new FeedforwardNeuralNetwork(
					Commons.BREAKOUT_STATE_SIZE, Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS)), false, 1);
			population.add(breakoutBoard);
			breakoutBoard.runSimulation();
		}
	}

	public BreakoutBoard tournamentSelection(int tournamentSize) {
		BreakoutBoard bestCandidate = null;
		Random random = new Random();
		for (int i = 0; i < tournamentSize; i++) {
			int index = (int) (random.nextInt(population.size()));
			BreakoutBoard candidate = population.get(index);
			if (bestCandidate == null || candidate.getFitness() > bestCandidate.getFitness())
				bestCandidate = candidate;
		}
		return bestCandidate;
	}

	public void bestBreakoutBoards(BreakoutBoard board) {
		if (bestIndividuals.size() <= 9) {
			bestIndividuals.add(board);
		} else {
			bestIndividuals.remove(bestIndividuals.get(0));
			bestIndividuals.add(board);
		}
	}

	public ArrayList<BreakoutBoard> nPointCrossover(BreakoutBoard parentOne, BreakoutBoard parentTwo, int generation,
			int nPoints) throws IOException {
		ArrayList<Double> weightsOne = parentOne.getPredictor().getNetwork().getWeights();
		ArrayList<Double> weightsTwo = parentTwo.getPredictor().getNetwork().getWeights();

		ArrayList<Double> childrenWeightsOne = new ArrayList<>();
		ArrayList<Double> childrenWeightsTwo = new ArrayList<>();

		int length = weightsOne.size();
		List<Integer> crossoverPoints = generateCrossoverPoints(length, nPoints);

		boolean swap = false;
		int crossoverIndex = 0;
		for (int i = 0; i < length; i++) {
			if (crossoverIndex < crossoverPoints.size() && i == crossoverPoints.get(crossoverIndex)) {
				swap = !swap;
				crossoverIndex++;
			}

			if (swap) {
				childrenWeightsOne.add(weightsTwo.get(i));
				childrenWeightsTwo.add(weightsOne.get(i));
			} else {
				childrenWeightsOne.add(weightsOne.get(i));
				childrenWeightsTwo.add(weightsTwo.get(i));
			}
		}

		BreakoutBoard childOne = new BreakoutBoard(
				new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE,
						Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS, childrenWeightsOne)),
				false, 1);
		childOne.runSimulation();
		write(childOne, generation, "cross");

		BreakoutBoard childTwo = new BreakoutBoard(
				new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE,
						Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS, childrenWeightsTwo)),
				false, 1);
		childTwo.runSimulation();
		write(childTwo, generation, "cross");

		ArrayList<BreakoutBoard> children = new ArrayList<>();
		children.add(childOne);
		children.add(childTwo);
		return children;
	}

	private List<Integer> generateCrossoverPoints(int length, int nPoints) {
		List<Integer> crossoverPoints = new ArrayList<>();
		Random random = new Random();

		for (int i = 0; i < nPoints; i++) {
			int point = random.nextInt(length);
			if (!crossoverPoints.contains(point)) {
				crossoverPoints.add(point);
			}
		}

		Collections.sort(crossoverPoints);
		return crossoverPoints;
	}

	public BreakoutBoard mutate(BreakoutBoard inputBoard, int generation) throws IOException {
		ArrayList<Double> weights = inputBoard.getPredictor().getNetwork().getWeights();
		int length = inputBoard.getPredictor().getNetwork().getWeightsLength();
		Random random = new Random();
		for (int mutate = 0; mutate < MUTATIONS; mutate++) {
			int position = (int) (random.nextInt(length));
			weights.set(position, -1 + (2) * random.nextDouble());
		}
		BreakoutBoard breakoutBoard = new BreakoutBoard(
				new PredictNextMove(new FeedforwardNeuralNetwork(Commons.BREAKOUT_STATE_SIZE,
						Commons.BREAKOUT_HIDDEN_DIM, Commons.BREAKOUT_NUM_ACTIONS, weights)),
				false, 1);
		breakoutBoard.runSimulation();
		write(breakoutBoard, generation, "mutation");
		return breakoutBoard;
	}

	public void start() throws IOException {
		File fitness = new File("fitness.txt");
		fitness.delete();
		fitness.createNewFile();
		initPopulation();

		for (int generation = 0; generation < GENERATIONS; generation++) {
			ArrayList<BreakoutBoard> newPopulation = new ArrayList<>();
			for (int best = 0; best < bestIndividuals.size(); best++) {
				newPopulation.add(bestIndividuals.get(best));
			}

			for (int index = 0; index < Math.floor((DIMPOPULATION - bestIndividuals.size())) * 0.25; index++) {
				BreakoutBoard parentOne = tournamentSelection(5);
				BreakoutBoard parentTwo = tournamentSelection(5);
				ArrayList<BreakoutBoard> children = nPointCrossover(parentOne, parentTwo, generation, 5);
				newPopulation.add(parentOne);
				newPopulation.add(parentTwo);

				for (BreakoutBoard br : children) {
					if (Math.random() <= 0.50)
						newPopulation.add(mutate(br, generation));
					else
						newPopulation.add(children.get(0));
				}

			}
			setPopulation(newPopulation);
		}
	}

	public void write(BreakoutBoard breakoutBoard, int generation, String spot) throws IOException {
		if (breakoutBoard.getFitness() > bestfitness) {
			FileWriter writer = new FileWriter(new File("fitness.txt"), true);
			writer.write("Generation: " + generation + "\ndouble[] values = {"
				+ breakoutBoard.getPredictor().getNetwork().getWeights() + "};\nFitness: " + breakoutBoard.getFitness()
				+ "\n");
			bestfitness = breakoutBoard.getFitness();
			bestBreakoutBoards(breakoutBoard);
			System.out.println(bestfitness + " " + generation +" " + spot);
			writer.close();
		}
	}

}
