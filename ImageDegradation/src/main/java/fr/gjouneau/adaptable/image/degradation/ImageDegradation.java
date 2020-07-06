package fr.gjouneau.adaptable.image.degradation;

import org.graalvm.options.OptionCategory;
import org.graalvm.options.OptionDescriptors;
import org.graalvm.options.OptionKey;
import org.graalvm.options.OptionStability;
import org.graalvm.options.OptionValues;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.adaptable.language.decision.model.Resource;
import com.oracle.truffle.adaptable.language.decision.model.Softgoal;
import com.oracle.truffle.adaptable.module.TruffleAdaptableModule;
import com.oracle.truffle.api.Option;
import com.oracle.truffle.api.instrumentation.Instrumenter;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Registration;

import fr.gjouneau.truffle.HTML.HTMLAdaptationContext;
import fr.gjouneau.truffle.HTML.HTMLLanguage;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.Attribute;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.BASE;
import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags.IMG;


@Registration(id = ImageDegradation.ID, name = "ImageDegradation", version = "0.1", services = ImageDegradation.class)
public class ImageDegradation extends TruffleAdaptableModule<HTMLAdaptationContext, HTMLLanguage> {
	
	@Option(name = "", help = "Enable image degradation (default: false).", category = OptionCategory.USER, stability = OptionStability.STABLE)
    static final OptionKey<Boolean> ENABLED = new OptionKey<Boolean>(false);
	
	@Option(name = "folder", help = "Folder for local images", category = OptionCategory.USER, stability = OptionStability.STABLE)
    static final OptionKey<String> FOLDER = new OptionKey<String>("");

	@CompilationFinal private String url;
	
	private Resource impact;
	
	public static final String ID = "degrade-image";
	
	@Override
    protected OptionDescriptors getOptionDescriptors() {
        return new ImageDegradationOptionDescriptors();
    }

	@Override
	public void init(HTMLAdaptationContext adaptationContext, OptionValues options) {
		String folder = FOLDER.getValue(options);
        if (!ENABLED.getValue(options) || folder.equals("")) {
            return;
        }
        
        SourceSectionFilter imageFilter = SourceSectionFilter.newBuilder().tagIs(IMG.class).includeInternal(false).build();
        SourceSectionFilter attriFilter = SourceSectionFilter.newBuilder().tagIs(Attribute.class).includeInternal(false).build();
        SourceSectionFilter baseFilter = SourceSectionFilter.newBuilder().tagIs(BASE.class).includeInternal(false).build();
        SrcListener srcListener = new SrcListener(100*1000, folder);
        attachExecutionListener(imageFilter, new ImageListener(srcListener));
        attachExecutionListener(baseFilter, new BaseListener(srcListener));
        attachExecutionListener(attriFilter, srcListener);
        
        impact = new Resource("Image Degradation");
        impact.setValue(1.0);
	}

	@Override
	public void connectSoftGoal(Softgoal softgoal) {
		if (softgoal.ID.equals("Energy")) {
			softgoal.addContribution(impact, 0.4);
		}
		if (softgoal.ID.equals("Accuracy")) {
			softgoal.addContribution(impact, -0.8);
		}
	}

	@Override
	public void connectResource(Resource resource) {}

}
