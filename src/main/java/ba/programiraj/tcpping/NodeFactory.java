package ba.programiraj.tcpping;

public class NodeFactory {

    public static Node getNode(NodeSettings nodeSettings) {
        if (nodeSettings.getNodeType() == NodeType.CATCHER) {
            return new Catcher(nodeSettings);
        } else if (nodeSettings.getNodeType() == NodeType.PITCHER) {
            return new Pitcher(nodeSettings);
        }
        return null;
    }
}
