/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package de.csbd.learnathon.command;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.scijava.Context;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import ij.IJ;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;
import net.miginfocom.swing.MigLayout;

/**
 * This example illustrates how to create an ImageJ {@link Command} plugin.
 * <p>
 * The code here is a simple Gaussian blur using ImageJ Ops.
 * </p>
 * <p>
 * You should replace the parameter fields with your own inputs and outputs,
 * and replace the {@link run} method implementation with your own logic.
 * </p>
 */
@Plugin( type = Command.class, menuPath = "Plugins>Hello World" )
public class HelloWorldCommand<T extends RealType<T>> implements Command {
	
	
	@Parameter
	Dataset image;

	@Parameter
	Context context;

	@Parameter
	UIService ui;

	JFrame frame;
	
	
	private SimplePanel panel;


   
    @Override
	public void run() {
		panel = new SimplePanel( toDoubleType( image.getImgPlus().getImg() ), context);
		JPanel p = panel.getPanel();
		p.setMinimumSize(new Dimension(1000,  1000));
		frame = new JFrame( image.getImgPlus().getSource() );
		frame.setLayout( new MigLayout( "", "[grow]", "[][]" ) );
		frame.add( p, "h 100%, grow, wrap" );
		frame.addWindowListener( new WindowAdapter() {

			@Override
			public void windowClosed( final WindowEvent windowEvent ) {
				panel.close();
			}
		} );

		frame.setBounds( 100, 100, 1600, 1200 );
		frame.pack();
		frame.setVisible( true );
	}	
    	

    /**
     * This main function serves for development purposes.
     * It allows you to run the plugin immediately out of
     * your integrated development environment (IDE).
     *
     * @param args whatever, it's ignored
     * @throws Exception
     */
    public static void main(final String... args) throws Exception {
        // create the ImageJ application context with all available services
        final ImageJ ij = new ImageJ();
        ij.ui().showUI();
        
        IJ.openImage( "res/Stack.tif" ).show();

        ij.command().run( HelloWorldCommand.class, true );


    }
    
    
    public static RandomAccessibleInterval< DoubleType > toDoubleType( final RandomAccessibleInterval< ? extends RealType< ? > > image ) {
		if ( Util.getTypeFromInterval( image ) instanceof DoubleType )
			return ( RandomAccessibleInterval< DoubleType > ) image;
		return Converters.convert( image, ( i, o ) -> o.setReal( i.getRealDouble() ), new DoubleType() );
	}

}
