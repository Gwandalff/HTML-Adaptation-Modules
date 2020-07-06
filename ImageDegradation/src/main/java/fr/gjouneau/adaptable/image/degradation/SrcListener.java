package fr.gjouneau.adaptable.image.degradation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.oracle.truffle.adaptable.module.AdaptationListener;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventListener;
import com.oracle.truffle.api.nodes.Node;

import fr.gjouneau.truffle.HTML.nodes.HTMLNodeAttribute;
import fr.gjouneau.truffle.HTML.nodes.HTMLNodeEmptyTag;

public class SrcListener extends AdaptationListener {

	private Map<String, Boolean> imageSizes;
	private int threshold;
	private boolean isImage;
	private final String FOLDER;
	private String BASE;

	public SrcListener(int threshold, String folder) {
		this.imageSizes = new HashMap<String, Boolean>();
		this.threshold = threshold;
		this.isImage = false;
		this.BASE = "";
		this.FOLDER = folder + (folder.endsWith("/")?"":"/");
	}

	public void setIsImage(boolean isImage) {
		this.isImage = isImage;
	}
	
	public void setBASE(String base) {
		this.BASE = base;
	}
	
	@Override
	public void before(Node instrumentedNode) {
		HTMLNodeAttribute src = (HTMLNodeAttribute) instrumentedNode;
		if (isImage && src.getName().toLowerCase().equals("src")) {
			bypass(src);
		}
	}

	@Override
	public void afterResult(Node instrumentedNode, Object result) {}

	@Override
	public void afterException(Node instrumentedNode, Throwable exception) {}

	@Override
	public Object afterBypass(Node instrumentedNode, Object info) {
		HTMLNodeAttribute src = (HTMLNodeAttribute) instrumentedNode;

		String url = src.getValue();
		String newURL = FOLDER + url;

		if (!imageSizes.containsKey(url)) {
			try {
				saveImage(url, newURL);
				//newURL = "file://" + newURL;
				//newURL = newURL.replace("?", "%3F");
				newURL = getCDNAddress(newURL);
			} catch (Exception e) {
				newURL = url;
				imageSizes.remove(url);
				e.printStackTrace();
			}
		}

		if (imageSizes.get(url) != null && imageSizes.get(url).equals(true)) {
			//newURL = "file://" + getDegradedName(url);
			//newURL = newURL.replace("?", "%3F");
			newURL = getCDNAddress(getDegradedName(url));
		}

		if (src.getType() == null) return "src";
		
		switch (src.getType()) {
		case UNQUOTED:
			return "src=" + newURL;
		case SIMPLE_QUOTED:
			return "src='" + newURL + "'";
		case DOUBLE_QUOTED:
			return "src=\"" + newURL + "\"";
		default:
			return "src";
		}
	}

	private String getCDNAddress(String filePath) {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		String url = "";
		try {
			url = String.valueOf(engine.eval("encodeURIComponent('" + filePath + "')"));
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		return "http://127.0.0.1:8080/img/"+url;
	}

	private void saveImage(String imageUrl, String destinationFile) throws IOException {
		String resolvedURL = imageUrl;
		if (!imageUrl.startsWith("http"))
			resolvedURL = this.BASE + (imageUrl.startsWith("/")?"":"/") + imageUrl;
		URL url = new URL(resolvedURL);
		InputStream is = url.openStream();
		File img = new File(destinationFile);
		img.getParentFile().mkdirs();
		OutputStream os = new FileOutputStream(img);

		byte[] b = new byte[2048];
		int length;
		int bytes = 0;
		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
			bytes += length;
		}

		is.close();
		os.close();

		generateDegradation(imageUrl);

		imageSizes.put(imageUrl, bytes > threshold);
	}

	private String getDegradedName(String url) {
		return FOLDER + "/degraded/" + url + ".degraded.png";
	}

	private void generateDegradation(String url) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(FOLDER + url));
			if (img == null)
				return;

			img = scale(img, img.getWidth() / 2, img.getHeight() / 2);

			String filename = getDegradedName(url);
			File outputfile = new File(filename);
			outputfile.getParentFile().mkdirs();

			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
		}
	}

	private BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
		BufferedImage scaledImage = null;
		if (imageToScale != null) {
			scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
			Graphics2D graphics2D = scaledImage.createGraphics();
			graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
			graphics2D.dispose();
		}
		return scaledImage;
	}

}
