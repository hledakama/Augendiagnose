package de.eisfeldj.augendiagnosefx.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import de.eisfeldj.augendiagnosefx.util.DialogUtil;
import de.eisfeldj.augendiagnosefx.util.Logger;

/**
 * BaseController class for the menu.
 */
public class MenuController extends BaseController {
	/**
	 * The main menu bar.
	 */
	@FXML
	private MenuBar menuBar;

	/**
	 * The Menu entry "Close".
	 */
	@FXML
	private MenuItem menuClose;

	/**
	 * The Menu entry "Comment Pane".
	 */
	@FXML
	private CheckMenuItem menuCommentPane;

	@Override
	public final Parent getRoot() {
		return menuBar;
	}

	/**
	 * Get the main controller instance.
	 *
	 * @return The main controller instance.
	 */
	public static MenuController getInstance() {
		try {
			return getController(MenuController.class);
		}
		catch (TooManyControllersException | MissingControllerException e) {
			Logger.error("Could not find menu controller", e);
			return null;
		}
	}

	/**
	 * Handler for menu entry "Exit".
	 *
	 * @param event
	 *            The action event.
	 */
	@FXML
	protected final void exitApplication(final ActionEvent event) {
		Platform.exit();
	}

	/**
	 * Handler for menu entry "Preferences".
	 *
	 * @param event
	 *            The action event.
	 */
	@FXML
	public final void showPreferences(final ActionEvent event) {
		DialogUtil.displayPreferencesDialog();
	}

	/**
	 * Configure the "Close" menu entry.
	 *
	 * @param enabled
	 *            The enablement status.
	 * @param eventHandler
	 *            The event handler.
	 */
	public final void setMenuClose(final boolean enabled, final EventHandler<ActionEvent> eventHandler) {
		menuClose.setDisable(!enabled);
		menuClose.setOnAction(eventHandler);
	}

}
