package fr.gjouneau.image.degradation;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;

public class ImageListener implements ExecutionEventListener {
	
	SrcListener listner;

	public ImageListener(SrcListener listner) {
		this.listner = listner;
	}

	public void onEnter(EventContext context, VirtualFrame frame) {
		listner.setIsImage(true);
	}

	public void onReturnValue(EventContext context, VirtualFrame frame, Object result) {
		listner.setIsImage(false);
	}

	public void onReturnExceptional(EventContext context, VirtualFrame frame, Throwable exception) {
		listner.setIsImage(false);
	}

}
