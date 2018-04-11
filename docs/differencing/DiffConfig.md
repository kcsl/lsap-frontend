# DiffConfig
`DiffConfig` represents a reusable set of configuration variables that allow a user to easily setup instances of `DiffMapper` and `DiffLinker`. The configuration variables represent the shared information between both projects. `DiffConfig` includes a method of parsing Command Line Interface (CLI) arguments to setup a config options. `DiffConfig` also includes the ability to setup a config object by passing in a JSON file. The format of the json file is specified below.

## Purpose
When performing differencing of L-SAP results, it becomes clear that there are several shared pieces of data between the `DiffMapper` and `DiffLinker` projects. This means that we would be potentially having to specify the same settings twice, which could lead to errors if they are not setup to be the same. The `DiffConfig` object intends to package the shared settings together and provide meaningful defaults so users only have to specify the settings they want to change.

## Overview
`DiffConfig` uses the [builder pattern](https://en.wikipedia.org/wiki/Builder_pattern) to construct a `DiffConfig` object. This allows the user to specify the changes to the configuration they want and leave all other settings default. Further, a user can clone an existing `DiffConfig` object and override only one or two settings, making the construction of `DiffConfig` objects very flexible.  `DiffConfig` supports the following options:

- `old_tag`: The name of the tag representing the base version of the Linux Kernel to start tracking differences (e.g. `"v3.18-rc1"`)
- `new_tag`: The name of the tag representing the target version of the Linux Kernel to end tracking differences (e.g. `"v3.19-rc1"`)
- `diff_test_dir`: The directory where the instance maps from [InstanceTracker](InstanceTracker.md) are stored and the final output is generated
- `kernel_dir`: The directory where the Linux Kernel source code lives.
- `result_dir`: The directory where L-SAP results are stored.
- `types`: An array of the types that should be tracked for differencing (currently not supported)[^1]

## Usage
As stated earlier `DiffConfig`'s can be built through a number of options. `DiffConfig` is available for use through an API and does not include a standalone jar, but instead is packaged within other projects. For example, see [DiffMapper](DiffMapper.md) for an overview on how `DiffConfig` is used within it.

### API
The `DiffConfig` API is as follows:

#### Builders
`DiffConfig` objects are not constructed, but rather built by first getting a `builder` object. There are several options for specifying a starting point for the builder. All builders must then call the `build()` to return the constructed `DiffConfig` object.

##### Default
Method: `DiffConfig.builder()`

Usage:
```Java
DiffConfig config = DiffConfig.builder().build();
```

Description:

Construct a `DiffConfig` object with default options. The following options are used by default:
- `old_tag`: `"v3.17-rc1"`
- `new_tag`: `"v3.18-rc1"`
- `diff_test_dir`: `"./diffmap/"` (where `.` is the current working directory)
- `kernel_dir`: `"./kernel/"` (where `.` is the current working directory)
- `result_dir`: `"./diffmap/prev_result/"` (where `.` is the current working directory)
- `types`: `["mutex", "spin"]`

##### From an existing builder
Method: `DiffConfig.builder(DiffConfigBuilder base)`

Usage:
```Java
DiffConfigBuilder baseBuilder = DiffConfig.builder();
DiffConfig base = builder.build();
DiffConfig changed = DiffConfig.builder(baseBuilder).setOldTag("v3.5").build();
```

Description:

Construct a `DiffConfig` object using the config options from a previous builder as a base.

##### From an file
Method: `DiffConfig.builder(String fileName)`

Usage:
```Java
DiffConfig config = DiffConfig.builder("config.json").build();
```

Description:

Constructs a `DiffConfig` object using the options specified in a JSON file. An example `config.json` file is provided:
```json
{
  "old_tag": "v3.17-rc1",
  "new_tag": "v3.18-rc1",
  "diff_test_dir": "diffmap/",
  "kernel_dir": "kernel/",
  "result_dir": "diffmap/prev_result/",
  "types": [
    "mutex",
    "spin"
  ]
}
```

##### From command line
Method: `DiffConfig.builder(String[] args)`

Usage:
```Java
public static void main(String[] args) {
  DiffConfig config = DiffConfig.builder(args).build();
  // Use config object
  ...
}
```

Description:

Constructs a `DiffConfig` object using the options provided from the command line. In this mode, there are command line options for specifying a file, or specifying individual settings. The following help method will be printed when the `-h` flag is used:
```
usage: java -jar <jarfile> [OPTIONS]
 -c,--config-file <file>   The JSON configuration file to use
 -D <property=value>       use value for given property
 -h,--help                 Display this help menu
 -n,--dry-run              See a summary of what the DiffMapper will do.
                           No action will be performed.
```
The `-D` flag allow for individual settings to be specified. They use the format `-D <property=value>`, where `property` is the key for the setting you want to change (see the `config.json` example above for key names).
> Note: Command Line options using the -D flag will override options from a config file if one is specified using the -c flag.

#### Setting your own options
After a builder has been constructed, you can chain one or more setters to specify an individual setting before calling the `build()` method. The same setter can appear multiple times, but only the value from the last time a setter was called will used.

##### Set Old Tag
Method: `setOldTag(String old_tag)`

Usage:
```Java
DiffConfig config = DiffConfig.builder().setOldTag("v4.13").build();
```

Description:

Set the `DiffConfig`'s old tag setting.

##### Set New Tag
Method: `setNewTag(String new_tag)`

Usage:
```Java
DiffConfig config = DiffConfig.builder().setNewTag("v4.15").build();
```

Description:

Set the `DiffConfig`'s new tag setting.

##### Set Diff Test Directory
Method: `setDiffTestDir(String diff_test_dir)`

Usage:
```Java
DiffConfig config = DiffConfig.builder().setDiffTestDir("/new/path/").build();
```

Description:

Set the `DiffConfig`'s diff test directory setting.

##### Set Kernel Directory
Method: `setKernelDir(String kernel_dir)`

Usage:
```Java
DiffConfig config = DiffConfig.builder().setKernelDir("/path/to/kernel/").build();
```

Description:

Set the `DiffConfig`'s kernel directory setting.

##### Set Result Directory
Method: `setResultDir(String result_dir)`

Usage:
```Java
DiffConfig config = DiffConfig.builder().setResultDir("/path/to/result/").build();
```

Description:

Set the `DiffConfig`'s result directory setting.

##### Set Types
Method: `setTypes(String[] types)`

Usage:
```Java
DiffConfig config = DiffConfig.builder().setTypes(new String[] {"mutex"}).build();
```

Description:

Set the `DiffConfig`'s types setting.

##### Example
```Java
DiffConfig config = DiffConfig.builder()
    .setOldTag("v4.13")
    .setNewTag("v4.15")
    .setDiffTestDir("/new/path/")
    .setKernelDir("/path/to/kernel/")
    .setResultDir("/path/to/result/")
    .setTypes(new String[] {"mutex"})
    .build();
```

[^1]: This will be supported in an upcoming update
