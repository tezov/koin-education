# Dependency Injection with Koin — Using qualifiers (Part 3)

This repository contains the source code for **Part 3** of an educational series about dependency injection.

In this part, we extend the previous example to show how **Koin handles multiple implementations of the same interface** using **qualifiers**.  
This allows the same `Software` class to work with different platform-specific variants, like Android and iOS.

## What this part covers

- Defining platform-specific `Program` variants (`Program.Android` and `Program.iOS`)
- Introducing interfaces for `Engine` and `Simulator`
- Creating platform-specific implementations (`XcodeEngine`, `AndroidStudioEngine`, `iOSSimulator`, `AndroidSimulator`)
- Using **Koin qualifiers** to bind a specific implementation to an interface
- Retrieving platform-specific `Software` instances with `koinInject` and the appropriate qualifier
- Understanding why qualifiers are sometimes necessary and the limitations of overusing them

This setup demonstrates **binding an implementation to a definition** and prepares the ground for the next part on scopes.

## Full article

This repository only contains the source code.

For the full explanation, context, and reasoning behind each step, read the complete article on **Medium**:

➡️ **[Read the full article on Medium](https://medium.com/itnext/dependency-injection-with-koin-interfaces-and-qualifiers-7a7c078d9689)**

