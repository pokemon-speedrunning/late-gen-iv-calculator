package be.lycoops.vincent.iv.calculator.reset;

import be.lycoops.vincent.iv.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

public class ResetPresenter implements Initializable {

    private Preferences prefs;

    private static String routePref = "ROUTE_PREF";
    private static String askOnChangePref = "ASK_ON_CHANGE_PREF";

    @FXML
    private ComboBox<String> routeSelect;

    @FXML
    private CheckBox askOnChangeButton;

    @Inject
    private Pokemon pokemon;

    @Inject
    private HiddenPowerCalculator hiddenPowerCalculator;

    @Inject
    private NatureCalculator natureCalculator;

    @Inject
    private History history;

    public void reset() {
        pokemon.setPokemonModelFromFile(PokemonModelProvider.loadPokemonFile(EffortValueProvider.getRoute()));
        pokemon.reset();
        natureCalculator.reset();
        pokemon.setHiddenPower(hiddenPowerCalculator.setUnknown());
        history.reset();
    }

    public void askOnChangeAction() {
        prefs.put(routePref, String.valueOf(askOnChangeButton.isSelected()));
    }

    public void routeChange() {
        if (!askOnChangeButton.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Route Change");
            alert.setHeaderText("Do you really want to change the route?");
            alert.setContentText("This resets all current progress");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                changeRoute();
            }
        } else {
            changeRoute();
        }

    }

    private void changeRoute() {
        String route = routeSelect.getSelectionModel().getSelectedItem();
        EffortValueProvider.setRoute(route);

        prefs.put(routePref, route);

        reset();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prefs = Preferences.userRoot().node(this.getClass().getName());

        askOnChangeButton.setSelected(prefs.getBoolean(askOnChangePref,false));

        routeSelect.getItems().removeAll(routeSelect.getItems());

        Stream<Path> walk = null;
        try {
            walk = Files.walk(FilePathProvider.getPath("be/lycoops/vincent/iv/routes"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (walk != null) {
            for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
                String route = it.next().getFileName().toString().split("\\.txt")[0];
                if (route.equals("routes")) continue;
                routeSelect.getItems().add(route);
                if (route.equals(prefs.get(routePref, ""))) {
                    routeSelect.getSelectionModel().select(route);
                    EffortValueProvider.setRoute(route);
                }
            }
        }

        reset();
    }

}
