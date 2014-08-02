package de.eisfeldj.augendiagnose.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import de.eisfeldj.augendiagnose.R;
import de.eisfeldj.augendiagnose.activities.DisplayImageActivity;
import de.eisfeldj.augendiagnose.util.KeyboardUtil;

/**
 * Fragment to add the comment of a picture.
 */
public class EditCommentFragment extends Fragment {

	/**
	 * The resource key for the initial text.
	 */
	private static final String STRING_TEXT = "de.eisfeldj.augendiagnose.TEXT";

	/**
	 * The EditText displaying the comment.
	 */
	private EditText editText;

	/**
	 * The comment text.
	 */
	private String text;

	/**
	 * Initialize the EditCommentFragment with the text.
	 *
	 * @param initialText
	 *            The initial text of the comment.
	 */
	public final void setParameters(final String initialText) {
		Bundle args = new Bundle();
		args.putString(STRING_TEXT, initialText);

		setArguments(args);
	}

	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		text = getArguments().getString(STRING_TEXT);
	}

	@Override
	public final View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_edit_comment, container, false);
	}

	@Override
	public final void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		editText = (EditText) getView().findViewById(R.id.input_edit_comment);
		editText.setText(text);

		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(final View v, final boolean hasFocus) {
				if (hasFocus) {
					getActivity().getWindow().setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			}
		});

		final Button buttonCancel = (Button) getView().findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				DisplayImageActivity activity = (DisplayImageActivity) getActivity();
				activity.processUpdatedComment(editText.getText().toString(), false);
			}
		});

		final Button buttonClear = (Button) getView().findViewById(R.id.buttonClear);
		buttonClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				editText.setText("");
			}
		});

		final Button buttonOk = (Button) getView().findViewById(R.id.buttonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				DisplayImageActivity activity = (DisplayImageActivity) getActivity();
				activity.processUpdatedComment(editText.getText().toString(), true);
			}
		});

	}

	/**
	 * Hide the soft keyboard triggered from this listFoldersFragment.
	 */
	public final void hideKeyboard() {
		KeyboardUtil.hideKeyboard(getActivity(), editText);
	}

}
