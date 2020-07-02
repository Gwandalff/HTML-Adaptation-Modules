package fr.gjouneau.HTMLLoopPerforation;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;

import fr.gjouneau.truffle.HTML.nodes.HTMLNodeBase;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeBlockTag;

public class ListListner implements ExecutionEventListener {
	
	private int step = 3;
	
	public ListListner() {
	}

	public void onEnter(EventContext context, VirtualFrame frame) {
		HTMLNodeBlockTag ul = (HTMLNodeBlockTag) context.getInstrumentedNode();
		HTMLNodeBase[] children =  ul.children();
		if (children.length > 5) {
			throw context.createUnwind(ul);
		}
	}

	public void onReturnValue(EventContext context, VirtualFrame frame, Object result) {
		// TODO Auto-generated method stub

	}

	public void onReturnExceptional(EventContext context, VirtualFrame frame, Throwable exception) {
		// TODO Auto-generated method stub

	}

	public Object onUnwind(EventContext context, VirtualFrame frame, Object info) {
		HTMLNodeBlockTag ul = (HTMLNodeBlockTag) info;
		String out = "<"+ul.getTag().getSimpleName();
		for (int i = 0; i < ul.getAttributes().length; i++) {
			out += " "+ul.getAttributes()[i].execute(frame);
		}
		out += ">";
		
		for (int i = 0; i < ul.children().length; i += step) {
			out += ul.children()[i].execute(frame);
		}
		
		out += "</"+ul.getTag().getSimpleName().toLowerCase() + ">";
		return out;
	}

}
