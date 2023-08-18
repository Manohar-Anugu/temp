package version2

import android.content.Context
import android.graphics.Rect
import com.journeyapps.barcodescanner.BarcodeView

/**
 * Created by Manohar on 28-10-2021.
 */
class CustomScannerView(context: Context) : BarcodeView(context) {


    override fun calculateFramingRect(
        container: Rect?,
        surface: Rect
    ): Rect {
        // intersection is the part of the container that is used for the preview
        val intersection = Rect(container)
        val intersects = intersection.intersect(surface)
        if (framingRectSize != null) {
            // Specific size is specified. Make sure it's not larger than the container or surface.
            intersection.top = 0
            intersection.left = 0
            intersection.right = framingRectSize.width
            intersection.bottom = framingRectSize.height

        }
        return intersection
    }
}