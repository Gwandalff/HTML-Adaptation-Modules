// CheckStyle: start generated
package fr.gjouneau.adaptable.HTMLTreePruning;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Provider;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Registration;
import fr.gjouneau.adaptable.HTMLTreePruning.HTMLTreePruning;
import java.util.Arrays;
import java.util.Collection;

@GeneratedBy(HTMLTreePruning.class)
@Registration(id = "loop-perforation", name = "HTML Tree Pruning", version = "0.1")
public class HTMLTreePruningProvider implements Provider {

    @Override
    public String getInstrumentClassName() {
        return "fr.gjouneau.adaptable.HTMLTreePruning.HTMLTreePruning";
    }

    @Override
    public TruffleInstrument create() {
        return new HTMLTreePruning();
    }

    @Override
    public Collection<String> getServicesClassNames() {
        return Arrays.asList("fr.gjouneau.adaptable.HTMLTreePruning.HTMLTreePruning");
    }

}
