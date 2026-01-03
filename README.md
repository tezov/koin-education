# Dependency Injection with Koin — Basic setup (Part 2)

This repository contains the source code for **Part 2** of an educational series about dependency injection.

In this part, we build on the first one by introducing **Koin**, a Kotlin Multiplatform–compatible dependency injection framework.  
The goal is to remove manual wiring while keeping the same clean design, letting Koin handle object creation automatically.

## What this part covers

- How to add Koin to a Kotlin Multiplatform project
- The basic concepts: `single` and `factory`
- Wiring dependencies with Koin modules
- Using `koinInject` to retrieve instances
- How Koin restores the “lazy creation” pattern without manual instantiation

The example reuses the `Software / Engine / Simulator` model from Part 1, but now the dependencies are declared in a Koin module and injected automatically.

## Full article

This repository only contains the source code.

For the full explanation, context, and reasoning behind each step, read the complete article on **Medium**:

➡️ **[Read the full article on Medium](https://medium.com/itnext/dependency-injection-with-koin-the-minimal-setup-kmp-d696ae2db09f)**
