package fr.gjouneau.adaptable.HTMLLoopPerforation;

import org.graalvm.options.OptionCategory;
import org.graalvm.options.OptionDescriptors;
import org.graalvm.options.OptionKey;
import org.graalvm.options.OptionStability;
import org.graalvm.options.OptionValues;

import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.language.decision.model.Softgoal;
import com.oracle.truffle.adaptable.language.decision.model.Task;
import com.oracle.truffle.adaptable.language.decision.model.Variable;
import com.oracle.truffle.adaptable.module.TruffleAdaptableModule;
import com.oracle.truffle.api.Option;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Registration;

import fr.gjouneau.truffle.HTML.HTMLAdaptationContext;
import fr.gjouneau.truffle.HTML.HTMLLanguage;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.UL;

@Registration(id = HTMLLoopPerforationInstrument.ID, name = "HTML Loop Perforation", version = "0.1", services = HTMLLoopPerforationInstrument.class)
public class HTMLLoopPerforationInstrument extends TruffleAdaptableModule<HTMLAdaptationContext, HTMLLanguage> {
	
	public static final String ID = "loop-perforation";
	
	private Task perforationRate;
	private Task accuracyImpact;
	
	@Option(name = "", help = "Enable loop perforation (default: false).", category = OptionCategory.USER, stability = OptionStability.STABLE)
    static final OptionKey<Boolean> ENABLED = new OptionKey<Boolean>(false);
	
	@Override
    protected OptionDescriptors getOptionDescriptors() {
        return new HTMLLoopPerforationInstrumentOptionDescriptors();
    }

	@Override
	public void init(HTMLAdaptationContext adaptationContext, OptionValues options) {
		Variable smallListPR = new Variable("Small List PR", 0, 0);
		Variable listPR = new Variable("List PR", 0, 0);
		Variable bigListPR = new Variable("Big List PR", 0, 0);
		
		SourceSectionFilter filter = SourceSectionFilter.newBuilder().tagIs(UL.class).build();
		attachExecutionListener(filter, new ListListner(smallListPR, listPR, bigListPR));
		
		perforationRate = new Task("Perforation Rate");
		accuracyImpact = new Task("Accuracy Impact");
		
		perforationRate.addContribution(smallListPR, 0.33);
		perforationRate.addContribution(listPR, 0.33);
		perforationRate.addContribution(bigListPR, 0.33);
		
		accuracyImpact.addContribution(smallListPR, 4.0 / 7.0);
		accuracyImpact.addContribution(listPR, 2.0 / 7.0);
		accuracyImpact.addContribution(bigListPR, 1.0 / 7.0);
	}

	@Override
	public void connectSoftGoal(Softgoal softgoal) {
		if (softgoal.ID.equals("Energy")) {
			softgoal.addContribution(perforationRate, 0.3);
		}
		if (softgoal.ID.equals("Accuracy")) {
			softgoal.addContribution(accuracyImpact, -0.7);
		}
	}

	@Override
	public void connectResource(Resource resource) {}

}
