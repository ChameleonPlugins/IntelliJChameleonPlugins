package com.github.chameleon.intellij.alm.psi;

import com.intellij.psi.tree.IElementType;
import com.github.chameleon.intellij.alm.SimpleLanguage;
import org.jetbrains.annotations.*;

public class SimpleElementType extends IElementType {
    public SimpleElementType(@NotNull @NonNls String debugName) {
        super(debugName, SimpleLanguage.INSTANCE);
    }
}