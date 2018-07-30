package ba.programiraj.tcpping;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NodeFactoryTest {

    @Test
    public void factoryCreatesCatcher() {
        NodeSettings settings = new NodeSettings();
        settings.setNodeType(NodeType.CATCHER);
        Node node = NodeFactory.getNode(settings);
        assertTrue(node instanceof Catcher);
    }

    @Test
    public void factoryCreatesPither() {
        NodeSettings settings = new NodeSettings();
        settings.setNodeType(NodeType.PITCHER);
        Node node = NodeFactory.getNode(settings);
        assertTrue(node instanceof Pitcher);
    }
}
