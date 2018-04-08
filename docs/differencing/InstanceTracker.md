# InstanceTracker
`InstanceTracker` aims to parse through L-SAP's result directories and track instances in a more easily mutable JSON format. This allows other modules to perform processing on the results using a more structured object instead of having to parse through a file system.

## Purpose
`DiffMapper` (see [documentation](./DiffMapper.md)) handles the overall process for mapping differences between kernels, but one crucial step discussed in the detailed overview is tracking instances. This represents enough of a distinction to require its own module. Enter __InstanceTracker__!

## Overview
`InstanceTracker` parses through the results directories returned by L-SAP and converts the result data into a JSON format that can more easily be manipulated for processing by `DiffMapper`. It does this by simply file I/O operations.

`InstanceTracker` requires a source directory on construction, which represents the location of the results to be tracked. Then, an output directory is then passed to `InstanceTracker`, which represents the location where the instance map will be saved.

First, `InstanceTracker` will check for an existing instance map in the given output directory. If you choose, `InstanceTracker` can override this map, or it can exit early since a mapping already exists. If no map exists or you choose to override an existing map, `InstanceTracker` will loop through the source directory and capture all instances it finds in the map. This map will then be written out to the output directory.

## Usage
`InstanceTracker` is packaged with `DiffMapper` and thus is only available for use as an API. See instructions on how to run the standalone `DiffMapper` jar [here](./DiffMapper.md).

### API
The InstanceTracker API is as follows:
#### Constructor
Method: `InstanceTracker(String sourceDirectory)`

Usage:
```
InstanceTracker it = new InstanceTracker("/path/to/results");
```

Parameters:

| Parameter       | Description                          |
| :-------------: | :----------------------------------: |
| sourceDirectory | The source directory of results data |

Description:

Constructs an instance of `InstanceTracker`

#### run
Method: `int run(String outputDirectory, boolean checkOutputOverride)`

Usage
```
it.run("/path/to/output/", false);
```
Parameters:

| Parameter           | Description                                                                                                |
| :-----------------: | :--------------------------------------------------------------------------------------------------------: |
| outputDirectory     | The directory where the instance map should be written                                                     |
| checkOutputOverride | Always write a new instance map instead of checking for an existing one and skipping the instance tracking |

Description:

Search through the source directory and parse instances that appear. Store them in an instance map that will be saved in the specified output directory. Optionally, check for an existing instance map and either override it, or exit the tracking early.
