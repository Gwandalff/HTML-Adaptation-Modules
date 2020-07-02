package fr.gjouneau.ConditionalLoading;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;

public class ParentListener implements ExecutionEventListener {

	private final AttributeListner attr;

	public ParentListener(AttributeListner attr) {
		this.attr = attr;
	}

	public void onEnter(EventContext context, VirtualFrame frame) {
		attr.setisOK();
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

}
