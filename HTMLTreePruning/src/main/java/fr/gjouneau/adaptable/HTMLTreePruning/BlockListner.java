package fr.gjouneau.adaptable.HTMLTreePruning;

import com.oracle.truffle.adaptable.language.decision.model.Variable;
import com.oracle.truffle.adaptable.module.AdaptationListener;
import com.oracle.truffle.api.nodes.Node;

import fr.gjouneau.truffle.HTML.nodes.HTMLNodeBlockTag;

public class BlockListner extends AdaptationListener {
	
	private final int smallMedium = 7;
	private final int mediumBig = 20;
	
	private final int small = 2;
	private final int med = 4;
	private final int big = 8;
	
	private Variable smallListPR, listPR, bigListPR;
	
	public BlockListner(Variable smallListPR, Variable listPR, Variable bigListPR) {
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
			perforation = small;
		} 

		if (smallMedium <= length && length < mediumBig && listPR.value() > 0.5) {
			perforation = med;
		} 

		if(mediumBig <= length && bigListPR.value() > 0.5){
			perforation = big;
		}
		bypass(perforation);
	}

	@Override
	public void afterResult(Node instrumentedNode, Object result) {}

	@Override
	public void afterException(Node instrumentedNode, Throwable exception) {}

	@Override
	public Object afterBypass(Node instrumentedNode, Object info) {
		HTMLNodeBlockTag block = (HTMLNodeBlockTag) instrumentedNode;
		String out = "<"+block.getTag().getSimpleName();
		for (int i = 0; i < block.getAttributes().length; i++) {
			out += " "+block.getAttributes()[i].execute(frame());
		}
		out += ">";
		
		
		int perforation = (int) info;		
		for (int i = 0; i < block.children().length; i += perforation) {
			out += block.children()[i].execute(frame());
		}
		
		
		out += "</"+block.getTag().getSimpleName().toLowerCase() + ">";
		return out;
	}

}
