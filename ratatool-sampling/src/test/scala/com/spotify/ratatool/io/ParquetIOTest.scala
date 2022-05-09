/*
 * Copyright 2016 Spotify AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spotify.ratatool.io

import com.spotify.ratatool.samplers.ParquetTestData
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ParquetIOTest extends AnyFlatSpec with Matchers with BeforeAndAfterAll {
  private lazy val (typedOut, avroOut) = (
    ParquetTestData.createTempDir("typed"),
    ParquetTestData.createTempDir("avro"))

  override protected def beforeAll(): Unit = {
    ParquetTestData.writeTestData(avroPath = avroOut, typedPath = typedOut)
  }

  "ParquetIO" should "read file-based parquet-avro into GenericRecords" in {
    ParquetIO.readFromFile(avroOut + "/part-00000-of-00001.parquet")
      .toList should contain theSameElementsAs ParquetTestData.ParquetAvroData
  }

  it should "read file-based typed-parquet into GenericRecords" in {
    ParquetIO.readFromFile(typedOut + "/part-00000-of-00001.parquet")
      .toList should contain theSameElementsAs ParquetTestData.ParquetAvroData
  }

  it should "get Avro schema for a non-wildcard Parquet file" in {
    ParquetIO.getAvroSchemaFromFile(
      typedOut + "/part-00000-of-00001.parquet"
    ) shouldEqual ParquetTestData.avroSchema
  }

  it should "get Avro schema for a wildcard Parquet file glob" in {
    ParquetIO.getAvroSchemaFromFile(
      typedOut + "/part-*"
    ) shouldEqual ParquetTestData.avroSchema
  }
}
