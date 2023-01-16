# Version Control System
Version Control System project created as part of the Kotlin Basics course on JetBrains Academy

## Task:
### Description

In the last stage, implement the `checkout` command. It allows a user to switch between commits and restore the contents of the files according to the current commit.

Get the files you need from the commit directory by the commit id and rewrite the current files.

```
.
├── vcs
│   ├── commits
│   │   ├── 2853da19f31cfc086cd5c40915253cb28d5eb01c
│   │   │   ├── file1.txt
│   │   │   └── file2.txt
│   │   └── 0b4f05fcd3e1dcc47f58fed4bb189196f99da89a
│   │       ├── file1.txt
│   │       └── file2.txt
│   ├── config.txt
│   ├── index.txt
│   └── log.txt
├── file1.txt
├── file2.txt
└── untracked_file.txt
```

### Objectives

The `checkout` command must be passed to the program together with the commit ID to indicate which commit should be used. If a commit with the given ID exists, the contents of the tracked file should be restored in accordance with this commit.

### Examples

**Example 1:** *the* `log` *argument*
```
commit 2853da19f31cfc086cd5c40915253cb28d5eb01c
Author: John
Changed several lines of code

commit 0b4f05fcd3e1dcc47f58fed4bb189196f99da89a
Author: John
Added several lines of code
```

**Example 2:** *the* `checkout 0b4f05fcd3e1dcc47f58fed4bb189196f99da89a` *argument*
```
Switched to commit 0b4f05fcd3e1dcc47f58fed4bb189196f99da89a.
```

**Example 3:** *the* `checkout fb92cc1be7f60c8d9acf74cbd4a67841d8d2e844` *argument*
```
Commit does not exist.
```

**Example 4:** *the* `checkout` *argument*
```
Commit id was not passed.
```
