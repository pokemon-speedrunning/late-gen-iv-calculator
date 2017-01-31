package be.lycoops.vincent.iv.calculator.reset;

import be.lycoops.vincent.iv.model.HiddenPowerCalculator;
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

    public void reset() {
        natureCalculator.reset();
        pokemon.reset();
        pokemon.setHiddenPower(hiddenPowerCalculator.setUnknown());
    }

}
