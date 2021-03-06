package ikm;
import javax.microedition.lcdui.Displayable;

/**
 * ViewManager interface for handling switches from one view to the next of
 * previous one. Usually the only outside reference need for views
 * (Displayables, such as Forms and Canvases) is to be able to set another view
 * active. This interface hides the actual implementation from views, so when a
 * ViewManager is passed to a view instead of e.g. passing a reference to the
 * main MIDlet itself, view can not access any other public methods of the
 * implementation class. Also the actual class implementing the interface can be
 * easily changed later on if needed.
 */
public interface ViewManager {
    /**
     * Show the view passed as an argument. In other words, take the passed view
     * and set it to be the currently shown view of the MIDlet display.
     * @param view The new view to be shown
     */
    public void showView(Displayable view);

    /**
     * Show the view which was shown before the currently shown view. Discard
     * the current view.
     */
    public void goBack();
}
