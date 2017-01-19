package com.github.chameleon.intellij.alm.psi;

import com.intellij.psi.tree.IElementType;
import com.github.chameleon.intellij.alm.SimpleLanguage;
import org.jetbrains.annotations.*;

public class SimpleTokenType extends IElementType {
    public SimpleTokenType(@NotNull @NonNls String debugName) {
        super(debugName, SimpleLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "SimpleTokenType." + super.toString();
    }
}