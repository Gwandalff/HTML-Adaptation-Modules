package fr.gjouneau.adaptable.image.degradation;

import com.oracle.truffle.adaptable.module.AdaptationListener;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;
import com.oracle.truffle.api.nodes.Node;

public class ImageListener extends AdaptationListener {
	
	SrcListener listner;

	public ImageListener(SrcListener listner) {
		this.listner = listner;
	}

	@Override
	public void before(Node instrumentedNode) {
		listner.setIsImage(true);
	}

	@Override
	public void afterResult(Node instrumentedNode, Object result) {
		listner.setIsImage(false);
	}

	@Override
	public void afterException(Node instrumentedNode, Throwable exception) {
		listner.setIsImage(false);
	}

	@Override
	public Object afterBypass(Node instrumentedNode, Object info) {
		return "";
	}

}
