package popsizefunc.lphystudio.spi;

import lphy.core.model.Value;
import lphystudio.app.graphicalmodelpanel.viewer.Viewer;
import lphystudio.spi.StudioViewerImpl;
import popsizefunc.lphy.evolution.popsize.PopulationFunction;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The "Container" provider class that implements SPI
 * which include a list of {@link Viewer} to extend.
 * It requires a public no-args constructor.
 * @author Walter Xie
 */
public class PopSizeFuncViewerImpl extends StudioViewerImpl {

    /**
     * Required by ServiceLoader.
     */
    public PopSizeFuncViewerImpl() {
        if (viewerList == null) viewerList = new ArrayList<>();
    }


    /**
     * @return a list viewers to register
     */
    @Override
    public List<Viewer> getViewers() {
        return List.of(popSizeFuncViewer);
    }

    @Override
    public void register() {

//        Map<String, ? extends Viewer> newTypes = declareViewers();

        addViewers(getViewers(), viewerList, "LPhy studio viewers : ");
    }

    public String getExtensionName() {
        return "Pop-size function viewers";
    }


    /**
     * create the new viewers below, and add them to getViewers() ...
     */

    private static Viewer popSizeFuncViewer = new Viewer() {

        @Override
        public boolean match(Object value) {
            return value instanceof PopulationFunction ||
                    (value instanceof Value && ((Value) value).value() instanceof PopulationFunction);
        }

        @Override
        public JComponent getViewer(Object value) {
            //TODO create PopulationFunctionComponent((Value<PopulationFunction>) value)
            if (value instanceof PopulationFunction) {
                return new JLabel("PopulationFunction TODO : " + value.toString());
            }
            return new JLabel("Value<PopulationFunction> TODO : " + ((Value<PopulationFunction>) value).value().toString());
        }
        @Override
        public String toString() {
            return "Pop-size function Viewer";
        }
    };

}
