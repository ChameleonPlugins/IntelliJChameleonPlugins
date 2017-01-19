package com.github.chameleon.intellij.all;

import com.intellij.openapi.editor.Editor;
import org.eclipse.core.runtime.CoreException;

import javax.swing.text.BadLocationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntelliJDocumentInfo extends com.github.chameleon.intellij.IntelliJDocumentInfo {

	protected Editor editor;

	public IntelliJDocumentInfo(boolean testing, String testingLine, int testingOffset) throws CoreException {
		super(testing, testingLine, testingOffset);
	}

	protected void setEditor(Editor editor) {
		this.editor = editor;
	}

	protected void getDocumentInfo() {
		setEditor(editor);
		super.getDocumentInfo(editor);
		if (!testing ) {
/*			
			if (console ) {
//				JavaConsole myConsole = (Console) Console
//						.getActiveScriptConsole();
//				ScriptConsoleHistory history = myConsole.getHistory();
				editor = (ITextEditor) editorPart.getAdapter(ITextEditor.class);
				documentTextSelection = (ITextSelection) editorPart.getSite()
						.getSelectionProvider().getSelection();
				document = history.getAsDoc();
				// globalLine = history.get();
				globalLine = history.getBufferLine();
				String session = myConsole.getSession().toString();
				lineNumber = countLines(session);
				lineLength = globalLine.length();
				globalOffset = session.length()
						+ myConsole.getPrompt().toString().length()
						+ history.getAsList().size() + 2;

				// globalOffset += globalLine.length();
			}
*/			
		} else {
			setGlobalLine(testingLine);
			globalOffset = testingOffset;
		}
	}

	public String getGlobalLine() {
		return globalLine;
	}

	public void setGlobalLine(String globalLine) {
		this.globalLine = globalLine;
	}
	
	protected Matcher matchLastToken(final String pattern)
			throws BadLocationException {
		final Pattern LINE_DATA_PATTERN = Pattern.compile(pattern);
		// final String data = getCurrentLine();
		final Matcher matcher = LINE_DATA_PATTERN.matcher(globalLine);
		matcher.matches();
		return matcher;
	}
	
}

