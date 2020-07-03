package fr.gjouneau.adaptable.ConditionalLoading;

import org.graalvm.options.OptionCategory;
import org.graalvm.options.OptionDescriptors;
import org.graalvm.options.OptionKey;
import org.graalvm.options.OptionStability;
import org.graalvm.options.OptionValues;

import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.language.decision.model.Softgoal;
import com.oracle.truffle.adaptable.language.decision.model.Variable;
import com.oracle.truffle.adaptable.module.TruffleAdaptableModule;
import com.oracle.truffle.api.Option;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Registration;

import fr.gjouneau.truffle.HTML.HTMLAdaptationContext;
import fr.gjouneau.truffle.HTML.HTMLLanguage;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.A;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.Attribute;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.BASE;

@Registration(id = ConditionalLoading.ID, name = "ConditionalLoading", version = "0.1", services = ConditionalLoading.class)
public class ConditionalLoading extends TruffleAdaptableModule<HTMLAdaptationContext, HTMLLanguage> {
	
	public static final String ID = "conditional-loading";
	
	private Resource destroy;
	
	@Option(name = "", help = "Enable conditional loading (default: false).", category = OptionCategory.USER, stability = OptionStability.STABLE)
    static final OptionKey<Boolean> ENABLED = new OptionKey<Boolean>(false);
	
	@Override
    protected OptionDescriptors getOptionDescriptors() {
        return new ConditionalLoadingOptionDescriptors();
    }

	@Override
	public void init(HTMLAdaptationContext adaptationContext, OptionValues options) {
        if (!ENABLED.getValue(options)) {return;}
		
		AttributeAdaptationListner attr = new AttributeAdaptationListner();
		ParentAdaptationListener a = new ParentAdaptationListener(attr);
		ParentAdaptationListener base = new ParentAdaptationListener(attr);
		
		SourceSectionFilter filter = SourceSectionFilter.newBuilder().tagIs(Attribute.class).build();
        SourceSectionFilter anchor = SourceSectionFilter.newBuilder().tagIs(A.class).build();
        SourceSectionFilter baseFilter = SourceSectionFilter.newBuilder().tagIs(BASE.class).build();
        
        attachExecutionListener(filter, attr);
        attachExecutionListener(anchor, a);
        attachExecutionListener(baseFilter, base);
        
        destroy = new Resource("Apply ConditionalLoading");
        destroy.setValue(1.0);
	}

	@Override
	public void connectSoftGoal(Softgoal softgoal) {
		if (softgoal.ID.equals("Energy")) {
			softgoal.addContribution(destroy, 0.3);
		}
		if (softgoal.ID.equals("Accuracy")) {
			softgoal.addContribution(destroy, -0.5);
		}
	}

	@Override
	public void connectResource(Resource resource) {
	}

}
