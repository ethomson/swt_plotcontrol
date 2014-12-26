import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class PlotControl extends Composite implements PaintListener
{
    private static Point SIZE_MINIMUM = new Point(200, 100);
    private static Point SIZE_DEFAULT = new Point(500, 200);
    private static int CHUNK_SIZE = 3;
    
    private Point size;
    private Canvas canvas;
    private GC gc;
    private PlotSetup plotSetup;
    
    public PlotControl(Composite parent, int style)
    {
        super(parent, style);

        this.setLayout(new FillLayout());

        size = new Point(SIZE_DEFAULT.x, SIZE_DEFAULT.y);

        canvas = new Canvas(this, SWT.NONE);
        canvas.setLocation(0, 0);
        canvas.addPaintListener(this);
        canvas.pack();

        gc = new GC(canvas);

        this.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

        pack();
    }

    public Point computeSize(int xhint, int yhint, boolean changed)
    {
        Point computed = new Point(SIZE_DEFAULT.x, SIZE_DEFAULT.y);

        // if they're asking for a hint bigger than our default size, use the
        // hinted size
        if (xhint != SWT.DEFAULT && xhint > computed.x)
            computed.x = xhint;
        if (yhint != SWT.DEFAULT && yhint > computed.y)
            computed.y = yhint;

        return computed;
    }
    
    public void setSize(int x, int y)
    {
        size.x = Math.max(x, SIZE_MINIMUM.x);
        size.y = Math.max(y, SIZE_MINIMUM.y);
    }

    
    public Point computeSize(int xhint, int yhint)
    {
        return computeSize(xhint, yhint, true);
    }
    
    public void setSize(Point p)
    {
        setSize(p.x, p.y);
    }

    public void pack(boolean changed)
    {
        setSize(SIZE_DEFAULT.x, SIZE_DEFAULT.y);
    }
    
    public void pack()
    {
        pack(true);
    }
    
    public void setBackground(Color background)
    {
        super.setBackground(background);
        canvas.setBackground(background);
    }
    
    public void setPlotSetup(PlotSetup plotSetup)
    {
        this.plotSetup = plotSetup;
    }
    
    public void paintControl(PaintEvent e)
    {
        // handle if this is a resize event
        // TODO: really we should handle resizeevents with a ControlListener
        if (e.width != size.x || e.height != size.y)
            setSize(e.width, e.height);
     
        // reference: if we use "domain", or "range" we're talking about the
        // domain/range of the actual data points.  when we talk about "x" and
        // "y", we're talking about the coordinates used to paint the control.
        int domainStart = plotSetup.getDomainStart();
        int domainEnd = plotSetup.getDomainEnd();
        int domainSize = domainEnd - domainStart;
        int domainStepping = plotSetup.getDomainStepping();
        int domainPoints = (domainSize / domainStepping) + 1;
        
        // if we're using the inverse style of multiple stacking, then we need to correct
        // the range start...
        if((plotSetup.getMultipleStyle() & PlotSetup.MULTIPLE_STYLE_INVERSE) == PlotSetup.MULTIPLE_STYLE_INVERSE)
        {
            plotSetup.setRangeStart(0 - plotSetup.getRangeEnd());
        }
        
        // how much we need to stretch to fill this control
        int xStepping = (size.x / (domainPoints - 1));
        
        // we "chunk" so that we don't use enormous amounts of memory for large datasets
        // so figure out how much to paint at a time
        int chunkSize = (domainPoints <= CHUNK_SIZE || CHUNK_SIZE == 0) ? domainPoints : CHUNK_SIZE;
        
        PlotRenderer plotRenderer = new PlotRendererStandard(plotSetup, size.x, size.y, chunkSize);
        
        xStepping = plotRenderer.getXStepping();
        
        for(int domain = domainStart, x = 0;
            domain <= domainEnd;
            domain += (chunkSize * domainStepping), x += (chunkSize * xStepping))
        {
            int[][] polygonPoints = new int[plotSetup.getPlotData().size()][];
            int[][] polylinePoints = new int[plotSetup.getPlotData().size()][];
            
            for(int i = 0; i < plotSetup.getPlotData().size(); i++)
            {
                // setup the data to stack upon, if necessary
                int diff = (plotSetup.getMultipleStyle() & PlotSetup.MULTIPLE_STYLE_INVERSE) == PlotSetup.MULTIPLE_STYLE_INVERSE ? 2 : 1;
                int[] polygonStack = (i >= diff) ? polygonPoints[i - diff] : null;
                int[] polylineStack = (i >= diff) ? polylinePoints[i - diff] : null;
                
                polygonPoints[i] = plotRenderer.getPolygon(i, x, xStepping, domain, domainEnd, domainStepping, polygonStack);
                polylinePoints[i] = plotRenderer.getPolyline(i, x, xStepping, domain, domainEnd, domainStepping, polylineStack);
            }
            
            for(int i = plotSetup.getPlotData().size() - 1; i >= 0; i--)
            {
                drawPolygon(polygonPoints[i], (PlotData)plotSetup.getPlotData().get(i));
                drawPolyline(polylinePoints[i], (PlotData)plotSetup.getPlotData().get(i));
            }
        }
        
        drawLabel(plotRenderer);
    }
    
    private void drawLabel(PlotRenderer plotRenderer)
    {
        gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
        gc.setLineWidth(1);
        gc.setLineStyle(SWT.LINE_SOLID);

        drawLabelAxes(plotRenderer);
        
        gc.setAlpha(plotSetup.getGuideAlpha());
        gc.setLineStyle(plotSetup.getGuideLineStyle());

        if(plotSetup.getRangeGuideStepping() > 0)
            drawLabelRange(plotRenderer);
        
        if(plotSetup.getDomainGuideStepping() > 0)
            drawLabelDomain(plotRenderer);
    }
    
    private void drawLabelAxes(PlotRenderer plotRenderer)
    {
        int xOrigin = plotRenderer.computeX(0);
        int yOrigin = plotRenderer.computeY(0);
        
        // draw the axes in solid, always
                
        gc.drawLine(0, yOrigin, size.x, yOrigin);
        gc.drawLine(xOrigin, 0, xOrigin, size.y);
    }
    
    private void drawLabelRange(PlotRenderer plotRenderer)
    {
        for(int rangeGuide = plotSetup.getRangeStart();
            rangeGuide <= plotSetup.getRangeEnd();
            rangeGuide += plotSetup.getRangeGuideStepping())
        {
            // we've already drawn the axes
            if(rangeGuide != 0)
            {
                int yLabel = plotRenderer.computeY(rangeGuide);
                gc.drawLine(0, yLabel, size.x, yLabel);
            }
        }
    }
    
    private void drawLabelDomain(PlotRenderer plotRenderer)
    {
        for(int domainGuide = plotSetup.getDomainStart();
            domainGuide <= plotSetup.getDomainEnd();
            domainGuide += plotSetup.getDomainGuideStepping())
        {
            // we've already drawn the axes
            if(domainGuide != 0)
            {
                int xGuide = plotRenderer.computeX(domainGuide);
                gc.drawLine(xGuide, 0, xGuide, size.y);
            }
        }
    }
    
    private void drawPolygon(int[] polygonPoints, PlotData data)
    {
        if(data.getFillColor() == null)
            return;
        
        gc.setAlpha(data.getAlpha());
        gc.setBackground(data.getFillColor());
        gc.fillPolygon(polygonPoints);
    }
    
    private void drawPolyline(int[] polylinePoints, PlotData data)
    {        
        if(data.getStrokeColor() == null)
            return;
        
        gc.setAlpha(data.getAlpha());
        gc.setForeground(data.getStrokeColor());
        gc.setLineWidth(data.getStrokeWidth());
        gc.setLineStyle(SWT.LINE_SOLID);
        gc.drawPolyline(polylinePoints);
    }
    
    public void dispose()
    {
        super.dispose();
        gc.dispose();
    }
}
