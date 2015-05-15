package Inference;

import Microblog.EdgeFeature;
import Microblog.Node;
import Microblog.NodeFeature;
import Microblog.Thread;
import Utils.Constant;
import cc.mallet.grmm.inference.Inferencer;
import cc.mallet.grmm.inference.JunctionTreeInferencer;
import cc.mallet.grmm.types.*;

import javax.swing.text.TabExpander;
import java.lang.ThreadGroup;

public class GraphBuilder {

    /*

     */
    public FactorGraph buildWithCRF(VarSet[] xNode, VarSet[] xEdge, VarSet y,
                                    Thread thread, double[] params) {


        FactorGraph mdl = new FactorGraph();

        // for each node feature
        // double[] potentialValue = new double[params.length];
        for(int i=0; i<thread.nodeFeatures.length; i++) {
            // for each node
            NodeFeature nodeFeature = thread.nodeFeatures[i];
            int nodeVariableCur = 0;
            for(int j=0; j<thread.nodes.size(); j++) {

                VarSet varSet = new HashVarSet(new Variable[]{
                        xNode[i].get(nodeVariableCur),
                        y.get(nodeVariableCur)
                });

                // potentialValue
                int potentialLength = nodeFeature.potentials[j].length;
                double[] potentialValue = new double[potentialLength];
                for (int t = 0; t < potentialLength; t++) {
                    // param times potential
                    potentialValue[t] = params[i] * potentialValue[t];
                }
                Factor factor = new TableFactor(varSet, potentialValue);
                mdl.addFactor(factor);
                nodeVariableCur += 1;
            }
        }

        // for each edge feature
        for(int i=0; i<thread.edgeFeatures.length; i++) {
            // for each node, from the second node

            int edgeVariableCur = 0;
            EdgeFeature edgeFeature = thread.edgeFeatures[i];
            for(int j=1; j<thread.nodes.size(); j++) {
                VarSet varSet = new HashVarSet(new Variable[]{

                        xEdge[i].get(edgeVariableCur),
                        y.get(edgeVariableCur),
                        y.get(edgeVariableCur-1)
                });
                int potentialLength = edgeFeature.potentials[j].length;
                double[] potentialValue = new double[potentialLength];
                for (int t = 0; t < potentialLength; t++) {
                    // param times potential
                    potentialValue[t] = params[i] * potentialValue[t];
                }
                Factor factor = new TableFactor(varSet, potentialValue);
                mdl.addFactor(factor);
                edgeVariableCur += 1;
            }
        }
    }


    /*
    public void inference(FactorGraph mdl, Thread thread) {

        Inferencer inf = new JunctionTreeInferencer();
        inf.computeMarginals(mdl);
        int nodeFeatureNum = thread.nodeFeatureNum;
        int edgeFeatureNum = thread.edgeFeatureNum;
        int nodeSize = thread.nodes.size();
        for(int n = 0; n < nodeFeatureNum; n++) {

            NodeFeature nodeFeature = thread.nodeFeatures[n];
            for(int j = 0; j < nodeSize; j++) {
                // Factor single = inf.lookupMarginal()
            }

        }

        for(int e = 1; e < edgeFeatureNum; e++) {


        }

    }
    */

    public static void main(String[] args) {


    }
}
