# Dependency Injection with Koin — Part 4: Scopes

This repository contains the source code for **Part 4** of an educational series about **Dependency Injection with Kotlin and Koin**.

This part focuses on **Koin scopes**: how they work, how they differ from qualifiers, and how they help structure dependency lifecycles in a contextual and explicit way.

---

## 🎯 What is covered in this part

- Why qualifiers don’t scale well for contextual lifecycles
- What a Koin scope really is
- How scoped definitions differ from global singletons
- Passing parameters at resolution time
- Using scopes to select implementations without qualifiers
- Explicit scope creation and destruction
- How dependency resolution behaves inside a scope

## Full article

This repository only contains the source code.

For the full explanation, context, and reasoning behind each step, read the complete article on **Medium**:

➡️ **[Read the full article on Medium](https://medium.com/itnext/dependency-injection-with-koin-scopes-4b45bbcd243d)**


## Update

**Check to this article to understand Linked Scope** In the next release, 4.2.0, the initial example won't compile any more. A stricter control
of scope resolution make impossible for root parent to resolve its own needs with children known scope.

But it is still possible with an extra scope and linked scope. 