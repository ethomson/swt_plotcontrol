import org.eclipse.swt.graphics.Color;

public abstract class PlotData
{
    /**
     * The color to draw the fill beneath the graph, or null for no fill
     * @return a Color, or null
     */
    public Color getFillColor()
    {
        return null;
    }
    
    /**
     * The color to draw the stroke, or null for no stroke
     * @return a Color, or null
     */
    public Color getStrokeColor()
    {
        return null;
    }
    
    /**
     * The width to stroke at the maximum of the function
     * @return an integer describing the width of the stroke
     */
    public int getStrokeWidth()
    {
        return 5;
    }
    
    /**
     * The alpha to draw this data with
     * @return an integer describing the alpha transparency
     */
    public int getAlpha()
    {
        return 255;
    }
    
    /**
     * Get a y-value for a particular x-value
     * @param x a multiple of PlotSetup.getDomainStepping() between PlotSetup.getDomainStart()
     *          and PlotSetup.getDomainEnd() (inclusive) which is to be plotted
     * @return  the y value for this particular x (must be between PlotSetup.getRangeStart() and
     *          PlotSetup.getRangeEnd(), inclusive)
     */
    abstract public int getValue(int x);

    /**
     * Get the maximum y-value contained in this dataset.
     * Note: this function is not called unless the PlotSetup.setMultipleType() is set to
     * PlotSetup.MULTIPLE_STACK and you have multiple PlotDatas associated with a PlotSetup.
     * @return the largest y-value which getValue() will return
     */
    abstract public int getMaximum();    
}
