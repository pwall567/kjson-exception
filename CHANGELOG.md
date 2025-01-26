# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [1.3] - 2025-01-27
### Changed
- `pom.xml`: upgraded Kotlin version to 2.0.21
- tests: converted to `should-test` library

## [1.2] - 2024-07-08
### Changed
- `JSONException`: call the `extendMessage()` function dynamically instead of at initialisation

## [1.1] - 2024-07-03
### Changed
- `JSONException`: made the `key` `open`, so that it can be overridden with a stronger type

## [1.0] - 2024-07-02
### Added
- all files: initial versions (extracted from `kjson-core`)
