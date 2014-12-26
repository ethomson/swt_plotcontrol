import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class TestPlotData extends PlotData
{
    Display display;
    int[] xvalues;
    Color fillColor;
    Color strokeColor;

    TestPlotData(Display display)
    {
        this.display = display;
        
        fillColor = display.getSystemColor(SWT.COLOR_GREEN);
        strokeColor = display.getSystemColor(SWT.COLOR_DARK_GREEN);
        
        xvalues = new int[1010];
    }
    
    public void setFillColor(Color color)
    {
        fillColor = color;
    }
    
    public Color getFillColor()
    {
        return fillColor;
    }
    
    public void setStrokeColor(Color color)
    {
        strokeColor = color;
    }
    
    public Color getStrokeColor()
    {
        return strokeColor;
    }

    public int getStrokeWidth()
    {
        return 2;
    }
    
    public int getValue(int domain)
    {        
        while(xvalues[domain + 500] == 0)
        {
            int rand1 = (int)(Math.random() * 100);
            int rand2 = (int)(Math.random() * 100);
            int posneg = (rand2 % 2 == 0) ? -1 : 1;
            posneg = 1;
            
            xvalues[domain + 500] = rand1 * posneg;
        }
        
        return xvalues[domain + 500];
    }

    public int getMaximum()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
