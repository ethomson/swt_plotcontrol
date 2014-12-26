import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class GraphWidget
{
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
        
        TestPlotData pd = new TestPlotData(display);
        pd.setFillColor(display.getSystemColor(SWT.COLOR_RED));
        pd.setStrokeColor(display.getSystemColor(SWT.COLOR_DARK_RED));
        
        TestPlotData pd2 = new TestPlotData(display);
        pd2.setFillColor(display.getSystemColor(SWT.COLOR_GREEN));
        pd2.setStrokeColor(display.getSystemColor(SWT.COLOR_DARK_GREEN));
        
        TestPlotData pd3 = new TestPlotData(display);
        pd3.setFillColor(display.getSystemColor(SWT.COLOR_GRAY));
        pd3.setStrokeColor(display.getSystemColor(SWT.COLOR_DARK_GRAY));
        
        TestPlotData pd4 = new TestPlotData(display);
        pd4.setFillColor(display.getSystemColor(SWT.COLOR_BLUE));
        pd4.setStrokeColor(display.getSystemColor(SWT.COLOR_DARK_BLUE));
        
        PlotSetup plotSetup = new PlotSetup();
        plotSetup.setDomainStart(-500);
        plotSetup.setDomainEnd(500);
        plotSetup.setDomainStepping(10);
        plotSetup.setDomainGuideStepping(50);
        plotSetup.setRangeGuideStepping(10);
        plotSetup.setGuideAlpha(25);
        plotSetup.setRangeStart(-200);
        plotSetup.setRangeEnd(200);
        plotSetup.addPlotData(pd);
        plotSetup.addPlotData(pd2);
        plotSetup.addPlotData(pd3);
        plotSetup.addPlotData(pd4);        
        plotSetup.setMultipleStyle(PlotSetup.MULTIPLE_STYLE_STACK | PlotSetup.MULTIPLE_STYLE_INVERSE);
		
		PlotControl plotControl = new PlotControl(shell, SWT.NONE);
        plotControl.setPlotSetup(plotSetup);
        		
		shell.pack();
		shell.open();
		
		while(! shell.isDisposed())
		{
			if(! display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}