# Image to OSC
Reduce images to average RGB values and send over OSC

Drag images from a web browser or file on your desktop
Pixels from the source image are split into a grid averaged
Click the send button to send an OSC message containing
* 3 float values per grid cell
* Ordering: red, green, blue
* Range: float values between 0 and 1

Intended for use with Wekinator: http://www.wekinator.org/
See: http://opensoundcontrol.org/introduction-osc

## Getting Started

Recommend using a java version manager such as [jabba](https://github.com/shyiko/jabba) to install an appropriate java version

Developed with [IntelliJ](https://www.jetbrains.com/idea/) against java vm 1.13.0

```bash
$  jabba install openjdk@1.1.3.0
```

This project uses the [Gradle build system](https://gradle.org/gradle) and is self bootstrapping
Once an appropriate jvm is on the path this project can be run with

```bash
$  ./gradlew run
```

Note that Slf4j is in use and currently backed by log4j 1.12 since that is what currently works with the latest version of javaosc libraries at the time of this writing

### Prerequisites

Java 1.13

## Built With

*  [Gradle](https://gradle.org/gradle)
*  [IntelliJ](https://www.jetbrains.com/idea/)
*  [JDK 13](https://openjdk.java.net/projects/jdk/13/)

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Alexander Barnes** - *Initial work* - [ToastedBits](http://toastedbits.com/)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* [Wekinator \| Software for real-time, interactive machine learning](http://www.wekinator.org/) by Rebecca Fiebrink
* [Machine Learning for Musicians and Artists - an Online Machine Art Course at Kadenze](https://www.kadenze.com/courses/machine-learning-for-musicians-and-artists-v)