# Android Java Playground

Personal repository for practicing and learning Android development with Java and XML.

This project works as an educational monorepo where exercises, experiments, code analysis tasks, and small Android projects are organized by category.

---

# Goals

- Practice Android fundamentals
- Learn Java applied to Android development
- Work with XML layouts
- Experiment with accessible UI design
- Maintain a real learning history using Git and GitHub
- Build small projects before turning some of them into standalone repositories

---

# Repository Structure

```text
android-java-playground/
│
├── apps/
│   │
│   ├── java-code-analysis/
│   │   └── Exercises focused on reading,
│   │       understanding, and analyzing Java/Android code.
│   │
│   ├── layout-exercises/
│   │   └── XML layout practice, views,
│   │       positioning, and UI design exercises.
│   │
│   └── showcase-projects/
│       └── More complete demos or projects
│           that may eventually become standalone repositories.
│
├── .gitignore
└── README.md
```

---

# Project Organization

Each exercise or mini Android project usually contains its own independent Gradle structure:

```text
example-project/
│
├── app/
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

This makes it possible to open each project individually in Android Studio.

---

# Technologies Used

- Java
- XML
- Android SDK
- Android Studio
- Gradle (Kotlin DSL)
- Git / GitHub

---

# Repository Conventions

## Ignored Files

The repository automatically ignores:

- `.idea/`
- `build/`
- `.gradle/`
- generated APKs
- local caches
- temporary IDE files

to avoid uploading automatically generated files.

---

# Opening a Project

From Android Studio:

```text
Open
→ select the folder of the specific exercise/project
```

Example:

```text
apps/layout-exercises/exercise1
```

or:

```text
apps/showcase-projects/1-5-1-ui-demo-accessible
```

---

# Repository Status

Work in progress and continuously evolving.

The structure, conventions, and organization may change as the learning process advances and projects become more complex.

---

# License

Personal and educational use.
