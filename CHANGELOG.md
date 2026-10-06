<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# AWS SDK Client Reuse Companion Changelog

## [Unreleased]

### Changed

- The rating prompt's local counter keeps one-way fingerprints of findings
  instead of their file paths, and deletes the list that earlier versions
  kept.
- `PRIVACY.md` describes the values the plugin keeps in the IDE's local
  settings.

## [0.1.1]

### Fixed

- Review/star CTA now links to this plugin's own Marketplace
  reviews page instead of the vendor's generic plugin list.

## [0.1.0]

### Added

- Gutter warning icon on an AWS SDK for Java 2.x service client built
  via `.builder().build()` inside a regular method body instead of
  once and reused.
- 100% static text/PSI analysis, Java and Kotlin, no network calls, no
  telemetry. Free.

[Unreleased]: https://github.com/GapHunterLabs/aws-sdk-client-reuse-companion/compare/0.1.1...HEAD
[0.1.1]: https://github.com/GapHunterLabs/aws-sdk-client-reuse-companion/compare/0.1.0...0.1.1
[0.1.0]: https://github.com/GapHunterLabs/aws-sdk-client-reuse-companion/commits/0.1.0
