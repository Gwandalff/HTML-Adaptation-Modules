package fr.gjouneau.HTMLLoopPerforation;

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

import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.OL;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.UL;

@Registration(id = HTMLLoopPerforationInstrument.ID, name = "HTML Loop Perforation", version = "0.1", services = HTMLLoopPerforationInstrument.class)
public class HTMLLoopPerforationInstrument extends TruffleInstrument {
	
	public static final String ID = "loop-perforation";
	
	@Option(name = "", help = "Enable loop perforation (default: false).", category = OptionCategory.USER, stability = OptionStability.STABLE)
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
        SourceSectionFilter filter = SourceSectionFilter.newBuilder().tagIs(UL.class).tagIs(OL.class).build();
        instrumenter.attachExecutionEventListener(filter, new ListListner());
    }
	
	@Override
    protected OptionDescriptors getOptionDescriptors() {
        return new HTMLLoopPerforationInstrumentOptionDescriptors();
    }

}
