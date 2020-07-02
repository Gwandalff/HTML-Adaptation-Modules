package fr.gjouneau.image.degradation;

import org.graalvm.options.OptionCategory;
import org.graalvm.options.OptionDescriptors;
import org.graalvm.options.OptionKey;
import org.graalvm.options.OptionStability;
import org.graalvm.options.OptionValues;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.Option;
import com.oracle.truffle.api.instrumentation.Instrumenter;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Registration;

import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.Attribute;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.BASE;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.IMG;


@Registration(id = ImageDegradationInstrument.ID, name = "ImageDegradation", version = "0.1", services = ImageDegradationInstrument.class)
public class ImageDegradationInstrument extends TruffleInstrument {
	
	@Option(name = "", help = "Enable image degradation (default: false).", category = OptionCategory.USER, stability = OptionStability.STABLE)
    static final OptionKey<Boolean> ENABLED = new OptionKey<Boolean>(false);
	
	@Option(name = "folder", help = "Folder for local images", category = OptionCategory.USER, stability = OptionStability.STABLE)
    static final OptionKey<String> FOLDER = new OptionKey<String>("");

	@CompilationFinal private String url;
	
	public static final String ID = "degrade-image";

	@Override
	protected void onCreate(Env env) {
		final OptionValues options = env.getOptions();
		String folder = FOLDER.getValue(options);
        if (ENABLED.getValue(options) && !folder.equals("")) {
        	System.err.println("HELLO");
            enable(env, folder);
            env.registerService(this);
        }
	}
	
	private void enable(final Env env, final String folder) {
		Instrumenter instrumenter = env.getInstrumenter();
		
        SourceSectionFilter imageFilter = SourceSectionFilter.newBuilder().tagIs(IMG.class).includeInternal(false).build();
        SourceSectionFilter attriFilter = SourceSectionFilter.newBuilder().tagIs(Attribute.class).includeInternal(false).build();
        SourceSectionFilter baseFilter = SourceSectionFilter.newBuilder().tagIs(BASE.class).includeInternal(false).build();
        SrcListener srcListener = new SrcListener(100*1000, folder);
        instrumenter.attachExecutionEventListener(imageFilter, new ImageListener(srcListener));
        instrumenter.attachExecutionEventListener(baseFilter, new BaseListener(srcListener));
        instrumenter.attachExecutionEventListener(attriFilter, srcListener);
    }
	
	@Override
    protected OptionDescriptors getOptionDescriptors() {
        return new ImageDegradationInstrumentOptionDescriptors();
    }

}
