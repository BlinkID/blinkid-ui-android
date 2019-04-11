# BlinkID UI for Android
BlinkID UI is a library that lets you scan any BlinkID supported document without even knowing what a `Recognizer` is.

It includes customisable scan activity and country selection activity.

To see BlinkID UI in action, check our [demo app](https://play.google.com/store/apps/details?id=com.microblink.blinkidapp).

New to _BlinkID_? Check [BlinkID SDK](https://github.com/BlinkID/blinkid-android) first.

## <a name="quickStart"></a> Quick Start
_BlinkID UI_ is a Kotlin library so first make sure you have enabled Kotlin support in your project

* in your top level `build.gradle` add buildscript dependency `classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.51"`

This library depends on _BlinkID SDK_ so you'll need to add _BlinkID_ maven repository to repositories list

* `maven { url 'http://maven.microblink.com' }`

Now you can add _BlinkID UI_ as a new module to your project

* in Android Studio menu, click File - New - New Module - Import Gradle Project
* select blinkid-ui-lib folder and click Finish
* in your `settings.gradle` add `include 'blinkid-ui-lib'`
* in your app's `build.gradle` add new module dependency `implementation project(':blinkid-ui-lib')`

After doing a Gradle sync, you'll be able to start using _BlinkID UI_ in your app

* first make sure that your Microblink licence is correctly configured, check [BlinkID documentation](https://github.com/BlinkID/blinkid-android/blob/master/README.md#quickScan) for more info
* extend `BaseDocumentScanActivity` and implement required methods
	* `getInitialDocument()` - return the document that will be selected before the user has the chance to change it
	* `shouldScanBothDocumentSides` - most documents support scanning both front and back side, if you just want to scan one side, return `false`
	* `createScanFlowListener()` - this listener gets notified on scan flow updates, most importantly, you'll get final scan results in `onEntireDocumentScanned` method

If you want to customise scanning behavior and UI take a look at [FAQ](#faq).

## <a name="proguard"></a>Proguard config
If you're using Proguard, you'll need to add the following line to your Proguard rules
`-keep class android.support.v7.widget.SearchView { *; }`

## <a name="faq"></a> FAQ
### How do I customise colors, icons and other resources?
To use your custom theme in scan activity, extend `MbScanTheme` and override attribues you want to change.

To use custom theme in country selection activity, extend either `MbChooseCountryLightTheme` or `MbChooseCountryDarkTheme` and override `createDocumentChooser()` in your scan activity to return your theme.

For other resources, name your resource the same as the resource you want to override in the library. For example, if you want to change torch icon, name you icon `mb_ic_torch_on`.

Check the sample app for examples of customisation.

### How do I disable translations?
To disable translations for country names, override `createLocalizationManager()` and return your custom `LocaleManager` that limits languages to which to translate to. 

If you want to disable translations for other strings, change your gradle configuration as explained [here](https://developer.android.com/studio/build/shrink-code#unused-alt-resources).

### How do I disable scan sound or use a custom one?
In your scan activity, override `createScanSuccessSoundPlayer()` and return your own implementation. If you want to disable sound just return `new EmptyScanSuccessPlayer()`. 

### How do I show a splash screen?
In your scan activity, override `createSplashOverlaySettings()` and return your own implementation. 

### How do I change scan timeout behavior?
In your scan activity, override `createScanTimeoutHandler()` and return your own implementation. If you just want to change timeout duration you can use `DefaultScanTimeoutHandler`.

### How do I disable or change scan line animation?
In your scan activity, override `createScanLineAnimator()` and return your own implementation. If you want to disable the animation, just return `new EmptyScanLineAnimator()`. If you just want to change line color, override `@color/mbIconScanLine`.

### How do I limit selection to specific countries and document types?
In your scan activity, override `createDocumentChooser()` and return your own subclass of `DefaultDocumentChooser`, in which you'll override `getCountryFilter()` to return your desired filter and override `isDocumentTypeSupportedForCountry()` where you'll need to return whether a document type is supported for a specific country.

### How do I use custom country selection activity?
In your scan activity, override `createDocumentChooser()` and return your own implementation. Start your country selection activity in `onChooseCountryClick()`.

### How can I get success frames?
In your scan activity, override `getFrameGrabberMode()` and return `FrameGrabberMode.SUCCESS_FRAMES`. Then override `createFrameListener()` and return listener that will handle success frames.
