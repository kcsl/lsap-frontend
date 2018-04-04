# DiffMapper
The DiffMapper project aims map metadata between two version of the Linux Kernel. This is a companion project with L-SAP and only supports L-SAP metadata at this time.

## Purpose
L-SAP completes a run and provides results for a specific version of the Linux Kernel. However, L-SAP does not support generating a report of the differences between two versions of the kernel. This is due to number of locking instances in the kernel and the potential changes that could occur from version to version of the kernel. Between two versions, we can't guarantee that the same number of locking instances will appear in each file. Further, we can't guarantee the relative order of locking instances in a file. Because of this, we need some way to map result data that will satisfy the following constraints:
1. If a locking instance changes locations in a newer source file, there is still a link back to the old location in previous versions.
2. If a locking instance has its variable name changed in a newer source file, there is still a link back to the old location in previous versions

Because of these constraints, we can't simply look for the same named instance in a file or look at the same location of a file to determine whether or not the same instance exists between versions. A deeper solution is needed to solve this problem. Enter __DiffMapper__!

## Overview
DiffMapper leverages `git` to handle tracking instances as they move between versions. Because the kernel is developed using git, we have a very robust record of how source files change over time. `git` tracks not only movements of code between files, and inside files, but it also tracks edits to name changes too! Because of this, as well as `git`'s efficient implementation, we can insert metadata within the source code and have `git` do the heavy lifting for us. 

The basic steps of DiffMapper are as follows:
1. Checkout the old version of the kernel
2. Use L-SAP results of the old version to find the correct locations of all instances in the kernel
3. Insert the L-SAP results as comments in the source code (referred as `metadata` throughout the document)
4. Use `git` to upgrade to the new version of the kernel - This allows the metadata to follow along with the code changes

At this point, the previous version's instances will be "mapped" and the kernel can be passed to L-SAP. 

## Usage
`DiffMapper` is available as a standalone jar, or as a maven package to import into your project. Both usages will be discussed, however they both require knowledge of the `DiffConfig` object. 
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
java -jar DiffMapper.jar -D old_tag=3.17-rc1 -D new_tag=3.18-rc1 -D types.1=spin -D types.2=mutex
```
> See the `DiffConfig` docs (need link for this!) for a listing of keys. Invalid keys will be ignored.

### API
The DiffMapper API is as follows:
#### Constructor 
Method: `DiffMapper(DiffConfig config, boolean allowPrintStatements)`
Usage:
```
DiffConfig config = DiffConfig.builder().build();
DiffMapper dm = new DiffMapper(config, true);
```
Parameters: 
| Parameter            | Description                                      |
| :------------------: | :----------------------------------------------: |
| config               | A valid `DiffConfig` object                      |
| allowPrintStatements | A boolean value to enable/disable console output |
Description:
Constructs an instance of `DiffMapper` 

#### run
Method: `int run(String inputMapFilename) throws JSONException, IOException`
Usage
```
dm.run("oldInstanceMap.json");
```
Parameters:
| Parameter        | Description                                        |
| :--------------: | :------------------------------------------------: |
| inputMapFilename | The file location of the intermediate instance map |
Description:
Takes an input JSON file of tracked instances and uses this map to insert metadata and upgrade the kernel from the old version to the new version.

## Detailed Overview
The following sections provide a detailed overview of each step. Before reading the detailed overview, it is important to note the data that is required by `DiffMapper` so it can run. The following data is stored in a `DiffConfig` and is required by `DiffMapper` to complete successfully.

- Kernel Location - The location of the kernel we want to manipulate is required by `DiffMapper`. This kernel is required to be a local clone of the Linux Kernel Repository. Note the `DiffMapper` does change the source code in this directory. It is recommended that you make a backup of the current state of the kernel before using `DiffMapper`!
- L-SAP Result Location - `DiffMapper` uses the results from L-SAP to generate and insert metadata.
- DiffMapper Workspace - In order to support running`DiffMapper` on various directories from a single location, `DiffMapper` a location where it can store the intermediate tracked instances. See below for more information on this data.
- Old Version Tag - Since `DiffMapper` uses the Linux Kernel's git history, the tag of the old version needs to be known in order to correctly check it out
- New Version Tag - Since `DiffMapper` uses the Linux Kernel's git history, the tag of the new version needs to be known in order to correctly upgrade from the old version to the new version
- Types of Locks - Currently this feature is not supported, but the `DiffConfig` option allows a consumer to specify what type of locks they want to map. This makes `DiffMapper` run faster, although it is recommended to add all types of locks to the mapping.

### Checkout the old version of the kernel
This step is relatively simple, `DiffMapper` invokes a git command to checkout the old version (i.e. `git checkout v3.17-rc1`). A clean is then performed to make sure the checkout is in the correct state. Finally, a new branch is made from this tag, so changes can be made without interfering with the original linux kernel git history.

### Use L-SAP results of the old version to find the correct locations of all instances in the kernel
Once the kernel has been prepared with the old version, We track instances from the input instance map and store them in memory to be inserted in a batch based on the file name. This map is necessary from two perspectives:
1. We don't know the order in which instances are tracked. It is easier for us for first create the metadata and store it based on the source file. Once we prepare all tracked instances, we now have a more cohesive grouping based on the filename.
2. Inserting comments requires file I/O operations that are much slower than storing data in memory. If we were to prepare a comment, then insert it directly in code, we would waste a lot of time opening a file and closing it multiple times just to insert one line. It is more efficient to open a file once, insert all metadata, then close it. 

### Insert the L-SAP results as comments in the source code
After we have metadata generated and stored based on filename, we must insert all metadata for a file in one batch. To do this, we must first sort the instances based on location. This is because when we enter metadata into the source file for one instances, we change all offsets for instances located later on in the file. If we sort instances based on their location, we can insert metadata and keep track of our induced offset so the metadata goes in the correct location. Metadata is inserted as a comment in the line above the location of the instance. This is to prevent our change from conflicting with any changes that could potentially occur. 

### Use `git` to upgrade to the new version of the kernel
Once we have inserted our metadata, we add a new commit from our branch that tracks the source code changes. We then perform the following steps to "replay" all commits from the old version to the new version:
1. Checkout the new version
2. Perform a reset to remove the potential thousands of commits and only track the file differences between the two versions.
3. Perform a commit and create an "upgraded" branch, so we have only one commit between the old and new versions.
4. Rebase the "upgraded" branch onto our "metadata" branch so the git history starts from the metadata changes and then performs the necessary merges required to upgrade to the new version. 

These steps will result in the code from the new version of the Linux Kernel, but with comments inserted nearby the previous version's locking instances. 
