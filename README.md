# Junit Helpers

## Requirements

| Dependency | Version | Description |
| ---------- | ------- | ----------- |
| NodeJS | 0.10+ | |
| grunt.cli | 0.1+ | This is a command line wrapper around grunt itself. This allows you to have a dedicated version of Grunt for each project you manage and only one command line entry point for that command tool. |

## How to setup your environment

You need to be sure that you have already installed `maven-settings-bootstrap` module. Take a look to that [page][maven-bootstrap-project] to do the setup.

Before running the `msb` command, be sure that your `~/.m2/msb.yml` file contains the following variables:

```yml
vars:
	nexus_aws_developer_password: <nexus aws developer password. No password? Ask DevOps to get it>
```

Then, you have to run the commands below:

```bash
$> cd <projectFolder>
$> msb
```

In fact, to run `Maven` commands correctly on those projects, you need to be sure that the correct `settings.xml` is used. By default, `Maven` is looking for a `settings.xml` into `.m2` directory present in your `home_dir`. Then, to override this behavior, `Maven` command can take the argument `-s<pathToTheSettingsXmlFile>`. Then, if you are using different tool or command line to run the `Maven` goals, you need to ensure that the correct `settings.xml` is used (the one present in each project).

## Ready to compile something

Follow the next steps:

1. Open the [FD Junit Helpers][project-repo] in Netbeans
2. You can also open the sub-modules
3. Right click on the project
4. Go to Custom menu
5. Compile

The other goals are used by the `DevOps` team to be able to release the library on `Nexus`.

## Contribute

If you want to propose improvements to that library, follow the [instructions on confluence][confluence]

[maven-bootstrap-project]: https://github.com/lotaris/maven-settings-bootstrap
[project-repo]: http://stash.aws.onlotaris.com/projects/LIB/repos/fd-jee-rest/browse
[confluence]: https://lotaris.atlassian.net/wiki/display/FDW/How+to+Work+with+Libraries
