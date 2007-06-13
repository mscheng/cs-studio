package org.csstudio.util.formula.node;

import org.csstudio.util.formula.Node;

/** One computational node.
 *  @author Kay Kasemir
 */
public class LessThanNode extends AbstractBinaryNode
{
    public LessThanNode(Node left, Node right)
    {
        super(left, right);
    }
    
    public double eval()
    {
        final double a = left.eval();
        final double b = right.eval();
        return (a < b) ? 1.0 : 0.0;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString()
    {
        return "(" + left + " < " + right + ")";
    }
}
