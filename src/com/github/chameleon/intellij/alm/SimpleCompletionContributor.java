package com.github.chameleon.intellij.alm;

import com.github.chameleon.core.DocumentInfo;
import com.github.chameleon.core.OpenProposal;
import com.github.chameleon.intellij.alm.psi.SimpleTypes;
import com.github.chameleon.intellij.all.ChameleonIntelliJCompletionProposalComputer;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.xml.XmlElementType;
import com.intellij.util.ProcessingContext;

import com.github.chameleon.core.templates.TemplatesMaintenance;
import com.github.chameleon.core.ChameleonCompletionProposalComputer;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.authorisation.Authorisation;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.utils.CommonUtils;


public class SimpleCompletionContributor extends CompletionContributor {
    public static SimpleCompletionContributor us;
    protected static NGA nga;
    protected static Metadata metadata;
    private static final String DEFECT = "defects";
    private static final String NAME_VALUE = "Defect name";
    private static boolean initialized = false;
    Map<String , Object> completions = new HashMap<String, Object>();

    /**
     * If you need to set a proxy (especially if you are using hackathon.almoctane.com)
     *
     * See the README file for more information
     */
    static {
        // for local execution
        if (System.getProperty("should.set.proxy") == null) {
            System.setProperty("should.set.proxy", "true");
        }
        System.setProperty("http.proxyHost", "web-proxy.ftc.hpecorp.net");
        System.setProperty("http.proxyPort", "8088");
        System.setProperty("https.proxyHost", "web-proxy.ftc.hpecorp.net");
        System.setProperty("https.proxyPort", "8088");
    }

    private String cleanPath(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);   // e.g. /C:/path ==> C:/path (Windows)
        }
        return path;
    }

    private static void loadTemplates(SimpleCompletionContributor simpleCompletionContributor, Map<String, Object> completions, ChameleonCompletionProposalComputer computer) {

        String homeDir = System.getProperty("user.home");
        simpleCompletionContributor.loadTemplatesForLanguages(completions, homeDir+"/chameleon/templates/", computer);
    }

    private void loadTemplatesForLanguages(Map<String, Object> completions, String path, ChameleonCompletionProposalComputer computer) {
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isDirectory()){
                    String fileType = child.getName();
                    computer.setLanguage(fileType);
                    Map<String, Object> allCompletionsList = computer.getAllCompletionsAsMap();
                    completions.put(fileType, allCompletionsList);

                }
            }
        }
    }

    private void loadTemplatesForPath(Map<String , Object> completions, String path, String fileType) {
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isDirectory()){
                    loadTemplatesForPath(completions, child.getPath(), fileType);
                }
                else{
                    loadTemplate(child, completions, fileType);
                }
            }
        }
    }

    private void loadTemplate(File file, Map<String , Object> completions, String fileType) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(cleanPath(file.getPath()))));
            content = content.replaceAll("\r\n", "\n");
//            completions.get(fileType).put(file.getName().replace(".txt",""), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTemplateForResource(BufferedReader reader, String name, Map<String , String> completions) {
        try {
            StringBuffer buffer = new StringBuffer();
            for (String line; (line = reader.readLine()) != null;) {
                buffer.append(line);
            }
            String content = buffer.toString();
            content = content.replaceAll("\r\n", "\n");
            completions.put(name, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTemplates() {




        // Add commands
//        if(fileType.equals("PLAIN_TEXT"/*"vb"*/)){
//            completions.put("add defect", "");
//            completions.put("remove last defect", "");
//            completions.put("how many defects are there?", "");
//        }

    }

    public SimpleCompletionContributor() {
//        initAlm();
//        Project project = ProjectManager.getInstance().getOpenProjects()[0];
//        Document currentDoc = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
//        String fileType = FileDocumentManager.getInstance().getFile(currentDoc).getFileType().getName();
        us = this;
        initTemplates();


        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(),
                        new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {


                        List<LookupElementBuilder> builders = new ArrayList<LookupElementBuilder>();

                        // Get fileType...
                        Editor editor = parameters.getEditor();
                        Document currentDoc = parameters.getEditor().getDocument();
                        String fileType = FileDocumentManager.getInstance().getFile(currentDoc).getFileType().getName();

                        //if (!initialized)
                        {
                            ChameleonCompletionProposalComputer computer = new ChameleonIntelliJCompletionProposalComputer();
                            computer.setEditor(editor);
                            //        List<ICompletionProposal> allCompletionsList = computer.getAllCompletionsList();alComputer();
                            // Load IntelliJ template files
                            completions = new HashMap<String, Object>();
                            loadTemplates(us, completions, computer);
                            initialized = true;
                        }


                        Iterator it = ((Map)(completions.get(fileType))).entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            builders.add(LookupElementBuilder.create(pair.getKey().toString()));
                        }


                        final InsertHandler<LookupElement> _function = new InsertHandler<LookupElement>() {
                            @Override
                            public void handleInsert(final InsertionContext context, final LookupElement item) {
                                Editor _editor = context.getEditor();
                                _editor.getCaretModel().getCurrentCaret().selectLineAtCaret();
                                Document currentDoc = _editor.getDocument();
                                String fileType = FileDocumentManager.getInstance().getFile(currentDoc).getFileType().getName();
                                Iterator it = ((Map)(completions.get(fileType))).entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry pair = (Map.Entry) it.next();
                                    if (item.getLookupString().equals(pair.getKey().toString())) {
                                        if ( !pair.getValue().toString().isEmpty() ) {
                                            String fReplacementString = ((OpenProposal)pair.getValue()).fReplacementString;
                                            EditorModificationUtil.insertStringAtCaret(_editor, fReplacementString + "\n", true, true);
                                        }
                                    }
                                }
                                if ( item.getLookupString().equals("how many defects are there?")) {
                                    int defectsCount = getDefectsCount();
                                    EditorModificationUtil.insertStringAtCaret(_editor, defectsCount + "\n", true, true);
                                } else if ( item.getLookupString().equals("add defect")) {
                                    EditorModificationUtil.insertStringAtCaret(_editor, "added defect " + addDefect() + "\n", true, true);
                                }
                            }
                        };
                        for ( LookupElementBuilder builder : builders) {
                            resultSet.addElement(builder.withInsertHandler(_function));
                        }
                    }
                }
        );
    }

    private int getDefectsCount() {
        Query query = new Query().field("name", true).equal("lkdfhouifhwen Some text that should not be found").build();
        Collection<EntityModel> defects2 = nga.entityList(DEFECT).get().query(query).execute();
        return defects2.size();
    }

    private int addDefect() {
        Collection<EntityModel> defects = null;
        try {
            defects = createEntity(DEFECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityModel defect = defects.iterator().next();
        int id = CommonUtils.getIdFromEntityModel(defect);
        Set<FieldModel> fields = new HashSet<>();
        fields.add(new StringFieldModel("name", NAME_VALUE));
        defect.setValues(fields);
        nga.entityList(DEFECT).at(id).update().entity(defect).execute();
        return id;
    }

    public void initAlm() {
        HttpUtils.SetSystemKeepAlive(false);
        HttpUtils.SetSystemProxy();

/*
        // Ran into issues with config file jar and plug-in
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        String workspaceId = configuration.getString("sdk.workspaceId");
*/
        Authorisation authorisation = AuthorisationUtils.getAuthorisation();
        String url = "https://hackathon.almoctane.com";
        String sharedSpaceId = "1001";
        String workspaceId = "1002";
        nga = ContextUtils.getContextWorkspace(url, authorisation, sharedSpaceId, workspaceId);
        metadata = nga.metadata();
    }

    public static Collection<EntityModel> createEntity(String entityName) throws Exception {
        //Todo implement a full data generator using the metadata
        Collection<EntityModel> defects = DummyGenerator.generateEntityModel(nga, entityName);
        return nga.entityList(DEFECT).create().entities(defects).execute();
    }


}