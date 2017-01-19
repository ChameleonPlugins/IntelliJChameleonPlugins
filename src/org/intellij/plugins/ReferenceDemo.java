/*
package org.intellij.plugins;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.PsiReferenceProvider;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.resolve.reference.ReferenceType;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagValue;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Registers simple references to XML tags that look like this:
//
// // equals // toString // finalize //
public class ReferenceDemo implements ProjectComponent {
    public ReferenceDemo(final PsiManager psiManager, ReferenceProvidersRegistry registry) {
        registry.registerReferenceProvider(XmlTag.class, new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(PsiElement psiElement) {
                final XmlTag tag = (XmlTag) psiElement;
                if (tag.getLocalName().equals("method") && tag.getNamespace().equals("java")) {
                    final String name = tag.getParentTag().getAttributeValue("name");
                    final PsiClass psiClass = psiManager.findClass(name, tag.getResolveScope());
                    if (psiClass == null) return PsiReference.EMPTY_ARRAY;
                    final XmlTagValue tagValue = tag.getValue();
                    final PsiMethod[] psiMethods = psiClass.findMethodsByName(tagValue.getTrimmedText(), false);
                    return new PsiReference[]{new PsiReference() {
                        public PsiElement getElement() {
                            return tag;
                        }

                        public TextRange getRangeInElement() {
                            final int start = tag.getTextRange().getStartOffset();
                            return tag.getValue().getTextRange().shiftRight(-start);
                        }

                        @Nullable
                        public PsiElement resolve() {
                            return psiMethods.length >0 ? psiMethods[0] : null;
                        }

                        public String getCanonicalText() {
                            return tagValue.getTrimmedText();
                        }

                        public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
                            throw new UnsupportedOperationException();
                        }

                        public PsiElement bindToElement(PsiElement element) throws IncorrectOperationException {
                            throw new UnsupportedOperationException();
                        }

                        public boolean isReferenceTo(PsiElement element) {
                            return resolve() == element;
                        }

                        public Object[] getVariants() {
                            return psiClass.getMethods();
                        }

                        public boolean isSoft() {
                            return false;
                        }
                    }
                    };
                }
                return PsiReference.EMPTY_ARRAY;
            }

            @NotNull
            public PsiReference[] getReferencesByElement(PsiElement psiElement, ReferenceType referenceType) {
                return PsiReference.EMPTY_ARRAY;
            }

            @NotNull
            public PsiReference[] getReferencesByString(String string, PsiElement psiElement, ReferenceType referenceType, int i) {
                return PsiReference.EMPTY_ARRAY;
            }

            public void handleEmptyContext(PsiScopeProcessor psiScopeProcessor, PsiElement psiElement) {
            }
        });
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    @NonNls
    public String getComponentName() {
        return "ReferenceDemo";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }
}
*/