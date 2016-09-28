/*
 * Copyright 2016 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.ufpr.inf.gres.irace.enums;

import java.util.Objects;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public enum MutationOperatorType {

    BIT_FLIP_MUTATION("BitFlipMutation"),    
    NULLMUTATION("NullMutation");

    /**
     *
     * @param value
     * @return
     */
    public static MutationOperatorType getEnum(String value) {
        for (MutationOperatorType v : values()) {
            if (Objects.equals(v.getValue(), value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }

    private final String value;

    MutationOperatorType(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
