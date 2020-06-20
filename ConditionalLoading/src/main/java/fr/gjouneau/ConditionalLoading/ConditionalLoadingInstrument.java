package fr.gjouneau.ConditionalLoading;

import org.graalvm.options.OptionCategory;
import org.graalvm.options.OptionDescriptors;
import org.graalvm.options.OptionKey;
import org.graalvm.options.OptionStability;
import org.graalvm.options.OptionValues;

import com.oracle.truffle.api.Option;
import com.oracle.truffle.api.instrumentation.Instrumenter;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Registration;

import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.Attribute;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.Text;

@Registration(id = ConditionalLoadingInstrument.ID, name = "ConditionalLoading", version = "0.1", services = ConditionalLoadingInstrument.class)
public class ConditionalLoadingInstrument extends TruffleInstrument {
	
	public static final String ID = "conditional-loading";
	
	@Option(name = "", help = "Enable conditional loading (default: false).", category = OptionCategory.USER, stability = OptionStability.STABLE)
    static final OptionKey<Boolean> ENABLED = new OptionKey<Boolean>(false);

	@Override
	protected void onCreate(Env env) {
		final OptionValues options = env.getOptions();
        if (ENABLED.getValue(options)) {
            enable(env);
            env.registerService(this);
        }
	}
	
	private void enable(final Env env) {
		Instrumenter instrumenter = env.getInstrumenter();
		
        SourceSectionFilter filter = SourceSectionFilter.newBuilder().tagIs(Attribute.class).build();
        instrumenter.attachExecutionEventListener(filter, new AttributeListner());
    }
	
	@Override
    protected OptionDescriptors getOptionDescriptors() {
        return new ConditionalLoadingInstrumentOptionDescriptors();
    }

}
