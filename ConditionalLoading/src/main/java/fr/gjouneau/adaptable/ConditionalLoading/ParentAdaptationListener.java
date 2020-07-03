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
		attr.setisOK();		
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
