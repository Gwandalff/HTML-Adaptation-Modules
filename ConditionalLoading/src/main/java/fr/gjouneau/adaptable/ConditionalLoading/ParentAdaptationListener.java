package fr.gjouneau.adaptable.ConditionalLoading;

import com.oracle.truffle.adaptable.language.decision.model.Variable;
import com.oracle.truffle.adaptable.module.AdaptationListener;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;
import com.oracle.truffle.api.nodes.Node;

public class ParentAdaptationListener extends AdaptationListener {

	private final AttributeAdaptationListner attr;
	private final Variable observed;

	public ParentAdaptationListener(AttributeAdaptationListner attr) {
		this.attr = attr;
		this.observed = null;
	}
	
	public ParentAdaptationListener(AttributeAdaptationListner attr, Variable observed) {
		this.attr = attr;
		this.observed = observed;
	}

	@Override
	public void before(Node instrumentedNode) {
		if(observed == null) {
			attr.setisOK(true);
			return;
		}
		System.err.println(observed.ID + " : " + observed.value());
		attr.setisOK(observed.value()<=0);
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
