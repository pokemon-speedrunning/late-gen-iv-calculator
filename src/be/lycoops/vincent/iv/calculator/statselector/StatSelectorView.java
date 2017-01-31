package be.lycoops.vincent.iv.calculator.statselector;

import com.airhacks.afterburner.views.FXMLView;

import java.util.function.Function;

public class StatSelectorView extends FXMLView {
    public StatSelectorView(Function<String, Object> injectionContext) {
        super(injectionContext);
    }

    public static Function<String,Object> injectionStrategy(String stat) {
        return name -> name.equals("stat") ? stat : null;
    }
}
