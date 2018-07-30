package ba.programiraj.tcpping;

public abstract class Node {
    
    protected NodeSettings settings;
    
    public Node(NodeSettings settings) {
        this.settings = settings;
    }
    
    public abstract void run();
}
