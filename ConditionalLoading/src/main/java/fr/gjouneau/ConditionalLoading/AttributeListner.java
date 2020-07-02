package fr.gjouneau.ConditionalLoading;

import java.util.HashSet;
import java.util.Set;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;

import fr.gjouneau.truffle.HTML.instrumentation.HTMLInstrumentationTags;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeAttribute;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeBaseTag;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeEmptyTag;

public class AttributeListner implements ExecutionEventListener {

	private final Set<String> attributesToChange;
	private boolean isOK;

	public AttributeListner() {
		this.attributesToChange = new HashSet<String>();
		this.attributesToChange.add("src");
		this.attributesToChange.add("srcset");
		this.attributesToChange.add("href");
		this.attributesToChange.add("data");
		this.attributesToChange.add("poster");
		this.isOK = false;
	}

	public void onEnter(EventContext context, VirtualFrame frame) {
		HTMLNodeAttribute attr = (HTMLNodeAttribute) context.getInstrumentedNode();
		
		if (isOK) {
			isOK = false;
			return;
		}
		
		if (attributesToChange.contains(attr.getName().toLowerCase())) {
			//System.out.println("DEBUG : "+attr.getName() + "="+ (attr.getValue()==null?"null":attr.getValue()));
			String value = attr.getValue();
			if (value == null || (value.startsWith("http"))) {
				//System.out.println("DEBUG : "+ value + " start with HTTP");
				throw context.createUnwind(attr);
			}
		}
	}

	public void onReturnValue(EventContext context, VirtualFrame frame, Object result) {
		// TODO Auto-generated method stub

	}

	public void onReturnExceptional(EventContext context, VirtualFrame frame, Throwable exception) {
		// TODO Auto-generated method stub

	}

	public Object onUnwind(EventContext context, VirtualFrame frame, Object info) {
		return "";
	}
	
	public void setisOK() {
		this.isOK = true;
	}

}
