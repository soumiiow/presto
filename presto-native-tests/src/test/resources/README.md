# Presto Native Tests

To create plugin shared libraries such as `libadd_function_dynamic` located in the `./plugin` directory, see [Function Plugin](https://prestodb.io/docs/current/presto-cpp/plugin.html) on how to use the Dynamic Library Loader.

The custom_add function implemented in `libadd_function_dynamic` follows the below format for the dynamic UDF creation and registration:

```
namespace facebook::velox::common::dynamicRegistry {

    template <typename T>
    struct DynamicFunction {
      VELOX_DEFINE_FUNCTION_TYPES(T);
      FOLLY_ALWAYS_INLINE bool call(
          int64_t& result,
          const arg_type<int64_t>& x1,
          const arg_type<int64_t>& x2) {
        result = x1 + x2;
        return true;
      }
    };
} // namespace facebook::velox::common::dynamicRegistry

extern "C" {
    void registerExtensions() {
      facebook::presto::registerPrestoFunction<
          facebook::velox::common::dynamicRegistry::DynamicFunction,
          int64_t,
          int64_t,
          int64_t>("custom_add");
    }
}
```