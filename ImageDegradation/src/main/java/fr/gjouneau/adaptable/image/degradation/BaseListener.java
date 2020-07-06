package fr.gjouneau.adaptable.image.degradation;

import com.oracle.truffle.adaptable.module.AdaptationListener;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.nodes.Node;

import fr.gjouneau.truffle.HTML.nodes.HTMLNodeAttribute;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeEmptyTag;

public class BaseListener extends AdaptationListener {
	
	SrcListener listner;

	public BaseListener(SrcListener listner) {
		this.listner = listner;
	}

	@Override
	public void before(Node instrumentedNode) {
		HTMLNodeEmptyTag base = (HTMLNodeEmptyTag) instrumentedNode;
		HTMLNodeAttribute[] attrs = base.getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			String attribute = attrs[i].execute(frame());
			String[] parts = attribute.split("=");
			if (parts[0].toLowerCase().equals("href")) {
				listner.setBASE(parts[1].substring(1, parts[1].length() - 1));
				break;
			}
		}
	}

	@Override
	public void afterResult(Node instrumentedNode, Object result) {}

	@Override
	public void afterException(Node instrumentedNode, Throwable exception) {}

	@Override
	public Object afterBypass(Node instrumentedNode, Object info) {
		return "";
	}

}
