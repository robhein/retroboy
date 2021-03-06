package se.embargo.onebit.rs;

import se.embargo.onebit.R;
import se.embargo.onebit.filter.ScriptC_bayer;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

public class BayerScript implements IBitmapFilter {
	private RenderScript _renderContext;
	private ScriptC_bayer _filter;

	public BayerScript(Context context) {
        _renderContext = RenderScript.create(context);
        _filter = new ScriptC_bayer(_renderContext, context.getResources(), R.raw.bayer);
	}
	
	@Override
	public Bitmap apply(Bitmap input) {
        Allocation imagebuf = Allocation.createFromBitmap(
        	_renderContext, input, 
        	Allocation.MipmapControl.MIPMAP_NONE, 
        	Allocation.USAGE_SCRIPT);

        _filter.set_gIn(imagebuf);
        _filter.set_gOut(imagebuf);
        _filter.set_gScript(_filter);
        _filter.invoke_filter();
        
        // Reuse the input bitmap
        imagebuf.copyTo(input);
        return input;
	}
}
