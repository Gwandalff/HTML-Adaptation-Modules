package fr.gjouneau.adaptable.ConditionalLoading;

import com.oracle.truffle.adaptable.module.AdaptationListener;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;
import com.oracle.truffle.api.nodes.Node;

public class ParentAdaptationListener extends AdaptationListener {

	private final AttributeAdaptationListner attr;

	public ParentAdaptationListener(AttributeAdaptationListner attr) {
		this.attr = attr;
	}

	@Override
	public void before(Node instrumentedNode) {
		attr.setisOK(true);		
	}

	@Override
	public void afterResult(Node instrumentedNode, Object result) {
		attr.setisOK(false);
	}

	@Override
	public void afterException(Node instrumentedNode, Throwable exception) {
		attr.setisOK(false);
	}

	@Override
	public Object afterBypass(Node instrumentedNode, Object info) {
		return "";
	}


}
