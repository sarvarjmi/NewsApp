# NewsApp

# 28A_Detekt_Setup.md

> **Phase:** Engineering Excellence
>
> **Module:** Detekt Static Analysis Configuration
>
> **Goal:** Configure Detekt to enforce code quality rules, detect complexity issues, and maintain clean code standards across the NewsApp project.

---

# Objective

Detekt is a static analysis tool for Kotlin that identifies code smells, performance issues, and violations of code quality standards. This document establishes Detekt configuration for the NewsApp project.

---

# Detekt Overview

Detekt detects:

- Code complexity issues
- Naming violations
- Style violations
- Performance anti-patterns
- Potential bugs
- Unused code

---

# Installation & Configuration

## Step 1: Add Detekt to build.gradle.kts (Project Level)

```kotlin
plugins {
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config = files("$rootDir/config/detekt.yml")
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.5")
}
```

## Step 2: Create detekt.yml Configuration

Create `config/detekt.yml` in the project root:

```yaml
# Detekt Configuration for NewsApp

build:
  maxIssues: 0
  excludeCorrectable: false
  weights:
    complexity: 2
    LongParameterList: 1
    LongMethod: 1
    LargeClass: 1
    CyclomaticComplexity: 2
    TooManyFunctions: 1
    NestedBlockDepth: 1

processors:
  active: true
  exclude:
    - 'DetektProgressListener'
  filters:
    - 'KotlinScriptFilter'
    - 'PathFilter'
    - 'RelativePathFilter'

output-reports:
  active: true
  exclude:
    - 'ProjectStatisticsReport'
    - 'ComplexityReport'
    - 'NotificationReport'
    - 'FindingsReport'
    - 'FileBasedFindingsReport'

comments:
  active: true
  excludes: ['**/test/**', '**/androidTest/**', '**/*.Test.kt', '**/*.Spec.kt', '**/java/**']
  AbsentOrWrongFile:
    active: false
  CommentOverPrivateFunction:
    active: false
  CommentOverPrivateProperty:
    active: false
  DeprecatedBlockTag:
    active: false
  EndOfLineComment:
    active: false
  OutdatedDocumentation:
    active: false
  UndocumentedPublicClass:
    active: true
    searchInNestedClass: true
    searchInInnerClass: true
    searchInInnerObject: true
    searchInInnerInterface: true
  UndocumentedPublicFunction:
    active: true
  UndocumentedPublicProperty:
    active: true

complexity:
  active: true
  ComplexCondition:
    active: true
    threshold: 4
  ComplexInterface:
    active: true
    threshold: 10
    includeStaticDeclarations: false
    includePrivateDeclarations: false
  CyclomaticComplexity:
    active: true
    threshold: 15
    ignoreSingleWhenExpression: false
    ignoreSimpleWhenEntries: false
    ignoreNestingFunctions: false
    nestingFunctions:
      - 'kotlin.apply'
      - 'kotlin.run'
      - 'kotlin.with'
      - 'kotlin.let'
      - 'kotlin.also'
  LabeledExpression:
    active: false
    ignoredLabels: []
  LargeClass:
    active: true
    threshold: 600
  LongMethod:
    active: true
    threshold: 60
  LongParameterList:
    active: true
    functionThreshold: 6
    constructorThreshold: 7
    ignoreDefaultParameters: false
    ignoreDataClasses: true
    ignoreAnnotatedParameter:
      - 'Composable'
  MethodOverloading:
    active: false
    threshold: 6
  NamedArguments:
    active: false
    threshold: 3
  NestedBlockDepth:
    active: true
    threshold: 4
  ReplaceSafeCallChainWithRun:
    active: false
  StringLiteralDuplication:
    active: true
    threshold: 3
    ignoredPatterns:
      - 'R\.drawable\.\w+'
      - 'R\.string\.\w+'
      - 'R\.color\.\w+'
  TooManyFunctions:
    active: true
    thresholdInClasses: 11
    thresholdInInterfaces: 11
    thresholdInObjects: 11
    thresholdInEnums: 11
    ignoreDeprecated: true
    ignorePrivate: false
    ignoreOverridden: false

coroutines:
  active: true
  GlobalCoroutineUsage:
    active: true
  RedundantSuspendModifier:
    active: true
  SuspendFunSwallowsCancellation:
    active: false
  SuspendFunWithFlowReturnType:
    active: true

empty-blocks:
  active: true
  EmptyCatchBlock:
    active: true
    allowedExceptionNameRegex: "_|(ignore|expected)"
  EmptyClassBlock:
    active: true
  EmptyDefaultConstructor:
    active: true
  EmptyDoWhileBlock:
    active: true
  EmptyElseBlock:
    active: true
  EmptyFinallyBlock:
    active: true
  EmptyForBlock:
    active: true
  EmptyFunctionBlock:
    active: true
    ignoreOverridden: false
  EmptyIfBlock:
    active: true
  EmptyInitBlock:
    active: true
  EmptyKtFile:
    active: true
  EmptySecondaryConstructor:
    active: true
  EmptyTryBlock:
    active: true
  EmptyWhenBlock:
    active: true

exceptions:
  active: true
  ExceptionRaisedInUnexpectedLocation:
    active: false
    methodNames:
      - 'toString'
      - 'hashCode'
      - 'equals'
      - 'finalize'
  InstanceOfCheckForException:
    active: false
  NotImplementedDeclaration:
    active: false
  ObjectExtendsThrowable:
    active: false
  PrintStackTrace:
    active: true
  RethrowCaughtException:
    active: true
  ReturnFromFinally:
    active: true
  SwallowedException:
    active: true
    ignoredExceptionTypes:
      - 'InterruptedException'
      - 'NumberFormatException'
      - 'ParseException'
      - 'MalformedURLException'
  ThrowingExceptionFromFinally:
    active: true
  ThrowingExceptionInMain:
    active: false
  ThrowingNewInstanceOfSameException:
    active: true
  TooGenericExceptionCaught:
    active: true
    exceptionNames:
      - 'ArrayIndexOutOfBoundsException'
      - 'Error'
      - 'Exception'
      - 'IllegalMonitorStateException'
      - 'NullPointerException'
      - 'RuntimeException'
      - 'Throwable'
    allowedExceptionNameRegex: "_|(ignore|expected)"
  TooGenericExceptionThrown:
    active: true
    exceptionNames:
      - 'Error'
      - 'Exception'
      - 'Throwable'
      - 'RuntimeException'

formatting:
  active: true
  autoCorrect: true
  AnnotationOnSeparateLine:
    active: false
  AnnotationSpacing:
    active: true
  ArgumentListWrapping:
    active: true
    indentSize: 4
  ArrayContentAlignment:
    active: false
  ChainMethodContinuation:
    active: false
  CommentSpacing:
    active: true
  EnumEntryNameCase:
    active: false
  EnumWrapping:
    active: false
  FilenameNamingConvention:
    active: true
    classnamePattern: '[A-Z][a-zA-Z0-9]*'
    enumClassName: '[A-Z][a-zA-Z0-9]*'
    objectNamePattern: '[A-Z][a-zA-Z0-9]*'
    packageNamePattern: '[a-z]+(\.[a-z][a-z0-9]*)*'
  FinalNewline:
    active: true
    insertFinalNewline: true
  FunctionName:
    active: true
    functionPattern: '[a-z][a-zA-Z0-9]*'
    excludeClassPattern: '$^'
  FunctionReturnTypeSpacing:
    active: true
  FunctionSignature:
    active: false
  FunctionStartOfBodySpacing:
    active: true
  ImportOrdering:
    active: true
  Indentation:
    active: true
    indentSize: 4
    continuationIndentSize: 4
  KdocWrapping:
    active: false
  MaximumLineLength:
    active: true
    maxLineLength: 120
    excludePackageStatements: false
    excludeImportStatements: false
    excludeCommentStatements: false
    ignoreBacktickedIdentifier: false
  ModifierListSpacing:
    active: true
  ModifierOrdering:
    active: true
  MultilineExpressionWrapping:
    active: false
  MultilineIfElse:
    active: true
  MultilineLoopSpacing:
    active: false
  NoBlankLineBeforeRbrace:
    active: true
  NoConsecutiveBlankLines:
    active: true
  NoEmptyClassBody:
    active: true
  NoEmptyFirstLineInClassBody:
    active: true
  NoEmptyFirstLineInMethodBody:
    active: true
  NoLineBreakAfterElse:
    active: true
  NoLineBreakBeforeAssignment:
    active: true
  NoMultipleSpaces:
    active: true
  NoSemicolons:
    active: true
  NoTrailingCommas:
    active: true
  NoTrailingSpaces:
    active: true
  NoUnitReturn:
    active: true
  NoUnusedImports:
    active: true
  NoWildcardImports:
    active: true
    packagesToUseImportOnDemandPattern: 'java.util.*'
  NullableTypeSpacing:
    active: true
  PackageName:
    active: true
    packagePattern: '[a-z]+(\.[a-z][a-zA-Z0-9]*)*'
  ParameterListSpacing:
    active: true
  ParameterListWrapping:
    active: true
  ParameterWrapping:
    active: false
  PropertyName:
    active: true
    propertyPattern: '[a-z][a-zA-Z0-9]*'
  PropertyWrapping:
    active: false
  RangeSpacing:
    active: true
  SemicolonSpacing:
    active: true
  SpacingAroundColon:
    active: true
  SpacingAroundComma:
    active: true
  SpacingAroundCurly:
    active: true
  SpacingAroundKeyword:
    active: true
  SpacingAroundOperators:
    active: true
  SpacingAroundParens:
    active: true
  SpacingAroundRangeOperator:
    active: true
  SpacingBetweenDeclarationsWithAnnotationsOrDocComments:
    active: false
  SpacingBetweenDeclarationsWithMultiLineAnnotations:
    active: true
  StringQuoteDelimiter:
    active: true
  StringTemplateDelimiter:
    active: true
  TrailingCommaOnArgumentList:
    active: false
  TrailingCommaOnCallSite:
    active: false
  TrailingCommaOnCollectionLiteral:
    active: false
  TrailingCommaOnFunctionParameters:
    active: false
  TypeArgumentListSpacing:
    active: true
  TypeParameterListSpacing:
    active: true
  UnnecessaryParenthesesInFunctionCallWithLambda:
    active: false
  UnnecessaryParenthesesInWhenWithoutElse:
    active: false
  Wrapping:
    active: false

naming:
  active: true
  BooleanPropertyNaming:
    active: true
    allowedPrefixes:
      - 'is'
      - 'has'
      - 'are'
  ClassNaming:
    active: true
    classPattern: '[A-Z][a-zA-Z0-9]*'
  ConstructorParameterNaming:
    active: true
    parameterPattern: '[a-z][a-zA-Z0-9]*'
    privateParameterPattern: '[a-z][a-zA-Z0-9]*'
    excludeClassPattern: '$^'
    ignoreOverridden: true
  DestructuringDeclarationName:
    active: false
    variablePattern: '[a-z][a-zA-Z0-9]*'
  EnumNaming:
    active: true
    enumEntryPattern: '[A-Z][A-Z0-9_]*'
  ForbiddenClassName:
    active: false
    forbiddenName:
      - 'Test'
  FunctionMaxLength:
    active: false
    maximumFunctionNameLength: 30
  FunctionMinLength:
    active: false
    minimumFunctionNameLength: 3
  FunctionNaming:
    active: true
    functionPattern: '[a-z][a-zA-Z0-9]*'
    excludeClassPattern: '$^'
    ignoreOverridden: true
  FunctionParameterNaming:
    active: true
    parameterPattern: '[a-z][a-zA-Z0-9]*'
    excludeClassPattern: '$^'
    ignoreOverridden: true
  InvalidPackageDeclaration:
    active: true
    rootPackage: ''
  LambdaParameterNaming:
    active: true
    parameterPattern: '[a-z][a-zA-Z0-9]*|_'
  MatchingDeclarationName:
    active: true
    mustBeFirst: true
  MemberNameEqualsClassName:
    active: true
    ignoreOverridden: true
  NoNameShadowing:
    active: true
  ObjectPropertyNaming:
    active: true
    constantPattern: '[A-Z][A-Z0-9_]*'
    propertyPattern: '[a-z][a-zA-Z0-9]*'
    privatePropertyPattern: '[a-z][a-zA-Z0-9]*'
  PackageNaming:
    active: true
    packagePattern: '[a-z]+(\.[a-z][a-zA-Z0-9]*)*'
  TopLevelPropertyNaming:
    active: true
    constantPattern: '[A-Z][A-Z0-9_]*'
    propertyPattern: '[a-z][a-zA-Z0-9]*'
    privatePropertyPattern: '[a-z][a-zA-Z0-9]*'
  VariableMaxLength:
    active: false
    maximumVariableNameLength: 64
  VariableMinLength:
    active: false
    minimumVariableNameLength: 1
  VariableNaming:
    active: true
    variablePattern: '[a-z][a-zA-Z0-9]*'
    privateVariablePattern: '[a-z][a-zA-Z0-9]*'
    excludeClassPattern: '$^'
    ignoreOverridden: true

performance:
  active: true
  ArrayPrimitive:
    active: true
  CouldBeSequence:
    active: true
    threshold: 2
  ForEachOnRange:
    active: true
    excludes: ['**/test/**', '**/androidTest/**']
  Lambda:
    active: false
  LambdaWithNonItVariable:
    active: true
  UnnecessaryTemporaryInstantiation:
    active: true
  UnnecessaryPartOfBinaryExpression:
    active: true

potential-bugs:
  active: true
  AvoidReferringToObjectProperty:
    active: true
    excludes: ['**/test/**', '**/androidTest/**']
  CastToNullableType:
    active: true
  CatchingExceptionByGenericName:
    active: true
    exceptions:
      - 'Exception'
      - 'RuntimeException'
      - 'Throwable'
  DontDowncastCollectionCast:
    active: false
  DoubleMutabilityForCollection:
    active: true
    mutableTypes:
      - 'kotlin.collections.MutableList'
      - 'kotlin.collections.MutableMap'
      - 'kotlin.collections.MutableSet'
      - 'java.util.ArrayList'
      - 'java.util.LinkedHashSet'
      - 'java.util.HashSet'
      - 'java.util.LinkedHashMap'
      - 'java.util.HashMap'
  DuplicateCaseInWhenExpression:
    active: true
  EqualsAlwaysReturnsTrueOrFalse:
    active: true
  EqualsWithHashCodeExist:
    active: true
  ExitOutsideMain:
    active: false
  ExplicitGarbageCollectionCall:
    active: true
  HasPlatformType:
    active: true
  IgnoredReturnValue:
    active: true
    restrictToConfig: true
    returnValueAnnotations:
      - 'CheckResult'
      - 'CheckReturnValue'
  ImplicitUnitReturnType:
    active: false
    allowExplicitReturnType: true
  InvalidRange:
    active: true
  IteratorImpl:
    active: false
  IteratorNotThrowingNoSuchElementException:
    active: false
  LateinitUsage:
    active: true
    excludes: ['**/test/**', '**/androidTest/**']
    ignoreOnClassesPattern: ''
  MapGetWithNotNullAssertionOperator:
    active: true
  MissingWhenCase:
    active: false
    allowElseExpression: true
  NullCheckOnMutableProperty:
    active: false
  NullableToStringCall:
    active: true
  PropertyUsedBeforeDeclaration:
    active: true
  UnconditionalJumpStatementInLoop:
    active: true
  UnnecessaryNotNullOperator:
    active: true
  UnnecessarySafeCall:
    active: true
  UnreachableCode:
    active: true
  UnsafeCallOnNullableType:
    active: true
  UnsafeCast:
    active: true
  UnusedUnderscoreExpression:
    active: false
  UselessPostfixExpression:
    active: true

style:
  active: true
  CollapsibleIfStatements:
    active: true
  DataClassContainsFunctions:
    active: true
    conversionFunctionPrefix:
      - 'to'
  DataClassShouldBeImmutable:
    active: true
  DestructuringAssignmentWithTooManyEntries:
    active: true
    maxDestructuringEntries: 3
  EqualsNullCall:
    active: true
  EqualsOnSignatureLine:
    active: false
  ExplicitCollectionElementAccessMethod:
    active: false
  ExplicitItLambdaParameter:
    active: true
  ExpressionBodySyntax:
    active: false
  ForbiddenComment:
    active: true
    values:
      - 'TODO:'
      - 'FIXME:'
      - 'STOPSHIP:'
      - 'HACK:'
    allowedPatterns: ''
  ForbiddenImport:
    active: true
    imports:
      - 'kotlin.io.println'
      - 'kotlin.io.print'
  ForbiddenMethod:
    active: true
    methods:
      - 'kotlin.io.print'
      - 'kotlin.io.println'
      - 'kotlin.system.exitProcess'
  ForbiddenSuppress:
    active: false
  ForbiddenVoid:
    active: true
    ignoreOverridden: false
    ignoreUsageInGenerics: false
  FunctionOnlyReturningConstant:
    active: true
    ignoreOverridableFunction: true
    excludeAnnotatedFunction:
      - 'dagger.Provides'
      - 'javax.inject.Provides'
  LibraryCodeMustSpecifyReturnType:
    active: true
  LoopWithTooManyJumpStatements:
    active: true
    maxJumpCount: 2
  MagicNumber:
    active: true
    ignoreNumbers:
      - '-1'
      - '0'
      - '1'
      - '2'
      - '360'
      - '180'
      - '90'
      - '45'
    ignoreHashCodeFunction: true
    ignorePropertyDeclaration: false
    ignoreLocalVariableDeclaration: false
    ignoreConstantDeclaration: true
    ignoreAnnotation: false
    ignoreNamedArgument: true
    ignoreEnumValues: true
    ignoreRanges: false
    ignoreExtensionFunctions: true
  MandatoryBracesLoops:
    active: true
  MaxLineLength:
    active: true
    maxLineLength: 120
    excludePackageStatements: false
    excludeImportStatements: false
    excludeCommentStatements: false
    ignoreBacktickedIdentifier: false
  MayBeConst:
    active: true
  ModifierOrder:
    active: true
  MultilineLambdaItParameterRoot:
    active: false
  MultilineRawStringIndentation:
    active: false
    indentSize: 4
  NestedClassesVisibility:
    active: true
  NewLineAtEndOfFile:
    active: true
  NoTabs:
    active: true
  NullableBooleanCheck:
    active: false
  ObjectLiteralToLambda:
    active: true
  PreferToOverPairSyntax:
    active: true
  ProtectedMemberInFinalClass:
    active: true
  RedundantExplicitType:
    active: false
  RedundantHigherOrderMapUsage:
    active: true
  RedundantVisibilityModifier:
    active: true
  RedundantVisibilityModifierRule:
    active: false
  SafeCast:
    active: true
  SerialVersionUIDInSerializableClass:
    active: true
  SpreadOperator:
    active: true
  StringShouldBeRawString:
    active: false
    maxEscapedCharacterCount: 2
  TrailingWhitespace:
    active: true
  TrimMultilineRawString:
    active: false
  UnnecessaryAbstractClass:
    active: true
  UnnecessaryAnnotationUseSiteTarget:
    active: true
  UnnecessaryApply:
    active: true
  UnnecessaryBackticks:
    active: true
  UnnecessaryBracesAroundTrailingLambda:
    active: true
  UnnecessaryFilter:
    active: true
  UnnecessaryInheritance:
    active: true
  UnnecessaryInnerClass:
    active: true
  UnnecessaryLet:
    active: true
  UnnecessaryParentheses:
    active: true
  UnnecessaryWildcardImport:
    active: true
  UnusedImports:
    active: true
  UnusedParameter:
    active: true
  UnusedPrivateMember:
    active: true
    allowedNames:
      - '(_|ignore|expected|actual)'
  UnusedVariable:
    active: true
    allowedNames:
      - '(_|ignore|expected|actual)'
  UseArrayLiterals:
    active: false
  UseCheckNotNull:
    active: true
  UseCheckOrError:
    active: false
  UseDataClass:
    active: true
  UseEmptyCounterpart:
    active: true
  UseIfEmptyOrIfBlank:
    active: true
  UseIfInsteadOfWhen:
    active: false
  UseIsEven:
    active: false
  UseOrEmpty:
    active: true
  UseRequire:
    active: true
  UseRequireNotNull:
    active: true
  UseSafeTransforms:
    active: true
  VarCouldBeVal:
    active: true
  WildcardImport:
    active: true

compose:
  active: true
  ComposableEventParametersNaming:
    active: true
    eventParameterPattern: 'on[A-Z].*'
  ComposableFunctionNaming:
    active: true
    functionPattern: '[A-Z][a-zA-Z0-9]*'
  ComposableModifierMissing:
    active: true
    checkComposePreviewParameter: true
  ContentTrailingLambda:
    active: true
  ModifierComposable:
    active: true
  ModifierMissing:
    active: true
    severity: 'warning'
  ModifierReused:
    active: true
  ModifierWithoutDefault:
    active: true
  MultipleEmitters:
    active: false
  UnstableCollections:
    active: true
  UnusedMaterialScaffoldPaddingParameter:
    active: true
```

## Step 3: Add Detekt Tasks to build.gradle.kts (App Level)

```kotlin
tasks {
    detekt {
        description = "Runs Detekt analysis"
        parallel = true
        ignoreFailures = false
        buildUponDefaultConfig = true
        reports {
            html.enabled = true
            txt.enabled = false
            sarif.enabled = false
        }
    }
}
```

---

# Running Detekt

## Command Line

```bash
# Run Detekt on the app module
./gradlew detekt

# Run and generate HTML report
./gradlew detekt --info

# Run on specific module
./gradlew :app:detekt
```

## In IDE

- Android Studio → Run → Run Detekt
- Or use the Detekt plugin for real-time analysis

---

# Key Rules Explained

## Complexity Rules

| Rule | Purpose |
|------|---------|
| CyclomaticComplexity (threshold: 15) | Limits method complexity to improve readability |
| NestingDepth (threshold: 4) | Prevents deeply nested conditionals |
| LongMethod (threshold: 60 lines) | Encourages breaking large methods into smaller ones |
| LongParameterList (threshold: 6 params) | Encourages using data classes for many parameters |
| TooManyFunctions (threshold: 11) | Prevents bloated classes with too many responsibilities |

## Naming Rules

| Rule | Purpose |
|------|---------|
| FunctionNaming | Functions start with lowercase letter |
| ClassNaming | Classes start with uppercase letter |
| VariableNaming | Variables start with lowercase letter |
| EnumNaming | Enum entries are UPPERCASE_WITH_UNDERSCORES |

## Performance Rules

| Rule | Purpose |
|------|---------|
| ForEachOnRange | Use range loops efficiently |
| CouldBeSequence | Suggests using sequences for chained operations |
| UnnecessaryTemporaryInstantiation | Avoids creating unnecessary objects |

## Potential Bugs Rules

| Rule | Purpose |
|------|---------|
| UnreachableCode | Detects code that can never execute |
| UnnecessarySafeCall | Warns when safe call (?.) is not needed |
| UnsafeCast | Detects potentially unsafe type casts |

## Style Rules

| Rule | Purpose |
|------|---------|
| MagicNumber | Discourages hardcoded numbers; use constants instead |
| ForbiddenComment | Warns about TODOs, FIXMEs without deadlines |
| FunctionOnlyReturningConstant | Functions should do more than just return a constant |

---

# Best Practices

1. **Fix Critical Issues First**: Address cyclomatic complexity and long methods first.
2. **Gradual Enforcement**: Start with warnings, gradually increase strictness.
3. **Suppress Strategically**: Use `@Suppress` only for legitimate exceptions.
4. **Review Reports**: Check HTML reports for detailed analysis.
5. **Integrate with CI**: Run Detekt as a CI quality gate.

---

# Gradle Integration Example

Add to `build.gradle.kts`:

```kotlin
// Fail build on critical Detekt issues
tasks.detekt.onlyIf {
    project.hasProperty("enableDetekt")
}

// Optional: Create a strict task
tasks.register("detektStrict") {
    dependsOn("detekt")
    doLast {
        val reportFile = file("build/reports/detekt/detekt.html")
        if (reportFile.exists()) {
            println("📊 Detekt report available at: ${reportFile.absolutePath}")
        }
    }
}
```

---

# Files Created

- `config/detekt.yml` - Detekt configuration file

---

# What We Completed

✅ Added Detekt to Gradle

✅ Created comprehensive detekt.yml configuration

✅ Documented all rules and their purposes

✅ Provided setup instructions and running commands

✅ Explained performance, complexity, and code quality rules

---

# Next Steps

Run Detekt analysis and fix violations to establish baseline code quality.

```bash
./gradlew detekt
```

Review the HTML report and address violations incrementally.
