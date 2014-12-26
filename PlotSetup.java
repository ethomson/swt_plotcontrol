import java.util.ArrayList;

import org.eclipse.swt.SWT;

public class PlotSetup
{
    public static int MULTIPLE_STYLE_OVERDRAW = 0x01;
    public static int MULTIPLE_STYLE_STACK = 0x02;
    public static int MULTIPLE_STYLE_INVERSE = 0x04;
    
    private ArrayList plotData;
    private int rangeStart, rangeEnd;
    private int domainStart, domainEnd;
    private int domainStepping = 1;
    private int domainGuideStepping = 0;
    private int rangeGuideStepping = 0;
    private int guideAlpha = 255;
    private int guideLineStyle = SWT.LINE_DOT;
    private int multipleStyle = MULTIPLE_STYLE_OVERDRAW;
    
    PlotSetup()
    {
        plotData = new ArrayList();
    }
    
    public ArrayList getPlotData()
    {
        return plotData;
    }
    
    public void addPlotData(PlotData p)
    {
        plotData.add(p);
    }

    public int getDomainEnd()
    {
        return domainEnd;
    }

    public void setDomainEnd(int domainEnd)
    {
        this.domainEnd = domainEnd;
    }

    public int getDomainStart()
    {
        return domainStart;
    }

    public void setDomainStart(int domainStart)
    {
        this.domainStart = domainStart;
    }

    public int getDomainStepping()
    {
        return domainStepping;
    }

    public void setDomainStepping(int domainStepping)
    {
        this.domainStepping = domainStepping;
    }
    
    public int getDomainGuideStepping()
    {
        return domainGuideStepping;
    }

    public void setDomainGuideStepping(int domainLabelStepping)
    {
        this.domainGuideStepping = domainLabelStepping;
    }
    
    public int getRangeGuideStepping()
    {
        return rangeGuideStepping;
    }
    
    public void setRangeGuideStepping(int rangeLabelStepping)
    {
        this.rangeGuideStepping = rangeLabelStepping;
    }

    public int getGuideAlpha()
    {
        return guideAlpha;
    }

    public void setGuideAlpha(int labelAlpha)
    {
        this.guideAlpha = labelAlpha;
    }

    public int getGuideLineStyle()
    {
        return guideLineStyle;
    }

    public void setGuideLineStyle(int labelLineStyle)
    {
        this.guideLineStyle = labelLineStyle;
    }

    public int getRangeEnd()
    {
        return rangeEnd;
    }

    public void setRangeEnd(int rangeEnd)
    {
        this.rangeEnd = rangeEnd;
    }

    public int getRangeStart()
    {
        return rangeStart;
    }

    public void setRangeStart(int rangeStart)
    {
        this.rangeStart = rangeStart;
    }

    public void setPlotData(ArrayList plotData)
    {
        this.plotData = plotData;
    }

    public int getMultipleStyle()
    {
        return multipleStyle;
    }

    public void setMultipleStyle(int multipleStyle)
    {
        this.multipleStyle = multipleStyle;
    }
}
