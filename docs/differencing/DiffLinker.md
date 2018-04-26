# DiffLinker
The DiffMapper project aims link instances between two version of the Linux Kernel. This is a companion project with L-SAP and only supports L-SAP metadata at this time. It should also be used in tandem with [DiffMapper](./DiffMapper.md).

## Purpose
L-SAP completes a run and provides results for a specific version of the Linux Kernel. If DiffMapper was used to insert metadata in the kernel source code before L-SAP was run. The results from L-SAP include the source code locations for all instances that appeared in the new version of the kernel. So there must be a tool that can search through the source code of the kernel and reclaim the inserted metadata from a previous version. This will allow us to link instances between versions and further analyze these links for interesting cases. Enter __DiffLinker__!

## Overview
DiffLinker leverages the metadata inserted by DiffMapper to detect and link instances. The results from L-SAP include the source code location for all instances that were found. DiffLinker simply searches through these locations for metadata nearby these locations. If the metadata exists, a link is created using the metadata (from a previous version) and the result data (from the current version).

The basic steps of DiffMapper are as follows:
1. Generate New Instance Map
2. Analyze the Kernel source code for metadata
3. Generate Reports for Linked Instances for "Interesting" cases

At this point, the reports for linked instances are generated and this data can be analyzed further by hand, or passed to the Website to display new information.

## Usage
`DiffLinker` is available as a standalone jar, or as a maven package to import into your project. Both usages will be discussed, however they both require knowledge of the `DiffConfig` object.
> For more information on this, checkout the `DiffConfig` docs (Need link for this!)

### Standalone
The standalone jar can be run as follows:
```
usage: java -jar <jarfile> [OPTIONS]
 -c,--config-file <file>   The JSON configuration file to use
 -D <property=value>       use value for given property
 -h,--help                 Display this help menu
 -n,--dry-run              See a summary of what the DiffMapper will do.
                           No action will be performed.
```
The config file refers to a JSON representation of a `DiffConfig` object. This allows a jar at the same location to be invoked on multiple projects without having to move the jar. Additionally, the `-D` property allows a consumer to override both default values and config file values. The order of priority for values is: `CLI > .json file > defaults`. In other words, CLI values are used (if provided), then .json file values are used (if provided), then default values will be used. An example usage of the `-D` property is provided below:
```
java -jar DiffLinker.jar -D old_tag=3.17-rc1 -D new_tag=3.18-rc1 -D types.1=spin -D types.2=mutex
```
> See the `DiffConfig` docs (need link for this!) for a listing of keys. Invalid keys will be ignored.

### API
The DiffMapper API is as follows:
#### Constructor
Method: `DiffLinker(DiffConfig config, boolean allowPrintStatements, int lineSearchThreshold)`

Usage:
```Java
DiffConfig config = DiffConfig.builder().build();
DiffLinker dl = new DiffLinker(config, true, 10);
```

Parameters:

| Parameter            | Description                                      |
| :------------------: | :----------------------------------------------: |
| config               | A valid `DiffConfig` object                      |
| allowPrintStatements | A boolean value to enable/disable console output |
| lineSearchThreshold  | The number of lines to search above the source code location to search for metadata |

Description:

Constructs an instance of `DiffLinker`

#### run
Method: `int run(String inputMapFilename) throws JSONException, IOException`

Usage
```Java
dl.run("newInstanceMap.json");
```

Parameters:

| Parameter        | Description                                        |
| :--------------: | :------------------------------------------------: |
| inputMapFilename | The file location of the intermediate instance map |

Description:

Takes an input JSON file of tracked instances and uses this to search the kernel source code for metadata. If metadata exists (within the line search threshold), a link is created and saved to a new `diffInstanceMap.json`. Further, links that include instances with a different status between versions are marked as "Interesting" and are saved to both a `.json` file and `.csv` file for further analysis.

## Detailed Overview
The following sections provide a detailed overview of each step. The following data is stored in a Config object and is required by DiffMapper to complete successfully.

- Kernel Location - The location of the kernel that was used by L-SAP to perform a run. This kernel is required because it contains the metadata links from the DiffMapper.
- L-SAP Result Location - DiffLinker uses the results from L-SAP to read.
- DiffLinker Workspace - In order to support running DiffLinker on various directories from a single location, DiffLinker a location where it can store the intermediate tracked instances. See below for more information on this data.
- Old Version Tag - Since DiffLinker uses the Linux Kernel's git history, the tag of the old version needs to be known in order to correctly label linked instances
- New Version Tag - Since DiffLinker uses the Linux Kernel's git history, the tag of the new version needs to be known in order to correctly label linked instances
- Types of Locks - This option allows a consumer to specify what type of locks they want to map. This makes DiffLinker run faster, although it is recommended to add all types of locks to the linking.

### Generate New Instance Map

Since the DiffMapper has been run previously, we have a map of all of the tracked instances in the old version of the Linux Kernel. Similarly, we need to generate an instance map for the new version of the Kernel. This is done in a similar way, except we look at the most recent results of L-SAP rather than the previous results.

### Analyze the Kernel Source Code for Metadata

Once we have created our map of instances from the new version of the Kernel, we can use this map to reveal the source code locations of the instances. Since we have the kernel that includes the metadata, we can look through the source code for instance metadata from the previous version. If the metadata exists around the source code location for an instance, we know we have a link from the previous version to the current version. If no instance metadata exists, there are a couple of reasons: 1) This instance is a new instance and did not exists in the previous version; 2) The instance metadata was not able to tracked to the new version. We determine the separation of these two instances by looking at the map of instances in the old version. We can compare the raw metadata of old instances in a single file to the new instances. Since the metadata tags are inserted for ~85% on average. This search is only done on single files sparingly, thus performance is not degraded.

### Generate Reports for Linked Instances and “Interesting” Cases

Once we have analyzed the source code and linked instances. We generate a JSON formatted report that represents the new and old instances that have been linked. Instances that do not link back to old instances are formatted separately.  After this report is generated, we perform another analysis on this set of data. As a starting heuristic, we look at the status of linked instances. If an instance exists that has different pairing statuses between versions, this case is marked as “interesting” and a separate report is generated to contain these cases. Such a report is a valuable tool to tell researchers an interesting subset of instances to focus on.
