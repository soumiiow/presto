
add_library(presto_function_metadata OBJECT FunctionMetadata.cpp)
add_library(presto_dynamic_function_loader OBJECT DynamicLibraryLoader.cpp)

target_link_libraries(presto_function_metadata velox_function_registry)
if(PRESTO_ENABLE_TESTING)
  add_subdirectory(tests)
endif()

if(PRESTO_ENABLE_REMOTE_FUNCTIONS)
  add_library(presto_server_remote_function RemoteFunctionRegisterer.cpp)

  target_link_libraries(presto_server_remote_function velox_expression
                        velox_functions_remote ${FOLLY_WITH_DEPENDENCIES})
  target_link_libraries(presto_server_lib presto_server_remote_function)
endif()
