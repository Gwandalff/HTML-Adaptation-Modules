// CheckStyle: start generated
package fr.gjouneau.adaptable.HTMLLoopPerforation;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Provider;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Registration;
import fr.gjouneau.adaptable.HTMLLoopPerforation.HTMLLoopPerforationInstrument;
import java.util.Arrays;
import java.util.Collection;

@GeneratedBy(HTMLLoopPerforationInstrument.class)
@Registration(id = "loop-perforation", name = "HTML Loop Perforation", version = "0.1")
public class HTMLLoopPerforationInstrumentProvider implements Provider {

    @Override
    public String getInstrumentClassName() {
        return "fr.gjouneau.adaptable.HTMLLoopPerforation.HTMLLoopPerforationInstrument";
    }

    @Override
    public TruffleInstrument create() {
        return new HTMLLoopPerforationInstrument();
    }

    @Override
    public Collection<String> getServicesClassNames() {
        return Arrays.asList("fr.gjouneau.adaptable.HTMLLoopPerforation.HTMLLoopPerforationInstrument");
    }

}
