package fr.gjouneau.adaptable.HTMLLoopPerforation;

import com.oracle.truffle.adaptable.language.decision.model.Variable;
import com.oracle.truffle.adaptable.module.AdaptationListener;
import com.oracle.truffle.api.nodes.Node;

import fr.gjouneau.truffle.HTML.nodes.HTMLNodeBlockTag;

public class ListListner extends AdaptationListener {
	
	private final int smallMedium = 7;
	private final int mediumBig = 20;
	
	private final int smallStep = 2;
	private final int step = 4;
	private final int bigStep = 8;
	
	private Variable smallListPR, listPR, bigListPR;
	
	public ListListner(Variable smallListPR, Variable listPR, Variable bigListPR) {
		this.smallListPR = smallListPR;
		this.listPR = listPR;
		this.bigListPR = bigListPR;
	}

	@Override
	public void before(Node instrumentedNode) {
		HTMLNodeBlockTag ul = (HTMLNodeBlockTag) instrumentedNode;
		
		Integer perforation = 1;
		int length = ul.children().length;
		if(length < smallMedium && smallListPR.value() > 0.5) {
			perforation = smallStep;
		} 

		if (smallMedium <= length && length < mediumBig && listPR.value() > 0.5) {
			perforation = step;
		} 

		if(mediumBig <= length && bigListPR.value() > 0.5){
			perforation = bigStep;
		}
		bypass(perforation);
	}

	@Override
	public void afterResult(Node instrumentedNode, Object result) {}

	@Override
	public void afterException(Node instrumentedNode, Throwable exception) {}

	@Override
	public Object afterBypass(Node instrumentedNode, Object info) {
		HTMLNodeBlockTag ul = (HTMLNodeBlockTag) instrumentedNode;
		String out = "<"+ul.getTag().getSimpleName();
		for (int i = 0; i < ul.getAttributes().length; i++) {
			out += " "+ul.getAttributes()[i].execute(frame());
		}
		out += ">";
		
		
		int perforation = (int) info;		
		for (int i = 0; i < ul.children().length; i += perforation) {
			out += ul.children()[i].execute(frame());
		}
		
		
		out += "</"+ul.getTag().getSimpleName().toLowerCase() + ">";
		return out;
	}

}
