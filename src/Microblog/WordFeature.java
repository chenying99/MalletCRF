package Microblog;

import Utils.Constant;

import java.util.ArrayList;

/**
 * Created by wyc on 2015/5/7.
 */
public class WordFeature extends NodeFeature {
  public int index;
  public int polarity; //0, 1, 2
  public double[] potentials;

  public WordFeature(int index, int polarity) {
    this.index = index;
    this.polarity = polarity;
    this.name = "Word-" + polarity + "-" + index;
  }

  public void extract(ArrayList<Node> nodes) {
    // compute x
    this.x = new int[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      this.x[n] = 0;
      Node node = nodes.get(n);
      for (int i : node.word) {
        if (i == this.index) {
          this.x[n] = 1;
          break;
        }
      }
    }
    // compute values
    this.values = new double[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      this.values[n] = 0;
      Node node = nodes.get(n);
      for (int i : node.word) {
        if (i == this.index && node.label == this.polarity) {
          this.values[n] = 1;
          break;
        }
      }
    }
    // compute potentials
    switch (this.polarity) {
      case 0:
        this.potentials = Constant.Word0Ptl;
        break;
      case 1:
        this.potentials = Constant.Word1Ptl;
        break;
      case 2:
        this.potentials = Constant.Word2Ptl;
        break;
      default: // should never be reached
        this.potentials = Constant.Word1Ptl;
        break;
    }
  }

  public void extract(ArrayList<Node> nodes, ArrayList<Integer> words) {
    this.extract(nodes);
  }
}

abstract class SentimentWord extends NodeFeature {
  int polarity;

  public SentimentWord() {
    this.name = "SentimentWord";
    this.choiceNum = 10;
  }

  public void extract(ArrayList<Node> nodes) {
    System.out.println("Cannot extract SentimentWord without dict.");
  }

  public void extract(ArrayList<Node> nodes, ArrayList<Integer> words) {
    // compute x
    this.x = new int[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      this.x[n] = 0;
      Node node = nodes.get(n);
      for (int i : node.word) {
        if (words.contains(i))
          this.x[n] += 1;
      }
      this.x[n] = Math.min(9, this.x[n]);
    }
    // compute values
    this.values = new double[nodes.size()];
    for (int n = 0; n < nodes.size(); n++) {
      this.values[n] = 0;
      Node node = nodes.get(n);
      if (this.x[n] > 0 && node.label == this.polarity)
        this.values[n] = (double) this.x[n];
    }
  }
}

class PositiveWord extends SentimentWord {

  public PositiveWord() {
    this.name = "PositiveWord";
    this.choiceNum = 10;
    this.polarity = 2;
    this.potentials = Constant.PositiveWordPtl;
  }

}

class NeutralWord extends SentimentWord {

  public NeutralWord() {
    this.name = "NeutralWord";
    this.choiceNum = 10;
    this.polarity = 1;
    this.potentials = Constant.NeutralWordPtl;
  }

}

class NegativeWord extends SentimentWord {

  public NegativeWord() {
    this.name = "NegativeWord";
    this.choiceNum = 10;
    this.polarity = 0;
    this.potentials = Constant.NegativeWordPtl;
  }

}