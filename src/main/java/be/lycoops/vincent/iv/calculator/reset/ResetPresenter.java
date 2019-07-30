package be.lycoops.vincent.iv.calculator.reset;

import be.lycoops.vincent.iv.model.HiddenPowerCalculator;
import be.lycoops.vincent.iv.model.History;
import be.lycoops.vincent.iv.model.NatureCalculator;
import be.lycoops.vincent.iv.model.Pokemon;

import javax.inject.Inject;

public class ResetPresenter {

    @Inject
    private Pokemon pokemon;

    @Inject
    private HiddenPowerCalculator hiddenPowerCalculator;

    @Inject
    private NatureCalculator natureCalculator;

    @Inject
    private History history;

    public void reset() {
        pokemon.reset();
        natureCalculator.reset();
        pokemon.setHiddenPower(hiddenPowerCalculator.setUnknown());
        history.reset();
    }

}
