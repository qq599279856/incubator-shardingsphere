/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sharding.rewrite.token.generator.impl.keygen;

import com.google.common.base.Preconditions;
import lombok.Setter;
import org.apache.shardingsphere.sharding.rewrite.token.pojo.impl.GeneratedKeyAssignmentToken;
import org.apache.shardingsphere.sharding.rewrite.token.pojo.impl.LiteralGeneratedKeyAssignmentToken;
import org.apache.shardingsphere.sharding.rewrite.token.pojo.impl.ParameterMarkerGeneratedKeyAssignmentToken;
import org.apache.shardingsphere.sharding.route.engine.keygen.GeneratedKey;
import org.apache.shardingsphere.sql.parser.binder.statement.dml.InsertStatementContext;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement;
import org.apache.shardingsphere.underlying.rewrite.sql.token.generator.aware.ParametersAware;

import java.util.List;

/**
 * Generated key assignment token generator.
 */
@Setter
public final class GeneratedKeyAssignmentTokenGenerator extends BaseGeneratedKeyTokenGenerator implements ParametersAware {
    
    private List<Object> parameters;
    
    @Override
    protected boolean isGenerateSQLToken(final InsertStatement insertStatement) {
        return insertStatement.getSetAssignment().isPresent();
    }
    
    @Override
    protected GeneratedKeyAssignmentToken generateSQLToken(final InsertStatementContext insertStatementContext, final GeneratedKey generatedKey) {
        Preconditions.checkState(insertStatementContext.getSqlStatement().getSetAssignment().isPresent());
        int startIndex = insertStatementContext.getSqlStatement().getSetAssignment().get().getStopIndex() + 1;
        return parameters.isEmpty() ? new LiteralGeneratedKeyAssignmentToken(startIndex, generatedKey.getColumnName(), generatedKey.getGeneratedValues().getLast())
                : new ParameterMarkerGeneratedKeyAssignmentToken(startIndex, generatedKey.getColumnName());
    }
}
