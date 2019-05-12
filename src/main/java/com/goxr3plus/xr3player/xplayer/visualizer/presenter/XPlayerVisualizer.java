/*
 * 
 */
package com.goxr3plus.xr3player.xplayer.visualizer.presenter;

import com.goxr3plus.xr3player.controllers.xplayer.XPlayerController;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;

/**
 * This class is updating the panel of the XPlayerUI.
 *
 * @author GOXR3PLUS
 */
public class XPlayerVisualizer extends Visualizer {

	/** The x player UI. */
	private XPlayerController xPlayerUI;

	/**
	 * Constructor.
	 *
	 * @param xPlayerUI the x player UI
	 */
	public XPlayerVisualizer(XPlayerController xPlayerUI, boolean addMouseListener) {

		this.xPlayerUI = xPlayerUI;
		this.getAnimationService().passXPlayer(xPlayerUI);

		setScopeColor(xPlayerUI.disc.getArcColor());
		if (addMouseListener)
			addMouseListener();
	}

	/**
	 * Add a mouse listener so every time primary mouse button is clicked the
	 * visualizer is changing.
	 */
	protected void addMouseListener() {

		setCursor(Cursor.HAND);
		setOnMouseReleased(m -> {
			// PRIMARY
			if (m.getButton() == MouseButton.PRIMARY) {
				displayMode.set((displayMode.get() + 1 > DISPLAYMODE_MAXIMUM) ? 0 : displayMode.get() + 1);
				// SECONDARY
			} else if (m.getButton() == MouseButton.SECONDARY)
				xPlayerUI.visualizerWindow.getVisualizerContextMenu().show(this, m.getScreenX(), m.getScreenY());
		});

	}

}
