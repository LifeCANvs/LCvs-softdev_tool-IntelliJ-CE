/*
 * Copyright 2000-2017 JetBrains s.r.o.
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
package com.jetbrains.rest;

import com.intellij.lexer.Lexer;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.search.IndexPatternBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.jetbrains.rest.lexer.RestFlexLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author traff
 */
public class RestIndexPatternBuilder implements IndexPatternBuilder {
  public static final TokenSet COMMENTS = TokenSet.create(RestTokenTypes.COMMENT);

  @Nullable
  @Override
  public Lexer getIndexingLexer(@NotNull PsiFile file) {
    if (file instanceof RestFile) {
      return new RestFlexLexer();
    }
    return null;
  }

  @Nullable
  @Override
  public TokenSet getCommentTokenSet(@NotNull PsiFile file) {
    if (file instanceof RestFile) {
      return COMMENTS;
    }
    return null;
  }

  @Override
  public int getCommentStartDelta(IElementType tokenType) {
    return 0;
  }

  @Override
  public int getCommentEndDelta(IElementType tokenType) {
    return 0;
  }
}
