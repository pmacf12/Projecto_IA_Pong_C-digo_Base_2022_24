package breakout;

import utils.GameController;

public class PredictNextMove implements GameController{

    private FeedforwardNeuralNetwork network;

    public PredictNextMove(FeedforwardNeuralNetwork network){
            this.network=network;
    }
    @Override
    public int nextMove(double[] currentState) {
        double[] outputs = this.network.forward(currentState);
        //Ver quando outputs sao iguais
        if(outputs[0] >= outputs[1]) {
            return 1;
        } else{
            return 2;
        }
    }

    public FeedforwardNeuralNetwork getNetwork() {
        return this.network;
    }
    
}