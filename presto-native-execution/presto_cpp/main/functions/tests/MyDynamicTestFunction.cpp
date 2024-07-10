/*
 * Copyright (c) Facebook, Inc. and its affiliates.
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

#include "velox/functions/Udf.h"

// This file defines a mock function that will be dynamically linked and
// registered. There are no restrictions as to how the function needs to be
// defined, but the library (.so) needs to provide a `void registry()` C
// function in the top-level namespace.
//
// (note the extern "C" directive to prevent the compiler from mangling the
// symbol name).

namespace facebook::presto::functions {

template <typename TExecParams>
struct Dynamic123Function {
  FOLLY_ALWAYS_INLINE bool call(int64_t& result) {
    result = 123;
    return true;
  }
};

} // namespace facebook::presto::functions

extern "C" {

void registry() {
  facebook::velox::registerFunction<
      facebook::presto::functions::Dynamic123Function,
      int64_t>({"dynamic_123"});
}
}
//need some kind of library to be able to get rid of registry dependency. make an abstraction somehow. get that linked dynamically into rpesto server and my library i built
//dlopen will always be dynamic and these dependencies will always be static
//need something that resolves later. maybe research how to abstract in such a way that you do not need to include all these internals. how does it map to the symbol
//problems when we load the same symbol from two sides. how would i rewrite or abstract this away that provides all we need. have this abstraction layer be dynamically linked to velox library.
//codegen happens at build time.
//udf api is only built for statically linking. not changed to dynamically linking. @jacob -- hes the assigned user in oss
// in a shared library, the extern flag + name of the thing. if gflags is a dll, define GFLAGS_DLL_DECLARE_FLAG __declspec(dllimport)
// GFLAGS_IS_A_DLL -> we would have to build gflags as a dll. looks to be that gflags is statically linked -DGFLAGS_IS_A_DLL=0
//cmake option verbose to see each command. see what it resolves to

//another issue is velox_memory_pool_mb something wrong with the dll thing. not intended to be dynamically linked. interestinggggg.

// 1. get the fyre machine. 2. get code in there and run 3. work on e2e