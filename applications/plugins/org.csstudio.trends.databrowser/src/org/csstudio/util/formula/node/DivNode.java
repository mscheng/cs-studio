package org.csstudio.util.formula.node;

import org.csstudio.util.formula.Node;

/** One computational node.
 *  @author Kay Kasemir
 */
public class DivNode extends AbstractBinaryNode
{
    public DivNode(Node left, Node right)
    {
        super(left, right);
    }
    
    public double eval()
    {
        final double a = left.eval();
        final double b = right.eval();
        if (b == 0)
            return 0;
        return a / b;
    }
    
    @SuppressWarnings("nls")
    @Override
   public String toString()
    {
        return "(" + left + " / " + right + ")";
    }

}
