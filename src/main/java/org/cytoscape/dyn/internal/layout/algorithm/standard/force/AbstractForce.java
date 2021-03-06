package org.cytoscape.dyn.internal.layout.algorithm.standard.force;

/**
 * <code> AbstractForce </code> is an abstract base class for force functions in a force simulation.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractForce implements Force 
{

    protected float[] params;
    protected float[] minValues;
    protected float[] maxValues;

    /**
     * Initialize this force function. This default implementation does nothing.
     * @param fsim the encompassing ForceSimulator
     */
    public void init(ForceSimulator fsim) 
    {
        // do nothing.
    }

    /**
     * @see prefuse.util.force.Force#getParameterCount()
     */
    public int getParameterCount() 
    {
        return ( params == null ? 0 : params.length );
    }

    /**
     * @see prefuse.util.force.Force#getParameter(int)
     */
    public float getParameter(int i) {
        if ( i < 0 || params == null || i >= params.length ) 
        {
            throw new IndexOutOfBoundsException();
        } 
        else 
        {
            return params[i];
        }
    }
    
    /**
     * @see prefuse.util.force.Force#getMinValue(int)
     */
    public float getMinValue(int i) {
        if ( i < 0 || params == null || i >= params.length ) 
        {
            throw new IndexOutOfBoundsException();
        } 
        else 
        {
            return minValues[i];
        }
    }
    
    /**
     * @see prefuse.util.force.Force#getMaxValue(int)
     */
    public float getMaxValue(int i) {
        if ( i < 0 || params == null || i >= params.length ) 
        {
            throw new IndexOutOfBoundsException();
        } 
        else 
        {
            return maxValues[i];
        }
    }
    
    /**
     * @see prefuse.util.force.Force#getParameterName(int)
     */
    public String getParameterName(int i) 
    {
        String[] pnames = getParameterNames();
        if ( i < 0 || pnames == null || i >= pnames.length ) 
        {
            throw new IndexOutOfBoundsException();
        } 
        else 
        {
            return pnames[i];
        }
    }

    /**
     * @see prefuse.util.force.Force#setParameter(int, float)
     */
    public void setParameter(int i, float val) 
    {
        if ( i < 0 || params == null || i >= params.length ) 
        {
            throw new IndexOutOfBoundsException();
        } 
        else 
        {
            params[i] = val;
        }
    }
    
    /**
     * @see prefuse.util.force.Force#setMinValue(int, float)
     */
    public void setMinValue(int i, float val) 
    {
        if ( i < 0 || params == null || i >= params.length ) 
        {
            throw new IndexOutOfBoundsException();
        } 
        else 
        {
            minValues[i] = val;
        }
    }
    
    /**
     * @see prefuse.util.force.Force#setMaxValue(int, float)
     */
    public void setMaxValue(int i, float val) 
    {
        if ( i < 0 || params == null || i >= params.length ) 
        {
            throw new IndexOutOfBoundsException();
        } 
        else 
        {
            maxValues[i] = val;
        }
    }
    
    protected abstract String[] getParameterNames();
    
    /**
     * @see prefuse.util.force.Force#isItemForce()
     */
    public boolean isItemForce() 
    {
        return false;
    }
    
    /**
     * @see prefuse.util.force.Force#isSpringForce()
     */
    public boolean isSpringForce() 
    {
        return false;
    }
    
    /**
     * @see prefuse.util.force.Force#getForce(prefuse.util.force.ForceItem)
     */
    public void getForce(ForceItem item) 
    {
        throw new UnsupportedOperationException(
            "This class does not support this operation");
    }
    
    /**
     * @see prefuse.util.force.Force#getForce(prefuse.util.force.Spring)
     */
    public void getForce(Spring spring) 
    {
        throw new UnsupportedOperationException(
            "This class does not support this operation");
    }
    
}
