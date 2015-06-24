package de.eisfeldj.augendiagnosefx.controller;

import static de.eisfeldj.augendiagnosefx.util.PreferenceUtil.KEY_SHOW_COMMENT_PANE;
import static de.eisfeldj.augendiagnosefx.util.PreferenceUtil.KEY_SHOW_OVERLAY_PANE;
import static de.eisfeldj.augendiagnosefx.util.PreferenceUtil.KEY_SHOW_SPLIT_WINDOW;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import de.eisfeldj.augendiagnosefx.Application;
import de.eisfeldj.augendiagnosefx.util.DialogUtil;
import de.eisfeldj.augendiagnosefx.util.DialogUtil.ConfirmDialogListener;
import de.eisfeldj.augendiagnosefx.util.Logger;
import de.eisfeldj.augendiagnosefx.util.PreferenceUtil;
import de.eisfeldj.augendiagnosefx.util.ResourceConstants;
import de.eisfeldj.augendiagnosefx.util.ResourceUtil;
import de.eisfeldj.augendiagnosefx.util.SystemUtil;
import de.eisfeldj.augendiagnosefx.util.VersioningUtil;

/**
 * BaseController class for the menu.
 */
public class MenuController extends BaseController implements Initializable {
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
	 * The Menu entry "Overlay Pane".
	 */
	@FXML
	private CheckMenuItem menuOverlayPane;

	public final CheckMenuItem getMenuOverlayPane() {
		return menuOverlayPane;
	}

	/**
	 * The Menu entry "Comment Pane".
	 */
	@FXML
	private CheckMenuItem menuCommentPane;

	public final CheckMenuItem getMenuCommentPane() {
		return menuCommentPane;
	}

	/**
	 * The Menu entry "Split window".
	 */
	@FXML
	private CheckMenuItem menuSplitWindow;

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
			throw new RuntimeException(e);
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
		Application.exitAfterConfirmation();
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
	 * Handler for menu entry "Comment pane".
	 *
	 * @param event
	 *            The action event.
	 */
	@FXML
	public final void toggleCommentPane(final ActionEvent event) {
		PreferenceUtil.setPreference(KEY_SHOW_COMMENT_PANE, menuCommentPane.isSelected());
		for (DisplayImageController controller : getControllers(DisplayImageController.class)) {
			controller.showCommentPane(menuCommentPane.isSelected());
		}
	}

	/**
	 * Handler for menu entry "Overlay pane".
	 *
	 * @param event
	 *            The action event.
	 */
	@FXML
	public final void toggleOverlayPane(final ActionEvent event) {
		PreferenceUtil.setPreference(KEY_SHOW_OVERLAY_PANE, menuOverlayPane.isSelected());
		for (DisplayImageController controller : getControllers(DisplayImageController.class)) {
			controller.showOverlayPane(menuOverlayPane.isSelected());
		}
	}

	/**
	 * Handler for menu entry "Split window".
	 *
	 * @param event
	 *            The action event.
	 */
	@FXML
	public final void toggleSplitWindow(final ActionEvent event) {
		if (!menuSplitWindow.isSelected() && MainController.hasDirtyBaseController()) {
			ConfirmDialogListener listener = new ConfirmDialogListener() {
				@Override
				public void onDialogPositiveClick() {
					PreferenceUtil.setPreference(KEY_SHOW_SPLIT_WINDOW, menuSplitWindow.isSelected());

					if (MainController.getInstance().hasClosablePage()) {
						MainController.getInstance().setSplitPane(menuSplitWindow.isSelected());
					}
				}

				@Override
				public void onDialogNegativeClick() {
					// revert
					menuSplitWindow.setSelected(!menuSplitWindow.isSelected());
				}
			};
			DialogUtil.displayConfirmationMessage(listener, ResourceConstants.BUTTON_OK,
					ResourceConstants.MESSAGE_CONFIRM_EXIT_UNSAVED);
		}
		else {
			PreferenceUtil.setPreference(KEY_SHOW_SPLIT_WINDOW, menuSplitWindow.isSelected());
			if (MainController.getInstance().hasClosablePage()) {
				MainController.getInstance().setSplitPane(menuSplitWindow.isSelected());
			}
		}
	}

	/**
	 * Handler for menu entry "Online Help".
	 *
	 * @param event
	 *            The action event.
	 */
	@FXML
	public final void showHelp(final ActionEvent event) {
		Application.getApplicationHostServices().showDocument(
				ResourceUtil.getString(ResourceConstants.DOCUMENTATION_URL));
	}

	/**
	 * Handler for menu entry "Check for updates".
	 *
	 * @param event
	 *            The action event.
	 */
	@FXML
	public final void checkUpdates(final ActionEvent event) {
		VersioningUtil.checkForNewerVersion(true);
	}

	/**
	 * Handler for menu entry "Uninstall".
	 *
	 * @param event
	 *            The action event.
	 */
	@FXML
	public final void uninstall(final ActionEvent event) {
		SystemUtil.uninstallApplication();
	}

	/**
	 * Handler for menu entry "About".
	 *
	 * @param event
	 *            The action event.
	 */
	@FXML
	public final void displayAboutMessage(final ActionEvent event) {
		DialogUtil.displayInfo(ResourceConstants.MESSAGE_INFO_APP_ABOUT,
				VersioningUtil.CURRENT_VERSION.getVersionString());
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

	@Override
	public final void initialize(final URL location, final ResourceBundle resources) {
		menuCommentPane.setSelected(PreferenceUtil.getPreferenceBoolean(KEY_SHOW_COMMENT_PANE));
		menuOverlayPane.setSelected(PreferenceUtil.getPreferenceBoolean(KEY_SHOW_OVERLAY_PANE));
		menuSplitWindow.setSelected(PreferenceUtil.getPreferenceBoolean(KEY_SHOW_SPLIT_WINDOW));
	}

}
