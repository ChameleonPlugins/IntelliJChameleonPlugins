package com.github.chameleon.intellij;
/*
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
*/
import com.github.chameleon.core.DocumentInfo;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import org.eclipse.core.runtime.CoreException;

import javax.swing.text.BadLocationException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntelliJDocumentInfo extends DocumentInfo{

	public String typedString;
	protected Document document;
//	protected ITextSelection documentTextSelection;
	protected boolean console = false;
	public int globalOffset;
	protected String globalLine;
	protected int lineNumber;
	protected int lineLength;
	protected String testingLine = "Testing Line";
	protected int testingOffset = -1;

	protected boolean testing = false;
	protected Editor editor;
//	protected IEditorPart editorPart;

	public IntelliJDocumentInfo(boolean testing, String testingLine, int testingOffset) throws CoreException {
		this.testing = testing;
		this.testingLine = testingLine;
		this.testingOffset = testingOffset;
	}

	protected void setEditor(Editor editor){
	    this.editor = editor;
    }
	public void getDocumentInfo(Editor editor) {
		setEditor(editor);
		if (!testing ) {
//			IEditorPart editorPart = PlatformUI.getWorkbench()
//					.getActiveWorkbenchWindow().getActivePage()
//					.getActiveEditor();
//			ITextEditor editor = (ITextEditor) editorPart
//					.getAdapter(ITextEditor.class);
			//IDocumentProvider provider = editor.getDocumentProvider();
			// Get fileType...
			document = editor.getDocument();
			String fileType = FileDocumentManager.getInstance().getFile(document).getFileType().getName();

//			document = provider.getDocument(editorPart.getEditorInput());
//			documentTextSelection = editorPart.getSite()
//					.getSelectionProvider().getSelection();

//			IEditorInput input = editorPart.getEditorInput();
//			if (input instanceof FileEditorInput)
			{
				// IFile file = ((FileEditorInput) input).getFile();
				// InputStream is = file.getContents();
				// TODO get contents from InputStream
			}

			if (!console ) {
				globalOffset = editor.getCaretModel().getCurrentCaret().getOffset();
//				globalOffset = documentTextSelection.getOffset();


//                editor.getCaretModel().getCurrentCaret().selectLineAtCaret();
//                EditorModificationUtil.insertStringAtCaret(_editor, pair.getValue() + "\n", true, true);


                lineNumber = document.getLineNumber(globalOffset);
                int endOffset = document.getLineEndOffset(lineNumber);
                int startOffset = document.getLineStartOffset(lineNumber);
                int lineLength = endOffset - startOffset;
                String line = document.getText(new TextRange(startOffset, endOffset));
                setGlobalLine(line);
//					setGlobalLine(document.get(
//							document.getLineOffset(lineNumber), lineLength));
			}
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

	public String getTypedString() {
		String WITH_SPACES_PATTERN = ".*\\s*([^\\p{Alnum}]?)(\\p{Alnum}*)\\s*$";
		Matcher matcher;
		try {
			matcher = matchLastToken(WITH_SPACES_PATTERN);
			typedString = matcher.group(0).replace("\n", "").replace("\r", "");
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return typedString;
	}

}

