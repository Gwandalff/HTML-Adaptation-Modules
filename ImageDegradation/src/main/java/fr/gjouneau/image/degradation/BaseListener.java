package fr.gjouneau.image.degradation;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;

import fr.gjouneau.truffle.HTML.nodes.HTMLNodeAttribute;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeEmptyTag;

public class BaseListener implements ExecutionEventListener {
	
	SrcListener listner;

	public BaseListener(SrcListener listner) {
		this.listner = listner;
	}

	public void onEnter(EventContext context, VirtualFrame frame) {
		System.err.println("THERE IS A BASE !!!!!!");
		HTMLNodeEmptyTag base = (HTMLNodeEmptyTag) context.getInstrumentedNode();
		HTMLNodeAttribute[] attrs = base.getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			System.err.println("          : "+attrs[i].execute(frame));
			System.err.println("ATTRIBUTE : "+attrs[i].getName()+" => "+attrs[i].getValue());
			if (attrs[i].getName().toLowerCase().equals("href")) {
				listner.setBASE(attrs[i].getValue());
				break;
			}
		}
		
	}

	public void onReturnValue(EventContext context, VirtualFrame frame, Object result) {
	}

	public void onReturnExceptional(EventContext context, VirtualFrame frame, Throwable exception) {
	}

}
