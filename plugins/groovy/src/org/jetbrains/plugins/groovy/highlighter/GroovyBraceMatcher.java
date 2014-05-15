/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.plugins.groovy.highlighter;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.GroovyFileType;
import org.jetbrains.plugins.groovy.lang.groovydoc.lexer.GroovyDocTokenTypes;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;
import org.jetbrains.plugins.groovy.lang.lexer.TokenSets;

import static org.jetbrains.plugins.groovy.lang.groovydoc.lexer.GroovyDocTokenTypes.*;
import static org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes.*;
import static org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes.mSEMI;

/**
 * @author ilyas
 */
public class GroovyBraceMatcher implements PairedBraceMatcher {

  private static final BracePair[] PAIRS = {
    new BracePair(mLPAREN, mRPAREN, false),
    new BracePair(mLBRACK, mRBRACK, false),
    new BracePair(mLCURLY, mRCURLY, true),

    new BracePair(mGDOC_INLINE_TAG_START, mGDOC_INLINE_TAG_END, false),
    new BracePair(mGDOC_TAG_VALUE_LPAREN, mGDOC_TAG_VALUE_RPAREN, false),

    new BracePair(GroovyTokenTypes.mGSTRING_BEGIN, GroovyTokenTypes.mGSTRING_END, false),
    new BracePair(GroovyTokenTypes.mREGEX_BEGIN, mREGEX_END, false),
    new BracePair(mDOLLAR_SLASH_REGEX_BEGIN, mDOLLAR_SLASH_REGEX_END, false),
  };

  @Override
  public BracePair[] getPairs() {
    return PAIRS;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType braceType, @Nullable IElementType tokenType) {
    return tokenType == null
           || tokenType == TokenType.WHITE_SPACE
           || tokenType == mSEMI
           || tokenType == mCOMMA
           || tokenType == mRPAREN
           || tokenType == mRBRACK
           || tokenType == mRCURLY
           || tokenType == mGSTRING_BEGIN
           || tokenType == mREGEX_BEGIN
           || tokenType == mDOLLAR_SLASH_REGEX_BEGIN
           || TokenSets.COMMENT_SET.contains(tokenType)
           || tokenType.getLanguage() != GroovyFileType.GROOVY_LANGUAGE;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}