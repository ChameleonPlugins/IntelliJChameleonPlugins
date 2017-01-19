package com.github.chameleon.intellij.all;

import com.github.chameleon.core.CompletionProposalBuilder;
import com.github.chameleon.core.OpenProposal;
import com.github.chameleon.intellij.all.IntelliJDocumentInfo;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;

import java.util.Collection;
import java.util.Map;

public class IntelliJCompletionProposalBuilder extends CompletionProposalBuilder {

	final String BUNDLE = "com.github.chameleon.eclipse.java";
	final static String TARGET_LANGUAGE = "Java";
	final String COMMENT_STARTER = "//// ";

	public IntelliJCompletionProposalBuilder(final String language,
											 String displayString,
											 String replacementString,
											 String additionalProposalInfo, final String message,
											 final String defaults,
											 final Map<String, Object> proposals, boolean testing,
											 String testingLine, int testingOffset, boolean console, IntelliJDocumentInfo docInfo, String typedString) {
		super(language, displayString, replacementString,
				additionalProposalInfo, message, defaults, proposals, testing,
				testingLine, testingOffset, console, docInfo, typedString);
	}

	protected String getCommentStarter() {
		return COMMENT_STARTER;
	}
	
	public OpenProposal createProposal() {
		preCreateProposal();
		org.eclipse.swt.graphics.Image image = null;
		if (!testing) {
			try {
//				image = new org.eclipse.swt.graphics.Image(getDisplay(),
//						locateFile(BUNDLE, "icons/Chameleon.gif").getPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ContextInformation javaContextInformation = 
				new ContextInformation(help,
				contextInformation.getInformationDisplayString());
		replacementOffset = getReplacementOffset();
		OpenProposal proposal =
                new OpenProposal(paddedReplacementString,
                		replacementOffset, replacementLength, 
                		paddedReplacementString.length(),
                        image, remainingString, javaContextInformation,
                        additionalProposalInfo);
        return proposal;
	}

	/**
	 * Get the current document information including: * Document *
	 * DocumentTextSelection
	 * 
	 * @throws CoreException
	 */
//	@Override
//	protected void getDocumentInfo() throws CoreException {
//		if (!testing) {
//			IEditorPart editorPart = PlatformUI.getWorkbench()
//					.getActiveWorkbenchWindow().getActivePage()
//					.getActiveEditor();
//			ITextEditor editor = (ITextEditor) editorPart
//					.getAdapter(ITextEditor.class);
//			IDocumentProvider provider = editor.getDocumentProvider();
//			document = provider.getDocument(editorPart.getEditorInput());
//			documentTextSelection = (ITextSelection) editorPart.getSite()
//					.getSelectionProvider().getSelection();
//
//			IEditorInput input = editorPart.getEditorInput();
//			if (input instanceof FileEditorInput) {
//				// IFile file = ((FileEditorInput) input).getFile();
//				// InputStream is = file.getContents();
//				// TODO get contents from InputStream
//			}
//
//			if (!console) {
//				globalOffset = documentTextSelection.getOffset();
//				globalLine = documentTextSelection.getText();
//				try {
//					lineNumber = document.getLineOfOffset(globalOffset);
//					lineLength = document.getLineLength(lineNumber);
//					globalLine = document.get(
//							document.getLineOffset(lineNumber), lineLength);
//				} catch (org.eclipse.jface.text.BadLocationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} else {
//				PydevConsole myConsole = (PydevConsole) PydevConsole
//						.getActiveScriptConsole();
//				ScriptConsoleHistory history = myConsole.getHistory();
//				editor = (ITextEditor) editorPart.getAdapter(ITextEditor.class);
//				documentTextSelection = (ITextSelection) editorPart.getSite()
//						.getSelectionProvider().getSelection();
//				document = history.getAsDoc();
//				// globalLine = history.get();
//				globalLine = history.getBufferLine();
//				String session = myConsole.getSession().toString();
//				lineNumber = countLines(session);
//				lineLength = globalLine.length();
//				globalOffset = session.length()
//						+ myConsole.getPrompt().toString().length()
//						+ history.getAsList().size() + 2;
//
//				// globalOffset += globalLine.length();
//			}
//		} else {
//			globalLine = testingLine;
//			globalOffset = testingOffset;
//		}
//	}

}
