package de.eisfeldj.augendiagnosefx.util;

import java.io.Serializable;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import de.eisfeldj.augendiagnosefx.Application;
import de.eisfeldj.augendiagnosefx.controller.DialogController;
import de.eisfeldj.augendiagnosefx.controller.MessageDialogController;

/**
 * Helper class to show standard dialogs.
 */
public abstract class DialogUtil {
	/**
	 * Create a basic dialog window.
	 *
	 * @param fxmlString
	 *            The FXML resource.
	 *
	 * @return The dialog controller.
	 */
	private static DialogController createDialog(final String fxmlString) {
		DialogController controller;
		controller = (DialogController) FXMLUtil.getRootFromFxml(fxmlString);

		Scene scene = new Scene(controller.getRoot());
		Stage dialog = new Stage();
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(Application.getStage());
		dialog.setScene(scene);
		controller.setStage(dialog);
		return controller;
	}

	/**
	 * Display an error and go back to the current activity.
	 *
	 * @param resource
	 *            the error message resource
	 * @param args
	 *            arguments for the error message
	 */
	public static void displayError(final String resource, final Object... args) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String message = String.format(ResourceUtil.getString(resource), args);
				Logger.warning("Dialog message: " + message);

				MessageDialogController controller = (MessageDialogController) createDialog("DialogError.fxml");

				controller.setHeading(ResourceUtil.getString(ResourceConstants.TITLE_DIALOG_ERROR));
				controller.setMessage(message);

				controller.getBtnBack().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent event) {
						controller.close();
					}
				});

				controller.show();
			}
		});
	}

	/**
	 * Display a confirmation message asking for cancel or ok.
	 *
	 * @param listener
	 *            The listener waiting for the response
	 * @param buttonResource
	 *            the display on the positive button
	 * @param messageResource
	 *            the confirmation message
	 * @param args
	 *            arguments for the confirmation message
	 */
	public static void displayConfirmationMessage(
			final ConfirmDialogListener listener, final String buttonResource,
			final String messageResource, final Object... args) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				String message = String.format(ResourceUtil.getString(messageResource), args);

				MessageDialogController controller = (MessageDialogController) createDialog("DialogConfirm.fxml");

				controller.setHeading(ResourceUtil.getString(ResourceConstants.TITLE_DIALOG_CONFIRMATION));
				controller.setMessage(message);

				if (buttonResource != null) {
					String buttonText = String.format(ResourceUtil.getString(buttonResource));
					controller.getBtnOk().setText(buttonText);
				}

				controller.getBtnCancel().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent event) {
						controller.close();
						listener.onDialogNegativeClick();
					}
				});
				controller.getBtnOk().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent event) {
						controller.close();
						listener.onDialogPositiveClick();
					}
				});

				controller.show();
			}
		});
	}

	/**
	 * Display the settings dialog.
	 */
	public static void displayPreferencesDialog() {
		createDialog("Preferences.fxml").show();
	}

	/**
	 * Display a progress dialog.
	 *
	 * @param messageResource
	 *            the displayed message
	 * @param args
	 *            arguments for the displayed message
	 * @return A reference to the dialog.
	 */
	public static ProgressDialog displayProgressDialog(final String messageResource, final Object... args) {
		String message = String.format(ResourceUtil.getString(messageResource), args);

		MessageDialogController controller = (MessageDialogController) createDialog("DialogProgress.fxml");

		controller.getStage().initStyle(StageStyle.UNDECORATED);
		controller.setHeading(ResourceUtil.getString(ResourceConstants.TITLE_DIALOG_PROGRESS));
		controller.setMessage(message);

		controller.show();

		return new ProgressDialog(controller);
	}

	/**
	 * A progress dialog.
	 */
	public static final class ProgressDialog {
		/**
		 * The controller.
		 */
		private MessageDialogController controller;

		/**
		 * Constructor setting the stage and the controller.
		 *
		 * @param controller
		 *            The controller.
		 */
		private ProgressDialog(final MessageDialogController controller) {
			this.controller = controller;
		}

		/**
		 * Getter for the controller.
		 *
		 * @return The controller.
		 */
		public MessageDialogController getController() {
			return controller;
		}

		/**
		 * Close the dialog.
		 */
		public void close() {
			controller.close();
		}
	}

	/**
	 * The activity that creates an instance of this dialog listFoldersFragment must implement this interface in order
	 * to receive event callbacks. Each method passes the DialogFragment in case the host needs to query it.
	 */
	public interface ConfirmDialogListener extends Serializable {
		/**
		 * Callback method for positive click from the confirmation dialog.
		 */
		void onDialogPositiveClick();

		/**
		 * Callback method for negative click from the confirmation dialog.
		 */
		void onDialogNegativeClick();
	}

}
