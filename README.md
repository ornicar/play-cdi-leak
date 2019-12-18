# PlayFramework dev workflow classloader leaks

Something causes a classloader leak and prevents any memory from being reclaimed after a run.
This causes OOM after a few runs, depending on the application size, and makes play dev workflow unusable.

## Demo project

This is a bare-bone playframework project for demonstrating classloader leaks in dev run workflow.
No routes, no controllers, no templates, no dependency injection.
Just an application loader from which the classloader leak is triggered.

## My setup

- Linux 5.4.2
- openjdk version "13.0.1" 2019-10-15
- scala 2.13.1
- playframework 2.8.0
- sbt 1.3.5

## Reproduce the leak

```
sbt
run
```

- Load http://127.0.0.1:9000 (it's a 404, there's no routes).
- Press <enter> to stop the run.
- Observe that classes are not unloaded, using any of these:
  - `jmap -clstats $(jps | grep sbt-launch.jar | cut -f1 -d' ') | head -n -1 | egrep MyComponents`
  - `jmap -clstats $(jps | grep sbt-launch.jar | cut -f1 -d' ') | wc -l`
  - `visualvm`
  - any tool showing loaded/unloaded classes

Further `run`s will show that more and more classes get loaded, and none unloaded.
