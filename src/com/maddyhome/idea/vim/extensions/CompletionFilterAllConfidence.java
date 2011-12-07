package com.maddyhome.idea.vim.extensions;

import com.intellij.codeInsight.completion.AbstractExpectedTypeSkipper;
import com.intellij.codeInsight.completion.CompletionConfidence;
import com.intellij.codeInsight.completion.CompletionLocation;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.util.ThreeState;
import com.maddyhome.idea.vim.command.CommandState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple wrapper that handles skipping completion when repeating a command by checking the current state
 */
public class CompletionFilterAllConfidence extends CompletionConfidence {
    Map<PsiFile, Editor> recentEditors = new HashMap<PsiFile, Editor>();

    @NotNull
    @Override
    public ThreeState shouldFocusLookup(@NotNull CompletionParameters parameters) {
        return ThreeState.UNSURE;
    }

    @NotNull
    @Override
    public ThreeState shouldSkipAutopopup(@Nullable PsiElement element, @NotNull PsiFile file, int offset) {
        return CommandState.getInstance(getEditor(file)).getMode()
                == CommandState.MODE_REPEAT ? ThreeState.YES : ThreeState.UNSURE;
    }

    private Editor getEditor(PsiFile file) {
        if (!recentEditors.containsKey(file)) {
            recentEditors.put(file, PsiUtilBase.findEditor(file));
        }

        return recentEditors.get(file);
    }
}
