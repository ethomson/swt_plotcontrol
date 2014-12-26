
public class PlotRendererStandard extends PlotRenderer
{    
    public PlotRendererStandard(PlotSetup plotSetup, int xOrigin, int yOrigin, int chunkSize)
    {
        super(plotSetup, xOrigin, yOrigin, chunkSize);
    }
    
    public int getXStepping()
    {
        int domainPoints = ((plotSetup.getDomainEnd() - plotSetup.getDomainStart()) / plotSetup.getDomainStepping()) + 1;
        return (xSize / (domainPoints - 1));
    }
       
    protected int getChunkReturnSize()
    {
        return 2;
    }
    
    protected void getChunk(int dataIdx, int xStart, int xStepping, int domainStart, int domainEnd, int domainStepping, int[] stack, int[] points, int pointStart)
    {
        PlotData plotData = (PlotData) plotSetup.getPlotData().get(dataIdx);
        boolean pad = ((plotSetup.getMultipleStyle() & PlotSetup.MULTIPLE_STYLE_STACK) == PlotSetup.MULTIPLE_STYLE_STACK && stack != null);
        boolean invert = ((plotSetup.getMultipleStyle() & PlotSetup.MULTIPLE_STYLE_INVERSE) == PlotSetup.MULTIPLE_STYLE_INVERSE && (dataIdx + 1) % 2 == 0);
        
        for(int i = 0, x = xStart, domain = domainStart, pointIdx = 0;
            i < chunkSize;
            i++, domain += domainStepping, x += xStepping)
        {
            // we need to complete the chunk.  (these won't actually be displayed.)
            if(domain > domainEnd)
            {
                points[pointStart + pointIdx++] = points[pointStart + (pointIdx - 3)];
                points[pointStart + pointIdx++] = points[pointStart + (pointIdx - 3)];
            }
            else
            {
                // when we invert, simply flip the sign of the value -- if we're stacking on top
                // of an existing graph, set that value as the padding.  then the y value is the
                // new y value on top of the padding.
                int range = invert ? (0 - plotData.getValue(domain)) : plotData.getValue(domain);
                int padding = pad ? stack[pointStart + pointIdx + 1] : 0;
                int y = pad ? (padding - (computeY(0) - computeY(range))) : computeY(range);
                
                points[pointStart + pointIdx++] = x;
                points[pointStart + pointIdx++] = y;
            }
        }        
    }    
}
