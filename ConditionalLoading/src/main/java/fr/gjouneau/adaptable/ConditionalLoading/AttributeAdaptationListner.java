package fr.gjouneau.adaptable.ConditionalLoading;

import java.util.HashSet;
import java.util.Set;

import com.oracle.truffle.adaptable.module.AdaptationListener;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;
import com.oracle.truffle.api.nodes.Node;

import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeAttribute;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeBaseTag;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeEmptyTag;

public class AttributeAdaptationListner extends AdaptationListener {

	private final Set<String> attributesToChange;
	private boolean isOK;

	public AttributeAdaptationListner() {
		this.attributesToChange = new HashSet<String>();
		this.attributesToChange.add("src");
		this.attributesToChange.add("srcset");
		this.attributesToChange.add("href");
		this.attributesToChange.add("data");
		this.attributesToChange.add("poster");
		this.isOK = false;
	}
	
	public void setisOK() {
		this.isOK = true;
	}

	@Override
	public void before(Node instrumentedNode) {
		HTMLNodeAttribute attr = (HTMLNodeAttribute) instrumentedNode;
		if (isOK) {
			isOK = false;
			return;
		}
		
		if (attributesToChange.contains(attr.getName().toLowerCase())) {
			String value = attr.getValue();
			if (value == null || (value.startsWith("http"))) {
				bypass(attr);
			}
		}
	}

	@Override
	public void afterResult(Node instrumentedNode, Object result) {}

	@Override
	public void afterException(Node instrumentedNode, Throwable exception) {}

	@Override
	public Object afterBypass(Node instrumentedNode) {
		return "";
	}

}
