
public abstract class PlotRenderer
{
    protected PlotSetup plotSetup;
    protected int xSize, ySize;
    protected int chunkSize;
    private int[] xLastPolygon, yLastPolygon;
    private int[] xLastPolyline, yLastPolyline;
    
    public PlotRenderer(PlotSetup plotSetup, int xSize, int ySize, int chunkSize)
    {
        this.plotSetup = plotSetup;
        this.xSize = xSize;
        this.ySize = ySize;
        this.chunkSize = chunkSize;
        
        int plotDataSize = plotSetup.getPlotData().size();
        this.xLastPolygon = new int[plotDataSize];
        this.yLastPolygon = new int[plotDataSize];
        this.xLastPolyline = new int[plotDataSize];
        this.yLastPolyline = new int[plotDataSize];
    }

    public int[] getPolygon(int dataIdx, int xStart, int xStepping, int domainStart, int domainEnd, int domainStepping, int[] polygonStack)
    {
        int pointSize = (chunkSize * getChunkReturnSize()) + 6;
        int[] points = new int[pointSize];
        
        // the polygon needs to start where the last polygon left off (if we're on the second
        // chunk, or at 0 if we're not chunking) so that a fill works properly
        if(xStart == 0)
        {
            points[0] = 0;
            points[1] = computeY(0);
            points[2] = 0;
            points[3] = computeY(0);
        }
        else
        {
            points[0] = xLastPolygon[dataIdx];
            points[1] = computeY(0);
            points[2] = xLastPolygon[dataIdx];
            points[3] = yLastPolygon[dataIdx];
        }
        
        getChunk(dataIdx, xStart, xStepping, domainStart, domainEnd, domainStepping, polygonStack, points, 4);
        
        // end back on the x axis, again for fill
        points[pointSize - 2] = points[pointSize - 4];
        points[pointSize - 1] = computeY(0);
        
        // store last drawn point for the next time getChunk is called...
        xLastPolygon[dataIdx] = points[pointSize - 4];
        yLastPolygon[dataIdx] = points[pointSize - 3];
        
        return points;
    }
    
    public int[] getPolyline(int dataIdx, int xStart, int xStepping, int domainStart, int domainEnd, int domainStepping, int[] polylineStack)
    {
        int pointSize = (chunkSize * getChunkReturnSize()) + 2;
        int[] points = new int[pointSize];
        
        getChunk(dataIdx, xStart, xStepping, domainStart, domainEnd, domainStepping, polylineStack, points, 2);
        
        // initial points for the polyline
        if(xStart == 0)
        {
            points[0] = 0;
            points[1] = points[3];
        }
        else
        {
            points[0] = xLastPolyline[dataIdx];
            points[1] = yLastPolyline[dataIdx];
        }
        
        xLastPolyline[dataIdx] = points[pointSize - 2];
        yLastPolyline[dataIdx] = points[pointSize - 1];
        
        return points;
    }
    
    protected abstract int  getChunkReturnSize();
    protected abstract void getChunk(int dataIdx, int xStart, int xStepping, int domainStart, int domainEnd, int domainStepping, int[] stack, int[] points, int pointStart);
    
    public abstract int getXStepping();
    
    public int computeX(int domain)
    {
        int xStepping = (xSize / ((plotSetup.getDomainEnd() - plotSetup.getDomainStart()) / plotSetup.getDomainStepping()));
        return ((domain - plotSetup.getDomainStart()) / plotSetup.getDomainStepping()) * xStepping;
    }
    
    public int computeY(int range)
    {
        int rangeStart = plotSetup.getRangeStart();
        int rangeEnd = plotSetup.getRangeEnd();
        int rangeHeight = rangeEnd - rangeStart;

        // adjust the height such that we have the point to plot as x% of the
        // range.
        float heightPercent = (float)(range - rangeStart) / (float)rangeHeight;

        // adjust the height again such that 100% of the range is at the top of
        // the control and 0% of the range is at the bottom of the control
        return (ySize - (int) (heightPercent * (float) ySize));
    }
}
