PlotControl for SWT
===================

PlotControl is a simple SWT widget for plotting a line graph. It includes
functionality for filling and stroking the resultant curve, handling positive
and negative x/y values, scaling to fit inside the widget space and standard
or MRTG-style plots.  PlotControl is available under an MIT license.

Example Usage
-------------

A data provider returns the values to plot - given any y-value, it will return
the appropriate x-value. The interface is simple, and it can be backed by any
data storage you'd like - however the data provider is queried by the UI
thread, so this should not be a long-running computation. (Better to cache a
lookup table.)

An example data provider (which draws a straight line at x=10):

````
public class SimplePlotData extends PlotData
{
	private Color fillColor;
	private Color strokeColor;

	SimplePlotData(Display display)
	{
		fillColor = display.getSystemColor(SWT.COLOR_GREEN);
		strokeColor = display.getSystemColor(SWT.COLOR_DARK_GREEN);
	}

	public Color getFillColor() { return fillColor; }
	public Color getStrokColor() { return strokeColor; }
	public int getStrokeWidth() { return 2; }
	public int getValue(int y) { return 10; }
	public int getMaximum() { return 10; }
}
````

Once you have created a data provider, you can instantiate a PlotControl that
uses that provider. PlotControl objects are configured by PlotSetup objects,
which setup their size (domain and range of the graph) and their data providers.

Example PlotControl instantiation:

````
// setup a plot from y=-50 to y=50, with a domain stepping of 5
// (ie, this will plot y={ -50, -45, -40, ... 40, 45, 50 }
// guidelines will be lightly drawn at y={-50, -40 .. 40, 50 }
// range will be drawn from x=0 to x=20
PlotSetup plotSetup = new PlotSetup();
plotSetup.setDomainStart(-50);
plotSetup.setDomainEnd(50);
plotSetup.setDomainStepping(5);
plotSetup.setDomainGuideStepping(10);
plotSetup.setGuideAlpha(25);
plotSetup.setRangeStart(0);
plotSetup.setRangeEnd(20);
plotSetup.addPlotData(new SimplePlotData(display));

PlotControl plotControl = new PlotControl(shell, SWT.NONE);
plotControl.setPlotSetup(plotSetup);
````

At this point, placing the PlotControl into your composite's layout
should be all that's required, and it should draw a dark green line at x=10,
filled down to the x-axis in lighter green.
