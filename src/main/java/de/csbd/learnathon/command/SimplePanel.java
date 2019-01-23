package de.csbd.learnathon.command;

import java.util.logging.Logger;

import javax.swing.JPanel;

import org.scijava.Context;
import org.scijava.command.CommandService;
import org.scijava.plugin.Parameter;

import com.indago.util.ImglibUtil;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvHandlePanel;
import bdv.util.BdvSource;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import ij.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;


import ij.ImageJ;

import io.scif.img.IO;
import io.scif.img.ImgIOException;
 
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.real.FloatType;


public class SimplePanel {
	@Parameter
	private Context context;

	@Parameter
	private CommandService commandService;

	public Logger log;

	private BdvHandlePanel bdv;

	private final RandomAccessibleInterval< DoubleType > rawData;
	
	public SimplePanel( final RandomAccessibleInterval< DoubleType > image, final Context context ) {
		//this.rawData = image;
		
		
		final ImgFactory< FloatType > imgFactory = new CellImgFactory<>( new FloatType(), 5 );
        final Img< FloatType > img1 = imgFactory.create(image.dimension(0), image.dimension(1), 2);
        
        
        Cursor< FloatType > cursorInput = img1.cursor();
        
        while (cursorInput.hasNext()) {
        	cursorInput.fwd();
        	cursorInput.get().set(5f);
        }
        	
        
        
        this.rawData = HelloWorldCommand.toDoubleType(img1);
		
		context.inject( this );
				
		
		
	}
	
	public JPanel getPanel() {
		final BdvHandlePanel bdv = new BdvHandlePanel( null, Bdv.options().is2D() );
		final BdvSource source = BdvFunctions.show( rawData , "img", Bdv.options().addTo( bdv ) );
		
		return bdv.getViewerPanel();
	}
	
	
	
	public void close() {
		bdv.close();
	}
	
	

}
