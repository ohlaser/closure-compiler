/*
 * Copyright 2020 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.javascript.jscomp.testing;

import static com.google.common.truth.Truth.assertAbout;

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.IterableSubject;
import com.google.common.truth.Subject;
import com.google.javascript.jscomp.colors.Color;
import com.google.javascript.jscomp.colors.NativeColorId;
import javax.annotation.Nullable;

/** Subject for {@link Color} */
public final class ColorSubject extends Subject {

  @Nullable private final Color actual;

  private ColorSubject(FailureMetadata metadata, @Nullable Color actual) {
    super(metadata, actual);
    this.actual = actual;
  }

  public static Factory<ColorSubject, Color> colors() {
    return ColorSubject::new;
  }

  public static ColorSubject assertThat(@Nullable Color actual) {
    return assertAbout(colors()).that(actual);
  }

  private Color actualNonNull() {
    isNotNull();
    return actual;
  }

  // Custom assertions

  public void isNative(NativeColorId nativeColorId) {
    check("is").that(actualNonNull().is(nativeColorId)).isTrue();
  }

  public void isUnion() {
    check("isUnion()").that(actualNonNull().isUnion()).isTrue();
  }

  public void isPrimitive() {
    check("isPrimitive()").that(actualNonNull().isPrimitive()).isTrue();
  }

  public void isInvalidating() {
    check("isInvalidating").that(actualNonNull().isInvalidating()).isTrue();
  }

  public void isNotInvalidating() {
    check("isInvalidating").that(actualNonNull().isInvalidating()).isFalse();
  }

  public void propertiesKeepOriginalName() {
    check("propertiesKeepOriginalName").that(actualNonNull().propertiesKeepOriginalName()).isTrue();
  }

  public IterableSubject withOwnPropertiesSetThat() {
    return check("getOwnProperties()").that(actualNonNull().getOwnProperties());
  }

  public void mayHaveProperty(String property) {
    check("mayHaveProperty()").that(actualNonNull().mayHaveProperty(property)).isTrue();
  }

  public void doesNotHaveProperty(String property) {
    check("mayHaveProperty()").that(actualNonNull().mayHaveProperty(property)).isFalse();
  }

  public void hasAlternates(Color... alternates) {
    isUnion();
    check("getAlternates().containsExactly()")
        .that(actualNonNull().union())
        // cast to Object[] to suppress warning about varargs vs. non-varargs call confusion
        .containsExactly((Object[]) alternates);
  }

  public void hasDisambiguationSupertypes(Color... alternates) {
    check("getDirectSupertypes().containsExactly()")
        .that(actualNonNull().getDisambiguationSupertypes())
        // cast to Object[] to suppress warning about varargs vs. non-varargs call confusion
        .containsExactly((Object[]) alternates);
  }
}
